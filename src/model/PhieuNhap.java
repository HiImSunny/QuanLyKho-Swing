package model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class PhieuNhap {
    private int ma_phieu_nhap;
    private String so_phieu;
    private Date ngay_nhap;
    private String nha_cung_cap;
    private Integer ma_ncc;
    private Integer ma_kho; // Kho nhập hàng
    private Integer nguoi_lap;
    private BigDecimal tong_tien;
    private String ghi_chu;
    private Timestamp ngay_tao;

    // Constructor đầy đủ
    public PhieuNhap(int ma_phieu_nhap, String so_phieu, Date ngay_nhap,
            String nha_cung_cap, Integer ma_ncc, Integer ma_kho, Integer nguoi_lap,
            BigDecimal tong_tien, String ghi_chu, Timestamp ngay_tao) {
        this.ma_phieu_nhap = ma_phieu_nhap;
        this.so_phieu = so_phieu;
        this.ngay_nhap = ngay_nhap;
        this.nha_cung_cap = nha_cung_cap;
        this.ma_ncc = ma_ncc;
        this.ma_kho = ma_kho;
        this.nguoi_lap = nguoi_lap;
        this.tong_tien = tong_tien;
        this.ghi_chu = ghi_chu;
        this.ngay_tao = ngay_tao;
    }

    // Constructor không có id (dùng khi insert)
    public PhieuNhap(String so_phieu, Date ngay_nhap, String nha_cung_cap,
            Integer ma_ncc, Integer ma_kho, Integer nguoi_lap, BigDecimal tong_tien,
            String ghi_chu) {
        this.so_phieu = so_phieu;
        this.ngay_nhap = ngay_nhap;
        this.nha_cung_cap = nha_cung_cap;
        this.ma_ncc = ma_ncc;
        this.ma_kho = ma_kho;
        this.nguoi_lap = nguoi_lap;
        this.tong_tien = tong_tien;
        this.ghi_chu = ghi_chu;
    }

    // Constructor rỗng
    public PhieuNhap() {
    }

    // Getters và Setters
    public int getMa_phieu_nhap() {
        return ma_phieu_nhap;
    }

    public void setMa_phieu_nhap(int ma_phieu_nhap) {
        this.ma_phieu_nhap = ma_phieu_nhap;
    }

    public String getSo_phieu() {
        return so_phieu;
    }

    public void setSo_phieu(String so_phieu) {
        this.so_phieu = so_phieu;
    }

    public Date getNgay_nhap() {
        return ngay_nhap;
    }

    public void setNgay_nhap(Date ngay_nhap) {
        this.ngay_nhap = ngay_nhap;
    }

    public String getNha_cung_cap() {
        return nha_cung_cap;
    }

    public void setNha_cung_cap(String nha_cung_cap) {
        this.nha_cung_cap = nha_cung_cap;
    }

    public Integer getMa_ncc() {
        return ma_ncc;
    }

    public void setMa_ncc(Integer ma_ncc) {
        this.ma_ncc = ma_ncc;
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
