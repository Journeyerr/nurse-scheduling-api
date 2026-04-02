package com.nurse.scheduling.dto.user;

import lombok.Data;

/**
 * 用户登录响应DTO
 *
 * @author nurse-scheduling
 */
@Data
public class UserLoginResponse {

    /**
     * token
     */
    private String token;

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
     * 科室信息（如果用户已加入科室）
     */
    private DepartmentInfo department;

    /**
     * 科室信息内部类
     */
    @Data
    public static class DepartmentInfo {
        /**
         * 科室ID
         */
        private String id;

        /**
         * 科室名称
         */
        private String name;

        /**
         * 创建者ID
         */
        private String creatorId;

        /**
         * 邀请码
         */
        private String inviteCode;

        /**
         * 成员数量
         */
        private Integer memberCount;

        /**
         * 当前用户是否为创建者
         */
        private Boolean isCreator;
    }
}
