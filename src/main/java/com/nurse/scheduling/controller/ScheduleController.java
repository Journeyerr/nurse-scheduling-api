package com.nurse.scheduling.controller;

import com.nurse.scheduling.common.Result;
import com.nurse.scheduling.context.UserContext;
import com.nurse.scheduling.dto.schedule.BatchScheduleRequest;
import com.nurse.scheduling.dto.schedule.ScheduleAddRequest;
import com.nurse.scheduling.dto.schedule.ScheduleResponse;
import com.nurse.scheduling.service.ScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 排班控制器
 *
 * @author nurse-scheduling
 */
@Slf4j
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    /**
     * 获取月排班
     */
    @GetMapping("/monthly")
    public Result<List<ScheduleResponse>> getMonthlySchedule(@RequestParam String departmentId,
                                                              @RequestParam Integer year,
                                                              @RequestParam Integer month) {
        log.info("获取月排班，科室ID：{}，年份：{}，月份：{}", departmentId, year, month);
        List<ScheduleResponse> response = scheduleService.getMonthlySchedule(departmentId, year, month);
        return Result.ok(response);
    }

    /**
     * 获取周排班
     */
    @GetMapping("/weekly")
    public Result<List<ScheduleResponse>> getWeeklySchedule(@RequestParam String departmentId,
                                                             @RequestParam String startDate) {
        log.info("获取周排班，科室ID：{}，开始日期：{}", departmentId, startDate);
        List<ScheduleResponse> response = scheduleService.getWeeklySchedule(departmentId, startDate);
        return Result.ok(response);
    }

    /**
     * 添加排班
     */
    @PostMapping("/add")
    public Result<ScheduleResponse> addSchedule(@RequestParam String departmentId,
                                                 @Valid @RequestBody ScheduleAddRequest addRequest) {
        log.info("添加排班，科室ID：{}", departmentId);
        ScheduleResponse response = scheduleService.addSchedule(departmentId, addRequest);
        return Result.ok(response);
    }

    /**
     * 批量操作排班（添加和删除）
     */
    @PostMapping("/batch")
    public Result<List<ScheduleResponse>> batchSchedule(@RequestParam String departmentId,
                                                         @Valid @RequestBody BatchScheduleRequest batchRequest) {
        log.info("批量操作排班，科室ID：{}", departmentId);
        List<ScheduleResponse> response = scheduleService.batchSchedule(departmentId, batchRequest);
        return Result.ok(response);
    }

    /**
     * 更新排班
     */
    @PutMapping("/update")
    public Result<ScheduleResponse> updateSchedule(@Valid @RequestBody ScheduleAddRequest updateRequest) {
        log.info("更新排班，排班ID：{}", updateRequest.getId());
        ScheduleResponse response = scheduleService.updateSchedule(updateRequest);
        return Result.ok(response);
    }

    /**
     * 删除排班
     */
    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteSchedule(@PathVariable String id) {
        log.info("删除排班，排班ID：{}", id);
        scheduleService.deleteSchedule(id);
        return Result.ok();
    }

    /**
     * 获取我的排班（支持多月查询）
     * @param yearMonths 年月列表，逗号分隔，如 "2025-12,2026-1,2026-2"
     */
    @GetMapping("/my")
    public Result<List<ScheduleResponse>> getMySchedule(@RequestParam String yearMonths) {
        String userId = UserContext.getUserId();
        log.info("获取我的排班，用户ID：{}，年月：{}", userId, yearMonths);
        
        List<ScheduleResponse> response = scheduleService.getMySchedule(userId, yearMonths);
        return Result.ok(response);
    }
}
