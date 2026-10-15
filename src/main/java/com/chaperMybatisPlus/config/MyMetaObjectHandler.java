package com.chaperMybatisPlus.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

/**
 * 自动填充处理器：配合实体类上的 @TableField(fill = ...) 使用。
 */
public class MyMetaObjectHandler implements MetaObjectHandler {

    /** 插入时填充 createTime 和 updateTime */
    @Override
    public void insertFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, now);
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, now);
    }

    /**
     * 更新时填充 updateTime。
     * 注意：strictUpdateFill 只在字段为 null 时才填充；
     * 先查询再更新的实体 updateTime 已有旧值，所以这里用 setFieldValByName 强制覆盖。
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
    }
}
