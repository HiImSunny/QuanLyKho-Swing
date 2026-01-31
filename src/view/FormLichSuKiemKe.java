package view;

import dao.KhoDAO;
import dao.KiemKeDAO;
import model.ChiTietKiemKe;
import model.KiemKe;
import model.Kho;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class FormLichSuKiemKe extends JFrame {
    private User currentUser;
    private JComboBox<String> cboKho;
    private JTextField txtSearch;
    private JTable tablePhieu;
    private DefaultTableModel modelPhieu;
    private JTable tableChiTiet;
    private DefaultTableModel modelChiTiet;
    private JButton btnFilter, btnRefresh, btnExportCSV, btnExportPDF;

    private KhoDAO khoDAO;
    private KiemKeDAO kiemKeDAO;
    private List<Kho> listKho;
    private List<KiemKe> listKiemKe;

    public FormLichSuKiemKe(User user) {
        this.currentUser = user;

        khoDAO = new KhoDAO();
        kiemKeDAO = new KiemKeDAO();
        listKiemKe = new ArrayList<>();

        setTitle("Lịch Sử Kiểm Kê Kho");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Root panel
        JPanel rootPanel = new JPanel(new BorderLayout(10, 10));
        rootPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header
        rootPanel.add(createHeaderPanel(), BorderLayout.NORTH);

        // Center: Split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(300);
        splitPane.setTopComponent(createPhieuPanel());
        splitPane.setBottomComponent(createChiTietPanel());

        rootPanel.add(splitPane, BorderLayout.CENTER);

        add(rootPanel);

        // Load data
        loadKho();
        loadPhieuKiemKe();
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("LỊCH SỬ KIỂM KÊ KHO", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        panel.add(lblTitle, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createPhieuPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Danh sách phiếu kiểm kê"));

        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        filterPanel.add(new JLabel("Kho:"));
        cboKho = new JComboBox<>();
        cboKho.setPreferredSize(new Dimension(200, 25));
        cboKho.addActionListener(e -> filterPhieu());
        filterPanel.add(cboKho);

        filterPanel.add(new JLabel("Tìm kiếm:"));
        txtSearch = new JTextField(20);
        txtSearch.addActionListener(e -> filterPhieu());
        filterPanel.add(txtSearch);

        btnFilter = new JButton("Lọc");
        btnFilter.addActionListener(e -> filterPhieu());
        filterPanel.add(btnFilter);

        btnRefresh = new JButton("Làm mới");
        btnRefresh.addActionListener(e -> {
            cboKho.setSelectedIndex(0);
            txtSearch.setText("");
            loadPhieuKiemKe();
        });
        filterPanel.add(btnRefresh);

        btnExportCSV = new JButton("Xuất CSV");
        btnExportCSV.setForeground(Color.BLACK);
        btnExportCSV.addActionListener(e -> exportToCSV());
        filterPanel.add(btnExportCSV);

        btnExportPDF = new JButton("Xuất PDF");
        btnExportPDF.setForeground(Color.BLACK);
        btnExportPDF.addActionListener(e -> exportToPDF());
        filterPanel.add(btnExportPDF);

        panel.add(filterPanel, BorderLayout.NORTH);

        // Table
        String[] columns = { "Mã", "Số Phiếu", "Ngày KK", "Kho", "Người KK", "Trạng Thái", "Ghi Chú" };
        modelPhieu = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablePhieu = new JTable(modelPhieu);
        tablePhieu.setRowHeight(25);
        tablePhieu.getColumnModel().getColumn(0).setPreferredWidth(50);
        tablePhieu.getColumnModel().getColumn(1).setPreferredWidth(100);
        tablePhieu.getColumnModel().getColumn(2).setPreferredWidth(100);
        tablePhieu.getColumnModel().getColumn(3).setPreferredWidth(150);
        tablePhieu.getColumnModel().getColumn(4).setPreferredWidth(150);
        tablePhieu.getColumnModel().getColumn(5).setPreferredWidth(100);
        tablePhieu.getColumnModel().getColumn(6).setPreferredWidth(200);

        tablePhieu.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadChiTiet();
            }
        });

        JScrollPane scrollPane = new JScrollPane(tablePhieu);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createChiTietPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Chi tiết phiếu kiểm kê"));

        String[] columns = { "STT", "Mã SP", "Tên SP", "ĐVT", "Tồn HT", "Tồn TT", "Chênh Lệch", "Ghi Chú" };
        modelChiTiet = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableChiTiet = new JTable(modelChiTiet);
        tableChiTiet.setRowHeight(25);
        tableChiTiet.getColumnModel().getColumn(0).setPreferredWidth(50);
        tableChiTiet.getColumnModel().getColumn(1).setPreferredWidth(70);
        tableChiTiet.getColumnModel().getColumn(2).setPreferredWidth(200);
        tableChiTiet.getColumnModel().getColumn(3).setPreferredWidth(60);
        tableChiTiet.getColumnModel().getColumn(4).setPreferredWidth(80);
        tableChiTiet.getColumnModel().getColumn(5).setPreferredWidth(80);
        tableChiTiet.getColumnModel().getColumn(6).setPreferredWidth(100);
        tableChiTiet.getColumnModel().getColumn(7).setPreferredWidth(150);

        JScrollPane scrollPane = new JScrollPane(tableChiTiet);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void loadKho() {
        listKho = khoDAO.getAll();
        cboKho.removeAllItems();
        cboKho.addItem("-- Tất cả kho --");
        for (Kho k : listKho) {
            cboKho.addItem(k.getId() + " - " + k.getTenKho());
        }
    }

    private void loadPhieuKiemKe() {
        listKiemKe = kiemKeDAO.getAll();
        displayPhieu(listKiemKe);
    }

    private void displayPhieu(List<KiemKe> list) {
        modelPhieu.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (KiemKe kk : list) {
            modelPhieu.addRow(new Object[] {
                    kk.getMa_kiem_ke(),
                    kk.getSo_phieu(),
                    sdf.format(kk.getNgay_kiem_ke()),
                    kk.getTen_kho() != null ? kk.getTen_kho() : "",
                    kk.getTen_nguoi_kiem_ke() != null ? kk.getTen_nguoi_kiem_ke() : "",
                    kk.getTrang_thai().equals("hoan_thanh") ? "Hoàn thành" : "Đang kiểm",
                    kk.getGhi_chu() != null ? kk.getGhi_chu() : ""
            });
        }
    }

    private void filterPhieu() {
        String selectedKho = (String) cboKho.getSelectedItem();
        String keyword = txtSearch.getText().toLowerCase().trim();

        List<KiemKe> filtered = new ArrayList<>();
        for (KiemKe kk : listKiemKe) {
            boolean matchKho = selectedKho.equals("-- Tất cả kho --") ||
                    (kk.getTen_kho() != null && selectedKho.contains(String.valueOf(kk.getMa_kho())));
            boolean matchKeyword = keyword.isEmpty() ||
                    kk.getSo_phieu().toLowerCase().contains(keyword) ||
                    (kk.getTen_kho() != null && kk.getTen_kho().toLowerCase().contains(keyword)) ||
                    (kk.getTen_nguoi_kiem_ke() != null && kk.getTen_nguoi_kiem_ke().toLowerCase().contains(keyword));

            if (matchKho && matchKeyword) {
                filtered.add(kk);
            }
        }
        displayPhieu(filtered);
    }

    private void loadChiTiet() {
        int selectedRow = tablePhieu.getSelectedRow();
        if (selectedRow == -1) {
            modelChiTiet.setRowCount(0);
            return;
        }

        int maKiemKe = (int) modelPhieu.getValueAt(selectedRow, 0);
        List<ChiTietKiemKe> chiTietList = kiemKeDAO.getChiTiet(maKiemKe);

        modelChiTiet.setRowCount(0);
        int stt = 1;
        for (ChiTietKiemKe ct : chiTietList) {
            // Highlight rows with differences
            modelChiTiet.addRow(new Object[] {
                    stt++,
                    ct.getMa_sp(),
                    ct.getTen_sp(),
                    ct.getDon_vi_tinh(),
                    ct.getTon_he_thong(),
                    ct.getTon_thuc_te(),
                    ct.getChenh_lech(),
                    ct.getGhi_chu() != null ? ct.getGhi_chu() : ""
            });
        }

        // Highlight rows with differences
        tableChiTiet.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (!isSelected) {
                    int chenhLech = (int) modelChiTiet.getValueAt(row, 6);
                    if (chenhLech < 0) {
                        c.setBackground(new Color(255, 200, 200)); // Red for shortage
                    } else if (chenhLech > 0) {
                        c.setBackground(new Color(200, 255, 200)); // Green for surplus
                    } else {
                        c.setBackground(Color.WHITE);
                    }
                }

                return c;
            }
        });
    }

    private void exportToCSV() {
        if (modelChiTiet.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu kiểm kê để xuất!");
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Lưu file CSV");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV Files", "csv"));

        int selectedRow = tablePhieu.getSelectedRow();
        String soPhieu = modelPhieu.getValueAt(selectedRow, 1).toString();
        String defaultName = "KiemKe_" + soPhieu + ".csv";
        fileChooser.setSelectedFile(new java.io.File(defaultName));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            java.io.File file = fileChooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".csv")) {
                file = new java.io.File(file.getAbsolutePath() + ".csv");
            }

            try (java.io.PrintWriter writer = new java.io.PrintWriter(file, "UTF-8")) {
                writer.write('\ufeff'); // BOM for Excel

                // Header info
                writer.println("Phiếu Kiểm Kê: " + soPhieu);
                writer.println("Ngày: " + modelPhieu.getValueAt(selectedRow, 2));
                writer.println("Kho: " + modelPhieu.getValueAt(selectedRow, 3));
                writer.println("Người kiểm kê: " + modelPhieu.getValueAt(selectedRow, 4));
                writer.println();

                // Table headers
                for (int i = 0; i < modelChiTiet.getColumnCount(); i++) {
                    writer.print(modelChiTiet.getColumnName(i));
                    if (i < modelChiTiet.getColumnCount() - 1)
                        writer.print(",");
                }
                writer.println();

                // Table data
                for (int i = 0; i < modelChiTiet.getRowCount(); i++) {
                    for (int j = 0; j < modelChiTiet.getColumnCount(); j++) {
                        Object value = modelChiTiet.getValueAt(i, j);
                        writer.print(value != null ? value.toString() : "");
                        if (j < modelChiTiet.getColumnCount() - 1)
                            writer.print(",");
                    }
                    writer.println();
                }

                JOptionPane.showMessageDialog(this, "Xuất CSV thành công!");

                int open = JOptionPane.showConfirmDialog(this,
                        "Bạn có muốn mở file?", "Thông báo", JOptionPane.YES_NO_OPTION);
                if (open == JOptionPane.YES_OPTION) {
                    java.awt.Desktop.getDesktop().open(file);
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    private void exportToPDF() {
        if (modelChiTiet.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu kiểm kê để xuất!");
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Lưu file PDF");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PDF Files", "pdf"));

        int selectedRow = tablePhieu.getSelectedRow();
        String soPhieu = modelPhieu.getValueAt(selectedRow, 1).toString();
        String defaultName = "KiemKe_" + soPhieu + ".pdf";
        fileChooser.setSelectedFile(new java.io.File(defaultName));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            java.io.File file = fileChooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".pdf")) {
                file = new java.io.File(file.getAbsolutePath() + ".pdf");
            }

            try {
                String title = "PHIẾU KIỂM KÊ KHO - " + soPhieu + " | Ngày: " + modelPhieu.getValueAt(selectedRow, 2);
                String footer = "Kho: " + modelPhieu.getValueAt(selectedRow, 3) + " | Người kiểm kê: "
                        + modelPhieu.getValueAt(selectedRow, 4);

                float[] columnWidths = { 0.5f, 0.7f, 2.5f, 0.7f, 1f, 1f, 1f, 1.5f };

                util.PDFExporter.exportTableToPDF(tableChiTiet.getModel(), title, footer, file, columnWidths);

                JOptionPane.showMessageDialog(this, "Xuất PDF thành công!");

                int open = JOptionPane.showConfirmDialog(this,
                        "Bạn có muốn mở file?", "Thông báo", JOptionPane.YES_NO_OPTION);
                if (open == JOptionPane.YES_OPTION) {
                    java.awt.Desktop.getDesktop().open(file);
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
}
