package com.lab;

import com.lab.entity.Emp;
import com.lab.mapper.EmpMapper;
import com.lab.util.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * 原生 MyBatis EmpMapper 测试（JUnit4）。
 * 增删改务必 session.commit()。
 */
public class EmpMapperTest {

    private SqlSession session;
    private EmpMapper empMapper;

    @Before
    public void init() {
        session = MyBatisUtil.openSession();
        empMapper = session.getMapper(EmpMapper.class);
    }

    @After
    public void destroy() {
        if (session != null) {
            session.close();
        }
    }

    @Test
    public void testSelectAll() {
        List<Emp> list = empMapper.selectAll();
        for (Emp emp : list) {
            System.out.println(emp);
        }
        Assert.assertFalse("emp 表无数据，请先执行 sql/emp.sql", list.isEmpty());
    }

    @Test
    public void testInsert() {
        Emp emp = new Emp();
        emp.setEmpName("陈晨");
        emp.setGender("男");
        emp.setDept("技术部");
        emp.setPost("实习生");
        emp.setSalary(new BigDecimal("6000.00"));
        emp.setHireDate(LocalDate.of(2024, 7, 1));
        emp.setStatus(1);

        int rows = empMapper.insert(emp);
        session.commit();

        System.out.println("影响行数：" + rows);
        System.out.println("回填主键 empId=" + emp.getEmpId());
        Assert.assertEquals(1, rows);
        Assert.assertNotNull(emp.getEmpId());

        // 清理本条，避免重复堆积
        empMapper.deleteById(emp.getEmpId());
        session.commit();
    }

    @Test
    public void testUpdate() {
        Emp before = empMapper.selectById(1);
        Assert.assertNotNull("请确认 id=1 样例数据存在", before);
        BigDecimal oldSalary = before.getSalary();

        Emp emp = new Emp();
        emp.setEmpId(1);
        emp.setEmpName(before.getEmpName());
        emp.setGender(before.getGender());
        emp.setDept(before.getDept());
        emp.setPost(before.getPost());
        emp.setSalary(new BigDecimal("12500.00"));
        emp.setHireDate(before.getHireDate());
        emp.setStatus(before.getStatus());

        int rows = empMapper.update(emp);
        session.commit();
        System.out.println("更新影响行数：" + rows);
        System.out.println("更新后：" + empMapper.selectById(1));
        Assert.assertEquals(1, rows);

        // 还原薪资，方便反复跑
        emp.setSalary(oldSalary);
        empMapper.update(emp);
        session.commit();
    }

    @Test
    public void testDelete() {
        // 先插入再删自己的数据，避免误删样例且可重复执行
        Emp emp = new Emp();
        emp.setEmpName("临时删除");
        emp.setGender("女");
        emp.setDept("测试部");
        emp.setPost("测试");
        emp.setSalary(new BigDecimal("5000.00"));
        emp.setHireDate(LocalDate.now());
        emp.setStatus(1);
        empMapper.insert(emp);
        session.commit();
        Integer id = emp.getEmpId();
        System.out.println("待删除 id=" + id);

        int rows = empMapper.deleteById(id);
        session.commit();
        System.out.println("删除影响行数：" + rows);
        Assert.assertEquals(1, rows);
        Assert.assertNull(empMapper.selectById(id));
    }

    @Test
    public void testSelectByCondition() {
        Emp condName = new Emp();
        condName.setEmpName("伟");
        List<Emp> byName = empMapper.selectByCondition(condName);
        System.out.println("姓名模糊「伟」：" + byName);
        Assert.assertFalse(byName.isEmpty());

        Emp condDept = new Emp();
        condDept.setDept("技术部");
        List<Emp> byDept = empMapper.selectByCondition(condDept);
        System.out.println("部门=技术部：" + byDept);
        Assert.assertFalse(byDept.isEmpty());
        for (Emp e : byDept) {
            Assert.assertEquals("技术部", e.getDept());
        }
    }

    @Test
    public void testUpdateDynamic() {
        Emp emp = new Emp();
        emp.setEmpId(2);
        emp.setSalary(new BigDecimal("8500.00"));
        int rows = empMapper.updateDynamic(emp);
        session.commit();
        System.out.println("动态更新影响行数：" + rows);
        Emp after = empMapper.selectById(2);
        System.out.println("更新后：" + after);
        Assert.assertEquals(1, rows);
        Assert.assertEquals(0, new BigDecimal("8500.00").compareTo(after.getSalary()));

        // 还原
        emp.setSalary(new BigDecimal("8000.00"));
        empMapper.updateDynamic(emp);
        session.commit();
    }

    @Test
    public void testInsertBatch() {
        Emp e1 = new Emp();
        e1.setEmpName("批量甲");
        e1.setGender("男");
        e1.setDept("市场部");
        e1.setPost("销售");
        e1.setSalary(new BigDecimal("7000.00"));
        e1.setHireDate(LocalDate.of(2023, 1, 1));
        e1.setStatus(1);

        Emp e2 = new Emp();
        e2.setEmpName("批量乙");
        e2.setGender("女");
        e2.setDept("市场部");
        e2.setPost("销售");
        e2.setSalary(new BigDecimal("7200.00"));
        e2.setHireDate(LocalDate.of(2023, 2, 1));
        e2.setStatus(1);

        List<Emp> list = Arrays.asList(e1, e2);
        int rows = empMapper.insertBatch(list);
        session.commit();
        System.out.println("批量插入行数：" + rows);
        System.out.println("回填 id：" + e1.getEmpId() + ", " + e2.getEmpId());
        Assert.assertEquals(2, rows);

        // 随即清理
        empMapper.deleteBatch(Arrays.asList(e1.getEmpId(), e2.getEmpId()));
        session.commit();
    }

    @Test
    public void testDeleteBatch() {
        Emp e1 = new Emp();
        e1.setEmpName("批删一");
        e1.setGender("男");
        e1.setDept("临时部");
        e1.setPost("临工");
        e1.setSalary(new BigDecimal("1000.00"));
        e1.setHireDate(LocalDate.now());
        e1.setStatus(1);

        Emp e2 = new Emp();
        e2.setEmpName("批删二");
        e2.setGender("女");
        e2.setDept("临时部");
        e2.setPost("临工");
        e2.setSalary(new BigDecimal("1000.00"));
        e2.setHireDate(LocalDate.now());
        e2.setStatus(1);

        empMapper.insertBatch(Arrays.asList(e1, e2));
        session.commit();

        List<Integer> ids = Arrays.asList(e1.getEmpId(), e2.getEmpId());
        int rows = empMapper.deleteBatch(ids);
        session.commit();
        System.out.println("批量删除行数：" + rows);
        Assert.assertEquals(2, rows);
    }

    @Test
    public void testSelectByChoose() {
        Emp byName = new Emp();
        byName.setEmpName("张伟");
        byName.setDept("人事部"); // 有姓名时忽略部门
        List<Emp> nameList = empMapper.selectByChoose(byName);
        System.out.println("choose 精确姓名：" + nameList);
        Assert.assertEquals(1, nameList.size());
        Assert.assertEquals("张伟", nameList.get(0).getEmpName());

        Emp byDept = new Emp();
        byDept.setDept("技术部");
        List<Emp> deptList = empMapper.selectByChoose(byDept);
        System.out.println("choose 按部门：" + deptList);
        Assert.assertFalse(deptList.isEmpty());

        Emp all = new Emp();
        List<Emp> allList = empMapper.selectByChoose(all);
        System.out.println("choose otherwise 全部：" + allList.size() + " 条");
        Assert.assertFalse(allList.isEmpty());
    }
}
