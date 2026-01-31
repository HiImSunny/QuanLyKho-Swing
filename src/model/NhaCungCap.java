package model;

import java.sql.Timestamp;

public class NhaCungCap {
    private int maNcc;
    private String tenNcc;
    private String diaChi;
    private String sdt;
    private String email;
    private String nguoiLienHe;
    private Timestamp ngayTao;
    private int trangThai;
    
    // Constructor
    public NhaCungCap() {
    }
    
    public NhaCungCap(int maNcc, String tenNcc, String diaChi, String sdt, String email, String nguoiLienHe) {
        this.maNcc = maNcc;
        this.tenNcc = tenNcc;
        this.diaChi = diaChi;
        this.sdt = sdt;
        this.email = email;
        this.nguoiLienHe = nguoiLienHe;
    }
    
    // Getters and Setters
    public int getMaNcc() {
        return maNcc;
    }
    
    public void setMaNcc(int maNcc) {
        this.maNcc = maNcc;
    }
    
    public String getTenNcc() {
        return tenNcc;
    }
    
    public void setTenNcc(String tenNcc) {
        this.tenNcc = tenNcc;
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
    
    public String getNguoiLienHe() {
        return nguoiLienHe;
    }
    
    public void setNguoiLienHe(String nguoiLienHe) {
        this.nguoiLienHe = nguoiLienHe;
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
        return tenNcc; // Dùng cho ComboBox
    }
}
