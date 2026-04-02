package com.nurse.scheduling.dto.expect;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 提交期望排班请求DTO
 *
 * @author nurse-scheduling
 */
@Data
public class ExpectScheduleRequest {

    /**
     * 期望班种ID
     */
    @NotBlank(message = "班种ID不能为空")
    private String shiftId;

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
     * 备注
     */
    private String remark;
}
