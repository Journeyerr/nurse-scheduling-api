package com.nurse.scheduling.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nurse.scheduling.common.BusinessException;
import com.nurse.scheduling.dto.schedule.BatchScheduleRequest;
import com.nurse.scheduling.dto.schedule.DailyScheduleVO;
import com.nurse.scheduling.dto.schedule.ScheduleAddRequest;
import com.nurse.scheduling.dto.schedule.ScheduleResponse;
import com.nurse.scheduling.dto.swap.UserScheduleVO;
import com.nurse.scheduling.entity.Schedule;
import com.nurse.scheduling.entity.Shift;
import com.nurse.scheduling.entity.User;
import com.nurse.scheduling.mapper.ScheduleMapper;
import com.nurse.scheduling.service.ScheduleService;
import com.nurse.scheduling.service.ShiftService;
import com.nurse.scheduling.service.StatisticsService;
import com.nurse.scheduling.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 排班服务实现类
 *
 * @author nurse-scheduling
 */
@Slf4j
@Service
public class ScheduleServiceImpl extends ServiceImpl<ScheduleMapper, Schedule> implements ScheduleService {

    @Autowired
    private ShiftService shiftService;

    @Autowired
    private UserService userService;

    @Autowired
    private StatisticsService statisticsService;

    @Override
    public List<ScheduleResponse> getMonthlySchedule(String departmentId, Integer year, Integer month) {
        log.info("获取月排班，科室ID：{}，年份：{}，月份：{}", departmentId, year, month);
        
        List<ScheduleResponse> schedules = baseMapper.getMonthlySchedule(
            Long.parseLong(departmentId), year, month
        );
        
        log.debug("月排班查询成功，排班数量：{}", schedules.size());
        return schedules;
    }

    @Override
    public List<ScheduleResponse> getWeeklySchedule(String departmentId, String startDate) {
        log.info("获取周排班，科室ID：{}，开始日期：{}", departmentId, startDate);
        
        // 计算周范围
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = start.plusDays(6);
        
        List<ScheduleResponse> schedules = baseMapper.getWeeklySchedule(
            Long.parseLong(departmentId), start, end
        );
        
        log.debug("周排班查询成功，排班数量：{}", schedules.size());
        return schedules;
    }

    @Override
    public ScheduleResponse addSchedule(String departmentId, ScheduleAddRequest request) {
        log.info("添加排班，科室ID：{}，成员ID：{}，日期：{}", departmentId, request.getMemberId(), request.getDate());
        
        // 获取班种信息
        Shift shift = shiftService.getById(Long.parseLong(request.getShiftId()));
        if (shift == null) {
            log.warn("班种不存在，班种ID：{}", request.getShiftId());
            return null;
        }
        
        // 创建排班
        Schedule schedule = new Schedule();
        schedule.setDepartmentId(Long.parseLong(departmentId));
        schedule.setMemberId(Long.parseLong(request.getMemberId()));
        schedule.setDate(request.getDate());
        schedule.setShiftId(Long.parseLong(request.getShiftId()));
        schedule.setShiftCode(shift.getCode());
        schedule.setShiftName(shift.getName());
        schedule.setShiftColor(shift.getColor());
        
        save(schedule);
        log.info("排班添加成功，排班ID：{}", schedule.getId());

        // 重算存欠班
        statisticsService.recalculateAndSaveBalanceDays(schedule.getMemberId(), schedule.getDepartmentId());
        
        ScheduleResponse response = new ScheduleResponse();
        BeanUtil.copyProperties(schedule, response);
        response.setId(schedule.getId().toString());
        response.setMemberId(schedule.getMemberId().toString());
        response.setShiftId(schedule.getShiftId().toString());
        
        return response;
    }

