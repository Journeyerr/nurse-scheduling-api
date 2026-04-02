package com.nurse.scheduling.dto.department;

import com.nurse.scheduling.dto.shift.TimeSlotDTO;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 创建科室请求DTO
 *
 * @author nurse-scheduling
 */
@Data
public class DepartmentCreateRequest {

    /**
     * 科室名称
     */
    @NotBlank(message = "科室名称不能为空")
    private String name;

    /**
     * 创建者姓名（用于更新用户昵称）
     */
    private String creatorName;

    /**
     * 初始班种列表
     */
    private List<ShiftInfo> shifts;

    /**
     * 班种信息
     */
    @Data
    public static class ShiftInfo {
        private String name;
        private String code;
        private String color;
        private List<TimeSlotDTO> timeSlots;
        private Integer duration;
    }
}
