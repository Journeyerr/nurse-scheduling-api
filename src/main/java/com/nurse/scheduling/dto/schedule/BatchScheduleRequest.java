package com.nurse.scheduling.dto.schedule;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 批量添加/删除排班请求DTO
 *
 * @author nurse-scheduling
 */
@Data
public class BatchScheduleRequest {

    /**
     * 要添加的排班列表
     */
    @Valid
    private List<ScheduleAddRequest> adds;

    /**
     * 要删除的排班ID列表
     */
    private List<String> deletes;
}
