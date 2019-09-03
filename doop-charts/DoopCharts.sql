-- phpMyAdmin SQL Dump
-- version 3.3.10deb1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Sep 12, 2011 at 08:48 PM
-- Server version: 5.1.54
-- PHP Version: 5.3.5-1ubuntu7.2

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `DoopCharts`
--

-- --------------------------------------------------------

--
-- Table structure for table `chartComments`
--

CREATE TABLE IF NOT EXISTS `chartComments` (
  `rID` int(11) NOT NULL,
  `mID` int(11) NOT NULL,
  `comment` varchar(500) NOT NULL,
  PRIMARY KEY (`rID`,`mID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `chartComments`
--


-- --------------------------------------------------------

--
-- Table structure for table `elements`
--

CREATE TABLE IF NOT EXISTS `elements` (
  `eID` int(11) NOT NULL AUTO_INCREMENT,
  `rID` int(11) NOT NULL,
  `title` varchar(100) NOT NULL,
  PRIMARY KEY (`eID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `elements`
--


-- --------------------------------------------------------

--
-- Table structure for table `groups`
--

CREATE TABLE IF NOT EXISTS `groups` (
  `gID` int(11) NOT NULL AUTO_INCREMENT,
  `rID` int(11) NOT NULL,
  `title` varchar(100) NOT NULL,
  PRIMARY KEY (`gID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `groups`
--


-- --------------------------------------------------------

--
-- Table structure for table `metrics`
--

CREATE TABLE IF NOT EXISTS `metrics` (
  `mID` int(11) NOT NULL AUTO_INCREMENT,
  `rID` int(11) NOT NULL,
  `title` varchar(100) NOT NULL,
  PRIMARY KEY (`mID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `metrics`
--


-- --------------------------------------------------------

--
-- Table structure for table `reports`
--

CREATE TABLE IF NOT EXISTS `reports` (
  `rID` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL,
  `comments` varchar(700) NOT NULL DEFAULT '',
  PRIMARY KEY (`rID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `reports`
--


-- --------------------------------------------------------

--
-- Table structure for table `results`
--

CREATE TABLE IF NOT EXISTS `results` (
  `rID` int(11) NOT NULL,
  `mID` int(11) NOT NULL,
  `gID` int(11) NOT NULL,
  `eID` int(11) NOT NULL,
  `value` int(11) NOT NULL,
  PRIMARY KEY (`rID`,`mID`,`gID`,`eID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `results`
--

