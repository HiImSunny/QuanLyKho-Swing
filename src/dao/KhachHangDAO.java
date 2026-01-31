package dao;

import database.DatabaseConnection;
import model.KhachHang;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KhachHangDAO {
    
    /**
     * Lấy tất cả khách hàng
     */
    public List<KhachHang> getAll() {
        List<KhachHang> list = new ArrayList<>();
        String sql = "SELECT * FROM khach_hang ORDER BY ma_kh DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                KhachHang kh = new KhachHang();
                kh.setMaKh(rs.getInt("ma_kh"));
                kh.setTenKh(rs.getString("ten_kh"));
                kh.setDiaChi(rs.getString("dia_chi"));
                kh.setSdt(rs.getString("sdt"));
                kh.setEmail(rs.getString("email"));
                kh.setLoaiKh(rs.getString("loai_kh"));
                kh.setNgayTao(rs.getTimestamp("ngay_tao"));
                kh.setTrangThai(rs.getInt("trang_thai"));
                list.add(kh);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return list;
    }
    
    /**
     * Thêm khách hàng mới
     */
    public boolean insert(KhachHang kh) {
        String sql = "INSERT INTO khach_hang (ten_kh, dia_chi, sdt, email, loai_kh, trang_thai) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, kh.getTenKh());
            pstmt.setString(2, kh.getDiaChi());
            pstmt.setString(3, kh.getSdt());
            pstmt.setString(4, kh.getEmail());
            pstmt.setString(5, kh.getLoaiKh());
            pstmt.setInt(6, kh.getTrangThai());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Cập nhật khách hàng
     */
    public boolean update(KhachHang kh) {
        String sql = "UPDATE khach_hang SET ten_kh=?, dia_chi=?, sdt=?, email=?, loai_kh=?, trang_thai=? " +
                     "WHERE ma_kh=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, kh.getTenKh());
            pstmt.setString(2, kh.getDiaChi());
            pstmt.setString(3, kh.getSdt());
            pstmt.setString(4, kh.getEmail());
            pstmt.setString(5, kh.getLoaiKh());
            pstmt.setInt(6, kh.getTrangThai());
            pstmt.setInt(7, kh.getMaKh());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Xóa khách hàng
     */
    public boolean delete(int maKh) {
        String sql = "DELETE FROM khach_hang WHERE ma_kh=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, maKh);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Tìm kiếm khách hàng
     */
    public List<KhachHang> search(String keyword) {
        List<KhachHang> list = new ArrayList<>();
        String sql = "SELECT * FROM khach_hang " +
                     "WHERE ten_kh LIKE ? OR dia_chi LIKE ? OR sdt LIKE ? OR email LIKE ? " +
                     "ORDER BY ma_kh DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            pstmt.setString(4, searchPattern);
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                KhachHang kh = new KhachHang();
                kh.setMaKh(rs.getInt("ma_kh"));
                kh.setTenKh(rs.getString("ten_kh"));
                kh.setDiaChi(rs.getString("dia_chi"));
                kh.setSdt(rs.getString("sdt"));
                kh.setEmail(rs.getString("email"));
                kh.setLoaiKh(rs.getString("loai_kh"));
                kh.setNgayTao(rs.getTimestamp("ngay_tao"));
                kh.setTrangThai(rs.getInt("trang_thai"));
                list.add(kh);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return list;
    }
    
    /**
     * Lấy KH đang hoạt động (cho ComboBox)
     */
    public List<KhachHang> getActive() {
        List<KhachHang> list = new ArrayList<>();
        String sql = "SELECT * FROM khach_hang WHERE trang_thai=1 ORDER BY ten_kh";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                KhachHang kh = new KhachHang();
                kh.setMaKh(rs.getInt("ma_kh"));
                kh.setTenKh(rs.getString("ten_kh"));
                kh.setDiaChi(rs.getString("dia_chi"));
                kh.setSdt(rs.getString("sdt"));
                kh.setEmail(rs.getString("email"));
                kh.setLoaiKh(rs.getString("loai_kh"));
                list.add(kh);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return list;
    }
}
