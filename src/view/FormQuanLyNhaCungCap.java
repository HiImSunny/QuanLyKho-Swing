package view;

import dao.NhaCungCapDAO;
import model.NhaCungCap;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FormQuanLyNhaCungCap extends JFrame {
    
    private JTable tableNCC;
    private DefaultTableModel tableModel;
    private JTextField txtMaNcc, txtTenNcc, txtDiaChi, txtSdt, txtEmail, txtNguoiLienHe, txtTimKiem;
    private JComboBox<String> cboTrangThai;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnTimKiem;
    
    private NhaCungCapDAO nhaCungCapDAO;
    
    public FormQuanLyNhaCungCap() {
        nhaCungCapDAO = new NhaCungCapDAO();
        initComponents();
        loadDataToTable();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("Quản lý nhà cung cấp");
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
        panel.setBorder(BorderFactory.createTitledBorder("Thông tin nhà cung cấp"));
        
        int yPos = 30;
        int labelWidth = 100;
        int fieldWidth = 220;
        int lineHeight = 35;
        
        // Mã NCC (disabled)
        JLabel lblMaNcc = new JLabel("Mã NCC:");
        lblMaNcc.setBounds(15, yPos, labelWidth, 25);
        panel.add(lblMaNcc);
        
        txtMaNcc = new JTextField();
        txtMaNcc.setBounds(120, yPos, fieldWidth, 28);
        txtMaNcc.setEnabled(false);
        txtMaNcc.setBackground(new Color(240, 240, 240));
        panel.add(txtMaNcc);
        
        yPos += lineHeight;
        
        // Tên NCC
        JLabel lblTenNcc = new JLabel("Tên NCC:");
        lblTenNcc.setBounds(15, yPos, labelWidth, 25);
        panel.add(lblTenNcc);
        
        txtTenNcc = new JTextField();
        txtTenNcc.setBounds(120, yPos, fieldWidth, 28);
        panel.add(txtTenNcc);
        
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
        
        // Người liên hệ
        JLabel lblNguoiLienHe = new JLabel("Người liên hệ:");
        lblNguoiLienHe.setBounds(15, yPos, labelWidth, 25);
        panel.add(lblNguoiLienHe);
        
        txtNguoiLienHe = new JTextField();
        txtNguoiLienHe.setBounds(120, yPos, fieldWidth, 28);
        panel.add(txtNguoiLienHe);
        
        yPos += lineHeight;
        
        // Trạng thái
        JLabel lblTrangThai = new JLabel("Trạng thái:");
        lblTrangThai.setBounds(15, yPos, labelWidth, 25);
        panel.add(lblTrangThai);
        
        cboTrangThai = new JComboBox<>(new String[]{"Hoạt động", "Ngừng hợp tác"});
        cboTrangThai.setBounds(120, yPos, fieldWidth, 28);
        panel.add(cboTrangThai);
        
        yPos += 50;
        
        // Buttons
        btnThem = new JButton("Thêm");
        btnThem.setBounds(15, yPos, 100, 35);
        btnThem.setBackground(new Color(40, 167, 69));
        btnThem.setForeground(Color.BLACK);
        btnThem.setFocusPainted(false);
        btnThem.addActionListener(e -> themNCC());
        panel.add(btnThem);
        
        btnSua = new JButton("Sửa");
        btnSua.setBounds(125, yPos, 100, 35);
        btnSua.setBackground(new Color(255, 193, 7));
        btnSua.setForeground(Color.BLACK);
        btnSua.setFocusPainted(false);
        btnSua.addActionListener(e -> suaNCC());
        panel.add(btnSua);
        
        btnXoa = new JButton("Xóa");
        btnXoa.setBounds(235, yPos, 100, 35);
        btnXoa.setBackground(new Color(220, 53, 69));
        btnXoa.setForeground(Color.BLACK);
        btnXoa.setFocusPainted(false);
        btnXoa.addActionListener(e -> xoaNCC());
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
        String[] columns = {"Mã NCC", "Tên nhà cung cấp", "Địa chỉ", "SĐT", "Email", "Người liên hệ", "Trạng thái"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableNCC = new JTable(tableModel);
        tableNCC.setRowHeight(25);
        tableNCC.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableNCC.getTableHeader().setReorderingAllowed(false);
        tableNCC.setAutoCreateRowSorter(true);
        
        // Sự kiện click chọn row
        tableNCC.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                hienThiThongTinNCC();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tableNCC);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Load dữ liệu lên bảng
     */
    private void loadDataToTable() {
        tableModel.setRowCount(0);
        List<NhaCungCap> list = nhaCungCapDAO.getAll();
        
        for (NhaCungCap ncc : list) {
            Object[] row = {
                ncc.getMaNcc(),
                ncc.getTenNcc(),
                ncc.getDiaChi(),
                ncc.getSdt(),
                ncc.getEmail(),
                ncc.getNguoiLienHe(),
                ncc.getTrangThai() == 1 ? "Hoạt động" : "Ngừng hợp tác"
            };
            tableModel.addRow(row);
        }
    }
    
    /**
     * Hiển thị thông tin NCC khi click vào table
     */
    private void hienThiThongTinNCC() {
        int selectedRow = tableNCC.getSelectedRow();
        if (selectedRow >= 0) {
            txtMaNcc.setText(tableModel.getValueAt(selectedRow, 0).toString());
            txtTenNcc.setText(tableModel.getValueAt(selectedRow, 1).toString());
            
            Object diaChi = tableModel.getValueAt(selectedRow, 2);
            txtDiaChi.setText(diaChi != null ? diaChi.toString() : "");
            
            Object sdt = tableModel.getValueAt(selectedRow, 3);
            txtSdt.setText(sdt != null ? sdt.toString() : "");
            
            Object email = tableModel.getValueAt(selectedRow, 4);
            txtEmail.setText(email != null ? email.toString() : "");
            
            Object nguoiLienHe = tableModel.getValueAt(selectedRow, 5);
            txtNguoiLienHe.setText(nguoiLienHe != null ? nguoiLienHe.toString() : "");
            
            String trangThai = tableModel.getValueAt(selectedRow, 6).toString();
            cboTrangThai.setSelectedIndex(trangThai.equals("Hoạt động") ? 0 : 1);
        }
    }
    
    /**
     * Thêm nhà cung cấp
     */
    private void themNCC() {
        if (!validateInput()) return;
        
        NhaCungCap ncc = new NhaCungCap();
        ncc.setTenNcc(txtTenNcc.getText().trim());
        ncc.setDiaChi(txtDiaChi.getText().trim());
        ncc.setSdt(txtSdt.getText().trim());
        ncc.setEmail(txtEmail.getText().trim());
        ncc.setNguoiLienHe(txtNguoiLienHe.getText().trim());
        ncc.setTrangThai(cboTrangThai.getSelectedIndex() == 0 ? 1 : 0);
        
        if (nhaCungCapDAO.insert(ncc)) {
            JOptionPane.showMessageDialog(this, "Thêm nhà cung cấp thành công!");
            loadDataToTable();
            lamMoi();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm nhà cung cấp thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Sửa nhà cung cấp
     */
    private void suaNCC() {
        if (txtMaNcc.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhà cung cấp cần sửa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!validateInput()) return;
        
        NhaCungCap ncc = new NhaCungCap();
        ncc.setMaNcc(Integer.parseInt(txtMaNcc.getText()));
        ncc.setTenNcc(txtTenNcc.getText().trim());
        ncc.setDiaChi(txtDiaChi.getText().trim());
        ncc.setSdt(txtSdt.getText().trim());
        ncc.setEmail(txtEmail.getText().trim());
        ncc.setNguoiLienHe(txtNguoiLienHe.getText().trim());
        ncc.setTrangThai(cboTrangThai.getSelectedIndex() == 0 ? 1 : 0);
        
        if (nhaCungCapDAO.update(ncc)) {
            JOptionPane.showMessageDialog(this, "Cập nhật nhà cung cấp thành công!");
            loadDataToTable();
            lamMoi();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật nhà cung cấp thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Xóa nhà cung cấp
     */
    private void xoaNCC() {
        if (txtMaNcc.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhà cung cấp cần xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int choice = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc muốn xóa nhà cung cấp này?", 
            "Xác nhận", 
            JOptionPane.YES_NO_OPTION);
        
        if (choice == JOptionPane.YES_OPTION) {
            int maNcc = Integer.parseInt(txtMaNcc.getText());
            
            if (nhaCungCapDAO.delete(maNcc)) {
                JOptionPane.showMessageDialog(this, "Xóa nhà cung cấp thành công!");
                loadDataToTable();
                lamMoi();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Xóa nhà cung cấp thất bại!\nCó thể NCC này đã có phiếu nhập.", 
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
        List<NhaCungCap> list = nhaCungCapDAO.search(keyword);
        
        for (NhaCungCap ncc : list) {
            Object[] row = {
                ncc.getMaNcc(),
                ncc.getTenNcc(),
                ncc.getDiaChi(),
                ncc.getSdt(),
                ncc.getEmail(),
                ncc.getNguoiLienHe(),
                ncc.getTrangThai() == 1 ? "Hoạt động" : "Ngừng hợp tác"
            };
            tableModel.addRow(row);
        }
        
        JOptionPane.showMessageDialog(this, "Tìm thấy " + list.size() + " nhà cung cấp!");
    }
    
    /**
     * Làm mới form
     */
    private void lamMoi() {
        txtMaNcc.setText("");
        txtTenNcc.setText("");
        txtDiaChi.setText("");
        txtSdt.setText("");
        txtEmail.setText("");
        txtNguoiLienHe.setText("");
        txtTimKiem.setText("");
        cboTrangThai.setSelectedIndex(0);
        tableNCC.clearSelection();
        loadDataToTable();
    }
    
    /**
     * Validate input
     */
    private boolean validateInput() {
        if (txtTenNcc.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên nhà cung cấp!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtTenNcc.requestFocus();
            return false;
        }
        return true;
    }
    
}
