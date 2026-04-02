package com.nurse.scheduling.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nurse.scheduling.entity.Department;
import org.apache.ibatis.annotations.Mapper;

/**
 * 科室Mapper接口
 *
 * @author nurse-scheduling
 */
@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {
}