    @Override
    public ScheduleResponse updateSchedule(ScheduleAddRequest request) {
        log.info("更新排班，排班ID：{}", request.getId());
        
        Schedule schedule = getById(Long.parseLong(request.getId()));
        if (schedule == null) {
            log.warn("排班不存在，排班ID：{}", request.getId());
            return null;
        }
        
        // 获取班种信息
        Shift shift = shiftService.getById(Long.parseLong(request.getShiftId()));
        if (shift == null) {
            log.warn("班种不存在，班种ID：{}", request.getShiftId());
            return null;
        }
        
        // 记录变更前的成员（用于重算存欠班）
        LocalDate oldDate = schedule.getDate();
        Long oldMemberId = schedule.getMemberId();
        Long oldDeptId = schedule.getDepartmentId();

        // 更新排班
        schedule.setMemberId(Long.parseLong(request.getMemberId()));
        schedule.setDate(request.getDate());
        schedule.setShiftId(Long.parseLong(request.getShiftId()));
        schedule.setShiftCode(shift.getCode());
        schedule.setShiftName(shift.getName());
        schedule.setShiftColor(shift.getColor());

        updateById(schedule);
        log.info("排班更新成功，排班ID：{}", schedule.getId());

        // 重算存欠班（新旧成员都可能受影响）
        if (!oldDate.equals(request.getDate()) || !oldMemberId.equals(Long.parseLong(request.getMemberId()))) {
            statisticsService.recalculateAndSaveBalanceDays(oldMemberId, oldDeptId);
        }
        statisticsService.recalculateAndSaveBalanceDays(Long.parseLong(request.getMemberId()), oldDeptId);
        
        ScheduleResponse response = new ScheduleResponse();
        BeanUtil.copyProperties(schedule, response);
        response.setId(schedule.getId().toString());
        response.setMemberId(schedule.getMemberId().toString());
        response.setShiftId(schedule.getShiftId().toString());
        
        return response;
    }

    @Override
    public void deleteSchedule(String scheduleId) {
        log.info("删除排班，排班ID：{}", scheduleId);
        // 先获取排班信息用于重算存欠班
        Schedule schedule = getById(Long.parseLong(scheduleId));
        if (schedule != null) {
            removeById(Long.parseLong(scheduleId));
            // 重算存欠班
            statisticsService.recalculateAndSaveBalanceDays(schedule.getMemberId(), schedule.getDepartmentId());
        }
        log.info("排班删除成功，排班ID：{}", scheduleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ScheduleResponse> batchSchedule(String departmentId, BatchScheduleRequest request) {
        log.info("批量操作排班，科室ID：{}，新增数：{}，删除数：{}", 
                departmentId, 
                request.getAdds() != null ? request.getAdds().size() : 0,
                request.getDeletes() != null ? request.getDeletes().size() : 0);
        
        List<ScheduleResponse> addedSchedules = new ArrayList<>();
        
        // 批量删除
        if (request.getDeletes() != null && !request.getDeletes().isEmpty()) {
            Long deptId = Long.parseLong(departmentId);
            for (String scheduleId : request.getDeletes()) {
                Schedule schedule = getById(Long.parseLong(scheduleId));
                if (schedule != null) {
                    removeById(Long.parseLong(scheduleId));
                    // 重算存欠班
                    statisticsService.recalculateAndSaveBalanceDays(schedule.getMemberId(), deptId);
                }
                log.debug("删除排班：{}", scheduleId);
            }
        }
        
        // 批量添加 - 添加冲突校验
        if (request.getAdds() != null && !request.getAdds().isEmpty()) {
            for (ScheduleAddRequest addRequest : request.getAdds()) {
                // 校验排班冲突
                List<ScheduleResponse> existingSchedules = baseMapper.getMemberScheduleByDate(
                    Long.parseLong(addRequest.getMemberId()), 
                    addRequest.getDate()
                );
                
                if (!existingSchedules.isEmpty()) {
                    // 获取成员姓名
                    String memberName = addRequest.getMemberName() != null ? addRequest.getMemberName() : "成员";
                    String dateStr = addRequest.getDate().toString();
                    String existingShiftName = existingSchedules.get(0).getShiftName();
                    
                    log.warn("排班冲突：成员 {} 在 {} 已有排班 {}", memberName, dateStr, existingShiftName);
                    throw new BusinessException(String.format("%s %s 已排班 %s，不能重复排班", 
                        memberName, dateStr, existingShiftName));
                }
                
                // 获取班种信息
                Shift shift = shiftService.getById(Long.parseLong(addRequest.getShiftId()));
                if (shift == null) {
                    log.warn("班种不存在，班种ID：{}", addRequest.getShiftId());
                    throw new BusinessException("班种不存在：" + addRequest.getShiftId());
                }
                
                // 创建排班
                Schedule schedule = new Schedule();
                schedule.setDepartmentId(Long.parseLong(departmentId));
                schedule.setMemberId(Long.parseLong(addRequest.getMemberId()));
                schedule.setDate(addRequest.getDate());
                schedule.setShiftId(Long.parseLong(addRequest.getShiftId()));
                schedule.setShiftCode(shift.getCode());
                schedule.setShiftName(shift.getName());
                schedule.setShiftColor(shift.getColor());
                
                save(schedule);
                log.debug("添加排班：{}", schedule.getId());

                // 重算存欠班
                statisticsService.recalculateAndSaveBalanceDays(schedule.getMemberId(), schedule.getDepartmentId());
                
                ScheduleResponse response = new ScheduleResponse();
                BeanUtil.copyProperties(schedule, response);
                response.setId(schedule.getId().toString());
                response.setMemberId(schedule.getMemberId().toString());
                response.setShiftId(schedule.getShiftId().toString());
                addedSchedules.add(response);
            }
        }
        
        log.info("批量操作排班完成，成功添加：{}", addedSchedules.size());
        return addedSchedules;
    }

    @Override
    public List<ScheduleResponse> getMySchedule(String memberId, String yearMonths) {
        log.info("获取我的排班，成员ID：{}，年月：{}", memberId, yearMonths);
        
        List<ScheduleResponse> allSchedules = new ArrayList<>();
        
        // 解析年月列表，格式："2025-12,2026-1,2026-2"
        String[] yearMonthArr = yearMonths.split(",");
        for (String yearMonth : yearMonthArr) {
            String[] parts = yearMonth.trim().split("-");
            if (parts.length == 2) {
                int year = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]);
                
                List<ScheduleResponse> schedules = baseMapper.getMySchedule(
                    Long.parseLong(memberId), year, month
                );
                allSchedules.addAll(schedules);
            }
        }
        
        log.debug("我的排班查询成功，排班数量：{}", allSchedules.size());
        return allSchedules;
    }

