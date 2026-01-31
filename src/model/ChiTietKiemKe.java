package model;

/**
 * Model cho chi tiết phiếu kiểm kê
 */
public class ChiTietKiemKe {
    private int id;
    private int ma_kiem_ke;
    private int ma_sp;
    private String ten_sp; // For display
    private String don_vi_tinh; // For display
    private int ton_he_thong; // Tồn kho theo hệ thống
    private int ton_thuc_te; // Tồn kho đếm thực tế
    private int chenh_lech; // Chênh lệch (generated column)
    private String ghi_chu;

    // Constructor đầy đủ (dùng khi lấy từ DB)
    public ChiTietKiemKe(int id, int ma_kiem_ke, int ma_sp, String ten_sp, String don_vi_tinh,
            int ton_he_thong, int ton_thuc_te, int chenh_lech, String ghi_chu) {
        this.id = id;
        this.ma_kiem_ke = ma_kiem_ke;
        this.ma_sp = ma_sp;
        this.ten_sp = ten_sp;
        this.don_vi_tinh = don_vi_tinh;
        this.ton_he_thong = ton_he_thong;
        this.ton_thuc_te = ton_thuc_te;
        this.chenh_lech = chenh_lech;
        this.ghi_chu = ghi_chu;
    }

    // Constructor cho insert (không cần id, chenh_lech)
    public ChiTietKiemKe(int ma_kiem_ke, int ma_sp, int ton_he_thong, int ton_thuc_te, String ghi_chu) {
        this.ma_kiem_ke = ma_kiem_ke;
        this.ma_sp = ma_sp;
        this.ton_he_thong = ton_he_thong;
        this.ton_thuc_te = ton_thuc_te;
        this.ghi_chu = ghi_chu;
    }

    // Constructor cho UI (không cần ma_kiem_ke ban đầu)
    public ChiTietKiemKe(int ma_sp, String ten_sp, String don_vi_tinh, int ton_he_thong,
            int ton_thuc_te, String ghi_chu) {
        this.ma_sp = ma_sp;
        this.ten_sp = ten_sp;
        this.don_vi_tinh = don_vi_tinh;
        this.ton_he_thong = ton_he_thong;
        this.ton_thuc_te = ton_thuc_te;
        this.ghi_chu = ghi_chu;
        this.chenh_lech = ton_thuc_te - ton_he_thong; // Tính tạm
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMa_kiem_ke() {
        return ma_kiem_ke;
    }

    public void setMa_kiem_ke(int ma_kiem_ke) {
        this.ma_kiem_ke = ma_kiem_ke;
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

    public String getDon_vi_tinh() {
        return don_vi_tinh;
    }

    public void setDon_vi_tinh(String don_vi_tinh) {
        this.don_vi_tinh = don_vi_tinh;
    }

    public int getTon_he_thong() {
        return ton_he_thong;
    }

    public void setTon_he_thong(int ton_he_thong) {
        this.ton_he_thong = ton_he_thong;
    }

    public int getTon_thuc_te() {
        return ton_thuc_te;
    }

    public void setTon_thuc_te(int ton_thuc_te) {
        this.ton_thuc_te = ton_thuc_te;
        this.chenh_lech = ton_thuc_te - ton_he_thong; // Auto update
    }

    public int getChenh_lech() {
        return chenh_lech;
    }

    public void setChenh_lech(int chenh_lech) {
        this.chenh_lech = chenh_lech;
    }

    public String getGhi_chu() {
        return ghi_chu;
    }

    public void setGhi_chu(String ghi_chu) {
        this.ghi_chu = ghi_chu;
    }
}
