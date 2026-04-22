package com.nurse.scheduling.dto.swap;

import lombok.Data;

/**
 * 指定日期排班用户响应VO
 *
 * @author nurse-scheduling
 */
@Data
public class UserScheduleVO {

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String nickName;

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
