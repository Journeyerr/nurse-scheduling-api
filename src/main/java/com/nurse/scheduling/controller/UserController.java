package com.nurse.scheduling.controller;

import com.nurse.scheduling.common.Result;
import com.nurse.scheduling.context.UserContext;
import com.nurse.scheduling.dto.user.UserLoginRequest;
import com.nurse.scheduling.dto.user.UserLoginResponse;
import com.nurse.scheduling.dto.user.UserInfoResponse;
import com.nurse.scheduling.dto.user.UserUpdateRequest;
import com.nurse.scheduling.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 用户控制器
 *
 * @author nurse-scheduling
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 微信登录
     */
    @PostMapping("/wxLogin")
    public Result<UserLoginResponse> wxLogin(@Valid @RequestBody UserLoginRequest request) {
        log.info("微信登录请求，code：{}", request.getCode());
        UserLoginResponse response = userService.wxLogin(request);
        return Result.ok(response);
    }

    /**
     * 获取用户信息
     */
    @GetMapping("/info")
    public Result<UserInfoResponse> getUserInfo() {
        String userId = UserContext.getUserId();
        log.info("获取用户信息，用户ID：{}", userId);
        UserInfoResponse response = userService.getUserInfo(userId);
        return Result.ok(response);
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/update")
    public Result<UserInfoResponse> updateUserInfo(@Valid @RequestBody UserUpdateRequest updateRequest) {
        String userId = UserContext.getUserId();
        log.info("更新用户信息，用户ID：{}", userId);
        UserInfoResponse response = userService.updateUserInfo(userId, updateRequest);
        return Result.ok(response);
    }
}
