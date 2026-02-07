-- =====================================================
-- RESET DATABASE - XÓA DỮ LIỆU CŨ VÀ NẠP DỮ LIỆU MỚI
-- Giữ lại bảng users (không xóa tài khoản)
-- Generated: 2026-02-07
-- =====================================================

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

USE qlkho_db;

-- =====================================================
-- BƯỚC 1: XÓA DỮ LIỆU CŨ (TRỪ BẢNG USERS)
-- =====================================================

SET FOREIGN_KEY_CHECKS = 0;

-- Xóa dữ liệu các bảng chi tiết trước
TRUNCATE TABLE `chi_tiet_kiem_ke`;
TRUNCATE TABLE `chi_tiet_phieu_nhap`;
TRUNCATE TABLE `chi_tiet_phieu_xuat`;

-- Xóa dữ liệu các bảng phiếu
TRUNCATE TABLE `kiem_ke`;
TRUNCATE TABLE `phieu_nhap`;
TRUNCATE TABLE `phieu_xuat`;

-- Xóa dữ liệu tồn kho
TRUNCATE TABLE `ton_kho`;

-- Xóa dữ liệu sản phẩm và danh mục
TRUNCATE TABLE `san_pham`;
TRUNCATE TABLE `loai_san_pham`;

-- Xóa dữ liệu khách hàng và nhà cung cấp
TRUNCATE TABLE `khach_hang`;
TRUNCATE TABLE `nha_cung_cap`;

-- Xóa dữ liệu kho
TRUNCATE TABLE `kho`;

-- Xóa dữ liệu sao lưu
TRUNCATE TABLE `sao_luu`;

SET FOREIGN_KEY_CHECKS = 1;

-- =====================================================
-- BƯỚC 2: NẠP DỮ LIỆU MỚI
-- =====================================================

-- -----------------------------------------------------
-- 1. KHO (3 kho)
-- -----------------------------------------------------
INSERT INTO `kho` (`id`, `ma_kho`, `ten_kho`, `dia_chi`, `dien_tich`, `nguoi_quan_ly`, `ghi_chu`, `trang_thai`) VALUES
(1, 'KHO-A', 'Kho Tổng', '123 Đường Nguyễn Văn Linh, Quận 7, TP.HCM', 500.00, 'Nguyễn Văn A', 'Kho chính, lưu trữ hàng hóa chính', 1),
(2, 'KHO-B', 'Kho Phụ 1', '456 Đường Lê Văn Việt, Quận 9, TP.HCM', 300.00, 'Trần Thị B', 'Kho phụ, lưu trữ hàng dự phòng', 1),
(3, 'KHO-C', 'Kho Phụ 2', '789 Đường Võ Văn Ngân, Thủ Đức, TP.HCM', 200.00, 'Lê Văn C', 'Kho phụ, lưu trữ hàng xuất khẩu', 1);

-- -----------------------------------------------------
-- 2. LOẠI SẢN PHẨM (5 loại)
-- -----------------------------------------------------
INSERT INTO `loai_san_pham` (`ma_loai`, `ten_loai`, `mo_ta`) VALUES
(1, 'Điện tử', 'Các sản phẩm điện tử, thiết bị công nghệ'),
(2, 'Văn phòng phẩm', 'Dụng cụ văn phòng, học tập'),
(3, 'Thực phẩm', 'Thực phẩm khô, đồ uống'),
(4, 'Gia dụng', 'Đồ gia dụng, nội thất'),
(5, 'Thời trang', 'Quần áo, phụ kiện thời trang');

-- -----------------------------------------------------
-- 3. SẢN PHẨM (15 sản phẩm)
-- -----------------------------------------------------
INSERT INTO `san_pham` (`ma_sp`, `ten_sp`, `ma_loai`, `don_vi_tinh`, `gia_nhap`, `gia_ban`, `so_luong_ton`, `mo_ta`, `hinh_anh`) VALUES
-- Điện tử
(1, 'Laptop Dell Inspiron 15', 1, 'Cái', 12000000.00, 15000000.00, 0, 'Laptop Dell Inspiron 15, RAM 8GB, SSD 256GB', NULL),
(2, 'Chuột Logitech M185', 1, 'Cái', 80000.00, 120000.00, 0, 'Chuột không dây Logitech M185', NULL),
(3, 'Bàn phím cơ Keychron K2', 1, 'Cái', 1500000.00, 2000000.00, 0, 'Bàn phím cơ Keychron K2, RGB', NULL),

