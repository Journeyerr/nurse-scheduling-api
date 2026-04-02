package com.nurse.scheduling.dto.department;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 加入科室请求DTO
 *
 * @author nurse-scheduling
 */
@Data
public class JoinDepartmentRequest {

    /**
     * 邀请码
     */
    @NotBlank(message = "邀请码不能为空")
    private String inviteCode;
}
