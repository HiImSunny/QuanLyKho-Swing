package view;

import model.User;
import util.DatabaseBackup;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.List;

public class FormSaoLuu extends JFrame {
    private User currentUser;
    private JTextField txtBackupPath;
    private JTextField txtRestorePath;
    private JTable tableHistory;
    private DefaultTableModel modelHistory;
    private JButton btnBrowseBackup, btnBackup, btnBrowseRestore, btnRestore, btnRefresh;

    private String defaultBackupDir = System.getProperty("user.home") + "\\Desktop\\QuanLyKho_Backup";

    public FormSaoLuu(User user) {
        this.currentUser = user;

        setTitle("Sao Lưu & Phục Hồi Database");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Root panel với padding
        JPanel rootPanel = new JPanel(new BorderLayout(10, 10));
        rootPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Header
        rootPanel.add(createHeaderPanel(), BorderLayout.NORTH);

        // Center
        rootPanel.add(createCenterPanel(), BorderLayout.CENTER);

        add(rootPanel);

        // Tạo thư mục backup mặc định nếu chưa có
        File backupDir = new File(defaultBackupDir);
        if (!backupDir.exists()) {
            backupDir.mkdirs();
        }

        // Load lịch sử backup
        loadBackupHistory();
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("SAO LƯU & PHỤC HỒI DATABASE", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        panel.add(lblTitle, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Top: Backup and Restore panels
        JPanel topPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        topPanel.add(createBackupPanel());
        topPanel.add(createRestorePanel());

        panel.add(topPanel, BorderLayout.NORTH);

        // Bottom: History table
        panel.add(createHistoryPanel(), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createBackupPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Sao lưu Database"));

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        inputPanel.add(new JLabel("Đường dẫn lưu:"));
        String defaultPath = defaultBackupDir + "\\" + DatabaseBackup.generateBackupFilename();
        txtBackupPath = new JTextField(defaultPath, 30);
        inputPanel.add(txtBackupPath);

        btnBrowseBackup = new JButton("Browse...");
        btnBrowseBackup.addActionListener(e -> browseBackupPath());
        inputPanel.add(btnBrowseBackup);

        btnBackup = new JButton("Sao lưu ngay");
        btnBackup.setBackground(new Color(40, 167, 69));
        btnBackup.setForeground(Color.BLACK);
        btnBackup.setPreferredSize(new Dimension(120, 30));
        btnBackup.addActionListener(e -> performBackup());
        inputPanel.add(btnBackup);

        panel.add(inputPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createRestorePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Phục hồi Database"));

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        inputPanel.add(new JLabel("Chọn file backup:"));
        txtRestorePath = new JTextField(30);
        txtRestorePath.setEditable(false);
        inputPanel.add(txtRestorePath);

        btnBrowseRestore = new JButton("Browse...");
        btnBrowseRestore.addActionListener(e -> browseRestorePath());
        inputPanel.add(btnBrowseRestore);

        btnRestore = new JButton("Phục hồi");
        btnRestore.setBackground(new Color(255, 193, 7));
        btnRestore.setForeground(Color.BLACK);
        btnRestore.setPreferredSize(new Dimension(120, 30));
        btnRestore.addActionListener(e -> performRestore());
        inputPanel.add(btnRestore);

        panel.add(inputPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Lịch sử Backup"));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnRefresh = new JButton("Làm mới");
        btnRefresh.addActionListener(e -> loadBackupHistory());
        topPanel.add(btnRefresh);
        panel.add(topPanel, BorderLayout.NORTH);

        String[] columns = { "Tên File", "Kích Thước", "Ngày Tạo", "Đường Dẫn" };
        modelHistory = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableHistory = new JTable(modelHistory);
        tableHistory.setRowHeight(25);
        tableHistory.getColumnModel().getColumn(0).setPreferredWidth(200);
        tableHistory.getColumnModel().getColumn(1).setPreferredWidth(100);
        tableHistory.getColumnModel().getColumn(2).setPreferredWidth(150);
        tableHistory.getColumnModel().getColumn(3).setPreferredWidth(300);

        // Double click to load file to restore
        tableHistory.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int row = tableHistory.getSelectedRow();
                    if (row >= 0) {
                        String filePath = modelHistory.getValueAt(row, 3).toString();
                        txtRestorePath.setText(filePath);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tableHistory);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void browseBackupPath() {
        JFileChooser fileChooser = new JFileChooser(defaultBackupDir);
        fileChooser.setDialogTitle("Chọn nơi lưu file backup");
        fileChooser.setSelectedFile(new File(defaultBackupDir, DatabaseBackup.generateBackupFilename()));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            txtBackupPath.setText(file.getAbsolutePath());
        }
    }

    private void browseRestorePath() {
        JFileChooser fileChooser = new JFileChooser(defaultBackupDir);
        fileChooser.setDialogTitle("Chọn file backup để phục hồi");
        fileChooser.setFileFilter(new FileNameExtensionFilter("SQL Files", "sql"));

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            txtRestorePath.setText(file.getAbsolutePath());
        }
    }

    private void performBackup() {
        String outputPath = txtBackupPath.getText().trim();

        if (outputPath.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đường dẫn lưu file!");
            return;
        }

        // Confirm
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn sao lưu database?",
                "Xác nhận", JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        // Show progress
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        boolean success = DatabaseBackup.backup(outputPath);

        setCursor(Cursor.getDefaultCursor());

        if (success) {
            JOptionPane.showMessageDialog(this,
                    "Sao lưu thành công!\nFile: " + outputPath,
                    "Thành công", JOptionPane.INFORMATION_MESSAGE);

            // Ask to open folder
            int open = JOptionPane.showConfirmDialog(this,
                    "Bạn có muốn mở thư mục chứa file backup?",
                    "Thông báo", JOptionPane.YES_NO_OPTION);

            if (open == JOptionPane.YES_OPTION) {
                try {
                    File file = new File(outputPath);
                    Desktop.getDesktop().open(file.getParentFile());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // Refresh history
            loadBackupHistory();

            // Save to Database History
            try {
                dao.LichSuSaoLuuDAO dao = new dao.LichSuSaoLuuDAO();
                model.LichSuSaoLuu ls = new model.LichSuSaoLuu();
                File f = new File(outputPath);
                ls.setTenFile(f.getName());
                ls.setDuongDan(outputPath);
                dao.insert(ls);

                // Reload again to show new record
                loadBackupHistory();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            // Generate new filename for next backup
            String newPath = defaultBackupDir + "\\" + DatabaseBackup.generateBackupFilename();
            txtBackupPath.setText(newPath);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Sao lưu thất bại!\nVui lòng kiểm tra:\n" +
                            "1. Đường dẫn mysqldump đúng chưa\n" +
                            "2. MySQL server có đang chạy không",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void performRestore() {
        String inputPath = txtRestorePath.getText().trim();

        if (inputPath.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn file backup để phục hồi!");
            return;
        }

        // Strong warning
        int confirm = JOptionPane.showConfirmDialog(this,
                "CẢNH BÁO: Phục hồi sẽ GHI ĐÈ toàn bộ dữ liệu hiện tại!\n" +
                        "Bạn có chắc chắn muốn tiếp tục?",
                "Xác nhận", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        // Show progress
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        boolean success = DatabaseBackup.restore(inputPath);

        setCursor(Cursor.getDefaultCursor());

        if (success) {
            JOptionPane.showMessageDialog(this,
                    "Phục hồi thành công!\nVui lòng khởi động lại ứng dụng.",
                    "Thành công", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Phục hồi thất bại!\nVui lòng kiểm tra:\n" +
                            "1. File backup có hợp lệ không\n" +
                            "2. Đường dẫn mysql đúng chưa\n" +
                            "3. MySQL server có đang chạy không",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadBackupHistory() {
        modelHistory.setRowCount(0);

        // Get directory from current backup path
        String backupPath = txtBackupPath.getText().trim();
        String scanDir = defaultBackupDir;

        if (!backupPath.isEmpty()) {
            File file = new File(backupPath);
            if (file.getParentFile() != null && file.getParentFile().exists()) {
                scanDir = file.getParentFile().getAbsolutePath();
            }
        }

        List<String[]> history = DatabaseBackup.getBackupHistory(scanDir);

        for (String[] record : history) {
            modelHistory.addRow(record);
        }

        if (history.isEmpty()) {
            JLabel lblEmpty = new JLabel("Chưa có file backup nào trong: " + scanDir, JLabel.CENTER);
            lblEmpty.setForeground(Color.GRAY);
        }
    }
}
