package com.nurse.scheduling.dto.statistics;

import lombok.Data;

import java.util.List;

/**
 * 个人统计响应DTO
 *
 * @author nurse-scheduling
 */
@Data
public class MyStatisticsResponse {

    /**
     * 总工作天数
     */
    private Integer totalDays;

    /**
     * 休息天数
     */
    private Integer restDays;

    /**
     * 请假天数
     */
    private Integer leaveDays;

    /**
     * 各班种详情
     */
    private List<ShiftDetailDTO> shiftDetails;

    /**
     * 班种详情DTO
     */
    @Data
    public static class ShiftDetailDTO {
        /**
         * 班种编码
         */
        private String code;

        /**
         * 班种名称
         */
        private String name;

        /**
         * 班种颜色
         */
        private String color;

        /**
         * 数量
         */
        private Integer count;
    }
}
