package com.lab.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;

/**
 * 原生 MyBatis 工具类：加载独立配置 mybatis-lab-config.xml（库 ssm_emp）。
 */
public final class MyBatisUtil {

    private static final SqlSessionFactory FACTORY;

    static {
        try (InputStream in = Resources.getResourceAsStream("mybatis-lab-config.xml")) {
            FACTORY = new SqlSessionFactoryBuilder().build(in);
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private MyBatisUtil() {
    }

    public static SqlSessionFactory getSqlSessionFactory() {
        return FACTORY;
    }

    /** 默认手动提交 */
    public static SqlSession openSession() {
        return FACTORY.openSession();
    }

    public static SqlSession openSession(boolean autoCommit) {
        return FACTORY.openSession(autoCommit);
    }
}
