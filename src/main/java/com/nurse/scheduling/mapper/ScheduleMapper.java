package com.nurse.scheduling.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nurse.scheduling.entity.Schedule;
import com.nurse.scheduling.dto.schedule.ScheduleResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 排班Mapper接口
 *
 * @author nurse-scheduling
 */
@Mapper
public interface ScheduleMapper extends BaseMapper<Schedule> {

    /**
     * 获取月排班
     *
     * @param departmentId 科室ID
     * @param year 年份
     * @param month 月份
     * @return 排班列表
     */
    List<ScheduleResponse> getMonthlySchedule(@Param("departmentId") Long departmentId,
                                               @Param("year") Integer year,
                                               @Param("month") Integer month);

    /**
     * 获取周排班
     *
     * @param departmentId 科室ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 排班列表
     */
    List<ScheduleResponse> getWeeklySchedule(@Param("departmentId") Long departmentId,
                                              @Param("startDate") LocalDate startDate,
                                              @Param("endDate") LocalDate endDate);

    /**
     * 获取我的排班
     *
     * @param memberId 成员ID
     * @param year 年份
     * @param month 月份
     * @return 排班列表
     */
    List<ScheduleResponse> getMySchedule(@Param("memberId") Long memberId,
                                          @Param("year") Integer year,
                                          @Param("month") Integer month);

    /**
     * 获取成员在某日期的排班
     *
     * @param memberId 成员ID
     * @param date 日期
     * @return 排班列表
     */
    List<ScheduleResponse> getMemberScheduleByDate(@Param("memberId") Long memberId,
                                                     @Param("date") LocalDate date);

    /**
     * 获取指定日期排班的用户列表
     *
     * @param date 日期
     * @return 用户列表（id, nickName）
     */
    List<java.util.Map<String, Object>> getUsersByDate(@Param("date") LocalDate date);
}
