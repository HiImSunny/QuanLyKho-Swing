package model;

import java.sql.Timestamp;

public class LichSuSaoLuu {
    private int id;
    private String tenFile;
    private String duongDan;
    private Timestamp thoiGian;

    public LichSuSaoLuu() {
    }

    public LichSuSaoLuu(int id, String tenFile, String duongDan, Timestamp thoiGian) {
        this.id = id;
        this.tenFile = tenFile;
        this.duongDan = duongDan;
        this.thoiGian = thoiGian;
    }

    public LichSuSaoLuu(String tenFile, String duongDan) {
        this.tenFile = tenFile;
        this.duongDan = duongDan;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Timestamp getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(Timestamp thoiGian) {
        this.thoiGian = thoiGian;
    }
}
