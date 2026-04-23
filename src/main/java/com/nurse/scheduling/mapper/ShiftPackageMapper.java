package com.nurse.scheduling.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nurse.scheduling.entity.ShiftPackage;
import org.apache.ibatis.annotations.Mapper;

/**
 * 班种套班Mapper
 *
 * @author nurse-scheduling
 */
@Mapper
public interface ShiftPackageMapper extends BaseMapper<ShiftPackage> {
}
