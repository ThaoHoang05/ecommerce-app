CREATE TABLE invoices (
                          invoice_id SERIAL PRIMARY KEY,
                          user_id varchar(5) NOT NULL,
                          customer_id INT NOT NULL,
                          invoice_date DATE NOT NULL,
                          total_amount DECIMAL(15,2) NOT NULL,
                          discount_amount DECIMAL(15,2) DEFAULT 0.00,
                          final_amount DECIMAL(15,2) GENERATED ALWAYS AS (total_amount - discount_amount) STORED,
                          status VARCHAR(20) CHECK (status IN ('pending', 'completed')) DEFAULT 'pending',

                          FOREIGN KEY (user_id) REFERENCES users(user_id),
                          FOREIGN KEY (customer_id) REFERENCES customer(customer_id)
);