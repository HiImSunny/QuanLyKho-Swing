package view;

import dao.TonKhoDAO;
import model.TonKho;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Dialog hiển thị chi tiết tồn kho của một sản phẩm theo từng kho
 */
public class DialogTonKhoTheoKho extends JDialog {

    private JTable tableTonKho;
    private DefaultTableModel tableModel;
    private TonKhoDAO tonKhoDAO;

    public DialogTonKhoTheoKho(JFrame parent, int maSp, String tenSp) {
        super(parent, "Chi Tiết Tồn Kho - " + tenSp, true);
        tonKhoDAO = new TonKhoDAO();

        setSize(700, 450);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // Root panel với padding
        JPanel rootPanel = new JPanel(new BorderLayout(10, 10));
        rootPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Header
        rootPanel.add(createHeaderPanel(maSp, tenSp), BorderLayout.NORTH);

        // Table
        rootPanel.add(createTablePanel(), BorderLayout.CENTER);

        // Footer
        rootPanel.add(createFooterPanel(), BorderLayout.SOUTH);

        add(rootPanel);

        // Load data
        loadData(maSp);
    }

    private JPanel createHeaderPanel(int maSp, String tenSp) {
        JPanel panel = new JPanel(new GridLayout(3, 1, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Thông tin sản phẩm"));

        JLabel lblMaSp = new JLabel("Mã SP: " + maSp);
        lblMaSp.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel lblTenSp = new JLabel("Tên SP: " + tenSp);
        lblTenSp.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel lblTongTon = new JLabel("Tổng tồn kho: Đang tính...");
        lblTongTon.setFont(new Font("Arial", Font.PLAIN, 14));
        lblTongTon.setName("lblTongTon"); // Để cập nhật sau

        panel.add(lblMaSp);
        panel.add(lblTenSp);
        panel.add(lblTongTon);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Tồn kho theo kho"));

        String[] columns = { "STT", "Kho", "Tồn kho", "Cập nhật" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableTonKho = new JTable(tableModel);
        tableTonKho.getColumnModel().getColumn(0).setPreferredWidth(50);
        tableTonKho.getColumnModel().getColumn(1).setPreferredWidth(250);
        tableTonKho.getColumnModel().getColumn(2).setPreferredWidth(100);
        tableTonKho.getColumnModel().getColumn(3).setPreferredWidth(150);

        JScrollPane scrollPane = new JScrollPane(tableTonKho);
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

    private void loadData(int maSp) {
        List<TonKho> list = tonKhoDAO.getByMaSp(maSp);
        tableModel.setRowCount(0);

        int tongTon = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        for (int i = 0; i < list.size(); i++) {
            TonKho tk = list.get(i);
            tableModel.addRow(new Object[] {
                    i + 1,
                    tk.getTenKho(),
                    tk.getSoLuongTon(),
                    sdf.format(tk.getNgayCapNhat())
            });
            tongTon += tk.getSoLuongTon();
        }

        // Cập nhật tổng tồn
        Component[] components = ((JPanel) ((JPanel) getContentPane().getComponent(0)).getComponent(0)).getComponents();
        for (Component comp : components) {
            if (comp instanceof JLabel && comp.getName() != null && comp.getName().equals("lblTongTon")) {
                ((JLabel) comp).setText("Tổng tồn kho: " + tongTon + " cái");
                ((JLabel) comp).setForeground(tongTon > 0 ? Color.BLUE : Color.RED);
                break;
            }
        }

        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Sản phẩm chưa có tồn kho trong kho nào!",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
