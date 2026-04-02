package com.nurse.scheduling.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户实体类
 *
 * @author nurse-scheduling
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
public class User extends BaseEntity {

    /**
     * 微信openid
     */
    private String openid;

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
