package com.example.entity;

import java.util.List;

/**
 * 供应商实体:
 *   supplier <-> product  多对多 (中间表 product_supplier)
 *   supplier <-> customer 多对多 (中间表 supplier_customer)
 */
public class Supplier {
    private Integer id;
    private String name;
    private String phone;
    /** 卖出的产品数量 (SQL 中用 COUNT 子查询统计) */
    private Integer productCount;
    /** 该供应商供应(并已卖出)的产品 */
    private List<Product> products;
    /** 买过该供应商产品的客户 (去重) */
    private List<Customer> customers;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public Integer getProductCount() { return productCount; }
    public void setProductCount(Integer productCount) { this.productCount = productCount; }

    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> products) { this.products = products; }

    public List<Customer> getCustomers() { return customers; }
    public void setCustomers(List<Customer> customers) { this.customers = customers; }

    @Override
    public String toString() {
        // 不输出 products / customers 集合, 避免死循环
        return "Supplier{id=" + id + ", name='" + name + "', phone='" + phone
                + "', productCount=" + productCount + "}";
    }
}
