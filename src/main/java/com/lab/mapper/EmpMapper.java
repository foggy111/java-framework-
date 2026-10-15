package com.lab.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lab.entity.Emp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 员工 Mapper：继承 BaseMapper 获得通用 CRUD（insert/selectById/deleteById 等），
 * 自定义方法由 EmpMapper.xml 提供；同名 XML 会覆盖 BaseMapper 注入的 SQL。
 */
public interface EmpMapper extends BaseMapper<Emp> {

    List<Emp> selectAll();

    int update(Emp emp);

    List<Emp> selectByCondition(Emp emp);

    int updateDynamic(Emp emp);

    int deleteBatch(@Param("ids") List<Integer> ids);

    int insertBatch(@Param("list") List<Emp> list);

    List<Emp> selectByChoose(Emp emp);
}
