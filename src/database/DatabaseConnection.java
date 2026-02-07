package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/qlkho_db?useSSL=false&serverTimezone=Asia/Ho_Chi_Minh&useUnicode=true&characterEncoding=UTF-8";
    private static final String USER = "root";
    private static final String PASS = "";

    private static Connection connection = null;

    private DatabaseConnection() {
    }

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(DB_URL, USER, PASS);
                System.out.println("✓ Kết nối database thành công!");
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

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✓ Đã đóng kết nối database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
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

}
