DROP TABLE IF EXISTS `wr_st_consumption_hour`;
CREATE TABLE `wr_st_consumption_hour`
(
    `id`     bigint      NOT NULL,
    `st_id`  bigint      NOT NULL COMMENT '测站id',
    `stcd`   varchar(64) NOT NULL COMMENT '测站编码',
    `sttp`   varchar(40) NOT NULL COMMENT '测站类型',
    `hour`   varchar(20) NOT NULL COMMENT '日期',
    `hour_w` decimal(12, 3) COMMENT '本期统计量',
    PRIMARY KEY (`id`)
);
DROP TABLE IF EXISTS `wr_st_consumption_day`;
CREATE TABLE `wr_st_consumption_day`
(
    `id`    bigint      NOT NULL,
    `st_id` bigint      NOT NULL COMMENT '测站id',
    `stcd`  varchar(64) NOT NULL COMMENT '测站编码',
    `sttp`  varchar(40) NOT NULL COMMENT '测站类型',
    `dt`    varchar(20) NOT NULL COMMENT '日期',
    `day_w` decimal(12, 3) COMMENT '本期统计量',
    PRIMARY KEY (`id`)
);
DROP TABLE IF EXISTS `wr_st_consumption_month`;
CREATE TABLE `wr_st_consumption_month`
(
    `id`      bigint      NOT NULL,
    `st_id`   bigint      NOT NULL COMMENT '测站id',
    `stcd`    varchar(64) NOT NULL COMMENT '测站编码',
    `sttp`    varchar(40) NOT NULL COMMENT '测站类型',
    `month`   varchar(8)  NOT NULL COMMENT '月份',
    `month_w` decimal(12, 3) COMMENT '本期统计量',
    PRIMARY KEY (`id`)
);
DROP TABLE IF EXISTS `wr_rcs_wq_day`;
CREATE TABLE `wr_rcs_wq_day`
(
    `id`           bigint      NOT NULL,
    `rcs_id`       bigint      NOT NULL COMMENT '断面id',
    `dt`           varchar(32) NOT NULL COMMENT '日期',
    `d0`           decimal(9, 3) COMMENT '溶解氧',
    `codcr`        decimal(9, 3) COMMENT '化学需氧量',
    `nh4n`         decimal(9, 3) COMMENT '氨氮',
    `tp`           decimal(9, 3) COMMENT '总磷',
    `tn`           decimal(9, 3) COMMENT '总氮',
    `wq_appraise`  varchar(20) COMMENT '水质评价',
    `wq_situation` varchar(20) COMMENT '水质状况',
    PRIMARY KEY (`id`)
);
DROP TABLE IF EXISTS `wr_rcs_wq_month`;
CREATE TABLE `wr_rcs_wq_month`
(
    `id`           bigint      NOT NULL,
    `rcs_id`       bigint      NOT NULL COMMENT '断面id',
    `month`        varchar(32) NOT NULL COMMENT '月份',
    `d0`           decimal(9, 3) COMMENT '溶解氧',
    `codcr`        decimal(9, 3) COMMENT '化学需氧量',
    `nh4n`         decimal(9, 3) COMMENT '氨氮',
    `tp`           decimal(9, 3) COMMENT '总磷',
    `tn`           decimal(9, 3) COMMENT '总氮',
    `wq_appraise`  varchar(20) COMMENT '水质评价',
    `wq_situation` varchar(20) COMMENT '水质状况',
    PRIMARY KEY (`id`)
);
DROP TABLE IF EXISTS `wr_rcs_wq_year`;
CREATE TABLE `wr_rcs_wq_year`
(
    `id`           bigint      NOT NULL,
    `rcs_id`       bigint      NOT NULL COMMENT '断面id',
    `year`         varchar(32) NOT NULL COMMENT '年份',
    `d0`           decimal(9, 3) COMMENT '溶解氧',
    `codcr`        decimal(9, 3) COMMENT '化学需氧量',
    `nh4n`         decimal(9, 3) COMMENT '氨氮',
    `tp`           decimal(9, 3) COMMENT '总磷',
    `tn`           decimal(9, 3) COMMENT '总氮',
    `wq_appraise`  varchar(20) COMMENT '水质评价',
    `wq_situation` varchar(20) COMMENT '水质状况',
    PRIMARY KEY (`id`)
);
