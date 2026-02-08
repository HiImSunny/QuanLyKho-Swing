package view;

import util.DatabaseBackup;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.text.SimpleDateFormat;

public class FormSaoLuu extends JFrame {
    private JTextField txtBackupPath;
    private JTextField txtRestorePath;
    private JTable tableHistory;
    private DefaultTableModel modelHistory;
    private JButton btnBrowseBackup, btnBackup, btnBrowseRestore, btnRestore, btnRefresh, btnDelete;

    private String defaultBackupDir = System.getProperty("user.home") + "\\Desktop\\QuanLyKho_Backup";

    public FormSaoLuu() {

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

        String[] columns = { "Tên File", "Kích Thước", "Ngày Sao Lưu", "Đường Dẫn" };
        modelHistory = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableHistory = new JTable(modelHistory);
        tableHistory.setRowHeight(25);
        tableHistory.getColumnModel().getColumn(0).setPreferredWidth(200);
        tableHistory.getColumnModel().getColumn(1).setPreferredWidth(80);
        tableHistory.getColumnModel().getColumn(2).setPreferredWidth(130);
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

        // Calculate time elapsed since backup file was created
        long fileTime = file.lastModified();
        long currentTime = System.currentTimeMillis();
        long diffMs = currentTime - fileTime;

        long diffSeconds = diffMs / 1000;
        long diffMinutes = diffSeconds / 60;
        long diffHours = diffMinutes / 60;
        long diffDays = diffHours / 24;

        String timeElapsed;
        String warningLevel = "";
        if (diffDays > 0) {
            long remainingHours = diffHours % 24;
            timeElapsed = diffDays + " ngày " + remainingHours + " giờ trước";
            if (diffDays >= 7) {
                warningLevel = "⚠️ CẢNH BÁO: File backup đã khá cũ!\n\n";
            }
        } else if (diffHours > 0) {
            long remainingMinutes = diffMinutes % 60;
            timeElapsed = diffHours + " giờ " + remainingMinutes + " phút trước";
        } else if (diffMinutes > 0) {
            timeElapsed = diffMinutes + " phút trước";
        } else {
            timeElapsed = "Vừa mới tạo";
        }

        // Format file size for display
        long fileSize = file.length();
        String fileSizeStr;
        if (fileSize > 1024 * 1024) {
            fileSizeStr = String.format("%.2f MB", fileSize / (1024.0 * 1024.0));
        } else if (fileSize > 1024) {
            fileSizeStr = String.format("%.2f KB", fileSize / 1024.0);
        } else {
            fileSizeStr = fileSize + " bytes";
        }

        // Strong warning with time info
        int confirm = JOptionPane.showConfirmDialog(this,
                warningLevel +
                        "🔄 PHỤC HỒI DATABASE\n" +
                        "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n" +
                        "📁 File: " + file.getName() + "\n" +
                        "📊 Kích thước: " + fileSizeStr + "\n" +
                        "⏰ Thời gian backup: " + timeElapsed + "\n\n" +
                        "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                        "⚠️ CẢNH BÁO:\n" +
                        "• Toàn bộ dữ liệu hiện tại sẽ bị GHI ĐÈ!\n" +
                        "• Một bản backup tự động sẽ được tạo trước khi phục hồi\n" +
                        "• Ứng dụng sẽ cần khởi động lại sau khi phục hồi\n\n" +
                        "Bạn có chắc chắn muốn tiếp tục?",
                "Xác nhận Phục Hồi", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        // Show progress
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        // Step 1: Create auto backup before restore
        String autoBackupPath = defaultBackupDir + "\\AUTO_BACKUP_BEFORE_RESTORE_" +
                new SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date()) + ".sql";

        boolean backupSuccess = DatabaseBackup.backup(autoBackupPath);

        if (backupSuccess) {
            // Auto backup created successfully - will appear in file history
        } else {
            setCursor(Cursor.getDefaultCursor());
            int proceed = JOptionPane.showConfirmDialog(this,
                    "⚠️ Không thể tạo bản sao lưu tự động!\n\n" +
                            "Bạn có muốn tiếp tục phục hồi mà KHÔNG có backup?\n" +
                            "(Dữ liệu hiện tại sẽ mất vĩnh viễn nếu có lỗi)",
                    "Cảnh báo nghiêm trọng",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.ERROR_MESSAGE);

            if (proceed != JOptionPane.YES_OPTION) {
                return;
            }
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        }

        // Step 2: Perform restore
        boolean success = DatabaseBackup.restore(inputPath);

        setCursor(Cursor.getDefaultCursor());

        if (success) {
            // Refresh history to show the auto backup
            loadBackupHistory();

            // Show success message with restart option
            String successMsg = "✅ Phục hồi thành công!\n\n";
            if (backupSuccess) {
                successMsg += "📦 Đã tự động sao lưu dữ liệu cũ:\n" + autoBackupPath + "\n\n";
            }
            successMsg += "Ứng dụng cần được khởi động lại để áp dụng thay đổi.\nBạn có muốn khởi động lại ngay bây giờ?";

            int choice = JOptionPane.showOptionDialog(this,
                    successMsg,
                    "Thành công",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    new Object[] { "Khởi động lại", "Để sau" },
                    "Khởi động lại");

            if (choice == JOptionPane.YES_OPTION) {
                // Restart application
                restartApplication();
            }
        } else {
            String errorMsg = "❌ Phục hồi thất bại!\n\n" +
                    "Vui lòng kiểm tra:\n" +
                    "1. File backup có hợp lệ không\n" +
                    "2. Đường dẫn mysql đúng chưa\n" +
                    "3. MySQL server có đang chạy không\n\n";

            if (backupSuccess) {
                errorMsg += "💾 Dữ liệu của bạn vẫn an toàn!\n" +
                        "Bản backup tự động: " + autoBackupPath;
            }

            JOptionPane.showMessageDialog(this, errorMsg, "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadBackupHistory() {
        modelHistory.setRowCount(0);

        // Scan backup directory for .sql files (không dùng database)
        File backupDir = new File(defaultBackupDir);

        if (!backupDir.exists() || !backupDir.isDirectory()) {
            System.out.println("Thư mục backup không tồn tại: " + defaultBackupDir);
            return;
        }

        // Get all .sql files
        File[] sqlFiles = backupDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".sql"));

        if (sqlFiles == null || sqlFiles.length == 0) {
            System.out.println("Chưa có bản sao lưu nào trong thư mục");
            return;
        }

        // Sort by last modified (newest first)
        java.util.Arrays.sort(sqlFiles, (f1, f2) -> Long.compare(f2.lastModified(), f1.lastModified()));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        for (File file : sqlFiles) {
            String tenFile = file.getName();
            String kichThuoc = formatFileSize(file.length());
            String ngaySaoLuu = sdf.format(new java.util.Date(file.lastModified()));
            String duongDan = file.getAbsolutePath();

            modelHistory.addRow(new Object[] { tenFile, kichThuoc, ngaySaoLuu, duongDan });
        }
    }

