-- Migration: Add ma_kho to phieu_nhap
-- Date: 2026-01-31
-- Description: Add warehouse tracking to import receipts

USE qlkho_db;

-- Add ma_kho column to phieu_nhap
ALTER TABLE phieu_nhap 
ADD COLUMN ma_kho INT DEFAULT NULL AFTER ma_ncc;

-- Add FK constraint
ALTER TABLE phieu_nhap
ADD CONSTRAINT phieu_nhap_ibfk_3 
FOREIGN KEY (ma_kho) REFERENCES kho(id) 
ON DELETE SET NULL ON UPDATE CASCADE;

-- Add index for performance
ALTER TABLE phieu_nhap
ADD KEY idx_ma_kho (ma_kho);

SELECT '✓ Đã thêm ma_kho vào phieu_nhap' as status;
