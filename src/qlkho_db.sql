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
-- Table structure for table `chi_tiet_kiem_ke`
--

DROP TABLE IF EXISTS `chi_tiet_kiem_ke`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `chi_tiet_kiem_ke` (
  `ma_kiem_ke` int(11) NOT NULL,
  `ma_sp` int(11) NOT NULL,
  `ton_he_thong` int(11) NOT NULL COMMENT 'T???n kho theo h??? th???ng',
  `ton_thuc_te` int(11) NOT NULL COMMENT 'T???n kho ?????m th???c t???',
  `chenh_lech` int(11) GENERATED ALWAYS AS (`ton_thuc_te` - `ton_he_thong`) STORED,
  `ghi_chu` text DEFAULT NULL,
  PRIMARY KEY (`ma_kiem_ke`,`ma_sp`),
  KEY `ma_kiem_ke` (`ma_kiem_ke`),
  KEY `ma_sp` (`ma_sp`),
  CONSTRAINT `chi_tiet_kiem_ke_ibfk_1` FOREIGN KEY (`ma_kiem_ke`) REFERENCES `kiem_ke` (`ma_kiem_ke`) ON DELETE CASCADE,
  CONSTRAINT `chi_tiet_kiem_ke_ibfk_2` FOREIGN KEY (`ma_sp`) REFERENCES `san_pham` (`ma_sp`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chi_tiet_kiem_ke`
--

LOCK TABLES `chi_tiet_kiem_ke` WRITE;
/*!40000 ALTER TABLE `chi_tiet_kiem_ke` DISABLE KEYS */;
/*!40000 ALTER TABLE `chi_tiet_kiem_ke` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chi_tiet_phieu_nhap`
--

DROP TABLE IF EXISTS `chi_tiet_phieu_nhap`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `chi_tiet_phieu_nhap` (
  `ma_phieu_nhap` int(11) NOT NULL,
  `ma_sp` int(11) NOT NULL,
  `so_luong` int(11) NOT NULL CHECK (`so_luong` > 0),
  `don_gia` decimal(15,2) NOT NULL,
  `thanh_tien` decimal(15,2) GENERATED ALWAYS AS (`so_luong` * `don_gia`) STORED,
  PRIMARY KEY (`ma_phieu_nhap`,`ma_sp`),
  KEY `ma_phieu_nhap` (`ma_phieu_nhap`),
  KEY `ma_sp` (`ma_sp`),
  CONSTRAINT `chi_tiet_phieu_nhap_ibfk_1` FOREIGN KEY (`ma_phieu_nhap`) REFERENCES `phieu_nhap` (`ma_phieu_nhap`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `chi_tiet_phieu_nhap_ibfk_2` FOREIGN KEY (`ma_sp`) REFERENCES `san_pham` (`ma_sp`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chi_tiet_phieu_nhap`
--

LOCK TABLES `chi_tiet_phieu_nhap` WRITE;
/*!40000 ALTER TABLE `chi_tiet_phieu_nhap` DISABLE KEYS */;
INSERT INTO `chi_tiet_phieu_nhap` VALUES (1,1,2,12000000.00,24000000.00),(1,2,20,80000.00,1600000.00),(1,3,1,1500000.00,1500000.00),(2,4,50,15000.00,750000.00),(2,5,10,80000.00,800000.00),(2,6,30,25000.00,750000.00),(3,7,10,100000.00,1000000.00),(3,8,10,150000.00,1500000.00),(3,9,10,50000.00,500000.00),(4,10,2,800000.00,1600000.00),(4,11,5,200000.00,1000000.00),(4,12,2,350000.00,700000.00),(5,13,20,80000.00,1600000.00),(5,14,10,200000.00,2000000.00),(5,15,4,300000.00,1200000.00);
/*!40000 ALTER TABLE `chi_tiet_phieu_nhap` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chi_tiet_phieu_xuat`
--

DROP TABLE IF EXISTS `chi_tiet_phieu_xuat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `chi_tiet_phieu_xuat` (
  `ma_phieu_xuat` int(11) NOT NULL,
  `ma_sp` int(11) NOT NULL,
  `so_luong` int(11) NOT NULL CHECK (`so_luong` > 0),
  `don_gia` decimal(15,2) NOT NULL,
  `thanh_tien` decimal(15,2) GENERATED ALWAYS AS (`so_luong` * `don_gia`) STORED,
  PRIMARY KEY (`ma_phieu_xuat`,`ma_sp`),
  KEY `ma_phieu_xuat` (`ma_phieu_xuat`),
  KEY `ma_sp` (`ma_sp`),
  CONSTRAINT `chi_tiet_phieu_xuat_ibfk_1` FOREIGN KEY (`ma_phieu_xuat`) REFERENCES `phieu_xuat` (`ma_phieu_xuat`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `chi_tiet_phieu_xuat_ibfk_2` FOREIGN KEY (`ma_sp`) REFERENCES `san_pham` (`ma_sp`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chi_tiet_phieu_xuat`
--

LOCK TABLES `chi_tiet_phieu_xuat` WRITE;
/*!40000 ALTER TABLE `chi_tiet_phieu_xuat` DISABLE KEYS */;
INSERT INTO `chi_tiet_phieu_xuat` VALUES (1,1,1,15000000.00,15000000.00),(1,2,1,120000.00,120000.00),(2,7,2,120000.00,240000.00),(2,8,1,180000.00,180000.00),(3,13,5,150000.00,750000.00),(3,14,1,350000.00,350000.00);
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
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `khach_hang`
--

LOCK TABLES `khach_hang` WRITE;
/*!40000 ALTER TABLE `khach_hang` DISABLE KEYS */;
INSERT INTO `khach_hang` VALUES (1,'Nguyễn Văn An','123 Lý Thường Kiệt, Quận 10, TP.HCM','0909111222','nguyenvanan@gmail.com','canhan','2026-02-07 16:44:09',1),(2,'Công ty TNHH ABC','456 Điện Biên Phủ, Quận Bình Thạnh, TP.HCM','0909222333','contact@abc.vn','doanhnghiep','2026-02-07 16:44:09',1),(3,'Trần Thị Bình','789 Cách Mạng Tháng 8, Quận 3, TP.HCM','0909333444','tranbinh@yahoo.com','canhan','2026-02-07 16:44:09',1),(4,'Công ty CP XYZ','321 Lê Văn Sỹ, Quận Phú Nhuận, TP.HCM','0909444555','info@xyz.vn','doanhnghiep','2026-02-07 16:44:09',1),(5,'Lê Văn Cường','654 Hoàng Văn Thụ, Quận Tân Bình, TP.HCM','0909555666','cuongle@hotmail.com','canhan','2026-02-07 16:44:09',1);
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
INSERT INTO `kho` VALUES (1,'KHO-A','Kho Tổng','123 Đường Nguyễn Văn Linh, Quận 7, TP.HCM',500.00,'Nguyễn Văn A','Kho chính, lưu trữ hàng hóa chính','2026-02-07 16:44:09',1),(2,'KHO-B','Kho Phụ 1','456 Đường Lê Văn Việt, Quận 9, TP.HCM',300.00,'Trần Thị B','Kho phụ, lưu trữ hàng dự phòng','2026-02-07 16:44:09',1),(3,'KHO-C','Kho Phụ 2','789 Đường Võ Văn Ngân, Thủ Đức, TP.HCM',200.00,'Lê Văn C','Kho phụ, lưu trữ hàng xuất khẩu','2026-02-07 16:44:09',1);
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kiem_ke`
--

LOCK TABLES `kiem_ke` WRITE;
/*!40000 ALTER TABLE `kiem_ke` DISABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `loai_san_pham`
--

LOCK TABLES `loai_san_pham` WRITE;
/*!40000 ALTER TABLE `loai_san_pham` DISABLE KEYS */;
INSERT INTO `loai_san_pham` VALUES (1,'Điện tử','Các sản phẩm điện tử, thiết bị công nghệ','2026-02-07 16:44:09'),(2,'Văn phòng phẩm','Dụng cụ văn phòng, học tập','2026-02-07 16:44:09'),(3,'Thực phẩm','Thực phẩm khô, đồ uống','2026-02-07 16:44:09'),(4,'Gia dụng','Đồ gia dụng, nội thất','2026-02-07 16:44:09'),(5,'Thời trang','Quần áo, phụ kiện thời trang','2026-02-07 16:44:09');
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
INSERT INTO `nha_cung_cap` VALUES (1,'Công ty TNHH Điện Tử Việt','123 Lê Lợi, Quận 1, TP.HCM','0901234567','contact@dientu.vn','Nguyễn Văn A','2026-02-07 16:44:09',1),(2,'Công ty CP Văn Phòng Phẩm Hà Nội','456 Trần Hưng Đạo, Hà Nội','0912345678','info@vanphongpham.vn','Trần Thị B','2026-02-07 16:44:09',1),(3,'Công ty TNHH Thực Phẩm An Toàn','789 Nguyễn Huệ, Quận 1, TP.HCM','0923456789','sales@thucpham.vn','Lê Văn C','2026-02-07 16:44:09',1),(4,'Công ty CP Gia Dụng Miền Nam','321 Võ Văn Tần, Quận 3, TP.HCM','0934567890','contact@giadung.vn','Phạm Thị D','2026-02-07 16:44:09',1),(5,'Công ty TNHH Thời Trang Trẻ','654 Nguyễn Trãi, Quận 5, TP.HCM','0945678901','info@thoitrang.vn','Hoàng Văn E','2026-02-07 16:44:09',1);
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
  `trang_thai` varchar(20) DEFAULT 'hoan_thanh',
  PRIMARY KEY (`ma_phieu_nhap`),
  UNIQUE KEY `so_phieu` (`so_phieu`),
  KEY `nguoi_lap` (`nguoi_lap`),
  KEY `ma_ncc` (`ma_ncc`),
  KEY `idx_ma_kho` (`ma_kho`),
  CONSTRAINT `phieu_nhap_ibfk_1` FOREIGN KEY (`nguoi_lap`) REFERENCES `users` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `phieu_nhap_ibfk_2` FOREIGN KEY (`ma_ncc`) REFERENCES `nha_cung_cap` (`ma_ncc`) ON DELETE SET NULL,
  CONSTRAINT `phieu_nhap_ibfk_3` FOREIGN KEY (`ma_kho`) REFERENCES `kho` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `phieu_nhap`
--

LOCK TABLES `phieu_nhap` WRITE;
/*!40000 ALTER TABLE `phieu_nhap` DISABLE KEYS */;
INSERT INTO `phieu_nhap` VALUES (1,'PN001','2026-01-15',1,1,1,24160000.00,'Nhập hàng điện tử đợt 1','2026-02-07 16:44:09','hoan_thanh'),(2,'PN002','2026-01-20',2,1,1,2120000.00,'Nhập văn phòng phẩm','2026-02-07 16:44:09','hoan_thanh'),(3,'PN003','2026-01-25',3,2,1,3000000.00,'Nhập thực phẩm','2026-02-07 16:44:09','hoan_thanh'),(4,'PN004','2026-02-01',4,2,1,2700000.00,'Nhập gia dụng','2026-02-07 16:44:09','hoan_thanh'),(5,'PN005','2026-02-05',5,3,1,2880000.00,'Nhập thời trang','2026-02-07 16:44:09','hoan_thanh');
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
  `trang_thai` varchar(20) DEFAULT 'hoan_thanh',
  PRIMARY KEY (`ma_phieu_xuat`),
  UNIQUE KEY `so_phieu` (`so_phieu`),
  KEY `nguoi_lap` (`nguoi_lap`),
  KEY `ma_kh` (`ma_kh`),
  KEY `idx_ma_kho` (`ma_kho`),
  CONSTRAINT `phieu_xuat_ibfk_1` FOREIGN KEY (`nguoi_lap`) REFERENCES `users` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `phieu_xuat_ibfk_2` FOREIGN KEY (`ma_kh`) REFERENCES `khach_hang` (`ma_kh`) ON DELETE SET NULL,
  CONSTRAINT `phieu_xuat_ibfk_3` FOREIGN KEY (`ma_kho`) REFERENCES `kho` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `phieu_xuat`
--

LOCK TABLES `phieu_xuat` WRITE;
/*!40000 ALTER TABLE `phieu_xuat` DISABLE KEYS */;
INSERT INTO `phieu_xuat` VALUES (1,'PX001','2026-02-06',1,1,1,15120000.00,'Xuất hàng cho khách lẻ','2026-02-07 16:44:09','hoan_thanh'),(2,'PX002','2026-02-07',2,2,1,360000.00,'Xuất hàng cho công ty ABC','2026-02-07 16:44:09','hoan_thanh'),(3,'PX003','2026-02-07',3,3,1,1050000.00,'Xuất hàng thời trang','2026-02-07 16:44:09','hoan_thanh');
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
  `don_vi_tinh` varchar(20) DEFAULT 'C??i',
  `gia_nhap` decimal(15,2) DEFAULT 0.00,
  `gia_ban` decimal(15,2) DEFAULT 0.00,
  `so_luong_ton` int(11) DEFAULT 0,
  `mo_ta` text DEFAULT NULL,
  `hinh_anh` varchar(255) DEFAULT NULL,
  `ngay_tao` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`ma_sp`),
  KEY `ma_loai` (`ma_loai`),
  CONSTRAINT `san_pham_ibfk_1` FOREIGN KEY (`ma_loai`) REFERENCES `loai_san_pham` (`ma_loai`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `san_pham`
--

LOCK TABLES `san_pham` WRITE;
/*!40000 ALTER TABLE `san_pham` DISABLE KEYS */;
INSERT INTO `san_pham` VALUES (1,'Laptop Dell Inspiron 15',1,'Cái',12000000.00,15000000.00,1,'Laptop Dell Inspiron 15, RAM 8GB, SSD 256GB',NULL,'2026-02-07 16:44:09'),(2,'Chuột Logitech M185',1,'Cái',80000.00,120000.00,19,'Chuột không dây Logitech M185',NULL,'2026-02-07 16:44:09'),(3,'Bàn phím cơ Keychron K2',1,'Cái',1500000.00,2000000.00,1,'Bàn phím cơ Keychron K2, RGB',NULL,'2026-02-07 16:44:09'),(4,'Bút bi Thiên Long',2,'Hộp',15000.00,25000.00,50,'Hộp 10 cây bút bi Thiên Long',NULL,'2026-02-07 16:44:09'),(5,'Giấy A4 Double A',2,'Ream',80000.00,100000.00,10,'Giấy A4 Double A 70gsm, 500 tờ',NULL,'2026-02-07 16:44:09'),(6,'Sổ tay Bìa Cứng A5',2,'Quyển',25000.00,40000.00,30,'Sổ tay bìa cứng A5, 200 trang',NULL,'2026-02-07 16:44:09'),(7,'Mì Hảo Hảo',3,'Thùng',100000.00,120000.00,8,'Thùng 30 gói mì Hảo Hảo',NULL,'2026-02-07 16:44:09'),(8,'Nước ngọt Coca Cola',3,'Thùng',150000.00,180000.00,9,'Thùng 24 lon Coca Cola 330ml',NULL,'2026-02-07 16:44:09'),(9,'Cà phê Trung Nguyên',3,'Hộp',50000.00,70000.00,10,'Hộp cà phê Trung Nguyên G7 3in1',NULL,'2026-02-07 16:44:09'),(10,'Nồi cơm điện Sharp',4,'Cái',800000.00,1000000.00,2,'Nồi cơm điện Sharp 1.8L',NULL,'2026-02-07 16:44:09'),(11,'Bình đun siêu tốc',4,'Cái',200000.00,300000.00,5,'Bình đun siêu tốc 1.7L',NULL,'2026-02-07 16:44:09'),(12,'Quạt điện Senko',4,'Cái',350000.00,500000.00,2,'Quạt điện Senko 16 inch',NULL,'2026-02-07 16:44:09'),(13,'Áo thun nam',5,'Cái',80000.00,150000.00,15,'Áo thun nam cotton 100%',NULL,'2026-02-07 16:44:09'),(14,'Quần jean nữ',5,'Cái',200000.00,350000.00,9,'Quần jean nữ skinny',NULL,'2026-02-07 16:44:09'),(15,'Giày thể thao',5,'Đôi',300000.00,500000.00,4,'Giày thể thao unisex',NULL,'2026-02-07 16:44:09');
/*!40000 ALTER TABLE `san_pham` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ton_kho`
--

DROP TABLE IF EXISTS `ton_kho`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ton_kho` (
  `ma_sp` int(11) NOT NULL,
  `ma_kho` int(11) NOT NULL,
  `so_luong_ton` int(11) DEFAULT 0,
  `ngay_cap_nhat` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  UNIQUE KEY `unique_sp_kho` (`ma_sp`,`ma_kho`),
  KEY `ma_kho` (`ma_kho`),
  CONSTRAINT `ton_kho_ibfk_1` FOREIGN KEY (`ma_sp`) REFERENCES `san_pham` (`ma_sp`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ton_kho_ibfk_2` FOREIGN KEY (`ma_kho`) REFERENCES `kho` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ton_kho`
--

LOCK TABLES `ton_kho` WRITE;
/*!40000 ALTER TABLE `ton_kho` DISABLE KEYS */;
INSERT INTO `ton_kho` VALUES (1,1,1,'2026-02-07 16:44:09'),(2,1,19,'2026-02-07 16:44:09'),(3,1,1,'2026-02-07 16:44:09'),(4,1,50,'2026-02-07 16:44:09'),(5,1,10,'2026-02-07 16:44:09'),(6,1,30,'2026-02-07 16:44:09'),(7,2,8,'2026-02-07 16:44:09'),(8,2,9,'2026-02-07 16:44:09'),(9,2,10,'2026-02-07 16:44:09'),(10,2,2,'2026-02-07 16:44:09'),(11,2,5,'2026-02-07 16:44:09'),(12,2,2,'2026-02-07 16:44:09'),(13,3,15,'2026-02-07 16:44:09'),(14,3,9,'2026-02-07 16:44:09'),(15,3,4,'2026-02-07 16:44:09');
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
INSERT INTO `users` VALUES (1,'admin','$2a$10$Bc5kdomyox5K1VE3UANVQe0Ho3kmxj3RelKJGM1bMbZZZE4vxS.Pq','Admin','admin','2026-01-30 10:52:31',1),(2,'nvkho01','$2a$10$7AGy2LQZap.vTKzQiUJN3.HKl5UxFmKKYxEfx6pKrkxRB2VeSURae','L????ng Duy Khang','nhanvien','2026-01-30 10:52:31',1),(3,'nvkho02','$2a$10$A6vaJWicUPUYdDqE/GIiY.wxYfPkiJu1QGE3Rvuotz0KeQ1DzEdCm','Phan Minh Kh??i','nhanvien','2026-01-30 10:52:31',1);
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

-- Dump completed on 2026-02-08 18:49:55
