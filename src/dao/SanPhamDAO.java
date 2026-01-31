package dao;

import database.DatabaseConnection;
import model.SanPham;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SanPhamDAO {

    /**
     * Lấy tất cả sản phẩm (JOIN với loai_san_pham)
     */
    public List<SanPham> getAll() {
        List<SanPham> list = new ArrayList<>();
        String sql = "SELECT sp.*, lsp.ten_loai " +
                "FROM san_pham sp " +
                "INNER JOIN loai_san_pham lsp ON sp.ma_loai = lsp.ma_loai " +
                "ORDER BY sp.ma_sp DESC";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                SanPham sp = new SanPham();
                sp.setMaSp(rs.getInt("ma_sp"));
                sp.setTenSp(rs.getString("ten_sp"));
                sp.setMaLoai(rs.getInt("ma_loai"));
                sp.setTenLoai(rs.getString("ten_loai"));
                sp.setDonViTinh(rs.getString("don_vi_tinh"));
                sp.setGiaNhap(rs.getBigDecimal("gia_nhap"));
                sp.setGiaBan(rs.getBigDecimal("gia_ban"));
                sp.setSoLuongTon(rs.getInt("so_luong_ton"));
                sp.setMoTa(rs.getString("mo_ta"));
                sp.setHinhAnh(rs.getString("hinh_anh"));
                sp.setNgayTao(rs.getTimestamp("ngay_tao"));
                list.add(sp);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Thêm sản phẩm mới
     */
    public boolean insert(SanPham sp) {
        String sql = "INSERT INTO san_pham (ten_sp, ma_loai, don_vi_tinh, gia_nhap, gia_ban, so_luong_ton, mo_ta) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, sp.getTenSp());
            pstmt.setInt(2, sp.getMaLoai());
            pstmt.setString(3, sp.getDonViTinh());
            pstmt.setBigDecimal(4, sp.getGiaNhap());
            pstmt.setBigDecimal(5, sp.getGiaBan());
            pstmt.setInt(6, sp.getSoLuongTon());
            pstmt.setString(7, sp.getMoTa());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cập nhật sản phẩm
     */
    public boolean update(SanPham sp) {
        String sql = "UPDATE san_pham SET ten_sp=?, ma_loai=?, don_vi_tinh=?, gia_nhap=?, gia_ban=?, so_luong_ton=?, mo_ta=? "
                +
                "WHERE ma_sp=?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, sp.getTenSp());
            pstmt.setInt(2, sp.getMaLoai());
            pstmt.setString(3, sp.getDonViTinh());
            pstmt.setBigDecimal(4, sp.getGiaNhap());
            pstmt.setBigDecimal(5, sp.getGiaBan());
            pstmt.setInt(6, sp.getSoLuongTon());
            pstmt.setString(7, sp.getMoTa());
            pstmt.setInt(8, sp.getMaSp());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Xóa sản phẩm
     */
    public boolean delete(int maSp) {
        String sql = "DELETE FROM san_pham WHERE ma_sp=?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, maSp);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Tìm kiếm sản phẩm theo tên
     */
    public List<SanPham> search(String keyword) {
        List<SanPham> list = new ArrayList<>();
        String sql = "SELECT sp.*, lsp.ten_loai " +
                "FROM san_pham sp " +
                "INNER JOIN loai_san_pham lsp ON sp.ma_loai = lsp.ma_loai " +
                "WHERE sp.ten_sp LIKE ? OR sp.mo_ta LIKE ? " +
                "ORDER BY sp.ma_sp DESC";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                SanPham sp = new SanPham();
                sp.setMaSp(rs.getInt("ma_sp"));
                sp.setTenSp(rs.getString("ten_sp"));
                sp.setMaLoai(rs.getInt("ma_loai"));
                sp.setTenLoai(rs.getString("ten_loai"));
                sp.setDonViTinh(rs.getString("don_vi_tinh"));
                sp.setGiaNhap(rs.getBigDecimal("gia_nhap"));
                sp.setGiaBan(rs.getBigDecimal("gia_ban"));
                sp.setSoLuongTon(rs.getInt("so_luong_ton"));
                sp.setMoTa(rs.getString("mo_ta"));
                sp.setNgayTao(rs.getTimestamp("ngay_tao"));
                list.add(sp);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
