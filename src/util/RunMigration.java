package util;

import database.DatabaseConnection;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.Statement;

public class RunMigration {

    public static void main(String[] args) {
        String migrationFile = "src/migration_ton_kho.sql";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement()) {

            System.out.println("=== BẮT ĐẦU MIGRATION ===");

            // Read SQL file
            StringBuilder sql = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(migrationFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    // Skip comments and empty lines
                    line = line.trim();
                    if (line.isEmpty() || line.startsWith("--")) {
                        continue;
                    }
                    sql.append(line).append(" ");
                }
            }

            // Split by semicolon and execute each statement
            String[] statements = sql.toString().split(";");
            int count = 0;

            for (String statement : statements) {
                statement = statement.trim();
                if (statement.isEmpty())
                    continue;

                try {
                    stmt.execute(statement);
                    count++;
                    System.out.println("✓ Executed statement " + count);
                } catch (Exception e) {
                    // Some statements might fail if already exists, that's OK
                    if (!e.getMessage().contains("already exists") &&
                            !e.getMessage().contains("Duplicate")) {
                        System.err.println("⚠ Warning: " + e.getMessage());
                    }
                }
            }

            System.out.println("\n=== MIGRATION HOÀN THÀNH ===");
            System.out.println("Tổng số câu lệnh đã thực thi: " + count);

            // Verify ton_kho table exists
            var rs = stmt.executeQuery("SELECT COUNT(*) as total FROM ton_kho");
            if (rs.next()) {
                System.out.println("✓ Bảng ton_kho đã được tạo với " + rs.getInt("total") + " bản ghi");
            }

        } catch (Exception e) {
            System.err.println("✗ Lỗi migration: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
