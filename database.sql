CREATE TABLE sale_items(
    sale_item_id: INT PRIMARY KEY AUTO_INCREMENT,
    sale_id: INT FOREIGN KEY REFERENCING sales(sale_id),
    medicine_id: INT FOREIGN KEY REFERENCING medicines(medicine_id),
    quantity_sold: INT,
    price_at_sale: DECIMAL(10,2)
);