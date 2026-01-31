package model;

import java.math.BigDecimal;

public class ChiTietPhieuNhap {
    private int id;
    private int ma_phieu_nhap;
    private int ma_sp;
    private String ten_sp; // For display (from JOIN)
    private int so_luong;
    private BigDecimal don_gia;
    private BigDecimal thanh_tien; // GENERATED COLUMN - chỉ dùng khi SELECT

    // Constructor đầy đủ (dùng khi SELECT từ DB với JOIN)
    public ChiTietPhieuNhap(int id, int ma_phieu_nhap, int ma_sp, String ten_sp,
            int so_luong, BigDecimal don_gia, BigDecimal thanh_tien) {
        this.id = id;
        this.ma_phieu_nhap = ma_phieu_nhap;
        this.ma_sp = ma_sp;
        this.ten_sp = ten_sp;
        this.so_luong = so_luong;
        this.don_gia = don_gia;
        this.thanh_tien = thanh_tien;
    }

    // Constructor không có id và thanh_tien (dùng khi INSERT)
    public ChiTietPhieuNhap(int ma_phieu_nhap, int ma_sp, int so_luong, BigDecimal don_gia) {
        this.ma_phieu_nhap = ma_phieu_nhap;
        this.ma_sp = ma_sp;
        this.so_luong = so_luong;
        this.don_gia = don_gia;
    }

    // Constructor rỗng
    public ChiTietPhieuNhap() {
    }

    // Getters và Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMa_phieu_nhap() {
        return ma_phieu_nhap;
    }

    public void setMa_phieu_nhap(int ma_phieu_nhap) {
        this.ma_phieu_nhap = ma_phieu_nhap;
    }

    public int getMa_sp() {
        return ma_sp;
    }

    public void setMa_sp(int ma_sp) {
        this.ma_sp = ma_sp;
    }

    public int getSo_luong() {
        return so_luong;
    }

    public void setSo_luong(int so_luong) {
        this.so_luong = so_luong;
    }

    public BigDecimal getDon_gia() {
        return don_gia;
    }

    public void setDon_gia(BigDecimal don_gia) {
        this.don_gia = don_gia;
    }

    public String getTen_sp() {
        return ten_sp;
    }

    public void setTen_sp(String ten_sp) {
        this.ten_sp = ten_sp;
    }

    public BigDecimal getThanh_tien() {
        return thanh_tien;
    }

    public void setThanh_tien(BigDecimal thanh_tien) {
        this.thanh_tien = thanh_tien;
    }
}
