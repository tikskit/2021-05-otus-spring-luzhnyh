CREATE TABLE products
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    name     VARCHAR(MAX) NOT NULL UNIQUE,
    quantity INT          NOT NULL
);