-- Văn phòng phẩm
(4, 'Bút bi Thiên Long', 2, 'Hộp', 15000.00, 25000.00, 0, 'Hộp 10 cây bút bi Thiên Long', NULL),
(5, 'Giấy A4 Double A', 2, 'Ream', 80000.00, 100000.00, 0, 'Giấy A4 Double A 70gsm, 500 tờ', NULL),
(6, 'Sổ tay Bìa Cứng A5', 2, 'Quyển', 25000.00, 40000.00, 0, 'Sổ tay bìa cứng A5, 200 trang', NULL),

-- Thực phẩm
(7, 'Mì Hảo Hảo', 3, 'Thùng', 100000.00, 120000.00, 0, 'Thùng 30 gói mì Hảo Hảo', NULL),
(8, 'Nước ngọt Coca Cola', 3, 'Thùng', 150000.00, 180000.00, 0, 'Thùng 24 lon Coca Cola 330ml', NULL),
(9, 'Cà phê Trung Nguyên', 3, 'Hộp', 50000.00, 70000.00, 0, 'Hộp cà phê Trung Nguyên G7 3in1', NULL),

-- Gia dụng
(10, 'Nồi cơm điện Sharp', 4, 'Cái', 800000.00, 1000000.00, 0, 'Nồi cơm điện Sharp 1.8L', NULL),
(11, 'Bình đun siêu tốc', 4, 'Cái', 200000.00, 300000.00, 0, 'Bình đun siêu tốc 1.7L', NULL),
(12, 'Quạt điện Senko', 4, 'Cái', 350000.00, 500000.00, 0, 'Quạt điện Senko 16 inch', NULL),

-- Thời trang
(13, 'Áo thun nam', 5, 'Cái', 80000.00, 150000.00, 0, 'Áo thun nam cotton 100%', NULL),
(14, 'Quần jean nữ', 5, 'Cái', 200000.00, 350000.00, 0, 'Quần jean nữ skinny', NULL),
(15, 'Giày thể thao', 5, 'Đôi', 300000.00, 500000.00, 0, 'Giày thể thao unisex', NULL);

-- -----------------------------------------------------
-- 4. NHÀ CUNG CẤP (5 nhà cung cấp)
-- -----------------------------------------------------
INSERT INTO `nha_cung_cap` (`ma_ncc`, `ten_ncc`, `dia_chi`, `sdt`, `email`, `nguoi_lien_he`, `trang_thai`) VALUES
(1, 'Công ty TNHH Điện Tử Việt', '123 Lê Lợi, Quận 1, TP.HCM', '0901234567', 'contact@dientu.vn', 'Nguyễn Văn A', 1),
(2, 'Công ty CP Văn Phòng Phẩm Hà Nội', '456 Trần Hưng Đạo, Hà Nội', '0912345678', 'info@vanphongpham.vn', 'Trần Thị B', 1),
(3, 'Công ty TNHH Thực Phẩm An Toàn', '789 Nguyễn Huệ, Quận 1, TP.HCM', '0923456789', 'sales@thucpham.vn', 'Lê Văn C', 1),
(4, 'Công ty CP Gia Dụng Miền Nam', '321 Võ Văn Tần, Quận 3, TP.HCM', '0934567890', 'contact@giadung.vn', 'Phạm Thị D', 1),
(5, 'Công ty TNHH Thời Trang Trẻ', '654 Nguyễn Trãi, Quận 5, TP.HCM', '0945678901', 'info@thoitrang.vn', 'Hoàng Văn E', 1);

-- -----------------------------------------------------
-- 5. KHÁCH HÀNG (5 khách hàng)
-- -----------------------------------------------------
INSERT INTO `khach_hang` (`ma_kh`, `ten_kh`, `dia_chi`, `sdt`, `email`, `loai_kh`, `trang_thai`) VALUES
(1, 'Nguyễn Văn An', '123 Lý Thường Kiệt, Quận 10, TP.HCM', '0909111222', 'nguyenvanan@gmail.com', 'canhan', 1),
(2, 'Công ty TNHH ABC', '456 Điện Biên Phủ, Quận Bình Thạnh, TP.HCM', '0909222333', 'contact@abc.vn', 'doanhnghiep', 1),
(3, 'Trần Thị Bình', '789 Cách Mạng Tháng 8, Quận 3, TP.HCM', '0909333444', 'tranbinh@yahoo.com', 'canhan', 1),
(4, 'Công ty CP XYZ', '321 Lê Văn Sỹ, Quận Phú Nhuận, TP.HCM', '0909444555', 'info@xyz.vn', 'doanhnghiep', 1),
(5, 'Lê Văn Cường', '654 Hoàng Văn Thụ, Quận Tân Bình, TP.HCM', '0909555666', 'cuongle@hotmail.com', 'canhan', 1);

