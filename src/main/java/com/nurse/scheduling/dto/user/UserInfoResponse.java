package com.nurse.scheduling.dto.user;

import lombok.Data;

/**
 * 用户信息响应DTO
 *
 * @author nurse-scheduling
 */
@Data
public class UserInfoResponse {

    /**
     * 用户ID
     */
    private String id;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像URL
     */
    private String avatarUrl;

    /**
     * 电话号码
     */
    private String phone;

    /**
     * 工号
     */
    private String workNo;

    /**
     * 职称
     */
    private String title;

    /**
     * 年资（年）
     */
    private Integer seniority;

    /**
     * 备注
     */
    private String remark;
}
