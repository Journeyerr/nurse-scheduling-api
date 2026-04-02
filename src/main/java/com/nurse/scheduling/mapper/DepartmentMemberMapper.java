package com.nurse.scheduling.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nurse.scheduling.entity.DepartmentMember;
import org.apache.ibatis.annotations.Mapper;

/**
 * 科室成员关联Mapper接口
 *
 * @author nurse-scheduling
 */
@Mapper
public interface DepartmentMemberMapper extends BaseMapper<DepartmentMember> {
}
