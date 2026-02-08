package model;

import java.math.BigDecimal;

public class ChiTietPhieuXuat {
    private int ma_phieu_xuat;
    private int ma_sp;
    private String ten_sp; // For display
    private int so_luong;
    private BigDecimal don_gia;
    private BigDecimal thanh_tien; // Calculated: so_luong * don_gia

    // Constructor đầy đủ
    public ChiTietPhieuXuat(int ma_phieu_xuat, int ma_sp, String ten_sp,
            int so_luong, BigDecimal don_gia, BigDecimal thanh_tien) {
        this.ma_phieu_xuat = ma_phieu_xuat;
        this.ma_sp = ma_sp;
        this.ten_sp = ten_sp;
        this.so_luong = so_luong;
        this.don_gia = don_gia;
        this.thanh_tien = thanh_tien;
    }

    // Constructor cho INSERT (không cần id, thanh_tien)
    public ChiTietPhieuXuat(int ma_phieu_xuat, int ma_sp, String ten_sp,
            int so_luong, BigDecimal don_gia) {
        this.ma_phieu_xuat = ma_phieu_xuat;
        this.ma_sp = ma_sp;
        this.ten_sp = ten_sp;
        this.so_luong = so_luong;
        this.don_gia = don_gia;
        this.thanh_tien = don_gia.multiply(new BigDecimal(so_luong));
    }

    // Constructor cho UI (chưa có ma_phieu_xuat)
    public ChiTietPhieuXuat(int ma_sp, String ten_sp, int so_luong, BigDecimal don_gia) {
        this.ma_sp = ma_sp;
        this.ten_sp = ten_sp;
        this.so_luong = so_luong;
        this.don_gia = don_gia;
        this.thanh_tien = don_gia.multiply(new BigDecimal(so_luong));
    }

    // Getters and Setters

    public int getMa_phieu_xuat() {
        return ma_phieu_xuat;
    }

    public void setMa_phieu_xuat(int ma_phieu_xuat) {
        this.ma_phieu_xuat = ma_phieu_xuat;
    }

    public int getMa_sp() {
        return ma_sp;
    }

    public void setMa_sp(int ma_sp) {
        this.ma_sp = ma_sp;
    }

    public String getTen_sp() {
        return ten_sp;
    }

    public void setTen_sp(String ten_sp) {
        this.ten_sp = ten_sp;
    }

    public int getSo_luong() {
        return so_luong;
    }

    public void setSo_luong(int so_luong) {
        this.so_luong = so_luong;
        this.thanh_tien = this.don_gia.multiply(new BigDecimal(so_luong));
    }

    public BigDecimal getDon_gia() {
        return don_gia;
    }

    public void setDon_gia(BigDecimal don_gia) {
        this.don_gia = don_gia;
        this.thanh_tien = don_gia.multiply(new BigDecimal(this.so_luong));
    }

    public BigDecimal getThanh_tien() {
        return thanh_tien;
    }

    public void setThanh_tien(BigDecimal thanh_tien) {
        this.thanh_tien = thanh_tien;
    }
}
