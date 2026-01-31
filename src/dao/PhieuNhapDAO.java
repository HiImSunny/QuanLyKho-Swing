package dao;

import database.DatabaseConnection;
import model.PhieuNhap;
import model.ChiTietPhieuNhap;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PhieuNhapDAO {

    // INSERT phiếu nhập + chi tiết (dùng Transaction)
    public boolean insert(PhieuNhap phieuNhap, List<ChiTietPhieuNhap> chiTietList) {
        Connection conn = null;
        PreparedStatement pstPhieu = null;
        PreparedStatement pstChiTiet = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Bắt đầu Transaction

            // 1. Insert vào bảng phieu_nhap (đã bỏ cột nha_cung_cap, thêm ma_kho)
            String sqlPhieu = "INSERT INTO phieu_nhap (so_phieu, ngay_nhap, ma_ncc, ma_kho, nguoi_lap, tong_tien, ghi_chu) VALUES (?, ?, ?, ?, ?, ?, ?)";
            pstPhieu = conn.prepareStatement(sqlPhieu, Statement.RETURN_GENERATED_KEYS);
            pstPhieu.setString(1, phieuNhap.getSo_phieu());
            pstPhieu.setDate(2, phieuNhap.getNgay_nhap());
            if (phieuNhap.getMa_ncc() != null) {
                pstPhieu.setInt(3, phieuNhap.getMa_ncc());
            } else {
                pstPhieu.setNull(3, Types.INTEGER);
            }
            if (phieuNhap.getMa_kho() != null) {
                pstPhieu.setInt(4, phieuNhap.getMa_kho());
            } else {
                pstPhieu.setNull(4, Types.INTEGER);
            }
            if (phieuNhap.getNguoi_lap() != null) {
                pstPhieu.setInt(5, phieuNhap.getNguoi_lap());
            } else {
                pstPhieu.setNull(5, Types.INTEGER);
            }
            pstPhieu.setBigDecimal(6, phieuNhap.getTong_tien());
            pstPhieu.setString(7, phieuNhap.getGhi_chu());

            int rowsPhieu = pstPhieu.executeUpdate();

            // Lấy ma_phieu_nhap vừa insert
            int maPhieuNhap = 0;
            rs = pstPhieu.getGeneratedKeys();
            if (rs.next()) {
                maPhieuNhap = rs.getInt(1);
            }

            // 2. Insert vào bảng chi_tiet_phieu_nhap
            String sqlChiTiet = "INSERT INTO chi_tiet_phieu_nhap (ma_phieu_nhap, ma_sp, so_luong, don_gia) VALUES (?, ?, ?, ?)";
            pstChiTiet = conn.prepareStatement(sqlChiTiet);

            for (ChiTietPhieuNhap ct : chiTietList) {
                pstChiTiet.setInt(1, maPhieuNhap);
                pstChiTiet.setInt(2, ct.getMa_sp());
                pstChiTiet.setInt(3, ct.getSo_luong());
                pstChiTiet.setBigDecimal(4, ct.getDon_gia());
                pstChiTiet.addBatch();
            }

            pstChiTiet.executeBatch();

            // 3. Cập nhật tồn kho trong bảng ton_kho
            // 3. Cập nhật ton_kho (dùng ma_kho từ phieu_nhap)
            String sqlUpdateTonKho = "INSERT INTO ton_kho (ma_sp, ma_kho, so_luong_ton) " +
                    "VALUES (?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE so_luong_ton = so_luong_ton + ?";
            PreparedStatement pstTonKho = conn.prepareStatement(sqlUpdateTonKho);

            Integer maKho = phieuNhap.getMa_kho();
            if (maKho != null) {
                for (ChiTietPhieuNhap ct : chiTietList) {
                    pstTonKho.setInt(1, ct.getMa_sp());
                    pstTonKho.setInt(2, maKho);
                    pstTonKho.setInt(3, ct.getSo_luong());
                    pstTonKho.setInt(4, ct.getSo_luong());
                    pstTonKho.addBatch();
                }
                pstTonKho.executeBatch();
                pstTonKho.close();
            }

            // 4. Cập nhật số lượng tồn tổng trong bảng san_pham (optional, để tương thích
            // ngược)
            String sqlUpdateSP = "UPDATE san_pham SET so_luong_ton = so_luong_ton + ? WHERE ma_sp = ?";
            PreparedStatement pstUpdate = conn.prepareStatement(sqlUpdateSP);

            for (ChiTietPhieuNhap ct : chiTietList) {
                pstUpdate.setInt(1, ct.getSo_luong());
                pstUpdate.setInt(2, ct.getMa_sp());
                pstUpdate.addBatch();
            }

            pstUpdate.executeBatch();
            pstUpdate.close();

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
                if (pstPhieu != null)
                    pstPhieu.close();
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

    // GET ALL phiếu nhập (JOIN với nha_cung_cap để lấy tên)
    public List<PhieuNhap> getAll() {
        List<PhieuNhap> list = new ArrayList<>();
        String sql = "SELECT pn.*, ncc.ten_ncc as nha_cung_cap " +
                "FROM phieu_nhap pn " +
                "LEFT JOIN nha_cung_cap ncc ON pn.ma_ncc = ncc.ma_ncc " +
                "ORDER BY pn.ngay_nhap DESC, pn.ma_phieu_nhap DESC";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                PhieuNhap pn = new PhieuNhap(
                        rs.getInt("ma_phieu_nhap"),
                        rs.getString("so_phieu"),
                        rs.getDate("ngay_nhap"),
                        rs.getString("nha_cung_cap"), // Từ JOIN
                        rs.getObject("ma_ncc", Integer.class),
                        rs.getObject("ma_kho", Integer.class), // Thêm ma_kho
                        rs.getObject("nguoi_lap", Integer.class),
                        rs.getBigDecimal("tong_tien"),
                        rs.getString("ghi_chu"),
                        rs.getTimestamp("ngay_tao"));
                list.add(pn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // SEARCH phiếu nhập theo từ khóa (JOIN với nha_cung_cap)
    public List<PhieuNhap> search(String keyword) {
        List<PhieuNhap> list = new ArrayList<>();
        String sql = "SELECT pn.*, ncc.ten_ncc as nha_cung_cap " +
                "FROM phieu_nhap pn " +
                "LEFT JOIN nha_cung_cap ncc ON pn.ma_ncc = ncc.ma_ncc " +
                "WHERE pn.so_phieu LIKE ? OR ncc.ten_ncc LIKE ? " +
                "ORDER BY pn.ngay_nhap DESC";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pst = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            pst.setString(1, searchPattern);
            pst.setString(2, searchPattern);

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                PhieuNhap pn = new PhieuNhap(
                        rs.getInt("ma_phieu_nhap"),
                        rs.getString("so_phieu"),
                        rs.getDate("ngay_nhap"),
                        rs.getString("nha_cung_cap"), // Từ JOIN
                        rs.getObject("ma_ncc", Integer.class),
                        rs.getObject("ma_kho", Integer.class), // Thêm ma_kho
                        rs.getObject("nguoi_lap", Integer.class),
                        rs.getBigDecimal("tong_tien"),
                        rs.getString("ghi_chu"),
                        rs.getTimestamp("ngay_tao"));
                list.add(pn);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // GET CHI TIẾT phiếu nhập
    public List<ChiTietPhieuNhap> getChiTiet(int maPhieuNhap) {
        List<ChiTietPhieuNhap> list = new ArrayList<>();
        String sql = "SELECT ct.*, sp.ten_sp " +
                "FROM chi_tiet_phieu_nhap ct " +
                "JOIN san_pham sp ON ct.ma_sp = sp.ma_sp " +
                "WHERE ct.ma_phieu_nhap = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, maPhieuNhap);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                ChiTietPhieuNhap ct = new ChiTietPhieuNhap(
                        rs.getInt("id"),
                        rs.getInt("ma_phieu_nhap"),
                        rs.getInt("ma_sp"),
                        rs.getString("ten_sp"),
                        rs.getInt("so_luong"),
                        rs.getBigDecimal("don_gia"),
                        rs.getBigDecimal("thanh_tien"));
                list.add(ct);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
