package com.nurse.scheduling.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一响应结果
 *
 * @author nurse-scheduling
 */
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 是否成功
     */
    private Boolean success;

    /**
     * 消息
     */
    private String message;

    /**
     * 数据
     */
    private T data;

    public Result() {
    }

    public Result(Integer code, Boolean success, String message, T data) {
        this.code = code;
        this.success = success;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功返回
     */
    public static <T> Result<T> ok() {
        return new Result<>(200, true, "SUCCESS", null);
    }

    /**
     * 成功返回（带数据）
     */
    public static <T> Result<T> ok(T data) {
        return new Result<>(200, true, "SUCCESS", data);
    }

    /**
     * 成功返回（带消息和数据）
     */
    public static <T> Result<T> ok(String message, T data) {
        return new Result<>(200, true, message, data);
    }

    /**
     * 失败返回
     */
    public static <T> Result<T> fail() {
        return new Result<>(500, false, "ERROR", null);
    }

    /**
     * 失败返回（带消息）
     */
    public static <T> Result<T> fail(String message) {
        return new Result<>(500, false, message, null);
    }

    /**
     * 失败返回（带状态码和消息）
     */
    public static <T> Result<T> fail(Integer code, String message) {
        return new Result<>(code, false, message, null);
    }

    /**
     * 自定义返回
     */
    public static <T> Result<T> build(Integer code, Boolean success, String message, T data) {
        return new Result<>(code, success, message, data);
    }
}
