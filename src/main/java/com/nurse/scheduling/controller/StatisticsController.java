package com.nurse.scheduling.controller;

import com.nurse.scheduling.common.Result;
import com.nurse.scheduling.context.UserContext;
import com.nurse.scheduling.dto.statistics.DepartmentStatisticsResponse;
import com.nurse.scheduling.dto.statistics.MyStatisticsResponse;
import com.nurse.scheduling.service.StatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 统计控制器
 *
 * @author nurse-scheduling
 */
@Slf4j
@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    /**
     * 获取我的排班统计
     *
     * @param year 年份
     * @param month 月份
     * @param departmentId 科室ID（用于计算累计存欠班）
     * @return 我的统计信息
     */
    @GetMapping("/my")
    public Result<MyStatisticsResponse> getMyStatistics(@RequestParam Integer year,
                                                         @RequestParam Integer month,
                                                         @RequestParam(required = false) String departmentId) {
        String userId = UserContext.getUserId();
        log.info("获取我的排班统计，用户ID：{}，年份：{}，月份：{}，科室ID：{}", userId, year, month, departmentId);

        MyStatisticsResponse response = statisticsService.getMyStatistics(userId, year, month, departmentId);
        return Result.ok(response);
    }

    /**
     * 获取科室排班统计
     *
     * @param departmentId 科室ID
     * @param year 年份
     * @param month 月份
     * @return 科室统计信息
     */
    @GetMapping("/department")
    public Result<DepartmentStatisticsResponse> getDepartmentStatistics(@RequestParam String departmentId,
                                                                          @RequestParam Integer year,
                                                                          @RequestParam Integer month) {
        log.info("获取科室排班统计，科室ID：{}，年份：{}，月份：{}", departmentId, year, month);

        DepartmentStatisticsResponse response = statisticsService.getDepartmentStatistics(departmentId, year, month);
        return Result.ok(response);
    }
}
