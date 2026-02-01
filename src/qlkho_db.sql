-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Feb 01, 2026 at 05:05 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `qlkho_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `chi_tiet_kiem_ke`
--

CREATE TABLE `chi_tiet_kiem_ke` (
  `id` int(11) NOT NULL,
  `ma_kiem_ke` int(11) NOT NULL,
  `ma_sp` int(11) NOT NULL,
  `ton_he_thong` int(11) NOT NULL COMMENT 'Tồn kho theo hệ thống',
  `ton_thuc_te` int(11) NOT NULL COMMENT 'Tồn kho đếm thực tế',
  `chenh_lech` int(11) GENERATED ALWAYS AS (`ton_thuc_te` - `ton_he_thong`) STORED,
  `ghi_chu` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `chi_tiet_phieu_nhap`
--

CREATE TABLE `chi_tiet_phieu_nhap` (
  `id` int(11) NOT NULL,
  `ma_phieu_nhap` int(11) NOT NULL,
  `ma_sp` int(11) NOT NULL,
  `so_luong` int(11) NOT NULL CHECK (`so_luong` > 0),
  `don_gia` decimal(15,2) NOT NULL,
  `thanh_tien` decimal(15,2) GENERATED ALWAYS AS (`so_luong` * `don_gia`) STORED
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `chi_tiet_phieu_xuat`
--

CREATE TABLE `chi_tiet_phieu_xuat` (
  `id` int(11) NOT NULL,
  `ma_phieu_xuat` int(11) NOT NULL,
  `ma_sp` int(11) NOT NULL,
  `so_luong` int(11) NOT NULL CHECK (`so_luong` > 0),
  `don_gia` decimal(15,2) NOT NULL,
  `thanh_tien` decimal(15,2) GENERATED ALWAYS AS (`so_luong` * `don_gia`) STORED
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `khach_hang`
--

CREATE TABLE `khach_hang` (
  `ma_kh` int(11) NOT NULL,
  `ten_kh` varchar(200) NOT NULL,
  `dia_chi` text DEFAULT NULL,
  `sdt` varchar(15) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `loai_kh` enum('canhan','doanhnghiep') DEFAULT 'canhan',
  `ngay_tao` timestamp NOT NULL DEFAULT current_timestamp(),
  `trang_thai` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `kho`
--

CREATE TABLE `kho` (
  `id` int(11) NOT NULL,
  `ma_kho` varchar(20) NOT NULL,
  `ten_kho` varchar(100) NOT NULL,
  `dia_chi` text DEFAULT NULL,
  `dien_tich` decimal(10,2) DEFAULT NULL,
  `nguoi_quan_ly` varchar(100) DEFAULT NULL,
  `ghi_chu` text DEFAULT NULL,
  `ngay_tao` timestamp NOT NULL DEFAULT current_timestamp(),
  `trang_thai` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `kiem_ke`
--

CREATE TABLE `kiem_ke` (
  `ma_kiem_ke` int(11) NOT NULL,
  `so_phieu` varchar(50) NOT NULL,
  `ngay_kiem_ke` date NOT NULL,
  `ma_kho` int(11) DEFAULT NULL,
  `nguoi_kiem_ke` int(11) DEFAULT NULL,
  `trang_thai` enum('dang_kiem','hoan_thanh') DEFAULT 'dang_kiem',
  `ghi_chu` text DEFAULT NULL,
  `ngay_tao` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `loai_san_pham`
--

CREATE TABLE `loai_san_pham` (
  `ma_loai` int(11) NOT NULL,
  `ten_loai` varchar(100) NOT NULL,
  `mo_ta` text DEFAULT NULL,
  `ngay_tao` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `nha_cung_cap`
--

CREATE TABLE `nha_cung_cap` (
  `ma_ncc` int(11) NOT NULL,
  `ten_ncc` varchar(200) NOT NULL,
  `dia_chi` text DEFAULT NULL,
  `sdt` varchar(15) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `nguoi_lien_he` varchar(100) DEFAULT NULL,
  `ngay_tao` timestamp NOT NULL DEFAULT current_timestamp(),
  `trang_thai` tinyint(1) DEFAULT 1 COMMENT '1: active, 0: inactive'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `phieu_nhap`
--

CREATE TABLE `phieu_nhap` (
  `ma_phieu_nhap` int(11) NOT NULL,
  `so_phieu` varchar(50) NOT NULL,
  `ngay_nhap` date NOT NULL,
  `ma_ncc` int(11) DEFAULT NULL,
  `ma_kho` int(11) DEFAULT NULL,
  `nguoi_lap` int(11) DEFAULT NULL,
  `tong_tien` decimal(15,2) DEFAULT 0.00,
  `ghi_chu` text DEFAULT NULL,
  `ngay_tao` timestamp NOT NULL DEFAULT current_timestamp(),
  `trang_thai` varchar(20) DEFAULT 'hoan_thanh'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `phieu_xuat`
--

CREATE TABLE `phieu_xuat` (
  `ma_phieu_xuat` int(11) NOT NULL,
  `so_phieu` varchar(50) NOT NULL,
  `ngay_xuat` date NOT NULL,
  `ma_kh` int(11) DEFAULT NULL,
  `ma_kho` int(11) DEFAULT NULL,
  `nguoi_lap` int(11) DEFAULT NULL,
  `tong_tien` decimal(15,2) DEFAULT 0.00,
  `ghi_chu` text DEFAULT NULL,
  `ngay_tao` timestamp NOT NULL DEFAULT current_timestamp(),
  `trang_thai` varchar(20) DEFAULT 'hoan_thanh'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `san_pham`
--

CREATE TABLE `san_pham` (
  `ma_sp` int(11) NOT NULL,
  `ten_sp` varchar(200) NOT NULL,
  `ma_loai` int(11) NOT NULL,
  `don_vi_tinh` varchar(20) DEFAULT 'Cái',
  `gia_nhap` decimal(15,2) DEFAULT 0.00,
  `gia_ban` decimal(15,2) DEFAULT 0.00,
  `so_luong_ton` int(11) DEFAULT 0,
  `mo_ta` text DEFAULT NULL,
  `hinh_anh` varchar(255) DEFAULT NULL,
  `ngay_tao` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `sao_luu`
--

CREATE TABLE `sao_luu` (
  `ma_sao_luu` int(11) NOT NULL,
  `ten_file` varchar(255) NOT NULL,
  `duong_dan` text NOT NULL,
  `kich_thuoc` bigint(20) DEFAULT NULL COMMENT 'Kích thước file (bytes)',
  `nguoi_thuc_hien` int(11) DEFAULT NULL,
  `loai` enum('backup','restore') DEFAULT 'backup',
  `ngay_thuc_hien` timestamp NOT NULL DEFAULT current_timestamp(),
  `ghi_chu` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `ton_kho`
--

CREATE TABLE `ton_kho` (
  `id` int(11) NOT NULL,
  `ma_sp` int(11) NOT NULL,
  `ma_kho` int(11) NOT NULL,
  `so_luong_ton` int(11) DEFAULT 0,
  `ngay_cap_nhat` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `ho_ten` varchar(100) NOT NULL,
  `role` enum('admin','nhanvien') DEFAULT 'nhanvien',
  `ngay_tao` timestamp NOT NULL DEFAULT current_timestamp(),
  `trang_thai` tinyint(1) DEFAULT 1 COMMENT '1: active, 0: inactive'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `chi_tiet_kiem_ke`
--
ALTER TABLE `chi_tiet_kiem_ke`
  ADD PRIMARY KEY (`id`),
  ADD KEY `ma_kiem_ke` (`ma_kiem_ke`),
  ADD KEY `ma_sp` (`ma_sp`);

--
-- Indexes for table `chi_tiet_phieu_nhap`
--
ALTER TABLE `chi_tiet_phieu_nhap`
  ADD PRIMARY KEY (`id`),
  ADD KEY `ma_phieu_nhap` (`ma_phieu_nhap`),
  ADD KEY `ma_sp` (`ma_sp`);

--
-- Indexes for table `chi_tiet_phieu_xuat`
--
ALTER TABLE `chi_tiet_phieu_xuat`
  ADD PRIMARY KEY (`id`),
  ADD KEY `ma_phieu_xuat` (`ma_phieu_xuat`),
  ADD KEY `ma_sp` (`ma_sp`);

--
-- Indexes for table `khach_hang`
--
ALTER TABLE `khach_hang`
  ADD PRIMARY KEY (`ma_kh`);

--
-- Indexes for table `kho`
--
ALTER TABLE `kho`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `ma_kho` (`ma_kho`);

--
-- Indexes for table `kiem_ke`
--
ALTER TABLE `kiem_ke`
  ADD PRIMARY KEY (`ma_kiem_ke`),
  ADD UNIQUE KEY `so_phieu` (`so_phieu`),
  ADD KEY `ma_kho` (`ma_kho`),
  ADD KEY `nguoi_kiem_ke` (`nguoi_kiem_ke`);

--
-- Indexes for table `loai_san_pham`
--
ALTER TABLE `loai_san_pham`
  ADD PRIMARY KEY (`ma_loai`);

--
-- Indexes for table `nha_cung_cap`
--
ALTER TABLE `nha_cung_cap`
  ADD PRIMARY KEY (`ma_ncc`);

--
-- Indexes for table `phieu_nhap`
--
ALTER TABLE `phieu_nhap`
  ADD PRIMARY KEY (`ma_phieu_nhap`),
  ADD UNIQUE KEY `so_phieu` (`so_phieu`),
  ADD KEY `nguoi_lap` (`nguoi_lap`),
  ADD KEY `ma_ncc` (`ma_ncc`),
  ADD KEY `idx_ma_kho` (`ma_kho`);

--
-- Indexes for table `phieu_xuat`
--
ALTER TABLE `phieu_xuat`
  ADD PRIMARY KEY (`ma_phieu_xuat`),
  ADD UNIQUE KEY `so_phieu` (`so_phieu`),
  ADD KEY `nguoi_lap` (`nguoi_lap`),
  ADD KEY `ma_kh` (`ma_kh`),
  ADD KEY `idx_ma_kho` (`ma_kho`);

--
-- Indexes for table `san_pham`
--
ALTER TABLE `san_pham`
  ADD PRIMARY KEY (`ma_sp`),
  ADD KEY `ma_loai` (`ma_loai`);

--
-- Indexes for table `sao_luu`
--
ALTER TABLE `sao_luu`
  ADD PRIMARY KEY (`ma_sao_luu`),
  ADD KEY `nguoi_thuc_hien` (`nguoi_thuc_hien`);

--
-- Indexes for table `ton_kho`
--
ALTER TABLE `ton_kho`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `unique_sp_kho` (`ma_sp`,`ma_kho`),
  ADD KEY `ma_kho` (`ma_kho`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `chi_tiet_kiem_ke`
--
ALTER TABLE `chi_tiet_kiem_ke`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `chi_tiet_phieu_nhap`
--
ALTER TABLE `chi_tiet_phieu_nhap`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `chi_tiet_phieu_xuat`
--
ALTER TABLE `chi_tiet_phieu_xuat`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `khach_hang`
--
ALTER TABLE `khach_hang`
  MODIFY `ma_kh` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `kho`
--
ALTER TABLE `kho`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `kiem_ke`
--
ALTER TABLE `kiem_ke`
  MODIFY `ma_kiem_ke` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `loai_san_pham`
--
ALTER TABLE `loai_san_pham`
  MODIFY `ma_loai` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `nha_cung_cap`
--
ALTER TABLE `nha_cung_cap`
  MODIFY `ma_ncc` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `phieu_nhap`
--
ALTER TABLE `phieu_nhap`
  MODIFY `ma_phieu_nhap` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `phieu_xuat`
--
ALTER TABLE `phieu_xuat`
  MODIFY `ma_phieu_xuat` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `san_pham`
--
ALTER TABLE `san_pham`
  MODIFY `ma_sp` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `sao_luu`
--
ALTER TABLE `sao_luu`
  MODIFY `ma_sao_luu` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `ton_kho`
--
ALTER TABLE `ton_kho`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `chi_tiet_kiem_ke`
--
ALTER TABLE `chi_tiet_kiem_ke`
  ADD CONSTRAINT `chi_tiet_kiem_ke_ibfk_1` FOREIGN KEY (`ma_kiem_ke`) REFERENCES `kiem_ke` (`ma_kiem_ke`) ON DELETE CASCADE,
  ADD CONSTRAINT `chi_tiet_kiem_ke_ibfk_2` FOREIGN KEY (`ma_sp`) REFERENCES `san_pham` (`ma_sp`);

--
-- Constraints for table `chi_tiet_phieu_nhap`
--
ALTER TABLE `chi_tiet_phieu_nhap`
  ADD CONSTRAINT `chi_tiet_phieu_nhap_ibfk_1` FOREIGN KEY (`ma_phieu_nhap`) REFERENCES `phieu_nhap` (`ma_phieu_nhap`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `chi_tiet_phieu_nhap_ibfk_2` FOREIGN KEY (`ma_sp`) REFERENCES `san_pham` (`ma_sp`) ON UPDATE CASCADE;

--
-- Constraints for table `chi_tiet_phieu_xuat`
--
ALTER TABLE `chi_tiet_phieu_xuat`
  ADD CONSTRAINT `chi_tiet_phieu_xuat_ibfk_1` FOREIGN KEY (`ma_phieu_xuat`) REFERENCES `phieu_xuat` (`ma_phieu_xuat`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `chi_tiet_phieu_xuat_ibfk_2` FOREIGN KEY (`ma_sp`) REFERENCES `san_pham` (`ma_sp`) ON UPDATE CASCADE;

--
-- Constraints for table `kiem_ke`
--
ALTER TABLE `kiem_ke`
  ADD CONSTRAINT `kiem_ke_ibfk_2` FOREIGN KEY (`nguoi_kiem_ke`) REFERENCES `users` (`id`) ON DELETE SET NULL;

--
-- Constraints for table `phieu_nhap`
--
ALTER TABLE `phieu_nhap`
  ADD CONSTRAINT `phieu_nhap_ibfk_1` FOREIGN KEY (`nguoi_lap`) REFERENCES `users` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `phieu_nhap_ibfk_2` FOREIGN KEY (`ma_ncc`) REFERENCES `nha_cung_cap` (`ma_ncc`) ON DELETE SET NULL,
  ADD CONSTRAINT `phieu_nhap_ibfk_3` FOREIGN KEY (`ma_kho`) REFERENCES `kho` (`id`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `phieu_xuat`
--
ALTER TABLE `phieu_xuat`
  ADD CONSTRAINT `phieu_xuat_ibfk_1` FOREIGN KEY (`nguoi_lap`) REFERENCES `users` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `phieu_xuat_ibfk_2` FOREIGN KEY (`ma_kh`) REFERENCES `khach_hang` (`ma_kh`) ON DELETE SET NULL,
  ADD CONSTRAINT `phieu_xuat_ibfk_3` FOREIGN KEY (`ma_kho`) REFERENCES `kho` (`id`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `san_pham`
--
ALTER TABLE `san_pham`
  ADD CONSTRAINT `san_pham_ibfk_1` FOREIGN KEY (`ma_loai`) REFERENCES `loai_san_pham` (`ma_loai`) ON UPDATE CASCADE;

--
-- Constraints for table `sao_luu`
--
ALTER TABLE `sao_luu`
  ADD CONSTRAINT `sao_luu_ibfk_1` FOREIGN KEY (`nguoi_thuc_hien`) REFERENCES `users` (`id`) ON DELETE SET NULL;

--
-- Constraints for table `ton_kho`
--
ALTER TABLE `ton_kho`
  ADD CONSTRAINT `ton_kho_ibfk_1` FOREIGN KEY (`ma_sp`) REFERENCES `san_pham` (`ma_sp`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `ton_kho_ibfk_2` FOREIGN KEY (`ma_kho`) REFERENCES `kho` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
