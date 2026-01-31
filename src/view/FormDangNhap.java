package view;

import dao.UserDAO;
import model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FormDangNhap extends JFrame {
    
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnExit;
    private UserDAO userDAO;
    
    public FormDangNhap() {
        userDAO = new UserDAO();
        initComponents();
        setLocationRelativeTo(null); // Giữa màn hình
    }
    
    private void initComponents() {
        // Cấu hình JFrame
        setTitle("Đăng nhập - Quản lý kho hàng");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(new Color(240, 240, 240));
        
        // Title Label
        JLabel lblTitle = new JLabel("ĐĂNG NHẬP HỆ THỐNG", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setBounds(50, 20, 300, 30);
        mainPanel.add(lblTitle);
        
        // Username Label
        JLabel lblUsername = new JLabel("Tên đăng nhập:");
        lblUsername.setFont(new Font("Arial", Font.PLAIN, 14));
        lblUsername.setBounds(50, 70, 120, 25);
        mainPanel.add(lblUsername);
        
        // Username TextField
        txtUsername = new JTextField();
        txtUsername.setFont(new Font("Arial", Font.PLAIN, 14));
        txtUsername.setBounds(170, 70, 180, 30);
        mainPanel.add(txtUsername);
        
        // Password Label
        JLabel lblPassword = new JLabel("Mật khẩu:");
        lblPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        lblPassword.setBounds(50, 115, 120, 25);
        mainPanel.add(lblPassword);
        
        // Password Field
        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        txtPassword.setBounds(170, 115, 180, 30);
        mainPanel.add(txtPassword);
        
        // Login Button
        btnLogin = new JButton("Đăng nhập");
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.setBounds(100, 165, 120, 35);
        btnLogin.setBackground(new Color(0, 123, 255));
        btnLogin.setForeground(Color.BLACK);
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
        mainPanel.add(btnLogin);
        
        // Exit Button
        btnExit = new JButton("Thoát");
        btnExit.setFont(new Font("Arial", Font.PLAIN, 14));
        btnExit.setBounds(230, 165, 120, 35);
        btnExit.setBackground(new Color(220, 53, 69));
        btnExit.setForeground(Color.BLACK);
        btnExit.setFocusPainted(false);
        btnExit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        mainPanel.add(btnExit);
        
        // Enter key để login
        txtPassword.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleLogin();
                }
            }
        });
        
        add(mainPanel);
    }
    
    /**
     * Xử lý đăng nhập
     */
    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng nhập tên đăng nhập!", 
                "Lỗi", 
                JOptionPane.WARNING_MESSAGE);
            txtUsername.requestFocus();
            return;
        }
        
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng nhập mật khẩu!", 
                "Lỗi", 
                JOptionPane.WARNING_MESSAGE);
            txtPassword.requestFocus();
            return;
        }
        
        User user = userDAO.login(username, password);
        
        if (user != null) {
            // Đăng nhập thành công - Mở FormMain
            new FormMain(user).setVisible(true);
            this.dispose(); // Đóng form đăng nhập
            
        } else {
            JOptionPane.showMessageDialog(this, 
                "Sai tên đăng nhập hoặc mật khẩu!", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            txtPassword.setText("");
            txtUsername.requestFocus();
        }
    }
    
    /**
     * Main method để chạy form
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FormDangNhap().setVisible(true);
            }
        });
    }
}
