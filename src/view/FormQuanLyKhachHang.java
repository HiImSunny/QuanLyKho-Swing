package view;

import dao.KhachHangDAO;
import model.KhachHang;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FormQuanLyKhachHang extends JFrame {
    
    private JTable tableKH;
    private DefaultTableModel tableModel;
    private JTextField txtMaKh, txtTenKh, txtDiaChi, txtSdt, txtEmail, txtTimKiem;
    private JComboBox<String> cboLoaiKh, cboTrangThai;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnTimKiem;
    
    private KhachHangDAO khachHangDAO;
    
    public FormQuanLyKhachHang() {
        khachHangDAO = new KhachHangDAO();
        initComponents();
        loadDataToTable();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("Quản lý khách hàng");
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
        panel.setBorder(BorderFactory.createTitledBorder("Thông tin khách hàng"));
        
        int yPos = 30;
        int labelWidth = 100;
        int fieldWidth = 220;
        int lineHeight = 35;
        
        // Mã KH (disabled)
        JLabel lblMaKh = new JLabel("Mã KH:");
        lblMaKh.setBounds(15, yPos, labelWidth, 25);
        panel.add(lblMaKh);
        
        txtMaKh = new JTextField();
        txtMaKh.setBounds(120, yPos, fieldWidth, 28);
        txtMaKh.setEnabled(false);
        txtMaKh.setBackground(new Color(240, 240, 240));
        panel.add(txtMaKh);
        
        yPos += lineHeight;
        
        // Tên KH
        JLabel lblTenKh = new JLabel("Tên KH:");
        lblTenKh.setBounds(15, yPos, labelWidth, 25);
        panel.add(lblTenKh);
        
        txtTenKh = new JTextField();
        txtTenKh.setBounds(120, yPos, fieldWidth, 28);
        panel.add(txtTenKh);
        
        yPos += lineHeight;
        
        // Loại KH
        JLabel lblLoaiKh = new JLabel("Loại KH:");
        lblLoaiKh.setBounds(15, yPos, labelWidth, 25);
        panel.add(lblLoaiKh);
        
        cboLoaiKh = new JComboBox<>(new String[]{"Cá nhân", "Doanh nghiệp"});
        cboLoaiKh.setBounds(120, yPos, fieldWidth, 28);
        panel.add(cboLoaiKh);
        
        yPos += lineHeight;
        
        // Địa chỉ
        JLabel lblDiaChi = new JLabel("Địa chỉ:");
        lblDiaChi.setBounds(15, yPos, labelWidth, 25);
        panel.add(lblDiaChi);
        
        txtDiaChi = new JTextField();
        txtDiaChi.setBounds(120, yPos, fieldWidth, 28);
        panel.add(txtDiaChi);
        
        yPos += lineHeight;
        
        // SĐT
        JLabel lblSdt = new JLabel("Số điện thoại:");
        lblSdt.setBounds(15, yPos, labelWidth, 25);
        panel.add(lblSdt);
        
        txtSdt = new JTextField();
        txtSdt.setBounds(120, yPos, fieldWidth, 28);
        panel.add(txtSdt);
        
        yPos += lineHeight;
        
        // Email
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setBounds(15, yPos, labelWidth, 25);
        panel.add(lblEmail);
        
        txtEmail = new JTextField();
        txtEmail.setBounds(120, yPos, fieldWidth, 28);
        panel.add(txtEmail);
        
        yPos += lineHeight;
        
        // Trạng thái
        JLabel lblTrangThai = new JLabel("Trạng thái:");
        lblTrangThai.setBounds(15, yPos, labelWidth, 25);
        panel.add(lblTrangThai);
        
        cboTrangThai = new JComboBox<>(new String[]{"Hoạt động", "Ngừng giao dịch"});
        cboTrangThai.setBounds(120, yPos, fieldWidth, 28);
        panel.add(cboTrangThai);
        
        yPos += 50;
        
        // Buttons
        btnThem = new JButton("Thêm");
        btnThem.setBounds(15, yPos, 100, 35);
        btnThem.setBackground(new Color(40, 167, 69));
        btnThem.setForeground(Color.BLACK);
        btnThem.setFocusPainted(false);
        btnThem.addActionListener(e -> themKH());
        panel.add(btnThem);
        
        btnSua = new JButton("Sửa");
        btnSua.setBounds(125, yPos, 100, 35);
        btnSua.setBackground(new Color(255, 193, 7));
        btnSua.setForeground(Color.BLACK);
        btnSua.setFocusPainted(false);
        btnSua.addActionListener(e -> suaKH());
        panel.add(btnSua);
        
        btnXoa = new JButton("Xóa");
        btnXoa.setBounds(235, yPos, 100, 35);
        btnXoa.setBackground(new Color(220, 53, 69));
        btnXoa.setForeground(Color.BLACK);
        btnXoa.setFocusPainted(false);
        btnXoa.addActionListener(e -> xoaKH());
        panel.add(btnXoa);
        
        yPos += 45;
        
        btnLamMoi = new JButton("Làm mới");
        btnLamMoi.setBounds(15, yPos, 320, 35);
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
        String[] columns = {"Mã KH", "Tên khách hàng", "Loại KH", "Địa chỉ", "SĐT", "Email", "Trạng thái"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableKH = new JTable(tableModel);
        tableKH.setRowHeight(25);
        tableKH.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableKH.getTableHeader().setReorderingAllowed(false);
        tableKH.setAutoCreateRowSorter(true);
        
        // Sự kiện click chọn row
        tableKH.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                hienThiThongTinKH();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tableKH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Load dữ liệu lên bảng
     */
    private void loadDataToTable() {
        tableModel.setRowCount(0);
        List<KhachHang> list = khachHangDAO.getAll();
        
        for (KhachHang kh : list) {
            String loaiKh = kh.getLoaiKh().equals("canhan") ? "Cá nhân" : "Doanh nghiệp";
            String trangThai = kh.getTrangThai() == 1 ? "Hoạt động" : "Ngừng giao dịch";
            
            Object[] row = {
                kh.getMaKh(),
                kh.getTenKh(),
                loaiKh,
                kh.getDiaChi(),
                kh.getSdt(),
                kh.getEmail(),
                trangThai
            };
            tableModel.addRow(row);
        }
    }
    
    /**
     * Hiển thị thông tin KH khi click vào table
     */
    private void hienThiThongTinKH() {
        int selectedRow = tableKH.getSelectedRow();
        if (selectedRow >= 0) {
            txtMaKh.setText(tableModel.getValueAt(selectedRow, 0).toString());
            txtTenKh.setText(tableModel.getValueAt(selectedRow, 1).toString());
            
            String loaiKh = tableModel.getValueAt(selectedRow, 2).toString();
            cboLoaiKh.setSelectedIndex(loaiKh.equals("Cá nhân") ? 0 : 1);
            
            Object diaChi = tableModel.getValueAt(selectedRow, 3);
            txtDiaChi.setText(diaChi != null ? diaChi.toString() : "");
            
            Object sdt = tableModel.getValueAt(selectedRow, 4);
            txtSdt.setText(sdt != null ? sdt.toString() : "");
            
            Object email = tableModel.getValueAt(selectedRow, 5);
            txtEmail.setText(email != null ? email.toString() : "");
            
            String trangThai = tableModel.getValueAt(selectedRow, 6).toString();
            cboTrangThai.setSelectedIndex(trangThai.equals("Hoạt động") ? 0 : 1);
        }
    }
    
    /**
     * Thêm khách hàng
     */
    private void themKH() {
        if (!validateInput()) return;
        
        KhachHang kh = new KhachHang();
        kh.setTenKh(txtTenKh.getText().trim());
        kh.setLoaiKh(cboLoaiKh.getSelectedIndex() == 0 ? "canhan" : "doanhnghiep");
        kh.setDiaChi(txtDiaChi.getText().trim());
        kh.setSdt(txtSdt.getText().trim());
        kh.setEmail(txtEmail.getText().trim());
        kh.setTrangThai(cboTrangThai.getSelectedIndex() == 0 ? 1 : 0);
        
        if (khachHangDAO.insert(kh)) {
            JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công!");
            loadDataToTable();
            lamMoi();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm khách hàng thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Sửa khách hàng
     */
    private void suaKH() {
        if (txtMaKh.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần sửa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!validateInput()) return;
        
        KhachHang kh = new KhachHang();
        kh.setMaKh(Integer.parseInt(txtMaKh.getText()));
        kh.setTenKh(txtTenKh.getText().trim());
        kh.setLoaiKh(cboLoaiKh.getSelectedIndex() == 0 ? "canhan" : "doanhnghiep");
        kh.setDiaChi(txtDiaChi.getText().trim());
        kh.setSdt(txtSdt.getText().trim());
        kh.setEmail(txtEmail.getText().trim());
        kh.setTrangThai(cboTrangThai.getSelectedIndex() == 0 ? 1 : 0);
        
        if (khachHangDAO.update(kh)) {
            JOptionPane.showMessageDialog(this, "Cập nhật khách hàng thành công!");
            loadDataToTable();
            lamMoi();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật khách hàng thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Xóa khách hàng
     */
    private void xoaKH() {
        if (txtMaKh.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int choice = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc muốn xóa khách hàng này?", 
            "Xác nhận", 
            JOptionPane.YES_NO_OPTION);
        
        if (choice == JOptionPane.YES_OPTION) {
            int maKh = Integer.parseInt(txtMaKh.getText());
            
            if (khachHangDAO.delete(maKh)) {
                JOptionPane.showMessageDialog(this, "Xóa khách hàng thành công!");
                loadDataToTable();
                lamMoi();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Xóa khách hàng thất bại!\nCó thể KH này đã có phiếu xuất.", 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            }
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
        List<KhachHang> list = khachHangDAO.search(keyword);
        
        for (KhachHang kh : list) {
            String loaiKh = kh.getLoaiKh().equals("canhan") ? "Cá nhân" : "Doanh nghiệp";
            String trangThai = kh.getTrangThai() == 1 ? "Hoạt động" : "Ngừng giao dịch";
            
            Object[] row = {
                kh.getMaKh(),
                kh.getTenKh(),
                loaiKh,
                kh.getDiaChi(),
                kh.getSdt(),
                kh.getEmail(),
                trangThai
            };
            tableModel.addRow(row);
        }
        
        JOptionPane.showMessageDialog(this, "Tìm thấy " + list.size() + " khách hàng!");
    }
    
    /**
     * Làm mới form
     */
    private void lamMoi() {
        txtMaKh.setText("");
        txtTenKh.setText("");
        txtDiaChi.setText("");
        txtSdt.setText("");
        txtEmail.setText("");
        txtTimKiem.setText("");
        cboLoaiKh.setSelectedIndex(0);
        cboTrangThai.setSelectedIndex(0);
        tableKH.clearSelection();
        loadDataToTable();
    }
    
    /**
     * Validate input
     */
    private boolean validateInput() {
        if (txtTenKh.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên khách hàng!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtTenKh.requestFocus();
            return false;
        }
        return true;
    }
    
}
