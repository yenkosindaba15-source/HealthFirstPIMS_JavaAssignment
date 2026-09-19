CREATE TABLE sales(
    sale_id: INT PRIMARY KEY AUTO_INCREMENT,
    sale_date: TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_amount: DECIMAL(10,2),
    user_id: INT FOREIGN KEY REFERENCING users(user_id)
);