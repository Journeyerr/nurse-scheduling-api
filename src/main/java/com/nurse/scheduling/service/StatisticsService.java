package com.nurse.scheduling.service;

import com.nurse.scheduling.dto.statistics.DepartmentStatisticsResponse;
import com.nurse.scheduling.dto.statistics.MyStatisticsResponse;

/**
 * 统计服务接口
 *
 * @author nurse-scheduling
 */
public interface StatisticsService {

    /**
     * 获取我的排班统计
     *
     * @param memberId 成员ID
     * @param year 年份
     * @param month 月份
     * @return 我的统计信息
     */
    MyStatisticsResponse getMyStatistics(String memberId, Integer year, Integer month);

    /**
     * 获取科室排班统计
     *
     * @param departmentId 科室ID
     * @param year 年份
     * @param month 月份
     * @return 科室统计信息
     */
    DepartmentStatisticsResponse getDepartmentStatistics(String departmentId, Integer year, Integer month);
}
