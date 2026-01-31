package model;

import java.sql.Timestamp;

public class KhachHang {
    private int maKh;
    private String tenKh;
    private String diaChi;
    private String sdt;
    private String email;
    private String loaiKh; // 'canhan' hoặc 'doanhnghiep'
    private Timestamp ngayTao;
    private int trangThai;
    
    // Constructor
    public KhachHang() {
    }
    
    public KhachHang(int maKh, String tenKh, String diaChi, String sdt, String email, String loaiKh) {
        this.maKh = maKh;
        this.tenKh = tenKh;
        this.diaChi = diaChi;
        this.sdt = sdt;
        this.email = email;
        this.loaiKh = loaiKh;
    }
    
    // Getters and Setters
    public int getMaKh() {
        return maKh;
    }
    
    public void setMaKh(int maKh) {
        this.maKh = maKh;
    }
    
    public String getTenKh() {
        return tenKh;
    }
    
    public void setTenKh(String tenKh) {
        this.tenKh = tenKh;
    }
    
    public String getDiaChi() {
        return diaChi;
    }
    
    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }
    
    public String getSdt() {
        return sdt;
    }
    
    public void setSdt(String sdt) {
        this.sdt = sdt;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getLoaiKh() {
        return loaiKh;
    }
    
    public void setLoaiKh(String loaiKh) {
        this.loaiKh = loaiKh;
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
        return tenKh; // Dùng cho ComboBox
    }
}
