-- =============================================================
-- 产品(product) / 供应商(supplier) / 客户(customer) 作业建表 + 测试数据
-- 数据库: test    字符集: utf8mb4    可重复执行
-- 关系:
--   product  <-> supplier : 多对多, 中间表 product_supplier
--   customer  -> product  : 一对多, product.customer_id = 买走该产品的客户
--   supplier <-> customer : 多对多, 中间表 supplier_customer
-- =============================================================
USE test;
SET NAMES utf8mb4;

-- 按外键依赖顺序删除(先删中间表, 再删 product, 最后删 supplier / customer)
DROP TABLE IF EXISTS supplier_customer;
DROP TABLE IF EXISTS product_supplier;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS supplier;
DROP TABLE IF EXISTS customer;

-- 客户表
CREATE TABLE customer (
    id    INT PRIMARY KEY AUTO_INCREMENT COMMENT '客户ID',
    name  VARCHAR(50) NOT NULL          COMMENT '客户姓名',
    phone VARCHAR(20)                   COMMENT '联系电话'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户';

-- 供应商表
CREATE TABLE supplier (
    id    INT PRIMARY KEY AUTO_INCREMENT COMMENT '供应商ID',
    name  VARCHAR(50) NOT NULL          COMMENT '供应商名称',
    phone VARCHAR(20)                   COMMENT '联系电话'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='供应商';

-- 产品表 (customer_id: 买走该产品的客户, NULL 表示还没卖出)
CREATE TABLE product (
    id          INT PRIMARY KEY AUTO_INCREMENT COMMENT '产品ID',
    name        VARCHAR(50) NOT NULL          COMMENT '产品名称',
    price       DECIMAL(10,2)                 COMMENT '价格',
    customer_id INT                           COMMENT '购买客户ID',
    CONSTRAINT fk_product_customer FOREIGN KEY (customer_id) REFERENCES customer(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品';

-- 产品-供应商 中间表 (多对多)
CREATE TABLE product_supplier (
    product_id  INT NOT NULL,
    supplier_id INT NOT NULL,
    PRIMARY KEY (product_id, supplier_id),
    CONSTRAINT fk_ps_product  FOREIGN KEY (product_id)  REFERENCES product(id),
    CONSTRAINT fk_ps_supplier FOREIGN KEY (supplier_id) REFERENCES supplier(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品-供应商';

-- 供应商-客户 中间表 (多对多)
CREATE TABLE supplier_customer (
    supplier_id INT NOT NULL,
    customer_id INT NOT NULL,
    PRIMARY KEY (supplier_id, customer_id),
    CONSTRAINT fk_sc_supplier FOREIGN KEY (supplier_id) REFERENCES supplier(id),
    CONSTRAINT fk_sc_customer FOREIGN KEY (customer_id) REFERENCES customer(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='供应商-客户';

-- ---------------- 测试数据 ----------------
INSERT INTO customer (id, name, phone) VALUES
(1, '张三', '13800000001'),
(2, '李四', '13800000002'),
(3, '王五', '13800000003'),
(4, '赵六', '13800000004');

INSERT INTO supplier (id, name, phone) VALUES
(1, '华强电子', '010-88880001'),
(2, '联达贸易', '021-66660002'),
(3, '新远科技', '020-77770003'),
(4, '恒通供应链', '0755-55550004');

-- 第 8 个产品还没卖出 (customer_id 为 NULL), 用来体现 LEFT JOIN
INSERT INTO product (id, name, price, customer_id) VALUES
(1, '笔记本电脑', 5999.00, 1),
(2, '无线鼠标',     99.00, 1),
(3, '机械键盘',    399.00, 2),
(4, '显示器',     1299.00, 2),
(5, '智能手机',   3999.00, 3),
(6, '蓝牙耳机',    299.00, 3),
(7, '平板电脑',   2999.00, 4),
(8, '移动硬盘',    459.00, NULL);

INSERT INTO product_supplier (product_id, supplier_id) VALUES
(1, 1), (1, 3),
(2, 2),
(3, 2), (3, 4),
(4, 1),
(5, 3),
(6, 4),
(7, 1), (7, 3),
(8, 2);

-- 供应商-客户: 与实际购买记录一致 (客户买了该供应商供应的产品)
-- 华强电子: 笔记本(张三) 显示器(李四) 平板(赵六)
-- 联达贸易: 鼠标(张三) 键盘(李四)            移动硬盘未售出
-- 新远科技: 笔记本(张三) 手机(王五) 平板(赵六)
-- 恒通供应链: 键盘(李四) 耳机(王五)
INSERT INTO supplier_customer (supplier_id, customer_id) VALUES
(1, 1), (1, 2), (1, 4),
(2, 1), (2, 2),
(3, 1), (3, 3), (3, 4),
(4, 2), (4, 3);
