package com.nurse.scheduling.dto.shift;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 创建/更新班种请求DTO
 *
 * @author nurse-scheduling
 */
@Data
public class ShiftCreateRequest {

    /**
     * 班种ID（更新时使用）
     */
    private String id;

    /**
     * 班种编号
     */
    @NotBlank(message = "班种编号不能为空")
    private String code;

    /**
     * 班种名称
     */
    @NotBlank(message = "班种名称不能为空")
    private String name;

    /**
     * 时间段列表
     */
    @NotNull(message = "时间段不能为空")
    private List<TimeSlotDTO> timeSlots;

    /**
     * 时长（小时）
     */
    @NotNull(message = "时长不能为空")
    private Integer duration;

    /**
     * 显示颜色
     */
    @NotBlank(message = "颜色不能为空")
    private String color;
}
