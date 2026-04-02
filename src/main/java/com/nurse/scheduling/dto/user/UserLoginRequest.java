package com.nurse.scheduling.dto.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 用户登录请求DTO
 *
 * @author nurse-scheduling
 */
@Data
public class UserLoginRequest {

    /**
     * 微信code
     */
    @NotBlank(message = "微信code不能为空")
    private String code;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像URL
     */
    private String avatarUrl;
}
