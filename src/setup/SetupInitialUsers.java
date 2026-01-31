package setup;

import dao.UserDAO;
import model.User;

// Chạy để tạo user mẫu
public class SetupInitialUsers {
    
    public static void main(String[] args) {
        UserDAO userDAO = new UserDAO();
        
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword("admin");
        admin.setHoTen("Admin");
        admin.setRole("admin");
        
        if (userDAO.insert(admin)) {
            System.out.println("✓ Tạo admin thành công");
        } else {
            System.out.println("✗ Lỗi tạo admin");
        }
        
        User nv1 = new User();
        nv1.setUsername("nvkho01");
        nv1.setPassword("123456");
        nv1.setHoTen("Lương Duy Khang");
        nv1.setRole("nhanvien");
        
        if (userDAO.insert(nv1)) {
            System.out.println("✓ Tạo nvkho01 thành công");
        } else {
            System.out.println("✗ Lỗi tạo nvkho01");
        }
        
        User nv2 = new User();
        nv2.setUsername("nvkho02");
        nv2.setPassword("123456");
        nv2.setHoTen("Phan Minh Khôi");
        nv2.setRole("nhanvien");
        
        if (userDAO.insert(nv2)) {
            System.out.println("✓ Tạo nvkho02 thành công");
        } else {
            System.out.println("✗ Lỗi tạo nvkho02");
        }
        
        System.out.println("\n=== Setup hoàn tất ===");
        System.out.println("Đăng nhập với:");
        System.out.println("  Username: admin    | Password: admin");
        System.out.println("  Username: nvkho01  | Password: 123456");
        System.out.println("  Username: nvkho02  | Password: 123456");
    }
}
