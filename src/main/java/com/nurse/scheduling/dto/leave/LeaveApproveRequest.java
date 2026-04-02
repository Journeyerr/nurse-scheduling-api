package com.nurse.scheduling.dto.leave;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 审批假勤申请请求DTO
 *
 * @author nurse-scheduling
 */
@Data
public class LeaveApproveRequest {

    /**
     * 申请ID
     */
    @NotBlank(message = "申请ID不能为空")
    private String id;

    /**
     * 状态：approved-已通过, rejected-已拒绝
     */
    @NotBlank(message = "审批状态不能为空")
    private String status;

    /**
     * 审批意见
     */
    private String approveRemark;
}
