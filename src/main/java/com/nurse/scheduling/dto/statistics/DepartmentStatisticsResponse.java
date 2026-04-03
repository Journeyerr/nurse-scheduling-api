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
     * 总排班数
     */
    private Integer totalSchedules;

    /**
     * 总成员数
     */
    private Integer totalMembers;

    /**
     * 总工时（小时）
     */
    private Integer totalHours;

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
         * 排班数
         */
        private Integer scheduleCount;

        /**
         * 总工时（小时）
         */
        private Integer totalHours;
    }
}
