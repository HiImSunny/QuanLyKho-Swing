package view;

import dao.KhoDAO;
import dao.NhaCungCapDAO;
import dao.PhieuNhapDAO;
import dao.SanPhamDAO;
import model.ChiTietPhieuNhap;
import model.Kho;
import model.NhaCungCap;
import model.PhieuNhap;
import model.SanPham;
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

public class FormNhapKho extends JFrame {
    private User currentUser;
    private JTextField txtSoPhieu;
    private JTextField txtNgayNhap;
    private JComboBox<String> cboNhaCungCap;
    private JComboBox<String> cboKho; // Chọn kho nhập
    private JTextArea txtGhiChu;
    private JTable tableChiTiet;
    private DefaultTableModel modelChiTiet;
    private JComboBox<String> cboSanPham;
    private JTextField txtSoLuong;
    private JTextField txtDonGia;
    private JLabel lblTongTien;
    private JButton btnThem, btnXoa, btnLuu, btnHuy;

    private KhoDAO khoDAO;
    private NhaCungCapDAO nccDAO;
    private SanPhamDAO sanPhamDAO;
    private PhieuNhapDAO phieuNhapDAO;
    private List<NhaCungCap> listNCC;
    private List<Kho> listKho;
    private List<SanPham> listSanPham;
    private java.text.DecimalFormatSymbols symbols = new java.text.DecimalFormatSymbols();
    private DecimalFormat dfCurrency;

