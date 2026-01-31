package test;

import dao.TonKhoDAO;
import database.DatabaseConnection;
import model.TonKho;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class VerifyMigration {

    public static void main(String[] args) {
        System.out.println("=== KIỂM TRA KẾT QUẢ MIGRATION ===\n");

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement()) {

            // 1. Kiểm tra bảng ton_kho tồn tại
            System.out.println("1. Kiểm tra bảng ton_kho:");
            ResultSet rs = stmt.executeQuery("SHOW TABLES LIKE 'ton_kho'");
            if (rs.next()) {
                System.out.println("   ✓ Bảng ton_kho đã tồn tại\n");
            } else {
                System.out.println("   ✗ Bảng ton_kho CHƯA tồn tại!\n");
                return;
            }
            rs.close();

            // 2. Kiểm tra cấu trúc bảng
            System.out.println("2. Cấu trúc bảng ton_kho:");
            rs = stmt.executeQuery("DESC ton_kho");
            while (rs.next()) {
                System.out.println("   - " + rs.getString("Field") +
                        " (" + rs.getString("Type") + ")");
            }
            rs.close();
            System.out.println();

            // 3. Đếm số bản ghi
            System.out.println("3. Số lượng bản ghi:");
            rs = stmt.executeQuery("SELECT COUNT(*) as total FROM ton_kho");
            if (rs.next()) {
                int total = rs.getInt("total");
                System.out.println("   Tổng số bản ghi: " + total + "\n");
            }
            rs.close();

            // 4. Hiển thị 5 bản ghi mẫu
            System.out.println("4. Dữ liệu mẫu (5 bản ghi đầu):");
            TonKhoDAO dao = new TonKhoDAO();
            List<TonKho> list = dao.getAll();

            if (list.isEmpty()) {
                System.out.println("   (Chưa có dữ liệu)\n");
            } else {
                System.out.println("   ┌─────────────────────────────────────────────────────────┐");
                System.out.println("   │ Sản phẩm          │ Kho        │ Số lượng │ Cập nhật  │");
                System.out.println("   ├─────────────────────────────────────────────────────────┤");

                for (int i = 0; i < Math.min(5, list.size()); i++) {
                    TonKho tk = list.get(i);
                    System.out.printf("   │ %-17s │ %-10s │ %8d │ %s │%n",
                            truncate(tk.getTenSp(), 17),
                            truncate(tk.getTenKho(), 10),
                            tk.getSoLuongTon(),
                            tk.getNgayCapNhat().toString().substring(0, 10));
                }
                System.out.println("   └─────────────────────────────────────────────────────────┘\n");
            }

            // 5. Kiểm tra FK constraints
            System.out.println("5. Foreign Key Constraints:");
            rs = stmt.executeQuery(
                    "SELECT CONSTRAINT_NAME, REFERENCED_TABLE_NAME " +
                            "FROM information_schema.KEY_COLUMN_USAGE " +
                            "WHERE TABLE_SCHEMA = 'qlkho_db' AND TABLE_NAME = 'ton_kho' " +
                            "AND REFERENCED_TABLE_NAME IS NOT NULL");
            while (rs.next()) {
                System.out.println("   ✓ " + rs.getString("CONSTRAINT_NAME") +
                        " → " + rs.getString("REFERENCED_TABLE_NAME"));
            }
            rs.close();

            System.out.println("\n=== MIGRATION THÀNH CÔNG! ===");

        } catch (Exception e) {
            System.err.println("✗ Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String truncate(String str, int maxLen) {
        if (str == null)
            return "";
        return str.length() > maxLen ? str.substring(0, maxLen - 3) + "..." : str;
    }
}
