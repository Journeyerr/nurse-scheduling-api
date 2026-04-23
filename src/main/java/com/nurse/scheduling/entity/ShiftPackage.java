package com.nurse.scheduling.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 班种套餐实体类
 *
 * @author nurse-scheduling
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "biz_shift_package", autoResultMap = true)
public class ShiftPackage extends BaseEntity {

    /**
     * 科室ID
     */
    private Long departmentId;

    /**
     * 套班名称
     */
    private String name;

    /**
     * 一周班种配置（7天）
     * 格式：[{"shiftId":1,"code":"A","name":"白班","color":"#7BA3C8"}, ...]
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<ShiftItem> shifts;

    /**
     * 班种项
     */
    @Data
    public static class ShiftItem {
        private Long shiftId;
        private String code;
        private String name;
        private String color;
    }
}
