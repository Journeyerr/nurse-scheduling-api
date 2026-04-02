package com.nurse.scheduling.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 申请实体类（期望排班、换班、请假等）
 *
 * @author nurse-scheduling
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_expect_schedule")
public class ExpectSchedule extends BaseEntity {

    /**
     * 科室ID
     */
    private Long departmentId;

    /**
     * 申请人ID
     */
    private Long userId;

    /**
     * 申请人姓名
     */
    private String userName;

    /**
     * 申请类型：schedule-期望排班, swap-换班, leave-请假
     */
    private String type;

    /**
     * 期望班种ID（期望排班时使用）
     */
    private Long shiftId;

    /**
     * 我方班次ID（换班时使用）
     */
    private Long myShiftId;

    /**
     * 目标班次ID（换班时使用）
     */
    private Long targetShiftId;

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
    private Long targetUserId;

    /**
     * 目标用户姓名
     */
    private String targetUserName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态：pending-待审批, approved-已通过, rejected-已拒绝
     */
    private String status;

    /**
     * 审批人ID
     */
    private Long approverId;

    /**
     * 审批时间
     */
    private LocalDate approveTime;

    /**
     * 审批意见
     */
    private String approveRemark;
}
