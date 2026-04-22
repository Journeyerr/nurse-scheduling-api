package com.nurse.scheduling.dto.schedule;

import lombok.Data;

/**
 * 每日排班响应VO
 *
 * @author nurse-scheduling
 */
@Data
public class DailyScheduleVO {

    /**
     * 成员ID
     */
    private String memberId;

    /**
     * 成员姓名
     */
    private String memberName;

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
