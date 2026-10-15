package com.example;

import com.example.entity.Order;
import com.example.entity.User;
import com.example.mapper.OrderMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

/**
 * JUnit 4 tests for OrderMapper multi-table queries.
 * Mirrors typical UserMapperTest session setup.
 */
public class OrderMapperTest {

    private SqlSessionFactory sqlSessionFactory;
    private SqlSession sqlSession;
    private OrderMapper orderMapper;

    @Before
    public void setUp() throws Exception {
        InputStream in = Resources.getResourceAsStream("mybatis-config.xml");
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(in);
        sqlSession = sqlSessionFactory.openSession(true);
        orderMapper = sqlSession.getMapper(OrderMapper.class);
    }

    public void tearDown() {
        if (sqlSession != null) {
            sqlSession.close();
        }
    }

    @Test
    public void testFindAll() {
        List<Order> list = orderMapper.findAll();
        for (Order order : list) {
            System.out.println(order);
        }
    }

    @Test
    public void testFindById() {
        Order order = orderMapper.findById(1);
        System.out.println(order);
    }

    @Test
    public void testInsert() {
        Order order = new Order();
        order.setUserId(1);
        order.setProductName("Test Product");
        order.setAmount(new BigDecimal("12.34"));
        int rows = orderMapper.insert(order);
        System.out.println("insert rows=" + rows + ", generated id=" + order.getId());
    }

    @Test
    public void testFindOrdersWithUser() {
        System.out.println("===== findOrdersWithUser =====");
        List<Order> list = orderMapper.findOrdersWithUser();
        for (Order order : list) {
            System.out.println(order);
        }
    }

    @Test
    public void testFindUserWithOrders() {
        System.out.println("===== findUserWithOrders(1) =====");
        User user = orderMapper.findUserWithOrders(1);
        System.out.println(user);
        if (user != null && user.getOrders() != null) {
            for (Order order : user.getOrders()) {
                System.out.println("  order -> " + order);
            }
        }
    }

    @Test
    public void testFindAllUsersWithOrders() {
        System.out.println("===== findAllUsersWithOrders =====");
        List<User> users = orderMapper.findAllUsersWithOrders();
        for (User user : users) {
            System.out.println(user);
            if (user.getOrders() != null) {
                for (Order order : user.getOrders()) {
                    System.out.println("  order -> " + order);
                }
            }
        }
    }
}
