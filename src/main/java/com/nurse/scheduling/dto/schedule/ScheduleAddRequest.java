package com.nurse.scheduling.dto.schedule;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 添加/更新排班请求DTO
 *
 * @author nurse-scheduling
 */
@Data
public class ScheduleAddRequest {

    /**
     * 排班ID（更新时使用）
     */
    private String id;

    /**
     * 成员ID
     */
    @NotBlank(message = "成员ID不能为空")
    private String memberId;

    /**
     * 日期
     */
    @NotNull(message = "日期不能为空")
    private LocalDate date;

    /**
     * 班种ID
     */
    @NotBlank(message = "班种ID不能为空")
    private String shiftId;

    /**
     * 成员姓名（可选，用于错误提示）
     */
    private String memberName;
}
