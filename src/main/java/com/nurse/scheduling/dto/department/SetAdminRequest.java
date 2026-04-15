package com.nurse.scheduling.dto.department;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 设置管理员请求DTO
 *
 * @author nurse-scheduling
 */
@Data
public class SetAdminRequest {

    /**
     * 成员ID（用户ID）
     */
    @NotNull(message = "成员ID不能为空")
    private String memberId;

    /**
     * 是否设为管理员
     */
    @NotNull(message = "isAdmin不能为空")
    private Boolean isAdmin;
}
