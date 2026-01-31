-- Migration Script: Simplify Schema Level 1
-- Date: 2026-01-31
-- Description: Remove duplicate/unnecessary columns

USE qlkho_db;

-- Backup tables (optional, uncomment if needed)
-- CREATE TABLE IF NOT EXISTS san_pham_backup_20260131 AS SELECT * FROM san_pham;
-- CREATE TABLE IF NOT EXISTS phieu_nhap_backup_20260131 AS SELECT * FROM phieu_nhap;
-- CREATE TABLE IF NOT EXISTS phieu_xuat_backup_20260131 AS SELECT * FROM phieu_xuat;

-- 1. Bỏ cột nha_cung_cap từ phieu_nhap (trùng lặp với ma_ncc)
ALTER TABLE phieu_nhap DROP COLUMN nha_cung_cap;
SELECT '✓ Đã bỏ phieu_nhap.nha_cung_cap' as status;

-- 2. Bỏ cột khach_hang từ phieu_xuat (trùng lặp với ma_kh)
ALTER TABLE phieu_xuat DROP COLUMN khach_hang;
SELECT '✓ Đã bỏ phieu_xuat.khach_hang' as status;

-- 3. Bỏ FK constraint và cột ma_kho từ san_pham (không cần thiết khi có ton_kho)
ALTER TABLE san_pham DROP FOREIGN KEY san_pham_ibfk_2;
SELECT '✓ Đã bỏ FK constraint san_pham_ibfk_2' as status;

ALTER TABLE san_pham DROP COLUMN ma_kho;
SELECT '✓ Đã bỏ san_pham.ma_kho' as status;

-- Verification
SELECT '=== HOÀN THÀNH CẤP ĐỘ 1 ===' as status;
SELECT 'Đã bỏ 3 cột trùng lặp/không cần thiết' as message;

-- Show updated table structures
SHOW COLUMNS FROM san_pham;
SHOW COLUMNS FROM phieu_nhap;
SHOW COLUMNS FROM phieu_xuat;
