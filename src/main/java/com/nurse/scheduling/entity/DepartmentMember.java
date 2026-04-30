package com.nurse.scheduling.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 科室成员关联实体类
 *
 * @author nurse-scheduling
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_department_member")
public class DepartmentMember extends BaseEntity {

    /**
     * 科室ID
     */
    private Long departmentId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 加入时间
     */
    private LocalDate joinTime;

    /**
     * 是否为创建者 0-否 1-是
     */
    private Integer isCreator;

    /**
     * 是否为管理员 0-否 1-是
     */
    private Integer isAdmin;

    /**
     * 存欠班天数（正数=存班，负数=欠班，累计值）
     * 加入科室开始计算，退出科室清零
     * null表示尚未计算，需要触发重算
     */
    private Integer balanceDays;
}
