package com.nurse.scheduling.controller;

import com.nurse.scheduling.common.Result;
import com.nurse.scheduling.dto.expect.ExpectScheduleRequest;
import com.nurse.scheduling.dto.expect.ExpectScheduleResponse;
import com.nurse.scheduling.service.ExpectScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 期望排班控制器
 *
 * @author nurse-scheduling
 */
@Slf4j
@RestController
@RequestMapping("/expect")
public class ExpectScheduleController {

    @Autowired
    private ExpectScheduleService expectScheduleService;

    /**
     * 获取我的期望排班列表
     */
    @GetMapping("/my")
    public Result<List<ExpectScheduleResponse>> getMyExpectSchedule() {
        log.info("获取我的期望排班列表");
        List<ExpectScheduleResponse> response = expectScheduleService.getMyExpectSchedule();
        return Result.ok(response);
    }

    /**
     * 获取科室所有期望排班列表
     */
    @GetMapping("/list")
    public Result<List<ExpectScheduleResponse>> getExpectScheduleList() {
        log.info("获取科室所有期望排班列表");
        List<ExpectScheduleResponse> response = expectScheduleService.getExpectScheduleList();
        return Result.ok(response);
    }

    /**
     * 提交期望排班
     */
    @PostMapping("/submit")
    public Result<ExpectScheduleResponse> submitExpectSchedule(@Valid @RequestBody ExpectScheduleRequest request) {
        log.info("提交期望排班：{}", request);
        ExpectScheduleResponse response = expectScheduleService.submitExpectSchedule(request);
        return Result.ok(response);
    }

    /**
     * 审批期望排班
     */
    @PostMapping("/approve")
    public Result<Void> approveExpectSchedule(@RequestBody ApproveRequest request) {
        log.info("审批期望排班：{}", request);
        expectScheduleService.approveExpectSchedule(request);
        return Result.ok();
    }

    /**
     * 审批请求DTO
     */
    @lombok.Data
    public static class ApproveRequest {
        private String id;
        private String status;
        private String approveRemark;
    }

    /**
     * 根据用户ID列表获取已审核通过的期望排班
     */
    @PostMapping("/approved-by-users")
    public Result<List<ExpectScheduleResponse>> getApprovedByUsers(@RequestBody List<String> userIds) {
        log.info("获取用户列表的已审核期望排班，用户数量：{}", userIds.size());
        List<ExpectScheduleResponse> response = expectScheduleService.getApprovedByUsers(userIds);
        return Result.ok(response);
    }

    /**
     * 获取待审批数量
     */
    @GetMapping("/pending-count")
    public Result<Integer> getPendingCount() {
        log.info("获取待审批数量");
        int count = expectScheduleService.getPendingCount();
        return Result.ok(count);
    }
}
