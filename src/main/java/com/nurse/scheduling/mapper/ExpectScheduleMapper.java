package com.nurse.scheduling.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nurse.scheduling.entity.ExpectSchedule;
import com.nurse.scheduling.dto.expect.ExpectScheduleResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 期望排班Mapper接口
 *
 * @author nurse-scheduling
 */
@Mapper
public interface ExpectScheduleMapper extends BaseMapper<ExpectSchedule> {

    /**
     * 获取所有期望列表（分页）
     *
     * @param departmentId 科室ID
     * @param limit 每页条数
     * @param offset 偏移量
     * @return 期望列表
     */
    List<ExpectScheduleResponse> getExpectScheduleList(@Param("departmentId") Long departmentId, @Param("limit") int limit, @Param("offset") int offset);

    /**
     * 获取科室期望排班总数
     */
    int getExpectScheduleCount(@Param("departmentId") Long departmentId);

    /**
     * 获取我的期望列表（分页）
     *
     * @param userId 用户ID
     * @param limit 每页条数
     * @param offset 偏移量
     * @return 期望列表
     */
    List<ExpectScheduleResponse> getMyExpectSchedule(@Param("userId") Long userId, @Param("limit") int limit, @Param("offset") int offset);

    /**
     * 获取我的期望排班总数
     */
    int getMyExpectScheduleCount(@Param("userId") Long userId);

    /**
     * 根据用户ID列表获取已审核通过的期望排班
     *
     * @param userIds 用户ID列表
     * @return 期望列表
     */
    List<ExpectScheduleResponse> getApprovedByUsers(@Param("userIds") List<Long> userIds);

    /**
     * 获取科室待审批数量
     *
     * @param departmentId 科室ID
     * @return 待审批数量
     */
    int getPendingCount(@Param("departmentId") Long departmentId);
}
