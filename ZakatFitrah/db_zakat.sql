-- phpMyAdmin SQL Dump
-- version 4.8.3
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 13 Jun 2020 pada 15.01
-- Versi server: 10.1.36-MariaDB
-- Versi PHP: 7.2.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db_zakat`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `dt_mustahiq`
--

CREATE TABLE `dt_mustahiq` (
  `nomor` int(11) NOT NULL,
  `tglInput` char(10) NOT NULL,
  `gol` varchar(20) NOT NULL,
  `namaMustahiq` varchar(30) NOT NULL,
  `alamat` text NOT NULL,
  `jmlUang` int(11) NOT NULL,
  `jmlBeras` int(11) NOT NULL,
  `panitia` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `dt_mustahiq`
--

INSERT INTO `dt_mustahiq` (`nomor`, `tglInput`, `gol`, `namaMustahiq`, `alamat`, `jmlUang`, `jmlBeras`, `panitia`) VALUES
(1, '11-06-2020', 'FAKIR MISKIN', 'SDFSDF', 'sdfsdf', 3000, 1, 'admin'),
(2, '11-06-2020', 'AMILIN', 'DFGSDFG', 'fgdsfg', 5000, 1, 'admin'),
(3, '11-06-2020', 'FAKIR MISKIN', 'EDGERGH', 'ertgerg', 50000, 1, 'admin'),
(4, '11-06-2020', 'AMILIN', 'GVGH', 'l,p[', 4000000, 1, 'admin'),
(5, '11-06-2020', 'RIQOB', 'VJGH', 'hvj', 500000, 1, 'admin'),
(6, '11-06-2020', 'FAKIR MISKIN', 'GYCVHG', 'jhvb', 442000, 0, 'admin'),
(7, '13-06-2020', 'FISABILILLAH', 'FGHG', 'fghfgjh', 2000000, 2, 'admin');

--
-- Trigger `dt_mustahiq`
--
DELIMITER $$
CREATE TRIGGER `update_after_mustahiq` AFTER INSERT ON `dt_mustahiq` FOR EACH ROW update log_muzakki set currDate=new.tglInput,
totalUang=totalUang-new.jmlUang,
totalBeras=totalBeras-new.jmlBeras
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Struktur dari tabel `dt_muzakki`
--

CREATE TABLE `dt_muzakki` (
  `nomor` int(11) NOT NULL,
  `tglInput` char(10) NOT NULL,
  `namaMuzakki` varchar(30) NOT NULL,
  `Alamat` text NOT NULL,
  `jmlJiwa` int(11) NOT NULL,
  `jmlZakat` int(11) NOT NULL,
  `jmlBeras` int(11) NOT NULL,
  `panitia` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `dt_muzakki`
--

INSERT INTO `dt_muzakki` (`nomor`, `tglInput`, `namaMuzakki`, `Alamat`, `jmlJiwa`, `jmlZakat`, `jmlBeras`, `panitia`) VALUES
(1, '13-06-2020', 'DGHDG', 'ghg', 1, 100000, 2, 'admin'),
(2, '13-06-2020', 'DFGHDFH', 'dghgd', 1, 200000, 0, 'admin'),
(3, '13-06-2020', 'DGHD', 'ghd', 2, 500000, 2, 'admin'),
(4, '13-06-2020', 'SAAYA', 'sgsfg', 1, 200000, 2, 'admin'),
(5, '13-06-2020', 'SGSFDGFSDGH', '', 3, 8000000, 2, 'admin'),
(6, '13-06-2020', 'FGHFGJHFJFG', 'hjfhj', 1, 11000, 0, 'admin'),
(8, '13-06-2020', 'ARI', 'jl', 3, 5000000, 25, 'admin');

--
-- Trigger `dt_muzakki`
--
DELIMITER $$
CREATE TRIGGER `update_log_muzakki` AFTER INSERT ON `dt_muzakki` FOR EACH ROW update log_muzakki set currDate=new.tglInput,
totalUang=totalUang+new.jmlZakat,
totalBeras=totalBeras+new.jmlBeras
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Struktur dari tabel `log_muzakki`
--

CREATE TABLE `log_muzakki` (
  `currDate` varchar(10) NOT NULL,
  `totalUang` int(9) NOT NULL,
  `totalBeras` int(6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `log_muzakki`
--

INSERT INTO `log_muzakki` (`currDate`, `totalUang`, `totalBeras`) VALUES
('13-06-2020', 24361000, 82),
('13-06-2020', 29361000, 132),
('13-06-2020', 29361000, 132),
('13-06-2020', 29411000, 133),
('13-06-2020', 30011000, 142),
('13-06-2020', 30011000, 142),
('13-06-2020', 30011000, 142),
('13-06-2020', 30011000, 142),
('13-06-2020', 20011000, 31),
('13-06-2020', 19911000, 29),
('13-06-2020', 19711000, 29),
('13-06-2020', 19211000, 27),
('13-06-2020', 19011000, 25),
('13-06-2020', 11011000, 23);

-- --------------------------------------------------------

--
-- Struktur dari tabel `users`
--

CREATE TABLE `users` (
  `idUser` int(11) NOT NULL,
  `nmLengkap` varchar(25) NOT NULL,
  `usrName` varchar(20) NOT NULL,
  `psw` varchar(100) NOT NULL,
  `email` varchar(40) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `users`
--

INSERT INTO `users` (`idUser`, `nmLengkap`, `usrName`, `psw`, `email`) VALUES
(1, 'Admin', 'admin', 'admin', 'hahahah');

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `dt_mustahiq`
--
ALTER TABLE `dt_mustahiq`
  ADD PRIMARY KEY (`nomor`);

--
-- Indeks untuk tabel `dt_muzakki`
--
ALTER TABLE `dt_muzakki`
  ADD PRIMARY KEY (`nomor`);

--
-- Indeks untuk tabel `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`idUser`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `users`
--
ALTER TABLE `users`
  MODIFY `idUser` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
