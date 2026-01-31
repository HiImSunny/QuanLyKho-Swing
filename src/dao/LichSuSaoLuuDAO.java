package dao;

import database.DatabaseConnection;
import model.LichSuSaoLuu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class LichSuSaoLuuDAO {

    public boolean insert(LichSuSaoLuu ls) {
        String sql = "INSERT INTO lich_su_sao_luu (ten_file, duong_dan) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, ls.getTenFile());
            pst.setString(2, ls.getDuongDan());

            return pst.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<LichSuSaoLuu> getAll() {
        List<LichSuSaoLuu> list = new ArrayList<>();
        String sql = "SELECT * FROM lich_su_sao_luu ORDER BY thoi_gian DESC";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pst = conn.prepareStatement(sql);
                ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                LichSuSaoLuu ls = new LichSuSaoLuu(
                        rs.getInt("id"),
                        rs.getString("ten_file"),
                        rs.getString("duong_dan"),
                        rs.getTimestamp("thoi_gian"));
                list.add(ls);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
