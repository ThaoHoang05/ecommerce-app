CREATE TABLE products_supplied (
    product_id INTEGER NOT NULL,
    supplier_id INTEGER NOT NULL,
    supply_price DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (product_id, supplier_id),
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE,
    FOREIGN KEY (supplier_id) REFERENCES suppliers(supplier_id) ON DELETE CASCADE
);
