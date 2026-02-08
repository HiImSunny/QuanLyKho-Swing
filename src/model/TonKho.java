package model;

import java.sql.Timestamp;

public class TonKho {
    private int maSp;
    private int maKho;
    private int soLuongTon;
    private Timestamp ngayCapNhat;

    // Thông tin bổ sung (join từ bảng khác)
    private String tenSp;
    private String tenKho;
    private String donViTinh;
    private String tenLoai;
    private java.math.BigDecimal giaTien;

    public TonKho() {
    }

    public TonKho(int maSp, int maKho, int soLuongTon) {
        this.maSp = maSp;
        this.maKho = maKho;
        this.soLuongTon = soLuongTon;
    }

    public TonKho(int maSp, int maKho, int soLuongTon, Timestamp ngayCapNhat) {
        this.maSp = maSp;
        this.maKho = maKho;
        this.soLuongTon = soLuongTon;
        this.ngayCapNhat = ngayCapNhat;
    }

    // Getters and Setters

    public int getMaSp() {
        return maSp;
    }

    public void setMaSp(int maSp) {
        this.maSp = maSp;
    }

    public int getMaKho() {
        return maKho;
    }

    public void setMaKho(int maKho) {
        this.maKho = maKho;
    }

    public int getSoLuongTon() {
        return soLuongTon;
    }

    public void setSoLuongTon(int soLuongTon) {
        this.soLuongTon = soLuongTon;
    }

    public Timestamp getNgayCapNhat() {
        return ngayCapNhat;
    }

    public void setNgayCapNhat(Timestamp ngayCapNhat) {
        this.ngayCapNhat = ngayCapNhat;
    }

    public String getTenSp() {
        return tenSp;
    }

    public void setTenSp(String tenSp) {
        this.tenSp = tenSp;
    }

    public String getTenKho() {
        return tenKho;
    }

    public void setTenKho(String tenKho) {
        this.tenKho = tenKho;
    }

    public String getDonViTinh() {
        return donViTinh;
    }

    public void setDonViTinh(String donViTinh) {
        this.donViTinh = donViTinh;
    }

    public String getTenLoai() {
        return tenLoai;
    }

    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai;
    }

    public java.math.BigDecimal getGiaTien() {
        return giaTien;
    }

    public void setGiaTien(java.math.BigDecimal giaTien) {
        this.giaTien = giaTien;
    }

    @Override
    public String toString() {
        return "TonKho{" +
                "maSp=" + maSp +
                ", maKho=" + maKho +
                ", soLuongTon=" + soLuongTon +
                ", ngayCapNhat=" + ngayCapNhat +
                '}';
    }
}
