-- Migration: Add ma_kho to phieu_xuat
-- Date: 2026-01-31
-- Description: Add warehouse tracking to export receipts

USE qlkho_db;

-- Add ma_kho column to phieu_xuat
ALTER TABLE phieu_xuat 
ADD COLUMN ma_kho INT DEFAULT NULL AFTER ma_kh;

-- Add FK constraint
ALTER TABLE phieu_xuat
ADD CONSTRAINT phieu_xuat_ibfk_3 
FOREIGN KEY (ma_kho) REFERENCES kho(id) 
ON DELETE SET NULL ON UPDATE CASCADE;

-- Add index for performance
ALTER TABLE phieu_xuat
ADD KEY idx_ma_kho (ma_kho);

SELECT '✓ Đã thêm ma_kho vào phieu_xuat' as status;
