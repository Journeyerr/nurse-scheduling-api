package com.nurse.scheduling.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 假勤申请实体类
 *
 * @author nurse-scheduling
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_leave_apply")
public class LeaveApply extends BaseEntity {

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
     * 类型：leave-请假, transfer-调班, exchange-换班
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
     * 换班对象ID（换班时使用）
     */
    private Long targetMemberId;

    /**
     * 事由
     */
    private String reason;

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
