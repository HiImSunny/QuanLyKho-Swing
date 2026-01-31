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
import java.util.Date;
import java.util.List;

public class FormKiemKe extends JFrame {
    private User currentUser;
    private JTextField txtSoPhieu;
    private JTextField txtNgayKiemKe;
    private JComboBox<String> cboKho;
    private JTextArea txtGhiChu;
    private JTable tableChiTiet;
    private DefaultTableModel modelChiTiet;
    private JButton btnLoadTonKho, btnLuu, btnHuy;

    private KhoDAO khoDAO;
    private KiemKeDAO kiemKeDAO;
    private List<Kho> listKho;

    public FormKiemKe(User user) {
        this.currentUser = user;

        khoDAO = new KhoDAO();
        kiemKeDAO = new KiemKeDAO();

        setTitle("Kiểm Kê Kho Hàng");
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
        generateSoPhieu();
        setDefaultDate();
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("KIỂM KÊ KHO HÀNG", JLabel.CENTER);
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
        panel.setBorder(BorderFactory.createTitledBorder("Thông tin phiếu kiểm kê"));
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
        panel.add(new JLabel("Ngày kiểm kê:"), gbc);
        gbc.gridx = 3;
        txtNgayKiemKe = new JTextField(20);
        panel.add(txtNgayKiemKe, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Kho kiểm kê:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        cboKho = new JComboBox<>();
        panel.add(cboKho, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
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
        panel.setBorder(BorderFactory.createTitledBorder("Chi tiết kiểm kê"));

        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        btnLoadTonKho = new JButton("Load Tồn Kho");
        btnLoadTonKho.setBackground(new Color(0, 123, 255));
        btnLoadTonKho.setForeground(Color.BLACK);
        btnLoadTonKho.addActionListener(e -> loadTonKho());
        pnlTop.add(btnLoadTonKho);

        JLabel lblNote = new JLabel("(Nhấp đúp vào ô 'Tồn Thực Tế' để nhập số lượng)");
        lblNote.setForeground(Color.BLUE);
        lblNote.setFont(new Font("Arial", Font.ITALIC, 12));
        pnlTop.add(lblNote);

        panel.add(pnlTop, BorderLayout.NORTH);

        String[] columns = { "STT", "Mã SP", "Tên sản phẩm", "ĐVT", "Tồn HT", "Tồn Thực Tế", "Chênh Lệch", "Ghi chú" };
        modelChiTiet = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Chỉ cho phép edit cột "Tồn Thực Tế" (column 5) và "Ghi chú" (column 7)
                return column == 5 || column == 7;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 4 || columnIndex == 5 || columnIndex == 6) {
                    return Integer.class;
                }
                return String.class;
            }
        };

        tableChiTiet = new JTable(modelChiTiet);
        tableChiTiet.setRowHeight(25);
        tableChiTiet.getColumnModel().getColumn(0).setPreferredWidth(50);
        tableChiTiet.getColumnModel().getColumn(1).setPreferredWidth(70);
        tableChiTiet.getColumnModel().getColumn(2).setPreferredWidth(200);
        tableChiTiet.getColumnModel().getColumn(3).setPreferredWidth(60);
        tableChiTiet.getColumnModel().getColumn(4).setPreferredWidth(80);
        tableChiTiet.getColumnModel().getColumn(5).setPreferredWidth(100);
        tableChiTiet.getColumnModel().getColumn(6).setPreferredWidth(100);
        tableChiTiet.getColumnModel().getColumn(7).setPreferredWidth(150);

        // Tự động tính chênh lệch khi nhập tồn thực tế
        modelChiTiet.addTableModelListener(e -> {
            if (e.getColumn() == 5) { // Cột "Tồn Thực Tế"
                int row = e.getFirstRow();
                try {
                    int tonHT = (int) modelChiTiet.getValueAt(row, 4);
                    Object tonTTObj = modelChiTiet.getValueAt(row, 5);
                    int tonTT = tonTTObj != null ? Integer.parseInt(tonTTObj.toString()) : 0;
                    int chenhLech = tonTT - tonHT;
                    modelChiTiet.setValueAt(chenhLech, row, 6);
                } catch (Exception ex) {
                    // Ignore invalid input
                }
            }
        });

