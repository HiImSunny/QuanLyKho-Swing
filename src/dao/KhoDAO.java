package dao;

import database.DatabaseConnection;
import model.Kho;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KhoDAO {

    /**
     * Lấy tất cả kho
     */
    public List<Kho> getAll() {
        List<Kho> list = new ArrayList<>();
        String sql = "SELECT * FROM kho ORDER BY id DESC";

        try (Connection conn = DatabaseConnection.getNewConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Kho kho = new Kho();
                kho.setId(rs.getInt("id"));
                kho.setMaKho(rs.getString("ma_kho"));
                kho.setTenKho(rs.getString("ten_kho"));
                kho.setDiaChi(rs.getString("dia_chi"));
                kho.setDienTich(rs.getDouble("dien_tich"));
                kho.setNguoiQuanLy(rs.getString("nguoi_quan_ly"));
                kho.setGhiChu(rs.getString("ghi_chu"));
                kho.setNgayTao(rs.getTimestamp("ngay_tao"));
                kho.setTrangThai(rs.getInt("trang_thai"));
                list.add(kho);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Lấy danh sách kho đang hoạt động
     */
    public List<Kho> getActive() {
        List<Kho> list = new ArrayList<>();
        String sql = "SELECT * FROM kho WHERE trang_thai = 1 ORDER BY ten_kho";

        try (Connection conn = DatabaseConnection.getNewConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Kho kho = new Kho();
                kho.setId(rs.getInt("id"));
                kho.setMaKho(rs.getString("ma_kho"));
                kho.setTenKho(rs.getString("ten_kho"));
                kho.setDiaChi(rs.getString("dia_chi"));
                kho.setDienTich(rs.getDouble("dien_tich"));
                kho.setNguoiQuanLy(rs.getString("nguoi_quan_ly"));
                kho.setGhiChu(rs.getString("ghi_chu"));
                kho.setNgayTao(rs.getTimestamp("ngay_tao"));
                kho.setTrangThai(rs.getInt("trang_thai"));
                list.add(kho);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Thêm kho mới
     */
    public boolean insert(Kho kho) {
        String sql = "INSERT INTO kho (ma_kho, ten_kho, dia_chi, dien_tich, nguoi_quan_ly, ghi_chu, trang_thai) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getNewConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, kho.getMaKho());
            pstmt.setString(2, kho.getTenKho());
            pstmt.setString(3, kho.getDiaChi());
            pstmt.setDouble(4, kho.getDienTich());
            pstmt.setString(5, kho.getNguoiQuanLy());
            pstmt.setString(6, kho.getGhiChu());
            pstmt.setInt(7, kho.getTrangThai());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cập nhật kho
     */
    public boolean update(Kho kho) {
        String sql = "UPDATE kho SET ma_kho=?, ten_kho=?, dia_chi=?, dien_tich=?, nguoi_quan_ly=?, ghi_chu=?, trang_thai=? WHERE id=?";

        try (Connection conn = DatabaseConnection.getNewConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, kho.getMaKho());
            pstmt.setString(2, kho.getTenKho());
            pstmt.setString(3, kho.getDiaChi());
            pstmt.setDouble(4, kho.getDienTich());
            pstmt.setString(5, kho.getNguoiQuanLy());
            pstmt.setString(6, kho.getGhiChu());
            pstmt.setInt(7, kho.getTrangThai());
            pstmt.setInt(8, kho.getId());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Xóa kho
     */
    public boolean delete(int id) {
        String sql = "DELETE FROM kho WHERE id=?";

        try (Connection conn = DatabaseConnection.getNewConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Tìm kiếm kho
     */
    public List<Kho> search(String keyword) {
        List<Kho> list = new ArrayList<>();
        String sql = "SELECT * FROM kho WHERE ma_kho LIKE ? OR ten_kho LIKE ? OR dia_chi LIKE ? OR nguoi_quan_ly LIKE ? ORDER BY id DESC";

        try (Connection conn = DatabaseConnection.getNewConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            pstmt.setString(4, searchPattern);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Kho kho = new Kho();
                kho.setId(rs.getInt("id"));
                kho.setMaKho(rs.getString("ma_kho"));
                kho.setTenKho(rs.getString("ten_kho"));
                kho.setDiaChi(rs.getString("dia_chi"));
                kho.setDienTich(rs.getDouble("dien_tich"));
                kho.setNguoiQuanLy(rs.getString("nguoi_quan_ly"));
                kho.setGhiChu(rs.getString("ghi_chu"));
                kho.setNgayTao(rs.getTimestamp("ngay_tao"));
                kho.setTrangThai(rs.getInt("trang_thai"));
                list.add(kho);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Kiểm tra mã kho đã tồn tại chưa
     */
    public boolean isMaKhoExist(String maKho) {
        String sql = "SELECT COUNT(*) FROM kho WHERE ma_kho = ?";

        try (Connection conn = DatabaseConnection.getNewConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maKho);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Kiểm tra mã kho đã tồn tại (trừ ID hiện tại khi update)
     */
    public boolean isMaKhoExist(String maKho, int excludeId) {
        String sql = "SELECT COUNT(*) FROM kho WHERE ma_kho = ? AND id != ?";

        try (Connection conn = DatabaseConnection.getNewConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maKho);
            pstmt.setInt(2, excludeId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Lấy kho theo ID
     */
    public Kho getById(int id) {
        String sql = "SELECT * FROM kho WHERE id = ?";

        try (Connection conn = DatabaseConnection.getNewConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Kho kho = new Kho();
                kho.setId(rs.getInt("id"));
                kho.setMaKho(rs.getString("ma_kho"));
                kho.setTenKho(rs.getString("ten_kho"));
                kho.setDiaChi(rs.getString("dia_chi"));
                kho.setDienTich(rs.getDouble("dien_tich"));
                kho.setNguoiQuanLy(rs.getString("nguoi_quan_ly"));
                kho.setGhiChu(rs.getString("ghi_chu"));
                kho.setNgayTao(rs.getTimestamp("ngay_tao"));
                kho.setTrangThai(rs.getInt("trang_thai"));
                return kho;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
