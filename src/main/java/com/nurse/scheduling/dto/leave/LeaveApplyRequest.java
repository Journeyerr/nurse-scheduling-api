package com.nurse.scheduling.dto.leave;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 提交假勤申请请求DTO
 *
 * @author nurse-scheduling
 */
@Data
public class LeaveApplyRequest {

    /**
     * 类型：leave-请假, transfer-调班, exchange-换班
     */
    @NotBlank(message = "类型不能为空")
    private String type;

    /**
     * 开始日期
     */
    @NotNull(message = "开始日期不能为空")
    private LocalDate startDate;

    /**
     * 结束日期
     */
    @NotNull(message = "结束日期不能为空")
    private LocalDate endDate;

    /**
     * 换班对象ID（换班时使用）
     */
    private String targetMemberId;

    /**
     * 事由
     */
    @NotBlank(message = "事由不能为空")
    private String reason;
}
