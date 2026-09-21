package com.example.entity;

import java.util.Date;
import java.util.List;

public class User {

    private Integer id;
    private String username;
    private String password;
    private String email;
    private Date createdAt;
    private Date updatedAt;
    private List<Order> orders;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
    public Date getCreateTime() { return createdAt; }
    public void setCreateTime(Date createTime) { this.createdAt = createTime; }
    public Date getUpdateTime() { return updatedAt; }
    public void setUpdateTime(Date updateTime) { this.updatedAt = updateTime; }
    public List<Order> getOrders() { return orders; }
    public void setOrders(List<Order> orders) { this.orders = orders; }


    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "', email='" + email + "'}";
    }
}