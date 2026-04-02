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
}
