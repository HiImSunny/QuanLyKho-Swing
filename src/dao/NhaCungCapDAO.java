package dao;

import database.DatabaseConnection;
import model.NhaCungCap;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NhaCungCapDAO {
    
    /**
     * Lấy tất cả nhà cung cấp
     */
    public List<NhaCungCap> getAll() {
        List<NhaCungCap> list = new ArrayList<>();
        String sql = "SELECT * FROM nha_cung_cap ORDER BY ma_ncc DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                NhaCungCap ncc = new NhaCungCap();
                ncc.setMaNcc(rs.getInt("ma_ncc"));
                ncc.setTenNcc(rs.getString("ten_ncc"));
                ncc.setDiaChi(rs.getString("dia_chi"));
                ncc.setSdt(rs.getString("sdt"));
                ncc.setEmail(rs.getString("email"));
                ncc.setNguoiLienHe(rs.getString("nguoi_lien_he"));
                ncc.setNgayTao(rs.getTimestamp("ngay_tao"));
                ncc.setTrangThai(rs.getInt("trang_thai"));
                list.add(ncc);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return list;
    }
    
    /**
     * Thêm nhà cung cấp mới
     */
    public boolean insert(NhaCungCap ncc) {
        String sql = "INSERT INTO nha_cung_cap (ten_ncc, dia_chi, sdt, email, nguoi_lien_he, trang_thai) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, ncc.getTenNcc());
            pstmt.setString(2, ncc.getDiaChi());
            pstmt.setString(3, ncc.getSdt());
            pstmt.setString(4, ncc.getEmail());
            pstmt.setString(5, ncc.getNguoiLienHe());
            pstmt.setInt(6, ncc.getTrangThai());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Cập nhật nhà cung cấp
     */
    public boolean update(NhaCungCap ncc) {
        String sql = "UPDATE nha_cung_cap SET ten_ncc=?, dia_chi=?, sdt=?, email=?, nguoi_lien_he=?, trang_thai=? " +
                     "WHERE ma_ncc=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, ncc.getTenNcc());
            pstmt.setString(2, ncc.getDiaChi());
            pstmt.setString(3, ncc.getSdt());
            pstmt.setString(4, ncc.getEmail());
            pstmt.setString(5, ncc.getNguoiLienHe());
            pstmt.setInt(6, ncc.getTrangThai());
            pstmt.setInt(7, ncc.getMaNcc());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Xóa nhà cung cấp
     */
    public boolean delete(int maNcc) {
        String sql = "DELETE FROM nha_cung_cap WHERE ma_ncc=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, maNcc);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Tìm kiếm nhà cung cấp
     */
    public List<NhaCungCap> search(String keyword) {
        List<NhaCungCap> list = new ArrayList<>();
        String sql = "SELECT * FROM nha_cung_cap " +
                     "WHERE ten_ncc LIKE ? OR dia_chi LIKE ? OR sdt LIKE ? OR email LIKE ? " +
                     "ORDER BY ma_ncc DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            pstmt.setString(4, searchPattern);
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                NhaCungCap ncc = new NhaCungCap();
                ncc.setMaNcc(rs.getInt("ma_ncc"));
                ncc.setTenNcc(rs.getString("ten_ncc"));
                ncc.setDiaChi(rs.getString("dia_chi"));
                ncc.setSdt(rs.getString("sdt"));
                ncc.setEmail(rs.getString("email"));
                ncc.setNguoiLienHe(rs.getString("nguoi_lien_he"));
                ncc.setNgayTao(rs.getTimestamp("ngay_tao"));
                ncc.setTrangThai(rs.getInt("trang_thai"));
                list.add(ncc);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return list;
    }
    
    /**
     * Lấy NCC đang hoạt động (cho ComboBox)
     */
    public List<NhaCungCap> getActive() {
        List<NhaCungCap> list = new ArrayList<>();
        String sql = "SELECT * FROM nha_cung_cap WHERE trang_thai=1 ORDER BY ten_ncc";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                NhaCungCap ncc = new NhaCungCap();
                ncc.setMaNcc(rs.getInt("ma_ncc"));
                ncc.setTenNcc(rs.getString("ten_ncc"));
                ncc.setDiaChi(rs.getString("dia_chi"));
                ncc.setSdt(rs.getString("sdt"));
                ncc.setEmail(rs.getString("email"));
                ncc.setNguoiLienHe(rs.getString("nguoi_lien_he"));
                list.add(ncc);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return list;
    }
}
