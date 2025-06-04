CREATE DATABASE WHISKEYDB;

USE WHISKEYDB;

CREATE TABLE Whiskey (
    id INT AUTO_INCREMENT PRIMARY KEY,
    distillery VARCHAR(100) NOT NULL,
    age INT,
    region VARCHAR(50),
    price DECIMAL(10, 2)
);

INSERT INTO Whiskey (distillery, age, region, price) VALUES
    ('Glenmorangie', 10, 'Highland', 45.00),
    ('Talisker', 18, 'Isle of Skye', 85.00),
    ('Laphroaig', 12, 'Islay', 55.00),
    ('Macallan', 15, 'Speyside', 75.00);
    
   

