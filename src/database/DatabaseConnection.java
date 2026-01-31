package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    
    private static final String DB_URL = "jdbc:mysql://localhost:3306/qlkho_db?useSSL=false&serverTimezone=Asia/Ho_Chi_Minh";
    private static final String USER = "root";
    private static final String PASS = "";
    
    private static Connection connection = null;
    
    private DatabaseConnection() {}
    
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

}
