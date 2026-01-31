-- Migration Script: Add ton_kho table for warehouse inventory tracking
-- Date: 2026-01-31
-- Description: Creates ton_kho table to track inventory per warehouse

USE qlkho_db;

-- Create ton_kho table
CREATE TABLE IF NOT EXISTS `ton_kho` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ma_sp` int(11) NOT NULL,
  `ma_kho` int(11) NOT NULL,
  `so_luong_ton` int(11) DEFAULT 0,
  `ngay_cap_nhat` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_sp_kho` (`ma_sp`,`ma_kho`),
  KEY `ma_kho` (`ma_kho`),
  CONSTRAINT `ton_kho_ibfk_1` FOREIGN KEY (`ma_sp`) REFERENCES `san_pham` (`ma_sp`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ton_kho_ibfk_2` FOREIGN KEY (`ma_kho`) REFERENCES `kho` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Add missing FK constraint for san_pham.ma_kho (if not exists)
-- First check if constraint exists, if not add it
SET @constraint_exists = (
  SELECT COUNT(*) 
  FROM information_schema.TABLE_CONSTRAINTS 
  WHERE CONSTRAINT_SCHEMA = 'qlkho_db' 
    AND TABLE_NAME = 'san_pham' 
    AND CONSTRAINT_NAME = 'san_pham_ibfk_2'
);

SET @sql = IF(@constraint_exists = 0,
  'ALTER TABLE `san_pham` ADD CONSTRAINT `san_pham_ibfk_2` FOREIGN KEY (`ma_kho`) REFERENCES `kho` (`id`) ON DELETE SET NULL ON UPDATE CASCADE',
  'SELECT "Constraint san_pham_ibfk_2 already exists" AS message'
);

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Migrate existing inventory data from san_pham to ton_kho
-- This will create ton_kho records for all existing products
INSERT INTO `ton_kho` (`ma_sp`, `ma_kho`, `so_luong_ton`)
SELECT `ma_sp`, COALESCE(`ma_kho`, 1), COALESCE(`so_luong_ton`, 0)
FROM `san_pham`
WHERE `ma_sp` NOT IN (SELECT `ma_sp` FROM `ton_kho`)
ON DUPLICATE KEY UPDATE `so_luong_ton` = VALUES(`so_luong_ton`);

-- Success message
SELECT 'Migration completed successfully!' AS status,
       (SELECT COUNT(*) FROM `ton_kho`) AS total_ton_kho_records;
