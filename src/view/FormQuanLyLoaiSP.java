package view;

import dao.LoaiSanPhamDAO;
import model.LoaiSanPham;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FormQuanLyLoaiSP extends JFrame {
    
    private JTable tableLoaiSP;
    private DefaultTableModel tableModel;
    private JTextField txtMaLoai, txtTenLoai, txtMoTa, txtTimKiem;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnTimKiem;
    
    private LoaiSanPhamDAO loaiSanPhamDAO;
    
    public FormQuanLyLoaiSP() {
        loaiSanPhamDAO = new LoaiSanPhamDAO();
        initComponents();
        loadDataToTable();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("Quản lý loại sản phẩm");
        setSize(900, 550);
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
        panel.setPreferredSize(new Dimension(320, 500));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder("Thông tin loại sản phẩm"));
        
        int yPos = 30;
        int labelWidth = 100;
        int fieldWidth = 200;
        int lineHeight = 40;
        
        // Mã loại (disabled)
        JLabel lblMaLoai = new JLabel("Mã loại:");
        lblMaLoai.setBounds(15, yPos, labelWidth, 25);
        panel.add(lblMaLoai);
        
        txtMaLoai = new JTextField();
        txtMaLoai.setBounds(15, yPos + 25, fieldWidth, 28);
        txtMaLoai.setEnabled(false);
        txtMaLoai.setBackground(new Color(240, 240, 240));
        panel.add(txtMaLoai);
        
        yPos += lineHeight + 25;
        
        // Tên loại
        JLabel lblTenLoai = new JLabel("Tên loại:");
        lblTenLoai.setBounds(15, yPos, labelWidth, 25);
        panel.add(lblTenLoai);
        
        txtTenLoai = new JTextField();
        txtTenLoai.setBounds(15, yPos + 25, fieldWidth, 28);
        panel.add(txtTenLoai);
        
        yPos += lineHeight + 25;
        
        // Mô tả
        JLabel lblMoTa = new JLabel("Mô tả:");
        lblMoTa.setBounds(15, yPos, labelWidth, 25);
        panel.add(lblMoTa);
        
        txtMoTa = new JTextField();
        txtMoTa.setBounds(15, yPos + 25, fieldWidth, 60);
        panel.add(txtMoTa);
        
        yPos += 110;
        
        // Buttons
        btnThem = new JButton("Thêm");
        btnThem.setBounds(15, yPos, 95, 35);
        btnThem.setBackground(new Color(40, 167, 69));
        btnThem.setForeground(Color.BLACK);
        btnThem.setFocusPainted(false);
        btnThem.addActionListener(e -> themLoaiSP());
        panel.add(btnThem);
        
        btnSua = new JButton("Sửa");
        btnSua.setBounds(120, yPos, 95, 35);
        btnSua.setBackground(new Color(255, 193, 7));
        btnSua.setForeground(Color.BLACK);
        btnSua.setFocusPainted(false);
        btnSua.addActionListener(e -> suaLoaiSP());
        panel.add(btnSua);
        
        yPos += 45;
        
        btnXoa = new JButton("Xóa");
        btnXoa.setBounds(15, yPos, 95, 35);
        btnXoa.setBackground(new Color(220, 53, 69));
        btnXoa.setForeground(Color.BLACK);
        btnXoa.setFocusPainted(false);
        btnXoa.addActionListener(e -> xoaLoaiSP());
        panel.add(btnXoa);
        
        btnLamMoi = new JButton("Làm mới");
        btnLamMoi.setBounds(120, yPos, 95, 35);
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
        String[] columns = {"Mã loại", "Tên loại", "Mô tả", "Ngày tạo"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableLoaiSP = new JTable(tableModel);
        tableLoaiSP.setRowHeight(25);
        tableLoaiSP.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableLoaiSP.getTableHeader().setReorderingAllowed(false);
        tableLoaiSP.setAutoCreateRowSorter(true);
        
        // Sự kiện click chọn row
        tableLoaiSP.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                hienThiThongTinLoaiSP();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tableLoaiSP);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Load dữ liệu lên bảng
     */
    private void loadDataToTable() {
        tableModel.setRowCount(0);
        List<LoaiSanPham> list = loaiSanPhamDAO.getAll();
        
        for (LoaiSanPham loai : list) {
            Object[] row = {
                loai.getMaLoai(),
                loai.getTenLoai(),
                loai.getMoTa(),
                loai.getNgayTao()
            };
            tableModel.addRow(row);
        }
    }
    
    /**
     * Hiển thị thông tin loại SP khi click vào table
     */
    private void hienThiThongTinLoaiSP() {
        int selectedRow = tableLoaiSP.getSelectedRow();
        if (selectedRow >= 0) {
            txtMaLoai.setText(tableModel.getValueAt(selectedRow, 0).toString());
            txtTenLoai.setText(tableModel.getValueAt(selectedRow, 1).toString());
            
            Object moTa = tableModel.getValueAt(selectedRow, 2);
            txtMoTa.setText(moTa != null ? moTa.toString() : "");
        }
    }
    
    /**
     * Thêm loại sản phẩm
     */
    private void themLoaiSP() {
        if (!validateInput()) return;
        
        LoaiSanPham loai = new LoaiSanPham();
        loai.setTenLoai(txtTenLoai.getText().trim());
        loai.setMoTa(txtMoTa.getText().trim());
        
        if (loaiSanPhamDAO.insert(loai)) {
            JOptionPane.showMessageDialog(this, "Thêm loại sản phẩm thành công!");
            loadDataToTable();
            lamMoi();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm loại sản phẩm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Sửa loại sản phẩm
     */
    private void suaLoaiSP() {
        if (txtMaLoai.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn loại sản phẩm cần sửa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!validateInput()) return;
        
        LoaiSanPham loai = new LoaiSanPham();
        loai.setMaLoai(Integer.parseInt(txtMaLoai.getText()));
        loai.setTenLoai(txtTenLoai.getText().trim());
        loai.setMoTa(txtMoTa.getText().trim());
        
        if (loaiSanPhamDAO.update(loai)) {
            JOptionPane.showMessageDialog(this, "Cập nhật loại sản phẩm thành công!");
            loadDataToTable();
            lamMoi();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật loại sản phẩm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Xóa loại sản phẩm
     */
    private void xoaLoaiSP() {
        if (txtMaLoai.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn loại sản phẩm cần xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int choice = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc muốn xóa loại sản phẩm này?\nLưu ý: Không thể xóa nếu đã có sản phẩm thuộc loại này.", 
            "Xác nhận", 
            JOptionPane.YES_NO_OPTION);
        
        if (choice == JOptionPane.YES_OPTION) {
            int maLoai = Integer.parseInt(txtMaLoai.getText());
            
            if (loaiSanPhamDAO.delete(maLoai)) {
                JOptionPane.showMessageDialog(this, "Xóa loại sản phẩm thành công!");
                loadDataToTable();
                lamMoi();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Xóa loại sản phẩm thất bại!\nCó thể loại này đã có sản phẩm.", 
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
        List<LoaiSanPham> list = loaiSanPhamDAO.search(keyword);
        
        for (LoaiSanPham loai : list) {
            Object[] row = {
                loai.getMaLoai(),
                loai.getTenLoai(),
                loai.getMoTa(),
                loai.getNgayTao()
            };
            tableModel.addRow(row);
        }
        
        JOptionPane.showMessageDialog(this, "Tìm thấy " + list.size() + " loại sản phẩm!");
    }
    
    /**
     * Làm mới form
     */
    private void lamMoi() {
        txtMaLoai.setText("");
        txtTenLoai.setText("");
        txtMoTa.setText("");
        txtTimKiem.setText("");
        tableLoaiSP.clearSelection();
        loadDataToTable();
    }
    
    /**
     * Validate input
     */
    private boolean validateInput() {
        if (txtTenLoai.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên loại sản phẩm!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtTenLoai.requestFocus();
            return false;
        }
        return true;
    }
    
}
