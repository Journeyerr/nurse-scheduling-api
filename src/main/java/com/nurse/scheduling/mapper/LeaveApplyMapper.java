package com.nurse.scheduling.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nurse.scheduling.entity.LeaveApply;
import com.nurse.scheduling.dto.leave.LeaveApplyResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 假勤申请Mapper接口
 *
 * @author nurse-scheduling
 */
@Mapper
public interface LeaveApplyMapper extends BaseMapper<LeaveApply> {

    /**
     * 获取所有申请列表
     *
     * @param departmentId 科室ID
     * @param status 状态（可选）
     * @return 申请列表
     */
    List<LeaveApplyResponse> getLeaveApplyList(@Param("departmentId") Long departmentId,
                                                @Param("status") String status);

    /**
     * 获取我的申请列表
     *
     * @param userId 用户ID
     * @return 申请列表
     */
    List<LeaveApplyResponse> getMyLeaveApplyList(@Param("userId") Long userId);
}
