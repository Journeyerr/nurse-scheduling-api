package com.nurse.scheduling.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nurse.scheduling.common.HolidayConstants;
import com.nurse.scheduling.dto.statistics.DepartmentStatisticsResponse;
import com.nurse.scheduling.dto.statistics.MyStatisticsResponse;
import com.nurse.scheduling.entity.DepartmentMember;
import com.nurse.scheduling.entity.Schedule;
import com.nurse.scheduling.entity.Shift;
import com.nurse.scheduling.entity.User;
import com.nurse.scheduling.mapper.ScheduleMapper;
import com.nurse.scheduling.service.DepartmentMemberService;
import com.nurse.scheduling.service.ShiftService;
import com.nurse.scheduling.service.StatisticsService;
import com.nurse.scheduling.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 统计服务实现类
 *
 * @author nurse-scheduling
 */
@Slf4j
@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private ScheduleMapper scheduleMapper;

    @Autowired
    private DepartmentMemberService departmentMemberService;

    @Autowired
    private ShiftService shiftService;

    @Autowired
    private UserService userService;

    @Override
    public MyStatisticsResponse getMyStatistics(String memberId, Integer year, Integer month, String departmentId) {
        MyStatisticsResponse response = new MyStatisticsResponse();

        // 查询该成员当月的所有排班
        LambdaQueryWrapper<Schedule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Schedule::getMemberId, Long.parseLong(memberId))
                .apply("YEAR(date) = {0}", year)
                .apply("MONTH(date) = {0}", month);
        if (departmentId != null) {
            queryWrapper.eq(Schedule::getDepartmentId, Long.parseLong(departmentId));
        }

        List<Schedule> schedules = scheduleMapper.selectList(queryWrapper);

        // 统计各班次数量
        Map<Long, Long> shiftCountMap = schedules.stream()
                .collect(Collectors.groupingBy(Schedule::getShiftId, Collectors.counting()));

        // 获取所有班种
        List<Shift> shifts = shiftService.list();
        Map<Long, Shift> shiftMap = shifts.stream()
                .collect(Collectors.toMap(Shift::getId, shift -> shift));

        // 构建班次详情
        List<MyStatisticsResponse.ShiftDetailDTO> shiftDetails = new ArrayList<>();
        int totalHours = 0;
        int restDays = 0;
        int workDays = 0;
        double coefficientSum = 0.0;

        for (Shift shift : shifts) {
            Long count = shiftCountMap.getOrDefault(shift.getId(), 0L);

            MyStatisticsResponse.ShiftDetailDTO detail = new MyStatisticsResponse.ShiftDetailDTO();
            detail.setName(shift.getName());
            detail.setCode(shift.getCode());
            detail.setColor(shift.getColor());
            detail.setCount(count.intValue());
            detail.setCoefficient(shift.getCoefficient() != null ? shift.getCoefficient() : 1.0);

            shiftDetails.add(detail);

            // 判断是否为休息班种（优先使用 isRest 标记，兼容 duration=0）
            boolean isRestShift = (shift.getIsRest() != null && shift.getIsRest() == 1)
                    || (shift.getDuration() != null && shift.getDuration() == 0);

            if (isRestShift) {
                // 累加休息天数
                restDays += count.intValue();
            } else {
                // 累加工作排班数和工时
                workDays += count.intValue();
                if (count > 0) {
                    totalHours += shift.getDuration() * count.intValue();
                    // 累加系数：次数 × 系数
                    double coefficient = shift.getCoefficient() != null ? shift.getCoefficient() : 1.0;
                    coefficientSum += coefficient * count.intValue();
                }
            }
        }

        response.setTotalDays(workDays);
        response.setRestDays(restDays);
        response.setTotalHours(totalHours);
        response.setCoefficientSum(Math.round(coefficientSum * 10) / 10.0);
        response.setShiftDetails(shiftDetails);

        // 直接从 DepartmentMember 读取存欠班
        int balanceDays = 0;
        DepartmentMember member = departmentId != null
                ? getDepartmentMember(Long.parseLong(memberId), Long.parseLong(departmentId))
                : getDepartmentMemberByUser(Long.parseLong(memberId));
        if (member != null) {
            if (member.getBalanceDays() == null) {
                // 首次访问，触发重算
                recalculateAndSaveBalanceDays(member, shiftMap);
            }
            balanceDays = member.getBalanceDays() != null ? member.getBalanceDays() : 0;
        }
        response.setBalanceDays(balanceDays);

        return response;
    }

    @Override
    public DepartmentStatisticsResponse getDepartmentStatistics(String departmentId, Integer year, Integer month) {
        DepartmentStatisticsResponse response = new DepartmentStatisticsResponse();

        // 查询该科室当月的所有排班
        LambdaQueryWrapper<Schedule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Schedule::getDepartmentId, Long.parseLong(departmentId))
                .apply("YEAR(date) = {0}", year)
                .apply("MONTH(date) = {0}", month);

        List<Schedule> schedules = scheduleMapper.selectList(queryWrapper);

        // 获取所有班次信息
        List<Shift> shifts = shiftService.list();
        Map<Long, Shift> shiftMap = shifts.stream()
                .collect(Collectors.toMap(Shift::getId, shift -> shift));

        // 计算总排班数（工作排班）、总休息数（休息班）和总工时
        int totalSchedules = 0;
        int totalRestSchedules = 0;
        int totalHours = 0;
        double totalCoefficientSum = 0.0;
        for (Schedule schedule : schedules) {
            Shift shift = shiftMap.get(schedule.getShiftId());
            if (shift != null) {
                // 优先使用 isRest 标记，兼容 duration=0
                boolean isRestShift = (shift.getIsRest() != null && shift.getIsRest() == 1)
                        || (shift.getDuration() != null && shift.getDuration() == 0);
                if (isRestShift) {
                    totalRestSchedules++;
                } else {
                    totalSchedules++;
                    if (shift.getDuration() != null && shift.getDuration() > 0) {
                        totalHours += shift.getDuration();
                    }
                    double coefficient = shift.getCoefficient() != null ? shift.getCoefficient() : 1.0;
                    totalCoefficientSum += coefficient;
                }
            }
        }

        // 查询该科室所有成员
        LambdaQueryWrapper<DepartmentMember> memberQuery = new LambdaQueryWrapper<>();
        memberQuery.eq(DepartmentMember::getDepartmentId, Long.parseLong(departmentId));
        List<DepartmentMember> members = departmentMemberService.list(memberQuery);

        // 统计每个成员的工作排班数、休息数和工时
        Map<Long, Long> memberWorkScheduleCount = new HashMap<>();
        Map<Long, Long> memberRestCountMap = new HashMap<>();
        Map<Long, Integer> memberHoursMap = new HashMap<>();
        Map<Long, Double> memberCoefficientSumMap = new HashMap<>();
        for (Schedule schedule : schedules) {
            Shift shift = shiftMap.get(schedule.getShiftId());
            if (shift != null) {
                // 优先使用 isRest 标记，兼容 duration=0
                boolean isRestShift = (shift.getIsRest() != null && shift.getIsRest() == 1)
                        || (shift.getDuration() != null && shift.getDuration() == 0);
                if (isRestShift) {
                    memberRestCountMap.merge(schedule.getMemberId(), 1L, Long::sum);
                } else {
                    memberWorkScheduleCount.merge(schedule.getMemberId(), 1L, Long::sum);
                    if (shift.getDuration() != null && shift.getDuration() > 0) {
                        memberHoursMap.merge(schedule.getMemberId(), shift.getDuration(), Integer::sum);
                    }
                    // 累加系数
                    double coefficient = shift.getCoefficient() != null ? shift.getCoefficient() : 1.0;
                    memberCoefficientSumMap.merge(schedule.getMemberId(), coefficient, Double::sum);
                }
            }
        }

        // 批量查询用户信息，避免循环内单条查询
        List<Long> userIds = members.stream().map(DepartmentMember::getUserId).collect(Collectors.toList());
        Map<Long, User> userMap = userService.listByIds(userIds).stream()
                .collect(Collectors.toMap(User::getId, u -> u));

        // 构建成员排行榜（按工作排班数从大到小排序）
        List<DepartmentStatisticsResponse.MemberRankDTO> memberRank = new ArrayList<>();
        for (DepartmentMember member : members) {
            Long scheduleCount = memberWorkScheduleCount.getOrDefault(member.getUserId(), 0L);
            Integer restCount = memberRestCountMap.getOrDefault(member.getUserId(), 0L).intValue();
            Integer memberHours = memberHoursMap.getOrDefault(member.getUserId(), 0);

            // 从map中获取用户信息
            User user = userMap.get(member.getUserId());
            if (user != null) {
                Double coefficientSum = memberCoefficientSumMap.getOrDefault(member.getUserId(), 0.0);

                DepartmentStatisticsResponse.MemberRankDTO rankDTO = new DepartmentStatisticsResponse.MemberRankDTO();
                rankDTO.setId(member.getUserId().toString());
                rankDTO.setName(user.getNickName());
                rankDTO.setScheduleCount(scheduleCount.intValue());
                rankDTO.setRestCount(restCount);
                rankDTO.setTotalHours(memberHours);
                rankDTO.setCoefficientSum(Math.round(coefficientSum * 10) / 10.0);

                // 直接从 DepartmentMember 读取存欠班
                if (member.getBalanceDays() == null) {
                    recalculateAndSaveBalanceDays(member, shiftMap);
                }
                rankDTO.setBalanceDays(member.getBalanceDays() != null ? member.getBalanceDays() : 0);

                memberRank.add(rankDTO);
            }
        }

        // 按排班数从大到小排序
        memberRank.sort((a, b) -> b.getScheduleCount().compareTo(a.getScheduleCount()));

        response.setTotalSchedules(totalSchedules);
        response.setTotalRestSchedules(totalRestSchedules);
        response.setTotalCount(members.size());
        response.setTotalHours(totalHours);
        response.setCoefficientSum(Math.round(totalCoefficientSum * 10) / 10.0);
        response.setMemberRank(memberRank);

        return response;
    }

    @Override
    public void recalculateAndSaveBalanceDays(Long userId, Long departmentId) {
        DepartmentMember member = getDepartmentMember(userId, departmentId);
        if (member == null) {
            return;
        }
        Map<Long, Shift> shiftMap = shiftService.list().stream()
                .collect(Collectors.toMap(Shift::getId, shift -> shift));
        recalculateAndSaveBalanceDays(member, shiftMap);
    }

    /**
     * 重算存欠班并保存到 DepartmentMember
     * 从加入月份逐月累加到当前月，当前月只算到今天
     */
    private void recalculateAndSaveBalanceDays(DepartmentMember member, Map<Long, Shift> shiftMap) {
        LocalDate joinTime = member.getJoinTime();
        if (joinTime == null) {
            member.setBalanceDays(0);
            departmentMemberService.updateById(member);
            return;
        }

        LocalDate now = LocalDate.now();
        LocalDate currentMonthStart = now.withDayOfMonth(1);
        LocalDate startMonth = joinTime.withDayOfMonth(1);

        int totalBalance = 0;
        LocalDate monthIter = startMonth;
        while (!monthIter.isAfter(currentMonthStart)) {
            List<Schedule> monthSchedules = getSchedulesForMonth(member.getUserId(), monthIter);
            int monthlyBalance = calculateBalanceDaysForMonth(monthSchedules, shiftMap,
                    monthIter.getYear(), monthIter.getMonthValue(), joinTime);
            totalBalance += monthlyBalance;
            monthIter = monthIter.plusMonths(1);
        }

        member.setBalanceDays(totalBalance);
        departmentMemberService.updateById(member);
        log.debug("重算存欠班：用户={}, 科室={}, balance={}", member.getUserId(), member.getDepartmentId(), totalBalance);
    }

    /**
     * 查询科室成员记录
     */
    private DepartmentMember getDepartmentMember(Long userId, Long departmentId) {
        LambdaQueryWrapper<DepartmentMember> query = new LambdaQueryWrapper<>();
        query.eq(DepartmentMember::getUserId, userId)
                .eq(DepartmentMember::getDepartmentId, departmentId);
        return departmentMemberService.getOne(query, false);
    }

    /**
     * 通过用户ID查询科室成员记录（一个用户只属于一个科室）
     */
    private DepartmentMember getDepartmentMemberByUser(Long userId) {
        LambdaQueryWrapper<DepartmentMember> query = new LambdaQueryWrapper<>();
        query.eq(DepartmentMember::getUserId, userId)
                .last("LIMIT 1");
        return departmentMemberService.getOne(query, false);
    }

    /**
     * 查询指定月份的排班数据
     */
    private List<Schedule> getSchedulesForMonth(Long userId, LocalDate month) {
        LocalDate firstDay = month.withDayOfMonth(1);
        LocalDate lastDay = month.with(TemporalAdjusters.lastDayOfMonth());
        LambdaQueryWrapper<Schedule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Schedule::getMemberId, userId)
                .apply("date >= {0}", firstDay)
                .apply("date < {0}", lastDay.plusDays(1));
        return scheduleMapper.selectList(queryWrapper);
    }

    /**
     * 计算单月存欠班天数
     * 规则：
     * - 当月应排班天数 = 当月天数 - 周末天数 - 法定假日(非周末) + 调休上班日
     * - 存欠班 = 实际工作排班天数 - 应排班天数
     * - 如果是加入当月，从加入日开始计算（不欠加入前的天数）
     * - 正数=存班，负数=欠班
     *
     * @param schedules 成员当月的排班列表
     * @param shiftMap  班种映射
     * @param year      年份
     * @param month     月份
     * @param joinTime  加入科室时间（可为null）
     * @return 单月存欠班天数（正数=存班，负数=欠班）
     */
    private int calculateBalanceDaysForMonth(List<Schedule> schedules, Map<Long, Shift> shiftMap,
                                              int year, int month, LocalDate joinTime) {
        // 未来月份不计算欠存，直接返回0
        LocalDate now = LocalDate.now();
        LocalDate firstDay = LocalDate.of(year, month, 1);
        if (firstDay.isAfter(now.withDayOfMonth(1))) {
            return 0;
        }

        // 如果是加入当月，从加入日开始计算，避免欠加入前的天数
        if (joinTime != null) {
            LocalDate joinMonthStart = joinTime.withDayOfMonth(1);
            if (firstDay.equals(joinMonthStart) && joinTime.isAfter(firstDay)) {
                firstDay = joinTime;
            }
        }

        // 构建每日排班映射
        Map<LocalDate, Schedule> dayMap = new HashMap<>();
        for (Schedule schedule : schedules) {
            dayMap.put(schedule.getDate(), schedule);
        }

        // 当前月只计算到今天，历史月计算整月
        LocalDate lastDay = LocalDate.of(year, month, 1).with(TemporalAdjusters.lastDayOfMonth());
        boolean isCurrentMonth = !now.isAfter(lastDay);
        if (isCurrentMonth) {
            lastDay = now;
        }

        // 如果加入日在当月内且在lastDay之后，从加入日开始
        if (joinTime != null && firstDay.isBefore(joinTime) && !joinTime.isAfter(lastDay)) {
            firstDay = joinTime;
        }

        int expectedWorkDays = 0;   // 当月应排班天数
        int actualWorkDays = 0;     // 实际工作排班天数

        for (LocalDate d = firstDay; !d.isAfter(lastDay); d = d.plusDays(1)) {
            DayOfWeek dow = d.getDayOfWeek();
            boolean isWeekend = dow == DayOfWeek.SATURDAY || dow == DayOfWeek.SUNDAY;
            boolean isHoliday = HolidayConstants.isHoliday(d);
            boolean isAdjustedWorkday = HolidayConstants.isWorkday(d);

            // 判断这天是否应该上班
            boolean shouldWork = false;
            if (isAdjustedWorkday) {
                // 调休上班日（通常是周末补班）
                shouldWork = true;
            } else if (!isWeekend && !isHoliday) {
                // 普通工作日（非周末、非法定假日）
                shouldWork = true;
            }

            if (shouldWork) {
                expectedWorkDays++;
            }

            // 统计实际工作排班
            Schedule schedule = dayMap.get(d);
            if (schedule != null) {
                Shift shift = shiftMap.get(schedule.getShiftId());
                if (shift != null) {
                    boolean isRestShift = (shift.getIsRest() != null && shift.getIsRest() == 1)
                            || (shift.getDuration() != null && shift.getDuration() == 0);
                    if (!isRestShift) {
                        actualWorkDays++;
                    }
                }
            }
        }

        // 存欠班 = 实际排班 - 应排班
        // 法定假日上班自然产生+1效果（应排班天数已减去法定假日，但实际排班包含该天）
        return actualWorkDays - expectedWorkDays;
    }
}
