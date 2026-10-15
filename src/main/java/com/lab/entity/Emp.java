package com.lab.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 员工实体，对应表 emp。
 * 同时供原生 MyBatis 与 MyBatis-Plus 使用（不加 @TableLogic，status 表示在职/离职）。
 */
@Data
@TableName("emp")
public class Emp {

    @TableId(value = "emp_id", type = IdType.AUTO)
    private Integer empId;

    @TableField("emp_name")
    private String empName;

    private String gender;

    private String dept;

    private String post;

    private BigDecimal salary;

    @TableField("hire_date")
    private LocalDate hireDate;

    /** 1=在职，0=离职（不作逻辑删除字段） */
    private Integer status;
}
