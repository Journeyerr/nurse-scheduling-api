package com.nurse.scheduling.dto.department;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 踢出成员请求DTO
 *
 * @author nurse-scheduling
 */
@Data
public class KickMemberRequest {

    /**
     * 成员ID
     */
    @NotBlank(message = "成员ID不能为空")
    private String memberId;
}
