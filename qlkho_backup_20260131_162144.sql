-- MariaDB dump 10.19  Distrib 10.4.32-MariaDB, for Win64 (AMD64)
--
-- Host: localhost    Database: qlkho_db
-- ------------------------------------------------------
-- Server version	10.4.32-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `qlkho_db`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `qlkho_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;

USE `qlkho_db`;

--
-- Table structure for table `chi_tiet_kiem_ke`
--

DROP TABLE IF EXISTS `chi_tiet_kiem_ke`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `chi_tiet_kiem_ke` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ma_kiem_ke` int(11) NOT NULL,
  `ma_sp` int(11) NOT NULL,
  `ton_he_thong` int(11) NOT NULL COMMENT 'Tồn kho theo hệ thống',
  `ton_thuc_te` int(11) NOT NULL COMMENT 'Tồn kho đếm thực tế',
  `chenh_lech` int(11) GENERATED ALWAYS AS (`ton_thuc_te` - `ton_he_thong`) STORED,
  `ghi_chu` text DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ma_kiem_ke` (`ma_kiem_ke`),
  KEY `ma_sp` (`ma_sp`),
  CONSTRAINT `chi_tiet_kiem_ke_ibfk_1` FOREIGN KEY (`ma_kiem_ke`) REFERENCES `kiem_ke` (`ma_kiem_ke`) ON DELETE CASCADE,
  CONSTRAINT `chi_tiet_kiem_ke_ibfk_2` FOREIGN KEY (`ma_sp`) REFERENCES `san_pham` (`ma_sp`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chi_tiet_kiem_ke`
--

