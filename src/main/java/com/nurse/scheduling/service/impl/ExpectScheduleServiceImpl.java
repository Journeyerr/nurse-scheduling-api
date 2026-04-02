package com.nurse.scheduling.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nurse.scheduling.context.UserContext;
import com.nurse.scheduling.dto.expect.ExpectScheduleRequest;
import com.nurse.scheduling.dto.expect.ExpectScheduleResponse;
import com.nurse.scheduling.dto.swap.SwapRequestDto;
import com.nurse.scheduling.entity.DepartmentMember;
import com.nurse.scheduling.entity.ExpectSchedule;
import com.nurse.scheduling.entity.Schedule;
import com.nurse.scheduling.entity.Shift;
import com.nurse.scheduling.entity.User;
import com.nurse.scheduling.mapper.ExpectScheduleMapper;
import com.nurse.scheduling.mapper.ScheduleMapper;
import com.nurse.scheduling.service.DepartmentMemberService;
import com.nurse.scheduling.service.ExpectScheduleService;
import com.nurse.scheduling.service.ShiftService;
import com.nurse.scheduling.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 申请服务实现类（期望排班、换班等）
 *
 * @author nurse-scheduling
 */
@Slf4j
@Service
public class ExpectScheduleServiceImpl extends ServiceImpl<ExpectScheduleMapper, ExpectSchedule> implements ExpectScheduleService {

    @Autowired
    private ExpectScheduleMapper expectScheduleMapper;

    @Autowired
    private ScheduleMapper scheduleMapper;

    @Autowired
    private DepartmentMemberService departmentMemberService;

    @Autowired
    private UserService userService;

    @Autowired
    private ShiftService shiftService;

    @Override
    public List<ExpectScheduleResponse> getMyExpectSchedule() {
        Long userId = UserContext.getUserIdAsLong();
        log.info("获取我的申请列表，用户ID：{}", userId);
        
        List<ExpectScheduleResponse> list = expectScheduleMapper.getMyExpectSchedule(userId);
        log.info("查询到{}条申请记录", list.size());
        
        return list;
    }

    @Override
    public List<ExpectScheduleResponse> getExpectScheduleList() {
        Long userId = UserContext.getUserIdAsLong();
        log.info("获取科室所有申请列表，用户ID：{}", userId);
        
        // 获取当前用户的科室ID
        DepartmentMember member = departmentMemberService.lambdaQuery()
                .eq(DepartmentMember::getUserId, userId)
                .one();
        
        if (member == null) {
            log.warn("用户未加入任何科室，用户ID：{}", userId);
            return List.of();
        }
        
        Long departmentId = member.getDepartmentId();
        List<ExpectScheduleResponse> list = expectScheduleMapper.getExpectScheduleList(departmentId);
        log.info("查询到{}条申请记录", list.size());
        
        return list;
    }

    @Override
    public ExpectScheduleResponse submitExpectSchedule(ExpectScheduleRequest request) {
        Long userId = UserContext.getUserIdAsLong();
        log.info("提交期望排班，用户ID：{}，班种ID：{}", userId, request.getShiftId());
        
        // 获取当前用户信息
        User user = userService.getById(userId);
        if (user == null) {
            log.error("用户不存在，用户ID：{}", userId);
            throw new RuntimeException("用户不存在");
        }
        
        // 获取当前用户的科室ID
        DepartmentMember member = departmentMemberService.lambdaQuery()
                .eq(DepartmentMember::getUserId, userId)
                .one();
        
        if (member == null) {
            log.warn("用户未加入任何科室，用户ID：{}", userId);
            throw new RuntimeException("用户未加入科室");
        }
        
        // 创建期望排班
        ExpectSchedule expectSchedule = new ExpectSchedule();
        expectSchedule.setDepartmentId(member.getDepartmentId());
        expectSchedule.setUserId(userId);
        expectSchedule.setUserName(user.getNickName());
        expectSchedule.setType("schedule");  // 期望排班类型
        expectSchedule.setShiftId(Long.parseLong(request.getShiftId()));
        expectSchedule.setStartDate(request.getStartDate());
        expectSchedule.setEndDate(request.getEndDate());
        expectSchedule.setRemark(request.getRemark());
        expectSchedule.setStatus("pending"); // 默认待审批
        
        save(expectSchedule);
        log.info("期望排班提交成功，ID：{}", expectSchedule.getId());
        
        // 构建响应
        ExpectScheduleResponse response = new ExpectScheduleResponse();
        response.setId(expectSchedule.getId().toString());
        response.setType("schedule");
        response.setUserId(userId.toString());
        response.setUserName(user.getNickName());
        response.setShiftId(request.getShiftId());
        response.setStartDate(request.getStartDate());
        response.setEndDate(request.getEndDate());
        response.setRemark(request.getRemark());
        response.setStatus("pending");
        
        return response;
    }

