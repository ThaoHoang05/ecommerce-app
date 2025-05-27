create table users(
    user_id varchar(5) primary key not null,
    user_name varchar(50) not null unique,
    pwd_hash varchar(256),
    role_id integer not null,
    CONSTRAINT user_role_fk FOREIGN KEY (role_id) REFERENCES roles(role_id)
);