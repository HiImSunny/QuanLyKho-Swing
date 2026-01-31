package test;

import database.DatabaseConnection;
import java.sql.*;

/**
 * Chạy migration Cấp độ 1: Đơn giản hóa schema
 * - Bỏ phieu_nhap.nha_cung_cap
 * - Bỏ phieu_xuat.khach_hang
 * - Bỏ san_pham.ma_kho
 */
public class RunSchemaFix {

    public static void main(String[] args) {
        System.out.println("=== CHẠY MIGRATION CẤP ĐỘ 1 ===\n");

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement()) {

            conn.setAutoCommit(false); // Start transaction

            try {
                // 1. Bỏ phieu_nhap.nha_cung_cap
                System.out.println("1. Bỏ phieu_nhap.nha_cung_cap...");
                stmt.execute("ALTER TABLE phieu_nhap DROP COLUMN nha_cung_cap");
                System.out.println("   ✓ Thành công\n");

                // 2. Bỏ phieu_xuat.khach_hang
                System.out.println("2. Bỏ phieu_xuat.khach_hang...");
                stmt.execute("ALTER TABLE phieu_xuat DROP COLUMN khach_hang");
                System.out.println("   ✓ Thành công\n");

                // 3. Bỏ san_pham.ma_kho
                System.out.println("3. Bỏ san_pham.ma_kho...");
                try {
                    stmt.execute("ALTER TABLE san_pham DROP FOREIGN KEY san_pham_ibfk_2");
                    System.out.println("   ✓ Đã bỏ FK constraint");
                } catch (SQLException e) {
                    if (!e.getMessage().contains("check that column/key exists")) {
                        throw e;
                    }
                    System.out.println("   ⚠ FK constraint không tồn tại (đã bỏ rồi)");
                }

                stmt.execute("ALTER TABLE san_pham DROP COLUMN ma_kho");
                System.out.println("   ✓ Đã bỏ cột ma_kho\n");

                conn.commit(); // Commit transaction

                System.out.println("=== HOÀN THÀNH ===");
                System.out.println("Đã bỏ 3 cột trùng lặp/không cần thiết\n");

                // Verification
                System.out.println("=== KIỂM TRA KẾT QUẢ ===\n");

                verifyTable(conn, "san_pham");
                verifyTable(conn, "phieu_nhap");
                verifyTable(conn, "phieu_xuat");

            } catch (SQLException e) {
                conn.rollback();
                System.err.println("✗ Lỗi! Đã rollback transaction");
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }

        } catch (Exception e) {
            System.err.println("\n✗ Lỗi migration: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void verifyTable(Connection conn, String tableName) throws SQLException {
        System.out.println("📋 Bảng: " + tableName);
        DatabaseMetaData meta = conn.getMetaData();
        ResultSet cols = meta.getColumns(null, null, tableName, null);

        System.out.println("   Columns:");
        while (cols.next()) {
            String colName = cols.getString("COLUMN_NAME");
            String colType = cols.getString("TYPE_NAME");
            System.out.println("      - " + colName + " (" + colType + ")");
        }
        System.out.println();
    }
}