    public FormNhapKho(User user) {
        this.currentUser = user;

        // Define VN format: dot for thousands, comma for decimal
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        dfCurrency = new DecimalFormat("#,##0", symbols);

        khoDAO = new KhoDAO();
        nccDAO = new NhaCungCapDAO();
        sanPhamDAO = new SanPhamDAO();
        phieuNhapDAO = new PhieuNhapDAO();

        setTitle("Phiếu Nhập Kho");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Root panel với padding
        JPanel rootPanel = new JPanel(new BorderLayout(10, 10));
        rootPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header
        rootPanel.add(createHeaderPanel(), BorderLayout.NORTH);

        // Center
        rootPanel.add(createCenterPanel(), BorderLayout.CENTER);

        // Footer
        rootPanel.add(createFooterPanel(), BorderLayout.SOUTH);

        add(rootPanel);

        // Load dữ liệu
        loadKho();
        loadNhaCungCap();
        loadSanPham();
        generateSoPhieu();
        setDefaultDate();
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("PHIẾU NHẬP KHO", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        panel.add(lblTitle, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.add(createInfoPanel(), BorderLayout.NORTH);
        panel.add(createTablePanel(), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Thông tin phiếu nhập"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Số phiếu:"), gbc);
        gbc.gridx = 1;
        txtSoPhieu = new JTextField(20);
        txtSoPhieu.setEditable(false);
        txtSoPhieu.setBackground(Color.LIGHT_GRAY);
        panel.add(txtSoPhieu, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        panel.add(new JLabel("Ngày nhập:"), gbc);
        gbc.gridx = 3;
        txtNgayNhap = new JTextField(20);
        panel.add(txtNgayNhap, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Nhà cung cấp:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        cboNhaCungCap = new JComboBox<>();
        panel.add(cboNhaCungCap, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Kho nhập:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        cboKho = new JComboBox<>();
        panel.add(cboKho, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Ghi chú:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        txtGhiChu = new JTextArea(3, 20);
        txtGhiChu.setLineWrap(true);
        txtGhiChu.setWrapStyleWord(true);
        JScrollPane scrollGhiChu = new JScrollPane(txtGhiChu);
        panel.add(scrollGhiChu, gbc);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Chi tiết nhập"));

        JPanel pnlAdd = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        pnlAdd.add(new JLabel("Sản phẩm:"));
        cboSanPham = new JComboBox<>();
        cboSanPham.setPreferredSize(new Dimension(250, 25));
        pnlAdd.add(cboSanPham);

        pnlAdd.add(new JLabel("Số lượng:"));
        txtSoLuong = new JTextField(8);
        pnlAdd.add(txtSoLuong);

        pnlAdd.add(new JLabel("Đơn giá:"));
        txtDonGia = new JTextField(12);
        pnlAdd.add(txtDonGia);

        btnThem = new JButton("Thêm");
        btnThem.addActionListener(e -> themSanPham());
        pnlAdd.add(btnThem);

        btnXoa = new JButton("Xóa dòng");
        btnXoa.addActionListener(e -> xoaDong());
        pnlAdd.add(btnXoa);

        panel.add(pnlAdd, BorderLayout.NORTH);

        String[] columns = { "STT", "Mã SP", "Tên sản phẩm", "Số lượng", "Đơn giá", "Thành tiền" };
        modelChiTiet = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableChiTiet = new JTable(modelChiTiet);
        tableChiTiet.getColumnModel().getColumn(0).setPreferredWidth(50);
        tableChiTiet.getColumnModel().getColumn(1).setPreferredWidth(80);
        tableChiTiet.getColumnModel().getColumn(2).setPreferredWidth(250);
        tableChiTiet.getColumnModel().getColumn(3).setPreferredWidth(100);
        tableChiTiet.getColumnModel().getColumn(4).setPreferredWidth(120);
        tableChiTiet.getColumnModel().getColumn(5).setPreferredWidth(120);

        JScrollPane scrollTable = new JScrollPane(tableChiTiet);
        panel.add(scrollTable, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createFooterPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel pnlTong = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlTong.add(new JLabel("TỔNG TIỀN:"));
        lblTongTien = new JLabel("0 VNĐ");
        lblTongTien.setFont(new Font("Arial", Font.BOLD, 18));
        lblTongTien.setForeground(Color.RED);
        pnlTong.add(lblTongTien);

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        btnLuu = new JButton("Lưu phiếu nhập");
        btnLuu.setPreferredSize(new Dimension(150, 35));
        btnLuu.addActionListener(e -> luuPhieuNhap());
        pnlButtons.add(btnLuu);

        btnHuy = new JButton("Làm mới");
        btnHuy.setPreferredSize(new Dimension(150, 35));
        btnHuy.addActionListener(e -> lamMoi());
        pnlButtons.add(btnHuy);

        panel.add(pnlTong, BorderLayout.NORTH);
        panel.add(pnlButtons, BorderLayout.CENTER);

        return panel;
    }

    private void loadNhaCungCap() {
        listNCC = nccDAO.getActive();
        cboNhaCungCap.removeAllItems();
        cboNhaCungCap.addItem("-- Chọn nhà cung cấp --");
        for (NhaCungCap ncc : listNCC) {
            cboNhaCungCap.addItem(ncc.getMaNcc() + " - " + ncc.getTenNcc());
        }
    }

    private void loadKho() {
        listKho = khoDAO.getAll();
        cboKho.removeAllItems();
        cboKho.addItem("-- Chọn kho nhập --");
        for (Kho kho : listKho) {
            cboKho.addItem(kho.getId() + " - " + kho.getTenKho());
        }
    }

    private void loadSanPham() {
        listSanPham = sanPhamDAO.getAll();
        cboSanPham.removeAllItems();
        cboSanPham.addItem("-- Chọn sản phẩm --");
        for (SanPham sp : listSanPham) {
            cboSanPham.addItem(sp.getMaSp() + " - " + sp.getTenSp());
        }
    }

    private void generateSoPhieu() {
        String soPhieu = "PN" + System.currentTimeMillis();
        txtSoPhieu.setText(soPhieu);
    }

    private void setDefaultDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        txtNgayNhap.setText(sdf.format(new java.util.Date()));
    }

    private void themSanPham() {
        try {
            if (cboSanPham.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm!");
                return;
            }

            int soLuong = Integer.parseInt(txtSoLuong.getText().trim());
            if (soLuong <= 0) {
                JOptionPane.showMessageDialog(this, "Số lượng phải > 0!");
                return;
            }

            BigDecimal donGia = new BigDecimal(txtDonGia.getText().trim().replace(".", "").replace(",", ""));
            if (donGia.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(this, "Đơn giá phải > 0!");
                return;
            }

            String selectedSP = cboSanPham.getSelectedItem().toString();
            int maSP = Integer.parseInt(selectedSP.split(" - ")[0]);
            String tenSP = selectedSP.split(" - ")[1];

            for (int i = 0; i < modelChiTiet.getRowCount(); i++) {
                if ((int) modelChiTiet.getValueAt(i, 1) == maSP) {
                    JOptionPane.showMessageDialog(this, "Sản phẩm đã có trong danh sách!");
                    return;
                }
            }

            BigDecimal thanhTien = donGia.multiply(new BigDecimal(soLuong));

            modelChiTiet.addRow(new Object[] {
                    modelChiTiet.getRowCount() + 1,
                    maSP,
                    tenSP,
                    soLuong,
                    dfCurrency.format(donGia) + " VNĐ",
                    dfCurrency.format(thanhTien) + " VNĐ"
            });

            cboSanPham.setSelectedIndex(0);
            txtSoLuong.setText("");
            txtDonGia.setText("");
            tinhTongTien();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Số lượng và đơn giá phải là số hợp lệ!");
        }
    }

    private void xoaDong() {
        int selectedRow = tableChiTiet.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần xóa!");
            return;
        }

        modelChiTiet.removeRow(selectedRow);
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

    private void luuPhieuNhap() {
        try {
            if (cboNhaCungCap.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn nhà cung cấp!");
                return;
            }

            if (cboKho.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn kho nhập!");
                return;
            }

            if (modelChiTiet.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng thêm ít nhất một sản phẩm!");
                return;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            java.util.Date utilDate = sdf.parse(txtNgayNhap.getText().trim());
            Date ngayNhap = new Date(utilDate.getTime());

            String selectedNCC = cboNhaCungCap.getSelectedItem().toString();
            int maNCC = Integer.parseInt(selectedNCC.split(" - ")[0]);
            String tenNCC = selectedNCC.split(" - ")[1];

            String selectedKho = cboKho.getSelectedItem().toString();
            int maKho = Integer.parseInt(selectedKho.split(" - ")[0]);

            int nguoiLap = currentUser.getId(); // ← DÙNG currentUser.getId()

            BigDecimal tongTien = new BigDecimal(lblTongTien.getText()
                    .replace(".", "").replace(",", "").replace(" VNĐ", ""));

            PhieuNhap phieuNhap = new PhieuNhap(
                    txtSoPhieu.getText(),
                    ngayNhap,
                    tenNCC,
                    maNCC,
                    maKho, // Thêm ma_kho
                    nguoiLap,
                    tongTien,
                    txtGhiChu.getText().trim());

            List<ChiTietPhieuNhap> chiTietList = new ArrayList<>();
            for (int i = 0; i < modelChiTiet.getRowCount(); i++) {
                int maSP = (int) modelChiTiet.getValueAt(i, 1);
                int soLuong = (int) modelChiTiet.getValueAt(i, 3);
                String donGiaStr = modelChiTiet.getValueAt(i, 4).toString().replace(".", "").replace(",", "")
                        .replace(" VNĐ", "");
                BigDecimal donGia = new BigDecimal(donGiaStr);

                ChiTietPhieuNhap ct = new ChiTietPhieuNhap(0, maSP, soLuong, donGia);
                chiTietList.add(ct);
            }

            boolean success = phieuNhapDAO.insert(phieuNhap, chiTietList);

            if (success) {
                JOptionPane.showMessageDialog(this, "Lưu phiếu nhập thành công!");
                lamMoi();
            } else {
                JOptionPane.showMessageDialog(this, "Lưu phiếu nhập thất bại!");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void lamMoi() {
        generateSoPhieu();
        setDefaultDate();
        cboNhaCungCap.setSelectedIndex(0);
        cboKho.setSelectedIndex(0);
        txtGhiChu.setText("");
        modelChiTiet.setRowCount(0);
        cboSanPham.setSelectedIndex(0);
        txtSoLuong.setText("");
        txtDonGia.setText("");
        lblTongTien.setText("0 VNĐ");
    }
}
