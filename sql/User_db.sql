CREATE DATABASE IF NOT EXISTS test DEFAULT CHARACTER SET utf8mb4;
USE test;

CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `user` (`username`, `password`, `email`) VALUES
('testuser1', 'password123', 'testuser1@example.com'),
('testuser2', 'password456', 'testuser2@example.com'),
('testuser3', 'password789', 'testuser3@example.com'),
('testuser4', 'password123', 'testuser4@example.com'),
('testuser5', 'password123', 'testuser5@example.com'),
('testuser6', 'password456', 'testuser6@example.com'),
('testuser7', 'password789', 'testuser7@example.com'),
('testuser8', 'password123', 'testuser8@example.com'),
('testuser9', 'password456', 'testuser9@example.com'),
('testuser10', 'password123', 'testuser10@example.com');

-- ============================================================
-- orders table (requires existing `user` table)
-- Prerequisite: run User_db.sql (or equivalent) first so `user` exists.
-- Table name uses backticks because ORDER is a reserved word; we use `orders`.
-- ============================================================

CREATE TABLE IF NOT EXISTS `orders` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `product_name` VARCHAR(100) NOT NULL,
  `amount` DECIMAL(10, 2) NOT NULL,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT `fk_orders_user` FOREIGN KEY (`user_id`) REFERENCES `user`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Sample data linked to existing user ids 1-3
INSERT INTO `orders` (`user_id`, `product_name`, `amount`) VALUES
(1, 'Mechanical Keyboard', 299.00),
(1, 'USB-C Hub', 89.50),
(2, 'Monitor 27 inch', 1299.00),
(2, 'Webcam HD', 199.99),
(3, 'Laptop Stand', 159.00),
(3, 'Wireless Mouse', 79.00);
