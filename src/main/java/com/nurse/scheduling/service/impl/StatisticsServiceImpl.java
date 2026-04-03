package com.nurse.scheduling.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nurse.scheduling.dto.statistics.DepartmentStatisticsResponse;
import com.nurse.scheduling.dto.statistics.MyStatisticsResponse;
import com.nurse.scheduling.entity.DepartmentMember;
import com.nurse.scheduling.entity.Schedule;
import com.nurse.scheduling.entity.Shift;
import com.nurse.scheduling.entity.User;
import com.nurse.scheduling.mapper.ScheduleMapper;
import com.nurse.scheduling.service.DepartmentMemberService;
import com.nurse.scheduling.service.ScheduleService;
import com.nurse.scheduling.service.ShiftService;
import com.nurse.scheduling.service.StatisticsService;
import com.nurse.scheduling.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    private ScheduleService scheduleService;

    @Autowired
    private ScheduleMapper scheduleMapper;

    @Autowired
    private DepartmentMemberService departmentMemberService;

    @Autowired
    private ShiftService shiftService;

    @Autowired
    private UserService userService;

    @Override
    public MyStatisticsResponse getMyStatistics(String memberId, Integer year, Integer month) {
        MyStatisticsResponse response = new MyStatisticsResponse();

        // 查询该成员当月的所有排班
        LambdaQueryWrapper<Schedule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Schedule::getMemberId, Long.parseLong(memberId))
                .apply("YEAR(date) = {0}", year)
                .apply("MONTH(date) = {0}", month);

        List<Schedule> schedules = scheduleService.list(queryWrapper);

        // 计算总排班天数
        int totalDays = schedules.size();

        // 统计各班次数量
        Map<Long, Long> shiftCountMap = schedules.stream()
                .collect(Collectors.groupingBy(Schedule::getShiftId, Collectors.counting()));

        // 获取所有班种
        List<Shift> shifts = shiftService.list();

        // 构建班次详情
        List<MyStatisticsResponse.ShiftDetailDTO> shiftDetails = new ArrayList<>();
        int totalHours = 0;
        int restDays = 0;

        for (Shift shift : shifts) {
            Long count = shiftCountMap.getOrDefault(shift.getId(), 0L);

            MyStatisticsResponse.ShiftDetailDTO detail = new MyStatisticsResponse.ShiftDetailDTO();
            detail.setName(shift.getName());
            detail.setCode(shift.getCode());
            detail.setColor(shift.getColor());
            detail.setCount(count.intValue());

            shiftDetails.add(detail);

            // 统计休息天数（duration = 0）
            if (shift.getDuration() != null && shift.getDuration() == 0) {
                restDays += count.intValue();
            }

            // 计算总工时
            if (shift.getDuration() != null && count > 0) {
                totalHours += shift.getDuration() * count.intValue();
            }
        }

        response.setTotalDays(totalDays);
        response.setRestDays(restDays);
        response.setTotalHours(totalHours);
        response.setShiftDetails(shiftDetails);

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

        List<Schedule> schedules = scheduleService.list(queryWrapper);

        // 计算总排班数
        int totalSchedules = schedules.size();

        // 获取所有班次信息
        List<Shift> shifts = shiftService.list();
        Map<Long, Shift> shiftMap = shifts.stream()
                .collect(Collectors.toMap(Shift::getId, shift -> shift));

        // 计算总工时
        int totalHours = 0;
        for (Schedule schedule : schedules) {
            Shift shift = shiftMap.get(schedule.getShiftId());
            if (shift != null && shift.getDuration() != null) {
                totalHours += shift.getDuration();
            }
        }

        // 查询该科室所有成员
        LambdaQueryWrapper<DepartmentMember> memberQuery = new LambdaQueryWrapper<>();
        memberQuery.eq(DepartmentMember::getDepartmentId, Long.parseLong(departmentId));
        List<DepartmentMember> members = departmentMemberService.list(memberQuery);

        // 统计每个成员的排班数和工时
        Map<Long, Long> memberScheduleCount = schedules.stream()
                .collect(Collectors.groupingBy(Schedule::getMemberId, Collectors.counting()));

        // 计算每个成员的总工时
        Map<Long, Integer> memberHoursMap = new HashMap<>();
        for (Schedule schedule : schedules) {
            Shift shift = shiftMap.get(schedule.getShiftId());
            if (shift != null && shift.getDuration() != null) {
                memberHoursMap.merge(schedule.getMemberId(), shift.getDuration(), Integer::sum);
            }
        }

        // 计算参与人数（有排班的成员数）
        int participatingMembers = memberScheduleCount.size();

        // 构建成员排行榜（按排班数从大到小排序）
        List<DepartmentStatisticsResponse.MemberRankDTO> memberRank = new ArrayList<>();
        for (DepartmentMember member : members) {
            Long scheduleCount = memberScheduleCount.getOrDefault(member.getUserId(), 0L);
            Integer memberHours = memberHoursMap.getOrDefault(member.getUserId(), 0);

            // 获取用户信息
            User user = userService.getById(member.getUserId());
            if (user != null) {
                DepartmentStatisticsResponse.MemberRankDTO rankDTO = new DepartmentStatisticsResponse.MemberRankDTO();
                rankDTO.setId(member.getUserId().toString());
                rankDTO.setName(user.getNickName());
                rankDTO.setScheduleCount(scheduleCount.intValue());
                rankDTO.setTotalHours(memberHours);

                memberRank.add(rankDTO);
            }
        }

        // 按排班数从大到小排序
        memberRank.sort((a, b) -> b.getScheduleCount().compareTo(a.getScheduleCount()));

        response.setTotalSchedules(totalSchedules);
        response.setTotalMembers(participatingMembers);
        response.setTotalHours(totalHours);
        response.setMemberRank(memberRank);

        return response;
    }
}
