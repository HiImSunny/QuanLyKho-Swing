package view;

import dao.SaoLuuDAO;
import model.SaoLuu;
import model.User;
import util.DatabaseBackup;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

public class FormSaoLuu extends JFrame {
    private User currentUser;
    private JTextField txtBackupPath;
    private JTextField txtRestorePath;
    private JTable tableHistory;
    private DefaultTableModel modelHistory;
    private JButton btnBrowseBackup, btnBackup, btnBrowseRestore, btnRestore, btnRefresh, btnDelete;
    private SaoLuuDAO saoLuuDAO;

    private String defaultBackupDir = System.getProperty("user.home") + "\\Desktop\\QuanLyKho_Backup";

    public FormSaoLuu(User user) {
        this.currentUser = user;
        this.saoLuuDAO = new SaoLuuDAO();

        setTitle("Sao Lưu & Phục Hồi Database");
        setSize(900, 650);
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

        // Load lịch sử backup từ database
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
        txtBackupPath = new JTextField(defaultPath, 35);
        inputPanel.add(txtBackupPath);

        btnBrowseBackup = new JButton("Browse...");
        btnBrowseBackup.addActionListener(e -> browseBackupPath());
        inputPanel.add(btnBrowseBackup);

        btnBackup = new JButton("Sao lưu ngay");
        btnBackup.setBackground(new Color(40, 167, 69));
        btnBackup.setForeground(Color.BLACK);
        btnBackup.setPreferredSize(new Dimension(120, 30));
        btnBackup.setFocusPainted(false);
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
        txtRestorePath = new JTextField(35);
        txtRestorePath.setEditable(false);
        inputPanel.add(txtRestorePath);

        btnBrowseRestore = new JButton("Browse...");
        btnBrowseRestore.addActionListener(e -> browseRestorePath());
        inputPanel.add(btnBrowseRestore);

        btnRestore = new JButton("Phục hồi");
        btnRestore.setBackground(new Color(255, 193, 7));
        btnRestore.setForeground(Color.BLACK);
        btnRestore.setPreferredSize(new Dimension(120, 30));
        btnRestore.setFocusPainted(false);
        btnRestore.addActionListener(e -> performRestore());
        inputPanel.add(btnRestore);

        panel.add(inputPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Lịch sử Sao Lưu"));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        btnDelete = new JButton("Xóa backup");
        btnDelete.setBackground(new Color(220, 53, 69));
        btnDelete.setForeground(Color.BLACK);
        btnDelete.setFocusPainted(false);
        btnDelete.addActionListener(e -> deleteSelectedBackup());
        topPanel.add(btnDelete);

        btnRefresh = new JButton("Làm mới");
        btnRefresh.addActionListener(e -> loadBackupHistory());
        topPanel.add(btnRefresh);

        panel.add(topPanel, BorderLayout.NORTH);

        String[] columns = { "Tên File", "Kích Thước", "Ngày Sao Lưu", "Người Thực Hiện", "Đường Dẫn" };
        modelHistory = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableHistory = new JTable(modelHistory);
        tableHistory.setRowHeight(25);
        tableHistory.getColumnModel().getColumn(0).setPreferredWidth(180);
        tableHistory.getColumnModel().getColumn(1).setPreferredWidth(80);
        tableHistory.getColumnModel().getColumn(2).setPreferredWidth(130);
        tableHistory.getColumnModel().getColumn(3).setPreferredWidth(120);
        tableHistory.getColumnModel().getColumn(4).setPreferredWidth(250);

        // Double click to load file to restore
        tableHistory.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int row = tableHistory.getSelectedRow();
                    if (row >= 0) {
                        String filePath = modelHistory.getValueAt(row, 4).toString();
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
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đường dẫn lưu file!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate path for whitespace
        String validationError = DatabaseBackup.validatePath(outputPath);
        if (validationError != null) {
            int choice = JOptionPane.showConfirmDialog(this,
                    validationError + "\n\nBạn có muốn tiếp tục?",
                    "Cảnh báo",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (choice != JOptionPane.YES_OPTION) {
                return;
            }
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
            // Save to database
            try {
                File file = new File(outputPath);
                SaoLuu saoLuu = new SaoLuu(
                        file.getName(),
                        outputPath,
                        file.length(),
                        currentUser.getId(),
                        "backup");
                saoLuuDAO.insert(saoLuu);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

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
            JOptionPane.showMessageDialog(this, "Vui lòng chọn file backup để phục hồi!", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if file exists
        File file = new File(inputPath);
        if (!file.exists()) {
            JOptionPane.showMessageDialog(this,
                    "File không tồn tại!\nĐường dẫn: " + inputPath,
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
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

        // Get all backup records from database
        List<SaoLuu> backups = saoLuuDAO.getBackupsOnly();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        for (SaoLuu backup : backups) {
            // Check if file still exists
            File file = new File(backup.getDuongDan());

            if (!file.exists()) {
                // File deleted - remove from database
                saoLuuDAO.deleteById(backup.getMaSaoLuu());
                continue; // Skip this record
            }

            // File exists - add to table
            String tenFile = backup.getTenFile();
            String kichThuoc = backup.getFormattedSize();
            String ngaySaoLuu = sdf.format(backup.getNgayThucHien());
            String nguoiThucHien = "User ID: " + backup.getNguoiThucHien(); // Could join with users table for name
            String duongDan = backup.getDuongDan();

            modelHistory.addRow(new Object[] { tenFile, kichThuoc, ngaySaoLuu, nguoiThucHien, duongDan });
        }

        if (modelHistory.getRowCount() == 0) {
            // Show empty message (optional)
            System.out.println("Chưa có bản sao lưu nào trong database");
        }
    }

    private void deleteSelectedBackup() {
        int selectedRow = tableHistory.getSelectedRow();

        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn một bản sao lưu để xóa!",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String tenFile = modelHistory.getValueAt(selectedRow, 0).toString();
        String duongDan = modelHistory.getValueAt(selectedRow, 4).toString();

        // Confirm deletion
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa bản sao lưu này?\n" +
                        "File: " + tenFile + "\n" +
                        "\nFile sẽ bị xóa khỏi ổ đĩa và database!",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        boolean success = true;
        StringBuilder errorMsg = new StringBuilder();

        // Delete file from disk
        File file = new File(duongDan);
        if (file.exists()) {
            if (!file.delete()) {
                success = false;
                errorMsg.append("- Không thể xóa file từ ổ đĩa\n");
            }
        }

        // Delete record from database
        if (!saoLuuDAO.deleteByPath(duongDan)) {
            success = false;
            errorMsg.append("- Không thể xóa record từ database\n");
        }

        if (success) {
            JOptionPane.showMessageDialog(this,
                    "Đã xóa bản sao lưu thành công!",
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE);
            loadBackupHistory();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Xóa bản sao lưu thất bại!\n" + errorMsg.toString(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
