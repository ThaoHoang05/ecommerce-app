CREATE TABLE invoice_details (
                                 invoice_detail_id serial PRIMARY KEY ,
                                 invoice_id INT,                     -- FK đến invoices
                                 product_id INT,                     -- FK đến products
                                 quantity INT,
                                 unit_price DECIMAL(10,2),          -- Giá tại thời điểm bán
                                 subtotal DECIMAL(15,2),            -- unit_price * quantity
                                 FOREIGN KEY (invoice_id) REFERENCES invoices(invoice_id),
                                 FOREIGN KEY (product_id) REFERENCES products(product_id)
);