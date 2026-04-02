package com.nurse.scheduling.dto.expect;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 审批期望排班请求DTO
 *
 * @author nurse-scheduling
 */
@Data
public class ExpectApproveRequest {

    /**
     * 期望ID
     */
    @NotBlank(message = "期望ID不能为空")
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
