-- 班种套餐表
CREATE TABLE IF NOT EXISTS `biz_shift_package` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `department_id` bigint(20) NOT NULL COMMENT '科室ID',
  `name` varchar(100) NOT NULL COMMENT '套餐名称',
  `shifts` json DEFAULT NULL COMMENT '一周班种配置（7天）',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除标识 0-未删除 1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_department_id` (`department_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='班种套班表';
