package com.example;

import com.example.entity.User;
import com.example.mapper.UserMapperAnnotation;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

public class UserMapperAnnotationTest {

    private SqlSessionFactory sqlSessionFactory;

    @Before
    public void init() throws Exception {
        InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
    }

    @Test
    public void testFindAll() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapperAnnotation userMapper = sqlSession.getMapper(UserMapperAnnotation.class);
        List<User> users = userMapper.findAll();
        for (User user : users) {
            System.out.println(user);
        }
        sqlSession.close();
    }

    @Test
    public void testFindById() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapperAnnotation userMapper = sqlSession.getMapper(UserMapperAnnotation.class);
        System.out.println(userMapper.findById(1));
        sqlSession.close();
    }
}