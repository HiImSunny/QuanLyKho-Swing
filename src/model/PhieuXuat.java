package model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class PhieuXuat {
    private int ma_phieu_xuat;
    private String so_phieu;
    private Date ngay_xuat;
    private String khach_hang; // For display (from JOIN)
    private Integer ma_kh;
    private Integer ma_kho; // Warehouse to export from
    private Integer nguoi_lap;
    private BigDecimal tong_tien;
    private String ghi_chu;
    private Timestamp ngay_tao;

    // Constructor đầy đủ (cho SELECT)
    public PhieuXuat(int ma_phieu_xuat, String so_phieu, Date ngay_xuat,
            String khach_hang, Integer ma_kh, Integer ma_kho, Integer nguoi_lap,
            BigDecimal tong_tien, String ghi_chu, Timestamp ngay_tao) {
        this.ma_phieu_xuat = ma_phieu_xuat;
        this.so_phieu = so_phieu;
        this.ngay_xuat = ngay_xuat;
        this.khach_hang = khach_hang;
        this.ma_kh = ma_kh;
        this.ma_kho = ma_kho;
        this.nguoi_lap = nguoi_lap;
        this.tong_tien = tong_tien;
        this.ghi_chu = ghi_chu;
        this.ngay_tao = ngay_tao;
    }

    // Constructor cho INSERT
    public PhieuXuat(String so_phieu, Date ngay_xuat, String khach_hang,
            Integer ma_kh, Integer ma_kho, Integer nguoi_lap, BigDecimal tong_tien,
            String ghi_chu) {
        this.so_phieu = so_phieu;
        this.ngay_xuat = ngay_xuat;
        this.khach_hang = khach_hang;
        this.ma_kh = ma_kh;
        this.ma_kho = ma_kho;
        this.nguoi_lap = nguoi_lap;
        this.tong_tien = tong_tien;
        this.ghi_chu = ghi_chu;
    }

    // Getters and Setters
    public int getMa_phieu_xuat() {
        return ma_phieu_xuat;
    }

    public void setMa_phieu_xuat(int ma_phieu_xuat) {
        this.ma_phieu_xuat = ma_phieu_xuat;
    }

    public String getSo_phieu() {
        return so_phieu;
    }

    public void setSo_phieu(String so_phieu) {
        this.so_phieu = so_phieu;
    }

    public Date getNgay_xuat() {
        return ngay_xuat;
    }

    public void setNgay_xuat(Date ngay_xuat) {
        this.ngay_xuat = ngay_xuat;
    }

    public String getKhach_hang() {
        return khach_hang;
    }

    public void setKhach_hang(String khach_hang) {
        this.khach_hang = khach_hang;
    }

    public Integer getMa_kh() {
        return ma_kh;
    }

    public void setMa_kh(Integer ma_kh) {
        this.ma_kh = ma_kh;
    }

    public Integer getMa_kho() {
        return ma_kho;
    }

    public void setMa_kho(Integer ma_kho) {
        this.ma_kho = ma_kho;
    }

    public Integer getNguoi_lap() {
        return nguoi_lap;
    }

    public void setNguoi_lap(Integer nguoi_lap) {
        this.nguoi_lap = nguoi_lap;
    }

    public BigDecimal getTong_tien() {
        return tong_tien;
    }

    public void setTong_tien(BigDecimal tong_tien) {
        this.tong_tien = tong_tien;
    }

    public String getGhi_chu() {
        return ghi_chu;
    }

    public void setGhi_chu(String ghi_chu) {
        this.ghi_chu = ghi_chu;
    }

    public Timestamp getNgay_tao() {
        return ngay_tao;
    }

    public void setNgay_tao(Timestamp ngay_tao) {
        this.ngay_tao = ngay_tao;
    }
}
