package com.nurse.scheduling.dto.schedule;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

/**
 * 排班信息响应DTO
 *
 * @author nurse-scheduling
 */
@Data
public class ScheduleResponse {

    /**
     * 排班ID
     */
    private String id;

    /**
     * 成员ID
     */
    private String memberId;

    /**
     * 日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    /**
     * 班种ID
     */
    private String shiftId;

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
