package com.nurse.scheduling.dto.leave;

import lombok.Data;

import java.time.LocalDate;

/**
 * 假勤申请响应DTO
 *
 * @author nurse-scheduling
 */
@Data
public class LeaveApplyResponse {

    /**
     * 申请ID
     */
    private String id;

    /**
     * 申请人ID
     */
    private String userId;

    /**
     * 申请人姓名
     */
    private String userName;

    /**
     * 类型
     */
    private String type;

    /**
     * 开始日期
     */
    private LocalDate startDate;

    /**
     * 结束日期
     */
    private LocalDate endDate;

    /**
     * 天数
     */
    private Integer days;

    /**
     * 事由
     */
    private String reason;

    /**
     * 状态
     */
    private String status;
}
