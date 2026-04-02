package com.nurse.scheduling.dto.department;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 科室信息响应DTO
 *
 * @author nurse-scheduling
 */
@Data
public class DepartmentResponse {

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
     * 成员数量
     */
    private Integer memberCount;

    /**
     * 邀请码
     */
    private String inviteCode;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
