package com.nurse.scheduling.dto.swap;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 换班申请DTO
 *
 * @author nurse-scheduling
 */
@Data
public class SwapRequestDto {

    /**
     * 我方日期
     */
    @NotBlank(message = "我方日期不能为空")
    private String myDate;

    /**
     * 目标日期
     */
    @NotBlank(message = "目标日期不能为空")
    private String targetDate;

    /**
     * 目标用户ID
     */
    @NotBlank(message = "换班对象不能为空")
    private String targetUserId;

    /**
     * 我方班次ID
     */
    private Long myShiftId;

    /**
     * 目标班次ID
     */
    private Long targetShiftId;

    /**
     * 备注
     */
    private String remark;
}
