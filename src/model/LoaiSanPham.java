package model;

import java.sql.Timestamp;

public class LoaiSanPham {
    private int maLoai;
    private String tenLoai;
    private String moTa;
    private Timestamp ngayTao;
    
    public LoaiSanPham() {}
    
    public LoaiSanPham(int maLoai, String tenLoai, String moTa, Timestamp ngayTao) {
        this.maLoai = maLoai;
        this.tenLoai = tenLoai;
        this.moTa = moTa;
        this.ngayTao = ngayTao;
    }
    
    // Getters và Setters
    public int getMaLoai() {
        return maLoai;
    }
    
    public void setMaLoai(int maLoai) {
        this.maLoai = maLoai;
    }
    
    public String getTenLoai() {
        return tenLoai;
    }
    
    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai;
    }
    
    public String getMoTa() {
        return moTa;
    }
    
    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }
    
    public Timestamp getNgayTao() {
        return ngayTao;
    }
    
    public void setNgayTao(Timestamp ngayTao) {
        this.ngayTao = ngayTao;
    }
    
    @Override
    public String toString() {
        return tenLoai; // Dùng cho JComboBox
    }
}
