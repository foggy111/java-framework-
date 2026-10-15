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
import java.util.List;

/**
 * 单独类：专门运行 user / orders 关联查询（对照课件 Chapter05 关联映射）。
 *
 * 多对一：多个订单属于一个用户 -> association（Order.user）
 * 一对多：一个用户有多个订单 -> collection（User.orders）
 */
public class UserOrderAssociationTest {

    private SqlSession sqlSession;
    private OrderMapper orderMapper;

    @Before
    public void init() throws Exception {
        InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(is);
        sqlSession = factory.openSession(true);
        orderMapper = sqlSession.getMapper(OrderMapper.class);
    }

    @After
    public void destroy() {
        if (sqlSession != null) {
            sqlSession.close();
        }
    }

    /**
     * 多对一（XML + association）：订单 LEFT JOIN 用户
     */
    @Test
    public void testManyToOne_OrderWithUser() {
        System.out.println("========== 多对一：订单关联用户（association）==========");
        System.out.println("知识点：JOIN + resultMap association 嵌套 User");

        List<Order> orders = orderMapper.findOrdersWithUser();
        System.out.println("订单总数：" + orders.size());
        for (Order order : orders) {
            String username = (order.getUser() != null) ? order.getUser().getUsername() : "无";
            System.out.println("  订单#" + order.getId()
                    + " " + order.getProductName()
                    + " 金额=" + order.getAmount()
                    + " -> 用户=" + username);
        }
    }

    /**
     * 一对多（XML + collection）：用户 LEFT JOIN 订单列表
     */
    @Test
    public void testOneToMany_UserWithOrders() {
        System.out.println("========== 一对多：用户关联订单列表（collection）==========");
        System.out.println("知识点：JOIN + resultMap collection ofType=Order");

        User user = orderMapper.findUserWithOrders(1);
        if (user == null) {
            System.out.println("未找到 id=1 的用户，请先执行 sql/orders.sql 并确保 user 表有数据");
            return;
        }
        System.out.println("用户：" + user.getUsername() + " (id=" + user.getId() + ")");
        int count = (user.getOrders() != null) ? user.getOrders().size() : 0;
        System.out.println("订单数量：" + count);
        if (user.getOrders() != null) {
            for (Order order : user.getOrders()) {
                System.out.println("  - #" + order.getId()
                        + " " + order.getProductName()
                        + " 金额=" + order.getAmount());
            }
        }
    }

    /**
     * 一对多批量：查出所有用户及其订单
     */
    @Test
    public void testOneToMany_AllUsersWithOrders() {
        System.out.println("========== 一对多：全部用户及其订单 ==========");

        List<User> users = orderMapper.findAllUsersWithOrders();
        System.out.println("用户总数：" + users.size());
        for (User user : users) {
            int count = (user.getOrders() != null) ? user.getOrders().size() : 0;
            System.out.println("用户 " + user.getUsername() + " 订单数=" + count);
            if (user.getOrders() != null) {
                for (Order order : user.getOrders()) {
                    System.out.println("    -> " + order.getProductName() + " " + order.getAmount());
                }
            }
        }
    }
}
