package dao;

import database.DatabaseConnection;
import model.SaoLuu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SaoLuuDAO {

    /**
     * Insert a new backup record into sao_luu table
     */
    public boolean insert(SaoLuu saoLuu) {
        String sql = "INSERT INTO sao_luu (ten_file, duong_dan, kich_thuoc, nguoi_thuc_hien, loai, ghi_chu) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getNewConnection();
                PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, saoLuu.getTenFile());
            pst.setString(2, saoLuu.getDuongDan());
            pst.setLong(3, saoLuu.getKichThuoc());
            pst.setInt(4, saoLuu.getNguoiThucHien());
            pst.setString(5, saoLuu.getLoai());
            pst.setString(6, saoLuu.getGhiChu());

            return pst.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get all backup records ordered by date (newest first)
     */
    public List<SaoLuu> getAll() {
        List<SaoLuu> list = new ArrayList<>();
        String sql = "SELECT * FROM sao_luu ORDER BY ngay_thuc_hien DESC";

        try (Connection conn = DatabaseConnection.getNewConnection();
                PreparedStatement pst = conn.prepareStatement(sql);
                ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                SaoLuu sl = new SaoLuu(
                        rs.getInt("ma_sao_luu"),
                        rs.getString("ten_file"),
                        rs.getString("duong_dan"),
                        rs.getLong("kich_thuoc"),
                        rs.getInt("nguoi_thuc_hien"),
                        rs.getString("loai"),
                        rs.getTimestamp("ngay_thuc_hien"),
                        rs.getString("ghi_chu"));
                list.add(sl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Get only backup records (not restore records)
     */
    public List<SaoLuu> getBackupsOnly() {
        List<SaoLuu> list = new ArrayList<>();
        String sql = "SELECT * FROM sao_luu WHERE loai = 'backup' ORDER BY ngay_thuc_hien DESC";

        try (Connection conn = DatabaseConnection.getNewConnection();
                PreparedStatement pst = conn.prepareStatement(sql);
                ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                SaoLuu sl = new SaoLuu(
                        rs.getInt("ma_sao_luu"),
                        rs.getString("ten_file"),
                        rs.getString("duong_dan"),
                        rs.getLong("kich_thuoc"),
                        rs.getInt("nguoi_thuc_hien"),
                        rs.getString("loai"),
                        rs.getTimestamp("ngay_thuc_hien"),
                        rs.getString("ghi_chu"));
                list.add(sl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Delete a backup record by ID
     */
    public boolean deleteById(int maSaoLuu) {
        String sql = "DELETE FROM sao_luu WHERE ma_sao_luu = ?";
        try (Connection conn = DatabaseConnection.getNewConnection();
                PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, maSaoLuu);
            return pst.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete a backup record by file path
     */
    public boolean deleteByPath(String duongDan) {
        String sql = "DELETE FROM sao_luu WHERE duong_dan = ?";
        try (Connection conn = DatabaseConnection.getNewConnection();
                PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, duongDan);
            return pst.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