    @Override
    public void submitSwapRequest(SwapRequestDto request) {
        log.info("提交换班申请，我方日期：{}，目标日期：{}，目标用户ID：{}，我方班次：{}，目标班次：{}",
                request.getMyDate(), request.getTargetDate(), request.getTargetUserId(),
                request.getMyShiftId(), request.getTargetShiftId());

        // 获取当前用户
        User currentUser = UserContext.getUser();
        if (currentUser == null) {
            throw new RuntimeException("用户未登录");
        }

        // 获取当前用户的科室ID
        Long userId = UserContext.getUserIdAsLong();
        DepartmentMember member = departmentMemberService.lambdaQuery()
                .eq(DepartmentMember::getUserId, userId)
                .one();
        if (member == null) {
            throw new RuntimeException("用户未加入科室");
        }

        // 获取目标用户信息
        User targetUser = userService.getById(Long.parseLong(request.getTargetUserId()));
        if (targetUser == null) {
            throw new RuntimeException("目标用户不存在");
        }

        // 创建换班申请（存储到 expect_schedule 表）
        ExpectSchedule expectSchedule = new ExpectSchedule();
        expectSchedule.setDepartmentId(member.getDepartmentId());
        expectSchedule.setUserId(currentUser.getId());
        expectSchedule.setUserName(currentUser.getNickName());
        expectSchedule.setType("swap");  // 换班类型
        expectSchedule.setMyShiftId(request.getMyShiftId());
        expectSchedule.setTargetShiftId(request.getTargetShiftId());
        expectSchedule.setStartDate(java.time.LocalDate.parse(request.getMyDate()));  // 我方日期
        expectSchedule.setEndDate(java.time.LocalDate.parse(request.getMyDate()));
        expectSchedule.setTargetDate(java.time.LocalDate.parse(request.getTargetDate()));  // 目标日期
        expectSchedule.setTargetUserId(targetUser.getId());
        expectSchedule.setTargetUserName(targetUser.getNickName());
        expectSchedule.setRemark(request.getRemark());
        expectSchedule.setStatus("pending");

        save(expectSchedule);
        log.info("换班申请提交成功，申请ID：{}", expectSchedule.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approveExpectSchedule(String id, String status, String approveRemark) {
        log.info("审批申请，ID：{}，状态：{}", id, status);
        
        ExpectSchedule expectSchedule = getById(Long.parseLong(id));
        if (expectSchedule == null) {
            log.error("申请不存在，ID：{}", id);
            throw new RuntimeException("申请不存在");
        }
        
        expectSchedule.setStatus(status);
        expectSchedule.setApproveRemark(approveRemark);
        expectSchedule.setApproverId(UserContext.getUserIdAsLong());
        expectSchedule.setApproveTime(java.time.LocalDate.now());
        
        updateById(expectSchedule);
        log.info("申请审批成功，ID：{}", id);

        // 如果是换班申请且审批通过，执行班次交换
        if ("swap".equals(expectSchedule.getType()) && "approved".equals(status)) {
            executeSwap(expectSchedule);
        }
    }

    /**
     * 执行换班操作
     */
    private void executeSwap(ExpectSchedule swapRequest) {
        log.info("开始执行换班操作，申请ID：{}", swapRequest.getId());
        
        Long userId = swapRequest.getUserId();  // 申请人ID
        Long targetUserId = swapRequest.getTargetUserId();  // 目标用户ID
        LocalDate myDate = swapRequest.getStartDate();  // 我方日期
        LocalDate targetDate = swapRequest.getTargetDate();  // 目标日期
        Long myShiftId = swapRequest.getMyShiftId();  // 我方班次ID
        Long targetShiftId = swapRequest.getTargetShiftId();  // 目标班次ID
        
        log.info("换班详情：用户{}在{}的班次 ⇄ 用户{}在{}的班次", 
                userId, myDate, targetUserId, targetDate);

        // 获取班次详细信息
        Shift myShift = myShiftId != null ? shiftService.getById(myShiftId) : null;
        Shift targetShift = targetShiftId != null ? shiftService.getById(targetShiftId) : null;

        // 1. 更新申请人在我方日期的排班为目标班次
        Schedule mySchedule = scheduleMapper.selectOne(
            new LambdaQueryWrapper<Schedule>()
                .eq(Schedule::getMemberId, userId)
                .eq(Schedule::getDate, myDate)
        );
        
        if (mySchedule != null && targetShift != null) {
            mySchedule.setShiftId(targetShiftId);
            mySchedule.setShiftCode(targetShift.getCode());
            mySchedule.setShiftName(targetShift.getName());
            mySchedule.setShiftColor(targetShift.getColor());
            scheduleMapper.updateById(mySchedule);
            log.info("已更新用户{}在{}的班次为：{}", userId, myDate, targetShift.getName());
        }

        // 2. 更新目标用户在目标日期的排班为我方班次
        Schedule targetSchedule = scheduleMapper.selectOne(
            new LambdaQueryWrapper<Schedule>()
                .eq(Schedule::getMemberId, targetUserId)
                .eq(Schedule::getDate, targetDate)
        );
        
        if (targetSchedule != null && myShift != null) {
            targetSchedule.setShiftId(myShiftId);
            targetSchedule.setShiftCode(myShift.getCode());
            targetSchedule.setShiftName(myShift.getName());
            targetSchedule.setShiftColor(myShift.getColor());
            scheduleMapper.updateById(targetSchedule);
            log.info("已更新用户{}在{}的班次为：{}", targetUserId, targetDate, myShift.getName());
        }

        log.info("换班操作完成");
    }

    /**
     * 审批申请（重载方法，接收ApproveRequest对象）
     */
    public void approveExpectSchedule(com.nurse.scheduling.controller.ExpectScheduleController.ApproveRequest request) {
        approveExpectSchedule(request.getId(), request.getStatus(), request.getApproveRemark());
    }

    @Override
    public List<ExpectScheduleResponse> getApprovedByUsers(List<String> userIds) {
        log.info("获取用户列表的已审核申请，用户数量：{}", userIds.size());
        
        if (userIds == null || userIds.isEmpty()) {
            return List.of();
        }
        
        List<Long> userIdLongs = userIds.stream()
                .map(Long::parseLong)
                .collect(Collectors.toList());
        
        List<ExpectScheduleResponse> list = expectScheduleMapper.getApprovedByUsers(userIdLongs);
        log.info("查询到{}条已审核申请记录", list.size());
        
        return list;
    }

    @Override
    public int getPendingCount() {
        Long userId = UserContext.getUserIdAsLong();
        log.info("获取待审批数量，用户ID：{}", userId);
        
        // 获取当前用户的科室ID
        DepartmentMember member = departmentMemberService.lambdaQuery()
                .eq(DepartmentMember::getUserId, userId)
                .one();
        
        if (member == null) {
            log.warn("用户未加入任何科室，用户ID：{}", userId);
            return 0;
        }
        
        Long departmentId = member.getDepartmentId();
        int count = expectScheduleMapper.getPendingCount(departmentId);
        log.info("查询到{}条待审批记录", count);
        
        return count;
    }
}
