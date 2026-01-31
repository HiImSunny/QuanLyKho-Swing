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
    private static final String MYSQL_PATH = "C:\\\\xampp\\\\mysql\\\\bin\\\\";

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
                output.append(line).append("\\n");
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
     * Restore database from SQL file
     */
    public static boolean restore(String inputPath) {
        try {
            // Check if file exists
            File file = new File(inputPath);
            if (!file.exists()) {
                System.err.println("File not found: " + inputPath);
                return false;
            }

            // Build command as List
            List<String> commands = new ArrayList<>();
            commands.add(MYSQL_PATH + "mysql.exe");
            commands.add("-u" + DB_USER);
            if (!DB_PASS.isEmpty()) {
                commands.add("-p" + DB_PASS);
            }
            commands.add(DB_NAME);
            commands.add("-e");
            commands.add("source " + inputPath);

            ProcessBuilder pb = new ProcessBuilder(commands);
            pb.redirectErrorStream(true);

            Process process = pb.start();

            // Read output
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            StringBuilder output = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\\n");
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
     * Get list of backup files in a directory
     */
    public static List<String[]> getBackupHistory(String backupDir) {
        List<String[]> history = new ArrayList<>();

        File dir = new File(backupDir);
        if (!dir.exists() || !dir.isDirectory()) {
            return history;
        }

        File[] files = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".sql"));
        if (files == null)
            return history;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        for (File file : files) {
            String filename = file.getName();
            long sizeBytes = file.length();
            String size = formatFileSize(sizeBytes);
            String date = sdf.format(new Date(file.lastModified()));

            history.add(new String[] { filename, size, date, file.getAbsolutePath() });
        }

        // Sort by date descending
        history.sort((a, b) -> b[2].compareTo(a[2]));

        return history;
    }

    /**
     * Format file size to human-readable format
     */
    private static String formatFileSize(long bytes) {
        if (bytes < 1024)
            return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format("%.1f %sB", bytes / Math.pow(1024, exp), pre);
    }

    /**
     * Generate default backup filename
     */
    public static String generateBackupFilename() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return "qlkho_backup_" + sdf.format(new Date()) + ".sql";
    }
}
