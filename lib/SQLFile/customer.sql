CREATE TABLE customer (
                          customer_id INTEGER NOT NULL PRIMARY KEY,
                          full_name VARCHAR(100) NOT NULL,
                          phone_number VARCHAR(20),
                          email VARCHAR(100),
                          address TEXT,
                          user_id VARCHAR(5) NOT NULL UNIQUE,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          CONSTRAINT user_id_fk FOREIGN KEY (user_id) REFERENCES users(user_id)
);