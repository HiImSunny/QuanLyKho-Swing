package view;

import dao.KhoDAO;
import model.Kho;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FormQuanLyKho extends JFrame {

    private JTable tableKho;
    private DefaultTableModel tableModel;
    private JTextField txtId, txtMaKho, txtTenKho, txtDiaChi, txtDienTich, txtNguoiQuanLy, txtGhiChu, txtTimKiem;
    private JComboBox<String> cboTrangThai;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnTimKiem;

    private KhoDAO khoDAO;

    public FormQuanLyKho() {
        khoDAO = new KhoDAO();
        initComponents();
        loadDataToTable();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Quản lý kho");
        setSize(1200, 600); // ← Tăng width lên 1200
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
        panel.setBorder(BorderFactory.createTitledBorder("Thông tin kho"));

        int yPos = 30;
        int labelWidth = 120;
        int fieldWidth = 200;
        int lineHeight = 40;

        // ID (disabled)
        JLabel lblId = new JLabel("ID:");
        lblId.setBounds(15, yPos, labelWidth, 25);
        panel.add(lblId);

        txtId = new JTextField();
        txtId.setBounds(140, yPos, fieldWidth, 28);
        txtId.setEnabled(false);
        txtId.setBackground(new Color(240, 240, 240));
        panel.add(txtId);

        yPos += lineHeight;

        // Mã kho
        JLabel lblMaKho = new JLabel("Mã kho:");
        lblMaKho.setBounds(15, yPos, labelWidth, 25);
        panel.add(lblMaKho);

        txtMaKho = new JTextField();
        txtMaKho.setBounds(140, yPos, fieldWidth, 28);
        panel.add(txtMaKho);

        yPos += lineHeight;

        // Tên kho
        JLabel lblTenKho = new JLabel("Tên kho:");
        lblTenKho.setBounds(15, yPos, labelWidth, 25);
        panel.add(lblTenKho);

        txtTenKho = new JTextField();
        txtTenKho.setBounds(140, yPos, fieldWidth, 28);
        panel.add(txtTenKho);

        yPos += lineHeight;

        // Địa chỉ
        JLabel lblDiaChi = new JLabel("Địa chỉ:");
        lblDiaChi.setBounds(15, yPos, labelWidth, 25);
        panel.add(lblDiaChi);

        txtDiaChi = new JTextField();
        txtDiaChi.setBounds(140, yPos, fieldWidth, 28);
        panel.add(txtDiaChi);

        yPos += lineHeight;

        // Diện tích
        JLabel lblDienTich = new JLabel("Diện tích (m²):");
        lblDienTich.setBounds(15, yPos, labelWidth, 25);
        panel.add(lblDienTich);

        txtDienTich = new JTextField();
        txtDienTich.setBounds(140, yPos, fieldWidth, 28);
        panel.add(txtDienTich);

        yPos += lineHeight;

        JLabel lblNguoiQuanLy = new JLabel("Người quản lý:");
        lblNguoiQuanLy.setBounds(15, yPos, labelWidth, 25);
        panel.add(lblNguoiQuanLy);

        txtNguoiQuanLy = new JTextField();
        txtNguoiQuanLy.setBounds(140, yPos, fieldWidth, 28);
        panel.add(txtNguoiQuanLy);

        yPos += lineHeight;

        // Ghi chú
        JLabel lblGhiChu = new JLabel("Ghi chú:");
        lblGhiChu.setBounds(15, yPos, labelWidth, 25);
        panel.add(lblGhiChu);

        txtGhiChu = new JTextField();
        txtGhiChu.setBounds(140, yPos, fieldWidth, 28);
        panel.add(txtGhiChu);

        yPos += lineHeight;

        // Trạng thái
        JLabel lblTrangThai = new JLabel("Trạng thái:");
        lblTrangThai.setBounds(15, yPos, labelWidth, 25);
        panel.add(lblTrangThai);

        cboTrangThai = new JComboBox<>(new String[] { "Hoạt động", "Ngừng hoạt động" });
        cboTrangThai.setBounds(140, yPos, fieldWidth, 28);
        panel.add(cboTrangThai);

        yPos += 50;

        // Buttons
        btnThem = new JButton("Thêm");
        btnThem.setBounds(15, yPos, 100, 35);
        btnThem.setBackground(new Color(40, 167, 69));
        btnThem.setForeground(Color.BLACK);
        btnThem.setFocusPainted(false);
        btnThem.addActionListener(e -> themKho());
        panel.add(btnThem);

        btnSua = new JButton("Sửa");
        btnSua.setBounds(125, yPos, 100, 35);
        btnSua.setBackground(new Color(255, 193, 7));
        btnSua.setForeground(Color.BLACK);
        btnSua.setFocusPainted(false);
        btnSua.addActionListener(e -> suaKho());
        panel.add(btnSua);

        btnXoa = new JButton("Xóa");
        btnXoa.setBounds(235, yPos, 100, 35);
        btnXoa.setBackground(new Color(220, 53, 69));
        btnXoa.setForeground(Color.BLACK);
        btnXoa.setFocusPainted(false);
        btnXoa.addActionListener(e -> xoaKho());
        panel.add(btnXoa);

        yPos += 50;

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

        txtTimKiem = new JTextField(30);
        searchPanel.add(txtTimKiem);

        btnTimKiem = new JButton("Tìm");
        btnTimKiem.setBackground(new Color(0, 123, 255));
        btnTimKiem.setForeground(Color.BLACK);
        btnTimKiem.setFocusPainted(false);
        btnTimKiem.addActionListener(e -> timKiem());
        searchPanel.add(btnTimKiem);

        JButton btnXemSanPham = new JButton("Xem sản phẩm trong kho");
        btnXemSanPham.setBackground(new Color(40, 167, 69));
        btnXemSanPham.setForeground(Color.BLACK);
        btnXemSanPham.setFocusPainted(false);
        btnXemSanPham.addActionListener(e -> xemSanPhamTrongKho());
        searchPanel.add(btnXemSanPham);

        panel.add(searchPanel, BorderLayout.NORTH);

        String[] columns = { "ID", "Mã kho", "Tên kho", "Địa chỉ", "Diện tích (m²)", "Người quản lý", "Ghi chú",
                "Trạng thái" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableKho = new JTable(tableModel);
        tableKho.setRowHeight(25);
        tableKho.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableKho.getTableHeader().setReorderingAllowed(false);
        tableKho.setAutoCreateRowSorter(true);

        // Sự kiện click chọn row
        tableKho.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                hienThiThongTinKho();
            }
        });

        JScrollPane scrollPane = new JScrollPane(tableKho);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Load dữ liệu lên bảng
     */
    private void loadDataToTable() {
        tableModel.setRowCount(0);
        List<Kho> list = khoDAO.getAll();

        for (Kho kho : list) {
            String trangThai = kho.getTrangThai() == 1 ? "Hoạt động" : "Ngừng hoạt động";

            Object[] row = {
                    kho.getId(),
                    kho.getMaKho(),
                    kho.getTenKho(),
                    kho.getDiaChi(),
                    kho.getDienTich(),
                    kho.getNguoiQuanLy(),
                    kho.getGhiChu(),
                    trangThai,
            };
            tableModel.addRow(row);
        }
    }

    /**
     * Hiển thị thông tin kho khi click vào table
     */
    private void hienThiThongTinKho() {
        int selectedRow = tableKho.getSelectedRow();
        if (selectedRow >= 0) {
            txtId.setText(tableModel.getValueAt(selectedRow, 0).toString());
            txtMaKho.setText(tableModel.getValueAt(selectedRow, 1).toString());
            txtTenKho.setText(tableModel.getValueAt(selectedRow, 2).toString());
            txtDiaChi.setText(tableModel.getValueAt(selectedRow, 3).toString());
            txtDienTich.setText(tableModel.getValueAt(selectedRow, 4).toString());
            txtNguoiQuanLy.setText(tableModel.getValueAt(selectedRow, 5).toString());
            txtGhiChu.setText(tableModel.getValueAt(selectedRow, 6).toString());

            String trangThai = tableModel.getValueAt(selectedRow, 7).toString();
            cboTrangThai.setSelectedIndex(trangThai.equals("Hoạt động") ? 0 : 1);
        }
    }

    /**
     * Thêm kho
     */
    private void themKho() {
        if (!validateInput())
            return;

        // Kiểm tra mã kho đã tồn tại
        if (khoDAO.isMaKhoExist(txtMaKho.getText().trim())) {
            JOptionPane.showMessageDialog(this, "Mã kho đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Kho kho = new Kho();
        kho.setMaKho(txtMaKho.getText().trim());
        kho.setTenKho(txtTenKho.getText().trim());
        kho.setDiaChi(txtDiaChi.getText().trim());
        kho.setDienTich(Double.parseDouble(txtDienTich.getText().trim()));
        kho.setNguoiQuanLy(txtNguoiQuanLy.getText().trim());
        kho.setGhiChu(txtGhiChu.getText().trim());
        kho.setTrangThai(cboTrangThai.getSelectedIndex() == 0 ? 1 : 0);

        if (khoDAO.insert(kho)) {
            JOptionPane.showMessageDialog(this, "Thêm kho thành công!");
            loadDataToTable();
            lamMoi();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm kho thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Sửa kho
     */
    private void suaKho() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn kho cần sửa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!validateInput())
            return;

        int id = Integer.parseInt(txtId.getText());

        // Kiểm tra mã kho đã tồn tại (trừ kho hiện tại)
        if (khoDAO.isMaKhoExist(txtMaKho.getText().trim(), id)) {
            JOptionPane.showMessageDialog(this, "Mã kho đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Kho kho = new Kho();
        kho.setId(id);
        kho.setMaKho(txtMaKho.getText().trim());
        kho.setTenKho(txtTenKho.getText().trim());
        kho.setDiaChi(txtDiaChi.getText().trim());
        kho.setDienTich(Double.parseDouble(txtDienTich.getText().trim()));
        kho.setNguoiQuanLy(txtNguoiQuanLy.getText().trim());
        kho.setGhiChu(txtGhiChu.getText().trim());
        kho.setTrangThai(cboTrangThai.getSelectedIndex() == 0 ? 1 : 0);

        if (khoDAO.update(kho)) {
            JOptionPane.showMessageDialog(this, "Cập nhật kho thành công!");
            loadDataToTable();
            lamMoi();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật kho thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Xóa kho
     */
    private void xoaKho() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn kho cần xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa kho này?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(txtId.getText());

            if (khoDAO.delete(id)) {
                JOptionPane.showMessageDialog(this, "Xóa kho thành công!");
                loadDataToTable();
                lamMoi();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa kho thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
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
        List<Kho> list = khoDAO.search(keyword);

        for (Kho kho : list) {
            String trangThai = kho.getTrangThai() == 1 ? "Hoạt động" : "Ngừng hoạt động";

            Object[] row = {
                    kho.getId(),
                    kho.getMaKho(),
                    kho.getTenKho(),
                    kho.getDiaChi(),
                    kho.getDienTich(),
                    kho.getNguoiQuanLy(),
                    kho.getGhiChu(),
                    trangThai,
                    kho.getNgayTao()
            };
            tableModel.addRow(row);
        }

        JOptionPane.showMessageDialog(this, "Tìm thấy " + list.size() + " kho!");
    }

    /**
     * Làm mới form
     */
    private void lamMoi() {
        txtId.setText("");
        txtMaKho.setText("");
        txtTenKho.setText("");
        txtDiaChi.setText("");
        txtDienTich.setText("");
        txtNguoiQuanLy.setText("");
        txtGhiChu.setText("");
        txtTimKiem.setText("");
        cboTrangThai.setSelectedIndex(0);
        tableKho.clearSelection();
        loadDataToTable();
    }

    /**
     * Xem sản phẩm trong kho
     */
    private void xemSanPhamTrongKho() {
        int selectedRow = tableKho.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn kho!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int maKho = (int) tableModel.getValueAt(selectedRow, 0);
        String tenKho = (String) tableModel.getValueAt(selectedRow, 2);

        new DialogSanPhamTrongKho(this, maKho, tenKho).setVisible(true);
    }

    /**
     * Validate input
     */
    private boolean validateInput() {
        if (txtMaKho.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã kho!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (txtTenKho.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên kho!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (txtDiaChi.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập địa chỉ!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (txtDienTich.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập diện tích!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        try {
            double dienTich = Double.parseDouble(txtDienTich.getText().trim());
            if (dienTich <= 0) {
                JOptionPane.showMessageDialog(this, "Diện tích phải lớn hơn 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Diện tích không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Người quản lý có thể để trống (không bắt buộc)

        return true;
    }
}
