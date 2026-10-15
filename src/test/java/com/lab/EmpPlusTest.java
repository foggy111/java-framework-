package com.lab;

import com.baomidou.mybatisplus.core.MybatisSqlSessionFactoryBuilder;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lab.entity.Emp;
import com.lab.mapper.EmpMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * MyBatis-Plus 测试（非 Spring）：库 ssm_emp，分页插件在 init 中注册。
 */
public class EmpPlusTest {

    private SqlSession sqlSession;
    private EmpMapper empMapper;

    @Before
    public void init() throws Exception {
        InputStream in = Resources.getResourceAsStream("mybatis-lab-plus-config.xml");
        SqlSessionFactory factory = new MybatisSqlSessionFactoryBuilder().build(in);
        in.close();

        Configuration configuration = factory.getConfiguration();
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        configuration.addInterceptor(interceptor);

        sqlSession = factory.openSession(true);
        empMapper = sqlSession.getMapper(EmpMapper.class);
    }

    @After
    public void destroy() {
        if (sqlSession != null) {
            sqlSession.close();
        }
    }

    @Test
    public void testSelectList() {
        System.out.println("========== MP 查询全部 ==========");
        List<Emp> list = empMapper.selectList(null);
        for (Emp e : list) {
            System.out.println(e);
        }
        System.out.println("共 " + list.size() + " 条");
        Assert.assertFalse("请先执行 sql/emp.sql", list.isEmpty());
    }

    @Test
    public void testInsert() {
        System.out.println("========== MP 插入 ==========");
        Emp emp = new Emp();
        emp.setEmpName("周杰");
        emp.setGender("男");
        emp.setDept("技术部");
        emp.setPost("架构师");
        emp.setSalary(new BigDecimal("22000.00"));
        emp.setHireDate(LocalDate.of(2018, 5, 20));
        emp.setStatus(1);

        int rows = empMapper.insert(emp);
        System.out.println("影响行数：" + rows + "，主键=" + emp.getEmpId());
        Assert.assertEquals(1, rows);
        Assert.assertNotNull(emp.getEmpId());

        empMapper.deleteById(emp.getEmpId());
    }

    @Test
    public void testLambdaQuery() {
        System.out.println("========== Lambda 条件查询 ==========");
        LambdaQueryWrapper<Emp> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Emp::getEmpName, "王")
                .eq(Emp::getStatus, 1)
                .orderByDesc(Emp::getSalary);
        List<Emp> list = empMapper.selectList(wrapper);
        for (Emp e : list) {
            System.out.println(e);
        }
        Assert.assertFalse(list.isEmpty());
        for (Emp e : list) {
            Assert.assertTrue(e.getEmpName().contains("王"));
            Assert.assertEquals(Integer.valueOf(1), e.getStatus());
        }
    }

    @Test
    public void testPage() {
        System.out.println("========== 分页查询 ==========");
        Page<Emp> page = new Page<>(1, 3);
        empMapper.selectPage(page, null);
        for (Emp e : page.getRecords()) {
            System.out.println(e);
        }
        System.out.println("total=" + page.getTotal() + ", pages=" + page.getPages()
                + ", current=" + page.getCurrent() + ", size=" + page.getSize());
        Assert.assertTrue(page.getRecords().size() <= 3);
        Assert.assertTrue(page.getTotal() >= page.getRecords().size());
    }
}
