package com.nurse.scheduling.dto.package_;

import com.nurse.scheduling.entity.ShiftPackage;
import lombok.Data;

import java.util.List;

/**
 * 班种套餐响应
 *
 * @author nurse-scheduling
 */
@Data
public class ShiftPackageResponse {

    /**
     * 套餐ID
     */
    private Long id;

    /**
     * 套餐名称
     */
    private String name;

    /**
     * 一周班种配置（7天）
     */
    private List<ShiftPackage.ShiftItem> shifts;
}
