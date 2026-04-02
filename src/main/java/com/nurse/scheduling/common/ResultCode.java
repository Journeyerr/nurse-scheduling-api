package com.nurse.scheduling.common;

import lombok.Getter;

/**
 * 响应状态码枚举
 *
 * @author nurse-scheduling
 */
@Getter
public enum ResultCode {

    /**
     * 操作成功
     */
    SUCCESS(200, "操作成功"),

    /**
     * 操作失败
     */
    FAILED(500, "操作失败"),

    /**
     * 参数错误
     */
    PARAM_ERROR(400, "参数错误"),

    /**
     * 未授权
     */
    UNAUTHORIZED(401, "未授权"),

    /**
     * 禁止访问
     */
    FORBIDDEN(403, "禁止访问"),

    /**
     * 资源不存在
     */
    NOT_FOUND(404, "资源不存在"),

    /**
     * 用户名或密码错误
     */
    LOGIN_ERROR(4001, "用户名或密码错误"),

    /**
     * token无效
     */
    TOKEN_INVALID(4002, "token无效"),

    /**
     * token已过期
     */
    TOKEN_EXPIRED(4003, "token已过期"),

    /**
     * 用户已存在
     */
    USER_EXISTS(5001, "用户已存在"),

    /**
     * 科室已存在
     */
    DEPARTMENT_EXISTS(5002, "科室已存在"),

    /**
     * 邀请码无效
     */
    INVITE_CODE_INVALID(5003, "邀请码无效"),

    /**
     * 无权限操作
     */
    NO_PERMISSION(5004, "无权限操作");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
