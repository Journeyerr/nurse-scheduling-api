package com.nurse.scheduling.dto.expect;

import lombok.Data;

import java.time.LocalDate;

/**
 * 申请响应DTO（期望排班、换班等）
 *
 * @author nurse-scheduling
 */
@Data
public class ExpectScheduleResponse {

    /**
     * 申请ID
     */
    private String id;

    /**
     * 申请类型：schedule-期望排班, swap-换班
     */
    private String type;

    /**
     * 申请人ID
     */
    private String userId;

    /**
     * 申请人姓名
     */
    private String userName;

    /**
     * 期望班种ID（期望排班时使用）
     */
    private String shiftId;

    /**
     * 我方班次ID（换班时使用）
     */
    private String myShiftId;

    /**
     * 目标班次ID（换班时使用）
     */
    private String targetShiftId;

    /**
     * 开始日期/我方日期
     */
    private LocalDate startDate;

    /**
     * 结束日期
     */
    private LocalDate endDate;

    /**
     * 目标日期（换班时使用）
     */
    private LocalDate targetDate;

    /**
     * 目标用户ID（换班对象）
     */
    private String targetUserId;

    /**
     * 目标用户姓名
     */
    private String targetUserName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 审批意见
     */
    private String approveRemark;

    /**
     * 状态
     */
    private String status;

    /**
     * 提交时间
     */
    private String createTime;
}