    /**
     * Format file size to human readable string
     */
    private String formatFileSize(long size) {
        if (size > 1024 * 1024) {
            return String.format("%.2f MB", size / (1024.0 * 1024.0));
        } else if (size > 1024) {
            return String.format("%.2f KB", size / 1024.0);
        } else {
            return size + " bytes";
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
        String duongDan = modelHistory.getValueAt(selectedRow, 3).toString();

        // Confirm deletion
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa bản sao lưu này?\n" +
                        "File: " + tenFile + "\n" +
                        "\nFile sẽ bị xóa vĩnh viễn!",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        // Delete file from disk
        File file = new File(duongDan);
        if (file.exists()) {
            if (file.delete()) {
                JOptionPane.showMessageDialog(this,
                        "Đã xóa bản sao lưu thành công!",
                        "Thành công",
                        JOptionPane.INFORMATION_MESSAGE);
                loadBackupHistory();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Không thể xóa file!\nCó thể file đang được sử dụng.",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // File already deleted, just refresh
            loadBackupHistory();
        }
    }

    /**
     * Restart the application by closing all windows and opening login form
     */
    private void restartApplication() {
        try {
            // Close all windows
            for (Window window : Window.getWindows()) {
                window.dispose();
            }

            // Open login form
            SwingUtilities.invokeLater(() -> {
                FormDangNhap loginForm = new FormDangNhap();
                loginForm.setVisible(true);
            });
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Không thể khởi động lại ứng dụng tự động.\nVui lòng đóng và mở lại ứng dụng thủ công.",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
