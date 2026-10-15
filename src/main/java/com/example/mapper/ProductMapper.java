package com.example.mapper;

import com.example.entity.Product;

import java.util.List;

/**
 * 问题1 (XML 方式): 哪个产品被哪个客户买走了, 由哪些供应商提供
 * SQL 与 resultMap 写在 resources/mapper/ProductMapper.xml 中
 */
public interface ProductMapper {

    /** 查询所有产品, 同时带出购买客户(association)和供应商列表(collection) */
    List<Product> findAllWithCustomerAndSuppliers();
}