-- -----------------------------------------------------
-- 6. PHIẾU NHẬP (5 phiếu nhập)
-- -----------------------------------------------------
INSERT INTO `phieu_nhap` (`ma_phieu_nhap`, `so_phieu`, `ngay_nhap`, `ma_ncc`, `ma_kho`, `nguoi_lap`, `tong_tien`, `ghi_chu`, `trang_thai`) VALUES
(1, 'PN001', '2026-01-15', 1, 1, 1, 24160000.00, 'Nhập hàng điện tử đợt 1', 'hoan_thanh'),
(2, 'PN002', '2026-01-20', 2, 1, 1, 2120000.00, 'Nhập văn phòng phẩm', 'hoan_thanh'),
(3, 'PN003', '2026-01-25', 3, 2, 1, 3000000.00, 'Nhập thực phẩm', 'hoan_thanh'),
(4, 'PN004', '2026-02-01', 4, 2, 1, 2700000.00, 'Nhập gia dụng', 'hoan_thanh'),
(5, 'PN005', '2026-02-05', 5, 3, 1, 2880000.00, 'Nhập thời trang', 'hoan_thanh');

-- -----------------------------------------------------
-- 7. CHI TIẾT PHIẾU NHẬP
-- -----------------------------------------------------
INSERT INTO `chi_tiet_phieu_nhap` (`id`, `ma_phieu_nhap`, `ma_sp`, `so_luong`, `don_gia`) VALUES
-- Phiếu PN001 - Điện tử
(1, 1, 1, 2, 12000000.00),  -- 2 Laptop
(2, 1, 2, 20, 80000.00),    -- 20 Chuột
(3, 1, 3, 1, 1500000.00),   -- 1 Bàn phím

-- Phiếu PN002 - Văn phòng phẩm
(4, 2, 4, 50, 15000.00),    -- 50 Hộp bút
(5, 2, 5, 10, 80000.00),    -- 10 Ream giấy
(6, 2, 6, 30, 25000.00),    -- 30 Sổ tay

-- Phiếu PN003 - Thực phẩm
(7, 3, 7, 10, 100000.00),   -- 10 Thùng mì
(8, 3, 8, 10, 150000.00),   -- 10 Thùng Coca
(9, 3, 9, 10, 50000.00),    -- 10 Hộp cà phê

-- Phiếu PN004 - Gia dụng
(10, 4, 10, 2, 800000.00),  -- 2 Nồi cơm
(11, 4, 11, 5, 200000.00),  -- 5 Bình đun
(12, 4, 12, 2, 350000.00),  -- 2 Quạt

-- Phiếu PN005 - Thời trang
(13, 5, 13, 20, 80000.00),  -- 20 Áo thun
(14, 5, 14, 10, 200000.00), -- 10 Quần jean
(15, 5, 15, 4, 300000.00);  -- 4 Giày

-- -----------------------------------------------------
-- 8. TỒN KHO (Sau khi nhập hàng)
-- -----------------------------------------------------
INSERT INTO `ton_kho` (`id`, `ma_sp`, `ma_kho`, `so_luong_ton`) VALUES
-- Kho A (KHO-A) - Điện tử + Văn phòng phẩm
(1, 1, 1, 2),   -- Laptop
(2, 2, 1, 20),  -- Chuột
(3, 3, 1, 1),   -- Bàn phím
(4, 4, 1, 50),  -- Bút
(5, 5, 1, 10),  -- Giấy
(6, 6, 1, 30),  -- Sổ tay

-- Kho B (KHO-B) - Thực phẩm + Gia dụng
(7, 7, 2, 10),  -- Mì
(8, 8, 2, 10),  -- Coca
(9, 9, 2, 10),  -- Cà phê
(10, 10, 2, 2), -- Nồi cơm
(11, 11, 2, 5), -- Bình đun
(12, 12, 2, 2), -- Quạt

-- Kho C (KHO-C) - Thời trang
(13, 13, 3, 20), -- Áo thun
(14, 14, 3, 10), -- Quần jean
(15, 15, 3, 4);  -- Giày

-- -----------------------------------------------------
-- 9. CẬP NHẬT SỐ LƯỢNG TỒN TRONG BẢNG SAN_PHAM
-- (Tổng tồn kho từ tất cả các kho)
-- -----------------------------------------------------
UPDATE `san_pham` sp
SET sp.so_luong_ton = (
    SELECT COALESCE(SUM(tk.so_luong_ton), 0)
    FROM `ton_kho` tk
    WHERE tk.ma_sp = sp.ma_sp
);

