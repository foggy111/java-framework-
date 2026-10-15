package com.example.entity;

import java.util.List;

/**
 * 产品实体:
 *   product  -> customer : 多对一 (customer_id = 买走该产品的客户)
 *   product <-> supplier : 多对多 (中间表 product_supplier)
 */
public class Product {
    private Integer id;
    private String name;
    private Double price;
    private Integer customerId;
    /** 买走该产品的客户 (association) */
    private Customer customer;
    /** 提供该产品的供应商 (collection) */
    private List<Supplier> suppliers;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public List<Supplier> getSuppliers() { return suppliers; }
    public void setSuppliers(List<Supplier> suppliers) { this.suppliers = suppliers; }

    @Override
    public String toString() {
        // 只输出客户名, 不输出 suppliers 集合, 避免死循环
        return "Product{id=" + id + ", name='" + name + "', price=" + price
                + ", customerId=" + customerId
                + ", customer=" + (customer == null ? null : customer.getName()) + "}";
    }
}
