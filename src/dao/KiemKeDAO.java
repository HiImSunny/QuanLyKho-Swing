package dao;

import database.DatabaseConnection;
import model.KiemKe;
import model.ChiTietKiemKe;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KiemKeDAO {

    // INSERT phiếu kiểm kê + chi tiết (dùng Transaction)
    public boolean insert(KiemKe kiemKe, List<ChiTietKiemKe> chiTietList) {
        Connection conn = null;
        PreparedStatement pstKiemKe = null;
        PreparedStatement pstChiTiet = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getNewConnection();
            conn.setAutoCommit(false); // Bắt đầu Transaction

            // 1. Insert vào bảng kiem_ke
            String sqlKiemKe = "INSERT INTO kiem_ke (so_phieu, ngay_kiem_ke, ma_kho, nguoi_kiem_ke, trang_thai, ghi_chu) VALUES (?, ?, ?, ?, ?, ?)";
            pstKiemKe = conn.prepareStatement(sqlKiemKe, Statement.RETURN_GENERATED_KEYS);
            pstKiemKe.setString(1, kiemKe.getSo_phieu());
            pstKiemKe.setDate(2, new java.sql.Date(kiemKe.getNgay_kiem_ke().getTime()));

            if (kiemKe.getMa_kho() != 0) {
                pstKiemKe.setInt(3, kiemKe.getMa_kho());
            } else {
                pstKiemKe.setNull(3, Types.INTEGER);
            }

            if (kiemKe.getNguoi_kiem_ke() != 0) {
                pstKiemKe.setInt(4, kiemKe.getNguoi_kiem_ke());
            } else {
                pstKiemKe.setNull(4, Types.INTEGER);
            }

            pstKiemKe.setString(5, kiemKe.getTrang_thai());
            pstKiemKe.setString(6, kiemKe.getGhi_chu());

            pstKiemKe.executeUpdate();

            // Lấy ma_kiem_ke vừa insert
            int maKiemKe = 0;
            rs = pstKiemKe.getGeneratedKeys();
            if (rs.next()) {
                maKiemKe = rs.getInt(1);
            }

            // 2. Insert vào bảng chi_tiet_kiem_ke
            String sqlChiTiet = "INSERT INTO chi_tiet_kiem_ke (ma_kiem_ke, ma_sp, ton_he_thong, ton_thuc_te, ghi_chu) VALUES (?, ?, ?, ?, ?)";
            pstChiTiet = conn.prepareStatement(sqlChiTiet);

            for (ChiTietKiemKe ct : chiTietList) {
                pstChiTiet.setInt(1, maKiemKe);
                pstChiTiet.setInt(2, ct.getMa_sp());
                pstChiTiet.setInt(3, ct.getTon_he_thong());
                pstChiTiet.setInt(4, ct.getTon_thuc_te());
                pstChiTiet.setString(5, ct.getGhi_chu());
                pstChiTiet.addBatch();
            }

            pstChiTiet.executeBatch();

            conn.commit(); // Commit transaction
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback nếu có lỗi
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (pstKiemKe != null)
                    pstKiemKe.close();
                if (pstChiTiet != null)
                    pstChiTiet.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // GET ALL phiếu kiểm kê (JOIN với kho và users)
    public List<KiemKe> getAll() {
        List<KiemKe> list = new ArrayList<>();
        String sql = "SELECT kk.*, k.ten_kho, u.ho_ten as ten_nguoi_kiem_ke " +
                "FROM kiem_ke kk " +
                "LEFT JOIN kho k ON kk.ma_kho = k.id " +
                "LEFT JOIN users u ON kk.nguoi_kiem_ke = u.id " +
                "ORDER BY kk.ngay_kiem_ke DESC, kk.ma_kiem_ke DESC";

        try (Connection conn = DatabaseConnection.getNewConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                KiemKe kk = new KiemKe(
                        rs.getInt("ma_kiem_ke"),
                        rs.getString("so_phieu"),
                        rs.getDate("ngay_kiem_ke"),
                        rs.getInt("ma_kho"),
                        rs.getString("ten_kho"),
                        rs.getInt("nguoi_kiem_ke"),
                        rs.getString("ten_nguoi_kiem_ke"),
                        rs.getString("trang_thai"),
                        rs.getString("ghi_chu"),
                        rs.getTimestamp("ngay_tao"));
                list.add(kk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // GET CHI TIẾT phiếu kiểm kê (JOIN với san_pham)
    public List<ChiTietKiemKe> getChiTiet(int maKiemKe) {
        List<ChiTietKiemKe> list = new ArrayList<>();
        String sql = "SELECT ct.*, sp.ten_sp, sp.don_vi_tinh " +
                "FROM chi_tiet_kiem_ke ct " +
                "JOIN san_pham sp ON ct.ma_sp = sp.ma_sp " +
                "WHERE ct.ma_kiem_ke = ?";

        try (Connection conn = DatabaseConnection.getNewConnection();
                PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, maKiemKe);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                ChiTietKiemKe ct = new ChiTietKiemKe(
                        rs.getInt("ma_kiem_ke"),
                        rs.getInt("ma_sp"),
                        rs.getString("ten_sp"),
                        rs.getString("don_vi_tinh"),
                        rs.getInt("ton_he_thong"),
                        rs.getInt("ton_thuc_te"),
                        rs.getInt("chenh_lech"),
                        rs.getString("ghi_chu"));
                list.add(ct);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // GET TỒN KHO HIỆN TẠI theo kho (từ bảng ton_kho)
    public List<ChiTietKiemKe> getTonKhoTheoKho(int maKho) {
        List<ChiTietKiemKe> list = new ArrayList<>();
        String sql = "SELECT tk.ma_sp, sp.ten_sp, sp.don_vi_tinh, tk.so_luong_ton " +
                "FROM ton_kho tk " +
                "JOIN san_pham sp ON tk.ma_sp = sp.ma_sp " +
                "WHERE tk.ma_kho = ? AND tk.so_luong_ton > 0 " +
                "ORDER BY sp.ten_sp";

        try (Connection conn = DatabaseConnection.getNewConnection();
                PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, maKho);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                // Tạo ChiTietKiemKe với tồn hệ thống, tồn thực tế = 0 (chờ nhập)
                ChiTietKiemKe ct = new ChiTietKiemKe(
                        rs.getInt("ma_sp"),
                        rs.getString("ten_sp"),
                        rs.getString("don_vi_tinh"),
                        rs.getInt("so_luong_ton"), // Tồn hệ thống
                        0, // Tồn thực tế - chờ user nhập
                        null // Ghi chú
                );
                list.add(ct);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // GENERATE số phiếu kiểm kê tự động
    public String generateSoPhieu() {
        String prefix = "KK";
        String sql = "SELECT MAX(CAST(SUBSTRING(so_phieu, 3) AS UNSIGNED)) as max_num FROM kiem_ke WHERE so_phieu LIKE 'KK%'";

        try (Connection conn = DatabaseConnection.getNewConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                Object maxNumObj = rs.getObject("max_num");
                if (maxNumObj != null) {
                    int maxNum = ((Number) maxNumObj).intValue();
                    return prefix + String.format("%06d", maxNum + 1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return prefix + "000001";
    }

    // HỦY phiếu kiểm kê (đánh dấu trạng thái = 'da_huy')
    public boolean cancelPhieu(int maKiemKe) {
        String sql = "UPDATE kiem_ke SET trang_thai = 'da_huy' WHERE ma_kiem_ke = ?";

        try (Connection conn = DatabaseConnection.getNewConnection();
                PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, maKiemKe);
            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
