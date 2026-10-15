package com.example.entity;

import java.util.List;

/**
 * 客户实体: 一个客户可以买走多个产品 (customer -> product 一对多)
 */
public class Customer {
    private Integer id;
    private String name;
    private String phone;
    /** 该客户买走的产品 */
    private List<Product> products;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> products) { this.products = products; }

    @Override
    public String toString() {
        // 不输出 products, 避免对象之间互相引用造成死循环
        return "Customer{id=" + id + ", name='" + name + "', phone='" + phone + "'}";
    }
}
