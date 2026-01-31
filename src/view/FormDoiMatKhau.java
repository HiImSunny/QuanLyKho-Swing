package view;

import dao.UserDAO;
import model.User;
import utils.BCryptHelper;
import javax.swing.*;
import java.awt.*;

public class FormDoiMatKhau extends JFrame {
    
    private JPasswordField txtMatKhauCu, txtMatKhauMoi, txtXacNhanMatKhau;
    private JButton btnDoiMatKhau, btnHuy;
    private JLabel lblUsername, lblHoTen;
    
    private User currentUser;
    private UserDAO userDAO;
    
    public FormDoiMatKhau(User currentUser) {
        this.currentUser = currentUser;
        this.userDAO = new UserDAO();
        
        initComponents();
        loadUserInfo();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("Đổi mật khẩu");
        setSize(450, 380);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);
        
        // Tiêu đề
        JLabel lblTitle = new JLabel("ĐỔI MẬT KHẨU");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setBounds(150, 15, 200, 30);
        add(lblTitle);
        
        int yPos = 60;
        int labelWidth = 150;
        int fieldWidth = 220;
        int lineHeight = 45;
        
        // Thông tin user (chỉ hiển thị, không cho sửa)
        JLabel lblUserLabel = new JLabel("Tên đăng nhập:");
        lblUserLabel.setBounds(30, yPos, labelWidth, 25);
        add(lblUserLabel);
        
        lblUsername = new JLabel();
        lblUsername.setBounds(180, yPos, fieldWidth, 25);
        lblUsername.setFont(new Font("Arial", Font.BOLD, 13));
        add(lblUsername);
        
        yPos += 30;
        
        JLabel lblHoTenLabel = new JLabel("Họ và tên:");
        lblHoTenLabel.setBounds(30, yPos, labelWidth, 25);
        add(lblHoTenLabel);
        
        lblHoTen = new JLabel();
        lblHoTen.setBounds(180, yPos, fieldWidth, 25);
        lblHoTen.setFont(new Font("Arial", Font.BOLD, 13));
        add(lblHoTen);
        
        yPos += 40;
        
        // Separator
        JSeparator separator = new JSeparator();
        separator.setBounds(30, yPos, 370, 2);
        add(separator);
        
        yPos += 15;
        
        // Mật khẩu cũ
        JLabel lblMatKhauCu = new JLabel("Mật khẩu cũ:");
        lblMatKhauCu.setBounds(30, yPos, labelWidth, 25);
        add(lblMatKhauCu);
        
        txtMatKhauCu = new JPasswordField();
        txtMatKhauCu.setBounds(180, yPos, fieldWidth, 30);
        add(txtMatKhauCu);
        
        yPos += lineHeight;
        
        // Mật khẩu mới
        JLabel lblMatKhauMoi = new JLabel("Mật khẩu mới:");
        lblMatKhauMoi.setBounds(30, yPos, labelWidth, 25);
        add(lblMatKhauMoi);
        
        txtMatKhauMoi = new JPasswordField();
        txtMatKhauMoi.setBounds(180, yPos, fieldWidth, 30);
        add(txtMatKhauMoi);
        
        yPos += lineHeight;
        
        // Xác nhận mật khẩu mới
        JLabel lblXacNhan = new JLabel("Xác nhận mật khẩu:");
        lblXacNhan.setBounds(30, yPos, labelWidth, 25);
        add(lblXacNhan);
        
        txtXacNhanMatKhau = new JPasswordField();
        txtXacNhanMatKhau.setBounds(180, yPos, fieldWidth, 30);
        add(txtXacNhanMatKhau);
        
        yPos += 55;
        
        // Buttons
        btnDoiMatKhau = new JButton("Đổi mật khẩu");
        btnDoiMatKhau.setBounds(120, yPos, 130, 35);
        btnDoiMatKhau.setBackground(new Color(40, 167, 69));
        btnDoiMatKhau.setForeground(Color.BLACK);
        btnDoiMatKhau.setFocusPainted(false);
        btnDoiMatKhau.setFont(new Font("Arial", Font.BOLD, 12));
        btnDoiMatKhau.addActionListener(e -> doiMatKhau());
        add(btnDoiMatKhau);
        
