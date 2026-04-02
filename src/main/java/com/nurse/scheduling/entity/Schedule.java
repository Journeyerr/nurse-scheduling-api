package com.nurse.scheduling.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 排班实体类
 *
 * @author nurse-scheduling
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_schedule")
public class Schedule extends BaseEntity {

    /**
     * 科室ID
     */
    private Long departmentId;

    /**
     * 成员ID
     */
    private Long memberId;

    /**
     * 日期
     */
    private LocalDate date;

    /**
     * 班种ID
     */
    private Long shiftId;

    /**
     * 班种编号
     */
    private String shiftCode;

    /**
     * 班种名称
     */
    private String shiftName;

    /**
     * 班种颜色
     */
    private String shiftColor;
}
