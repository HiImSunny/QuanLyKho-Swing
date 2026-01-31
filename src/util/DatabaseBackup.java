package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Utility class for database backup and restore operations
 */
public class DatabaseBackup {

    // Database connection info
    private static final String DB_NAME = "qlkho_db";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";
    private static final String MYSQL_PATH = "C:\\xampp\\mysql\\bin\\";

    /**
     * Validate path for whitespace issues
     */
    public static String validatePath(String path) {
        if (path == null || path.trim().isEmpty()) {
            return "Đường dẫn không được để trống!";
        }

        // Check for whitespace in path (can cause issues with some MySQL commands)
        if (path.contains(" ")) {
            return "Cảnh báo: Đường dẫn chứa khoảng trắng.\nNên lưu vào thư mục không có khoảng trắng để tránh lỗi!";
        }

        return null; // No error
    }

    /**
     * Backup database to SQL file
     */
    public static boolean backup(String outputPath) {
        try {
            // Ensure file has .sql extension
            if (!outputPath.toLowerCase().endsWith(".sql")) {
                outputPath += ".sql";
            }

            // Build command as List (proper way for ProcessBuilder)
            List<String> commands = new ArrayList<>();
            commands.add(MYSQL_PATH + "mysqldump.exe");
            commands.add("-u" + DB_USER);
            if (!DB_PASS.isEmpty()) {
                commands.add("-p" + DB_PASS);
            }
            commands.add("--databases");
            commands.add(DB_NAME);
            commands.add("--result-file=" + outputPath);

            ProcessBuilder pb = new ProcessBuilder(commands);
            pb.redirectErrorStream(true);

            Process process = pb.start();

            // Read output
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            StringBuilder output = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
                System.out.println(line);
            }

            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("Backup successful: " + outputPath);
                return true;
            } else {
                System.err.println("Backup failed with exit code: " + exitCode);
                System.err.println("Output: " + output.toString());
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Restore database from SQL file using PowerShell (handles paths with spaces)
     */
    public static boolean restore(String inputPath) {
        try {
            // Check if file exists
            File file = new File(inputPath);
            if (!file.exists()) {
                System.err.println("File not found: " + inputPath);
                return false;
            }

            // Use PowerShell to handle paths with spaces
            List<String> commands = new ArrayList<>();
            commands.add("powershell.exe");
            commands.add("-Command");

            // Build PowerShell command: Get-Content "path" | mysql -u root
            StringBuilder psCommand = new StringBuilder();
            psCommand.append("Get-Content \"").append(inputPath).append("\" | ");
            psCommand.append("& \"").append(MYSQL_PATH).append("mysql.exe\" ");
            psCommand.append("-u ").append(DB_USER);
            if (!DB_PASS.isEmpty()) {
                psCommand.append(" -p").append(DB_PASS);
            }

            commands.add(psCommand.toString());

            ProcessBuilder pb = new ProcessBuilder(commands);
            pb.redirectErrorStream(true);

            Process process = pb.start();

            // Read output
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            StringBuilder output = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
                System.out.println(line);
            }

            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("Restore successful from: " + inputPath);
                return true;
            } else {
                System.err.println("Restore failed with exit code: " + exitCode);
                System.err.println("Output: " + output.toString());
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Generate default backup filename
     */
    public static String generateBackupFilename() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return "qlkho_backup_" + sdf.format(new Date()) + ".sql";
    }
}
