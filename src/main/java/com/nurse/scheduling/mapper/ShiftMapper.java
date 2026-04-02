package com.nurse.scheduling.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nurse.scheduling.entity.Shift;
import org.apache.ibatis.annotations.Mapper;

/**
 * 班种Mapper接口
 *
 * @author nurse-scheduling
 */
@Mapper
public interface ShiftMapper extends BaseMapper<Shift> {
}
