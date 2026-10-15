package com.chaperMybatisPlus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chaperMybatisPlus.entity.Student;

/**
 * 学生 Mapper：继承 BaseMapper 后即拥有通用 CRUD 方法，无需编写 XML。
 */
public interface StudentMapper extends BaseMapper<Student> {
}
