package com.example;

import com.example.entity.User;
import com.example.mapper.UserMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.util.List;

public class MybatisTest {
    public static void main(String[] args) throws Exception {
        SqlSession session = new SqlSessionFactoryBuilder()
                .build(Resources.getResourceAsStream("mybatis-config.xml"))
                .openSession(true);
        Integer count = session.selectOne("com.example.mapper.UserMapper.countUser");
        Integer maxId = session.selectOne("com.example.mapper.UserMapper.maxUserId");
        Integer minId = session.selectOne("com.example.mapper.UserMapper.minUserId");
        System.out.println("COUNT(*) = " + count);
        System.out.println("MAX(id) = " + maxId);
        System.out.println("MIN(id) = " + minId);
        UserMapper mapper = session.getMapper(UserMapper.class);

        List<User> users = mapper.selectAll();
        for (User user : users) {
            System.out.println(user);
        }
        System.out.println(mapper.findById(1));

        session.close();
    }
}