package dao;

import database.DatabaseConnection;
import model.TonKho;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TonKhoDAO {

    /**
     * Lấy danh sách tồn kho của một sản phẩm theo tất cả các kho
     */
    public List<TonKho> getByMaSp(int maSp) {
        List<TonKho> list = new ArrayList<>();
        String sql = "SELECT tk.*, sp.ten_sp, sp.don_vi_tinh, sp.gia_ban, k.ten_kho, lsp.ten_loai " +
                "FROM ton_kho tk " +
                "JOIN san_pham sp ON tk.ma_sp = sp.ma_sp " +
                "JOIN kho k ON tk.ma_kho = k.id " +
                "LEFT JOIN loai_san_pham lsp ON sp.ma_loai = lsp.ma_loai " +
                "WHERE tk.ma_sp = ?";

        try (Connection conn = DatabaseConnection.getNewConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, maSp);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                TonKho tk = new TonKho();
                tk.setMaSp(rs.getInt("ma_sp"));
                tk.setMaKho(rs.getInt("ma_kho"));
                tk.setSoLuongTon(rs.getInt("so_luong_ton"));
                tk.setNgayCapNhat(rs.getTimestamp("ngay_cap_nhat"));
                tk.setTenSp(rs.getString("ten_sp"));
                tk.setTenKho(rs.getString("ten_kho"));
                // Extra fields
                tk.setDonViTinh(rs.getString("don_vi_tinh"));
                tk.setGiaTien(rs.getBigDecimal("gia_ban"));
                tk.setTenLoai(rs.getString("ten_loai"));
                list.add(tk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Lấy danh sách tất cả sản phẩm trong một kho
     */
    public List<TonKho> getByMaKho(int maKho) {
        List<TonKho> list = new ArrayList<>();
        String sql = "SELECT tk.*, sp.ten_sp, sp.don_vi_tinh, sp.gia_ban, k.ten_kho, lsp.ten_loai " +
                "FROM ton_kho tk " +
                "JOIN san_pham sp ON tk.ma_sp = sp.ma_sp " +
                "JOIN kho k ON tk.ma_kho = k.id " +
                "LEFT JOIN loai_san_pham lsp ON sp.ma_loai = lsp.ma_loai " +
                "WHERE tk.ma_kho = ?";

        try (Connection conn = DatabaseConnection.getNewConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, maKho);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                TonKho tk = new TonKho();
                tk.setMaSp(rs.getInt("ma_sp"));
                tk.setMaKho(rs.getInt("ma_kho"));
                tk.setSoLuongTon(rs.getInt("so_luong_ton"));
                tk.setNgayCapNhat(rs.getTimestamp("ngay_cap_nhat"));
                tk.setTenSp(rs.getString("ten_sp"));
                tk.setTenKho(rs.getString("ten_kho"));
                // Extra fields
                tk.setDonViTinh(rs.getString("don_vi_tinh"));
                tk.setGiaTien(rs.getBigDecimal("gia_ban"));
                tk.setTenLoai(rs.getString("ten_loai"));
                list.add(tk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Lấy thông tin tồn kho của một sản phẩm tại một kho cụ thể
     */
    public TonKho getByMaSpAndMaKho(int maSp, int maKho) {
        String sql = "SELECT tk.*, sp.ten_sp, sp.don_vi_tinh, sp.gia_ban, k.ten_kho, lsp.ten_loai " +
                "FROM ton_kho tk " +
                "JOIN san_pham sp ON tk.ma_sp = sp.ma_sp " +
                "JOIN kho k ON tk.ma_kho = k.id " +
                "LEFT JOIN loai_san_pham lsp ON sp.ma_loai = lsp.ma_loai " +
                "WHERE tk.ma_sp = ? AND tk.ma_kho = ?";

        try (Connection conn = DatabaseConnection.getNewConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, maSp);
            pstmt.setInt(2, maKho);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                TonKho tk = new TonKho();
                tk.setMaSp(rs.getInt("ma_sp"));
                tk.setMaKho(rs.getInt("ma_kho"));
                tk.setSoLuongTon(rs.getInt("so_luong_ton"));
                tk.setNgayCapNhat(rs.getTimestamp("ngay_cap_nhat"));
                tk.setTenSp(rs.getString("ten_sp"));
                tk.setTenKho(rs.getString("ten_kho"));
                // Extra fields
                tk.setDonViTinh(rs.getString("don_vi_tinh"));
                tk.setGiaTien(rs.getBigDecimal("gia_ban"));
                tk.setTenLoai(rs.getString("ten_loai"));
                return tk;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Cập nhật số lượng tồn kho (set giá trị mới)
     */
    public boolean updateSoLuong(int maSp, int maKho, int soLuong) {
        String sql = "UPDATE ton_kho SET so_luong_ton = ? WHERE ma_sp = ? AND ma_kho = ?";

        try (Connection conn = DatabaseConnection.getNewConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, soLuong);
            pstmt.setInt(2, maSp);
            pstmt.setInt(3, maKho);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Cập nhật tồn kho (tăng/giảm theo delta)
     * delta > 0: nhập kho
     * delta < 0: xuất kho
     */
    public boolean capNhatTonKho(int maSp, int maKho, int delta) {
        String sql = "INSERT INTO ton_kho (ma_sp, ma_kho, so_luong_ton) " +
                "VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE so_luong_ton = so_luong_ton + ?";

        try (Connection conn = DatabaseConnection.getNewConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, maSp);
            pstmt.setInt(2, maKho);
            pstmt.setInt(3, delta);
            pstmt.setInt(4, delta);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Thêm mới bản ghi tồn kho
     */
    public boolean insert(TonKho tonKho) {
        String sql = "INSERT INTO ton_kho (ma_sp, ma_kho, so_luong_ton) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getNewConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, tonKho.getMaSp());
            pstmt.setInt(2, tonKho.getMaKho());
            pstmt.setInt(3, tonKho.getSoLuongTon());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Lấy tất cả tồn kho
     */
    public List<TonKho> getAll() {
        List<TonKho> list = new ArrayList<>();
        String sql = "SELECT tk.*, sp.ten_sp, sp.don_vi_tinh, sp.gia_ban, k.ten_kho, lsp.ten_loai " +
                "FROM ton_kho tk " +
                "JOIN san_pham sp ON tk.ma_sp = sp.ma_sp " +
                "JOIN kho k ON tk.ma_kho = k.id " +
                "LEFT JOIN loai_san_pham lsp ON sp.ma_loai = lsp.ma_loai " +
                "ORDER BY k.ten_kho, sp.ten_sp";

        try (Connection conn = DatabaseConnection.getNewConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                TonKho tk = new TonKho();
                tk.setMaSp(rs.getInt("ma_sp"));
                tk.setMaKho(rs.getInt("ma_kho"));
                tk.setSoLuongTon(rs.getInt("so_luong_ton"));
                tk.setNgayCapNhat(rs.getTimestamp("ngay_cap_nhat"));
                tk.setTenSp(rs.getString("ten_sp"));
                tk.setTenKho(rs.getString("ten_kho"));
                // Extra fields
                tk.setDonViTinh(rs.getString("don_vi_tinh"));
                tk.setGiaTien(rs.getBigDecimal("gia_ban"));
                tk.setTenLoai(rs.getString("ten_loai"));
                list.add(tk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
