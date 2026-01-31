package model;

import java.sql.Timestamp;

public class User {
    private int id;
    private String username;
    private String password;
    private String hoTen;
    private String role;
    private Timestamp ngayTao;
    private int trangThai;
    
    // Constructor mặc định
    public User() {}
    
    // Constructor đầy đủ
    public User(int id, String username, String password, String hoTen, String role, 
                Timestamp ngayTao, int trangThai) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.hoTen = hoTen;
        this.role = role;
        this.ngayTao = ngayTao;
        this.trangThai = trangThai;
    }
    
    // Getters và Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getHoTen() {
        return hoTen;
    }
    
    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
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
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", hoTen='" + hoTen + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
