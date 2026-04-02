package com.nurse.scheduling.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 科室实体类
 *
 * @author nurse-scheduling
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_department")
public class Department extends BaseEntity {

    /**
     * 科室名称
     */
    private String name;

    /**
     * 创建者ID（护士长）
     */
    private Long creatorId;

    /**
     * 成员数量
     */
    private Integer memberCount;

    /**
     * 邀请码
     */
    private String inviteCode;
}
