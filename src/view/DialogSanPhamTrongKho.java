package view;

import dao.TonKhoDAO;
import model.TonKho;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Dialog hiển thị danh sách sản phẩm trong một kho
 */
public class DialogSanPhamTrongKho extends JDialog {

    private JTable tableSanPham;
    private DefaultTableModel tableModel;
    private TonKhoDAO tonKhoDAO;

    public DialogSanPhamTrongKho(JFrame parent, int maKho, String tenKho) {
        super(parent, "Sản Phẩm Trong Kho - " + tenKho, true);
        tonKhoDAO = new TonKhoDAO();

        setSize(800, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // Root panel với padding
        JPanel rootPanel = new JPanel(new BorderLayout(10, 10));
        rootPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Header
        rootPanel.add(createHeaderPanel(maKho, tenKho), BorderLayout.NORTH);

        // Table
        rootPanel.add(createTablePanel(), BorderLayout.CENTER);

        // Footer
        rootPanel.add(createFooterPanel(), BorderLayout.SOUTH);

        add(rootPanel);

        // Load data
        loadData(maKho);
    }

    private JPanel createHeaderPanel(int maKho, String tenKho) {
        JPanel panel = new JPanel(new GridLayout(2, 1, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Thông tin kho"));

        JLabel lblMaKho = new JLabel("Mã kho: " + maKho);
        lblMaKho.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel lblTenKho = new JLabel("Tên kho: " + tenKho);
        lblTenKho.setFont(new Font("Arial", Font.BOLD, 14));

        panel.add(lblMaKho);
        panel.add(lblTenKho);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Danh sách sản phẩm"));

        String[] columns = { "STT", "Mã SP", "Tên sản phẩm", "Tồn kho", "Cập nhật" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableSanPham = new JTable(tableModel);
        tableSanPham.getColumnModel().getColumn(0).setPreferredWidth(50);
        tableSanPham.getColumnModel().getColumn(1).setPreferredWidth(80);
        tableSanPham.getColumnModel().getColumn(2).setPreferredWidth(300);
        tableSanPham.getColumnModel().getColumn(3).setPreferredWidth(100);
        tableSanPham.getColumnModel().getColumn(4).setPreferredWidth(150);

        JScrollPane scrollPane = new JScrollPane(tableSanPham);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createFooterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton btnClose = new JButton("Đóng");
        btnClose.setPreferredSize(new Dimension(100, 30));
        btnClose.addActionListener(e -> dispose());

        panel.add(btnClose);

        return panel;
    }

    private void loadData(int maKho) {
        List<TonKho> list = tonKhoDAO.getByMaKho(maKho);
        tableModel.setRowCount(0);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        for (int i = 0; i < list.size(); i++) {
            TonKho tk = list.get(i);
            tableModel.addRow(new Object[] {
                    i + 1,
                    tk.getMaSp(),
                    tk.getTenSp(),
                    tk.getSoLuongTon(),
                    sdf.format(tk.getNgayCapNhat())
            });
        }

        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Kho chưa có sản phẩm nào!",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
