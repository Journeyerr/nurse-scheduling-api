package com.nurse.scheduling.controller;

import com.nurse.scheduling.common.Result;
import com.nurse.scheduling.context.UserContext;
import com.nurse.scheduling.dto.department.*;
import com.nurse.scheduling.dto.user.UserUpdateRequest;
import com.nurse.scheduling.service.DepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 科室控制器
 *
 * @author nurse-scheduling
 */
@Slf4j
@RestController
@RequestMapping("/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    /**
     * 创建科室
     */
    @PostMapping("/create")
    public Result<DepartmentResponse> createDepartment(@Valid @RequestBody DepartmentCreateRequest createRequest) {
        String userId = UserContext.getUserId();
        log.info("创建科室，用户ID：{}", userId);
        DepartmentResponse response = departmentService.createDepartment(userId, createRequest);
        return Result.ok(response);
    }

    /**
     * 获取科室信息
     */
    @GetMapping("/info")
    public Result<DepartmentResponse> getDepartmentInfo(@RequestParam String departmentId) {
        log.info("获取科室信息，科室ID：{}", departmentId);
        DepartmentResponse response = departmentService.getDepartmentInfo(departmentId);
        return Result.ok(response);
    }

    /**
     * 加入科室
     */
    @PostMapping("/join")
    public Result<DepartmentResponse> joinDepartment(@Valid @RequestBody JoinDepartmentRequest joinRequest) {
        String userId = UserContext.getUserId();
        log.info("加入科室，用户ID：{}", userId);
        DepartmentResponse response = departmentService.joinDepartment(userId, joinRequest);
        return Result.ok(response);
    }

    /**
     * 解散科室
     */
    @DeleteMapping("/dismiss")
    public Result<Void> dismissDepartment(@RequestParam String departmentId) {
        String userId = UserContext.getUserId();
        log.info("解散科室，用户ID：{}，科室ID：{}", userId, departmentId);
        departmentService.dismissDepartment(userId, departmentId);
        return Result.ok();
    }

    /**
     * 转让科室
     */
    @PostMapping("/transfer")
    public Result<Void> transferDepartment(@RequestParam String departmentId,
                                            @Valid @RequestBody TransferDepartmentRequest transferRequest) {
        String userId = UserContext.getUserId();
        log.info("转让科室，用户ID：{}，科室ID：{}", userId, departmentId);
        departmentService.transferDepartment(userId, departmentId, transferRequest);
        return Result.ok();
    }

    /**
     * 获取成员列表
     */
    @GetMapping("/members")
    public Result<List<MemberInfoResponse>> getMemberList(@RequestParam String departmentId) {
        log.info("获取成员列表，科室ID：{}", departmentId);
        List<MemberInfoResponse> response = departmentService.getMemberList(departmentId);
        return Result.ok(response);
    }

    /**
     * 踢出成员
     */
    @PostMapping("/kick")
    public Result<Void> kickMember(@RequestParam String departmentId,
                                    @Valid @RequestBody KickMemberRequest kickRequest) {
        String userId = UserContext.getUserId();
        log.info("踢出成员，操作者ID：{}，科室ID：{}", userId, departmentId);
        departmentService.kickMember(userId, departmentId, kickRequest);
        return Result.ok();
    }

    /**
     * 获取邀请链接
     */
    @GetMapping("/invite")
    public Result<String> getInviteLink(@RequestParam String departmentId) {
        log.info("获取邀请链接，科室ID：{}", departmentId);
        String inviteCode = departmentService.getInviteLink(departmentId);
        return Result.ok(inviteCode);
    }

    /**
     * 获取成员详情
     */
    @GetMapping("/member/{memberId}")
    public Result<MemberInfoResponse> getMemberInfo(@PathVariable String memberId) {
        log.info("获取成员详情，成员ID：{}", memberId);
        MemberInfoResponse response = departmentService.getMemberInfo(memberId);
        return Result.ok(response);
    }

    /**
     * 更新成员信息
     */
    @PutMapping("/member/{memberId}")
    public Result<MemberInfoResponse> updateMemberInfo(@PathVariable String memberId,
                                                        @Valid @RequestBody UserUpdateRequest updateRequest) {
        log.info("更新成员信息，成员ID：{}", memberId);
        MemberInfoResponse response = departmentService.updateMemberInfo(memberId, updateRequest);
        return Result.ok(response);
    }

    /**
     * 退出科室
     */
    @PostMapping("/quit")
    public Result<Void> quitDepartment(@RequestParam String departmentId) {
        String userId = UserContext.getUserId();
        log.info("退出科室，用户ID：{}，科室ID：{}", userId, departmentId);
        departmentService.quitDepartment(userId, departmentId);
        return Result.ok();
    }
}
