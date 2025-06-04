CREATE DATABASE USERS;

USE USERS;

CREATE TABLE PASSWORDS
(
    USERNAME VARCHAR(16) NOT NULL,
    PASSWORD VARCHAR(64) NOT NULL,
    PRIMARY KEY (USERNAME)
);

INSERT INTO PASSWORDS(USERNAME, PASSWORD)
VALUES 
('jhj', 'Pass@123'),
('sarah', 'password'),
('mike', 'password'),
('adam1', 'Adam123'),
('david', 'david!456'),
('lisa', 'lisa789#');



