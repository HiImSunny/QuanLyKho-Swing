package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/qlkho_db?useSSL=false&serverTimezone=Asia/Ho_Chi_Minh&useUnicode=true&characterEncoding=UTF-8";
    private static final String USER = "root";
    private static final String PASS = "";

    private static Connection connection = null;
    private static boolean hasLoggedConnection = false;

    private DatabaseConnection() {
    }

    /**
     * Lấy connection singleton (giữ lại để tương thích code cũ)
     * Lưu ý: Connection này không tự động đóng, dùng cho các operation đơn giản
     */
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(DB_URL, USER, PASS);
                if (!hasLoggedConnection) {
                    System.out.println("✓ Kết nối database thành công!");
                    hasLoggedConnection = true;
                }
            }
        } catch (ClassNotFoundException e) {
            System.err.println("✗ Không tìm thấy MySQL JDBC Driver!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("✗ Lỗi kết nối database!");
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Tạo connection mới - nên dùng với try-with-resources để tự động đóng
     * Ví dụ:
     * try (Connection conn = DatabaseConnection.getNewConnection()) {
     * // sử dụng conn
     * } // tự động đóng khi kết thúc block
     */
    public static Connection getNewConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Không tìm thấy MySQL JDBC Driver!", e);
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                connection = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Đóng connection một cách an toàn (dùng cho các connection riêng lẻ)
     */
    public static void closeQuietly(Connection conn) {
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                // Ignore
            }
        }
    }

    /**
     * Kiểm tra kết nối database
     * 
     * @throws SQLException nếu không kết nối được
     */
    public static void testConnection() throws SQLException {
        Connection conn = getConnection();
        if (conn == null || conn.isClosed()) {
            throw new SQLException("Không thể kết nối đến database. Vui lòng kiểm tra:\n" +
                    "1. MySQL Server đã được khởi động chưa\n" +
                    "2. Database 'qlkho_db' đã được tạo chưa\n" +
                    "3. Thông tin kết nối (host, port, user, password) có đúng không");
        }
    }

    /**
     * Lấy chuỗi kết nối database (dùng cho backup/restore)
     */
    public static String getDbUrl() {
        return DB_URL;
    }

    public static String getDbUser() {
        return USER;
    }

    public static String getDbPassword() {
        return PASS;
    }

    public static String getDbName() {
        return "qlkho_db";
    }

}