    @Override
    public List<UserScheduleVO> getUsersByDate(String date) {
        log.info("获取指定日期排班的用户列表，日期：{}", date);
        LocalDate localDate = LocalDate.parse(date);
        List<UserScheduleVO> users = baseMapper.getUsersByDate(localDate);
        log.debug("查询成功，用户数量：{}", users.size());
        return users;
    }

    @Override
    public List<DailyScheduleVO> getDailySchedule(String date, String departmentId) {
        log.info("获取某天科室排班，日期：{}，科室ID：{}", date, departmentId);
        LocalDate localDate = LocalDate.parse(date);
        Long deptId = Long.parseLong(departmentId);
        // 查询该科室该天所有排班，关联用户和班种信息
        LambdaQueryWrapper<Schedule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Schedule::getDate, localDate)
                .eq(Schedule::getDepartmentId, deptId);
        List<Schedule> schedules = this.list(queryWrapper);

        if (schedules.isEmpty()) {
            return Collections.emptyList();
        }

        // 批量查询用户信息
        List<Long> memberIds = schedules.stream().map(Schedule::getMemberId).distinct().collect(Collectors.toList());
        Map<Long, User> userMap = userService.listByIds(memberIds).stream()
                .collect(Collectors.toMap(User::getId, u -> u));

        // 批量查询班种信息
        List<Long> shiftIds = schedules.stream().map(Schedule::getShiftId).distinct().collect(Collectors.toList());
        Map<Long, Shift> shiftMap = shiftService.listByIds(shiftIds).stream()
                .collect(Collectors.toMap(Shift::getId, s -> s));

        // 组装结果
        List<DailyScheduleVO> result = new ArrayList<>();
        for (Schedule schedule : schedules) {
            User user = userMap.get(schedule.getMemberId());
            Shift shift = shiftMap.get(schedule.getShiftId());
            if (user != null) {
                DailyScheduleVO vo = new DailyScheduleVO();
                vo.setMemberId(String.valueOf(user.getId()));
                vo.setMemberName(user.getNickName());
                if (shift != null) {
                    vo.setShiftCode(shift.getCode());
                    vo.setShiftName(shift.getName());
                    vo.setShiftColor(shift.getColor());
                }
                result.add(vo);
            }
        }
        return result;
    }
}
