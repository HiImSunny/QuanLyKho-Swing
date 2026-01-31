package test;

import database.DatabaseConnection;
import java.sql.*;

/**
 * Distribute inventory across multiple warehouses for testing
 */
public class DistributeInventory {

    public static void main(String[] args) {
        System.out.println("=== PHAN BO TON KHO NHIEU KHO ===\n");

        try (Connection conn = DatabaseConnection.getConnection()) {
            Statement stmt = conn.createStatement();

            // 1. Kiem tra danh sach kho
            System.out.println("Danh sach kho:");
            ResultSet rsKho = stmt.executeQuery("SELECT id, ten_kho FROM kho ORDER BY id");
            while (rsKho.next()) {
                System.out.println("  - ID " + rsKho.getInt("id") + ": " + rsKho.getString("ten_kho"));
            }
            rsKho.close();

            // 2. Lay danh sach san pham co trong ton_kho
            System.out.println("\n--- THEM DU LIEU TEST ---\n");

            ResultSet rsSP = stmt.executeQuery("SELECT DISTINCT ma_sp FROM ton_kho ORDER BY ma_sp");
            int count = 0;

            while (rsSP.next()) {
                int maSp = rsSP.getInt("ma_sp");

                // Phan bo theo quy tac:
                // - 70% san pham: chi o kho 1 (mac dinh)
                // - 20% san pham: co o ca kho 1 va kho 2
                // - 10% san pham: chi o kho 2

                int rule = count % 10;

                if (rule >= 0 && rule <= 6) {
                    // 70% - Kho 1 only (mac dinh, khong can thay doi)
                    System.out.println("SP " + maSp + ": Giu nguyen kho 1");
                } else if (rule >= 7 && rule <= 8) {
                    // 20% - Co o ca 2 kho
                    System.out.println("SP " + maSp + ": Them vao kho 2");

                    // Them vao kho 2 voi so luong 25
                    PreparedStatement pst = conn.prepareStatement(
                            "INSERT INTO ton_kho (ma_sp, ma_kho, so_luong_ton) " +
                                    "VALUES (?, 2, 25) " +
                                    "ON DUPLICATE KEY UPDATE so_luong_ton = so_luong_ton + 25");
                    pst.setInt(1, maSp);
                    pst.executeUpdate();
                    pst.close();
                } else {
                    // 10% - Chi o kho 2
                    System.out.println("SP " + maSp + ": Chuyen sang kho 2 only");

                    // Cap nhat ma_kho tu 1 sang 2
                    PreparedStatement pst = conn.prepareStatement(
                            "UPDATE ton_kho SET ma_kho = 2 WHERE ma_sp = ? AND ma_kho = 1");
                    pst.setInt(1, maSp);
                    pst.executeUpdate();
                    pst.close();
                }

                count++;
            }
            rsSP.close();

            // 3. Thong ke ket qua
            System.out.println("\n=== THONG KE ===\n");

            ResultSet rsStats = stmt.executeQuery(
                    "SELECT ma_kho, COUNT(*) as so_sp, SUM(so_luong_ton) as tong_ton " +
                            "FROM ton_kho GROUP BY ma_kho");
            while (rsStats.next()) {
                System.out.println("Kho " + rsStats.getInt("ma_kho") +
                        ": " + rsStats.getInt("so_sp") + " san pham, " +
                        "tong ton " + rsStats.getInt("tong_ton"));
            }
            rsStats.close();

            stmt.close();
            System.out.println("\n[OK] Hoan thanh phan bo ton kho!");

        } catch (Exception e) {
            System.err.println("[FAIL] Loi: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
