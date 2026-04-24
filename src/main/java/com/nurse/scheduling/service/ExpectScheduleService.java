package com.nurse.scheduling.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nurse.scheduling.common.PageResult;
import com.nurse.scheduling.dto.expect.ExpectScheduleRequest;
import com.nurse.scheduling.dto.expect.ExpectScheduleResponse;
import com.nurse.scheduling.dto.swap.SwapRequestDto;
import com.nurse.scheduling.entity.ExpectSchedule;

import java.util.List;

/**
 * 申请服务接口（期望排班、换班等）
 *
 * @author nurse-scheduling
 */
public interface ExpectScheduleService extends IService<ExpectSchedule> {

    /**
     * 获取我的申请列表（分页）
     *
     * @param page 页码
     * @param pageSize 每页条数
     * @return 分页结果
     */
    PageResult<ExpectScheduleResponse> getMyExpectSchedule(int page, int pageSize);

    /**
     * 获取科室所有申请列表（分页）
     *
     * @param page 页码
     * @param pageSize 每页条数
     * @return 分页结果
     */
    PageResult<ExpectScheduleResponse> getExpectScheduleList(int page, int pageSize);

    /**
     * 提交期望排班
     *
     * @param request 提交请求
     * @return 期望排班信息
     */
    ExpectScheduleResponse submitExpectSchedule(ExpectScheduleRequest request);

    /**
     * 提交换班申请
     *
     * @param request 换班申请
     */
    void submitSwapRequest(SwapRequestDto request);

    /**
     * 审批申请
     *
     * @param id 申请ID
     * @param status 状态
     * @param approveRemark 审批意见
     */
    void approveExpectSchedule(String id, String status, String approveRemark);

    /**
     * 审批申请（重载方法，接收ApproveRequest对象）
     *
     * @param request 审批请求
     */
    default void approveExpectSchedule(com.nurse.scheduling.controller.ExpectScheduleController.ApproveRequest request) {
        approveExpectSchedule(request.getId(), request.getStatus(), request.getApproveRemark());
    }

    /**
     * 根据用户ID列表获取已审核通过的申请
     *
     * @param userIds 用户ID列表
     * @return 申请列表
     */
    List<ExpectScheduleResponse> getApprovedByUsers(List<String> userIds);

    /**
     * 获取待审批数量
     *
     * @return 待审批数量
     */
    int getPendingCount();

    /**
     * 取消申请
     *
     * @param id 申请ID
     */
    void cancelExpectSchedule(String id);
}
