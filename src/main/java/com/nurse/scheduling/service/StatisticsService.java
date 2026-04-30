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
     * @param departmentId 科室ID（用于读取存欠班）
     * @return 我的统计信息
     */
    MyStatisticsResponse getMyStatistics(String memberId, Integer year, Integer month, String departmentId);

    /**
     * 获取科室排班统计
     *
     * @param departmentId 科室ID
     * @param year 年份
     * @param month 月份
     * @return 科室统计信息
     */
    DepartmentStatisticsResponse getDepartmentStatistics(String departmentId, Integer year, Integer month);

    /**
     * 重算存欠班并保存到 DepartmentMember
     * 排班变更时调用，从加入月份逐月累加到当前月
     *
     * @param userId       用户ID
     * @param departmentId 科室ID
     */
    void recalculateAndSaveBalanceDays(Long userId, Long departmentId);
}
