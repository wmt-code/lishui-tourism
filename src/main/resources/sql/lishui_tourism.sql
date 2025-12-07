-- ==========================================
-- 丽水智慧旅游管理系统数据库脚本
-- Database: lishui_tourism
-- ==========================================

CREATE DATABASE IF NOT EXISTS lishui_tourism DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE lishui_tourism;

-- ==========================================
-- 1. 用户表
-- ==========================================
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `password` VARCHAR(255) NOT NULL COMMENT '密码（加密）',
  `nickname` VARCHAR(100) DEFAULT NULL COMMENT '昵称',
  `avatar` VARCHAR(500) DEFAULT NULL COMMENT '头像URL',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `role` VARCHAR(20) NOT NULL DEFAULT 'TOURIST' COMMENT '角色：TOURIST/GUIDE/ADMIN',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_role` (`role`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- ==========================================
-- 2. 公告表
-- ==========================================
DROP TABLE IF EXISTS `announcement`;
CREATE TABLE `announcement` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `title` VARCHAR(200) NOT NULL COMMENT '标题',
  `content` TEXT NOT NULL COMMENT '内容',
  `publisher_id` BIGINT NOT NULL COMMENT '发布者ID',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-下架 1-发布',
  `view_count` INT NOT NULL DEFAULT 0 COMMENT '浏览次数',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  KEY `idx_publisher` (`publisher_id`),
  KEY `idx_status` (`status`),
  KEY `idx_created` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='公告表';

-- ==========================================
-- 3. 旅游资讯表
-- ==========================================
DROP TABLE IF EXISTS `travel_news`;
CREATE TABLE `travel_news` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '资讯ID',
  `title` VARCHAR(200) NOT NULL COMMENT '标题',
  `summary` VARCHAR(500) DEFAULT NULL COMMENT '摘要',
  `content` TEXT NOT NULL COMMENT '内容',
  `cover` VARCHAR(500) DEFAULT NULL COMMENT '封面图',
  `author_id` BIGINT NOT NULL COMMENT '作者ID',
  `category` VARCHAR(50) DEFAULT NULL COMMENT '分类',
  `tags` VARCHAR(200) DEFAULT NULL COMMENT '标签，逗号分隔',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-下架 1-发布',
  `view_count` INT NOT NULL DEFAULT 0 COMMENT '浏览次数',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  KEY `idx_author` (`author_id`),
  KEY `idx_category` (`category`),
  KEY `idx_status` (`status`),
  KEY `idx_created` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='旅游资讯表';

-- ==========================================
-- 4. 目的地表
-- ==========================================
DROP TABLE IF EXISTS `destination`;
CREATE TABLE `destination` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '目的地ID',
  `name` VARCHAR(100) NOT NULL COMMENT '名称',
  `description` TEXT DEFAULT NULL COMMENT '描述',
  `cover` VARCHAR(500) DEFAULT NULL COMMENT '封面图',
  `province` VARCHAR(50) DEFAULT NULL COMMENT '省份',
  `city` VARCHAR(50) DEFAULT NULL COMMENT '城市',
  `district` VARCHAR(50) DEFAULT NULL COMMENT '区县',
  `address` VARCHAR(200) DEFAULT NULL COMMENT '详细地址',
  `latitude` DECIMAL(10,7) DEFAULT NULL COMMENT '纬度',
  `longitude` DECIMAL(10,7) DEFAULT NULL COMMENT '经度',
  `hot_score` INT NOT NULL DEFAULT 0 COMMENT '热度分数',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-下架 1-上架',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  KEY `idx_city` (`city`),
  KEY `idx_hot` (`hot_score`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='目的地表';

-- ==========================================
-- 5. 景点表
-- ==========================================
DROP TABLE IF EXISTS `scenic_spot`;
CREATE TABLE `scenic_spot` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '景点ID',
  `destination_id` BIGINT DEFAULT NULL COMMENT '目的地ID',
  `name` VARCHAR(100) NOT NULL COMMENT '景点名称',
  `description` TEXT DEFAULT NULL COMMENT '景点描述',
  `cover` VARCHAR(500) DEFAULT NULL COMMENT '封面图',
  `address` VARCHAR(200) DEFAULT NULL COMMENT '地址',
  `latitude` DECIMAL(10,7) DEFAULT NULL COMMENT '纬度',
  `longitude` DECIMAL(10,7) DEFAULT NULL COMMENT '经度',
  `opening_hours` VARCHAR(200) DEFAULT NULL COMMENT '开放时间',
  `ticket_price` DECIMAL(10,2) DEFAULT NULL COMMENT '门票价格',
  `traffic_guide` TEXT DEFAULT NULL COMMENT '交通指南',
  `level` VARCHAR(20) DEFAULT NULL COMMENT '景点等级（如5A/4A）',
  `hot_score` INT NOT NULL DEFAULT 0 COMMENT '热度分数',
  `rating` DECIMAL(3,2) DEFAULT 0.00 COMMENT '评分',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-下架 1-上架',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  KEY `idx_destination` (`destination_id`),
  KEY `idx_hot` (`hot_score`),
  KEY `idx_rating` (`rating`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='景点表';

-- ==========================================
-- 6. 景点图集表
-- ==========================================
DROP TABLE IF EXISTS `scenic_spot_image`;
CREATE TABLE `scenic_spot_image` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '图片ID',
  `spot_id` BIGINT NOT NULL COMMENT '景点ID',
  `image_url` VARCHAR(500) NOT NULL COMMENT '图片URL',
  `description` VARCHAR(200) DEFAULT NULL COMMENT '图片描述',
  `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  KEY `idx_spot` (`spot_id`),
  KEY `idx_sort` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='景点图集表';

