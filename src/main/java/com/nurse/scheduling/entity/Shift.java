package com.nurse.scheduling.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.nurse.scheduling.config.TimeSlotListTypeHandler;
import com.nurse.scheduling.dto.shift.TimeSlotDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 班种实体类
 *
 * @author nurse-scheduling
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "biz_shift", autoResultMap = true)
public class Shift extends BaseEntity {

    /**
     * 科室ID
     */
    private Long departmentId;

    /**
     * 班种编号（显示在日历上）
     */
    private String code;

    /**
     * 班种名称
     */
    private String name;

    /**
     * 时间段列表（支持多段班）
     * 格式：[{"startTime":"08:00","endTime":"16:00","startIsNextDay":false,"endIsNextDay":false}]
     */
    @TableField(typeHandler = TimeSlotListTypeHandler.class)
    private List<TimeSlotDTO> timeSlots;

    /**
     * 时长（小时）
     */
    private Integer duration;

    /**
     * 显示颜色
     */
    private String color;
}
