package view;

import dao.SanPhamDAO;
import model.SanPham;
import model.User;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class FormMain extends JFrame {

    private User currentUser;
    private JLabel lblWelcome;
    private JPanel contentPanel;
    private SanPhamDAO sanPhamDAO;

    public FormMain(User user) {
        this.currentUser = user;
        this.sanPhamDAO = new SanPhamDAO();
        initComponents();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Hệ thống quản lý kho hàng");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Menu Bar
        createMenuBar();

        // Top Panel - Hiển thị thông tin user
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setBackground(new Color(52, 58, 64));
        topPanel.setPreferredSize(new Dimension(getWidth(), 45));

        lblWelcome = new JLabel("  Xin chào: " + currentUser.getHoTen() + " (" + currentUser.getRole() + ")  ");
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 14));
        lblWelcome.setForeground(Color.WHITE);
        topPanel.add(lblWelcome);

        add(topPanel, BorderLayout.NORTH);

        // Content Panel - Dashboard mặc định
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(240, 242, 245));
        add(contentPanel, BorderLayout.CENTER);

        // Hiển thị Dashboard ngay khi vào
        showDashboard();

        // Status Bar
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBackground(new Color(248, 249, 250));
        bottomPanel.setPreferredSize(new Dimension(getWidth(), 30));

        JLabel lblStatus = new JLabel("  Sẵn sàng");
        lblStatus.setFont(new Font("Arial", Font.PLAIN, 12));
        bottomPanel.add(lblStatus);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Hiển thị Dashboard (trang chủ)
     */
    private void showDashboard() {
        contentPanel.removeAll();

        JPanel dashboardPanel = new JPanel();
        dashboardPanel.setLayout(new BorderLayout(15, 15));
        dashboardPanel.setBackground(new Color(240, 242, 245));
        dashboardPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel lblTitle = new JLabel("TRANG CHỦ - TỔNG QUAN KHO HÀNG");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        dashboardPanel.add(lblTitle, BorderLayout.NORTH);

        // Thống kê cards
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setBackground(new Color(240, 242, 245));

        // Tổng số sản phẩm
        List<SanPham> allProducts = sanPhamDAO.getAll();
        int totalProducts = allProducts.size();
        JPanel card1 = createStatCard("Tổng sản phẩm", String.valueOf(totalProducts), new Color(0, 123, 255));
        statsPanel.add(card1);

        // Sản phẩm tồn kho thấp (< 20)
        long lowStock = allProducts.stream().filter(sp -> sp.getSoLuongTon() < 20).count();
        JPanel card2 = createStatCard("Tồn kho thấp", String.valueOf(lowStock), new Color(220, 53, 69));
        statsPanel.add(card2);

        // Tổng giá trị tồn kho
        double totalValue = allProducts.stream()
                .mapToDouble(sp -> sp.getGiaBan().doubleValue() * sp.getSoLuongTon())
                .sum();
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        JPanel card3 = createStatCard("Giá trị tồn kho", currencyFormat.format(totalValue), new Color(40, 167, 69));
        statsPanel.add(card3);

        dashboardPanel.add(statsPanel, BorderLayout.CENTER);

        // Bảng sản phẩm tồn kho thấp
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createTitledBorder("Sản phẩm tồn kho thấp (< 20)"));

        String[] columns = { "Mã SP", "Tên sản phẩm", "Loại", "Đơn vị", "Tồn kho", "Giá bán" };
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        table.setRowHeight(25);
        table.setAutoCreateRowSorter(true);

        allProducts.stream()
                .filter(sp -> sp.getSoLuongTon() < 20)
                .forEach(sp -> {
                    Object[] row = {
                            sp.getMaSp(),
                            sp.getTenSp(),
                            sp.getTenLoai(),
                            sp.getDonViTinh(),
                            sp.getSoLuongTon(),
                            currencyFormat.format(sp.getGiaBan())
                    };
                    model.addRow(row);
                });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(900, 250));
        tablePanel.add(scrollPane);

        dashboardPanel.add(tablePanel, BorderLayout.SOUTH);

        contentPanel.add(dashboardPanel);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    /**
     * Tạo card thống kê
     */
    private JPanel createStatCard(String title, String value, Color bgColor) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(bgColor);
        card.setBorder(BorderFactory.createLineBorder(bgColor.darker(), 2));
        card.setPreferredSize(new Dimension(200, 120));

        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 10, 5, 10));

        JLabel lblValue = new JLabel(value, SwingConstants.CENTER);
        lblValue.setFont(new Font("Arial", Font.BOLD, 28));
        lblValue.setForeground(Color.WHITE);
        lblValue.setBorder(BorderFactory.createEmptyBorder(5, 10, 15, 10));

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);

        return card;
    }

    /**
     * Tạo Menu Bar
     */
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.WHITE);

        // Menu Hệ thống
        JMenu menuHeThong = new JMenu("Hệ thống");
        menuHeThong.setFont(new Font("Arial", Font.PLAIN, 14));

        JMenuItem itemTrangChu = new JMenuItem("Trang chủ");
        itemTrangChu.setFont(new Font("Arial", Font.PLAIN, 13));
        itemTrangChu.addActionListener(e -> showDashboard());

        JMenuItem itemDoiMatKhau = new JMenuItem("Đổi mật khẩu");
        itemDoiMatKhau.setFont(new Font("Arial", Font.PLAIN, 13));
        itemDoiMatKhau.addActionListener(e -> {
            new FormDoiMatKhau(currentUser).setVisible(true);
        });

        JMenuItem itemSaoLuu = new JMenuItem("Sao lưu dữ liệu");
        itemSaoLuu.setFont(new Font("Arial", Font.PLAIN, 13));
        itemSaoLuu.addActionListener(e -> {
            new FormSaoLuu(currentUser).setVisible(true);
        });

        JMenuItem itemDangXuat = new JMenuItem("Đăng xuất");
        itemDangXuat.setFont(new Font("Arial", Font.PLAIN, 13));
        itemDangXuat.addActionListener(e -> dangXuat());

        JMenuItem itemThoat = new JMenuItem("Thoát");
        itemThoat.setFont(new Font("Arial", Font.PLAIN, 13));
        itemThoat.addActionListener(e -> System.exit(0));

        menuHeThong.add(itemTrangChu);
        menuHeThong.addSeparator();
        menuHeThong.add(itemDoiMatKhau);
        menuHeThong.add(itemSaoLuu);
        menuHeThong.addSeparator();
        menuHeThong.add(itemDangXuat);
        menuHeThong.add(itemThoat);

        // Menu Quản lý
        JMenu menuQuanLy = new JMenu("Quản lý");
        menuQuanLy.setFont(new Font("Arial", Font.PLAIN, 14));

        JMenuItem itemQuanLySanPham = new JMenuItem("Quản lý sản phẩm");
        itemQuanLySanPham.setFont(new Font("Arial", Font.PLAIN, 13));
        itemQuanLySanPham.addActionListener(e -> {
            new FormQuanLySanPham().setVisible(true);
        });

        JMenuItem itemQuanLyLoaiSP = new JMenuItem("Quản lý loại sản phẩm");
        itemQuanLyLoaiSP.setFont(new Font("Arial", Font.PLAIN, 13));
        itemQuanLyLoaiSP.addActionListener(e -> {
            new FormQuanLyLoaiSP().setVisible(true);
        });

        JMenuItem itemQuanLyKho = new JMenuItem("Quản lý kho");
        itemQuanLyKho.setFont(new Font("Arial", Font.PLAIN, 13));
        itemQuanLyKho.addActionListener(e -> {
            new FormQuanLyKho().setVisible(true);
        });

        JMenuItem itemQuanLyKH = new JMenuItem("Quản lý khách hàng");
        itemQuanLyKH.setFont(new Font("Arial", Font.PLAIN, 13));
        itemQuanLyKH.addActionListener(e -> {
            new FormQuanLyKhachHang().setVisible(true);
        });

        JMenuItem itemQuanLyNCC = new JMenuItem("Quản lý nhà cung cấp");
        itemQuanLyNCC.setFont(new Font("Arial", Font.PLAIN, 13));
        itemQuanLyNCC.addActionListener(e -> {
            new FormQuanLyNhaCungCap().setVisible(true);
        });

        JMenuItem itemQuanLyUser = new JMenuItem("Quản lý người dùng");
        itemQuanLyUser.setFont(new Font("Arial", Font.PLAIN, 13));
        itemQuanLyUser.addActionListener(e -> {
            if (currentUser.getRole().equals("admin")) {
                new FormQuanLyNguoiDung(currentUser).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Bạn không có quyền!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        menuQuanLy.add(itemQuanLySanPham);
        menuQuanLy.add(itemQuanLyLoaiSP);
        menuQuanLy.add(itemQuanLyKho);
        menuQuanLy.addSeparator();
        menuQuanLy.add(itemQuanLyKH);
        menuQuanLy.add(itemQuanLyNCC);
        menuQuanLy.addSeparator();
        menuQuanLy.add(itemQuanLyUser);

        // Menu Nghiệp vụ
        JMenu menuNghiepVu = new JMenu("Nghiệp vụ");
        menuNghiepVu.setFont(new Font("Arial", Font.PLAIN, 14));

        JMenuItem itemNhapKho = new JMenuItem("Nhập kho");
        itemNhapKho.setFont(new Font("Arial", Font.PLAIN, 13));
        itemNhapKho.addActionListener(e -> {
            new FormNhapKho(currentUser).setVisible(true);
        });

        JMenuItem itemXuatKho = new JMenuItem("Xuất kho");
        itemXuatKho.setFont(new Font("Arial", Font.PLAIN, 13));
        itemXuatKho.addActionListener(e -> {
            new FormXuatKho(currentUser).setVisible(true);
        });

        JMenuItem itemKiemKe = new JMenuItem("Kiểm kê kho");
        itemKiemKe.setFont(new Font("Arial", Font.PLAIN, 13));
        itemKiemKe.addActionListener(e -> {
            new FormKiemKe(currentUser).setVisible(true);
        });

        JMenuItem itemLichSu = new JMenuItem("Lịch sử nhập/xuất");
        itemLichSu.setFont(new Font("Arial", Font.PLAIN, 13));
        itemLichSu.addActionListener(e -> {
            new FormLichSuNhapXuat(currentUser).setVisible(true);
        });

        JMenuItem itemLichSuKiemKe = new JMenuItem("Lịch sử kiểm kê");
        itemLichSuKiemKe.setFont(new Font("Arial", Font.PLAIN, 13));
        itemLichSuKiemKe.addActionListener(e -> {
            new FormLichSuKiemKe(currentUser).setVisible(true);
        });

        menuNghiepVu.add(itemNhapKho);
        menuNghiepVu.add(itemXuatKho);
        menuNghiepVu.add(itemKiemKe);
        menuNghiepVu.addSeparator();
        menuNghiepVu.add(itemLichSu);
        menuNghiepVu.add(itemLichSuKiemKe);

        // Menu Báo cáo
        JMenu menuBaoCao = new JMenu("Báo cáo");
        menuBaoCao.setFont(new Font("Arial", Font.PLAIN, 14));

        JMenuItem itemBaoCaoTonKho = new JMenuItem("Báo cáo tồn kho");
        itemBaoCaoTonKho.setFont(new Font("Arial", Font.PLAIN, 13));
        itemBaoCaoTonKho.addActionListener(e -> {
            new FormBaoCaoTonKho(currentUser).setVisible(true);
        });

        menuBaoCao.add(itemBaoCaoTonKho);

        // Menu Trợ giúp
        JMenu menuHelp = new JMenu("Trợ giúp");
        menuHelp.setFont(new Font("Arial", Font.PLAIN, 14));

        JMenuItem itemAbout = new JMenuItem("Về phần mềm");
        itemAbout.setFont(new Font("Arial", Font.PLAIN, 13));
        itemAbout.addActionListener(e -> showAbout());

        menuHelp.add(itemAbout);

        // Add menus
        menuBar.add(menuHeThong);
        menuBar.add(menuQuanLy);
        menuBar.add(menuNghiepVu);
        menuBar.add(menuBaoCao);
        menuBar.add(menuHelp);

        setJMenuBar(menuBar);
    }

    public void dangXuat() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn đăng xuất?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            this.dispose();
            new FormDangNhap().setVisible(true);
        }
    }

    private void showAbout() {
        String info = "HỆ THỐNG QUẢN LÝ KHO HÀNG\n\n" +
                "Phiên bản: 1.4.0\n" +
                "Công nghệ: Java Swing + MySQL\n" +
                "Năm: 2026\n\n" +
                "Phát triển bởi: adselvn & meankhoiii\n" +
                "DNC University";

        JOptionPane.showMessageDialog(this, info, "Về phần mềm", JOptionPane.INFORMATION_MESSAGE);
    }
}