-- -----------------------------------------------------
-- 10. PHIẾU XUẤT (3 phiếu xuất)
-- -----------------------------------------------------
INSERT INTO `phieu_xuat` (`ma_phieu_xuat`, `so_phieu`, `ngay_xuat`, `ma_kh`, `ma_kho`, `nguoi_lap`, `tong_tien`, `ghi_chu`, `trang_thai`) VALUES
(1, 'PX001', '2026-02-06', 1, 1, 1, 15120000.00, 'Xuất hàng cho khách lẻ', 'hoan_thanh'),
(2, 'PX002', '2026-02-07', 2, 2, 1, 360000.00, 'Xuất hàng cho công ty ABC', 'hoan_thanh'),
(3, 'PX003', '2026-02-07', 3, 3, 1, 1050000.00, 'Xuất hàng thời trang', 'hoan_thanh');

-- -----------------------------------------------------
-- 11. CHI TIẾT PHIẾU XUẤT
-- -----------------------------------------------------
INSERT INTO `chi_tiet_phieu_xuat` (`id`, `ma_phieu_xuat`, `ma_sp`, `so_luong`, `don_gia`) VALUES
-- Phiếu PX001 - Xuất điện tử
(1, 1, 1, 1, 15000000.00),  -- 1 Laptop
(2, 1, 2, 1, 120000.00),    -- 1 Chuột

-- Phiếu PX002 - Xuất thực phẩm
(3, 2, 7, 2, 120000.00),    -- 2 Thùng mì
(4, 2, 8, 1, 180000.00),    -- 1 Thùng Coca

-- Phiếu PX003 - Xuất thời trang
(5, 3, 13, 5, 150000.00),   -- 5 Áo thun
(6, 3, 14, 1, 350000.00);   -- 1 Quần jean

-- -----------------------------------------------------
-- 12. CẬP NHẬT TỒN KHO SAU KHI XUẤT
-- -----------------------------------------------------
-- Giảm tồn kho sau khi xuất hàng
UPDATE `ton_kho` SET `so_luong_ton` = `so_luong_ton` - 1 WHERE `ma_sp` = 1 AND `ma_kho` = 1;  -- Laptop
UPDATE `ton_kho` SET `so_luong_ton` = `so_luong_ton` - 1 WHERE `ma_sp` = 2 AND `ma_kho` = 1;  -- Chuột
UPDATE `ton_kho` SET `so_luong_ton` = `so_luong_ton` - 2 WHERE `ma_sp` = 7 AND `ma_kho` = 2;  -- Mì
UPDATE `ton_kho` SET `so_luong_ton` = `so_luong_ton` - 1 WHERE `ma_sp` = 8 AND `ma_kho` = 2;  -- Coca
UPDATE `ton_kho` SET `so_luong_ton` = `so_luong_ton` - 5 WHERE `ma_sp` = 13 AND `ma_kho` = 3; -- Áo thun
UPDATE `ton_kho` SET `so_luong_ton` = `so_luong_ton` - 1 WHERE `ma_sp` = 14 AND `ma_kho` = 3; -- Quần jean

-- Cập nhật lại tổng tồn trong bảng san_pham
UPDATE `san_pham` sp
SET sp.so_luong_ton = (
    SELECT COALESCE(SUM(tk.so_luong_ton), 0)
    FROM `ton_kho` tk
    WHERE tk.ma_sp = sp.ma_sp
);

-- =====================================================
-- HOÀN TẤT
-- =====================================================

COMMIT;

-- Hiển thị thống kê
SELECT 'RESET DATABASE THÀNH CÔNG!' as status;
SELECT '================================' as separator;
SELECT CONCAT('Số kho: ', COUNT(*)) as thong_ke FROM kho;
SELECT CONCAT('Số loại sản phẩm: ', COUNT(*)) as thong_ke FROM loai_san_pham;
SELECT CONCAT('Số sản phẩm: ', COUNT(*)) as thong_ke FROM san_pham;
SELECT CONCAT('Số nhà cung cấp: ', COUNT(*)) as thong_ke FROM nha_cung_cap;
SELECT CONCAT('Số khách hàng: ', COUNT(*)) as thong_ke FROM khach_hang;
SELECT CONCAT('Số phiếu nhập: ', COUNT(*)) as thong_ke FROM phieu_nhap;
SELECT CONCAT('Số phiếu xuất: ', COUNT(*)) as thong_ke FROM phieu_xuat;
SELECT CONCAT('Số bản ghi tồn kho: ', COUNT(*)) as thong_ke FROM ton_kho;
SELECT CONCAT('Tổng giá trị tồn kho: ', FORMAT(SUM(sp.gia_nhap * tk.so_luong_ton), 0), ' VNĐ') as thong_ke 
FROM ton_kho tk 
JOIN san_pham sp ON tk.ma_sp = sp.ma_sp;
