package test;

import database.DatabaseConnection;
import java.sql.*;

/**
 * Add ma_kho column to phieu_xuat table
 */
public class AddMaKhoToPhieuXuat {

    public static void main(String[] args) {
        System.out.println("=== THÊM MA_KHO VÀO PHIEU_XUAT ===\n");

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement()) {

            conn.setAutoCommit(false);

            try {
                // 1. Add ma_kho column
                System.out.println("1. Thêm cột ma_kho...");
                stmt.execute("ALTER TABLE phieu_xuat ADD COLUMN ma_kho INT DEFAULT NULL AFTER ma_kh");
                System.out.println("   ✓ Thành công\n");

                // 2. Add FK constraint
                System.out.println("2. Thêm FK constraint...");
                stmt.execute("ALTER TABLE phieu_xuat ADD CONSTRAINT phieu_xuat_ibfk_3 " +
                        "FOREIGN KEY (ma_kho) REFERENCES kho(id) ON DELETE SET NULL ON UPDATE CASCADE");
                System.out.println("   ✓ Thành công\n");

                // 3. Add index
                System.out.println("3. Thêm index...");
                stmt.execute("ALTER TABLE phieu_xuat ADD KEY idx_ma_kho (ma_kho)");
                System.out.println("   ✓ Thành công\n");

                conn.commit();

                System.out.println("=== HOÀN THÀNH ===");
                System.out.println("Đã thêm ma_kho vào phieu_xuat\n");

                // Verification
                System.out.println("=== KIỂM TRA ===\n");
                DatabaseMetaData meta = conn.getMetaData();
                ResultSet cols = meta.getColumns(null, null, "phieu_xuat", null);

                System.out.println("Columns trong phieu_xuat:");
                while (cols.next()) {
                    String colName = cols.getString("COLUMN_NAME");
                    String colType = cols.getString("TYPE_NAME");
                    System.out.println("   - " + colName + " (" + colType + ")");
                }

            } catch (SQLException e) {
                conn.rollback();
                System.err.println("✗ Lỗi! Đã rollback");
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }

        } catch (Exception e) {
            System.err.println("\n✗ Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
