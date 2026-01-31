package model;

import java.sql.Timestamp;

public class SaoLuu {
    private int maSaoLuu;
    private String tenFile;
    private String duongDan;
    private long kichThuoc; // in bytes
    private int nguoiThucHien; // user ID
    private String loai; // 'backup' or 'restore'
    private Timestamp ngayThucHien;
    private String ghiChu;

    // Default constructor
    public SaoLuu() {
    }

    // Constructor for creating new backup record
    public SaoLuu(String tenFile, String duongDan, long kichThuoc, int nguoiThucHien, String loai) {
        this.tenFile = tenFile;
        this.duongDan = duongDan;
        this.kichThuoc = kichThuoc;
        this.nguoiThucHien = nguoiThucHien;
        this.loai = loai;
    }

    // Full constructor (for reading from database)
    public SaoLuu(int maSaoLuu, String tenFile, String duongDan, long kichThuoc,
            int nguoiThucHien, String loai, Timestamp ngayThucHien, String ghiChu) {
        this.maSaoLuu = maSaoLuu;
        this.tenFile = tenFile;
        this.duongDan = duongDan;
        this.kichThuoc = kichThuoc;
        this.nguoiThucHien = nguoiThucHien;
        this.loai = loai;
        this.ngayThucHien = ngayThucHien;
        this.ghiChu = ghiChu;
    }

    // Getters and Setters
    public int getMaSaoLuu() {
        return maSaoLuu;
    }

    public void setMaSaoLuu(int maSaoLuu) {
        this.maSaoLuu = maSaoLuu;
    }

    public String getTenFile() {
        return tenFile;
    }

    public void setTenFile(String tenFile) {
        this.tenFile = tenFile;
    }

    public String getDuongDan() {
        return duongDan;
    }

    public void setDuongDan(String duongDan) {
        this.duongDan = duongDan;
    }

    public long getKichThuoc() {
        return kichThuoc;
    }

    public void setKichThuoc(long kichThuoc) {
        this.kichThuoc = kichThuoc;
    }

    public int getNguoiThucHien() {
        return nguoiThucHien;
    }

    public void setNguoiThucHien(int nguoiThucHien) {
        this.nguoiThucHien = nguoiThucHien;
    }

    public String getLoai() {
        return loai;
    }

    public void setLoai(String loai) {
        this.loai = loai;
    }

    public Timestamp getNgayThucHien() {
        return ngayThucHien;
    }

    public void setNgayThucHien(Timestamp ngayThucHien) {
        this.ngayThucHien = ngayThucHien;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    // Helper method to format file size
    public String getFormattedSize() {
        if (kichThuoc < 1024) {
            return kichThuoc + " B";
        }
        int exp = (int) (Math.log(kichThuoc) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format("%.1f %sB", kichThuoc / Math.pow(1024, exp), pre);
    }
}
