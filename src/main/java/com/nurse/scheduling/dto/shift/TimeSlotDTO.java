package com.nurse.scheduling.dto.shift;

import lombok.Data;

/**
 * 时间段DTO
 *
 * @author nurse-scheduling
 */
@Data
public class TimeSlotDTO {

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 开始是否次日
     */
    private Boolean startIsNextDay;

    /**
     * 结束是否次日
     */
    private Boolean endIsNextDay;
}
