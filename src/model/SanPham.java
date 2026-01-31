package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class SanPham {
    private int maSp;
    private String tenSp;
    private int maLoai;
    private String tenLoai; // Join từ loai_san_pham
    private String donViTinh;
    private BigDecimal giaNhap;
    private BigDecimal giaBan;
    private int soLuongTon;
    private String moTa;
    private String hinhAnh;
    private Timestamp ngayTao;

    public SanPham() {
    }

    public SanPham(int maSp, String tenSp, int maLoai, String donViTinh,
            BigDecimal giaNhap, BigDecimal giaBan, int soLuongTon) {
        this.maSp = maSp;
        this.tenSp = tenSp;
        this.maLoai = maLoai;
        this.donViTinh = donViTinh;
        this.giaNhap = giaNhap;
        this.giaBan = giaBan;
        this.soLuongTon = soLuongTon;
    }

    // Getters và Setters
    public int getMaSp() {
        return maSp;
    }

    public void setMaSp(int maSp) {
        this.maSp = maSp;
    }

    public String getTenSp() {
        return tenSp;
    }

    public void setTenSp(String tenSp) {
        this.tenSp = tenSp;
    }

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

    public String getDonViTinh() {
        return donViTinh;
    }

    public void setDonViTinh(String donViTinh) {
        this.donViTinh = donViTinh;
    }

    public BigDecimal getGiaNhap() {
        return giaNhap;
    }

    public void setGiaNhap(BigDecimal giaNhap) {
        this.giaNhap = giaNhap;
    }

    public BigDecimal getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(BigDecimal giaBan) {
        this.giaBan = giaBan;
    }

    public int getSoLuongTon() {
        return soLuongTon;
    }

    public void setSoLuongTon(int soLuongTon) {
        this.soLuongTon = soLuongTon;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public Timestamp getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(Timestamp ngayTao) {
        this.ngayTao = ngayTao;
    }

    @Override
    public String toString() {
        return tenSp; // Dùng cho JComboBox
    }
}
