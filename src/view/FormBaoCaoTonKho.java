package view;

import dao.KhoDAO;
import dao.TonKhoDAO;
import model.Kho;
import model.TonKho;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FormBaoCaoTonKho extends JFrame {
    private User currentUser;
    private JComboBox<String> cboKho;
    private JTextField txtSearch;
    private JCheckBox chkSapHetHang;
    private JTable tableTonKho;
    private DefaultTableModel modelTonKho;
    private JLabel lblTongSoLuong;
    private JLabel lblTongGiaTri;

    private KhoDAO khoDAO;
    private TonKhoDAO tonKhoDAO;
    private List<Kho> listKho;
    private List<TonKho> listTonKho;

    private NumberFormat currencyFormat;

    public FormBaoCaoTonKho(User user) {
        this.currentUser = user;
        khoDAO = new KhoDAO();
        tonKhoDAO = new TonKhoDAO();
        currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        initComponents();
        loadKho();
        loadData(); // Load all data initially
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Báo Cáo Tồn Kho");
        setSize(1200, 700); // Increased size for more columns
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // --- Header Panel ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("BÁO CÁO TỒN KHO CHI TIẾT", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        headerPanel.add(lblTitle, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);

        // --- Filter Panel ---
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Bộ lọc & Tác vụ"));

        // Filter: Kho
        filterPanel.add(new JLabel("Kho:"));
        cboKho = new JComboBox<>();
        cboKho.setPreferredSize(new Dimension(200, 25));
        cboKho.addActionListener(e -> filterData());
        filterPanel.add(cboKho);

        // Filter: Search
        filterPanel.add(new JLabel("Tìm kiếm (Tên SP):"));
        txtSearch = new JTextField(15);
        txtSearch.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                filterData();
            }
        });
        filterPanel.add(txtSearch);

        // Filter: Low Stock
        chkSapHetHang = new JCheckBox("Sắp hết hàng (< 10)");
        chkSapHetHang.addActionListener(e -> filterData());
        filterPanel.add(chkSapHetHang);

        JButton btnLamMoi = new JButton("Làm mới");
        btnLamMoi.addActionListener(e -> {
            cboKho.setSelectedIndex(0);
            txtSearch.setText("");
            chkSapHetHang.setSelected(false);
            loadData();
        });
        filterPanel.add(btnLamMoi);

        // Export Button
        JButton btnExport = new JButton("Xuất CSV");
        btnExport.setBackground(new Color(40, 167, 69));
        btnExport.setForeground(Color.BLACK);
        btnExport.addActionListener(e -> exportToCSV());
        filterPanel.add(btnExport);

        // Print/PDF Button
        JButton btnPrint = new JButton("Xuất PDF");
        btnPrint.setBackground(new Color(0, 123, 255));
        btnPrint.setForeground(Color.WHITE);
        btnPrint.addActionListener(e -> exportToPDF());
        filterPanel.add(btnPrint);

        // Combine Header and Filter
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(lblTitle, BorderLayout.NORTH);
        topPanel.add(filterPanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // --- Table Panel ---
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        // New Columns: STT, Mã SP, Tên SP, ĐVT, Loại, Giá Bán, Kho, Số Lượng, Thành
        // Tiền
        String[] columns = { "STT", "Mã SP", "Tên Sản Phẩm", "ĐVT", "Loại", "Giá Bán", "Kho", "Số Lượng",
                "Thành Tiền" };
        modelTonKho = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                // STT=0, MaSP=1, Qty=7 -> Integer
                if (columnIndex == 0 || columnIndex == 1 || columnIndex == 7)
                    return Integer.class;
                return String.class;
            }
        };

        tableTonKho = new JTable(modelTonKho);
        tableTonKho.setRowHeight(25);

        // Set widths
        tableTonKho.getColumnModel().getColumn(0).setPreferredWidth(50); // STT
        tableTonKho.getColumnModel().getColumn(1).setPreferredWidth(70); // Ma SP
        tableTonKho.getColumnModel().getColumn(2).setPreferredWidth(250); // Ten SP
        tableTonKho.getColumnModel().getColumn(3).setPreferredWidth(80); // DVT
        tableTonKho.getColumnModel().getColumn(4).setPreferredWidth(120); // Loai
        tableTonKho.getColumnModel().getColumn(5).setPreferredWidth(120); // Gia Ban
        tableTonKho.getColumnModel().getColumn(6).setPreferredWidth(150); // Kho
        tableTonKho.getColumnModel().getColumn(7).setPreferredWidth(80); // Qty
        tableTonKho.getColumnModel().getColumn(8).setPreferredWidth(150); // Thanh Tien

        // Custom Renderer (Column 7 is Quantity)
        StockCellRenderer renderer = new StockCellRenderer();
        for (int i = 0; i < tableTonKho.getColumnCount(); i++) {
            tableTonKho.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
        tableTonKho.setAutoCreateRowSorter(true);

        JScrollPane scrollPane = new JScrollPane(tableTonKho);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        add(tablePanel, BorderLayout.CENTER);

        // --- Footer Panel ---
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 5));

        lblTongSoLuong = new JLabel("Tổng số lượng: 0");
        lblTongSoLuong.setFont(new Font("Arial", Font.BOLD, 14));

        lblTongGiaTri = new JLabel("Tổng giá trị tồn: 0 đ");
        lblTongGiaTri.setFont(new Font("Arial", Font.BOLD, 14));
        lblTongGiaTri.setForeground(Color.RED);

        footerPanel.add(lblTongSoLuong);
        footerPanel.add(lblTongGiaTri);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private void loadKho() {
        listKho = khoDAO.getAll();
        cboKho.removeAllItems();
        cboKho.addItem("Tất cả kho");
        for (Kho kho : listKho) {
            cboKho.addItem(kho.getId() + " - " + kho.getTenKho());
        }
    }

    private void loadData() {
        listTonKho = tonKhoDAO.getAll();
        displayData(listTonKho);
    }

    private void filterData() {
        if (listTonKho == null)
            return;

        List<TonKho> filteredList = new ArrayList<>();
        String search = txtSearch.getText().toLowerCase();
        boolean lowStockOnly = chkSapHetHang.isSelected();
        int selectedKhoId = -1;

        if (cboKho.getSelectedIndex() > 0) {
            String selected = cboKho.getSelectedItem().toString();
            selectedKhoId = Integer.parseInt(selected.split(" - ")[0]);
        }

        for (TonKho tk : listTonKho) {
            if (selectedKhoId != -1 && tk.getMaKho() != selectedKhoId)
                continue;
            if (!search.isEmpty() && !tk.getTenSp().toLowerCase().contains(search))
                continue;
            if (lowStockOnly && tk.getSoLuongTon() >= 10)
                continue;
            filteredList.add(tk);
        }

        displayData(filteredList);
    }

    private void displayData(List<TonKho> list) {
        modelTonKho.setRowCount(0);
        int totalQty = 0;
        BigDecimal totalValue = BigDecimal.ZERO;
        int stt = 1;

        for (TonKho tk : list) {
            BigDecimal donGia = tk.getGiaTien() != null ? tk.getGiaTien() : BigDecimal.ZERO;
            BigDecimal thanhTien = donGia.multiply(new BigDecimal(tk.getSoLuongTon()));

            modelTonKho.addRow(new Object[] {
                    stt++,
                    tk.getMaSp(),
                    tk.getTenSp(),
                    tk.getDonViTinh(),
                    tk.getTenLoai(),
                    currencyFormat.format(donGia),
                    tk.getTenKho(),
                    tk.getSoLuongTon(),
                    currencyFormat.format(thanhTien)
            });
            totalQty += tk.getSoLuongTon();
            totalValue = totalValue.add(thanhTien);
        }

        lblTongSoLuong.setText("Tổng số lượng: " + totalQty);
        lblTongGiaTri.setText("Tổng giá trị: " + currencyFormat.format(totalValue));
    }

    private void exportToCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Lưu file CSV");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV Files", "csv"));

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("ddMMyyyy_HHmm");
        String defaultName = "BaoCaoTonKho_" + sdf.format(new java.util.Date()) + ".csv";
        fileChooser.setSelectedFile(new java.io.File(defaultName));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            java.io.File file = fileChooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".csv")) {
                file = new java.io.File(file.getAbsolutePath() + ".csv");
            }

            try (java.io.PrintWriter pw = new java.io.PrintWriter(file, "UTF-8")) {
                pw.write('\ufeff'); // BOM for Excel

                // Header
                for (int i = 0; i < modelTonKho.getColumnCount(); i++) {
                    pw.print(quoteCsv(modelTonKho.getColumnName(i)));
                    if (i < modelTonKho.getColumnCount() - 1)
                        pw.print(",");
                }
                pw.println();

                // Data
                for (int i = 0; i < modelTonKho.getRowCount(); i++) {
                    for (int j = 0; j < modelTonKho.getColumnCount(); j++) {
                        Object value = modelTonKho.getValueAt(i, j);
                        pw.print(quoteCsv(value));
                        if (j < modelTonKho.getColumnCount() - 1)
                            pw.print(",");
                    }
                    pw.println();
                }

                int open = JOptionPane.showConfirmDialog(this,
                        "Xuất file CSV thành công! Bạn có muốn mở file ngay không?",
                        "Thông báo", JOptionPane.YES_NO_OPTION);
                if (open == JOptionPane.YES_OPTION) {
                    Desktop.getDesktop().open(file);
                }

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi xuất file: " + e.getMessage());
            }
        }
    }

    private String quoteCsv(Object value) {
        if (value == null)
            return "";
        String str = value.toString();
        if (str.contains(",") || str.contains("\"") || str.contains("\n")) {
            str = str.replace("\"", "\"\"");
            return "\"" + str + "\"";
        }
        return str;
    }

    private void exportToPDF() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Lưu file PDF");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PDF Files", "pdf"));

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("ddMMyyyy_HHmm");
        String defaultName = "BaoCaoTonKho_" + sdf.format(new java.util.Date()) + ".pdf";
        fileChooser.setSelectedFile(new java.io.File(defaultName));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            java.io.File file = fileChooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".pdf")) {
                file = new java.io.File(file.getAbsolutePath() + ".pdf");
            }

            try {
                // Custom column width ratios for better layout
                // Columns: STT, Mã SP, Tên Sản Phẩm, ĐVT, Loại, Giá Bán, Kho, Số Lượng, Thành
                // Tiền
                float[] columnWidthRatios = {
                        0.5f, // STT - very narrow
                        0.7f, // Mã SP - narrow
                        2.5f, // Tên Sản Phẩm - widest
                        0.7f, // ĐVT - narrow
                        1.0f, // Loại - medium
                        1.0f, // Giá Bán - medium
                        1.2f, // Kho - medium-wide
                        0.7f, // Số Lượng - narrow
                        1.2f // Thành Tiền - medium-wide
                };

                String footer = lblTongSoLuong.getText() + " | " + lblTongGiaTri.getText();
                util.PDFExporter.exportTableToPDF(modelTonKho, "BÁO CÁO TỒN KHO", footer, file, columnWidthRatios);

                int open = JOptionPane.showConfirmDialog(this,
                        "Xuất PDF thành công! Bạn có muốn mở file ngay không?",
                        "Thông báo", JOptionPane.YES_NO_OPTION);
                if (open == JOptionPane.YES_OPTION) {
                    Desktop.getDesktop().open(file);
                }

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Lỗi khi xuất PDF: " + e.getMessage() +
                                "\n\nVui lòng kiểm tra:\n" +
                                "1. Đã cài đặt thư viện PDFBox chưa?\n" +
                                "2. Font Arial có tồn tại tại C:/Windows/Fonts/arial.ttf?");
            }
        }
    }

    private class StockCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            int modelRow = table.convertRowIndexToModel(row);
            // Column "So Luong" is at index 7 now
            int quantity = 0;
            try {
                quantity = (int) modelTonKho.getValueAt(modelRow, 7);
            } catch (Exception ex) {
                quantity = 0;
            }

            if (!isSelected) {
                if (quantity == 0) {
                    c.setBackground(new Color(255, 200, 200));
                } else if (quantity < 10) {
                    c.setBackground(new Color(255, 255, 200));
                } else {
                    c.setBackground(Color.WHITE);
                }
                c.setForeground(Color.BLACK);
            }
            return c;
        }
    }
}