        btnHuy = new JButton("Hủy");
        btnHuy.setBounds(260, yPos, 90, 35);
        btnHuy.setBackground(new Color(108, 117, 125));
        btnHuy.setForeground(Color.BLACK);
        btnHuy.setFocusPainted(false);
        btnHuy.setFont(new Font("Arial", Font.BOLD, 12));
        btnHuy.addActionListener(e -> dispose());
        add(btnHuy);
    }
    
    /**
     * Load thông tin user hiện tại
     */
    private void loadUserInfo() {
        lblUsername.setText(currentUser.getUsername());
        lblHoTen.setText(currentUser.getHoTen());
    }
    
    /**
     * Đổi mật khẩu
     */
    private void doiMatKhau() {
        // Validate input
        if (!validateInput()) {
            return;
        }
        
        String matKhauCu = new String(txtMatKhauCu.getPassword()).trim();
        String matKhauMoi = new String(txtMatKhauMoi.getPassword()).trim();
        
        // Kiểm tra mật khẩu cũ có đúng không
        if (!BCryptHelper.checkPassword(matKhauCu, currentUser.getPassword())) {
            JOptionPane.showMessageDialog(this, 
                "Mật khẩu cũ không đúng!", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            txtMatKhauCu.setText("");
            txtMatKhauCu.requestFocus();
            return;
        }
        
        // Kiểm tra mật khẩu mới không được trùng mật khẩu cũ
        if (matKhauCu.equals(matKhauMoi)) {
            JOptionPane.showMessageDialog(this, 
                "Mật khẩu mới không được trùng với mật khẩu cũ!", 
                "Cảnh báo", 
                JOptionPane.WARNING_MESSAGE);
            txtMatKhauMoi.setText("");
            txtXacNhanMatKhau.setText("");
            txtMatKhauMoi.requestFocus();
            return;
        }
        
        // Đổi mật khẩu
        if (userDAO.changePassword(currentUser.getId(), matKhauMoi)) {
            JOptionPane.showMessageDialog(this, 
                "Đổi mật khẩu thành công!\nVui lòng đăng nhập lại với mật khẩu mới.", 
                "Thành công", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // Đóng form và logout
            dispose();
            
            // Tìm FormMain và thực hiện logout
            for (Window window : Window.getWindows()) {
                if (window instanceof FormMain) {
                    ((FormMain) window).dangXuat();
                    break;
                }
            }
            
        } else {
            JOptionPane.showMessageDialog(this, 
                "Đổi mật khẩu thất bại!", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Validate input
     */
    private boolean validateInput() {
        String matKhauCu = new String(txtMatKhauCu.getPassword()).trim();
        String matKhauMoi = new String(txtMatKhauMoi.getPassword()).trim();
        String xacNhan = new String(txtXacNhanMatKhau.getPassword()).trim();
        
        if (matKhauCu.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng nhập mật khẩu cũ!", 
                "Cảnh báo", 
                JOptionPane.WARNING_MESSAGE);
            txtMatKhauCu.requestFocus();
            return false;
        }
        
        if (matKhauMoi.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng nhập mật khẩu mới!", 
                "Cảnh báo", 
                JOptionPane.WARNING_MESSAGE);
            txtMatKhauMoi.requestFocus();
            return false;
        }
        
        if (matKhauMoi.length() < 6) {
            JOptionPane.showMessageDialog(this, 
                "Mật khẩu mới phải có ít nhất 6 ký tự!", 
                "Cảnh báo", 
                JOptionPane.WARNING_MESSAGE);
            txtMatKhauMoi.requestFocus();
            return false;
        }
        
        if (xacNhan.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng xác nhận mật khẩu mới!", 
                "Cảnh báo", 
                JOptionPane.WARNING_MESSAGE);
            txtXacNhanMatKhau.requestFocus();
            return false;
        }
        
        if (!matKhauMoi.equals(xacNhan)) {
            JOptionPane.showMessageDialog(this, 
                "Mật khẩu xác nhận không khớp!", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            txtXacNhanMatKhau.setText("");
            txtXacNhanMatKhau.requestFocus();
            return false;
        }
        
        return true;
    }
}
