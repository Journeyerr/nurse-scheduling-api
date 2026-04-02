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
     * 平均工作天数
     */
    private Integer avgDays;

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
         * 工作天数
         */
        private Integer days;
    }
}
