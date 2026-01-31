package view;

import dao.UserDAO;
import model.User;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FormQuanLyNguoiDung extends JFrame {
    
    private JTable tableUser;
    private DefaultTableModel tableModel;
    private JTextField txtUserId, txtUsername, txtHoTen, txtTimKiem;
    private JPasswordField txtPassword, txtConfirmPassword;
    private JComboBox<String> cboRole, cboTrangThai;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnTimKiem, btnDoiMatKhau;
    
    private UserDAO userDAO;
    private User currentUser; // User đang đăng nhập
    
    public FormQuanLyNguoiDung(User currentUser) {
        this.currentUser = currentUser;
        
        // Kiểm tra quyền truy cập
        if (!currentUser.getRole().equals("admin")) {
            JOptionPane.showMessageDialog(null, 
                "Bạn không có quyền truy cập chức năng này!", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }
        
        userDAO = new UserDAO();
        initComponents();
        loadDataToTable();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("Quản lý người dùng");
        setSize(1100, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        // Panel nhập liệu bên trái
        JPanel leftPanel = createInputPanel();
        add(leftPanel, BorderLayout.WEST);
        
        // Panel bảng bên phải
        JPanel rightPanel = createTablePanel();
        add(rightPanel, BorderLayout.CENTER);
    }
    
    /**
     * Tạo panel nhập liệu
     */
    private JPanel createInputPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(350, 600));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder("Thông tin người dùng"));
        
        int yPos = 30;
        int labelWidth = 120;
        int fieldWidth = 200;
        int lineHeight = 35;
        
        // User ID (disabled)
        JLabel lblUserId = new JLabel("ID:");
        lblUserId.setBounds(15, yPos, labelWidth, 25);
        panel.add(lblUserId);
        
        txtUserId = new JTextField();
        txtUserId.setBounds(140, yPos, fieldWidth, 28);
        txtUserId.setEnabled(false);
        txtUserId.setBackground(new Color(240, 240, 240));
        panel.add(txtUserId);
        
        yPos += lineHeight;
        
        // Username
        JLabel lblUsername = new JLabel("Tên đăng nhập:");
        lblUsername.setBounds(15, yPos, labelWidth, 25);
        panel.add(lblUsername);
        
        txtUsername = new JTextField();
        txtUsername.setBounds(140, yPos, fieldWidth, 28);
        panel.add(txtUsername);
        
        yPos += lineHeight;
        
        // Họ tên
        JLabel lblHoTen = new JLabel("Họ và tên:");
        lblHoTen.setBounds(15, yPos, labelWidth, 25);
        panel.add(lblHoTen);
        
        txtHoTen = new JTextField();
        txtHoTen.setBounds(140, yPos, fieldWidth, 28);
        panel.add(txtHoTen);
        
        yPos += lineHeight;
        
        // Mật khẩu
        JLabel lblPassword = new JLabel("Mật khẩu:");
        lblPassword.setBounds(15, yPos, labelWidth, 25);
        panel.add(lblPassword);
        
        txtPassword = new JPasswordField();
        txtPassword.setBounds(140, yPos, fieldWidth, 28);
        panel.add(txtPassword);
        
        yPos += lineHeight;
        
        // Xác nhận mật khẩu
        JLabel lblConfirmPassword = new JLabel("Xác nhận MK:");
        lblConfirmPassword.setBounds(15, yPos, labelWidth, 25);
        panel.add(lblConfirmPassword);
        
        txtConfirmPassword = new JPasswordField();
        txtConfirmPassword.setBounds(140, yPos, fieldWidth, 28);
        panel.add(txtConfirmPassword);
        
        yPos += lineHeight;
        
        // Vai trò
        JLabel lblRole = new JLabel("Vai trò:");
        lblRole.setBounds(15, yPos, labelWidth, 25);
        panel.add(lblRole);
        
        cboRole = new JComboBox<>(new String[]{"nhanvien", "admin"});
        cboRole.setBounds(140, yPos, fieldWidth, 28);
        panel.add(cboRole);
        
        yPos += lineHeight;
        
        // Trạng thái
        JLabel lblTrangThai = new JLabel("Trạng thái:");
        lblTrangThai.setBounds(15, yPos, labelWidth, 25);
        panel.add(lblTrangThai);
        
        cboTrangThai = new JComboBox<>(new String[]{"Hoạt động", "Vô hiệu hóa"});
        cboTrangThai.setBounds(140, yPos, fieldWidth, 28);
        panel.add(cboTrangThai);
        
        yPos += 50;
        
        // Buttons
        btnThem = new JButton("Thêm");
        btnThem.setBounds(15, yPos, 100, 35);
        btnThem.setBackground(new Color(40, 167, 69));
        btnThem.setForeground(Color.BLACK);
        btnThem.setFocusPainted(false);
        btnThem.addActionListener(e -> themUser());
        panel.add(btnThem);
        
        btnSua = new JButton("Sửa");
        btnSua.setBounds(125, yPos, 100, 35);
        btnSua.setBackground(new Color(255, 193, 7));
        btnSua.setForeground(Color.BLACK);
        btnSua.setFocusPainted(false);
        btnSua.addActionListener(e -> suaUser());
        panel.add(btnSua);
        
        btnXoa = new JButton("Xóa");
        btnXoa.setBounds(235, yPos, 100, 35);
        btnXoa.setBackground(new Color(220, 53, 69));
        btnXoa.setForeground(Color.BLACK);
        btnXoa.setFocusPainted(false);
        btnXoa.addActionListener(e -> xoaUser());
        panel.add(btnXoa);
        
        yPos += 45;
        
        btnDoiMatKhau = new JButton("Đổi mật khẩu");
        btnDoiMatKhau.setBounds(15, yPos, 150, 35);
        btnDoiMatKhau.setBackground(new Color(23, 162, 184));
        btnDoiMatKhau.setForeground(Color.BLACK);
        btnDoiMatKhau.setFocusPainted(false);
        btnDoiMatKhau.addActionListener(e -> doiMatKhau());
        panel.add(btnDoiMatKhau);
        
        btnLamMoi = new JButton("Làm mới");
        btnLamMoi.setBounds(175, yPos, 160, 35);
        btnLamMoi.setBackground(new Color(108, 117, 125));
        btnLamMoi.setForeground(Color.BLACK);
        btnLamMoi.setFocusPainted(false);
        btnLamMoi.addActionListener(e -> lamMoi());
        panel.add(btnLamMoi);
        
        return panel;
    }
    
    /**
     * Tạo panel bảng
     */
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel tìm kiếm
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.WHITE);
        
        JLabel lblTimKiem = new JLabel("Tìm kiếm:");
        searchPanel.add(lblTimKiem);
        
        txtTimKiem = new JTextField(25);
        searchPanel.add(txtTimKiem);
        
        btnTimKiem = new JButton("Tìm");
        btnTimKiem.setBackground(new Color(0, 123, 255));
        btnTimKiem.setForeground(Color.BLACK);
        btnTimKiem.setFocusPainted(false);
        btnTimKiem.addActionListener(e -> timKiem());
        searchPanel.add(btnTimKiem);
        
        panel.add(searchPanel, BorderLayout.NORTH);
        
        // Table
        String[] columns = {"ID", "Tên đăng nhập", "Họ và tên", "Vai trò", "Trạng thái", "Ngày tạo"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableUser = new JTable(tableModel);
        tableUser.setRowHeight(25);
        tableUser.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableUser.getTableHeader().setReorderingAllowed(false);
        tableUser.setAutoCreateRowSorter(true);
        
        // Sự kiện click chọn row
        tableUser.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                hienThiThongTinUser();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tableUser);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Load dữ liệu lên bảng
     */
    private void loadDataToTable() {
        tableModel.setRowCount(0);
        List<User> list = userDAO.getAll();
        
        for (User user : list) {
            String role = user.getRole().equals("admin") ? "Admin" : "Nhân viên";
            String trangThai = user.getTrangThai() == 1 ? "Hoạt động" : "Vô hiệu hóa";
            
            Object[] row = {
                user.getId(),
                user.getUsername(),
                user.getHoTen(),
                role,
                trangThai,
                user.getNgayTao()
            };
            tableModel.addRow(row);
        }
    }
    
    /**
     * Hiển thị thông tin user khi click vào table
     */
    private void hienThiThongTinUser() {
        int selectedRow = tableUser.getSelectedRow();
        if (selectedRow >= 0) {
            txtUserId.setText(tableModel.getValueAt(selectedRow, 0).toString());
            txtUsername.setText(tableModel.getValueAt(selectedRow, 1).toString());
            txtUsername.setEnabled(false); // Không cho sửa username
            txtHoTen.setText(tableModel.getValueAt(selectedRow, 2).toString());
            
            String role = tableModel.getValueAt(selectedRow, 3).toString();
            cboRole.setSelectedIndex(role.equals("Admin") ? 1 : 0);
            
            String trangThai = tableModel.getValueAt(selectedRow, 4).toString();
            cboTrangThai.setSelectedIndex(trangThai.equals("Hoạt động") ? 0 : 1);
            
            // Xóa password cũ
            txtPassword.setText("");
            txtConfirmPassword.setText("");
        }
    }
    
    /**
     * Thêm user
     */
    private void themUser() {
        if (!validateInput(true)) return;
        
        // Kiểm tra username đã tồn tại
        if (userDAO.isUsernameExist(txtUsername.getText().trim())) {
            JOptionPane.showMessageDialog(this, "Tên đăng nhập đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        User user = new User();
        user.setUsername(txtUsername.getText().trim());
        user.setPassword(new String(txtPassword.getPassword()));
        user.setHoTen(txtHoTen.getText().trim());
        user.setRole(cboRole.getSelectedIndex() == 0 ? "nhanvien" : "admin");
        user.setTrangThai(cboTrangThai.getSelectedIndex() == 0 ? 1 : 0);
        
        if (userDAO.insert(user)) {
            JOptionPane.showMessageDialog(this, "Thêm người dùng thành công!");
            loadDataToTable();
            lamMoi();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm người dùng thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Sửa user
     */
    private void suaUser() {
        if (txtUserId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn người dùng cần sửa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!validateInput(false)) return;
        
        User user = new User();
        user.setId(Integer.parseInt(txtUserId.getText()));
        user.setHoTen(txtHoTen.getText().trim());
        user.setRole(cboRole.getSelectedIndex() == 0 ? "nhanvien" : "admin");
        user.setTrangThai(cboTrangThai.getSelectedIndex() == 0 ? 1 : 0);
        
        if (userDAO.update(user)) {
            JOptionPane.showMessageDialog(this, "Cập nhật người dùng thành công!");
            loadDataToTable();
            lamMoi();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật người dùng thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Xóa user
     */
    private void xoaUser() {
        if (txtUserId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn người dùng cần xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int userId = Integer.parseInt(txtUserId.getText());
        
        // Không cho xóa chính mình
        if (userId == currentUser.getId()) {
            JOptionPane.showMessageDialog(this, "Không thể xóa tài khoản đang đăng nhập!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int choice = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc muốn xóa người dùng này?", 
            "Xác nhận", 
            JOptionPane.YES_NO_OPTION);
        
        if (choice == JOptionPane.YES_OPTION) {
            if (userDAO.delete(userId)) {
                JOptionPane.showMessageDialog(this, "Xóa người dùng thành công!");
                loadDataToTable();
                lamMoi();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa người dùng thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Đổi mật khẩu
     */
    private void doiMatKhau() {
        if (txtUserId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn người dùng cần đổi mật khẩu!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String password = new String(txtPassword.getPassword()).trim();
        String confirmPassword = new String(txtConfirmPassword.getPassword()).trim();
        
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mật khẩu mới!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Mật khẩu xác nhận không khớp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int userId = Integer.parseInt(txtUserId.getText());
        
        if (userDAO.changePassword(userId, password)) {
            JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công!");
            txtPassword.setText("");
            txtConfirmPassword.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Đổi mật khẩu thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Tìm kiếm
     */
    private void timKiem() {
        String keyword = txtTimKiem.getText().trim();
        
        if (keyword.isEmpty()) {
            loadDataToTable();
            return;
        }
        
        tableModel.setRowCount(0);
        List<User> list = userDAO.search(keyword);
        
        for (User user : list) {
            String role = user.getRole().equals("admin") ? "Admin" : "Nhân viên";
            String trangThai = user.getTrangThai() == 1 ? "Hoạt động" : "Vô hiệu hóa";
            
            Object[] row = {
                user.getId(),
                user.getUsername(),
                user.getHoTen(),
                role,
                trangThai,
                user.getNgayTao()
            };
            tableModel.addRow(row);
        }
        
        JOptionPane.showMessageDialog(this, "Tìm thấy " + list.size() + " người dùng!");
    }
    
    /**
     * Làm mới form
     */
    private void lamMoi() {
        txtUserId.setText("");
        txtUsername.setText("");
        txtUsername.setEnabled(true);
        txtHoTen.setText("");
        txtPassword.setText("");
        txtConfirmPassword.setText("");
        txtTimKiem.setText("");
        cboRole.setSelectedIndex(0);
        cboTrangThai.setSelectedIndex(0);
        tableUser.clearSelection();
        loadDataToTable();
    }
    
    /**
     * Validate input
     */
    private boolean validateInput(boolean isInsert) {
        if (txtUsername.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên đăng nhập!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        if (txtHoTen.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập họ và tên!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        // Chỉ kiểm tra password khi thêm mới
        if (isInsert) {
            String password = new String(txtPassword.getPassword()).trim();
            String confirmPassword = new String(txtConfirmPassword.getPassword()).trim();
            
            if (password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập mật khẩu!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            
            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Mật khẩu xác nhận không khớp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        
        return true;
    }
}
