package com.example.mapper;

import com.example.entity.Customer;
import com.example.entity.Product;
import com.example.entity.Supplier;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 问题2 (注解方式): 查每个供应商卖了多少产品, 分别被哪些客户买走了
 * 全部使用注解, 不需要 XML
 */
public interface SupplierMapper {

    /**
     * 查询所有供应商:
     *   productCount : COUNT 子查询统计已卖出(customer_id 不为空)的产品数量
     *   products     : @Many 嵌套查询, 该供应商已卖出的产品(每个产品再用 @One 带出购买客户)
     *   customers    : @Many 嵌套查询, 买过该供应商产品的客户(DISTINCT 去重)
     */
    @Select("SELECT s.id, s.name, s.phone, "
            + "       (SELECT COUNT(*) "
            + "          FROM product_supplier ps "
            + "          JOIN product p ON p.id = ps.product_id "
            + "         WHERE ps.supplier_id = s.id "
            + "           AND p.customer_id IS NOT NULL) AS product_count "
            + "FROM supplier s "
            + "ORDER BY s.id")
    @Results(id = "supplierSalesMap", value = {
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "phone", column = "phone"),
            @Result(property = "productCount", column = "product_count"),
            @Result(property = "products", column = "id", javaType = List.class,
                    many = @Many(select = "findSoldProductsBySupplierId")),
            @Result(property = "customers", column = "id", javaType = List.class,
                    many = @Many(select = "findCustomersBySupplierId"))
    })
    List<Supplier> findAllSupplierSales();

    /** 某供应商已卖出的产品, 每个产品带出购买它的客户 */
    @Select("SELECT p.id, p.name, p.price, p.customer_id "
            + "FROM product p "
            + "JOIN product_supplier ps ON ps.product_id = p.id "
            + "WHERE ps.supplier_id = #{supplierId} "
            + "  AND p.customer_id IS NOT NULL "
            + "ORDER BY p.id")
    @Results(id = "soldProductMap", value = {
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "price", column = "price"),
            @Result(property = "customerId", column = "customer_id"),
            @Result(property = "customer", column = "customer_id", javaType = Customer.class,
                    one = @One(select = "findCustomerById"))
    })
    List<Product> findSoldProductsBySupplierId(Integer supplierId);

    /** 买过某供应商产品的客户 (去重) */
    @Select("SELECT DISTINCT c.id, c.name, c.phone "
            + "FROM customer c "
            + "JOIN product p           ON p.customer_id = c.id "
            + "JOIN product_supplier ps ON ps.product_id = p.id "
            + "WHERE ps.supplier_id = #{supplierId} "
            + "ORDER BY c.id")
    List<Customer> findCustomersBySupplierId(Integer supplierId);

    /** 根据 id 查客户 */
    @Select("SELECT id, name, phone FROM customer WHERE id = #{id}")
    Customer findCustomerById(Integer id);
}
