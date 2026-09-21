package com.example.mapper;

import com.example.entity.Order;
import com.example.entity.User;

import java.util.List;

/**
 * MyBatis mapper for `orders` and multi-table queries with `user`.
 */
public interface OrderMapper {

    List<Order> findAll();

    Order findById(int id);

    int insert(Order order);

    /** JOIN: each Order with nested User info. */
    List<Order> findOrdersWithUser();

    /** One user + collection of orders (resultMap collection). */
    User findUserWithOrders(int userId);

    /** All users with their orders. */
    List<User> findAllUsersWithOrders();
}
