package com.nurse.scheduling.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nurse.scheduling.dto.user.UserLoginRequest;
import com.nurse.scheduling.dto.user.UserLoginResponse;
import com.nurse.scheduling.dto.user.UserInfoResponse;
import com.nurse.scheduling.dto.user.UserUpdateRequest;
import com.nurse.scheduling.entity.User;

/**
 * 用户服务接口
 *
 * @author nurse-scheduling
 */
public interface UserService extends IService<User> {

    /**
     * 微信登录
     *
     * @param request 登录请求
     * @return 登录响应
     */
    UserLoginResponse wxLogin(UserLoginRequest request);

    /**
     * 获取用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    UserInfoResponse getUserInfo(String userId);

    /**
     * 更新用户信息
     *
     * @param userId 用户ID
     * @param request 更新请求
     * @return 用户信息
     */
    UserInfoResponse updateUserInfo(String userId, UserUpdateRequest request);
}

