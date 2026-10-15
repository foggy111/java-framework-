-- ============================================================
-- MyBatis-Plus 练习表：student
-- 可重复执行（先 DROP 再 CREATE），不影响 test 库中的其它表
-- 使用：mysql -uroot -p test < sql/student.sql
-- ============================================================
USE test;

DROP TABLE IF EXISTS student;

CREATE TABLE student (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键（自增）',
    name        VARCHAR(50)  NOT NULL COMMENT '姓名',
    age         INT          DEFAULT NULL COMMENT '年龄',
    email       VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    version     INT          NOT NULL DEFAULT 1 COMMENT '乐观锁版本号',
    deleted     TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除：0=未删除，1=已删除',
    create_time DATETIME     DEFAULT NULL COMMENT '创建时间（自动填充）',
    update_time DATETIME     DEFAULT NULL COMMENT '更新时间（自动填充）',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生表（MyBatis-Plus 练习）';

INSERT INTO student (name, age, email, create_time, update_time) VALUES
('张三', 20, 'zhangsan@example.com', NOW(), NOW()),
('李四', 21, 'lisi@example.com',     NOW(), NOW()),
('王五', 19, 'wangwu@example.com',   NOW(), NOW()),
('赵六', 22, 'zhaoliu@example.com',  NOW(), NOW()),
('钱七', 20, 'qianqi@example.com',   NOW(), NOW()),
('孙八', 23, 'sunba@example.com',    NOW(), NOW());
