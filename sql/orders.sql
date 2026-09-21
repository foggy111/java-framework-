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