        JScrollPane scrollTable = new JScrollPane(tableChiTiet);
        panel.add(scrollTable, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createFooterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        btnLuu = new JButton("Lưu Kiểm Kê");
        btnLuu.setPreferredSize(new Dimension(150, 35));
        btnLuu.setBackground(new Color(40, 167, 69));
        btnLuu.setForeground(Color.BLACK);
        btnLuu.addActionListener(e -> luuKiemKe());
        panel.add(btnLuu);

        btnHuy = new JButton("Làm mới");
        btnHuy.setPreferredSize(new Dimension(150, 35));
        btnHuy.setBackground(new Color(108, 117, 125));
        btnHuy.setForeground(Color.BLACK);
        btnHuy.addActionListener(e -> lamMoi());
        panel.add(btnHuy);

        return panel;
    }

    private void loadKho() {
        listKho = khoDAO.getAll();
        cboKho.removeAllItems();
        cboKho.addItem("-- Chọn kho kiểm kê --");
        for (Kho kho : listKho) {
            cboKho.addItem(kho.getId() + " - " + kho.getTenKho());
        }
    }

    private void generateSoPhieu() {
        String soPhieu = kiemKeDAO.generateSoPhieu();
        txtSoPhieu.setText(soPhieu);
    }

    private void setDefaultDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        txtNgayKiemKe.setText(sdf.format(new Date()));
    }

    private void loadTonKho() {
        if (cboKho.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn kho kiểm kê!");
            return;
        }

        String selectedKho = cboKho.getSelectedItem().toString();
        int maKho = Integer.parseInt(selectedKho.split(" - ")[0]);

        List<ChiTietKiemKe> tonKhoList = kiemKeDAO.getTonKhoTheoKho(maKho);

        if (tonKhoList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Kho này không có sản phẩm nào!");
            return;
        }

        modelChiTiet.setRowCount(0);
        int stt = 1;
        for (ChiTietKiemKe ct : tonKhoList) {
            modelChiTiet.addRow(new Object[] {
                    stt++,
                    ct.getMa_sp(),
                    ct.getTen_sp(),
                    ct.getDon_vi_tinh(),
                    ct.getTon_he_thong(),
                    ct.getTon_he_thong(), // Pre-fill tồn thực tế = tồn hệ thống
                    0, // Chênh lệch ban đầu = 0
                    ""
            });
        }

        JOptionPane.showMessageDialog(this,
                "Đã load " + tonKhoList.size()
                        + " sản phẩm.\nSố lượng thực tế đã được điền sẵn = tồn hệ thống.\nVui lòng kiểm tra và sửa những chỗ khác biệt.");
    }

    private void luuKiemKe() {
        try {
            if (cboKho.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn kho kiểm kê!");
                return;
            }

            if (modelChiTiet.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng load tồn kho trước!");
                return;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            java.util.Date utilDate = sdf.parse(txtNgayKiemKe.getText().trim());
            Date ngayKiemKe = new Date(utilDate.getTime());

            String selectedKho = cboKho.getSelectedItem().toString();
            int maKho = Integer.parseInt(selectedKho.split(" - ")[0]);

            int nguoiKiemKe = currentUser.getId();

            KiemKe kiemKe = new KiemKe(
                    txtSoPhieu.getText(),
                    ngayKiemKe,
                    maKho,
                    nguoiKiemKe,
                    "hoan_thanh",
                    txtGhiChu.getText().trim());

            List<ChiTietKiemKe> chiTietList = new ArrayList<>();
            for (int i = 0; i < modelChiTiet.getRowCount(); i++) {
                int maSP = (int) modelChiTiet.getValueAt(i, 1);
                int tonHT = (int) modelChiTiet.getValueAt(i, 4);
                Object tonTTObj = modelChiTiet.getValueAt(i, 5);
                int tonTT = tonTTObj != null ? Integer.parseInt(tonTTObj.toString()) : 0;
                String ghiChu = modelChiTiet.getValueAt(i, 7) != null ? modelChiTiet.getValueAt(i, 7).toString() : "";

                ChiTietKiemKe ct = new ChiTietKiemKe(0, maSP, tonHT, tonTT, ghiChu);
                chiTietList.add(ct);
            }

            boolean success = kiemKeDAO.insert(kiemKe, chiTietList);

            if (success) {
                JOptionPane.showMessageDialog(this, "Lưu phiếu kiểm kê thành công!");
                lamMoi();
            } else {
                JOptionPane.showMessageDialog(this, "Lưu phiếu kiểm kê thất bại!");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void lamMoi() {
        generateSoPhieu();
        setDefaultDate();
        cboKho.setSelectedIndex(0);
        txtGhiChu.setText("");
        modelChiTiet.setRowCount(0);
    }
}
