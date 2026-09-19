CREATE DATABASE healthfirst_pims;
USE healthfirst_pims;

CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL,
    full_name VARCHAR(100) NOT NULL
);

INSERT INTO users (username,password,role,full_name)
VALUES ('admin','admin123','Admin','System Administrator');

INSERT INTO users (username,password,role,full_name)
VALUES ('cashier','cash123','Cashier','Default Cashier');