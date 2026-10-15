package com.lab.test;

import com.lab.entity.Emp;
import com.lab.mapper.EmpMapper;
import com.lab.util.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * 原生 MyBatis 演示：查询全部员工并打印。
 * 运行前请先执行 sql/emp.sql 建库建表。
 */
public class MyBatisDemo {

    public static void main(String[] args) {
        try (SqlSession session = MyBatisUtil.openSession(true)) {
            EmpMapper mapper = session.getMapper(EmpMapper.class);
            List<Emp> list = mapper.selectAll();
            System.out.println("========== 查询全部员工 ==========");
            for (Emp emp : list) {
                System.out.println(emp);
            }
            System.out.println("共 " + list.size() + " 条记录");
        }
    }
}
