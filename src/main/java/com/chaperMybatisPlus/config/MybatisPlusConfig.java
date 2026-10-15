package com.chaperMybatisPlus.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.session.Configuration;

/**
 * MyBatis-Plus 插件配置（非 Spring 环境，手动注册）。
 */
public class MybatisPlusConfig {

    private MybatisPlusConfig() {
    }

    /**
     * 创建 MyBatis-Plus 拦截器，并添加内置插件：
     * 1. PaginationInnerInterceptor     分页插件（MySQL 方言，生成 LIMIT）
     * 2. OptimisticLockerInnerInterceptor 乐观锁插件（配合 @Version）
     * 3. BlockAttackInnerInterceptor    防止全表更新/删除
     */
    public static MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        return interceptor;
    }

    /**
     * 把拦截器注册到 MyBatis 的 Configuration 上。
     * 必须在 openSession() 之前调用。
     */
    public static void registerInterceptor(Configuration configuration) {
        configuration.addInterceptor(mybatisPlusInterceptor());
    }
}
