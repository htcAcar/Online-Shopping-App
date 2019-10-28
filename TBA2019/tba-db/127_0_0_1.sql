-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 07, 2019 at 10:32 AM
-- Server version: 10.1.39-MariaDB
-- PHP Version: 7.1.29

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `tba-db`
--
CREATE DATABASE IF NOT EXISTS `tba-db` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `tba-db`;

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE `admin` (
  `admin_ID` int(11) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `books`
--

CREATE TABLE `books` (
  `product_ID` int(11) NOT NULL,
  `name` text NOT NULL,
  `author` text NOT NULL,
  `publisher` text NOT NULL,
  `description` text NOT NULL,
  `type` text NOT NULL,
  `price` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `books`
--

INSERT INTO `books` (`product_ID`, `name`, `author`, `publisher`, `description`, `type`, `price`) VALUES
(1, 'Notre Dame\'?n Kamburu', 'Victor Hugo', 'Butik', 'Bebekken terk edilen ve Notre Dame katedralinde yeti?tirilen kambur Quasimodo, hayat?n? toplumdan d??lanarak sürdürür.\r\nGüzel bir çingene k?z?n, Esmeralda\'n?n geli?i, k?skançl??a, ihanete ve cinayete sürükleyen bir dizi trajik olay?n ba?lang?c? olur.', 'Roman', 30),
(2, 'Melekler ve ?eytanlar', 'Dan Brown', 'Alt?n Kitaplar', 'Harvard Üniversitesi Simgebilim Profesörü Robert Langdon efsanevi gizli örgüt Illuminati\'nin -Galileo zaman?ndan beri Katolik Kilisesi\'nin ba?naz inançlar?n? lanetleyerek bilimin yararlar?n? yücelten- hala faaliyette olup cinayetler i?ledi?ini ö?renince ?ok geçirir. Parlak bir fizikçi olan Leonarda Vetra cinayete kurban gitmi?tir. Tek gözü oyulmu? ve gö?sü örgütün sembolüyle da?lanm??t?r. Bilim adam?n?n son bulu?u güçlü ve çok tehlikeli enerji kayna?? kar??madde çal?nm?? ve yeni Papa seçiminin gerçekle?ece?i gün Vatikan ?ehri\'nin alt?na saklanm??t?r. Langdon, Vetra\'n?n meslekta?? ve ayn? zamanda k?z? olan Vittoria ile medeniyeti yok olmaktan kurtarmak amac?yla Roma sokaklar?nda, kiliselerde ve katakomplarda soluk solu?a ko?u?turarak 400 y?ll?k izi sürerek Illuminati\'nin izini bulmaya çal???rlar.', 'roman', 40),
(3, 'OD', '?skender Pala', 'Kap? Yay?nlar?', 'Her yazd??? romanla yüz binlerin kalbini feth eden ?skender Pala yeni roman? ‘OD’ ile yeniden okurlar?n? selaml?yor. Od bir Yunus Emre roman?. Gök kubbemizin her zaman parlayan ve hep çok sevilen, ?iirleri gönülden gönüle dolup dilden dile dola?an Yunus Emre, bu kez OD’un ana kahraman?. ?skender Pala’n?n ilim ve kültür adam? olmas?n?n yan?nda, yazar ki?ili?inin imbi?inden geçirilerek a?k?n taht?na bir kez daha oturtuluyor. 13. yüzy?l?n her bak?mdan kavruk ve yan?p y?k?lan ortam?na Yunus Emre’nin geli?i tarihi atmosfer içerisinde hakiki anlam?na kavu?turuluyor. Y?k?nt?lar ve yang?nlar içinden bir gönül ve bir insanl?k an?t?n?n in?a edili?i cümle cümle anlat?yor ve elbette kalbe dokuna dokuna yol al?yor. Roman?n her sayfas?nda Yunus’un haml?ktan safl??a geçi?i okunuyor.', 'Roman', 20),
(4, 'Saatleri Ayarlama Enstitüsü', 'Ahmet Hamdi Tanp?nar', 'Dergah Yay?nlar?', 'Ahmet Hamdi Tanp?nar\'?n ?iiri sembolist bir ifade üzerine kurulmu?tur. Ayn? anlat?m tarz? romanlar?na da zaman zaman sirayet eder. \"Saatleri Ayarlama Ensitüsü\" toplumumuzun bu de?i?me süreci içindeki durumunu, fertten yola ç?karak topluma varan bir teknikle anlat?yor.\r\n', 'Roman', 27);

-- --------------------------------------------------------

--
-- Table structure for table `carts`
--

CREATE TABLE `carts` (
  `product_ID` int(11) NOT NULL,
  `customer_ID` int(11) NOT NULL,
  `category` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `customers`
--

CREATE TABLE `customers` (
  `customer_ID` int(11) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `dob` date NOT NULL,
  `address` text NOT NULL,
  `isLogged` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `customers`
--

INSERT INTO `customers` (`customer_ID`, `email`, `password`, `dob`, `address`, `isLogged`) VALUES
(1, 'aa@aa.com', 'aaaaaa11', '0000-00-00', 'Aaa', 0),
(2, 'yeni@gmail.com', 'aaasss11', '0000-00-00', 'hsshhdjejs', 0);

-- --------------------------------------------------------

--
-- Table structure for table `headphone`
--

CREATE TABLE `headphone` (
  `product_ID` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `color` text NOT NULL,
  `brand` text NOT NULL,
  `cable_length` text NOT NULL,
  `price` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `headphone`
--

INSERT INTO `headphone` (`product_ID`, `name`, `color`, `brand`, `cable_length`, `price`) VALUES
(13, 'Sony MDR-ZX110', 'black', 'sony', '125cm', 50),
(14, 'Philips SHL5000', 'white', 'Philips', '120cm', 60),
(15, 'Philips SHL3075BL', 'pink', 'Philips', '130', 70),
(16, 'Sony MDR-ZX310', 'green', 'sony', '120cm', 56),
(17, 'xxx', 'xx', '1', '150', 100);

-- --------------------------------------------------------

--
-- Table structure for table `laptop`
--

CREATE TABLE `laptop` (
  `product_ID` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `color` varchar(15) NOT NULL,
  `screenSize` double NOT NULL,
  `weight` double NOT NULL,
  `brand` varchar(15) NOT NULL,
  `memory` varchar(15) NOT NULL,
  `price` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `laptop`
--

INSERT INTO `laptop` (`product_ID`, `name`, `color`, `screenSize`, `weight`, `brand`, `memory`, `price`) VALUES
(1, '', 'black', 15.7, 2.13, 'Asus', '128GB SSD+8GB R', 0),
(2, '', 'grey', 17.1, 3, 'Lenova', '64GB SSD+8GB RA', 0),
(3, '', 'black', 15.5, 1.9, 'HP', '128GB SSD+16GB ', 0),
(4, '', 'black', 15.5, 2, 'Dell', '64GB SSD+16GB R', 0);

-- --------------------------------------------------------

--
-- Table structure for table `phone`
--

CREATE TABLE `phone` (
  `product_ID` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `color` varchar(15) NOT NULL,
  `screen_size` varchar(100) NOT NULL,
  `brand` varchar(15) NOT NULL,
  `memory` varchar(10) NOT NULL,
  `camera` varchar(10) NOT NULL,
  `price` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `phone`
--

INSERT INTO `phone` (`product_ID`, `name`, `color`, `screen_size`, `brand`, `memory`, `camera`, `price`) VALUES
(13, 'Samsung Galaxy J8', 'black', '5.7', 'Samsung', '4GB', '16MP', 2000),
(14, ' Huawei Honor 10 lite', 'white', '6.21', 'Huawei', '3GB', '13MP', 1700),
(15, 'Samsung Galaxy A30', 'blue', '6.1', 'Samsung', '4GB', '13MP', 1500),
(16, 'iPhone 6S 16GB', 'pink', '5.5', 'Apple', '4GB', '16MP', 7000),
(17, 'aa', 'black', 'a', 'a', 'a', 'a', 7);

-- --------------------------------------------------------

--
-- Table structure for table `products`
--

CREATE TABLE `products` (
  `product_ID` int(11) NOT NULL,
  `seller_ID` int(11) NOT NULL,
  `category` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `products`
--

INSERT INTO `products` (`product_ID`, `seller_ID`, `category`) VALUES
(9, 1, 'headphone'),
(7, 1, 'television'),
(11, 1, 'phone'),
(12, 1, 'phone'),
(10, 1, 'headphone'),
(11, 1, 'headphone'),
(12, 1, 'headphone'),
(17, 2, 'headphone'),
(17, 2, 'phone'),
(16, 2, 'television');

-- --------------------------------------------------------

--
-- Table structure for table `sellers`
--

CREATE TABLE `sellers` (
  `seller_ID` int(11) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `sellers`
--

INSERT INTO `sellers` (`seller_ID`, `email`, `password`) VALUES
(0, 'seller@email.com', 'sellerpass12'),
(1, 'aa@aa.com', 'aaaaaa11'),
(2, 'seller@gmail.com', 'sellerpass1');

-- --------------------------------------------------------

--
-- Table structure for table `television`
--

CREATE TABLE `television` (
  `product_ID` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `brand` varchar(50) NOT NULL,
  `screen_size` varchar(100) NOT NULL,
  `resolution` varchar(100) NOT NULL,
  `price` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `television`
--

INSERT INTO `television` (`product_ID`, `name`, `brand`, `screen_size`, `resolution`, `price`) VALUES
(12, 'SEG 32SCH5630 32\" 81 Ekran LED TV', 'Sunny', '32', ' 1366 x 768 HD', 800),
(13, 'Samsung 40N5300 40\" TV', 'Samsung', '35', ' 1566 x 564 Full HD', 1500),
(14, 'Grundig 32VLE6730 BP ', 'Grundig', '32', '1920 x 540 Full HD', 1400),
(15, 'Philips 46PDL8908S 32\" Full HD', 'Philips', '32', '1920 x 1080 Full HD', 1400),
(16, 'ahaha', 'ahahah', '15', '15x15', 1555);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`admin_ID`);

--
-- Indexes for table `books`
--
ALTER TABLE `books`
  ADD PRIMARY KEY (`product_ID`);

--
-- Indexes for table `customers`
--
ALTER TABLE `customers`
  ADD PRIMARY KEY (`customer_ID`);

--
-- Indexes for table `headphone`
--
ALTER TABLE `headphone`
  ADD PRIMARY KEY (`product_ID`);

--
-- Indexes for table `laptop`
--
ALTER TABLE `laptop`
  ADD PRIMARY KEY (`product_ID`);

--
-- Indexes for table `phone`
--
ALTER TABLE `phone`
  ADD PRIMARY KEY (`product_ID`);

--
-- Indexes for table `sellers`
--
ALTER TABLE `sellers`
  ADD UNIQUE KEY `seller_ID` (`seller_ID`);

--
-- Indexes for table `television`
--
ALTER TABLE `television`
  ADD PRIMARY KEY (`product_ID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `admin`
--
ALTER TABLE `admin`
  MODIFY `admin_ID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `books`
--
ALTER TABLE `books`
  MODIFY `product_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `customers`
--
ALTER TABLE `customers`
  MODIFY `customer_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `headphone`
--
ALTER TABLE `headphone`
  MODIFY `product_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT for table `laptop`
--
ALTER TABLE `laptop`
  MODIFY `product_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `phone`
--
ALTER TABLE `phone`
  MODIFY `product_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT for table `television`
--
ALTER TABLE `television`
  MODIFY `product_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
