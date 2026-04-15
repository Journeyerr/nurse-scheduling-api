package com.nurse.scheduling.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nurse.scheduling.dto.department.*;
import com.nurse.scheduling.dto.user.UserUpdateRequest;
import com.nurse.scheduling.entity.Department;

import java.util.List;

/**
 * 科室服务接口
 *
 * @author nurse-scheduling
 */
public interface DepartmentService extends IService<Department> {

    /**
     * 创建科室
     *
     * @param userId 用户ID
     * @param request 创建请求
     * @return 科室信息
     */
    DepartmentResponse createDepartment(String userId, DepartmentCreateRequest request);

    /**
     * 获取科室信息
     *
     * @param departmentId 科室ID
     * @return 科室信息
     */
    DepartmentResponse getDepartmentInfo(String departmentId);

    /**
     * 加入科室
     *
     * @param userId 用户ID
     * @param request 加入请求
     * @return 科室信息
     */
    DepartmentResponse joinDepartment(String userId, JoinDepartmentRequest request);

    /**
     * 解散科室
     *
     * @param userId 用户ID
     * @param departmentId 科室ID
     */
    void dismissDepartment(String userId, String departmentId);

    /**
     * 转让科室
     *
     * @param userId 用户ID
     * @param departmentId 科室ID
     * @param request 转让请求
     */
    void transferDepartment(String userId, String departmentId, TransferDepartmentRequest request);

    /**
     * 获取成员列表
     *
     * @param departmentId 科室ID
     * @return 成员列表
     */
    List<MemberInfoResponse> getMemberList(String departmentId);

    /**
     * 踢出成员
     *
     * @param userId 用户ID
     * @param departmentId 科室ID
     * @param request 踢出请求
     */
    void kickMember(String userId, String departmentId, KickMemberRequest request);

    /**
     * 获取邀请链接
     *
     * @param departmentId 科室ID
     * @return 邀请码
     */
    String getInviteLink(String departmentId);

    /**
     * 获取成员详情
     *
     * @param memberId 成员ID
     * @return 成员信息
     */
    MemberInfoResponse getMemberInfo(String memberId);

    /**
     * 更新成员信息
     *
     * @param memberId 成员ID
     * @param request 更新请求
     * @return 成员信息
     */
    MemberInfoResponse updateMemberInfo(String memberId, UserUpdateRequest request);

    /**
     * 退出科室
     *
     * @param userId 用户ID
     * @param departmentId 科室ID
     */
    void quitDepartment(String userId, String departmentId);

    /**
     * 设置管理员
     *
     * @param userId 操作者ID（仅创建者可操作）
     * @param departmentId 科室ID
     * @param request 管理员设置请求
     */
    void setAdmin(String userId, String departmentId, SetAdminRequest request);
}

