-- Script: Phân bố tồn kho sang nhiều kho
-- Chạy trong MySQL Workbench hoặc phpMyAdmin

USE qlkho_db;

-- 1. Kiểm tra danh sách kho
SELECT id, ten_kho FROM kho;

-- 2. Kiểm tra tồn kho hiện tại
SELECT tk.ma_sp, sp.ten_sp, tk.ma_kho, tk.so_luong_ton 
FROM ton_kho tk 
JOIN san_pham sp ON tk.ma_sp = sp.ma_sp 
ORDER BY tk.ma_sp;

-- 3. Phân bố tồn kho:
-- - Một số SP có ở cả 2 kho
-- - Một số SP chỉ ở kho 2

-- Thêm SP vào kho 2 (những SP có ID chia hết cho 3)
INSERT INTO ton_kho (ma_sp, ma_kho, so_luong_ton)
SELECT ma_sp, 2, 25
FROM san_pham 
WHERE ma_sp % 3 = 0
ON DUPLICATE KEY UPDATE so_luong_ton = so_luong_ton + 25;

-- Chuyển một số SP sang chỉ có ở kho 2 (ID chia hết cho 5)
UPDATE ton_kho SET ma_kho = 2 
WHERE ma_sp % 5 = 0 AND ma_kho = 1;

-- 4. Kiểm tra kết quả
SELECT 
    tk.ma_sp, 
    sp.ten_sp, 
    tk.ma_kho, 
    k.ten_kho,
    tk.so_luong_ton 
FROM ton_kho tk 
JOIN san_pham sp ON tk.ma_sp = sp.ma_sp 
JOIN kho k ON tk.ma_kho = k.id
ORDER BY tk.ma_kho, tk.ma_sp;

-- 5. Thống kê theo kho
SELECT 
    k.ten_kho,
    COUNT(*) as so_san_pham,
    SUM(tk.so_luong_ton) as tong_ton_kho
FROM ton_kho tk
JOIN kho k ON tk.ma_kho = k.id
GROUP BY tk.ma_kho;
