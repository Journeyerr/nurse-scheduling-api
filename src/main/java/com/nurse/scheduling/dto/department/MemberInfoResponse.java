package com.nurse.scheduling.dto.department;

import lombok.Data;

import java.time.LocalDate;

/**
 * 成员信息响应DTO
 *
 * @author nurse-scheduling
 */
@Data
public class MemberInfoResponse {

    /**
     * 成员ID
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
     * 加入时间
     */
    private LocalDate joinTime;

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

    /**
     * 是否为创建者
     */
    private Boolean isCreator;
}
