package model;

import java.util.Date;

/**
 * Model cho phiếu kiểm kê kho
 */
public class KiemKe {
    private int ma_kiem_ke;
    private String so_phieu;
    private Date ngay_kiem_ke;
    private int ma_kho;
    private String ten_kho; // For display
    private int nguoi_kiem_ke;
    private String ten_nguoi_kiem_ke; // For display
    private String trang_thai; // 'dang_kiem', 'hoan_thanh'
    private String ghi_chu;
    private Date ngay_tao;

    // Constructor đầy đủ (dùng khi lấy từ DB)
    public KiemKe(int ma_kiem_ke, String so_phieu, Date ngay_kiem_ke, int ma_kho, String ten_kho,
            int nguoi_kiem_ke, String ten_nguoi_kiem_ke, String trang_thai, String ghi_chu, Date ngay_tao) {
        this.ma_kiem_ke = ma_kiem_ke;
        this.so_phieu = so_phieu;
        this.ngay_kiem_ke = ngay_kiem_ke;
        this.ma_kho = ma_kho;
        this.ten_kho = ten_kho;
        this.nguoi_kiem_ke = nguoi_kiem_ke;
        this.ten_nguoi_kiem_ke = ten_nguoi_kiem_ke;
        this.trang_thai = trang_thai;
        this.ghi_chu = ghi_chu;
        this.ngay_tao = ngay_tao;
    }

    // Constructor cho insert (không cần ma_kiem_ke, ngay_tao)
    public KiemKe(String so_phieu, Date ngay_kiem_ke, int ma_kho, int nguoi_kiem_ke,
            String trang_thai, String ghi_chu) {
        this.so_phieu = so_phieu;
        this.ngay_kiem_ke = ngay_kiem_ke;
        this.ma_kho = ma_kho;
        this.nguoi_kiem_ke = nguoi_kiem_ke;
        this.trang_thai = trang_thai;
        this.ghi_chu = ghi_chu;
    }

    // Getters and Setters
    public int getMa_kiem_ke() {
        return ma_kiem_ke;
    }

    public void setMa_kiem_ke(int ma_kiem_ke) {
        this.ma_kiem_ke = ma_kiem_ke;
    }

    public String getSo_phieu() {
        return so_phieu;
    }

    public void setSo_phieu(String so_phieu) {
        this.so_phieu = so_phieu;
    }

    public Date getNgay_kiem_ke() {
        return ngay_kiem_ke;
    }

    public void setNgay_kiem_ke(Date ngay_kiem_ke) {
        this.ngay_kiem_ke = ngay_kiem_ke;
    }

    public int getMa_kho() {
        return ma_kho;
    }

    public void setMa_kho(int ma_kho) {
        this.ma_kho = ma_kho;
    }

    public String getTen_kho() {
        return ten_kho;
    }

    public void setTen_kho(String ten_kho) {
        this.ten_kho = ten_kho;
    }

    public int getNguoi_kiem_ke() {
        return nguoi_kiem_ke;
    }

    public void setNguoi_kiem_ke(int nguoi_kiem_ke) {
        this.nguoi_kiem_ke = nguoi_kiem_ke;
    }

    public String getTen_nguoi_kiem_ke() {
        return ten_nguoi_kiem_ke;
    }

    public void setTen_nguoi_kiem_ke(String ten_nguoi_kiem_ke) {
        this.ten_nguoi_kiem_ke = ten_nguoi_kiem_ke;
    }

    public String getTrang_thai() {
        return trang_thai;
    }

    public void setTrang_thai(String trang_thai) {
        this.trang_thai = trang_thai;
    }

    public String getGhi_chu() {
        return ghi_chu;
    }

    public void setGhi_chu(String ghi_chu) {
        this.ghi_chu = ghi_chu;
    }

    public Date getNgay_tao() {
        return ngay_tao;
    }

    public void setNgay_tao(Date ngay_tao) {
        this.ngay_tao = ngay_tao;
    }
}
