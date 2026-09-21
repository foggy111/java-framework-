package com.example.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Order entity mapped to table `orders`.
 */
public class Order {
    private Integer id;
    private Integer userId;
    private String productName;
    private BigDecimal amount;
    private Date createdAt;

    /** Nested user for join queries (findOrdersWithUser). */
    private User user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", userId=" + userId +
                ", productName='" + productName + '\'' +
                ", amount=" + amount +
                ", createdAt=" + createdAt +
                ", user=" + user +
                '}';
    }
}
