package com.nurse.scheduling.dto.statistics;

import lombok.Data;

import java.util.List;

/**
 * 科室统计响应DTO
 *
 * @author nurse-scheduling
 */
@Data
public class DepartmentStatisticsResponse {

    /**
     * 总排班数（工作排班）
     */
    private Integer totalSchedules;

    /**
     * 总休息数（休息排班数）
     */
    private Integer totalRestSchedules;

    /**
     * 人数总数（科室成员总数）
     */
    private Integer totalCount;

    /**
     * 总工时（小时）
     */
    private Integer totalHours;

    /**
     * 总系数总和
     */
    private Double coefficientSum;

    /**
     * 成员排名
     */
    private List<MemberRankDTO> memberRank;

    /**
     * 成员排名DTO
     */
    @Data
    public static class MemberRankDTO {
        /**
         * 成员ID
         */
        private String id;

        /**
         * 成员姓名
         */
        private String name;

        /**
         * 工作排班数
         */
        private Integer scheduleCount;

        /**
         * 休息天数
         */
        private Integer restCount;

        /**
         * 总工时（小时）
         */
        private Integer totalHours;

        /**
         * 系数总和（各班次次数×系数的累加）
         */
        private Double coefficientSum;

        /**
         * 存欠班天数（正数=存班，负数=欠班，累计值）
         */
        private Integer balanceDays;
    }
}
