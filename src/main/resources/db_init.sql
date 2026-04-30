-- ========================================
-- 护士排班系统数据库初始化脚本
-- ========================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS nurse_scheduling DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE nurse_scheduling;

-- ========================================
-- 1. 用户表 (sys_user)
-- ========================================
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `openid` VARCHAR(100) DEFAULT NULL COMMENT '微信openid',
    `nick_name` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
    `avatar_url` VARCHAR(500) DEFAULT NULL COMMENT '头像URL',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '电话号码',
    `work_no` VARCHAR(50) DEFAULT NULL COMMENT '工号',
    `title` VARCHAR(50) DEFAULT NULL COMMENT '职称',
    `seniority` INT(11) DEFAULT NULL COMMENT '年资（年）',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT(1) DEFAULT 0 COMMENT '逻辑删除标识 0-未删除 1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_openid` (`openid`),
    KEY `idx_nick_name` (`nick_name`),
    KEY `idx_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ========================================
-- 2. 科室表 (sys_department)
-- ========================================
DROP TABLE IF EXISTS `sys_department`;
CREATE TABLE `sys_department` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '科室ID',
    `name` VARCHAR(100) NOT NULL COMMENT '科室名称',
    `creator_id` BIGINT(20) NOT NULL COMMENT '创建者ID（护士长）',
    `member_count` INT(11) DEFAULT 1 COMMENT '成员数量',
    `invite_code` VARCHAR(10) DEFAULT NULL COMMENT '邀请码',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT(1) DEFAULT 0 COMMENT '逻辑删除标识 0-未删除 1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_invite_code` (`invite_code`),
    KEY `idx_creator_id` (`creator_id`),
    KEY `idx_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='科室表';

-- ========================================
-- 3. 科室成员关联表 (sys_department_member)
-- ========================================
DROP TABLE IF EXISTS `sys_department_member`;
CREATE TABLE `sys_department_member` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `department_id` BIGINT(20) NOT NULL COMMENT '科室ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `join_time` DATE DEFAULT NULL COMMENT '加入时间',
    `is_creator` TINYINT(1) DEFAULT 0 COMMENT '是否为创建者 0-否 1-是',
    `is_admin` TINYINT(1) DEFAULT 0 COMMENT '是否为管理员 0-否 1-是',
    `balance_days` INT(11) DEFAULT NULL COMMENT '存欠班天数（正数=存班，负数=欠班，累计值）',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT(1) DEFAULT 0 COMMENT '逻辑删除标识 0-未删除 1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_department_user` (`department_id`, `user_id`),
    KEY `idx_department_id` (`department_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='科室成员关联表';

-- ========================================
-- 4. 班种表 (biz_shift)
-- ========================================
DROP TABLE IF EXISTS `biz_shift`;
CREATE TABLE `biz_shift` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '班种ID',
    `department_id` BIGINT(20) NOT NULL COMMENT '科室ID',
    `code` VARCHAR(10) NOT NULL COMMENT '班种编号（显示在日历上）',
    `name` VARCHAR(50) NOT NULL COMMENT '班种名称',
    `time_slots` TEXT COMMENT '时间段JSON（支持多段班）',
    `duration` INT(11) DEFAULT NULL COMMENT '时长（小时）',
    `color` VARCHAR(20) DEFAULT NULL COMMENT '显示颜色',
    `coefficient` DOUBLE DEFAULT 1.0 COMMENT '班种系数（默认1，用于酬劳计算）',
    `is_rest` TINYINT(1) DEFAULT 0 COMMENT '是否为休息班 0-否 1-是',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT(1) DEFAULT 0 COMMENT '逻辑删除标识 0-未删除 1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_department_id` (`department_id`),
    KEY `idx_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='班种表';

-- ========================================
-- 5. 排班表 (biz_schedule)
-- ========================================
DROP TABLE IF EXISTS `biz_schedule`;
CREATE TABLE `biz_schedule` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '排班ID',
    `department_id` BIGINT(20) NOT NULL COMMENT '科室ID',
    `member_id` BIGINT(20) NOT NULL COMMENT '成员ID',
    `date` DATE NOT NULL COMMENT '日期',
    `shift_id` BIGINT(20) NOT NULL COMMENT '班种ID',
    `shift_code` VARCHAR(10) DEFAULT NULL COMMENT '班种编号',
    `shift_name` VARCHAR(50) DEFAULT NULL COMMENT '班种名称',
    `shift_color` VARCHAR(20) DEFAULT NULL COMMENT '班种颜色',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT(1) DEFAULT 0 COMMENT '逻辑删除标识 0-未删除 1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_department_date` (`department_id`, `date`),
    KEY `idx_member_date` (`member_id`, `date`),
    KEY `idx_date` (`date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='排班表';

-- ========================================
-- 6. 假勤申请表 (biz_leave_apply)
-- ========================================
DROP TABLE IF EXISTS `biz_leave_apply`;
CREATE TABLE `biz_leave_apply` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '申请ID',
    `department_id` BIGINT(20) NOT NULL COMMENT '科室ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '申请人ID',
    `user_name` VARCHAR(50) DEFAULT NULL COMMENT '申请人姓名',
    `type` VARCHAR(20) NOT NULL COMMENT '类型：leave-请假, transfer-调班, exchange-换班',
    `start_date` DATE NOT NULL COMMENT '开始日期',
    `end_date` DATE NOT NULL COMMENT '结束日期',
    `days` INT(11) DEFAULT NULL COMMENT '天数',
    `target_member_id` BIGINT(20) DEFAULT NULL COMMENT '换班对象ID（换班时使用）',
    `reason` VARCHAR(500) DEFAULT NULL COMMENT '事由',
    `status` VARCHAR(20) DEFAULT 'pending' COMMENT '状态：pending-待审批, approved-已通过, rejected-已拒绝',
    `approver_id` BIGINT(20) DEFAULT NULL COMMENT '审批人ID',
    `approve_time` DATETIME DEFAULT NULL COMMENT '审批时间',
    `approve_remark` VARCHAR(500) DEFAULT NULL COMMENT '审批意见',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT(1) DEFAULT 0 COMMENT '逻辑删除标识 0-未删除 1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_department_id` (`department_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_status` (`status`),
    KEY `idx_start_date` (`start_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='假勤申请表';

-- ========================================
-- 7. 期望排班表 (biz_expect_schedule)
-- ========================================
DROP TABLE IF EXISTS `biz_expect_schedule`;
CREATE TABLE `biz_expect_schedule` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '申请ID',
    `department_id` BIGINT(20) NOT NULL COMMENT '科室ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '申请人ID',
    `user_name` VARCHAR(50) DEFAULT NULL COMMENT '申请人姓名',
    `type` VARCHAR(20) DEFAULT 'schedule' COMMENT '申请类型：schedule-期望排班, swap-换班, leave-请假',
    `shift_id` BIGINT(20) DEFAULT NULL COMMENT '期望班种ID（期望排班时使用）',
    `my_shift_id` BIGINT(20) DEFAULT NULL COMMENT '我方班次ID（换班时使用）',
    `target_shift_id` BIGINT(20) DEFAULT NULL COMMENT '目标班次ID（换班时使用）',
    `start_date` DATE DEFAULT NULL COMMENT '开始日期/我方日期',
    `end_date` DATE DEFAULT NULL COMMENT '结束日期',
    `target_date` DATE DEFAULT NULL COMMENT '目标日期（换班时使用）',
    `target_user_id` BIGINT(20) DEFAULT NULL COMMENT '目标用户ID（换班对象）',
    `target_user_name` VARCHAR(50) DEFAULT NULL COMMENT '目标用户姓名',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `status` VARCHAR(20) DEFAULT 'pending' COMMENT '状态：pending-待审批, approved-已通过, rejected-已拒绝',
    `approver_id` BIGINT(20) DEFAULT NULL COMMENT '审批人ID',
    `approve_time` DATETIME DEFAULT NULL COMMENT '审批时间',
    `approve_remark` VARCHAR(500) DEFAULT NULL COMMENT '审批意见',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT(1) DEFAULT 0 COMMENT '逻辑删除标识 0-未删除 1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_department_id` (`department_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_type` (`type`),
    KEY `idx_status` (`status`),
    KEY `idx_start_date` (`start_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='申请表（期望排班、换班、请假等）';

-- ========================================
-- 初始化测试数据（可选）
-- ========================================

-- 插入测试用户
INSERT INTO `sys_user` (`openid`, `nick_name`, `avatar_url`, `phone`, `work_no`, `title`, `seniority`, `remark`) VALUES
('test_openid_001', '张护士长', '', '13800138001', 'N001', '护士长', 10, '测试账号'),
('test_openid_002', '李护士', '', '13800138002', 'N002', '主管护师', 5, '测试账号'),
('test_openid_003', '王护士', '', '13800138003', 'N003', '护师', 3, '测试账号');

-- 插入测试科室
INSERT INTO `sys_department` (`name`, `creator_id`, `member_count`, `invite_code`) VALUES
('内科一病区', 1, 3, 'ABC123');

-- 插入科室成员关联
INSERT INTO `sys_department_member` (`department_id`, `user_id`, `join_time`, `is_creator`) VALUES
(1, 1, CURDATE(), 1),
(1, 2, CURDATE(), 0),
(1, 3, CURDATE(), 0);

-- 插入测试班种
INSERT INTO `biz_shift` (`department_id`, `code`, `name`, `time_slots`, `duration`, `color`) VALUES
(1, 'A', '白班', '[{"startTime":"08:00","endTime":"16:00","startIsNextDay":false,"endIsNextDay":false}]', 8, '#7BA3C8'),
(1, 'P', '中班', '[{"startTime":"16:00","endTime":"00:00","startIsNextDay":false,"endIsNextDay":true}]', 8, '#6BAF92'),
(1, 'N', '夜班', '[{"startTime":"00:00","endTime":"08:00","startIsNextDay":false,"endIsNextDay":false}]', 8, '#9B8AA8');

-- ========================================
-- 查询验证
-- ========================================
SELECT 'Database initialized successfully!' AS message;
SELECT COUNT(*) AS user_count FROM sys_user;
SELECT COUNT(*) AS department_count FROM sys_department;
SELECT COUNT(*) AS shift_count FROM biz_shift;