-- ==========================================
-- 7. 标签表
-- ==========================================
DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '标签ID',
  `name` VARCHAR(50) NOT NULL COMMENT '标签名称',
  `type` VARCHAR(20) DEFAULT NULL COMMENT '标签类型',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='标签表';

-- ==========================================
-- 8. 景点标签关联表
-- ==========================================
DROP TABLE IF EXISTS `scenic_tag_rel`;
CREATE TABLE `scenic_tag_rel` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `spot_id` BIGINT NOT NULL COMMENT '景点ID',
  `tag_id` BIGINT NOT NULL COMMENT '标签ID',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_spot_tag` (`spot_id`, `tag_id`),
  KEY `idx_spot` (`spot_id`),
  KEY `idx_tag` (`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='景点标签关联表';

-- ==========================================
-- 9. 评论表
-- ==========================================
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `spot_id` BIGINT NOT NULL COMMENT '景点ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `content` TEXT NOT NULL COMMENT '评论内容',
  `rating` TINYINT DEFAULT NULL COMMENT '评分（1-5）',
  `images` VARCHAR(1000) DEFAULT NULL COMMENT '图片URLs，逗号分隔',
  `parent_id` BIGINT DEFAULT NULL COMMENT '父评论ID（回复）',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-隐藏 1-显示',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  KEY `idx_spot` (`spot_id`),
  KEY `idx_user` (`user_id`),
  KEY `idx_parent` (`parent_id`),
  KEY `idx_created` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评论表';

-- ==========================================
-- 10. 收藏表
-- ==========================================
DROP TABLE IF EXISTS `favorite`;
CREATE TABLE `favorite` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `target_type` VARCHAR(20) NOT NULL COMMENT '收藏类型：SPOT/DESTINATION/NEWS',
  `target_id` BIGINT NOT NULL COMMENT '目标ID',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_target` (`user_id`, `target_type`, `target_id`),
  KEY `idx_user` (`user_id`),
  KEY `idx_target` (`target_type`, `target_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='收藏表';

-- ==========================================
-- 11. 导游线路表
-- ==========================================
DROP TABLE IF EXISTS `guide_route`;
CREATE TABLE `guide_route` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '线路ID',
  `guide_id` BIGINT NOT NULL COMMENT '导游ID',
  `name` VARCHAR(100) NOT NULL COMMENT '线路名称',
  `description` TEXT DEFAULT NULL COMMENT '线路描述',
  `days` INT NOT NULL COMMENT '天数',
  `price` DECIMAL(10,2) DEFAULT NULL COMMENT '价格',
  `max_people` INT DEFAULT NULL COMMENT '最大人数',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-下架 1-上架',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  KEY `idx_guide` (`guide_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='导游线路表';

