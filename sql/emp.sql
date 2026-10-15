-- ============================================================
-- 实验一：员工表 emp（库 ssm_emp）
-- 可重复执行：先建库再建表插样例
-- ============================================================
CREATE DATABASE IF NOT EXISTS ssm_emp DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE ssm_emp;

DROP TABLE IF EXISTS emp;

CREATE TABLE emp (
    emp_id    INT            NOT NULL AUTO_INCREMENT COMMENT '员工编号',
    emp_name  VARCHAR(50)    DEFAULT NULL COMMENT '姓名',
    gender    CHAR(1)        DEFAULT '男' COMMENT '性别',
    dept      VARCHAR(50)    DEFAULT NULL COMMENT '部门',
    post      VARCHAR(50)    DEFAULT NULL COMMENT '岗位',
    salary    DECIMAL(10,2)  DEFAULT NULL COMMENT '薪资',
    hire_date DATE           DEFAULT NULL COMMENT '入职日期',
    status    TINYINT        DEFAULT 1 COMMENT '状态：1在职 0离职',
    PRIMARY KEY (emp_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='员工表';

INSERT INTO emp (emp_name, gender, dept, post, salary, hire_date, status) VALUES
('张伟', '男', '技术部', '工程师',     12000.00, '2020-03-15', 1),
('李娜', '女', '人事部', '专员',       8000.00,  '2021-06-01', 1),
('王强', '男', '技术部', '高级工程师', 18000.00, '2019-01-10', 1),
('赵敏', '女', '财务部', '会计',       9000.00,  '2022-02-20', 1);
