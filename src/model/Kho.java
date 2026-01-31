package model;

import java.sql.Timestamp;

public class Kho {
    private int id;
    private String maKho;
    private String tenKho;
    private String diaChi;
    private Double dienTich;
    private String nguoiQuanLy;
    private String ghiChu;
    private Timestamp ngayTao;
    private int trangThai;
    
    // Constructor
    public Kho() {
    }
    
    public Kho(int id, String maKho, String tenKho, String diaChi, Double dienTich, 
               String nguoiQuanLy, String ghiChu, Timestamp ngayTao, int trangThai) {
        this.id = id;
        this.maKho = maKho;
        this.tenKho = tenKho;
        this.diaChi = diaChi;
        this.dienTich = dienTich;
        this.nguoiQuanLy = nguoiQuanLy;
        this.ghiChu = ghiChu;
        this.ngayTao = ngayTao;
        this.trangThai = trangThai;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getMaKho() {
        return maKho;
    }
    
    public void setMaKho(String maKho) {
        this.maKho = maKho;
    }
    
    public String getTenKho() {
        return tenKho;
    }
    
    public void setTenKho(String tenKho) {
        this.tenKho = tenKho;
    }
    
    public String getDiaChi() {
        return diaChi;
    }
    
    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }
    
    public Double getDienTich() {
        return dienTich;
    }
    
    public void setDienTich(Double dienTich) {
        this.dienTich = dienTich;
    }
    
    // ← THÊM GETTER/SETTER CHO nguoiQuanLy
    public String getNguoiQuanLy() {
        return nguoiQuanLy;
    }
    
    public void setNguoiQuanLy(String nguoiQuanLy) {
        this.nguoiQuanLy = nguoiQuanLy;
    }
    
    public String getGhiChu() {
        return ghiChu;
    }
    
    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
    
    public Timestamp getNgayTao() {
        return ngayTao;
    }
    
    public void setNgayTao(Timestamp ngayTao) {
        this.ngayTao = ngayTao;
    }
    
    public int getTrangThai() {
        return trangThai;
    }
    
    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }
    
    @Override
    public String toString() {
        return tenKho;
    }
}