-- ==========================================
-- 12. 导游线路景点关联表
-- ==========================================
DROP TABLE IF EXISTS `guide_route_spot_rel`;
CREATE TABLE `guide_route_spot_rel` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `route_id` BIGINT NOT NULL COMMENT '线路ID',
  `spot_id` BIGINT NOT NULL COMMENT '景点ID',
  `day_number` INT NOT NULL COMMENT '第几天',
  `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序',
  `duration` INT DEFAULT NULL COMMENT '停留时长（分钟）',
  `description` VARCHAR(500) DEFAULT NULL COMMENT '说明',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_route` (`route_id`),
  KEY `idx_spot` (`spot_id`),
  KEY `idx_day` (`day_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='导游线路景点关联表';

-- ==========================================
-- 13. AI 聊天历史表
-- ==========================================
DROP TABLE IF EXISTS `ai_chat_history`;
CREATE TABLE `ai_chat_history` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `spot_id` BIGINT DEFAULT NULL COMMENT '景点ID',
  `question` TEXT NOT NULL COMMENT '用户问题',
  `answer` TEXT NOT NULL COMMENT 'AI回答',
  `context` TEXT DEFAULT NULL COMMENT '上下文信息',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  KEY `idx_user` (`user_id`),
  KEY `idx_spot` (`spot_id`),
  KEY `idx_created` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AI聊天历史表';

-- ==========================================
-- 14. AI 行程表
-- ==========================================
DROP TABLE IF EXISTS `ai_itinerary`;
CREATE TABLE `ai_itinerary` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '行程ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `title` VARCHAR(200) NOT NULL COMMENT '行程标题',
  `days` INT NOT NULL COMMENT '天数',
  `budget` DECIMAL(10,2) DEFAULT NULL COMMENT '预算',
  `preference` VARCHAR(200) DEFAULT NULL COMMENT '偏好',
  `season` VARCHAR(20) DEFAULT NULL COMMENT '季节',
  `itinerary_data` TEXT NOT NULL COMMENT '行程数据（JSON）',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  KEY `idx_user` (`user_id`),
  KEY `idx_created` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AI行程表';

-- ==========================================
-- 初始化数据
-- ==========================================

-- 插入管理员账户（密码：admin123，需要在代码中加密）
INSERT INTO `user` (`username`, `password`, `nickname`, `role`, `status`) 
VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '系统管理员', 'ADMIN', 1);

-- 插入测试导游（密码：guide123）
INSERT INTO `user` (`username`, `password`, `nickname`, `role`, `status`) 
VALUES ('guide01', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '张导游', 'GUIDE', 1);

-- 插入测试游客（密码：tourist123）
INSERT INTO `user` (`username`, `password`, `nickname`, `role`, `status`) 
VALUES ('tourist01', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '李游客', 'TOURIST', 1);

-- 插入测试标签
INSERT INTO `tag` (`name`, `type`) VALUES 
('自然风光', 'SCENERY'),
('人文历史', 'CULTURE'),
('亲子游', 'FAMILY'),
('摄影', 'PHOTO'),
('徒步', 'HIKING');

-- 插入测试目的地
INSERT INTO `destination` (`name`, `description`, `province`, `city`, `district`, `hot_score`, `status`) VALUES 
('丽水市', '秀山丽水，养生福地，华东地区重要的生态旅游目的地', '浙江省', '丽水市', '莲都区', 100, 1);

-- 插入测试景点
INSERT INTO `scenic_spot` (`destination_id`, `name`, `description`, `address`, `opening_hours`, `ticket_price`, `level`, `hot_score`, `rating`, `status`) VALUES 
(1, '缙云仙都景区', '仙都景区是国家级风景名胜区、国家AAAAA级旅游景区，以峰岩奇绝、山水神秀为特色', '浙江省丽水市缙云县仙都街道', '08:00-17:00', 60.00, '5A', 95, 4.80, 1),
(1, '古堰画乡', '古堰画乡景区位于丽水市莲都区碧湖镇和大港头镇，距丽水市区二十公里', '浙江省丽水市莲都区', '全天开放', 0.00, '4A', 88, 4.60, 1);
