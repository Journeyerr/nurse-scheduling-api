package com.nurse.scheduling.dto.package_;

import com.nurse.scheduling.entity.ShiftPackage;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 班种套餐创建/更新请求
 *
 * @author nurse-scheduling
 */
@Data
public class ShiftPackageCreateRequest {

    /**
     * 套餐ID（更新时必填）
     */
    private Long id;

    /**
     * 套餐名称
     */
    @NotBlank(message = "套班名称不能为空")
    private String name;

    /**
     * 一周班种配置（7天）
     */
    private List<ShiftPackage.ShiftItem> shifts;
}
