package dao;

import database.DatabaseConnection;
import model.PhieuXuat;
import model.ChiTietPhieuXuat;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PhieuXuatDAO {

    // INSERT phiếu xuất + chi tiết (dùng Transaction)
    public boolean insert(PhieuXuat phieuXuat, List<ChiTietPhieuXuat> chiTietList) {
        Connection conn = null;
        PreparedStatement pstPhieu = null;
        PreparedStatement pstChiTiet = null;
        PreparedStatement pstCheckStock = null;
        PreparedStatement pstUpdateTonKho = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getNewConnection();
            conn.setAutoCommit(false); // Bắt đầu Transaction

            // 0. KIỂM TRA TỒN KHO TRƯỚC KHI XUẤT (CRITICAL!)
            Integer maKho = phieuXuat.getMa_kho();
            if (maKho != null) {
                String sqlCheckStock = "SELECT so_luong_ton FROM ton_kho WHERE ma_sp = ? AND ma_kho = ?";
                pstCheckStock = conn.prepareStatement(sqlCheckStock);

                for (ChiTietPhieuXuat ct : chiTietList) {
                    pstCheckStock.setInt(1, ct.getMa_sp());
                    pstCheckStock.setInt(2, maKho);
                    ResultSet rsStock = pstCheckStock.executeQuery();

                    int availableStock = 0;
                    if (rsStock.next()) {
                        availableStock = rsStock.getInt("so_luong_ton");
                    }
                    rsStock.close();

                    // Nếu không đủ hàng, rollback và throw exception
                    if (availableStock < ct.getSo_luong()) {
                        throw new SQLException("Không đủ hàng trong kho! Sản phẩm " + ct.getTen_sp() +
                                " chỉ còn " + availableStock + " nhưng yêu cầu xuất " + ct.getSo_luong());
                    }
                }
            }

            // 1. Insert vào bảng phieu_xuat
            String sqlPhieu = "INSERT INTO phieu_xuat (so_phieu, ngay_xuat, ma_kh, ma_kho, nguoi_lap, tong_tien, ghi_chu) VALUES (?, ?, ?, ?, ?, ?, ?)";
            pstPhieu = conn.prepareStatement(sqlPhieu, Statement.RETURN_GENERATED_KEYS);
            pstPhieu.setString(1, phieuXuat.getSo_phieu());
            pstPhieu.setDate(2, phieuXuat.getNgay_xuat());
            if (phieuXuat.getMa_kh() != null) {
                pstPhieu.setInt(3, phieuXuat.getMa_kh());
            } else {
                pstPhieu.setNull(3, Types.INTEGER);
            }
            if (phieuXuat.getMa_kho() != null) {
                pstPhieu.setInt(4, phieuXuat.getMa_kho());
            } else {
                pstPhieu.setNull(4, Types.INTEGER);
            }
            if (phieuXuat.getNguoi_lap() != null) {
                pstPhieu.setInt(5, phieuXuat.getNguoi_lap());
            } else {
                pstPhieu.setNull(5, Types.INTEGER);
            }
            pstPhieu.setBigDecimal(6, phieuXuat.getTong_tien());
            pstPhieu.setString(7, phieuXuat.getGhi_chu());

            int rowsPhieu = pstPhieu.executeUpdate();

            // Lấy ma_phieu_xuat vừa insert
            int maPhieuXuat = 0;
            rs = pstPhieu.getGeneratedKeys();
            if (rs.next()) {
                maPhieuXuat = rs.getInt(1);
            }

            // 2. Insert vào bảng chi_tiet_phieu_xuat
            String sqlChiTiet = "INSERT INTO chi_tiet_phieu_xuat (ma_phieu_xuat, ma_sp, so_luong, don_gia) VALUES (?, ?, ?, ?)";
            pstChiTiet = conn.prepareStatement(sqlChiTiet);

            for (ChiTietPhieuXuat ct : chiTietList) {
                pstChiTiet.setInt(1, maPhieuXuat);
                pstChiTiet.setInt(2, ct.getMa_sp());
                pstChiTiet.setInt(3, ct.getSo_luong());
                pstChiTiet.setBigDecimal(4, ct.getDon_gia());
                pstChiTiet.addBatch();
            }

            pstChiTiet.executeBatch();

            // 3. GIẢM tồn kho trong bảng ton_kho (CRITICAL!)
            String sqlUpdateTonKho = "UPDATE ton_kho SET so_luong_ton = so_luong_ton - ? WHERE ma_sp = ? AND ma_kho = ?";
            pstUpdateTonKho = conn.prepareStatement(sqlUpdateTonKho);

            if (maKho != null) {
                for (ChiTietPhieuXuat ct : chiTietList) {
                    pstUpdateTonKho.setInt(1, ct.getSo_luong());
                    pstUpdateTonKho.setInt(2, ct.getMa_sp());
                    pstUpdateTonKho.setInt(3, maKho);
                    pstUpdateTonKho.addBatch();
                }
                pstUpdateTonKho.executeBatch();
            }

            conn.commit(); // Commit transaction
            return true;

        } catch (Exception e) {
            try {
                if (conn != null) {
                    conn.rollback(); // Rollback nếu có lỗi
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
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
                if (pstCheckStock != null)
                    pstCheckStock.close();
                if (pstUpdateTonKho != null)
                    pstUpdateTonKho.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // SELECT all phiếu xuất (JOIN với khach_hang)
    public List<PhieuXuat> getAll() {
        List<PhieuXuat> list = new ArrayList<>();
        String sql = "SELECT px.*, kh.ten_kh as khach_hang " +
                "FROM phieu_xuat px " +
                "LEFT JOIN khach_hang kh ON px.ma_kh = kh.ma_kh " +
                "ORDER BY px.ma_phieu_xuat DESC";

        try (Connection conn = DatabaseConnection.getNewConnection();
                PreparedStatement pst = conn.prepareStatement(sql);
                ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                PhieuXuat px = new PhieuXuat(
                        rs.getInt("ma_phieu_xuat"),
                        rs.getString("so_phieu"),
                        rs.getDate("ngay_xuat"),
                        rs.getString("khach_hang"),
                        rs.getObject("ma_kh", Integer.class),
                        rs.getObject("ma_kho", Integer.class),
                        rs.getObject("nguoi_lap", Integer.class),
                        rs.getBigDecimal("tong_tien"),
                        rs.getString("ghi_chu"),
                        rs.getTimestamp("ngay_tao"));
                list.add(px);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // SEARCH phiếu xuất
    public List<PhieuXuat> search(String keyword) {
        List<PhieuXuat> list = new ArrayList<>();
        String sql = "SELECT px.*, kh.ten_kh as khach_hang " +
                "FROM phieu_xuat px " +
                "LEFT JOIN khach_hang kh ON px.ma_kh = kh.ma_kh " +
                "WHERE px.so_phieu LIKE ? OR kh.ten_kh LIKE ? " +
                "ORDER BY px.ma_phieu_xuat DESC";

        try (Connection conn = DatabaseConnection.getNewConnection();
                PreparedStatement pst = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            pst.setString(1, searchPattern);
            pst.setString(2, searchPattern);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                PhieuXuat px = new PhieuXuat(
                        rs.getInt("ma_phieu_xuat"),
                        rs.getString("so_phieu"),
                        rs.getDate("ngay_xuat"),
                        rs.getString("khach_hang"),
                        rs.getObject("ma_kh", Integer.class),
                        rs.getObject("ma_kho", Integer.class),
                        rs.getObject("nguoi_lap", Integer.class),
                        rs.getBigDecimal("tong_tien"),
                        rs.getString("ghi_chu"),
                        rs.getTimestamp("ngay_tao"));
                list.add(px);
            }

            rs.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // GET chi tiết phiếu xuất
    public List<ChiTietPhieuXuat> getChiTiet(int maPhieuXuat) {
        List<ChiTietPhieuXuat> list = new ArrayList<>();
        String sql = "SELECT ct.*, sp.ten_sp " +
                "FROM chi_tiet_phieu_xuat ct " +
                "JOIN san_pham sp ON ct.ma_sp = sp.ma_sp " +
                "WHERE ct.ma_phieu_xuat = ?";

        try (Connection conn = DatabaseConnection.getNewConnection();
                PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, maPhieuXuat);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                ChiTietPhieuXuat ct = new ChiTietPhieuXuat(
                        rs.getInt("ma_phieu_xuat"),
                        rs.getInt("ma_sp"),
                        rs.getString("ten_sp"),
                        rs.getInt("so_luong"),
                        rs.getBigDecimal("don_gia"),
                        rs.getBigDecimal("thanh_tien"));
                list.add(ct);
            }

            rs.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // HỦY phiếu xuất (đánh dấu trạng thái = 'da_huy' VÀ revert tồn kho)
    public boolean cancelPhieu(int maPhieuXuat) {
        Connection conn = null;
        PreparedStatement pstUpdate = null;
        PreparedStatement pstGetChiTiet = null;
        PreparedStatement pstUpdateTonKho = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getNewConnection();
            conn.setAutoCommit(false); // Transaction

            // 1. Kiểm tra trạng thái hiện tại
            String sqlCheck = "SELECT trang_thai FROM phieu_xuat WHERE ma_phieu_xuat = ?";
            pstUpdate = conn.prepareStatement(sqlCheck);
            pstUpdate.setInt(1, maPhieuXuat);
            rs = pstUpdate.executeQuery();

            if (rs.next()) {
                String trangThai = rs.getString("trang_thai");
                if ("da_huy".equals(trangThai)) {
                    return false; // Đã hủy rồi
                }
            }
            rs.close();
            pstUpdate.close();

            // 2. Lấy chi tiết phiếu xuất để revert tồn kho
            String sqlChiTiet = "SELECT ct.ma_sp, ct.so_luong, px.ma_kho " +
                    "FROM chi_tiet_phieu_xuat ct " +
                    "JOIN phieu_xuat px ON ct.ma_phieu_xuat = px.ma_phieu_xuat " +
                    "WHERE ct.ma_phieu_xuat = ?";
            pstGetChiTiet = conn.prepareStatement(sqlChiTiet);
            pstGetChiTiet.setInt(1, maPhieuXuat);
            rs = pstGetChiTiet.executeQuery();

            // 3. Revert tồn kho (CỘNG lại số lượng đã xuất)
            String sqlUpdateTonKho = "UPDATE ton_kho SET so_luong_ton = so_luong_ton + ? " +
                    "WHERE ma_kho = ? AND ma_sp = ?";
            pstUpdateTonKho = conn.prepareStatement(sqlUpdateTonKho);

            while (rs.next()) {
                int maSP = rs.getInt("ma_sp");
                int soLuong = rs.getInt("so_luong");
                Integer maKho = rs.getObject("ma_kho", Integer.class);

                if (maKho != null) {
                    pstUpdateTonKho.setInt(1, soLuong);
                    pstUpdateTonKho.setInt(2, maKho);
                    pstUpdateTonKho.setInt(3, maSP);
                    pstUpdateTonKho.addBatch();
                }
            }
            pstUpdateTonKho.executeBatch();

            // 4. Cập nhật trạng thái phiếu xuất
            String sqlUpdate = "UPDATE phieu_xuat SET trang_thai = 'da_huy' WHERE ma_phieu_xuat = ?";
            pstUpdate = conn.prepareStatement(sqlUpdate);
            pstUpdate.setInt(1, maPhieuXuat);
            pstUpdate.executeUpdate();

            conn.commit();
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
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
                if (pstUpdate != null)
                    pstUpdate.close();
                if (pstGetChiTiet != null)
                    pstGetChiTiet.close();
                if (pstUpdateTonKho != null)
                    pstUpdateTonKho.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
