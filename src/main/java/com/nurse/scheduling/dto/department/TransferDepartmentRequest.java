package com.nurse.scheduling.dto.department;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 转让科室请求DTO
 *
 * @author nurse-scheduling
 */
@Data
public class TransferDepartmentRequest {

    /**
     * 新创建者ID
     */
    @NotBlank(message = "新创建者ID不能为空")
    private String newCreatorId;
}
