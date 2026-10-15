package com.example;

import com.example.entity.Customer;
import com.example.entity.Product;
import com.example.entity.Supplier;
import com.example.mapper.ProductMapper;
import com.example.mapper.SupplierMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 产品 / 供应商 / 客户 多表查询作业
 *   问题1 (XML 方式)  : 哪个产品被哪个客户买走了, 哪个供应商提供
 *   问题2 (注解方式)  : 每个供应商卖了多少产品, 分别被哪些客户买走了
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) // 按方法名排序: 先问题1, 后问题2
public class ProductSupplierCustomerTest {

    private static final String LINE  = "==============================================================================";
    private static final String SLINE = "------------------------------------------------------------------------------";

    private SqlSession sqlSession;

    @Before
    public void setUp() throws IOException {
        InputStream in = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(in);
        sqlSession = factory.openSession(true); // 自动提交
    }

    @After
    public void tearDown() {
        if (sqlSession != null) {
            sqlSession.close();
        }
    }

    /** 问题1 (XML 方式): 哪个产品被哪个客户买走了, 哪个供应商提供 */
    @Test
    public void testProductCustomerSupplierByXml() {
        ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
        List<Product> products = mapper.findAllWithCustomerAndSuppliers();
        Assert.assertNotNull(products);
        Assert.assertFalse(products.isEmpty());

        System.out.println();
        System.out.println(LINE);
        System.out.println("  问题1 (XML 方式): 哪个产品被哪个客户买走了, 哪个供应商提供");
        System.out.println(LINE);
        System.out.println(pad("编号", 6) + pad("产品", 14) + pad("价格(元)", 12)
                + pad("购买客户", 12) + "供应商");
        System.out.println(SLINE);

        for (Product p : products) {
            String customerName = p.getCustomer() == null ? "(未售出)" : p.getCustomer().getName();

            List<String> supplierNames = new ArrayList<String>();
            if (p.getSuppliers() != null) {
                for (Supplier s : p.getSuppliers()) {
                    supplierNames.add(s.getName());
                }
            }
            String suppliers = supplierNames.isEmpty() ? "(无)" : join(supplierNames, ", ");

            System.out.println(pad(String.valueOf(p.getId()), 6)
                    + pad(p.getName(), 14)
                    + pad(String.format("%.2f", p.getPrice()), 12)
                    + pad(customerName, 12)
                    + suppliers);
        }
        System.out.println(SLINE);
        System.out.println("  共 " + products.size() + " 个产品");
        System.out.println(LINE);
        System.out.println();
    }

    /** 问题2 (注解方式): 查每个供应商卖了多少产品, 分别被哪些客户买走了 */
    @Test
    public void testSupplierSalesByAnnotation() {
        SupplierMapper mapper = sqlSession.getMapper(SupplierMapper.class);
        List<Supplier> suppliers = mapper.findAllSupplierSales();
        Assert.assertNotNull(suppliers);
        Assert.assertFalse(suppliers.isEmpty());

        System.out.println();
        System.out.println(LINE);
        System.out.println("  问题2 (注解方式): 每个供应商卖了多少产品, 分别被哪些客户买走了");
        System.out.println(LINE);

        for (Supplier s : suppliers) {
            // COUNT 子查询得到的数量应与嵌套查询出来的产品数一致
            Assert.assertEquals(s.getProductCount().intValue(), s.getProducts().size());

            System.out.println("【" + s.getName() + "】 电话: " + s.getPhone()
                    + "   共卖出 " + s.getProductCount() + " 件产品");

            if (s.getProducts() == null || s.getProducts().isEmpty()) {
                System.out.println("    (暂无卖出的产品)");
            } else {
                for (Product p : s.getProducts()) {
                    String customerName = p.getCustomer() == null ? "(未知)" : p.getCustomer().getName();
                    System.out.println("    " + pad(p.getName(), 14) + " -> " + customerName);
                }
            }

            List<String> customerNames = new ArrayList<String>();
            if (s.getCustomers() != null) {
                for (Customer c : s.getCustomers()) {
                    customerNames.add(c.getName());
                }
            }
            System.out.println("    购买客户(" + customerNames.size() + " 人): "
                    + (customerNames.isEmpty() ? "(无)" : join(customerNames, ", ")));
            System.out.println(SLINE);
        }
        System.out.println("  共 " + suppliers.size() + " 个供应商");
        System.out.println(LINE);
        System.out.println();
    }

    // ------------------------------------------------------------------
    // 工具方法: 控制台里一个中文占 2 个英文字符宽度, String.format("%-10s")
    // 按字符个数补空格会导致中文列对不齐, 所以按"显示宽度"补空格
    // ------------------------------------------------------------------

    /** 右侧补空格到指定显示宽度 */
    private static String pad(String s, int width) {
        if (s == null) {
            s = "";
        }
        StringBuilder sb = new StringBuilder(s);
        for (int w = displayWidth(s); w < width; w++) {
            sb.append(' ');
        }
        return sb.toString();
    }

    /** 计算显示宽度: 中文/全角字符算 2, 其他算 1 */
    private static int displayWidth(String s) {
        int width = 0;
        for (char ch : s.toCharArray()) {
            width += (ch >= 0x2E80) ? 2 : 1;
        }
        return width;
    }

    private static String join(List<String> list, String sep) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) {
                sb.append(sep);
            }
            sb.append(list.get(i));
        }
        return sb.toString();
    }
}
