package dao;

import database.DatabaseConnection;
import model.User;
import utils.BCryptHelper;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    
    /**
     * Đăng nhập
     */
    public User login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND trang_thai = 1";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                
                if (BCryptHelper.checkPassword(password, hashedPassword)) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(hashedPassword);
                    user.setHoTen(rs.getString("ho_ten"));
                    user.setRole(rs.getString("role"));
                    user.setNgayTao(rs.getTimestamp("ngay_tao"));
                    user.setTrangThai(rs.getInt("trang_thai"));
                    return user;
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Lấy tất cả user
     */
    public List<User> getAll() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY id DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setHoTen(rs.getString("ho_ten"));
                user.setRole(rs.getString("role"));
                user.setNgayTao(rs.getTimestamp("ngay_tao"));
                user.setTrangThai(rs.getInt("trang_thai"));
                list.add(user);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return list;
    }
    
    /**
     * Thêm user mới
     */
    public boolean insert(User user) {
        String sql = "INSERT INTO users (username, password, ho_ten, role, trang_thai) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, BCryptHelper.hashPassword(user.getPassword())); // Hash password
            pstmt.setString(3, user.getHoTen());
            pstmt.setString(4, user.getRole());
            pstmt.setInt(5, user.getTrangThai());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Cập nhật user (không đổi password)
     */
    public boolean update(User user) {
        String sql = "UPDATE users SET ho_ten=?, role=?, trang_thai=? WHERE id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, user.getHoTen());
            pstmt.setString(2, user.getRole());
            pstmt.setInt(3, user.getTrangThai());
            pstmt.setInt(4, user.getId());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Đổi mật khẩu
     */
    public boolean changePassword(int userId, String newPassword) {
        String sql = "UPDATE users SET password=? WHERE id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, BCryptHelper.hashPassword(newPassword));
            pstmt.setInt(2, userId);
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Xóa user
     */
    public boolean delete(int userId) {
        String sql = "DELETE FROM users WHERE id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Tìm kiếm user
     */
    public List<User> search(String keyword) {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE username LIKE ? OR ho_ten LIKE ? ORDER BY id DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setHoTen(rs.getString("ho_ten"));
                user.setRole(rs.getString("role"));
                user.setNgayTao(rs.getTimestamp("ngay_tao"));
                user.setTrangThai(rs.getInt("trang_thai"));
                list.add(user);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return list;
    }
    
    /**
     * Kiểm tra username đã tồn tại chưa
     */
    public boolean isUsernameExist(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
}
