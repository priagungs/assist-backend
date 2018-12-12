-- MySQL dump 10.16  Distrib 10.1.16-MariaDB, for Win32 (AMD64)
--
-- Host: localhost    Database: peminjaman
-- ------------------------------------------------------
-- Server version	10.1.16-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `buku`
--

DROP TABLE IF EXISTS `buku`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `buku` (
  `id_buku` int(3) NOT NULL,
  `judul` varchar(50) NOT NULL,
  `penulis` varchar(50) NOT NULL,
  `tahun_terbit` int(4) NOT NULL,
  `penerbit` varchar(50) NOT NULL,
  PRIMARY KEY (`id_buku`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `buku`
--

LOCK TABLES `buku` WRITE;
/*!40000 ALTER TABLE `buku` DISABLE KEYS */;
INSERT INTO `buku` VALUES (1,'Matematika Dasar','Afrizal A. N.',2002,'PT. LEX Media'),(2,'Belajar Visual Basic','Doni Kusuma',2001,'PT. LEX Media'),(3,'Laskar Pelangi','Andrea Hirata',2006,'Bentang Pustaka'),(4,'Rumah Tanpa Jendela','Asma Nadia',2007,'Media Indonesia'),(5,'Manusia Setengah Salmon','Raditya Dika',2010,'Alenia Press'),(6,'Perahu Kertas','Dewi Lestari',2003,'Grasindo'),(7,'Emak Ingin Naik Haji','Asma Nadia',2007,'Aditya Media'),(8,'Sang Pemimpi','Andrea Hirata',2008,'Bentang Pustaka'),(9,'Manajemen Basis Data','Doni Kusuma',2004,'Gramedia'),(10,'Radikus Makankakus','Raditya Dika',2003,'Media Indonesia'),(11,'Kambing Jantan','Raditya Dika',2008,'Gramedia'),(12,'Supernova : Akar','Dewi Lestari',2000,'Grasindo'),(13,'Sebelas Patriot','Andrea Hirata',1997,'Bentang Pustaka'),(14,'Android Application Development','Doni Kusuma',2000,'Grasindo'),(15,'Cinta Brontosaurus','Raditya Dika',2009,'Alenia Press'),(16,'Kalkulus II','Afrizal A. N.',2002,'Aditya Media'),(17,'Edensor','Andrea Hirata',2003,'Bentang Pustaka'),(18,'Marmut Merah Jambu','Raditya Dika',1998,'Grasindo'),(19,'Supernova : Partikel','Dewi Lestari',2009,'Aditya Media'),(20,'Kalkulus III','Afrizal A. N.',2010,'Gramedia');
/*!40000 ALTER TABLE `buku` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mahasiswa`
--

DROP TABLE IF EXISTS `mahasiswa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mahasiswa` (
  `nim` int(8) NOT NULL,
  `nama` varchar(30) DEFAULT NULL,
  `jurusan` varchar(30) DEFAULT NULL,
  `alamat` varchar(50) DEFAULT NULL,
  `email` varchar(30) DEFAULT NULL,
  `no_telepon` varchar(13) DEFAULT NULL,
  PRIMARY KEY (`nim`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mahasiswa`
--

LOCK TABLES `mahasiswa` WRITE;
/*!40000 ALTER TABLE `mahasiswa` DISABLE KEYS */;
INSERT INTO `mahasiswa` VALUES (13516121,'Nicholas Wijaya','Teknik Informatika','Jl. Hayam Wuruk No. 96, Jakarta Pusat','nicholas_wijaya@gmail.com','089987109127'),(13516030,'Yuly Haruka','Teknik Informatika','Jl. Kembangan No. 223, Jakarta Barat','yuly_haruka@yahoo.com','081818236512'),(13516029,'Shinta Ayu CK','Teknik Informatika','Jl. Wolter Monginsidi No. 2, Jakarta Selatan','shinta_ayu@gmail.com','081727365421'),(13516050,'Dicky Adrian','Teknik Informatika','Jl. Pademangan No. 77, Jakarta Utara','amirasyarif@yahoo.com','081312987564'),(13211058,'Emil Hakim','Teknik Elektro','Jl. Cipucang No. 43, Jakarta Utara','emilhakim@yahoo.com','081865389765'),(13211076,'Andhika Prasetya','Teknik Elektro','Jl. Pajajaran No. 65, Bogor','andhikaprasetya@yahoo.com','081769871243'),(15411027,'David Santoso','Ilmu Hukum','Jl. Diponegoro No. 165, Bandung','davidsantoso@gmail.com','081218182736'),(15411049,'Yusuf Wicaksono','Ilmu Hukum','Jl. Cibeber No. 23, Jakarta Selatan','yusufwicaksono@gmail.com','081625431432'),(15511012,'Okta Wahyu','Ilmu Ekonomi','Jl. Dharmawangsa No. 11, Jakarta Selatan','oktawahyu@yahoo.com','081726317273'),(15511026,'Hanun Wisnu','Ilmu Ekonomi','Jl. Otto Iskandardinata No. 201, Bogor','hanunwisnu@gmail.com','081726172889'),(15610002,'Afif Ramadhan','Ilmu Politik','Jl. Veteran No. 282, Jakarta Pusat','afiframadhan@yahoo.com','08152632716'),(15611004,'Lintang Putra','Ilmu Politik','Jl. Setiabudi No. 54, Bandung','lintangputra@yahoo.com','081726543142'),(15710043,'Antonius Budi','Ilmu Komunikasi','Jl. Gempol Wetan No. 132, Bandung','antoniusbudi@gmail.com','087812654876'),(18010022,'Agatha Dyah','Teknik Tenaga Listrik','Jl. Kesehatan No. 23, Bogor','agathadyah@yahoo.com','081124316254'),(18010028,'Karina Savitri','Teknik Tenaga Listrik','Jl. Dahlia No. 187, Bekasi','karinasavitri@gmail.com','081927654211'),(18011033,'Muhammad Iqbal','Teknik Tenaga Listrik','Jl. Sukajadi No. 11, Bandung','muhammad.iqbal@gmail.com','08162690978'),(18011041,'Dhimas Ali','Teknik Tenaga Listrik','Jl. Trunojoyo No. 81, Bandung','dhimas.ali@gmail.com','08182815634'),(18110011,'Laily Annisa','Teknik Telekomunikasi','Jl. Buah Batu No. 39, Bandung','lailyannisa@yahoo.com','081726321625'),(18110052,'Renaissa Mutiara','Teknik Telekomunikasi','Jl. Merpati No. 65, Bekasi','renaissamutiara@gmail.com','087811762543'),(18111040,'Tsania Alma','Teknik Telekomunikasi','Jl. Soekarno Hatta No. 81, Bandung','tsaniaalma@gmail.com','089817362123');
/*!40000 ALTER TABLE `mahasiswa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `meminjam`
--

DROP TABLE IF EXISTS `meminjam`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `meminjam` (
  `nim` int(8) NOT NULL,
  `id_buku` int(3) NOT NULL,
  `tanggal_pinjam` date NOT NULL,
  `tanggal_pengembalian` date NOT NULL,
  KEY `nim` (`nim`),
  KEY `id_buku` (`id_buku`),
  CONSTRAINT `meminjam_ibfk_1` FOREIGN KEY (`nim`) REFERENCES `mahasiswa` (`nim`),
  CONSTRAINT `meminjam_ibfk_2` FOREIGN KEY (`id_buku`) REFERENCES `buku` (`id_buku`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meminjam`
--

LOCK TABLES `meminjam` WRITE;
/*!40000 ALTER TABLE `meminjam` DISABLE KEYS */;
INSERT INTO `meminjam` VALUES (13516121,19,'2017-01-02','2017-01-15'),(18110011,4,'2017-07-06','2017-07-10'),(18010028,20,'2017-03-10','2017-07-28'),(18011033,15,'2017-03-07','2017-03-12'),(13511076,3,'2017-03-23','2017-03-30'),(18011041,10,'2017-10-10','2017-10-22'),(13516050,18,'2017-01-26','2017-01-30'),(13516050,16,'2017-10-12','2017-10-25'),(15611004,8,'2017-08-27','2017-09-10'),(18010028,3,'2017-04-05','2017-04-10'),(13516121,14,'2017-06-28','2017-07-05'),(15411049,12,'2017-05-25','2017-06-09'),(18011033,1,'2017-05-14','2017-05-20'),(13516029,9,'2017-10-05','2017-10-16'),(13516121,9,'2017-04-27','2017-05-03'),(15611004,2,'2017-07-07','2017-07-20'),(15511026,6,'2017-07-19','2017-07-25'),(15511012,10,'2017-01-24','2017-02-05'),(13516030,15,'2017-03-21','2017-03-27'),(13211058,1,'2017-06-01','2017-06-21'),(18110011,15,'2017-08-10','2017-08-15'),(15611004,10,'2017-02-07','2017-02-14'),(18010022,14,'2017-09-03','2017-09-22'),(18111040,18,'2017-07-08','2017-07-22'),(15411027,14,'2017-06-06','2017-06-12'),(18010028,2,'2017-06-05','2017-06-12'),(15710043,14,'2017-08-27','2017-09-10'),(18011041,2,'2017-03-14','2017-03-20'),(15610002,16,'2017-10-15','2017-10-20'),(13516050,3,'2017-05-09','2017-05-21'),(18011041,5,'2017-08-18','2017-08-27'),(15710043,5,'2017-10-30','2017-11-09'),(15411027,10,'2017-07-23','2017-07-27'),(18011033,2,'2017-07-06','2017-07-18'),(15611004,12,'2017-01-14','2017-01-25'),(18011033,3,'2017-10-08','2017-10-15'),(13516121,20,'2017-09-06','2017-09-14'),(15411049,16,'2017-10-23','2017-11-07'),(13516050,5,'2017-04-05','2017-04-12'),(18110011,19,'2017-05-06','2017-05-13'),(18010028,3,'2017-02-02','2017-02-14'),(18110052,12,'2017-07-10','2017-07-17'),(18011033,1,'2017-04-05','2017-04-21'),(18011041,14,'2017-05-24','2017-06-03'),(18110011,16,'2017-04-21','2017-04-27'),(13516050,9,'2017-06-03','2017-06-14'),(15411027,6,'2017-08-27','2017-09-06'),(15611004,4,'2017-03-27','2017-04-09'),(18011033,10,'2017-08-16','2017-08-30'),(13516121,3,'2017-01-23','2017-01-28');
/*!40000 ALTER TABLE `meminjam` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-10-31  6:04:44
