CREATE DATABASE IF NOT EXISTS healthfirst_pims;
USE healthfirst_pims;

DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS suppliers;
DROP TABLE IF EXISTS medicines;
DROP TABLE IF EXISTS sales;
DROP TABLE IF EXISTS sales_items;

CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL,
    full_name VARCHAR(100) NOT NULL
);
CREATE TABLE suppliers (
    supplier_id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    contact_person VARCHAR(100),
    phone VARCHAR(20),
    email VARCHAR(100),
    address VARCHAR(50),
    PRIMARY KEY (supplier_id)
);
CREATE TABLE medicines (
    medicine_id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(150) NOT NULL,
    company VARCHAR(100) NOT NULL,
    medicine_type VARCHAR(50),
    price DECIMAL(10,2),
    quantity_in_stock INT,
    reorder_level INT,
    expiry_date DATE,
    supplier_id INT,
    PRIMARY KEY (medicine_id),
    KEY Supplier_id (supplier_id),
    CONSTRAINT medicines_ibfk_1 FOREIGN KEY (supplier_id) REFERENCES suppliers(supplier_id)
);
CREATE TABLE sales (
    sale_id INT NOT NULL AUTO_INCREMENT,
    sale_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10,2),
    user_id INT,
    PRIMARY KEY (sale_id),
    KEY User_id (user_id),
    CONSTRAINT sales_ibfk_1 FOREIGN KEY (user_id) REFERENCES users(user_id)
);
CREATE TABLE sale_items (
    sale_item_id INT NOT NULL AUTO_INCREMENT,
    sale_id INT NOT NULL,
    medicine_id INT NOT NULL,
    quantity_sold INT NOT NULL,
    price_at_sale DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (sale_item_id),
    KEY fk_sale_items_sale (sale_id),
    KEY fk_sale_items_medicine (medicine_id),
    CONSTRAINT fk_sale_items_sale FOREIGN KEY (sale_id) REFERENCES sales(sale_id),
    CONSTRAINT fk_sale_items_medicine FOREIGN KEY (medicine_id) REFERENCES medicines(medicine_id)
);

INSERT INTO users (username,password,role,full_name)
VALUES ('admin','admin123','Admin','System Administrator'),
       ('cashier','cash123','Cashier','Default Cashier'),
       ('cashier2','cash567','Cashier','Test Cashier');

INSERT INTO suppliers (name,contact_person,phone,email,address)
VALUES ('Aspen Pharmacare','John Smith','0811111111','aspen@example.com','Johannesburg'),
       ('Pfizer','Sarah Jones','0822222222','pfizer@example.com','Cape Town'),
       ('Adcock Ingram','Michael Brown', '0833333333','adcock@example.com','Durban'),
       ('MediPharm','Lisa Adams','0844444444','medipharm@example.com','Pretoria');

INSERT INTO medicines (name, company, medicine_type, price, quantity_in_stock, reorder_level, expiry_date, supplier_id)
VALUES ('Panado','Aspen','Tablet',55.00,100,20,'2028-12-31',1),
       ('Disprin','Bayer','Tablet',45.00,150,30,'2028-10-31',2),
       ('Myprodol','Aspen','Capsule',95.00,60,15,'2028-05-31',1),
       ('Cough Syrup','Mundipharma','Liquid',85.00,40,10,'2028-03-31',4),
       ('Allergex','Adcock Ingram','Tablet',65.00,120,25,'2028-08-31',3),
       ('Corenza C','Adcock Ingram','Tablet',75.00,90,20,'2028-06-30',3),
       ('Grand-Pa','Grand-Pa Company','Powder',10.00,50, 10, '2027-12-31',1),
       ('Calpol','GSK','Syrup', 70.00,35, 10,'2028-09-30',2);