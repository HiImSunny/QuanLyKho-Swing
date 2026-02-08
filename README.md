# 📦 Hệ Thống Quản Lý Kho Hàng

![Java](https://img.shields.io/badge/Java-17+-orange.svg)
![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue.svg)
![Swing](https://img.shields.io/badge/GUI-Java%20Swing-green.svg)
![License](https://img.shields.io/badge/License-MIT-yellow.svg)

Hệ thống quản lý kho hàng toàn diện được xây dựng bằng Java Swing và MySQL, hỗ trợ quản lý sản phẩm, nhập/xuất kho, kiểm kê, và báo cáo tồn kho.

## 📋 Mục Lục

- [Tính Năng](#-tính-năng)
- [Công Nghệ](#-công-nghệ)
- [Yêu Cầu Hệ Thống](#-yêu-cầu-hệ-thống)
- [Cài Đặt](#-cài-đặt)
- [Cấu Trúc Dự Án](#-cấu-trúc-dự-án)
- [Cấu Trúc Database](#-cấu-trúc-database)
- [Hướng Dẫn Sử Dụng](#-hướng-dẫn-sử-dụng)
- [Tài Khoản Mặc Định](#-tài-khoản-mặc-định)
- [Tính Năng Chi Tiết](#-tính-năng-chi-tiết)
- [API & DAO](#-api--dao)
- [Backup & Restore](#-backup--restore)
- [Troubleshooting](#-troubleshooting)
- [Đóng Góp](#-đóng-góp)
- [Tác Giả](#-tác-giả)
- [License](#-license)

---

## ✨ Tính Năng

### 🔐 Quản Lý Người Dùng
- ✅ Đăng nhập với mã hóa BCrypt
- ✅ Phân quyền Admin/Nhân viên
- ✅ Đổi mật khẩu
- ✅ Quản lý tài khoản (chỉ Admin)

### 📦 Quản Lý Sản Phẩm
- ✅ CRUD sản phẩm (Thêm, Sửa, Xóa, Tìm kiếm)
- ✅ Quản lý loại sản phẩm
- ✅ Quản lý giá nhập/bán
- ✅ Theo dõi tồn kho theo từng kho
- ✅ Hỗ trợ hình ảnh sản phẩm

### 🏢 Quản Lý Kho
- ✅ Quản lý nhiều kho hàng
- ✅ Phân bổ tồn kho theo kho
- ✅ Chuyển kho giữa các kho
- ✅ Theo dõi diện tích, người quản lý

### 📥 Nhập/Xuất Kho
- ✅ Tạo phiếu nhập kho
- ✅ Tạo phiếu xuất kho
- ✅ Tự động cập nhật tồn kho
- ✅ Tính toán tổng tiền tự động
- ✅ Lịch sử nhập/xuất chi tiết

### 🔍 Kiểm Kê
- ✅ Tạo phiếu kiểm kê
- ✅ So sánh tồn hệ thống vs thực tế
- ✅ Tính chênh lệch tự động
- ✅ Lịch sử kiểm kê

### 👥 Quản Lý Đối Tác
- ✅ Quản lý khách hàng (Cá nhân/Doanh nghiệp)
- ✅ Quản lý nhà cung cấp
- ✅ Thông tin liên hệ đầy đủ

### 📊 Báo Cáo
- ✅ Báo cáo tồn kho tổng hợp
- ✅ Báo cáo tồn kho theo kho
- ✅ Xuất PDF báo cáo
- ✅ Dashboard thống kê trực quan

### 💾 Sao Lưu & Phục Hồi
- ✅ Backup database tự động
- ✅ Restore từ file backup
- ✅ Lịch sử backup
- ✅ Xóa backup cũ

---

## 🛠 Công Nghệ

### Backend
- **Java 17+** - Ngôn ngữ lập trình chính
- **JDBC** - Kết nối database
- **BCrypt** - Mã hóa mật khẩu

### Frontend
- **Java Swing** - Giao diện người dùng
- **Custom UI Components** - Tùy chỉnh giao diện

### Database
- **MySQL 8.0+** / **MariaDB 10.4+**
- **InnoDB Engine** - Hỗ trợ transaction
- **Foreign Keys** - Đảm bảo tính toàn vẹn dữ liệu

### Libraries
| Library | Version | Mục đích |
|---------|---------|----------|
| `mysql-connector-j` | 8.0.33 | JDBC Driver cho MySQL |
| `jbcrypt` | 0.4 | Mã hóa mật khẩu |
| `pdfbox` | 2.0.31 | Xuất PDF |
| `fontbox` | 2.0.31 | Font cho PDF |
| `commons-logging` | 1.2 | Logging |

---

## 💻 Yêu Cầu Hệ Thống

### Phần Mềm
- ✅ **Java JDK 11** trở lên
- ✅ **MySQL 8.0+** hoặc **MariaDB 10.4+**
- ✅ **XAMPP** (khuyến nghị) hoặc MySQL Server độc lập
- ✅ **IDE**: Eclipse, IntelliJ IDEA, hoặc NetBeans

### Phần Cứng (Khuyến nghị)
- 💾 RAM: 4GB trở lên
- 💿 Ổ cứng: 500MB trống
- 🖥 Màn hình: 1366x768 trở lên

---

## 🚀 Cài Đặt

### Bước 1: Clone Repository

```bash
git clone https://github.com/your-username/QuanLyKho-Swing.git
cd QuanLyKho-Swing
```

### Bước 2: Cài Đặt Database

1. **Khởi động MySQL/MariaDB** (qua XAMPP hoặc service)

2. **Tạo database và import schema**:

```bash
# Mở MySQL Command Line hoặc phpMyAdmin
mysql -u root -p

# Trong MySQL shell:
CREATE DATABASE qlkho_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE qlkho_db;
SOURCE /path/to/QuanLyKho-Swing/src/qlkho_db.sql;
```

**Hoặc qua phpMyAdmin**:
- Mở `http://localhost/phpmyadmin`
- Tạo database mới: `qlkho_db`
- Import file: `src/qlkho_db.sql`

### Bước 3: Cấu Hình Kết Nối Database

Mở file `src/database/DatabaseConnection.java` và kiểm tra cấu hình:

```java
private static final String DB_URL = "jdbc:mysql://localhost:3306/qlkho_db?useSSL=false&serverTimezone=Asia/Ho_Chi_Minh&useUnicode=true&characterEncoding=UTF-8";
private static final String USER = "root";
private static final String PASS = ""; // Thay đổi nếu có password
```

### Bước 4: Thêm Libraries

**Nếu dùng Eclipse**:
1. Right-click project → **Build Path** → **Configure Build Path**
2. Tab **Libraries** → **Add External JARs**
3. Chọn tất cả file `.jar` trong thư mục `lib/`

**Nếu dùng IntelliJ IDEA**:
1. **File** → **Project Structure** → **Libraries**
2. Click **+** → **Java** → Chọn thư mục `lib/`

### Bước 5: Chạy Ứng Dụng

1. **Tìm file** `src/view/FormDangNhap.java`
2. **Right-click** → **Run As** → **Java Application**
3. Đăng nhập với tài khoản mặc định (xem [Tài Khoản Mặc Định](#-tài-khoản-mặc-định))

---

## 📁 Cấu Trúc Dự Án

```
QuanLyKho-Swing/
│
├── src/
│   ├── dao/                      # Data Access Objects
│   │   ├── KhachHangDAO.java
│   │   ├── KhoDAO.java
│   │   ├── KiemKeDAO.java
│   │   ├── LoaiSanPhamDAO.java
│   │   ├── NhaCungCapDAO.java
│   │   ├── PhieuNhapDAO.java
│   │   ├── PhieuXuatDAO.java
│   │   ├── SanPhamDAO.java
│   │   ├── SaoLuuDAO.java
│   │   ├── TonKhoDAO.java
│   │   └── UserDAO.java
│   │
│   ├── database/                 # Database Connection
│   │   └── DatabaseConnection.java
│   │
│   ├── model/                    # Data Models
│   │   ├── ChiTietKiemKe.java
│   │   ├── ChiTietPhieuNhap.java
│   │   ├── ChiTietPhieuXuat.java
│   │   ├── KhachHang.java
│   │   ├── Kho.java
│   │   ├── KiemKe.java
│   │   ├── LoaiSanPham.java
│   │   ├── NhaCungCap.java
│   │   ├── PhieuNhap.java
│   │   ├── PhieuXuat.java
│   │   ├── SanPham.java
│   │   ├── SaoLuu.java
│   │   ├── TonKho.java
│   │   └── User.java
│   │
│   ├── view/                     # UI Forms
│   │   ├── FormBaoCaoTonKho.java
│   │   ├── FormDangNhap.java
│   │   ├── FormDoiMatKhau.java
│   │   ├── FormKiemKe.java
│   │   ├── FormLichSuKiemKe.java
│   │   ├── FormLichSuNhapXuat.java
│   │   ├── FormMain.java
│   │   ├── FormNhapKho.java
│   │   ├── FormQuanLyKhachHang.java
│   │   ├── FormQuanLyKho.java
│   │   ├── FormQuanLyLoaiSP.java
│   │   ├── FormQuanLyNguoiDung.java
│   │   ├── FormQuanLyNhaCungCap.java
│   │   ├── FormQuanLySanPham.java
│   │   ├── FormSaoLuu.java
│   │   ├── FormXuatKho.java
│   │   ├── DialogSanPhamTrongKho.java
│   │   └── DialogTonKhoTheoKho.java
│   │
│   ├── util/                     # Utilities
│   │   ├── BCryptHelper.java     # Password Hashing
│   │   ├── DatabaseBackup.java   # Backup/Restore
│   │   └── PDFExporter.java      # Export PDF
│   │
│   ├── setup/                    # Setup Scripts
│   │   └── SetupInitialUsers.java
│   │
│   ├── test/                     # Test Scripts
│   │   └── TestTonKho.java       # Inventory Testing
│   │
│   └── qlkho_db.sql              # Database Schema
│
├── lib/                          # External Libraries
│   ├── commons-logging-1.2.jar
│   ├── fontbox-2.0.31.jar
│   ├── jbcrypt-0.4.jar
│   ├── mysql-connector-j-8.0.33.jar
│   └── pdfbox-2.0.31.jar
│
├── bin/                          # Compiled Classes
├── out/                          # Output Files
├── .classpath                    # Eclipse Classpath
├── .project                      # Eclipse Project
├── .gitignore                    # Git Ignore
└── README.md                     # This File
```

---

## 🗄 Cấu Trúc Database

### ERD (Entity Relationship Diagram)

```
users (Người dùng)
  ├── phieu_nhap (Phiếu nhập)
  │     └── chi_tiet_phieu_nhap
  ├── phieu_xuat (Phiếu xuất)
  │     └── chi_tiet_phieu_xuat
  └── kiem_ke (Kiểm kê)
        └── chi_tiet_kiem_ke

san_pham (Sản phẩm)
  ├── loai_san_pham (Loại SP)
  ├── ton_kho (Tồn kho)
  ├── chi_tiet_phieu_nhap
  ├── chi_tiet_phieu_xuat
  └── chi_tiet_kiem_ke

kho (Kho hàng)
  ├── ton_kho
  ├── phieu_nhap
  ├── phieu_xuat
  └── kiem_ke

nha_cung_cap (Nhà cung cấp)
  └── phieu_nhap

khach_hang (Khách hàng)
  └── phieu_xuat
```

### Các Bảng Chính

#### 1. `users` - Người Dùng
| Cột | Kiểu | Mô tả |
|-----|------|-------|
| `id` | INT (PK) | Mã người dùng |
| `username` | VARCHAR(50) | Tên đăng nhập (unique) |
| `password` | VARCHAR(255) | Mật khẩu (BCrypt) |
| `ho_ten` | VARCHAR(100) | Họ tên |
| `role` | ENUM | admin/nhanvien |
| `ngay_tao` | TIMESTAMP | Ngày tạo |
| `trang_thai` | TINYINT | 1=active, 0=inactive |

#### 2. `san_pham` - Sản Phẩm
| Cột | Kiểu | Mô tả |
|-----|------|-------|
| `ma_sp` | INT (PK) | Mã sản phẩm |
| `ten_sp` | VARCHAR(200) | Tên sản phẩm |
| `ma_loai` | INT (FK) | Loại sản phẩm |
| `don_vi_tinh` | VARCHAR(20) | Đơn vị tính |
| `gia_nhap` | DECIMAL(15,2) | Giá nhập |
| `gia_ban` | DECIMAL(15,2) | Giá bán |
| `so_luong_ton` | INT | Tồn kho tổng |
| `mo_ta` | TEXT | Mô tả |
| `hinh_anh` | VARCHAR(255) | Đường dẫn hình |
| `ngay_tao` | TIMESTAMP | Ngày tạo |

#### 3. `kho` - Kho Hàng
| Cột | Kiểu | Mô tả |
|-----|------|-------|
| `id` | INT (PK) | ID kho |
| `ma_kho` | VARCHAR(20) | Mã kho (unique) |
| `ten_kho` | VARCHAR(100) | Tên kho |
| `dia_chi` | TEXT | Địa chỉ |
| `dien_tich` | DECIMAL(10,2) | Diện tích (m²) |
| `nguoi_quan_ly` | VARCHAR(100) | Người quản lý |
| `ghi_chu` | TEXT | Ghi chú |
| `ngay_tao` | TIMESTAMP | Ngày tạo |
| `trang_thai` | TINYINT | Trạng thái |

#### 4. `ton_kho` - Tồn Kho Theo Kho
| Cột | Kiểu | Mô tả |
|-----|------|-------|
| `id` | INT (PK) | ID |
| `ma_sp` | INT (FK) | Mã sản phẩm |
| `ma_kho` | INT (FK) | Mã kho |
| `so_luong_ton` | INT | Số lượng tồn |
| `ngay_cap_nhat` | TIMESTAMP | Ngày cập nhật |

**Constraint**: `UNIQUE(ma_sp, ma_kho)` - Mỗi sản phẩm chỉ có 1 record trong 1 kho

#### 5. `phieu_nhap` - Phiếu Nhập Kho
| Cột | Kiểu | Mô tả |
|-----|------|-------|
| `ma_phieu_nhap` | INT (PK) | Mã phiếu |
| `so_phieu` | VARCHAR(50) | Số phiếu (unique) |
| `ngay_nhap` | DATE | Ngày nhập |
| `ma_ncc` | INT (FK) | Nhà cung cấp |
| `ma_kho` | INT (FK) | Kho nhập |
| `nguoi_lap` | INT (FK) | Người lập |
| `tong_tien` | DECIMAL(15,2) | Tổng tiền |
| `ghi_chu` | TEXT | Ghi chú |
| `ngay_tao` | TIMESTAMP | Ngày tạo |
| `trang_thai` | VARCHAR(20) | Trạng thái (default: 'hoan_thanh') |

#### 6. `chi_tiet_phieu_nhap` - Chi Tiết Phiếu Nhập
| Cột | Kiểu | Mô tả |
|-----|------|-------|
| `id` | INT (PK) | ID |
| `ma_phieu_nhap` | INT (FK) | Mã phiếu nhập |
| `ma_sp` | INT (FK) | Mã sản phẩm |
| `so_luong` | INT | Số lượng |
| `don_gia` | DECIMAL(15,2) | Đơn giá |
| `thanh_tien` | DECIMAL(15,2) | Thành tiền (computed) |

**Computed Column**: `thanh_tien = so_luong * don_gia`

#### 7. `phieu_xuat` - Phiếu Xuất Kho
| Cột | Kiểu | Mô tả |
|-----|------|-------|
| `ma_phieu_xuat` | INT (PK) | Mã phiếu |
| `so_phieu` | VARCHAR(50) | Số phiếu (unique) |
| `ngay_xuat` | DATE | Ngày xuất |
| `ma_kh` | INT (FK) | Khách hàng |
| `ma_kho` | INT (FK) | Kho xuất |
| `nguoi_lap` | INT (FK) | Người lập |
| `tong_tien` | DECIMAL(15,2) | Tổng tiền |
| `ghi_chu` | TEXT | Ghi chú |
| `ngay_tao` | TIMESTAMP | Ngày tạo |
| `trang_thai` | VARCHAR(20) | Trạng thái (default: 'hoan_thanh') |

#### 8. `kiem_ke` - Kiểm Kê
| Cột | Kiểu | Mô tả |
|-----|------|-------|
| `ma_kiem_ke` | INT (PK) | Mã kiểm kê |
| `so_phieu` | VARCHAR(50) | Số phiếu (unique) |
| `ngay_kiem_ke` | DATE | Ngày kiểm kê |
| `ma_kho` | INT (FK) | Kho kiểm kê |
| `nguoi_kiem_ke` | INT (FK) | Người kiểm kê |
| `trang_thai` | ENUM | dang_kiem/hoan_thanh |
| `ghi_chu` | TEXT | Ghi chú |
| `ngay_tao` | TIMESTAMP | Ngày tạo |

#### 9. `chi_tiet_kiem_ke` - Chi Tiết Kiểm Kê
| Cột | Kiểu | Mô tả |
|-----|------|-------|
| `id` | INT (PK) | ID |
| `ma_kiem_ke` | INT (FK) | Mã kiểm kê |
| `ma_sp` | INT (FK) | Mã sản phẩm |
| `ton_he_thong` | INT | Tồn theo hệ thống |
| `ton_thuc_te` | INT | Tồn thực tế |
| `chenh_lech` | INT | Chênh lệch (computed) |
| `ghi_chu` | TEXT | Ghi chú |

**Computed Column**: `chenh_lech = ton_thuc_te - ton_he_thong`

---

## 📖 Hướng Dẫn Sử Dụng

### 1. Đăng Nhập

1. Khởi động ứng dụng
2. Nhập **username** và **password**
3. Click **Đăng nhập**

### 2. Dashboard (Trang Chủ)

Sau khi đăng nhập, bạn sẽ thấy:
- **Tổng số sản phẩm**
- **Số sản phẩm tồn kho thấp** (< 20)
- **Tổng giá trị tồn kho**
- **Bảng sản phẩm tồn kho thấp**

### 3. Quản Lý Sản Phẩm

**Menu**: Quản lý → Quản lý sản phẩm

- **Thêm sản phẩm**: Click "Thêm mới", điền thông tin, click "Lưu"
- **Sửa sản phẩm**: Chọn sản phẩm, click "Sửa", chỉnh sửa, click "Lưu"
- **Xóa sản phẩm**: Chọn sản phẩm, click "Xóa", xác nhận
- **Tìm kiếm**: Nhập từ khóa vào ô tìm kiếm

### 4. Nhập Kho

**Menu**: Nghiệp vụ → Nhập kho

1. Chọn **Nhà cung cấp**
2. Chọn **Kho nhập**
3. Click **Thêm sản phẩm**
4. Chọn sản phẩm, nhập số lượng và đơn giá
5. Click **Lưu phiếu nhập**

### 5. Xuất Kho

**Menu**: Nghiệp vụ → Xuất kho

1. Chọn **Khách hàng**
2. Chọn **Kho xuất**
3. Click **Thêm sản phẩm**
4. Chọn sản phẩm, nhập số lượng và đơn giá
5. Click **Lưu phiếu xuất**

### 6. Kiểm Kê

**Menu**: Nghiệp vụ → Kiểm kê kho

1. Chọn **Kho kiểm kê**
2. Click **Tải sản phẩm** để lấy danh sách
3. Nhập **Tồn thực tế** cho từng sản phẩm
4. Hệ thống tự động tính **Chênh lệch**
5. Click **Lưu kiểm kê**

### 7. Báo Cáo Tồn Kho

**Menu**: Báo cáo → Báo cáo tồn kho

- Xem tồn kho tổng hợp
- Xem tồn kho theo từng kho
- Click **Xuất PDF** để tạo báo cáo

### 8. Sao Lưu Database

**Menu**: Hệ thống → Sao lưu dữ liệu

**Backup**:
1. Chọn đường dẫn lưu file
2. Click **Sao lưu ngay**
3. File `.sql` sẽ được tạo

**Restore**:
1. Click **Browse** để chọn file backup
2. Click **Phục hồi**
3. Xác nhận (⚠️ Sẽ ghi đè dữ liệu hiện tại)

---

## 🔑 Tài Khoản Mặc Định

| Username | Password | Role | Họ Tên |
|----------|----------|------|--------|
| `admin` | `123456` | Admin | Admin |
| `nvkho01` | `123456` | Nhân viên | Lương Duy Khang |
| `nvkho02` | `123456` | Nhân viên | Phan Minh Khôi |

> ⚠️ **Lưu ý**: Đổi mật khẩu ngay sau lần đăng nhập đầu tiên!

---

## 🎯 Tính Năng Chi Tiết

### Phân Quyền

| Tính năng | Admin | Nhân viên |
|-----------|-------|-----------|
| Quản lý sản phẩm | ✅ | ✅ |
| Quản lý loại SP | ✅ | ✅ |
| Quản lý kho | ✅ | ✅ |
| Quản lý khách hàng | ✅ | ✅ |
| Quản lý nhà cung cấp | ✅ | ✅ |
| **Quản lý người dùng** | ✅ | ❌ |
| Nhập kho | ✅ | ✅ |
| Xuất kho | ✅ | ✅ |
| Kiểm kê | ✅ | ✅ |
| Báo cáo | ✅ | ✅ |
| Sao lưu/Phục hồi | ✅ | ✅ |

### Tính Toán Tự Động

1. **Thành tiền** = Số lượng × Đơn giá
2. **Tổng tiền phiếu** = Σ Thành tiền các chi tiết
3. **Chênh lệch kiểm kê** = Tồn thực tế - Tồn hệ thống
4. **Tồn kho tổng** = Σ Tồn kho các kho

### Validation

- ✅ Kiểm tra số lượng > 0
- ✅ Kiểm tra giá > 0
- ✅ Kiểm tra tồn kho đủ khi xuất
- ✅ Kiểm tra trùng username
- ✅ Kiểm tra định dạng email
- ✅ Kiểm tra số điện thoại

---

## 🔌 API & DAO

### Các DAO Class

#### SanPhamDAO
```java
List<SanPham> getAll()                    // Lấy tất cả sản phẩm
SanPham getById(int id)                   // Lấy theo ID
boolean insert(SanPham sp)                // Thêm mới
boolean update(SanPham sp)                // Cập nhật
boolean delete(int id)                    // Xóa
List<SanPham> search(String keyword)      // Tìm kiếm
```

#### PhieuNhapDAO
```java
boolean insert(PhieuNhap pn, List<ChiTietPhieuNhap> chiTiet)
List<PhieuNhap> getAll()
List<PhieuNhap> getByDateRange(Date from, Date to)
```

#### TonKhoDAO
```java
List<TonKho> getByKho(int maKho)
List<TonKho> getBySanPham(int maSp)
boolean updateTonKho(int maSp, int maKho, int soLuong)
```

### Database Connection

```java
// Singleton pattern
Connection conn = DatabaseConnection.getConnection();

// Auto-close
try (Connection conn = DatabaseConnection.getConnection();
     PreparedStatement pst = conn.prepareStatement(sql)) {
    // Your code
}
```

---

## 💾 Backup & Restore

### Cơ Chế Backup

```java
// Sử dụng mysqldump với UTF-8 encoding
List<String> commands = new ArrayList<>();
commands.add(MYSQL_PATH + "mysqldump.exe");
commands.add("-u" + DB_USER);
commands.add("--default-character-set=utf8mb4");
commands.add("--set-charset");
commands.add("--databases");
commands.add(DB_NAME);
commands.add("--result-file=" + outputPath);
```

**Lưu ý**:
- Đường dẫn mysqldump: `C:\xampp\mysql\bin\mysqldump.exe`
- Hỗ trợ đường dẫn có khoảng trắng
- File backup có định dạng: `qlkho_backup_YYYYMMDD_HHMMSS.sql`
- File backup chứa đầy đủ thông tin charset UTF-8

### Cơ Chế Restore

```java
// Sử dụng ProcessBuilder.redirectInput() để xử lý UTF-8 và path có khoảng trắng
List<String> commands = new ArrayList<>();
commands.add(MYSQL_PATH + "mysql.exe");
commands.add("-u" + DB_USER);
commands.add("--default-character-set=utf8mb4");
commands.add(DB_NAME);

ProcessBuilder pb = new ProcessBuilder(commands);
pb.redirectInput(file);  // Redirect từ file SQL
```

**Ưu điểm**:
- ✅ Hỗ trợ đường dẫn có khoảng trắng
- ✅ Giữ nguyên encoding UTF-8 (tiếng Việt)
- ✅ Không cần PowerShell hoặc CMD escape

### Lịch Sử Backup

Lịch sử backup/restore được hiển thị trong form Sao lưu:
- Tên file backup
- Đường dẫn lưu trữ
- Kích thước file
- Thời gian thực hiện
- Trạng thái

---

## 🐛 Troubleshooting

### Lỗi Kết Nối Database

**Lỗi**: `Cannot connect to database`

**Giải pháp**:
1. Kiểm tra MySQL/MariaDB đã chạy chưa
2. Kiểm tra username/password trong `DatabaseConnection.java`
3. Kiểm tra database `qlkho_db` đã tạo chưa

### Lỗi JDBC Driver

**Lỗi**: `ClassNotFoundException: com.mysql.cj.jdbc.Driver`

**Giải pháp**:
1. Kiểm tra file `mysql-connector-j-8.0.33.jar` trong `lib/`
2. Add lại library vào Build Path

### Lỗi Backup

**Lỗi**: `Backup failed with exit code: 1`

**Giải pháp**:
1. Kiểm tra đường dẫn mysqldump trong `DatabaseBackup.java`
2. Đảm bảo XAMPP đã cài đặt đầy đủ
3. Tránh đường dẫn lưu file có khoảng trắng

### Lỗi Font PDF

**Lỗi**: Font tiếng Việt không hiển thị trong PDF

**Giải pháp**:
- Đảm bảo `fontbox-2.0.31.jar` đã được add vào Build Path
- Kiểm tra encoding UTF-8 trong `PDFExporter.java`

### Lỗi Tồn Kho Âm

**Lỗi**: Tồn kho bị âm sau khi xuất

**Giải pháp**:
1. Kiểm tra validation trong `FormXuatKho.java`
2. Chạy lại script `test/TestTonKho.java` để kiểm tra dữ liệu

---

## 🤝 Đóng Góp

Chúng tôi hoan nghênh mọi đóng góp! Vui lòng:

1. **Fork** repository
2. Tạo **branch** mới: `git checkout -b feature/TenTinhNang`
3. **Commit** thay đổi: `git commit -m 'Thêm tính năng XYZ'`
4. **Push** lên branch: `git push origin feature/TenTinhNang`
5. Tạo **Pull Request**

### Coding Standards

- ✅ Sử dụng tiếng Việt có dấu cho UI
- ✅ Comment code bằng tiếng Việt
- ✅ Tuân thủ Java naming conventions
- ✅ Sử dụng try-with-resources cho database
- ✅ Validate input trước khi lưu database

---

## 👨‍💻 Tác Giả

**Phát triển bởi**:
- 👤 **adselvn** - Developer
- 👤 **meankhoiii** - Developer

**Trường**: DNC University  
**Năm**: 2026  
**Phiên bản**: 1.4.0

---

## 📄 License

Dự án này được phân phối dưới giấy phép **MIT License**.

```
MIT License

Copyright (c) 2026 adselvn & meankhoiii

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

---

## 📞 Liên Hệ & Hỗ Trợ

- 📧 Email: [your-email@example.com](mailto:your-email@example.com)
- 🐛 Issues: [GitHub Issues](https://github.com/your-username/QuanLyKho-Swing/issues)
- 📖 Wiki: [GitHub Wiki](https://github.com/your-username/QuanLyKho-Swing/wiki)

---

## 🙏 Cảm Ơn

Cảm ơn bạn đã sử dụng **Hệ Thống Quản Lý Kho Hàng**!

Nếu thấy hữu ích, hãy cho chúng tôi một ⭐ trên GitHub!

---

<div align="center">
  <p>Made with ❤️ by adselvn & meankhoiii</p>
  <p>© 2026 DNC University. All rights reserved.</p>
</div>
