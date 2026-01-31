package test;

import dao.TonKhoDAO;
import database.DatabaseConnection;
import model.TonKho;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

/**
 * Test class để kiểm tra migration và TonKhoDAO
 * Chạy class này để:
 * 1. Tạo bảng ton_kho (nếu chưa có)
 * 2. Test các method của TonKhoDAO
 */
public class TestTonKho {

    public static void main(String[] args) {
        System.out.println("=== TEST TON KHO DAO ===\n");

        // Step 1: Run migration
        runMigration();

        // Step 2: Test TonKhoDAO
        testTonKhoDAO();
    }

    private static void runMigration() {
        System.out.println("1. Chạy migration...");
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement()) {

            // Create ton_kho table if not exists
            String createTable = "CREATE TABLE IF NOT EXISTS `ton_kho` (" +
                    "  `id` int(11) NOT NULL AUTO_INCREMENT," +
                    "  `ma_sp` int(11) NOT NULL," +
                    "  `ma_kho` int(11) NOT NULL," +
                    "  `so_luong_ton` int(11) DEFAULT 0," +
                    "  `ngay_cap_nhat` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()," +
                    "  PRIMARY KEY (`id`)," +
                    "  UNIQUE KEY `unique_sp_kho` (`ma_sp`,`ma_kho`)," +
                    "  KEY `ma_kho` (`ma_kho`)," +
                    "  CONSTRAINT `ton_kho_ibfk_1` FOREIGN KEY (`ma_sp`) REFERENCES `san_pham` (`ma_sp`) ON DELETE CASCADE ON UPDATE CASCADE,"
                    +
                    "  CONSTRAINT `ton_kho_ibfk_2` FOREIGN KEY (`ma_kho`) REFERENCES `kho` (`id`) ON DELETE CASCADE ON UPDATE CASCADE"
                    +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci";

            stmt.execute(createTable);
            System.out.println("   ✓ Bảng ton_kho đã sẵn sàng");

            // Add FK constraint for san_pham.ma_kho if not exists
            try {
                String addFK = "ALTER TABLE `san_pham` " +
                        "ADD CONSTRAINT `san_pham_ibfk_2` " +
                        "FOREIGN KEY (`ma_kho`) REFERENCES `kho` (`id`) " +
                        "ON DELETE SET NULL ON UPDATE CASCADE";
                stmt.execute(addFK);
                System.out.println("   ✓ Đã thêm FK constraint cho san_pham.ma_kho");
            } catch (Exception e) {
                if (e.getMessage().contains("Duplicate")) {
                    System.out.println("   ✓ FK constraint đã tồn tại");
                } else {
                    System.out.println("   ⚠ Warning: " + e.getMessage());
                }
            }

            // Migrate existing data
            String migrate = "INSERT INTO `ton_kho` (`ma_sp`, `ma_kho`, `so_luong_ton`) " +
                    "SELECT `ma_sp`, COALESCE(`ma_kho`, 1), COALESCE(`so_luong_ton`, 0) " +
                    "FROM `san_pham` " +
                    "WHERE `ma_sp` NOT IN (SELECT `ma_sp` FROM `ton_kho`) " +
                    "ON DUPLICATE KEY UPDATE `so_luong_ton` = VALUES(`so_luong_ton`)";

            int migrated = stmt.executeUpdate(migrate);
            System.out.println("   ✓ Đã migrate " + migrated + " bản ghi\n");

        } catch (Exception e) {
            System.err.println("   ✗ Lỗi migration: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void testTonKhoDAO() {
        System.out.println("2. Test TonKhoDAO...");
        TonKhoDAO dao = new TonKhoDAO();

        // Test getAll
        System.out.println("\n   a) Test getAll():");
        List<TonKho> all = dao.getAll();
        System.out.println("      Tổng số bản ghi: " + all.size());
        if (!all.isEmpty()) {
            System.out.println("      Ví dụ: " + all.get(0).getTenSp() +
                    " tại " + all.get(0).getTenKho() +
                    " - SL: " + all.get(0).getSoLuongTon());
        }

        // Test getByMaKho (assuming kho with id=1 exists)
        System.out.println("\n   b) Test getByMaKho(1):");
        List<TonKho> byKho = dao.getByMaKho(1);
        System.out.println("      Số sản phẩm trong kho 1: " + byKho.size());

        // Test capNhatTonKho
        if (!all.isEmpty()) {
            TonKho first = all.get(0);
            System.out.println("\n   c) Test capNhatTonKho():");
            System.out.println("      Sản phẩm: " + first.getTenSp());
            System.out.println("      Tồn kho hiện tại: " + first.getSoLuongTon());

            // Thử cập nhật +10
            boolean updated = dao.capNhatTonKho(first.getMaSp(), first.getMaKho(), 10);
            if (updated) {
                TonKho after = dao.getByMaSpAndMaKho(first.getMaSp(), first.getMaKho());
                System.out.println("      Sau khi +10: " + after.getSoLuongTon());

                // Rollback về số cũ
                dao.capNhatTonKho(first.getMaSp(), first.getMaKho(), -10);
                System.out.println("      ✓ Test thành công (đã rollback)");
            }
        }

        System.out.println("\n=== HOÀN THÀNH ===");
    }
}
