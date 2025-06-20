insert into suppliers (supplier_id, supplier_name, contact_person, email, phone, address) values
(1, 'Nhà cung cấp 1', 'contact 1', 'contact1@email.com', '03762719365', 'Hà Nội'),
(4, 'Nhà cung cấp 4', 'contact 4', 'contact4@email.com', '09853284627', 'Ninh Bình'),
(5, 'Nhà cung cấp 5', 'contact 5', 'contact5@email.com', '09364681246', 'Hải Phòng');

insert into products_supplied (product_id, supplier_id, supply_price) values 
(1, 5, 1),
(2, 4, 1),
(3, 1, 25),
(4, 5, 8),
(5, 2, 5),
(6, 5, 10),
(7, 1, 7),
(8, 4, 3),
(9, 3, 2),
(10, 5, 3),
(11, 4, 8);