LOCK TABLES `chi_tiet_kiem_ke` WRITE;
/*!40000 ALTER TABLE `chi_tiet_kiem_ke` DISABLE KEYS */;
INSERT INTO `chi_tiet_kiem_ke` VALUES (1,1,1,8,7,-1,'Thiếu 1 laptop - đã báo cáo'),(2,1,2,45,45,0,'OK'),(3,1,11,60,58,-2,'Thiếu 2 thùng mì'),(4,1,16,15,15,0,'OK');
/*!40000 ALTER TABLE `chi_tiet_kiem_ke` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chi_tiet_phieu_nhap`
--

DROP TABLE IF EXISTS `chi_tiet_phieu_nhap`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `chi_tiet_phieu_nhap` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ma_phieu_nhap` int(11) NOT NULL,
  `ma_sp` int(11) NOT NULL,
  `so_luong` int(11) NOT NULL CHECK (`so_luong` > 0),
  `don_gia` decimal(15,2) NOT NULL,
  `thanh_tien` decimal(15,2) GENERATED ALWAYS AS (`so_luong` * `don_gia`) STORED,
  PRIMARY KEY (`id`),
  KEY `ma_phieu_nhap` (`ma_phieu_nhap`),
  KEY `ma_sp` (`ma_sp`),
  CONSTRAINT `chi_tiet_phieu_nhap_ibfk_1` FOREIGN KEY (`ma_phieu_nhap`) REFERENCES `phieu_nhap` (`ma_phieu_nhap`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `chi_tiet_phieu_nhap_ibfk_2` FOREIGN KEY (`ma_sp`) REFERENCES `san_pham` (`ma_sp`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chi_tiet_phieu_nhap`
--

LOCK TABLES `chi_tiet_phieu_nhap` WRITE;
/*!40000 ALTER TABLE `chi_tiet_phieu_nhap` DISABLE KEYS */;
INSERT INTO `chi_tiet_phieu_nhap` VALUES (1,1,1,5,12000000.00,60000000.00),(2,1,2,30,80000.00,2400000.00),(3,1,3,10,650000.00,6500000.00),(4,1,4,15,450000.00,6750000.00),(5,2,6,500,2000.00,1000000.00),(6,2,7,200,8000.00,1600000.00),(7,2,8,80,15000.00,1200000.00),(8,2,9,150,75000.00,11250000.00),(9,3,11,50,80000.00,4000000.00),(10,3,12,30,95000.00,2850000.00),(11,3,13,80,25000.00,2000000.00),(12,3,14,60,55000.00,3300000.00),(13,4,16,15,450000.00,6750000.00),(14,4,17,20,280000.00,5600000.00),(15,4,18,30,120000.00,3600000.00),(16,4,19,40,85000.00,3400000.00),(17,5,26,40,85000.00,3400000.00),(18,5,27,50,65000.00,3250000.00),(19,5,29,100,18000.00,1800000.00),(20,5,30,20,120000.00,2400000.00),(21,6,1,1,500.00,500.00),(22,7,32,3,500000.00,1500000.00);
/*!40000 ALTER TABLE `chi_tiet_phieu_nhap` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chi_tiet_phieu_xuat`
--

DROP TABLE IF EXISTS `chi_tiet_phieu_xuat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `chi_tiet_phieu_xuat` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ma_phieu_xuat` int(11) NOT NULL,
  `ma_sp` int(11) NOT NULL,
  `so_luong` int(11) NOT NULL CHECK (`so_luong` > 0),
  `don_gia` decimal(15,2) NOT NULL,
  `thanh_tien` decimal(15,2) GENERATED ALWAYS AS (`so_luong` * `don_gia`) STORED,
  PRIMARY KEY (`id`),
  KEY `ma_phieu_xuat` (`ma_phieu_xuat`),
  KEY `ma_sp` (`ma_sp`),
  CONSTRAINT `chi_tiet_phieu_xuat_ibfk_1` FOREIGN KEY (`ma_phieu_xuat`) REFERENCES `phieu_xuat` (`ma_phieu_xuat`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `chi_tiet_phieu_xuat_ibfk_2` FOREIGN KEY (`ma_sp`) REFERENCES `san_pham` (`ma_sp`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chi_tiet_phieu_xuat`
--

LOCK TABLES `chi_tiet_phieu_xuat` WRITE;
/*!40000 ALTER TABLE `chi_tiet_phieu_xuat` DISABLE KEYS */;
INSERT INTO `chi_tiet_phieu_xuat` VALUES (1,1,6,200,3000.00,600000.00),(2,1,7,100,12000.00,1200000.00),(3,1,8,30,25000.00,750000.00),(4,1,9,50,95000.00,4750000.00),(5,2,1,3,15000000.00,45000000.00),(6,2,2,15,120000.00,1800000.00),(7,2,3,2,890000.00,1780000.00),(8,2,4,5,650000.00,3250000.00),(9,3,11,10,100000.00,1000000.00),(10,3,12,10,130000.00,1300000.00),(11,3,13,30,35000.00,1050000.00),(12,3,14,10,75000.00,750000.00),(13,4,3,2,890000.00,1780000.00);
/*!40000 ALTER TABLE `chi_tiet_phieu_xuat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `khach_hang`
--

DROP TABLE IF EXISTS `khach_hang`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `khach_hang` (
  `ma_kh` int(11) NOT NULL AUTO_INCREMENT,
  `ten_kh` varchar(200) NOT NULL,
  `dia_chi` text DEFAULT NULL,
  `sdt` varchar(15) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `loai_kh` enum('canhan','doanhnghiep') DEFAULT 'canhan',
  `ngay_tao` timestamp NOT NULL DEFAULT current_timestamp(),
  `trang_thai` tinyint(1) DEFAULT 1,
  PRIMARY KEY (`ma_kh`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `khach_hang`
--

LOCK TABLES `khach_hang` WRITE;
/*!40000 ALTER TABLE `khach_hang` DISABLE KEYS */;
INSERT INTO `khach_hang` VALUES (1,'Trường THPT Nguyễn Trãi','234 Trần Hưng Đạo, Q.5, TP.HCM','0281234567','thptnt@edu.vn','doanhnghiep','2026-01-30 11:42:37',1),(2,'Cửa hàng Điện Máy Xanh','567 Cách Mạng Tháng 8, Q.10, TP.HCM','0289998877','dmx@gmail.com','doanhnghiep','2026-01-30 11:42:37',1),(3,'Siêu thị Co.op Mart','890 Cộng Hòa, Tân Bình, TP.HCM','0287778899','coopmart@coop.vn','doanhnghiep','2026-01-30 11:42:37',1),(4,'Nguyễn Văn X','12 Nguyễn Văn Linh, Q.7, TP.HCM','0912345678','nguyenvanx@gmail.com','canhan','2026-01-30 11:42:37',1),(5,'Trần Thị Y','34 Lê Văn Sỹ, Q.3, TP.HCM','0987654321','tranthiy@yahoo.com','canhan','2026-01-30 11:42:37',1),(6,'Công ty TNHH ABC','56 Hoàng Diệu, Q.4, TP.HCM','0281119999','abc@company.vn','doanhnghiep','2026-01-30 11:42:37',1);
/*!40000 ALTER TABLE `khach_hang` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kho`
--

DROP TABLE IF EXISTS `kho`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `kho` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ma_kho` varchar(20) NOT NULL,
  `ten_kho` varchar(100) NOT NULL,
  `dia_chi` text DEFAULT NULL,
  `dien_tich` decimal(10,2) DEFAULT NULL,
  `nguoi_quan_ly` varchar(100) DEFAULT NULL,
  `ghi_chu` text DEFAULT NULL,
  `ngay_tao` timestamp NOT NULL DEFAULT current_timestamp(),
  `trang_thai` tinyint(1) DEFAULT 1,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ma_kho` (`ma_kho`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kho`
--

LOCK TABLES `kho` WRITE;
/*!40000 ALTER TABLE `kho` DISABLE KEYS */;
INSERT INTO `kho` VALUES (1,'KHO01','Kho chính','Bình Thủy, Cần Thơ',500.00,'Nguyễn Ngọc Trân','Kho lớn nhất, chứa hàng tồn','2026-01-30 13:53:10',1),(2,'KHO02','Kho phụ 1','Bình Minh, Vĩnh Long',150.00,'Nguyễn Ngọc Trúc Phương','Kho gần cửa hàng bán lẻ','2026-01-30 13:53:10',1),(3,'KHO03','Kho phụ 2','Ninh Kiều, Cần Thơ',120.00,'Dương Trần Phúc','Kho dự phòng','2026-01-30 13:53:10',1);
/*!40000 ALTER TABLE `kho` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kiem_ke`
--

DROP TABLE IF EXISTS `kiem_ke`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `kiem_ke` (
  `ma_kiem_ke` int(11) NOT NULL AUTO_INCREMENT,
  `so_phieu` varchar(50) NOT NULL,
  `ngay_kiem_ke` date NOT NULL,
  `ma_kho` int(11) DEFAULT NULL,
  `nguoi_kiem_ke` int(11) DEFAULT NULL,
  `trang_thai` enum('dang_kiem','hoan_thanh') DEFAULT 'dang_kiem',
  `ghi_chu` text DEFAULT NULL,
  `ngay_tao` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`ma_kiem_ke`),
  UNIQUE KEY `so_phieu` (`so_phieu`),
  KEY `ma_kho` (`ma_kho`),
  KEY `nguoi_kiem_ke` (`nguoi_kiem_ke`),
  CONSTRAINT `kiem_ke_ibfk_2` FOREIGN KEY (`nguoi_kiem_ke`) REFERENCES `users` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kiem_ke`
--

LOCK TABLES `kiem_ke` WRITE;
/*!40000 ALTER TABLE `kiem_ke` DISABLE KEYS */;
INSERT INTO `kiem_ke` VALUES (1,'KK-20260128-001','2026-01-28',1,2,'hoan_thanh','Kiểm kê định kỳ tháng 1','2026-01-30 11:42:37');
/*!40000 ALTER TABLE `kiem_ke` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `loai_san_pham`
--

DROP TABLE IF EXISTS `loai_san_pham`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `loai_san_pham` (
  `ma_loai` int(11) NOT NULL AUTO_INCREMENT,
  `ten_loai` varchar(100) NOT NULL,
  `mo_ta` text DEFAULT NULL,
  `ngay_tao` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`ma_loai`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `loai_san_pham`
--

LOCK TABLES `loai_san_pham` WRITE;
/*!40000 ALTER TABLE `loai_san_pham` DISABLE KEYS */;
INSERT INTO `loai_san_pham` VALUES (1,'Điện tử','Thiết bị điện tử, máy tính, phụ kiện','2026-01-30 10:53:15'),(2,'Văn phòng phẩm','Đồ dùng văn phòng, học tập','2026-01-30 10:53:15'),(3,'Thực phẩm','Thực phẩm đóng gói, đồ ăn nhanh','2026-01-30 10:53:15'),(4,'Đồ gia dụng','Đồ dùng nhà bếp, sinh hoạt','2026-01-30 10:53:15'),(5,'Thời trang','Quần áo, giày dép, phụ kiện','2026-01-30 10:53:15'),(6,'Sách báo','Sách giáo khoa, tham khảo, tạp chí','2026-01-30 10:53:15'),(7,'Đồ chơi','Đồ chơi trẻ em, học liệu','2026-01-30 10:53:15'),(8,'Mỹ phẩm','Chăm sóc da, trang điểm','2026-01-30 10:53:15');
/*!40000 ALTER TABLE `loai_san_pham` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nha_cung_cap`
--

DROP TABLE IF EXISTS `nha_cung_cap`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nha_cung_cap` (
  `ma_ncc` int(11) NOT NULL AUTO_INCREMENT,
  `ten_ncc` varchar(200) NOT NULL,
  `dia_chi` text DEFAULT NULL,
  `sdt` varchar(15) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `nguoi_lien_he` varchar(100) DEFAULT NULL,
  `ngay_tao` timestamp NOT NULL DEFAULT current_timestamp(),
  `trang_thai` tinyint(1) DEFAULT 1 COMMENT '1: active, 0: inactive',
  PRIMARY KEY (`ma_ncc`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nha_cung_cap`
--

LOCK TABLES `nha_cung_cap` WRITE;
/*!40000 ALTER TABLE `nha_cung_cap` DISABLE KEYS */;
INSERT INTO `nha_cung_cap` VALUES (1,'Cellphones','272 đường 30/4, Hưng Lợi, Ninh Kiều, Cần Thơ','02871009272','phongb2b@cellphones.com.vn','Nguyễn Anh Tú','2026-01-30 11:42:37',1),(2,'Văn phòng phẩm Thiên Long','39 đường 3/2, Xuân Khánh, Ninh Kiều, Cần Thơ','0287654321','sales@thienlong.vn','Trần Thị Tuyết Nhung','2026-01-30 11:42:37',1),(3,'Thực phẩm Bách Hóa Xanh','91/8B đường 30/4, Hưng Lợi, Ninh Kiều, Cần Thơ','0289876543','supplier@bachhoaxanh.com','Lê Minh Cường','2026-01-30 11:42:37',1),(4,'Điện máy Pico','321 Lý Thường Kiệt, Q.10, TP.HCM','0281112233','pico@pico.vn','Phạm Văn Dương','2026-01-30 11:42:37',1),(5,'NXB Kim Đồng','55 Quang Trung, Q.Gò Vấp, TP.HCM','0283334455','kimdong@nxb.vn','Hoàng Thị Tĩnh','2026-01-30 11:42:37',1);
/*!40000 ALTER TABLE `nha_cung_cap` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `phieu_nhap`
--

DROP TABLE IF EXISTS `phieu_nhap`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `phieu_nhap` (
  `ma_phieu_nhap` int(11) NOT NULL AUTO_INCREMENT,
  `so_phieu` varchar(50) NOT NULL,
  `ngay_nhap` date NOT NULL,
  `ma_ncc` int(11) DEFAULT NULL,
  `ma_kho` int(11) DEFAULT NULL,
  `nguoi_lap` int(11) DEFAULT NULL,
  `tong_tien` decimal(15,2) DEFAULT 0.00,
  `ghi_chu` text DEFAULT NULL,
  `ngay_tao` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`ma_phieu_nhap`),
  UNIQUE KEY `so_phieu` (`so_phieu`),
  KEY `nguoi_lap` (`nguoi_lap`),
  KEY `ma_ncc` (`ma_ncc`),
  KEY `idx_ma_kho` (`ma_kho`),
  CONSTRAINT `phieu_nhap_ibfk_1` FOREIGN KEY (`nguoi_lap`) REFERENCES `users` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `phieu_nhap_ibfk_2` FOREIGN KEY (`ma_ncc`) REFERENCES `nha_cung_cap` (`ma_ncc`) ON DELETE SET NULL,
  CONSTRAINT `phieu_nhap_ibfk_3` FOREIGN KEY (`ma_kho`) REFERENCES `kho` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `phieu_nhap`
--

LOCK TABLES `phieu_nhap` WRITE;
/*!40000 ALTER TABLE `phieu_nhap` DISABLE KEYS */;
INSERT INTO `phieu_nhap` VALUES (1,'PN-20260115-001','2026-01-15',1,NULL,2,24850000.00,'Nhập lô laptop Dell','2026-01-30 10:53:15'),(2,'PN-20260118-002','2026-01-18',2,NULL,2,15400000.00,'Nhập bút, giấy A4','2026-01-30 10:53:15'),(3,'PN-20260120-003','2026-01-20',3,NULL,3,8750000.00,'Nhập mì, nước ngọt','2026-01-30 10:53:15'),(4,'PN-20260125-004','2026-01-25',4,NULL,2,12500000.00,'Nhập đồ gia dụng','2026-01-30 10:53:15'),(5,'PN-20260128-005','2026-01-28',5,NULL,3,4850000.00,'Nhập sách, truyện','2026-01-30 10:53:15'),(6,'PN1769785483198','2026-01-30',1,NULL,1,500.00,'','2026-01-30 15:05:19'),(7,'PN1769840537396','2026-01-31',2,3,1,1500000.00,'','2026-01-31 06:22:47');
/*!40000 ALTER TABLE `phieu_nhap` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `phieu_xuat`
--

DROP TABLE IF EXISTS `phieu_xuat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `phieu_xuat` (
  `ma_phieu_xuat` int(11) NOT NULL AUTO_INCREMENT,
  `so_phieu` varchar(50) NOT NULL,
  `ngay_xuat` date NOT NULL,
  `ma_kh` int(11) DEFAULT NULL,
  `ma_kho` int(11) DEFAULT NULL,
  `nguoi_lap` int(11) DEFAULT NULL,
  `tong_tien` decimal(15,2) DEFAULT 0.00,
  `ghi_chu` text DEFAULT NULL,
  `ngay_tao` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`ma_phieu_xuat`),
  UNIQUE KEY `so_phieu` (`so_phieu`),
  KEY `nguoi_lap` (`nguoi_lap`),
  KEY `ma_kh` (`ma_kh`),
  KEY `idx_ma_kho` (`ma_kho`),
  CONSTRAINT `phieu_xuat_ibfk_1` FOREIGN KEY (`nguoi_lap`) REFERENCES `users` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `phieu_xuat_ibfk_2` FOREIGN KEY (`ma_kh`) REFERENCES `khach_hang` (`ma_kh`) ON DELETE SET NULL,
  CONSTRAINT `phieu_xuat_ibfk_3` FOREIGN KEY (`ma_kho`) REFERENCES `kho` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `phieu_xuat`
--

LOCK TABLES `phieu_xuat` WRITE;
/*!40000 ALTER TABLE `phieu_xuat` DISABLE KEYS */;
INSERT INTO `phieu_xuat` VALUES (1,'PX-20260122-001','2026-01-22',1,NULL,2,5250000.00,'Xuất văn phòng phẩm cho trường','2026-01-30 10:53:15'),(2,'PX-20260126-002','2026-01-26',2,NULL,3,18500000.00,'Xuất laptop, phụ kiện','2026-01-30 10:53:15'),(3,'PX-20260129-003','2026-01-29',3,NULL,2,3850000.00,'Xuất thực phẩm','2026-01-30 10:53:15'),(4,'PX1769840599250','2026-01-31',5,1,1,1780000.00,'','2026-01-31 06:23:35');
/*!40000 ALTER TABLE `phieu_xuat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `san_pham`
--

DROP TABLE IF EXISTS `san_pham`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `san_pham` (
  `ma_sp` int(11) NOT NULL AUTO_INCREMENT,
  `ten_sp` varchar(200) NOT NULL,
  `ma_loai` int(11) NOT NULL,
  `don_vi_tinh` varchar(20) DEFAULT 'Cái',
  `gia_nhap` decimal(15,2) DEFAULT 0.00,
  `gia_ban` decimal(15,2) DEFAULT 0.00,
  `so_luong_ton` int(11) DEFAULT 0,
  `mo_ta` text DEFAULT NULL,
  `hinh_anh` varchar(255) DEFAULT NULL,
  `ngay_tao` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`ma_sp`),
  KEY `ma_loai` (`ma_loai`),
  CONSTRAINT `san_pham_ibfk_1` FOREIGN KEY (`ma_loai`) REFERENCES `loai_san_pham` (`ma_loai`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `san_pham`
--

LOCK TABLES `san_pham` WRITE;
/*!40000 ALTER TABLE `san_pham` DISABLE KEYS */;
INSERT INTO `san_pham` VALUES (1,'Laptop Dell Vostro 3510',1,'Cái',12000000.00,15000000.00,9,'i5-1135G7, 8GB RAM, 512GB SSD',NULL,'2026-01-30 10:53:15'),(2,'Chuột Logitech M185',1,'Cái',80000.00,120000.00,45,'Chuột không dây, pin 12 tháng',NULL,'2026-01-30 10:53:15'),(3,'Bàn phím cơ Akko',1,'Cái',650000.00,890000.00,12,'Switch Blue, RGB',NULL,'2026-01-30 10:53:15'),(4,'Tai nghe Bluetooth JBL',1,'Cái',450000.00,650000.00,20,'Chống ồn, pin 30h',NULL,'2026-01-30 10:53:15'),(5,'USB Kingston 32GB',1,'Cái',85000.00,130000.00,100,'USB 3.0, tốc độ cao',NULL,'2026-01-30 10:53:15'),(6,'Bút bi Thiên Long TL-079',2,'Cây',2000.00,3000.00,500,'Mực xanh, ngòi 0.5mm',NULL,'2026-01-30 10:53:15'),(7,'Bút gel Pilot G2',2,'Cây',8000.00,12000.00,200,'Mực đen, mượt',NULL,'2026-01-30 10:53:15'),(8,'Sổ tay A5 Campus',2,'Quyển',15000.00,25000.00,80,'100 trang, kẻ ngang',NULL,'2026-01-30 10:53:15'),(9,'Giấy A4 Double A',2,'Ream',75000.00,95000.00,150,'500 tờ/ream, 80gsm',NULL,'2026-01-30 10:53:15'),(10,'Ghim Thiên Long',2,'Hộp',5000.00,8000.00,120,'Size 10, 1000 ghim/hộp',NULL,'2026-01-30 10:53:15'),(11,'Mì gói Hảo Hảo',3,'Thùng',80000.00,100000.00,60,'30 gói/thùng, vị tôm chua cay',NULL,'2026-01-30 10:53:15'),(12,'Nước ngọt Coca Cola',3,'Thùng',95000.00,130000.00,40,'24 lon/thùng, 330ml',NULL,'2026-01-30 10:53:15'),(13,'Bánh quy Cosy',3,'Hộp',25000.00,35000.00,90,'Hộp 300g, nhiều vị',NULL,'2026-01-30 10:53:15'),(14,'Cà phê G7 3in1',3,'Hộp',55000.00,75000.00,70,'21 gói/hộp',NULL,'2026-01-30 10:53:15'),(15,'Sữa Vinamilk',3,'Thùng',180000.00,240000.00,35,'48 hộp/thùng, 180ml',NULL,'2026-01-30 10:53:15'),(16,'Nồi cơm điện Sharp',4,'Cái',450000.00,650000.00,15,'1.8L, lòng chống dính',NULL,'2026-01-30 10:53:15'),(17,'Bình đun siêu tốc Philips',4,'Cái',280000.00,390000.00,25,'1.5L, tự ngắt',NULL,'2026-01-30 10:53:15'),(18,'Chảo chống dính 24cm',4,'Cái',120000.00,180000.00,40,'Đáy từ, phủ Teflon',NULL,'2026-01-30 10:53:15'),(19,'Bộ thìa nĩa inox',4,'Bộ',85000.00,130000.00,50,'6 món, cao cấp',NULL,'2026-01-30 10:53:15'),(20,'Ly thủy tinh Ocean',4,'Bộ',45000.00,70000.00,65,'Set 6 ly, 300ml',NULL,'2026-01-30 10:53:15'),(21,'Áo thun cotton nam',5,'Cái',60000.00,95000.00,100,'Size M/L/XL, nhiều màu',NULL,'2026-01-30 10:53:15'),(22,'Quần jean nữ',5,'Cái',150000.00,250000.00,45,'Chất liệu co giãn',NULL,'2026-01-30 10:53:15'),(23,'Giày thể thao Adidas',5,'Đôi',650000.00,950000.00,18,'Size 38-43, chính hãng',NULL,'2026-01-30 10:53:15'),(24,'Ba lô laptop',5,'Cái',180000.00,280000.00,30,'Chống nước, ngăn laptop 15.6\"',NULL,'2026-01-30 10:53:15'),(25,'Dây nít da nam',5,'Cái',80000.00,150000.00,55,'Da PU cao cấp',NULL,'2026-01-30 10:53:15'),(26,'Sách Lập trình Java',6,'Quyển',85000.00,125000.00,40,'Tác giả: Hoàng Đức Hải',NULL,'2026-01-30 10:53:15'),(27,'Tiếng Anh giao tiếp',6,'Quyển',65000.00,95000.00,50,'Kèm CD luyện nghe',NULL,'2026-01-30 10:53:15'),(28,'Tạp chí Forbes',6,'Quyển',45000.00,65000.00,30,'Phiên bản Việt Nam',NULL,'2026-01-30 10:53:15'),(29,'Truyện tranh Doraemon',6,'Quyển',18000.00,28000.00,120,'Tập 1-50',NULL,'2026-01-30 10:53:15'),(30,'Từ điển Anh-Việt',6,'Quyển',120000.00,180000.00,25,'150.000 từ, bìa cứng',NULL,'2026-01-30 10:53:15'),(31,'Lego City Police',7,'Hộp',450000.00,650000.00,12,'300 chi tiết, 6-12 tuổi',NULL,'2026-01-30 10:53:15'),(32,'Búp bê Barbie',7,'Cái',180000.00,280000.00,23,'Cao 29cm, kèm phụ kiện',NULL,'2026-01-30 10:53:15');
/*!40000 ALTER TABLE `san_pham` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sao_luu`
--

DROP TABLE IF EXISTS `sao_luu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sao_luu` (
  `ma_sao_luu` int(11) NOT NULL AUTO_INCREMENT,
  `ten_file` varchar(255) NOT NULL,
  `duong_dan` text NOT NULL,
  `kich_thuoc` bigint(20) DEFAULT NULL COMMENT 'Kích thước file (bytes)',
  `nguoi_thuc_hien` int(11) DEFAULT NULL,
  `loai` enum('backup','restore') DEFAULT 'backup',
  `ngay_thuc_hien` timestamp NOT NULL DEFAULT current_timestamp(),
  `ghi_chu` text DEFAULT NULL,
  PRIMARY KEY (`ma_sao_luu`),
  KEY `nguoi_thuc_hien` (`nguoi_thuc_hien`),
  CONSTRAINT `sao_luu_ibfk_1` FOREIGN KEY (`nguoi_thuc_hien`) REFERENCES `users` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sao_luu`
--

LOCK TABLES `sao_luu` WRITE;
/*!40000 ALTER TABLE `sao_luu` DISABLE KEYS */;
INSERT INTO `sao_luu` VALUES (1,'backup_20260125.sql','D:\\Backup\\backup_20260125.sql',2048576,1,'backup','2026-01-30 11:42:37','Sao lưu trước khi cập nhật hệ thống');
/*!40000 ALTER TABLE `sao_luu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ton_kho`
--

DROP TABLE IF EXISTS `ton_kho`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ton_kho` (
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
) ENGINE=InnoDB AUTO_INCREMENT=75 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ton_kho`
--

LOCK TABLES `ton_kho` WRITE;
/*!40000 ALTER TABLE `ton_kho` DISABLE KEYS */;
INSERT INTO `ton_kho` VALUES (1,1,1,9,'2026-01-31 02:20:41'),(2,2,1,45,'2026-01-31 02:20:41'),(3,3,1,10,'2026-01-31 06:23:35'),(4,4,1,20,'2026-01-31 02:20:41'),(5,5,1,100,'2026-01-31 02:20:41'),(6,6,1,500,'2026-01-31 02:20:41'),(7,7,1,200,'2026-01-31 02:20:41'),(8,8,1,80,'2026-01-31 02:20:41'),(9,9,1,150,'2026-01-31 02:20:41'),(10,10,2,120,'2026-01-31 05:13:54'),(11,11,1,60,'2026-01-31 02:20:41'),(12,12,1,40,'2026-01-31 02:20:41'),(13,13,1,90,'2026-01-31 02:20:41'),(14,14,1,70,'2026-01-31 02:20:41'),(15,15,1,35,'2026-01-31 02:20:41'),(16,16,1,15,'2026-01-31 02:20:41'),(17,17,1,25,'2026-01-31 02:20:41'),(18,18,1,40,'2026-01-31 02:20:41'),(19,19,1,50,'2026-01-31 02:20:41'),(20,20,2,65,'2026-01-31 05:13:54'),(21,21,1,100,'2026-01-31 02:20:41'),(22,22,1,45,'2026-01-31 02:20:41'),(23,23,1,18,'2026-01-31 02:20:41'),(24,24,1,30,'2026-01-31 02:20:41'),(25,25,1,55,'2026-01-31 02:20:41'),(26,26,1,40,'2026-01-31 02:20:41'),(27,27,1,50,'2026-01-31 02:20:41'),(28,28,1,30,'2026-01-31 02:20:41'),(29,29,1,120,'2026-01-31 02:20:41'),(30,30,2,25,'2026-01-31 05:13:54'),(31,31,1,12,'2026-01-31 02:20:41'),(32,32,1,20,'2026-01-31 02:20:41'),(66,8,2,85,'2026-01-31 05:13:54'),(69,9,2,25,'2026-01-31 05:13:54'),(70,18,2,25,'2026-01-31 05:13:54'),(71,19,2,25,'2026-01-31 05:13:54'),(72,28,2,25,'2026-01-31 05:13:54'),(73,29,2,25,'2026-01-31 05:13:54'),(74,32,3,3,'2026-01-31 06:22:47');
/*!40000 ALTER TABLE `ton_kho` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `ho_ten` varchar(100) NOT NULL,
  `role` enum('admin','nhanvien') DEFAULT 'nhanvien',
  `ngay_tao` timestamp NOT NULL DEFAULT current_timestamp(),
  `trang_thai` tinyint(1) DEFAULT 1 COMMENT '1: active, 0: inactive',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'admin','$2a$10$Bc5kdomyox5K1VE3UANVQe0Ho3kmxj3RelKJGM1bMbZZZE4vxS.Pq','Admin','admin','2026-01-30 10:52:31',1),(2,'nvkho01','$2a$10$7AGy2LQZap.vTKzQiUJN3.HKl5UxFmKKYxEfx6pKrkxRB2VeSURae','Lương Duy Khang','nhanvien','2026-01-30 10:52:31',1),(3,'nvkho02','$2a$10$A6vaJWicUPUYdDqE/GIiY.wxYfPkiJu1QGE3Rvuotz0KeQ1DzEdCm','Phan Minh Khôi','nhanvien','2026-01-30 10:52:31',1);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-01-31 16:21:54
