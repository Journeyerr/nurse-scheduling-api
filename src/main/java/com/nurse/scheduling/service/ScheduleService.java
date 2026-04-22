package com.nurse.scheduling.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nurse.scheduling.dto.schedule.BatchScheduleRequest;
import com.nurse.scheduling.dto.schedule.ScheduleAddRequest;
import com.nurse.scheduling.dto.schedule.DailyScheduleVO;
import com.nurse.scheduling.dto.schedule.ScheduleResponse;
import com.nurse.scheduling.dto.swap.UserScheduleVO;
import com.nurse.scheduling.entity.Schedule;

import java.util.List;

/**
 * 排班服务接口
 *
 * @author nurse-scheduling
 */
public interface ScheduleService extends IService<Schedule> {

    /**
     * 获取月排班
     *
     * @param departmentId 科室ID
     * @param year 年份
     * @param month 月份
     * @return 排班列表
     */
    List<ScheduleResponse> getMonthlySchedule(String departmentId, Integer year, Integer month);

    /**
     * 获取周排班
     *
     * @param departmentId 科室ID
     * @param startDate 开始日期
     * @return 排班列表
     */
    List<ScheduleResponse> getWeeklySchedule(String departmentId, String startDate);

    /**
     * 添加排班
     *
     * @param departmentId 科室ID
     * @param request 添加请求
     * @return 排班信息
     */
    ScheduleResponse addSchedule(String departmentId, ScheduleAddRequest request);

    /**
     * 批量操作排班（添加和删除）
     *
     * @param departmentId 科室ID
     * @param request 批量请求
     * @return 成功添加的排班列表
     */
    List<ScheduleResponse> batchSchedule(String departmentId, BatchScheduleRequest request);

    /**
     * 更新排班
     *
     * @param request 更新请求
     * @return 排班信息
     */
    ScheduleResponse updateSchedule(ScheduleAddRequest request);

    /**
     * 删除排班
     *
     * @param scheduleId 排班ID
     */
    void deleteSchedule(String scheduleId);

    /**
     * 获取我的排班（支持多月查询）
     *
     * @param memberId 成员ID
     * @param yearMonths 年月列表，格式："2025-12,2026-1,2026-2"
     * @return 排班列表
     */
    List<ScheduleResponse> getMySchedule(String memberId, String yearMonths);

    /**
     * 获取指定日期排班的用户列表
     *
     * @param date 日期
     * @return 用户列表（id, nickName）
     */
    List<UserScheduleVO> getUsersByDate(String date);

    /**
     * 获取某天科室所有排班
     *
     * @param date 日期
     * @param departmentId 科室ID
     * @return 排班列表（含成员姓名和班次信息）
     */
    List<DailyScheduleVO> getDailySchedule(String date, String departmentId);
}

