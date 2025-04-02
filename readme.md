Procedure for running the project

1.Install NetBeans Software in your System

2.Extract all files from the zip folder 

3. In the src folder Update existing root and password in the file DatabseConnection with root and password of your systems MySQL Workbench 

OR

Create tables for DataBase BusReservationSystem manually by manually running these codes on your MySqlWorkbench 

Create database bus_reservation;
use bus_reservation;


CREATE TABLE bookings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(100) NOT NULL,
    contact VARCHAR(15) NOT NULL,
    bus_no VARCHAR(20) NOT NULL,
    source VARCHAR(50) NOT NULL,
    destination VARCHAR(50) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    seat_no INT NOT NULL,
    time VARCHAR(20) NOT NULL,
    date VARCHAR(20) NOT NULL,
    payment_status VARCHAR(20) NOT NULL
);

CREATE TABLE bus_details (
    bus_no VARCHAR(30) PRIMARY KEY,
    source VARCHAR(200) NOT NULL,
    destination VARCHAR(200) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    seat DECIMAL(10,2) NOT NULL,
    time varchar(40) NOT NULL,
    date DATE NOT NULL
);

CREATE TABLE users (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    weburl VARCHAR(225),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE bus_reservation.admin (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(45) NOT NULL,
  username VARCHAR(50) unique NOT NULL,
  password VARCHAR(45) NOT NULL,
  PRIMARY KEY (id));

Manually add a sample data to the admin table using mysqlworkbench to access adminlogin page.

