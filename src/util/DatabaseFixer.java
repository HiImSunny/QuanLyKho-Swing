package util;

import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.Statement;

public class DatabaseFixer {

    public static void fixMissingColumns() {
        System.out.println("Đang kiểm tra và sửa lỗi database...");

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement()) {

            // 1. Thêm cột trang_thai cho phieu_nhap
            try {
                String sql = "ALTER TABLE phieu_nhap ADD COLUMN trang_thai VARCHAR(20) DEFAULT 'hoan_thanh'";
                stmt.executeUpdate(sql);
                System.out.println("✓ Đã thêm cột 'trang_thai' vào bảng phieu_nhap");
            } catch (Exception e) {
                if (e.getMessage().contains("Duplicate column")) {
                    System.out.println("✓ Cột 'trang_thai' đã tồn tại trong phieu_nhap");
                } else {
                    System.err.println("⚠ Lỗi thêm cột phieu_nhap: " + e.getMessage());
                }
            }

            // 2. Thêm cột trang_thai cho phieu_xuat
            try {
                String sql = "ALTER TABLE phieu_xuat ADD COLUMN trang_thai VARCHAR(20) DEFAULT 'hoan_thanh'";
                stmt.executeUpdate(sql);
                System.out.println("✓ Đã thêm cột 'trang_thai' vào bảng phieu_xuat");
            } catch (Exception e) {
                if (e.getMessage().contains("Duplicate column")) {
                    System.out.println("✓ Cột 'trang_thai' đã tồn tại trong phieu_xuat");
                } else {
                    System.err.println("⚠ Lỗi thêm cột phieu_xuat: " + e.getMessage());
                }
            }

            System.out.println("Hoàn tất sửa lỗi database!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Test method
    public static void main(String[] args) {
        fixMissingColumns();
    }
}
