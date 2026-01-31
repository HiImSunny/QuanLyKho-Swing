package view;

import dao.KhoDAO;
import dao.PhieuNhapDAO;
import dao.PhieuXuatDAO;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FormLichSuNhapXuat extends JFrame {
    private User currentUser;

    // Phiếu Nhập components
    private JComboBox<String> cboKhoNhap;
    private JTextField txtSearchNhap;
    private JTable tablePhieuNhap;
    private DefaultTableModel modelPhieuNhap;
    private JTable tableChiTietNhap;
    private DefaultTableModel modelChiTietNhap;
    private JLabel lblThongTinPhieuNhap;
    private JLabel lblTongTienNhap;

    // Phiếu Xuất components
    private JComboBox<String> cboKhoXuat;
    private JTextField txtSearchXuat;
    private JTable tablePhieuXuat;
    private DefaultTableModel modelPhieuXuat;
    private JTable tableChiTietXuat;
    private DefaultTableModel modelChiTietXuat;
    private JLabel lblThongTinPhieuXuat;
    private JLabel lblTongTienXuat;

    private PhieuNhapDAO phieuNhapDAO;
    private PhieuXuatDAO phieuXuatDAO;
    private KhoDAO khoDAO;

    private List<PhieuNhap> listPhieuNhap = new ArrayList<>();
    private List<PhieuXuat> listPhieuXuat = new ArrayList<>();

    public FormLichSuNhapXuat(User user) {
        this.currentUser = user;
        this.phieuNhapDAO = new PhieuNhapDAO();
        this.phieuXuatDAO = new PhieuXuatDAO();
        this.khoDAO = new KhoDAO();

        initComponents();
        loadKho();
        loadPhieuNhap();
        loadPhieuXuat();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponents() {
        setTitle("Lịch Sử Nhập/Xuất Kho");
        setSize(1400, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header
        JLabel lblTitle = new JLabel("LỊCH SỬ NHẬP/XUẤT KHO", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(lblTitle, BorderLayout.NORTH);

        // Tabbed Pane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Phiếu Nhập", createPhieuNhapPanel());
        tabbedPane.addTab("Phiếu Xuất", createPhieuXuatPanel());
        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createPhieuNhapPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Filter Panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Bộ lọc"));

        filterPanel.add(new JLabel("Kho:"));
        cboKhoNhap = new JComboBox<>();
        cboKhoNhap.setPreferredSize(new Dimension(200, 25));
        cboKhoNhap.addActionListener(e -> filterPhieuNhap());
        filterPanel.add(cboKhoNhap);

        filterPanel.add(new JLabel("Tìm kiếm:"));
        txtSearchNhap = new JTextField(20);
        txtSearchNhap.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filterPhieuNhap();
            }
        });
        filterPanel.add(txtSearchNhap);

        JButton btnRefreshNhap = new JButton("Làm mới");
        btnRefreshNhap.addActionListener(e -> {
            cboKhoNhap.setSelectedIndex(0);
            txtSearchNhap.setText("");
            loadPhieuNhap();
        });
        filterPanel.add(btnRefreshNhap);

        JButton btnExportCSVNhap = new JButton("Xuất CSV");
        btnExportCSVNhap.setBackground(new Color(40, 167, 69));
        btnExportCSVNhap.setForeground(Color.BLACK);
        btnExportCSVNhap.addActionListener(e -> exportAllPhieuNhapToCSV());
        filterPanel.add(btnExportCSVNhap);

        // Only admin can cancel
        if (currentUser.getRole().equals("admin")) {
            JButton btnCancelNhap = new JButton("Hủy phiếu");
            btnCancelNhap.setBackground(new Color(220, 53, 69));
            btnCancelNhap.setForeground(Color.BLACK);
            btnCancelNhap.addActionListener(e -> cancelPhieuNhap());
            filterPanel.add(btnCancelNhap);
        }

        panel.add(filterPanel, BorderLayout.NORTH);

        // Split pane for list and details
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.5);

        // Top: Danh sách phiếu nhập
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createTitledBorder("Danh sách phiếu nhập"));

        String[] columnsNhap = { "STT", "Số Phiếu", "Ngày Nhập", "Nhà Cung Cấp", "Kho", "Tổng Tiền", "Người Lập" };
        modelPhieuNhap = new DefaultTableModel(columnsNhap, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablePhieuNhap = new JTable(modelPhieuNhap);
        tablePhieuNhap.setRowHeight(25);
        tablePhieuNhap.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showChiTietPhieuNhap();
            }
        });

        JScrollPane scrollPhieuNhap = new JScrollPane(tablePhieuNhap);
        topPanel.add(scrollPhieuNhap, BorderLayout.CENTER);
        splitPane.setTopComponent(topPanel);

        // Bottom: Chi tiết phiếu nhập
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createTitledBorder("Chi tiết phiếu nhập"));

        lblThongTinPhieuNhap = new JLabel("Chọn một phiếu để xem chi tiết");
        lblThongTinPhieuNhap.setFont(new Font("Arial", Font.BOLD, 12));
        lblThongTinPhieuNhap.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        bottomPanel.add(lblThongTinPhieuNhap, BorderLayout.NORTH);

        String[] columnsChiTietNhap = { "STT", "Mã SP", "Tên Sản Phẩm", "Số Lượng", "Đơn Giá", "Thành Tiền" };
        modelChiTietNhap = new DefaultTableModel(columnsChiTietNhap, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableChiTietNhap = new JTable(modelChiTietNhap);
        tableChiTietNhap.setRowHeight(25);
        JScrollPane scrollChiTietNhap = new JScrollPane(tableChiTietNhap);
        bottomPanel.add(scrollChiTietNhap, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lblTongTienNhap = new JLabel("Tổng tiền: 0 ₫");
        lblTongTienNhap.setFont(new Font("Arial", Font.BOLD, 14));
        footerPanel.add(lblTongTienNhap);

        JButton btnPrintNhap = new JButton("In phiếu này");
        btnPrintNhap.setBackground(new Color(0, 123, 255));
        btnPrintNhap.setForeground(Color.BLACK);
        btnPrintNhap.addActionListener(e -> exportPhieuNhapToPDF());
        footerPanel.add(btnPrintNhap);

        JButton btnExportCSVPhieu = new JButton("Xuất CSV phiếu này");
        btnExportCSVPhieu.setBackground(new Color(40, 167, 69));
        btnExportCSVPhieu.setForeground(Color.BLACK);
        btnExportCSVPhieu.addActionListener(e -> exportPhieuNhapToCSV());
        footerPanel.add(btnExportCSVPhieu);

        bottomPanel.add(footerPanel, BorderLayout.SOUTH);
        splitPane.setBottomComponent(bottomPanel);

        panel.add(splitPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createPhieuXuatPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Filter Panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Bộ lọc"));

        filterPanel.add(new JLabel("Kho:"));
        cboKhoXuat = new JComboBox<>();
        cboKhoXuat.setPreferredSize(new Dimension(200, 25));
        cboKhoXuat.addActionListener(e -> filterPhieuXuat());
        filterPanel.add(cboKhoXuat);

        filterPanel.add(new JLabel("Tìm kiếm:"));
        txtSearchXuat = new JTextField(20);
        txtSearchXuat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filterPhieuXuat();
            }
        });
        filterPanel.add(txtSearchXuat);

        JButton btnRefreshXuat = new JButton("Làm mới");
        btnRefreshXuat.addActionListener(e -> {
            cboKhoXuat.setSelectedIndex(0);
            txtSearchXuat.setText("");
            loadPhieuXuat();
        });
        filterPanel.add(btnRefreshXuat);

        JButton btnExportCSVXuat = new JButton("Xuất CSV");
        btnExportCSVXuat.setBackground(new Color(40, 167, 69));
        btnExportCSVXuat.setForeground(Color.BLACK);
        btnExportCSVXuat.addActionListener(e -> exportAllPhieuXuatToCSV());
        filterPanel.add(btnExportCSVXuat);

        // Only admin can cancel
        if (currentUser.getRole().equals("admin")) {
            JButton btnCancelXuat = new JButton("Hủy phiếu");
            btnCancelXuat.setBackground(new Color(220, 53, 69));
            btnCancelXuat.setForeground(Color.BLACK);
            btnCancelXuat.addActionListener(e -> cancelPhieuXuat());
            filterPanel.add(btnCancelXuat);
        }

        panel.add(filterPanel, BorderLayout.NORTH);

        // Split pane for list and details
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.5);

        // Top: Danh sách phiếu xuất
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createTitledBorder("Danh sách phiếu xuất"));

        String[] columnsXuat = { "STT", "Số Phiếu", "Ngày Xuất", "Khách Hàng", "Kho", "Tổng Tiền", "Người Lập" };
        modelPhieuXuat = new DefaultTableModel(columnsXuat, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablePhieuXuat = new JTable(modelPhieuXuat);
        tablePhieuXuat.setRowHeight(25);
        tablePhieuXuat.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showChiTietPhieuXuat();
            }
        });

        JScrollPane scrollPhieuXuat = new JScrollPane(tablePhieuXuat);
        topPanel.add(scrollPhieuXuat, BorderLayout.CENTER);
        splitPane.setTopComponent(topPanel);

        // Bottom: Chi tiết phiếu xuất
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createTitledBorder("Chi tiết phiếu xuất"));

        lblThongTinPhieuXuat = new JLabel("Chọn một phiếu để xem chi tiết");
        lblThongTinPhieuXuat.setFont(new Font("Arial", Font.BOLD, 12));
        lblThongTinPhieuXuat.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        bottomPanel.add(lblThongTinPhieuXuat, BorderLayout.NORTH);

        String[] columnsChiTietXuat = { "STT", "Mã SP", "Tên Sản Phẩm", "Số Lượng", "Đơn Giá", "Thành Tiền" };
        modelChiTietXuat = new DefaultTableModel(columnsChiTietXuat, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableChiTietXuat = new JTable(modelChiTietXuat);
        tableChiTietXuat.setRowHeight(25);
        JScrollPane scrollChiTietXuat = new JScrollPane(tableChiTietXuat);
        bottomPanel.add(scrollChiTietXuat, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lblTongTienXuat = new JLabel("Tổng tiền: 0 ₫");
        lblTongTienXuat.setFont(new Font("Arial", Font.BOLD, 14));
        footerPanel.add(lblTongTienXuat);

        JButton btnPrintXuat = new JButton("In phiếu này");
        btnPrintXuat.setBackground(new Color(0, 123, 255));
        btnPrintXuat.setForeground(Color.BLACK);
        btnPrintXuat.addActionListener(e -> exportPhieuXuatToPDF());
        footerPanel.add(btnPrintXuat);

        JButton btnExportCSVPhieuXuat = new JButton("Xuất CSV phiếu này");
        btnExportCSVPhieuXuat.setBackground(new Color(40, 167, 69));
        btnExportCSVPhieuXuat.setForeground(Color.BLACK);
        btnExportCSVPhieuXuat.addActionListener(e -> exportPhieuXuatToCSV());
        footerPanel.add(btnExportCSVPhieuXuat);

        bottomPanel.add(footerPanel, BorderLayout.SOUTH);
        splitPane.setBottomComponent(bottomPanel);

        panel.add(splitPane, BorderLayout.CENTER);
        return panel;
    }

    private void loadKho() {
        List<Kho> khoList = khoDAO.getAll();

        cboKhoNhap.removeAllItems();
        cboKhoNhap.addItem("-- Tất cả kho --");
        for (Kho k : khoList) {
            cboKhoNhap.addItem(k.getId() + " - " + k.getTenKho());
        }

        cboKhoXuat.removeAllItems();
        cboKhoXuat.addItem("-- Tất cả kho --");
        for (Kho k : khoList) {
            cboKhoXuat.addItem(k.getId() + " - " + k.getTenKho());
        }
    }

    private void loadPhieuNhap() {
        listPhieuNhap = phieuNhapDAO.getAll();
        displayPhieuNhap(listPhieuNhap);
    }

    private void loadPhieuXuat() {
        listPhieuXuat = phieuXuatDAO.getAll();
        displayPhieuXuat(listPhieuXuat);
    }

    private void displayPhieuNhap(List<PhieuNhap> list) {
        modelPhieuNhap.setRowCount(0);
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        int stt = 1;
        for (PhieuNhap pn : list) {
            modelPhieuNhap.addRow(new Object[] {
                    stt++,
                    pn.getSo_phieu(),
                    dateFormat.format(pn.getNgay_nhap()),
                    pn.getNha_cung_cap(),
                    getKhoName(pn.getMa_kho()),
                    currencyFormat.format(pn.getTong_tien()),
                    "User #" + pn.getNguoi_lap()
            });
        }
    }

    private void displayPhieuXuat(List<PhieuXuat> list) {
        modelPhieuXuat.setRowCount(0);
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        int stt = 1;
        for (PhieuXuat px : list) {
            modelPhieuXuat.addRow(new Object[] {
                    stt++,
                    px.getSo_phieu(),
                    dateFormat.format(px.getNgay_xuat()),
                    px.getKhach_hang(),
                    getKhoName(px.getMa_kho()),
                    currencyFormat.format(px.getTong_tien()),
                    "User #" + px.getNguoi_lap()
            });
        }
    }

    private void filterPhieuNhap() {
        String selectedKho = (String) cboKhoNhap.getSelectedItem();
        String keyword = txtSearchNhap.getText().toLowerCase().trim();

        List<PhieuNhap> filtered = new ArrayList<>();
        for (PhieuNhap pn : listPhieuNhap) {
            boolean matchKho = selectedKho.equals("-- Tất cả kho --") ||
                    selectedKho.contains(String.valueOf(pn.getMa_kho()));
            boolean matchKeyword = keyword.isEmpty() ||
                    pn.getSo_phieu().toLowerCase().contains(keyword) ||
                    (pn.getNha_cung_cap() != null && pn.getNha_cung_cap().toLowerCase().contains(keyword));

            if (matchKho && matchKeyword) {
                filtered.add(pn);
            }
        }
        displayPhieuNhap(filtered);
    }

    private void filterPhieuXuat() {
        String selectedKho = (String) cboKhoXuat.getSelectedItem();
        String keyword = txtSearchXuat.getText().toLowerCase().trim();

        List<PhieuXuat> filtered = new ArrayList<>();
        for (PhieuXuat px : listPhieuXuat) {
            boolean matchKho = selectedKho.equals("-- Tất cả kho --") ||
                    selectedKho.contains(String.valueOf(px.getMa_kho()));
            boolean matchKeyword = keyword.isEmpty() ||
                    px.getSo_phieu().toLowerCase().contains(keyword) ||
                    (px.getKhach_hang() != null && px.getKhach_hang().toLowerCase().contains(keyword));

            if (matchKho && matchKeyword) {
                filtered.add(px);
            }
        }
        displayPhieuXuat(filtered);
    }

    private void showChiTietPhieuNhap() {
        int selectedRow = tablePhieuNhap.getSelectedRow();
        if (selectedRow < 0) {
            modelChiTietNhap.setRowCount(0);
            lblThongTinPhieuNhap.setText("Chọn một phiếu để xem chi tiết");
            lblTongTienNhap.setText("Tổng tiền: 0 ₫");
            return;
        }

        PhieuNhap phieu = listPhieuNhap.get(selectedRow);
        List<ChiTietPhieuNhap> chiTietList = phieuNhapDAO.getChiTiet(phieu.getMa_phieu_nhap());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        lblThongTinPhieuNhap.setText(String.format(
                "Phiếu: %s | Ngày: %s | NCC: %s | Kho: %s | Ghi chú: %s",
                phieu.getSo_phieu(),
                dateFormat.format(phieu.getNgay_nhap()),
                phieu.getNha_cung_cap(),
                getKhoName(phieu.getMa_kho()),
                phieu.getGhi_chu() != null ? phieu.getGhi_chu() : "Không có"));

        modelChiTietNhap.setRowCount(0);
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        int stt = 1;
        for (ChiTietPhieuNhap ct : chiTietList) {
            modelChiTietNhap.addRow(new Object[] {
                    stt++,
                    ct.getMa_sp(),
                    ct.getTen_sp(),
                    ct.getSo_luong(),
                    currencyFormat.format(ct.getDon_gia()),
                    currencyFormat.format(ct.getThanh_tien())
            });
        }

        lblTongTienNhap.setText("Tổng tiền: " + currencyFormat.format(phieu.getTong_tien()));
    }

    private void showChiTietPhieuXuat() {
        int selectedRow = tablePhieuXuat.getSelectedRow();
        if (selectedRow < 0) {
            modelChiTietXuat.setRowCount(0);
            lblThongTinPhieuXuat.setText("Chọn một phiếu để xem chi tiết");
            lblTongTienXuat.setText("Tổng tiền: 0 ₫");
            return;
        }

        PhieuXuat phieu = listPhieuXuat.get(selectedRow);
        List<ChiTietPhieuXuat> chiTietList = phieuXuatDAO.getChiTiet(phieu.getMa_phieu_xuat());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        lblThongTinPhieuXuat.setText(String.format(
                "Phiếu: %s | Ngày: %s | Khách hàng: %s | Kho: %s | Ghi chú: %s",
                phieu.getSo_phieu(),
                dateFormat.format(phieu.getNgay_xuat()),
                phieu.getKhach_hang(),
                getKhoName(phieu.getMa_kho()),
                phieu.getGhi_chu() != null ? phieu.getGhi_chu() : "Không có"));

        modelChiTietXuat.setRowCount(0);
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        int stt = 1;
        for (ChiTietPhieuXuat ct : chiTietList) {
            modelChiTietXuat.addRow(new Object[] {
                    stt++,
                    ct.getMa_sp(),
                    ct.getTen_sp(),
                    ct.getSo_luong(),
                    currencyFormat.format(ct.getDon_gia()),
                    currencyFormat.format(ct.getThanh_tien())
            });
        }

        lblTongTienXuat.setText("Tổng tiền: " + currencyFormat.format(phieu.getTong_tien()));
    }

    private String getKhoName(Integer khoId) {
        if (khoId == null)
            return "N/A";
        Kho kho = khoDAO.getById(khoId);
        if (kho != null) {
            return kho.getTenKho();
        }
        return "Kho #" + khoId;
    }

    // Export all Phieu Nhap to CSV
    private void exportAllPhieuNhapToCSV() {
        if (listPhieuNhap.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu để xuất!");
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Lưu file CSV");
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy_HHmm");
        String defaultName = "DanhSachPhieuNhap_" + sdf.format(new java.util.Date()) + ".csv";
        fileChooser.setSelectedFile(new java.io.File(defaultName));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            java.io.File file = fileChooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".csv")) {
                file = new java.io.File(file.getAbsolutePath() + ".csv");
            }

            try (java.io.PrintWriter writer = new java.io.PrintWriter(file, "UTF-8")) {
                writer.write('\ufeff'); // BOM for Excel UTF-8 support
                // Header
                writer.println("STT,Số Phiếu,Ngày Nhập,Nhà Cung Cấp,Kho,Tổng Tiền,Người Lập");

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                int stt = 1;
                for (PhieuNhap pn : listPhieuNhap) {
                    writer.printf("%d,\"%s\",\"%s\",\"%s\",\"%s\",%s,\"User #%d\"\n",
                            stt++,
                            pn.getSo_phieu(),
                            dateFormat.format(pn.getNgay_nhap()),
                            pn.getNha_cung_cap(),
                            getKhoName(pn.getMa_kho()),
                            pn.getTong_tien(),
                            pn.getNguoi_lap());
                }

                int open = JOptionPane.showConfirmDialog(this,
                        "Xuất CSV thành công! Bạn có muốn mở file ngay không?",
                        "Thông báo", JOptionPane.YES_NO_OPTION);
                if (open == JOptionPane.YES_OPTION) {
                    java.awt.Desktop.getDesktop().open(file);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi xuất CSV: " + e.getMessage());
            }
        }
    }

    // Export all Phieu Xuat to CSV
    private void exportAllPhieuXuatToCSV() {
        if (listPhieuXuat.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu để xuất!");
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Lưu file CSV");
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy_HHmm");
        String defaultName = "DanhSachPhieuXuat_" + sdf.format(new java.util.Date()) + ".csv";
        fileChooser.setSelectedFile(new java.io.File(defaultName));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            java.io.File file = fileChooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".csv")) {
                file = new java.io.File(file.getAbsolutePath() + ".csv");
            }

            try (java.io.PrintWriter writer = new java.io.PrintWriter(file, "UTF-8")) {
                writer.write('\ufeff'); // BOM for Excel UTF-8 support
                // Header
                writer.println("STT,Số Phiếu,Ngày Xuất,Khách Hàng,Kho,Tổng Tiền,Người Lập");

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                int stt = 1;
                for (PhieuXuat px : listPhieuXuat) {
                    writer.printf("%d,\"%s\",\"%s\",\"%s\",\"%s\",%s,\"User #%d\"\n",
                            stt++,
                            px.getSo_phieu(),
                            dateFormat.format(px.getNgay_xuat()),
                            px.getKhach_hang(),
                            getKhoName(px.getMa_kho()),
                            px.getTong_tien(),
                            px.getNguoi_lap());
                }

                int open = JOptionPane.showConfirmDialog(this,
                        "Xuất CSV thành công! Bạn có muốn mở file ngay không?",
                        "Thông báo", JOptionPane.YES_NO_OPTION);
                if (open == JOptionPane.YES_OPTION) {
                    java.awt.Desktop.getDesktop().open(file);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi xuất CSV: " + e.getMessage());
            }
        }
    }

    // Export single Phieu Nhap to CSV
    private void exportPhieuNhapToCSV() {
        int selectedRow = tablePhieuNhap.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một phiếu nhập để xuất CSV!");
            return;
        }

        PhieuNhap phieu = listPhieuNhap.get(selectedRow);
        List<ChiTietPhieuNhap> chiTietList = phieuNhapDAO.getChiTiet(phieu.getMa_phieu_nhap());

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Lưu file CSV");
        String defaultName = "PhieuNhap_" + phieu.getSo_phieu().replace("/", "_") + ".csv";
        fileChooser.setSelectedFile(new java.io.File(defaultName));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            java.io.File file = fileChooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".csv")) {
                file = new java.io.File(file.getAbsolutePath() + ".csv");
            }

            try (java.io.PrintWriter writer = new java.io.PrintWriter(file, "UTF-8")) {
                writer.write('\ufeff'); // BOM for Excel UTF-8 support
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                // Thông tin phiếu
                writer.println("PHIẾU NHẬP KHO");
                writer.println("Số phiếu:," + phieu.getSo_phieu());
                writer.println("Ngày nhập:," + dateFormat.format(phieu.getNgay_nhap()));
                writer.println("Nhà cung cấp:," + phieu.getNha_cung_cap());
                writer.println("Kho:," + getKhoName(phieu.getMa_kho()));
                writer.println("Ghi chú:," + (phieu.getGhi_chu() != null ? phieu.getGhi_chu() : "Không có"));
                writer.println();

                // Chi tiết
                writer.println("STT,Mã SP,Tên Sản Phẩm,Số Lượng,Đơn Giá,Thành Tiền");
                int stt = 1;
                for (ChiTietPhieuNhap ct : chiTietList) {
                    writer.printf("%d,%d,\"%s\",%d,%s,%s\n",
                            stt++,
                            ct.getMa_sp(),
                            ct.getTen_sp(),
                            ct.getSo_luong(),
                            ct.getDon_gia(),
                            ct.getThanh_tien());
                }

                writer.println();
                writer.println("Tổng tiền:," + phieu.getTong_tien());

                int open = JOptionPane.showConfirmDialog(this,
                        "Xuất CSV thành công! Bạn có muốn mở file ngay không?",
                        "Thông báo", JOptionPane.YES_NO_OPTION);
                if (open == JOptionPane.YES_OPTION) {
                    java.awt.Desktop.getDesktop().open(file);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi xuất CSV: " + e.getMessage());
            }
        }
    }

    // Export single Phieu Xuat to CSV
    private void exportPhieuXuatToCSV() {
        int selectedRow = tablePhieuXuat.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một phiếu xuất để xuất CSV!");
            return;
        }

        PhieuXuat phieu = listPhieuXuat.get(selectedRow);
        List<ChiTietPhieuXuat> chiTietList = phieuXuatDAO.getChiTiet(phieu.getMa_phieu_xuat());

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Lưu file CSV");
        String defaultName = "PhieuXuat_" + phieu.getSo_phieu().replace("/", "_") + ".csv";
        fileChooser.setSelectedFile(new java.io.File(defaultName));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            java.io.File file = fileChooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".csv")) {
                file = new java.io.File(file.getAbsolutePath() + ".csv");
            }

            try (java.io.PrintWriter writer = new java.io.PrintWriter(file, "UTF-8")) {
                writer.write('\ufeff'); // BOM for Excel UTF-8 support
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                // Thông tin phiếu
                writer.println("PHIẾU XUẤT KHO");
                writer.println("Số phiếu:," + phieu.getSo_phieu());
                writer.println("Ngày xuất:," + dateFormat.format(phieu.getNgay_xuat()));
                writer.println("Khách hàng:," + phieu.getKhach_hang());
                writer.println("Kho:," + getKhoName(phieu.getMa_kho()));
                writer.println("Ghi chú:," + (phieu.getGhi_chu() != null ? phieu.getGhi_chu() : "Không có"));
                writer.println();

                // Chi tiết
                writer.println("STT,Mã SP,Tên Sản Phẩm,Số Lượng,Đơn Giá,Thành Tiền");
                int stt = 1;
                for (ChiTietPhieuXuat ct : chiTietList) {
                    writer.printf("%d,%d,\"%s\",%d,%s,%s\n",
                            stt++,
                            ct.getMa_sp(),
                            ct.getTen_sp(),
                            ct.getSo_luong(),
                            ct.getDon_gia(),
                            ct.getThanh_tien());
                }

                writer.println();
                writer.println("Tổng tiền:," + phieu.getTong_tien());

                int open = JOptionPane.showConfirmDialog(this,
                        "Xuất CSV thành công! Bạn có muốn mở file ngay không?",
                        "Thông báo", JOptionPane.YES_NO_OPTION);
                if (open == JOptionPane.YES_OPTION) {
                    java.awt.Desktop.getDesktop().open(file);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi xuất CSV: " + e.getMessage());
            }
        }
    }

    private void exportPhieuNhapToPDF() {
        int selectedRow = tablePhieuNhap.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một phiếu nhập để xuất PDF!");
            return;
        }

        PhieuNhap phieu = listPhieuNhap.get(selectedRow);
        List<ChiTietPhieuNhap> chiTietList = phieuNhapDAO.getChiTiet(phieu.getMa_phieu_nhap());

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Lưu file PDF");
        String defaultName = "PhieuNhap_" + phieu.getSo_phieu().replace("/", "_") + ".pdf";
        fileChooser.setSelectedFile(new java.io.File(defaultName));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            java.io.File file = fileChooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".pdf")) {
                file = new java.io.File(file.getAbsolutePath() + ".pdf");
            }

            try {
                // Create table model for PDF
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

                String[] columns = { "STT", "Mã SP", "Tên Sản Phẩm", "Số Lượng", "Đơn Giá", "Thành Tiền" };
                DefaultTableModel model = new DefaultTableModel(columns, 0);

                int stt = 1;
                for (ChiTietPhieuNhap ct : chiTietList) {
                    model.addRow(new Object[] {
                            stt++,
                            ct.getMa_sp(),
                            ct.getTen_sp(),
                            ct.getSo_luong(),
                            currencyFormat.format(ct.getDon_gia()),
                            currencyFormat.format(ct.getThanh_tien())
                    });
                }

                String title = "PHIẾU NHẬP KHO - " +
                        "Số phiếu: " + phieu.getSo_phieu() + " | " +
                        "Ngày: " + dateFormat.format(phieu.getNgay_nhap()) + " | " +
                        "NCC: " + phieu.getNha_cung_cap() + " | " +
                        "Kho: " + getKhoName(phieu.getMa_kho());

                String footer = "Tổng tiền: " + currencyFormat.format(phieu.getTong_tien());

                // Custom column widths
                float[] columnWidths = { 0.5f, 0.8f, 2.5f, 0.8f, 1.2f, 1.2f };

                util.PDFExporter.exportTableToPDF(model, title, footer, file, columnWidths);

                int open = JOptionPane.showConfirmDialog(this,
                        "Xuất PDF thành công! Bạn có muốn mở file ngay không?",
                        "Thông báo", JOptionPane.YES_NO_OPTION);
                if (open == JOptionPane.YES_OPTION) {
                    java.awt.Desktop.getDesktop().open(file);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi xuất PDF: " + e.getMessage());
            }
        }
    }

    private void exportPhieuXuatToPDF() {
        int selectedRow = tablePhieuXuat.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một phiếu xuất để xuất PDF!");
            return;
        }

        PhieuXuat phieu = listPhieuXuat.get(selectedRow);
        List<ChiTietPhieuXuat> chiTietList = phieuXuatDAO.getChiTiet(phieu.getMa_phieu_xuat());

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Lưu file PDF");
        String defaultName = "PhieuXuat_" + phieu.getSo_phieu().replace("/", "_") + ".pdf";
        fileChooser.setSelectedFile(new java.io.File(defaultName));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            java.io.File file = fileChooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".pdf")) {
                file = new java.io.File(file.getAbsolutePath() + ".pdf");
            }

            try {
                // Create table model for PDF
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

                String[] columns = { "STT", "Mã SP", "Tên Sản Phẩm", "Số Lượng", "Đơn Giá", "Thành Tiền" };
                DefaultTableModel model = new DefaultTableModel(columns, 0);

                int stt = 1;
                for (ChiTietPhieuXuat ct : chiTietList) {
                    model.addRow(new Object[] {
                            stt++,
                            ct.getMa_sp(),
                            ct.getTen_sp(),
                            ct.getSo_luong(),
                            currencyFormat.format(ct.getDon_gia()),
                            currencyFormat.format(ct.getThanh_tien())
                    });
                }

                String title = "PHIẾU XUẤT KHO - " +
                        "Số phiếu: " + phieu.getSo_phieu() + " | " +
                        "Ngày: " + dateFormat.format(phieu.getNgay_xuat()) + " | " +
                        "Khách hàng: " + phieu.getKhach_hang() + " | " +
                        "Kho: " + getKhoName(phieu.getMa_kho());

                String footer = "Tổng tiền: " + currencyFormat.format(phieu.getTong_tien());

                // Custom column widths
                float[] columnWidths = { 0.5f, 0.8f, 2.5f, 0.8f, 1.2f, 1.2f };

                util.PDFExporter.exportTableToPDF(model, title, footer, file, columnWidths);

                int open = JOptionPane.showConfirmDialog(this,
                        "Xuất PDF thành công! Bạn có muốn mở file ngay không?",
                        "Thông báo", JOptionPane.YES_NO_OPTION);
                if (open == JOptionPane.YES_OPTION) {
                    java.awt.Desktop.getDesktop().open(file);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi xuất PDF: " + e.getMessage());
            }
        }
    }

    private void cancelPhieuNhap() {
        int selectedRow = tablePhieuNhap.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu cần hủy!");
            return;
        }

        int stt = selectedRow;
        if (stt >= listPhieuNhap.size()) {
            JOptionPane.showMessageDialog(this, "Phiếu không hợp lệ!");
            return;
        }

        PhieuNhap phieu = listPhieuNhap.get(stt);
        String soPhieu = phieu.getSo_phieu();

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn HỦY phiếu nhập " + soPhieu + "?\n" +
                        "Lưu ý: Tồn kho sẽ được revert (trừ đi số lượng đã nhập).",
                "Xác nhận hủy phiếu", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            boolean success = phieuNhapDAO.cancelPhieu(phieu.getMa_phieu_nhap());

            if (success) {
                JOptionPane.showMessageDialog(this, "Hủy phiếu thành công! Tồn kho đã được revert.");
                loadPhieuNhap();
            } else {
                JOptionPane.showMessageDialog(this, "Hủy phiếu thất bại! Phiếu có thể đã bị hủy rồi.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void cancelPhieuXuat() {
        int selectedRow = tablePhieuXuat.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu cần hủy!");
            return;
        }

        int stt = selectedRow;
        if (stt >= listPhieuXuat.size()) {
            JOptionPane.showMessageDialog(this, "Phiếu không hợp lệ!");
            return;
        }

        PhieuXuat phieu = listPhieuXuat.get(stt);
        String soPhieu = phieu.getSo_phieu();

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn HỦY phiếu xuất " + soPhieu + "?\n" +
                        "Lưu ý: Tồn kho sẽ được revert (cộng lại số lượng đã xuất).",
                "Xác nhận hủy phiếu", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            boolean success = phieuXuatDAO.cancelPhieu(phieu.getMa_phieu_xuat());

            if (success) {
                JOptionPane.showMessageDialog(this, "Hủy phiếu thành công! Tồn kho đã được revert.");
                loadPhieuXuat();
            } else {
                JOptionPane.showMessageDialog(this, "Hủy phiếu thất bại! Phiếu có thể đã bị hủy rồi.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
