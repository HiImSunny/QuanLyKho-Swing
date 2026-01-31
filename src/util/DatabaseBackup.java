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
    private static final String MYSQL_PATH = "C:\\xampp\\mysql\\bin\\"; // Adjust if needed

    /**
     * Backup database to SQL file
     * 
     * @param outputPath Full path to output SQL file
     * @return true if successful
     */
    public static boolean backup(String outputPath) {
        try {
            // Ensure file has .sql extension
            if (!outputPath.toLowerCase().endsWith(".sql")) {
                outputPath += ".sql";
            }

            // Build mysqldump command
            String command = MYSQL_PATH + "mysqldump" +
                    " -u" + DB_USER +
                    (DB_PASS.isEmpty() ? "" : " -p" + DB_PASS) +
                    " --databases " + DB_NAME +
                    " --result-file=" + outputPath;

            // Execute command
            Process process = Runtime.getRuntime().exec(command);
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("Backup successful: " + outputPath);
                return true;
            } else {
                // Read error stream
                BufferedReader errorReader = new BufferedReader(
                        new InputStreamReader(process.getErrorStream()));
                String line;
                System.err.println("Backup failed:");
                while ((line = errorReader.readLine()) != null) {
                    System.err.println(line);
                }
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Restore database from SQL file
     * 
     * @param inputPath Full path to input SQL file
     * @return true if successful
     */
    public static boolean restore(String inputPath) {
        try {
            // Check if file exists
            File file = new File(inputPath);
            if (!file.exists()) {
                System.err.println("File not found: " + inputPath);
                return false;
            }

            // Build mysql command
            String command = MYSQL_PATH + "mysql" +
                    " -u" + DB_USER +
                    (DB_PASS.isEmpty() ? "" : " -p" + DB_PASS) +
                    " " + DB_NAME +
                    " < " + inputPath;

            // Execute command using cmd
            String[] cmd = { "cmd.exe", "/c", command };
            Process process = Runtime.getRuntime().exec(cmd);
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("Restore successful from: " + inputPath);
                return true;
            } else {
                // Read error stream
                BufferedReader errorReader = new BufferedReader(
                        new InputStreamReader(process.getErrorStream()));
                String line;
                System.err.println("Restore failed:");
                while ((line = errorReader.readLine()) != null) {
                    System.err.println(line);
                }
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get list of backup files in a directory
     * 
     * @param backupDir Directory containing backup files
     * @return List of backup file info [filename, size, date]
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
