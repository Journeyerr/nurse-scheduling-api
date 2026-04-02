package com.nurse.scheduling.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nurse.scheduling.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper接口
 *
 * @author nurse-scheduling
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
