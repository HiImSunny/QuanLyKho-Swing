package view;

import dao.KhachHangDAO;
import dao.KhoDAO;
import dao.PhieuXuatDAO;
import dao.SanPhamDAO;
import dao.TonKhoDAO;
import model.ChiTietPhieuXuat;
import model.KhachHang;
import model.Kho;
import model.PhieuXuat;
import model.SanPham;
import model.TonKho;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class FormXuatKho extends JFrame {
    private User currentUser;
    private JTextField txtSoPhieu;
    private JTextField txtNgayXuat;
    private JComboBox<String> cboKhachHang;
    private JComboBox<String> cboKho; // Chọn kho xuất
    private JTextArea txtGhiChu;
    private JTable tableChiTiet;
    private DefaultTableModel modelChiTiet;
    private JComboBox<String> cboSanPham;
    private JTextField txtSoLuong;
    private JTextField txtDonGia;
    private JLabel lblTongTien;
    private JLabel lblTonKho; // Hiển thị tồn kho hiện tại
    private JButton btnThem, btnXoa, btnLuu, btnHuy;

    private KhachHangDAO khachHangDAO;
    private KhoDAO khoDAO;
    private SanPhamDAO sanPhamDAO;
    private TonKhoDAO tonKhoDAO;
    private PhieuXuatDAO phieuXuatDAO;
    private List<KhachHang> listKH;
    private List<Kho> listKho;
    private List<SanPham> listSanPham;
    private java.text.DecimalFormatSymbols symbols = new java.text.DecimalFormatSymbols();
    private DecimalFormat dfCurrency;

    public FormXuatKho(User user) {
        this.currentUser = user;

        // Define VN format: dot for thousands, comma for decimal
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        dfCurrency = new DecimalFormat("#,##0", symbols);

        khachHangDAO = new KhachHangDAO();
        khoDAO = new KhoDAO();
        sanPhamDAO = new SanPhamDAO();
        tonKhoDAO = new TonKhoDAO();
        phieuXuatDAO = new PhieuXuatDAO();

        initComponents();
        loadKhachHang();
        loadKho();
        loadSanPham();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Phiếu xuất kho");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Panel chính
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Panel thông tin phiếu xuất
        JPanel infoPanel = createInfoPanel();
        mainPanel.add(infoPanel, BorderLayout.NORTH);

        // Panel chi tiết
        JPanel detailPanel = createDetailPanel();
        mainPanel.add(detailPanel, BorderLayout.CENTER);

        // Panel tổng tiền
        JPanel footerPanel = createFooterPanel();
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Thông tin phiếu xuất"));
        panel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Số phiếu
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Số phiếu:"), gbc);

        gbc.gridx = 1;
        txtSoPhieu = new JTextField(20);
        txtSoPhieu.setEnabled(false);
        txtSoPhieu.setBackground(new Color(240, 240, 240));
        txtSoPhieu.setText("PX" + System.currentTimeMillis());
        panel.add(txtSoPhieu, gbc);

        // Ngày xuất
        gbc.gridx = 2;
        gbc.gridy = 0;
        panel.add(new JLabel("Ngày xuất:"), gbc);

        gbc.gridx = 3;
        txtNgayXuat = new JTextField(15);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        txtNgayXuat.setText(sdf.format(new java.util.Date()));
        panel.add(txtNgayXuat, gbc);

        // Khách hàng
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Khách hàng:"), gbc);

        gbc.gridx = 1;
        cboKhachHang = new JComboBox<>();
        panel.add(cboKhachHang, gbc);

        // Kho xuất
        gbc.gridx = 2;
        gbc.gridy = 1;
        panel.add(new JLabel("Kho xuất:"), gbc);

        gbc.gridx = 3;
        cboKho = new JComboBox<>();
        cboKho.addActionListener(e -> onKhoChanged());
        panel.add(cboKho, gbc);

        // Ghi chú
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Ghi chú:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 3;
        txtGhiChu = new JTextArea(3, 20);
        txtGhiChu.setLineWrap(true);
        JScrollPane scrollGhiChu = new JScrollPane(txtGhiChu);
        panel.add(scrollGhiChu, gbc);

        return panel;
    }

    private JPanel createDetailPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Chi tiết sản phẩm"));

        // Panel thêm sản phẩm
        JPanel addPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addPanel.setBackground(Color.WHITE);

        addPanel.add(new JLabel("Sản phẩm:"));
        cboSanPham = new JComboBox<>();
        cboSanPham.setPreferredSize(new Dimension(200, 25));
        cboSanPham.addActionListener(e -> updatePriceAndStock());
        addPanel.add(cboSanPham);

        addPanel.add(new JLabel("Số lượng:"));
        txtSoLuong = new JTextField(8);
        addPanel.add(txtSoLuong);

        addPanel.add(new JLabel("Đơn giá:"));
        txtDonGia = new JTextField(12);
        addPanel.add(txtDonGia);

        // Label hiển thị tồn kho
        lblTonKho = new JLabel("Tồn kho: 0");
        lblTonKho.setFont(new Font("Arial", Font.BOLD, 12));
        lblTonKho.setForeground(new Color(220, 53, 69));
        addPanel.add(lblTonKho);

        btnThem = new JButton("Thêm");
        btnThem.setBackground(new Color(40, 167, 69));
        btnThem.setForeground(Color.BLACK);
        btnThem.addActionListener(e -> themSanPham());
        addPanel.add(btnThem);

        btnXoa = new JButton("Xóa dòng");
        btnXoa.setBackground(new Color(220, 53, 69));
        btnXoa.setForeground(Color.BLACK);
        btnXoa.addActionListener(e -> xoaDong());
        addPanel.add(btnXoa);

        panel.add(addPanel, BorderLayout.NORTH);

        // Table chi tiết
        String[] columns = { "STT", "Mã SP", "Tên sản phẩm", "Số lượng", "Đơn giá", "Thành tiền" };
        modelChiTiet = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableChiTiet = new JTable(modelChiTiet);
        tableChiTiet.setRowHeight(25);
        JScrollPane scrollTable = new JScrollPane(tableChiTiet);
        panel.add(scrollTable, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createFooterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // Panel tổng tiền
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalPanel.add(new JLabel("TỔNG TIỀN:"));
        lblTongTien = new JLabel("0 VNĐ");
        lblTongTien.setFont(new Font("Arial", Font.BOLD, 18));
        lblTongTien.setForeground(new Color(220, 53, 69));
        totalPanel.add(lblTongTien);

        // Panel buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnLuu = new JButton("Lưu phiếu xuất");
        btnLuu.setBackground(new Color(0, 123, 255));
        btnLuu.setForeground(Color.BLACK);
        btnLuu.setPreferredSize(new Dimension(150, 35));
        btnLuu.addActionListener(e -> luuPhieuXuat());
        buttonPanel.add(btnLuu);

        btnHuy = new JButton("Làm mới");
        btnHuy.setBackground(new Color(108, 117, 125));
        btnHuy.setForeground(Color.BLACK);
        btnHuy.setPreferredSize(new Dimension(150, 35));
        btnHuy.addActionListener(e -> lamMoi());
        buttonPanel.add(btnHuy);

        panel.add(totalPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void loadKhachHang() {
        listKH = khachHangDAO.getAll();
        cboKhachHang.removeAllItems();
        cboKhachHang.addItem("-- Chọn khách hàng --");
        for (KhachHang kh : listKH) {
            cboKhachHang.addItem(kh.getMaKh() + " - " + kh.getTenKh());
        }
    }

    private void loadKho() {
        listKho = khoDAO.getAll();
        cboKho.removeAllItems();
        cboKho.addItem("-- Chọn kho xuất --");
        for (Kho kho : listKho) {
            cboKho.addItem(kho.getId() + " - " + kho.getTenKho());
        }
    }

    private void loadSanPham() {
        // Load all products (used at init)
        listSanPham = sanPhamDAO.getAll();
        cboSanPham.removeAllItems();
        cboSanPham.addItem("-- Chọn sản phẩm --");
    }

    /**
     * Khi chọn kho → load sản phẩm có trong kho đó
     */
    private void onKhoChanged() {
        // Null check
        if (cboKho.getSelectedItem() == null)
            return;

        String selectedKho = cboKho.getSelectedItem().toString();

        // Clear product combobox
        cboSanPham.removeAllItems();
        cboSanPham.addItem("-- Chọn sản phẩm --");

        if (selectedKho.equals("-- Chọn kho xuất --")) {
            lblTonKho.setText("Tồn kho: 0");
            return;
        }

        int maKho = Integer.parseInt(selectedKho.split(" - ")[0]);

        // Load products that have inventory in this warehouse
        List<TonKho> tonKhoList = tonKhoDAO.getByMaKho(maKho);

        for (TonKho tk : tonKhoList) {
            int availableStock = getAvailableStock(tk.getMaSp(), maKho);
            if (availableStock > 0) {
                cboSanPham.addItem(tk.getMaSp() + " - " + tk.getTenSp() + " (tồn: " + availableStock + ")");
            }
        }

        lblTonKho.setText("Tồn kho: 0");
    }

    /**
     * Tính tồn kho khả dụng (tồn thực tế - đã chọn trong table)
     */
    private int getAvailableStock(int maSp, int maKho) {
        // Get actual stock from database
        List<TonKho> tonKhoList = tonKhoDAO.getByMaSp(maSp);
        int actualStock = tonKhoList.stream()
                .filter(tk -> tk.getMaKho() == maKho)
                .mapToInt(TonKho::getSoLuongTon)
                .sum();

        // Subtract quantities already in the table
        int selectedQty = 0;
        for (int i = 0; i < modelChiTiet.getRowCount(); i++) {
            int tableMaSp = (int) modelChiTiet.getValueAt(i, 1);
            if (tableMaSp == maSp) {
                selectedQty += (int) modelChiTiet.getValueAt(i, 3);
            }
        }

        return actualStock - selectedQty;
    }

    private void updatePriceAndStock() {
        // Null check
        if (cboSanPham.getSelectedItem() == null) {
            txtDonGia.setText("");
            lblTonKho.setText("Tồn kho: 0");
            return;
        }

        String selected = cboSanPham.getSelectedItem().toString();
        if (selected.equals("-- Chọn sản phẩm --")) {
            txtDonGia.setText("");
            lblTonKho.setText("Tồn kho: 0");
            return;
        }

        int maSp = Integer.parseInt(selected.split(" - ")[0]);
        // Get san pham from list
        SanPham sp = listSanPham.stream()
                .filter(s -> s.getMaSp() == maSp)
                .findFirst()
                .orElse(null);

        if (sp != null) {
            txtDonGia.setText(dfCurrency.format(sp.getGiaBan()));
        }

        updateStockInfo();
    }

    private void updateStockInfo() {
        // Null check to prevent error during ComboBox loading
        if (cboSanPham.getSelectedItem() == null || cboKho.getSelectedItem() == null) {
            lblTonKho.setText("Tồn kho: 0");
            return;
        }

        String selectedSp = cboSanPham.getSelectedItem().toString();
        String selectedKho = cboKho.getSelectedItem().toString();

        if (selectedSp.equals("-- Chọn sản phẩm --") || selectedKho.equals("-- Chọn kho xuất --")) {
            lblTonKho.setText("Tồn kho: 0");
            return;
        }

        int maSp = Integer.parseInt(selectedSp.split(" - ")[0]);
        int maKho = Integer.parseInt(selectedKho.split(" - ")[0]);

        // Lấy tồn kho khả dụng (đã trừ đi số lượng đang chọn trong table)
        int tonKho = getAvailableStock(maSp, maKho);

        lblTonKho.setText("Tồn kho: " + tonKho);
        if (tonKho == 0) {
            lblTonKho.setForeground(Color.RED);
        } else if (tonKho < 20) {
            lblTonKho.setForeground(new Color(255, 193, 7));
        } else {
            lblTonKho.setForeground(new Color(40, 167, 69));
        }
    }

    private void themSanPham() {
        String selectedSp = cboSanPham.getSelectedItem().toString();
        if (selectedSp.equals("-- Chọn sản phẩm --")) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm!");
            return;
        }

        if (txtSoLuong.getText().trim().isEmpty() || txtDonGia.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ số lượng và đơn giá!");
            return;
        }

        // Validate and parse inputs FIRST
        int maSp, soLuong;
        String tenSp;
        BigDecimal donGia;

        try {
            // Parse product: "maSp - tenSp (tồn: X)"
            String spPart = selectedSp.split(" \\(tồn:")[0]; // Remove (tồn: X)
            maSp = Integer.parseInt(spPart.split(" - ")[0]);
            tenSp = spPart.split(" - ")[1];
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi parse sản phẩm: " + ex.getMessage());
            return;
        }

        try {
            soLuong = Integer.parseInt(txtSoLuong.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Số lượng không hợp lệ! Vui lòng nhập số nguyên.");
            return;
        }

        try {
            String donGiaStr = txtDonGia.getText().trim().replace(".", "").replace(",", "");
            donGia = new BigDecimal(donGiaStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Đơn giá không hợp lệ! Vui lòng nhập số.");
            return;
        }

        if (soLuong <= 0) {
            JOptionPane.showMessageDialog(this, "Số lượng phải > 0!");
            return;
        }

        // Kiểm tra tồn kho
        String selectedKho = cboKho.getSelectedItem().toString();
        if (!selectedKho.equals("-- Chọn kho xuất --")) {
            int maKho = Integer.parseInt(selectedKho.split(" - ")[0]);
            List<TonKho> tonKhoList = tonKhoDAO.getByMaSp(maSp);
            int tonKho = tonKhoList.stream()
                    .filter(tk -> tk.getMaKho() == maKho)
                    .mapToInt(TonKho::getSoLuongTon)
                    .sum();

            if (soLuong > tonKho) {
                int choice = JOptionPane.showConfirmDialog(this,
                        "Số lượng xuất (" + soLuong + ") lớn hơn tồn kho (" + tonKho
                                + ")!\nBạn có chắc muốn tiếp tục?",
                        "Cảnh báo",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                if (choice != JOptionPane.YES_OPTION) {
                    return;
                }
            }
        }

        // All validations passed, add to table
        BigDecimal thanhTien = donGia.multiply(new BigDecimal(soLuong));

        int stt = modelChiTiet.getRowCount() + 1;
        modelChiTiet.addRow(new Object[] {
                stt,
                maSp,
                tenSp,
                soLuong,
                dfCurrency.format(donGia),
                dfCurrency.format(thanhTien)
        });

        tinhTongTien();

        // Lock warehouse selection after adding first product
        cboKho.setEnabled(false);

        // Reload product list to update available stock
        onKhoChanged();

        // Clear inputs
        cboSanPham.setSelectedIndex(0);
        txtSoLuong.setText("");
        txtDonGia.setText("");
        lblTonKho.setText("Tồn kho: 0");
    }

    private void xoaDong() {
        int selectedRow = tableChiTiet.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần xóa!");
            return;
        }

        modelChiTiet.removeRow(selectedRow);

        // Cập nhật lại STT
        for (int i = 0; i < modelChiTiet.getRowCount(); i++) {
            modelChiTiet.setValueAt(i + 1, i, 0);
        }

        tinhTongTien();
    }

    private void tinhTongTien() {
        BigDecimal tongTien = BigDecimal.ZERO;

        for (int i = 0; i < modelChiTiet.getRowCount(); i++) {
            String thanhTienStr = modelChiTiet.getValueAt(i, 5).toString().replace(".", "").replace(",", "")
                    .replace(" VNĐ", "");
            BigDecimal thanhTien = new BigDecimal(thanhTienStr);
            tongTien = tongTien.add(thanhTien);
        }

        lblTongTien.setText(dfCurrency.format(tongTien) + " VNĐ");
    }

    private void luuPhieuXuat() {
        // Validation
        if (cboKhachHang.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng!");
            return;
        }

        if (cboKho.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn kho xuất!");
            return;
        }

        if (modelChiTiet.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng thêm ít nhất 1 sản phẩm!");
            return;
        }

        try {
            String soPhieu = txtSoPhieu.getText();
            Date ngayXuat = Date.valueOf(txtNgayXuat.getText());

            String selectedKH = cboKhachHang.getSelectedItem().toString();
            int maKH = Integer.parseInt(selectedKH.split(" - ")[0]);
            String tenKH = selectedKH.split(" - ")[1];

            String selectedKho = cboKho.getSelectedItem().toString();
            int maKho = Integer.parseInt(selectedKho.split(" - ")[0]);

            String ghiChu = txtGhiChu.getText().trim();

            BigDecimal tongTien = BigDecimal.ZERO;
            List<ChiTietPhieuXuat> chiTietList = new ArrayList<>();

            for (int i = 0; i < modelChiTiet.getRowCount(); i++) {
                int maSp = (int) modelChiTiet.getValueAt(i, 1);
                String tenSp = (String) modelChiTiet.getValueAt(i, 2);
                int soLuong = (int) modelChiTiet.getValueAt(i, 3);
                String donGiaStr = modelChiTiet.getValueAt(i, 4).toString().replace(".", "").replace(",", "")
                        .replace(" VNĐ", "");
                BigDecimal donGia = new BigDecimal(donGiaStr);

                ChiTietPhieuXuat ct = new ChiTietPhieuXuat(maSp, tenSp, soLuong, donGia);
                chiTietList.add(ct);

                tongTien = tongTien.add(donGia.multiply(new BigDecimal(soLuong)));
            }

            PhieuXuat phieuXuat = new PhieuXuat(soPhieu, ngayXuat, tenKH, maKH, maKho,
                    currentUser.getId(), tongTien, ghiChu);

            boolean success = phieuXuatDAO.insert(phieuXuat, chiTietList);

            if (success) {
                JOptionPane.showMessageDialog(this, "Lưu phiếu xuất thành công!");
                lamMoi();
            } else {
                JOptionPane.showMessageDialog(this, "Lưu phiếu xuất thất bại!\nKiểm tra tồn kho hoặc kết nối database.",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void lamMoi() {
        txtSoPhieu.setText("PX" + System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        txtNgayXuat.setText(sdf.format(new java.util.Date()));
        cboKhachHang.setSelectedIndex(0);
        cboKho.setEnabled(true); // Enable warehouse selection again
        cboKho.setSelectedIndex(0);
        txtGhiChu.setText("");
        modelChiTiet.setRowCount(0);
        cboSanPham.removeAllItems();
        cboSanPham.addItem("-- Chọn sản phẩm --");
        txtSoLuong.setText("");
        txtDonGia.setText("");
        lblTonKho.setText("Tồn kho: 0");
        lblTongTien.setText("0 VNĐ");
    }
}
