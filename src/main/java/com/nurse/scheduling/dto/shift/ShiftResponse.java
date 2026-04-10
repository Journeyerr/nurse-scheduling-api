package com.nurse.scheduling.dto.shift;

import lombok.Data;

import java.util.List;

/**
 * 班种信息响应DTO
 *
 * @author nurse-scheduling
 */
@Data
public class ShiftResponse {

    /**
     * 班种ID
     */
    private String id;

    /**
     * 班种编号
     */
    private String code;

    /**
     * 班种名称
     */
    private String name;

    /**
     * 时间段列表
     */
    private List<TimeSlotDTO> timeSlots;

    /**
     * 时长（小时）
     */
    private Integer duration;

    /**
     * 显示颜色
     */
    private String color;

    /**
     * 班种系数（默认1，用于酬劳计算）
     */
    private Double coefficient = 1.0;

    /**
     * 是否为休息班
     */
    private Boolean isRest = false;
}
