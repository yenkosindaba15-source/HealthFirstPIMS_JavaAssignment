INSERT INTO users (username,password,role,full_name)
VALUES ('admin','admin123','Admin','System Administrator'),
       ('cashier','cash123','Cashier','Default Cashier'),
       ('cashier2','cash567','Cashier','Test Cashier');

INSERT INTO suppliers (supplier_name,contact_person,phone,email,address)
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