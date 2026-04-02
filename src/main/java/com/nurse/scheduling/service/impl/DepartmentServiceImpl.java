package com.nurse.scheduling.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nurse.scheduling.common.BusinessException;
import com.nurse.scheduling.common.ResultCode;
import com.nurse.scheduling.dto.department.*;
import com.nurse.scheduling.dto.shift.TimeSlotDTO;
import com.nurse.scheduling.dto.user.UserUpdateRequest;
import com.nurse.scheduling.entity.Department;
import com.nurse.scheduling.entity.DepartmentMember;
import com.nurse.scheduling.entity.Shift;
import com.nurse.scheduling.entity.User;
import com.nurse.scheduling.mapper.DepartmentMapper;
import com.nurse.scheduling.service.DepartmentMemberService;
import com.nurse.scheduling.service.DepartmentService;
import com.nurse.scheduling.service.ShiftService;
import com.nurse.scheduling.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 科室服务实现类
 *
 * @author nurse-scheduling
 */
@Slf4j
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {

    @Autowired
    private DepartmentMemberService departmentMemberService;

    @Autowired
    private UserService userService;

    @Autowired
    private ShiftService shiftService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DepartmentResponse createDepartment(String userId, DepartmentCreateRequest request) {
        log.info("创建科室，用户ID：{}，科室名称：{}", userId, request.getName());

        // 检查用户是否已加入任何科室（一个用户只能创建/加入一个科室）
        DepartmentMember existMember = departmentMemberService.lambdaQuery()
                .eq(DepartmentMember::getUserId, Long.parseLong(userId))
                .one();
        if (existMember != null) {
            log.warn("用户已有科室，用户ID：{}，科室ID：{}", userId, existMember.getDepartmentId());
            throw new BusinessException("您已创建或加入了其他科室，一个用户只能创建或加入一个科室");
        }

        // 如果提供了创建者姓名，更新用户昵称
        if (request.getCreatorName() != null && !request.getCreatorName().trim().isEmpty()) {
            User user = userService.getById(Long.parseLong(userId));
            if (user != null) {
                user.setNickName(request.getCreatorName().trim());
                userService.updateById(user);
                log.info("更新创建者昵称：{}", request.getCreatorName());
            }
        }

        // 创建科室
        Department department = new Department();
        department.setName(request.getName());
        department.setCreatorId(Long.parseLong(userId));
        department.setMemberCount(1);
        department.setInviteCode(IdUtil.randomUUID().substring(0, 6).toUpperCase());
        save(department);
        log.debug("科室创建成功，科室ID：{}，邀请码：{}", department.getId(), department.getInviteCode());

        // 创建者加入科室
        DepartmentMember member = new DepartmentMember();
        member.setDepartmentId(department.getId());
        member.setUserId(Long.parseLong(userId));
        member.setJoinTime(LocalDate.now());
        member.setIsCreator(1);
        departmentMemberService.save(member);
        log.info("创建者加入科室成功，用户ID：{}", userId);

        // 创建初始班种
        if (request.getShifts() != null && !request.getShifts().isEmpty()) {
            for (DepartmentCreateRequest.ShiftInfo shiftInfo : request.getShifts()) {
                Shift shift = new Shift();
                shift.setDepartmentId(department.getId());
                shift.setName(shiftInfo.getName());
                shift.setCode(shiftInfo.getCode() != null ? shiftInfo.getCode() : shiftInfo.getName().substring(0, Math.min(2, shiftInfo.getName().length())));
                shift.setColor(shiftInfo.getColor() != null ? shiftInfo.getColor() : "#4A90D9");
                
                // 使用前端传入的时间段数据，如果没有则使用默认值
                if (shiftInfo.getTimeSlots() != null && !shiftInfo.getTimeSlots().isEmpty()) {
                    shift.setTimeSlots(shiftInfo.getTimeSlots());
                    shift.setDuration(shiftInfo.getDuration() != null ? shiftInfo.getDuration() : 0);
                } else {
                    // 默认时间段
                    TimeSlotDTO defaultSlot = new TimeSlotDTO();
                    defaultSlot.setStartTime("08:00");
                    defaultSlot.setEndTime("16:00");
                    defaultSlot.setStartIsNextDay(false);
                    defaultSlot.setEndIsNextDay(false);
                    shift.setTimeSlots(Collections.singletonList(defaultSlot));
                    shift.setDuration(8);
                }
                
                shiftService.save(shift);
                log.debug("创建班种：{}，时间段：{}", shiftInfo.getName(), shift.getTimeSlots());
            }
            log.info("创建初始班种成功，数量：{}", request.getShifts().size());
        }

        // 构建响应
        DepartmentResponse response = new DepartmentResponse();
        BeanUtil.copyProperties(department, response);
        response.setId(department.getId().toString());
        response.setCreatorId(department.getCreatorId().toString());

        log.info("科室创建完成，科室ID：{}", department.getId());
        return response;
    }

    @Override
    public DepartmentResponse getDepartmentInfo(String departmentId) {
        log.info("获取科室信息，科室ID：{}", departmentId);
        
        Department department = getById(Long.parseLong(departmentId));
        if (department == null) {
            log.warn("科室不存在，科室ID：{}", departmentId);
            return null;
        }
        
        DepartmentResponse response = new DepartmentResponse();
        BeanUtil.copyProperties(department, response);
        response.setId(department.getId().toString());
        response.setCreatorId(department.getCreatorId().toString());
        
        log.debug("科室信息查询成功，科室ID：{}", departmentId);
        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DepartmentResponse joinDepartment(String userId, JoinDepartmentRequest request) {
        log.info("加入科室，用户ID：{}，邀请码：{}", userId, request.getInviteCode());
        
        // 查找科室
        Department department = lambdaQuery().eq(Department::getInviteCode, request.getInviteCode()).one();
        
        if (department == null) {
            log.warn("邀请码无效：{}", request.getInviteCode());
            throw new BusinessException(ResultCode.INVITE_CODE_INVALID);
        }
        
        // 检查是否已加入
        DepartmentMember existMember = departmentMemberService.lambdaQuery()
                .eq(DepartmentMember::getDepartmentId, department.getId())
                .eq(DepartmentMember::getUserId, Long.parseLong(userId))
                .one();
        
        if (existMember != null) {
            log.warn("用户已在该科室，用户ID：{}，科室ID：{}", userId, department.getId());
            throw new BusinessException("您已在该科室");
        }
        
        // 加入科室
        DepartmentMember member = new DepartmentMember();
        member.setDepartmentId(department.getId());
        member.setUserId(Long.parseLong(userId));
        member.setJoinTime(LocalDate.now());
        member.setIsCreator(0);
        departmentMemberService.save(member);
        log.info("用户加入科室成功，用户ID：{}", userId);
        
        // 更新成员数量
        department.setMemberCount(department.getMemberCount() + 1);
        updateById(department);
        log.debug("科室成员数量更新，当前数量：{}", department.getMemberCount());
        
        return getDepartmentInfo(department.getId().toString());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void dismissDepartment(String userId, String departmentId) {
        log.info("解散科室，用户ID：{}，科室ID：{}", userId, departmentId);
        
        Department department = getById(Long.parseLong(departmentId));
        if (department == null) {
            log.warn("科室不存在，科室ID：{}", departmentId);
            throw new BusinessException("科室不存在");
        }
        
        // 验证权限
        if (!department.getCreatorId().toString().equals(userId)) {
            log.warn("无权限解散科室，用户ID：{}", userId);
            throw new BusinessException(ResultCode.NO_PERMISSION);
        }
        
        // 删除科室成员
        departmentMemberService.lambdaUpdate()
                .eq(DepartmentMember::getDepartmentId, Long.parseLong(departmentId))
                .remove();
        log.debug("删除科室成员成功，科室ID：{}", departmentId);
        
        // 删除科室
        removeById(Long.parseLong(departmentId));
        log.info("科室解散成功，科室ID：{}", departmentId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void transferDepartment(String userId, String departmentId, TransferDepartmentRequest request) {
        log.info("转让科室，用户ID：{}，科室ID：{}，新创建者ID：{}", userId, departmentId, request.getNewCreatorId());
        
        Department department = getById(Long.parseLong(departmentId));
        if (department == null) {
            log.warn("科室不存在，科室ID：{}", departmentId);
            throw new BusinessException("科室不存在");
        }
        
        // 验证权限
        if (!department.getCreatorId().toString().equals(userId)) {
            log.warn("无权限转让科室，用户ID：{}", userId);
            throw new BusinessException(ResultCode.NO_PERMISSION);
        }
        
        // 更新科室创建者
        department.setCreatorId(Long.parseLong(request.getNewCreatorId()));
        updateById(department);
        log.debug("科室创建者更新成功，新创建者ID：{}", request.getNewCreatorId());
        
        // 更新成员关系
        DepartmentMember oldCreator = departmentMemberService.lambdaQuery()
                .eq(DepartmentMember::getDepartmentId, Long.parseLong(departmentId))
                .eq(DepartmentMember::getUserId, Long.parseLong(userId))
                .one();
        if (oldCreator != null) {
            oldCreator.setIsCreator(0);
            departmentMemberService.updateById(oldCreator);
        }
        
        DepartmentMember newCreator = departmentMemberService.lambdaQuery()
                .eq(DepartmentMember::getDepartmentId, Long.parseLong(departmentId))
                .eq(DepartmentMember::getUserId, Long.parseLong(request.getNewCreatorId()))
                .one();
        if (newCreator != null) {
            newCreator.setIsCreator(1);
            departmentMemberService.updateById(newCreator);
        }
        
        log.info("科室转让成功，科室ID：{}", departmentId);
    }

    @Override
    public List<MemberInfoResponse> getMemberList(String departmentId) {
        log.info("获取成员列表，科室ID：{}", departmentId);
        
        List<DepartmentMember> members = departmentMemberService.lambdaQuery()
                .eq(DepartmentMember::getDepartmentId, Long.parseLong(departmentId))
                .orderByAsc(DepartmentMember::getJoinTime)
                .list();
        
        List<MemberInfoResponse> responseList = new ArrayList<>();
        for (DepartmentMember member : members) {
            User user = userService.getById(member.getUserId());
            if (user != null) {
                MemberInfoResponse response = new MemberInfoResponse();
                BeanUtil.copyProperties(user, response);
                response.setId(user.getId().toString());
                response.setJoinTime(member.getJoinTime());
                response.setIsCreator(member.getIsCreator() == 1);
                responseList.add(response);
            }
        }
        
        log.debug("成员列表查询成功，成员数量：{}", responseList.size());
        return responseList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void kickMember(String userId, String departmentId, KickMemberRequest request) {
        log.info("踢出成员，操作者ID：{}，科室ID：{}，被踢出成员ID：{}", userId, departmentId, request.getMemberId());
        
        Department department = getById(Long.parseLong(departmentId));
        if (department == null) {
            log.warn("科室不存在，科室ID：{}", departmentId);
            throw new BusinessException("科室不存在");
        }
        
        // 验证权限
        if (!department.getCreatorId().toString().equals(userId)) {
            log.warn("无权限踢出成员，用户ID：{}", userId);
            throw new BusinessException(ResultCode.NO_PERMISSION);
        }
        
        // 删除成员
        departmentMemberService.lambdaUpdate()
                .eq(DepartmentMember::getDepartmentId, Long.parseLong(departmentId))
                .eq(DepartmentMember::getUserId, Long.parseLong(request.getMemberId()))
                .remove();
        log.info("成员踢出成功，成员ID：{}", request.getMemberId());
        
        // 更新成员数量
        department.setMemberCount(department.getMemberCount() - 1);
        updateById(department);
    }

    @Override
    public String getInviteLink(String departmentId) {
        log.info("获取邀请链接，科室ID：{}", departmentId);
        
        Department department = getById(Long.parseLong(departmentId));
        if (department == null) {
            log.warn("科室不存在，科室ID：{}", departmentId);
            return null;
        }
        
        log.debug("邀请码：{}", department.getInviteCode());
        return department.getInviteCode();
    }

    @Override
    public MemberInfoResponse getMemberInfo(String memberId) {
        log.info("获取成员详情，成员ID：{}", memberId);
        
        User user = userService.getById(Long.parseLong(memberId));
        if (user == null) {
            log.warn("成员不存在，成员ID：{}", memberId);
            return null;
        }
        
        // 查找成员所属科室
        DepartmentMember member = departmentMemberService.lambdaQuery()
                .eq(DepartmentMember::getUserId, Long.parseLong(memberId))
                .last("LIMIT 1")
                .one();
        
        MemberInfoResponse response = new MemberInfoResponse();
        BeanUtil.copyProperties(user, response);
        response.setId(user.getId().toString());
        
        if (member != null) {
            response.setJoinTime(member.getJoinTime());
            response.setIsCreator(member.getIsCreator() == 1);
        }
        
        log.debug("成员详情查询成功，成员ID：{}", memberId);
        return response;
    }

    @Override
    public MemberInfoResponse updateMemberInfo(String memberId, UserUpdateRequest request) {
        log.info("更新成员信息，成员ID：{}", memberId);
        
        User user = userService.getById(Long.parseLong(memberId));
        if (user == null) {
            log.warn("成员不存在，成员ID：{}", memberId);
            throw new BusinessException(ResultCode.NOT_FOUND, "成员不存在");
        }
        
        // 更新用户信息
        if (request.getNickName() != null) {
            user.setNickName(request.getNickName());
        }
        if (request.getAvatarUrl() != null) {
            user.setAvatarUrl(request.getAvatarUrl());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }
        if (request.getWorkNo() != null) {
            user.setWorkNo(request.getWorkNo());
        }
        if (request.getTitle() != null) {
            user.setTitle(request.getTitle());
        }
        if (request.getSeniority() != null) {
            user.setSeniority(request.getSeniority());
        }
        if (request.getRemark() != null) {
            user.setRemark(request.getRemark());
        }
        
        userService.updateById(user);
        log.info("成员信息更新成功，成员ID：{}", memberId);
        
        // 返回更新后的信息
        return getMemberInfo(memberId);
    }
}
