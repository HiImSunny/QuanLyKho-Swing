package view;

import dao.LoaiSanPhamDAO;
import dao.SanPhamDAO;
import model.LoaiSanPham;
import model.SanPham;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class FormQuanLySanPham extends JFrame {

    private JTable tableSanPham;
    private DefaultTableModel tableModel;
    private JTextField txtMaSp, txtTenSp, txtGiaNhap, txtGiaBan, txtTonKho, txtMoTa, txtTimKiem;
    private JComboBox<LoaiSanPham> cboLoaiSP;
    private JComboBox<String> cboDonVi;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnTimKiem;

    private SanPhamDAO sanPhamDAO;
    private LoaiSanPhamDAO loaiSanPhamDAO;
    private NumberFormat currencyFormat;

    public FormQuanLySanPham() {
        sanPhamDAO = new SanPhamDAO();
        loaiSanPhamDAO = new LoaiSanPhamDAO();
        currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        initComponents();
        loadLoaiSanPham();
        loadDataToTable();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Quản lý sản phẩm");
        setSize(1100, 650);
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
        panel.setBorder(BorderFactory.createTitledBorder("Thông tin sản phẩm"));

        int yPos = 30;
        int labelWidth = 100;
        int fieldWidth = 220;
        int lineHeight = 35;

        // Mã SP (disabled)
        JLabel lblMaSp = new JLabel("Mã SP:");
        lblMaSp.setBounds(15, yPos, labelWidth, 25);
        panel.add(lblMaSp);

        txtMaSp = new JTextField();
        txtMaSp.setBounds(120, yPos, fieldWidth, 28);
        txtMaSp.setEnabled(false);
        txtMaSp.setBackground(new Color(240, 240, 240));
        panel.add(txtMaSp);

        yPos += lineHeight;

        // Tên SP
        JLabel lblTenSp = new JLabel("Tên sản phẩm:");
        lblTenSp.setBounds(15, yPos, labelWidth, 25);
        panel.add(lblTenSp);

        txtTenSp = new JTextField();
        txtTenSp.setBounds(120, yPos, fieldWidth, 28);
        panel.add(txtTenSp);

        yPos += lineHeight;

        // Loại SP
        JLabel lblLoaiSP = new JLabel("Loại SP:");
        lblLoaiSP.setBounds(15, yPos, labelWidth, 25);
        panel.add(lblLoaiSP);

        cboLoaiSP = new JComboBox<>();
        cboLoaiSP.setBounds(120, yPos, fieldWidth, 28);
        panel.add(cboLoaiSP);

        yPos += lineHeight;

        // Đơn vị tính
        JLabel lblDonVi = new JLabel("Đơn vị tính:");
        lblDonVi.setBounds(15, yPos, labelWidth, 25);
        panel.add(lblDonVi);

        cboDonVi = new JComboBox<>(
                new String[] { "Cái", "Hộp", "Thùng", "Bộ", "Quyển", "Cây", "Ream", "Đôi", "Kg", "Lít" });
        cboDonVi.setBounds(120, yPos, fieldWidth, 28);
        panel.add(cboDonVi);

        yPos += lineHeight;

        // Giá nhập
        JLabel lblGiaNhap = new JLabel("Giá nhập:");
        lblGiaNhap.setBounds(15, yPos, labelWidth, 25);
        panel.add(lblGiaNhap);

        txtGiaNhap = new JTextField();
        txtGiaNhap.setBounds(120, yPos, fieldWidth, 28);
        panel.add(txtGiaNhap);

        yPos += lineHeight;

        // Giá bán
        JLabel lblGiaBan = new JLabel("Giá bán:");
        lblGiaBan.setBounds(15, yPos, labelWidth, 25);
        panel.add(lblGiaBan);

        txtGiaBan = new JTextField();
        txtGiaBan.setBounds(120, yPos, fieldWidth, 28);
        panel.add(txtGiaBan);

        yPos += lineHeight;

        // Tồn kho
        JLabel lblTonKho = new JLabel("Tồn kho:");
        lblTonKho.setBounds(15, yPos, labelWidth, 25);
        panel.add(lblTonKho);

        txtTonKho = new JTextField("0");
        txtTonKho.setBounds(120, yPos, fieldWidth, 28);
        panel.add(txtTonKho);

        yPos += lineHeight;

        // Mô tả
        JLabel lblMoTa = new JLabel("Mô tả:");
        lblMoTa.setBounds(15, yPos, labelWidth, 25);
        panel.add(lblMoTa);

        txtMoTa = new JTextField();
        txtMoTa.setBounds(120, yPos, fieldWidth, 28);
        panel.add(txtMoTa);

        yPos += 50;

        // Buttons
        btnThem = new JButton("Thêm");
        btnThem.setBounds(15, yPos, 100, 35);
        btnThem.setBackground(new Color(40, 167, 69));
        btnThem.setForeground(Color.BLACK);
        btnThem.setFocusPainted(false);
        btnThem.addActionListener(e -> themSanPham());
        panel.add(btnThem);

        btnSua = new JButton("Sửa");
        btnSua.setBounds(125, yPos, 100, 35);
        btnSua.setBackground(new Color(255, 193, 7));
        btnSua.setForeground(Color.BLACK);
        btnSua.setFocusPainted(false);
        btnSua.addActionListener(e -> suaSanPham());
        panel.add(btnSua);

        btnXoa = new JButton("Xóa");
        btnXoa.setBounds(235, yPos, 100, 35);
        btnXoa.setBackground(new Color(220, 53, 69));
        btnXoa.setForeground(Color.BLACK);
        btnXoa.setFocusPainted(false);
        btnXoa.addActionListener(e -> xoaSanPham());
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

        JButton btnXemTonKho = new JButton("Xem chi tiết tồn kho");
        btnXemTonKho.setBackground(new Color(23, 162, 184));
        btnXemTonKho.setForeground(Color.BLACK);
        btnXemTonKho.setFocusPainted(false);
        btnXemTonKho.addActionListener(e -> xemChiTietTonKho());
        searchPanel.add(btnXemTonKho);

        panel.add(searchPanel, BorderLayout.NORTH);

        // Table
        String[] columns = { "Mã SP", "Tên sản phẩm", "Loại", "ĐVT", "Giá nhập", "Giá bán", "Tồn kho" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho edit trực tiếp trên table
            }
        };

        tableSanPham = new JTable(tableModel);
        tableSanPham.setRowHeight(25);
        tableSanPham.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableSanPham.getTableHeader().setReorderingAllowed(false);
        tableSanPham.setAutoCreateRowSorter(true);

        // Sự kiện click chọn row
        tableSanPham.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                hienThiThongTinSanPham();
            }
        });

        JScrollPane scrollPane = new JScrollPane(tableSanPham);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Load loại sản phẩm vào ComboBox
     */
    private void loadLoaiSanPham() {
        cboLoaiSP.removeAllItems();
        List<LoaiSanPham> list = loaiSanPhamDAO.getAll();
        for (LoaiSanPham loai : list) {
            cboLoaiSP.addItem(loai);
        }
    }

    /**
     * Load dữ liệu lên bảng
     */
    private void loadDataToTable() {
        tableModel.setRowCount(0);
        List<SanPham> list = sanPhamDAO.getAll();

        for (SanPham sp : list) {
            Object[] row = {
                    sp.getMaSp(),
                    sp.getTenSp(),
                    sp.getTenLoai(),
                    sp.getDonViTinh(),
                    currencyFormat.format(sp.getGiaNhap()),
                    currencyFormat.format(sp.getGiaBan()),
                    sp.getSoLuongTon()
            };
            tableModel.addRow(row);
        }
    }

    /**
     * Hiển thị thông tin SP khi click vào table
     */
    private void hienThiThongTinSanPham() {
        int selectedRow = tableSanPham.getSelectedRow();
        if (selectedRow >= 0) {
            txtMaSp.setText(tableModel.getValueAt(selectedRow, 0).toString());
            txtTenSp.setText(tableModel.getValueAt(selectedRow, 1).toString());

            String tenLoai = tableModel.getValueAt(selectedRow, 2).toString();
            for (int i = 0; i < cboLoaiSP.getItemCount(); i++) {
                if (cboLoaiSP.getItemAt(i).getTenLoai().equals(tenLoai)) {
                    cboLoaiSP.setSelectedIndex(i);
                    break;
                }
            }

            cboDonVi.setSelectedItem(tableModel.getValueAt(selectedRow, 3).toString());

            String giaNhap = tableModel.getValueAt(selectedRow, 4).toString().replace("₫", "").replace(".", "").trim();
            String giaBan = tableModel.getValueAt(selectedRow, 5).toString().replace("₫", "").replace(".", "").trim();

            txtGiaNhap.setText(giaNhap);
            txtGiaBan.setText(giaBan);
            txtTonKho.setText(tableModel.getValueAt(selectedRow, 6).toString());
        }
    }

    /**
     * Thêm sản phẩm
     */
    private void themSanPham() {
        if (!validateInput())
            return;

        SanPham sp = new SanPham();
        sp.setTenSp(txtTenSp.getText().trim());
        sp.setMaLoai(((LoaiSanPham) cboLoaiSP.getSelectedItem()).getMaLoai());
        sp.setDonViTinh(cboDonVi.getSelectedItem().toString());
        sp.setGiaNhap(new BigDecimal(txtGiaNhap.getText().trim()));
        sp.setGiaBan(new BigDecimal(txtGiaBan.getText().trim()));
        sp.setSoLuongTon(Integer.parseInt(txtTonKho.getText().trim()));
        sp.setMoTa(txtMoTa.getText().trim());

        if (sanPhamDAO.insert(sp)) {
            JOptionPane.showMessageDialog(this, "Thêm sản phẩm thành công!");
            loadDataToTable();
            lamMoi();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm sản phẩm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Sửa sản phẩm
     */
    private void suaSanPham() {
        if (txtMaSp.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần sửa!", "Cảnh báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!validateInput())
            return;

        SanPham sp = new SanPham();
        sp.setMaSp(Integer.parseInt(txtMaSp.getText()));
        sp.setTenSp(txtTenSp.getText().trim());
        sp.setMaLoai(((LoaiSanPham) cboLoaiSP.getSelectedItem()).getMaLoai());
        sp.setDonViTinh(cboDonVi.getSelectedItem().toString());
        sp.setGiaNhap(new BigDecimal(txtGiaNhap.getText().trim()));
        sp.setGiaBan(new BigDecimal(txtGiaBan.getText().trim()));
        sp.setSoLuongTon(Integer.parseInt(txtTonKho.getText().trim()));
        sp.setMoTa(txtMoTa.getText().trim());

        if (sanPhamDAO.update(sp)) {
            JOptionPane.showMessageDialog(this, "Cập nhật sản phẩm thành công!");
            loadDataToTable();
            lamMoi();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật sản phẩm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Xóa sản phẩm
     */
    private void xoaSanPham() {
        if (txtMaSp.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần xóa!", "Cảnh báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa sản phẩm này?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            int maSp = Integer.parseInt(txtMaSp.getText());

            if (sanPhamDAO.delete(maSp)) {
                JOptionPane.showMessageDialog(this, "Xóa sản phẩm thành công!");
                loadDataToTable();
                lamMoi();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Xóa sản phẩm thất bại! Có thể sản phẩm đã được sử dụng trong phiếu nhập/xuất.", "Lỗi",
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
        List<SanPham> list = sanPhamDAO.search(keyword);

        for (SanPham sp : list) {
            Object[] row = {
                    sp.getMaSp(),
                    sp.getTenSp(),
                    sp.getTenLoai(),
                    sp.getDonViTinh(),
                    currencyFormat.format(sp.getGiaNhap()),
                    currencyFormat.format(sp.getGiaBan()),
                    sp.getSoLuongTon()
            };
            tableModel.addRow(row);
        }

        JOptionPane.showMessageDialog(this, "Tìm thấy " + list.size() + " sản phẩm!");
    }

    /**
     * Làm mới form
     */
    private void lamMoi() {
        txtMaSp.setText("");
        txtTenSp.setText("");
        txtGiaNhap.setText("");
        txtGiaBan.setText("");
        txtTonKho.setText("0");
        txtMoTa.setText("");
        txtTimKiem.setText("");
        cboLoaiSP.setSelectedIndex(0);
        cboDonVi.setSelectedIndex(0);
        tableSanPham.clearSelection();
        loadDataToTable();
    }

    /**
     * Xem chi tiết tồn kho theo kho
     */
    private void xemChiTietTonKho() {
        int selectedRow = tableSanPham.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int maSp = (int) tableModel.getValueAt(selectedRow, 0);
        String tenSp = (String) tableModel.getValueAt(selectedRow, 1);

        new DialogTonKhoTheoKho(this, maSp, tenSp).setVisible(true);
    }

    /**
     * Validate input
     */
    private boolean validateInput() {
        if (txtTenSp.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên sản phẩm!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtTenSp.requestFocus();
            return false;
        }

        try {
            BigDecimal giaNhap = new BigDecimal(txtGiaNhap.getText().trim());
            if (giaNhap.compareTo(BigDecimal.ZERO) < 0) {
                JOptionPane.showMessageDialog(this, "Giá nhập phải >= 0!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Giá nhập không hợp lệ!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtGiaNhap.requestFocus();
            return false;
        }

        try {
            BigDecimal giaBan = new BigDecimal(txtGiaBan.getText().trim());
            if (giaBan.compareTo(BigDecimal.ZERO) < 0) {
                JOptionPane.showMessageDialog(this, "Giá bán phải >= 0!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Giá bán không hợp lệ!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtGiaBan.requestFocus();
            return false;
        }

        try {
            int tonKho = Integer.parseInt(txtTonKho.getText().trim());
            if (tonKho < 0) {
                JOptionPane.showMessageDialog(this, "Tồn kho phải >= 0!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Tồn kho không hợp lệ!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtTonKho.requestFocus();
            return false;
        }

        return true;
    }

}
