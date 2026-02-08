package dao;

import database.DatabaseConnection;
import model.LoaiSanPham;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoaiSanPhamDAO {

    /**
     * Lấy tất cả loại sản phẩm
     */
    public List<LoaiSanPham> getAll() {
        List<LoaiSanPham> list = new ArrayList<>();
        String sql = "SELECT * FROM loai_san_pham ORDER BY ma_loai";

        try (Connection conn = DatabaseConnection.getNewConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                LoaiSanPham loai = new LoaiSanPham();
                loai.setMaLoai(rs.getInt("ma_loai"));
                loai.setTenLoai(rs.getString("ten_loai"));
                loai.setMoTa(rs.getString("mo_ta"));
                loai.setNgayTao(rs.getTimestamp("ngay_tao"));
                list.add(loai);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Thêm loại sản phẩm mới
     */
    public boolean insert(LoaiSanPham loai) {
        String sql = "INSERT INTO loai_san_pham (ten_loai, mo_ta) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getNewConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, loai.getTenLoai());
            pstmt.setString(2, loai.getMoTa());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cập nhật loại sản phẩm
     */
    public boolean update(LoaiSanPham loai) {
        String sql = "UPDATE loai_san_pham SET ten_loai=?, mo_ta=? WHERE ma_loai=?";

        try (Connection conn = DatabaseConnection.getNewConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, loai.getTenLoai());
            pstmt.setString(2, loai.getMoTa());
            pstmt.setInt(3, loai.getMaLoai());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Xóa loại sản phẩm
     */
    public boolean delete(int maLoai) {
        String sql = "DELETE FROM loai_san_pham WHERE ma_loai=?";

        try (Connection conn = DatabaseConnection.getNewConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, maLoai);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Tìm kiếm loại sản phẩm
     */
    public List<LoaiSanPham> search(String keyword) {
        List<LoaiSanPham> list = new ArrayList<>();
        String sql = "SELECT * FROM loai_san_pham WHERE ten_loai LIKE ? OR mo_ta LIKE ? ORDER BY ma_loai";

        try (Connection conn = DatabaseConnection.getNewConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                LoaiSanPham loai = new LoaiSanPham();
                loai.setMaLoai(rs.getInt("ma_loai"));
                loai.setTenLoai(rs.getString("ten_loai"));
                loai.setMoTa(rs.getString("mo_ta"));
                loai.setNgayTao(rs.getTimestamp("ngay_tao"));
                list.add(loai);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
