package com.chaperMybatisPlus;

import com.baomidou.mybatisplus.core.MybatisSqlSessionFactoryBuilder;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.GlobalConfigUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chaperMybatisPlus.config.MyMetaObjectHandler;
import com.chaperMybatisPlus.config.MybatisPlusConfig;
import com.chaperMybatisPlus.entity.Student;
import com.chaperMybatisPlus.mapper.StudentMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

/**
 * MyBatis-Plus 基础功能测试（非 Spring 环境）。
 * 运行前请先执行 sql/student.sql 建表并插入示例数据。
 * 每个测试方法都可以单独运行。
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StudentTest {

    private SqlSession sqlSession;
    private StudentMapper studentMapper;

    @Before
    public void init() throws Exception {
        // 1. 读取 MyBatis-Plus 核心配置文件
        InputStream in = Resources.getResourceAsStream("mybatis-plus-config.xml");
        // 2. 注意：必须使用 MybatisSqlSessionFactoryBuilder（而不是 SqlSessionFactoryBuilder），
        //    否则 BaseMapper 的通用 CRUD 方法不会被注入
        SqlSessionFactory sqlSessionFactory = new MybatisSqlSessionFactoryBuilder().build(in);
        in.close();
        Configuration configuration = sqlSessionFactory.getConfiguration();

        // 3. 注册插件：分页、乐观锁、防全表更新/删除（必须在 openSession 之前）
        MybatisPlusConfig.registerInterceptor(configuration);

        // 4. XML 中无法配置 globalConfig，这里手动注入自动填充处理器
        GlobalConfig globalConfig = GlobalConfigUtils.getGlobalConfig(configuration);
        globalConfig.setMetaObjectHandler(new MyMetaObjectHandler());
        GlobalConfigUtils.setGlobalConfig(configuration, globalConfig);

        // 5. 打开会话（true = 自动提交事务）
        sqlSession = sqlSessionFactory.openSession(true);
        studentMapper = sqlSession.getMapper(StudentMapper.class);
    }

    @After
    public void destroy() {
        if (sqlSession != null) {
            sqlSession.close();
        }
    }

    /** 1. 环境验证：查询全部（自动带上 deleted=0 条件） */
    @Test
    public void testSelectList() {
        System.out.println("========== 查询全部 ==========");
        List<Student> list = studentMapper.selectList(null);
        for (Student s : list) {
            System.out.println(s);
        }
        System.out.println("共 " + list.size() + " 条记录");
        Assert.assertFalse("student 表没有数据，请先执行 sql/student.sql", list.isEmpty());
    }

    /** 2. 插入：id 自增回填，createTime/updateTime 自动填充 */
    @Test
    public void testInsert() {
        System.out.println("========== 插入数据（自动填充） ==========");
        Student student = new Student("周九", 18, "zhoujiu@example.com");
        int rows = studentMapper.insert(student);
        System.out.println("影响行数：" + rows);
        System.out.println("自增主键 id：" + student.getId());
        System.out.println("自动填充 createTime：" + student.getCreateTime());
        System.out.println("自动填充 updateTime：" + student.getUpdateTime());
        System.out.println("数据库中的记录：" + studentMapper.selectById(student.getId()));
        Assert.assertEquals(1, rows);
        Assert.assertNotNull(student.getId());
        Assert.assertNotNull(student.getCreateTime());
    }

    /** 3. 根据 id 更新：只更新非 null 字段，updateTime 自动填充 */
    @Test
    public void testUpdateById() {
        System.out.println("========== 根据 id 更新 ==========");
        System.out.println("更新前：" + studentMapper.selectById(1L));
        Student student = new Student();
        student.setId(1L);
        student.setEmail("zhangsan_new@example.com");   // 只设置要修改的字段
        int rows = studentMapper.updateById(student);
        System.out.println("影响行数：" + rows);
        System.out.println("更新后：" + studentMapper.selectById(1L));
        Assert.assertEquals(1, rows);
    }

    /** 4. 条件查询：LambdaQueryWrapper */
    @Test
    public void testQueryWrapper() {
        System.out.println("========== 条件查询（LambdaQueryWrapper） ==========");
        // 年龄 >= 20 且 邮箱包含 example，按年龄降序
        LambdaQueryWrapper<Student> wrapper = new LambdaQueryWrapper<>();
        wrapper.ge(Student::getAge, 20)
                .like(Student::getEmail, "example")
                .orderByDesc(Student::getAge);
        List<Student> list = studentMapper.selectList(wrapper);
        for (Student s : list) {
            System.out.println(s);
        }
        System.out.println("共 " + list.size() + " 条记录");
        for (Student s : list) {
            Assert.assertTrue(s.getAge() >= 20);
        }
    }

    /** 5. 分页查询：需要 PaginationInnerInterceptor */
    @Test
    public void testPage() {
        System.out.println("========== 分页查询 ==========");
        Page<Student> page = new Page<>(1, 3);   // 第 1 页，每页 3 条
        studentMapper.selectPage(page, null);
        for (Student s : page.getRecords()) {
            System.out.println(s);
        }
        System.out.println("当前页：" + page.getCurrent());
        System.out.println("每页条数：" + page.getSize());
        System.out.println("总记录数：" + page.getTotal());
        System.out.println("总页数：" + page.getPages());
        // 如果分页插件没生效，records 会是全部数据、total 为 0
        Assert.assertEquals(3, page.getRecords().size());
        Assert.assertTrue(page.getTotal() > 3);
    }

    /** 6. 逻辑删除：deleteById 实际执行 UPDATE ... SET deleted=1 */
    @Test
    public void testLogicDelete() throws Exception {
        System.out.println("========== 逻辑删除 ==========");
        // 先插入一条临时数据，避免删掉示例数据
        Student temp = new Student("临时学生", 30, "temp@example.com");
        studentMapper.insert(temp);
        Long id = temp.getId();
        System.out.println("新插入临时数据 id=" + id);

        int rows = studentMapper.deleteById(id);
        System.out.println("deleteById 影响行数：" + rows);

        Student afterDelete = studentMapper.selectById(id);
        System.out.println("MP 查询结果（自动过滤 deleted=1）：" + afterDelete);

        // 用原生 JDBC 查看数据库中的真实情况：记录仍在，只是 deleted=1
        Connection conn = sqlSession.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT deleted FROM student WHERE id = ?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                Assert.assertTrue("记录应仍在数据库中", rs.next());
                int deleted = rs.getInt("deleted");
                System.out.println("原生 SQL 查询：id=" + id + " 的记录仍存在，deleted=" + deleted);
                Assert.assertEquals(1, deleted);
            }
        }
        Assert.assertEquals(1, rows);
        Assert.assertNull(afterDelete);
    }

    /** 7. 乐观锁：updateById 时 version 自动 +1，版本不一致则更新失败 */
    @Test
    public void testOptimisticLock() {
        System.out.println("========== 乐观锁 ==========");
        // 模拟两个人同时查到同一条记录
        Student s1 = studentMapper.selectById(2L);
        // 注意：同一个 SqlSession 内有一级缓存，再查一次会直接返回同一个对象，
        // 这里清空缓存，让 s2 成为数据库中真正独立的一份（模拟另一个用户）
        sqlSession.clearCache();
        Student s2 = studentMapper.selectById(2L);
        Integer oldVersion = s1.getVersion();
        System.out.println("更新前 version=" + oldVersion);

        // 第一个人修改：成功，version + 1
        s1.setAge(s1.getAge() + 1);
        int rows1 = studentMapper.updateById(s1);
        Integer newVersion = studentMapper.selectById(2L).getVersion();
        System.out.println("第一次更新影响行数：" + rows1 + "，更新后 version=" + newVersion);

        // 第二个人拿着旧 version 修改：失败，影响行数为 0
        s2.setAge(s2.getAge() + 10);
        int rows2 = studentMapper.updateById(s2);
        System.out.println("第二次（旧版本号）更新影响行数：" + rows2 + "（乐观锁生效，更新失败）");

        Assert.assertEquals(1, rows1);
        Assert.assertEquals(oldVersion + 1, newVersion.intValue());
        Assert.assertEquals(0, rows2);
    }
}
