package com.example;

import com.example.entity.User;
import com.example.mapper.UserMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

public class UserMapperTest {

    private InputStream is;
    private SqlSession session;
    private UserMapper userMapper;

    @Before
    public void init() throws Exception {
        is = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(is);
        session = factory.openSession(true);
        userMapper = session.getMapper(UserMapper.class);
    }

    @After
    public void destroy() throws Exception {
        if (session != null) {
            session.close();
        }
        if (is != null) {
            is.close();
        }
    }

    @Test
    public void testFindAll() {
        List<User> users = userMapper.findAll();
        for (User user : users) {
            System.out.println(user);
        }
    }

    @Test
    public void testFindById() {
        System.out.println(userMapper.findById(1));
    }

    @Test
    public void testAddUser() {
        User user = new User();
        user.setUsername("anno_" + System.currentTimeMillis());
        user.setPassword("123456");
        user.setEmail("anno_" + System.currentTimeMillis() + "@qq.com");
        int rows = userMapper.addUser(user);
        System.out.println("影响行数：" + rows);
        System.out.println("自增id：" + user.getId());
    }

    @Test
    public void testUpdateUser() {
        User user = userMapper.findById(2);
        user.setUsername("anno_update");
        user.setEmail("anno_update@qq.com");
        System.out.println("影响行数：" + userMapper.updateUser(user));
    }

    @Test
    public void testDeleteUser() {
        User user = new User();
        user.setUsername("anno_del_" + System.currentTimeMillis());
        user.setPassword("123456");
        user.setEmail("anno_del_" + System.currentTimeMillis() + "@qq.com");
        userMapper.addUser(user);
        System.out.println("影响行数：" + userMapper.deleteUser(user.getId()));
    }
}