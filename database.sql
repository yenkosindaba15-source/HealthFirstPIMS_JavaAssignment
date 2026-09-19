CREATE TABLE medicines(
    medicine_id: INT PRIMARY KEY AUTO_INCREMENT,
    name: VARCHAR(150),
    company: VARCHAR(100),
    medicine_type: VARCHAR(50),
    price: DECIMAL(10,2),
    quantity_in_stock: INT,
    reorder_level: INT,
    expiry_date: DATE,
    supplier_id: INT, FOREIGN KEY REFERENCING suppliers(supplier_id)
);