package com.nurse.scheduling.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nurse.scheduling.dto.user.*;
import com.nurse.scheduling.entity.Department;
import com.nurse.scheduling.entity.DepartmentMember;
import com.nurse.scheduling.entity.User;
import com.nurse.scheduling.mapper.UserMapper;
import com.nurse.scheduling.service.DepartmentMemberService;
import com.nurse.scheduling.service.DepartmentService;
import com.nurse.scheduling.service.UserService;
import com.nurse.scheduling.service.WxLoginService;
import com.nurse.scheduling.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 用户服务实现类
 *
 * @author nurse-scheduling
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private WxLoginService wxLoginService;

    @Autowired
    private DepartmentMemberService departmentMemberService;

    @Lazy
    @Autowired
    private DepartmentService departmentService;

    @Override
    public UserLoginResponse wxLogin(UserLoginRequest request) {
        log.info("微信登录，code：{}", request.getCode());
        
        // 调用微信API获取openid
        Map<String, String> wxResult = wxLoginService.getOpenidByCode(request.getCode());
        String openid = wxResult.get("openid");
        log.debug("获取openid：{}", openid);
        
        // 查询用户是否存在
        User user = lambdaQuery().eq(User::getOpenid, openid).one();
        
        if (user == null) {
            // 新用户，创建账号
            user = new User();
            user.setOpenid(openid);
            // 昵称为空或空字符串时使用默认值"微信用户"
            String nickName = request.getNickName();
            user.setNickName((nickName != null && !nickName.trim().isEmpty()) ? nickName : "微信用户");
            user.setAvatarUrl(request.getAvatarUrl());
            save(user);
            log.info("创建新用户，用户ID：{}，昵称：{}", user.getId(), user.getNickName());
        } else {
            // 老用户：只更新头像，不更新昵称（保护用户已修改的昵称）
            boolean needUpdate = false;
            // 更新头像（如果传入了新的头像）
            if (request.getAvatarUrl() != null && !request.getAvatarUrl().equals(user.getAvatarUrl())) {
                user.setAvatarUrl(request.getAvatarUrl());
                needUpdate = true;
            }
            if (needUpdate) {
                updateById(user);
            }
            log.info("用户已存在，用户ID：{}，昵称：{}（保留用户昵称，不更新）", user.getId(), user.getNickName());
        }
        
        // 生成token
        String token = jwtUtil.generateToken(user.getId().toString());
        log.debug("生成token：{}", token);
        
        // 构建响应
        UserLoginResponse response = new UserLoginResponse();
        response.setToken(token);
        response.setId(user.getId().toString());
        response.setNickName(user.getNickName());
        response.setAvatarUrl(user.getAvatarUrl());
        
        // 查询用户所属科室（一个用户只能加入一个科室）
        DepartmentMember member = departmentMemberService.lambdaQuery()
                .eq(DepartmentMember::getUserId, user.getId())
                .one();
        if (member != null) {
            Department department = departmentService.getById(member.getDepartmentId());
            if (department != null) {
                UserLoginResponse.DepartmentInfo deptInfo = new UserLoginResponse.DepartmentInfo();
                deptInfo.setId(department.getId().toString());
                deptInfo.setName(department.getName());
                deptInfo.setCreatorId(department.getCreatorId().toString());
                deptInfo.setInviteCode(department.getInviteCode());
                deptInfo.setMemberCount(department.getMemberCount());
                deptInfo.setIsCreator(member.getIsCreator() == 1);
                response.setDepartment(deptInfo);
                log.debug("用户已加入科室，科室ID：{}，是否为创建者：{}", department.getId(), member.getIsCreator() == 1);
            }
        }
        
        log.info("登录成功，用户ID：{}", user.getId());
        return response;
    }

    @Override
    public UserInfoResponse getUserInfo(String userId) {
        log.info("获取用户信息，用户ID：{}", userId);
        
        User user = getById(Long.parseLong(userId));
        if (user == null) {
            log.warn("用户不存在，用户ID：{}", userId);
            return null;
        }
        
        UserInfoResponse response = new UserInfoResponse();
        BeanUtil.copyProperties(user, response);
        response.setId(user.getId().toString());
        
        log.debug("用户信息查询成功，用户ID：{}", userId);
        return response;
    }

    @Override
    public UserInfoResponse updateUserInfo(String userId, UserUpdateRequest request) {
        log.info("更新用户信息，用户ID：{}", userId);
        
        User user = getById(Long.parseLong(userId));
        if (user == null) {
            log.warn("用户不存在，用户ID：{}", userId);
            return null;
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
        
        updateById(user);
        log.info("用户信息更新成功，用户ID：{}", userId);
        
        // 返回更新后的信息
        return getUserInfo(userId);
    }
}

