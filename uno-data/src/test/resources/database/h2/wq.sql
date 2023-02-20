DROP TABLE IF EXISTS `wr_wq_real`;
CREATE TABLE `wr_wq_real`
(
    `id`           bigint      NOT NULL COMMENT 'id',
    `st_id`        bigint      NOT NULL COMMENT '测站id',
    `stcd`         varchar(64) NOT NULL COMMENT '测站编码',
    `mot`          TIMESTAMP   NOT NULL COMMENT '监测时间',
    `wt`           decimal(9, 1) COMMENT '水温',
    `ph`           decimal(9, 1) COMMENT 'PH值',
    `nh4n`         decimal(10, 3) COMMENT '氨氮',
    `d0`           decimal(9, 3) COMMENT '溶解氧',
    `codcr`        decimal(9, 3) COMMENT '化学需氧量',
    `cond`         decimal(9, 3) COMMENT '电导率',
    `turb`         decimal(9, 3) COMMENT '浑浊度',
    `tp`           decimal(9, 3) COMMENT '总磷',
    `tn`           decimal(9, 3) COMMENT '总氮',
    `wq_appraise`  varchar(50) COMMENT '水质评价',
    `alarm_status` varchar(20) COMMENT '工况报警状态 NORMAL 正常 ALARM 报警',
    `comm_status`  varchar(20) COMMENT '通讯状态 OFFLINE 离线 ONLINE 在线',
    `sttp`         varchar(20) NOT NULL COMMENT '测站类型',
    `wq_situation` varchar(20) COMMENT '水质情况',
    PRIMARY KEY (`id`)
);
DROP TABLE IF EXISTS `wr_wq_his`;
CREATE TABLE `wr_wq_his`
(
    `id`               bigint      NOT NULL COMMENT 'id',
    `st_id`            bigint      NOT NULL COMMENT '测站id',
    `stcd`             varchar(64) NOT NULL COMMENT '测站编码',
    `mot`              TIMESTAMP   NOT NULL COMMENT '监测时间',
    `wt`               decimal(9, 1) COMMENT '水温',
    `ph`               decimal(9, 1) COMMENT 'PH值',
    `nh4n`             decimal(10, 3) COMMENT '氨氮',
    `d0`               decimal(9, 3) COMMENT '溶解氧',
    `codcr`            decimal(9, 3) COMMENT '化学需氧量',
    `cond`             decimal(9, 3) COMMENT '电导率',
    `turb`             decimal(9, 3) COMMENT '浑浊度',
    `tp`               decimal(9, 3) COMMENT '总磷',
    `tn`               decimal(9, 3) COMMENT '总氮',
    `wq_appraise`      varchar(50) COMMENT '水质评价',
    `alarm_status`     varchar(20) COMMENT '工况报警状态 NORMAL 正常 ALARM 报警',
    `comm_status`      varchar(20) COMMENT '通讯状态 OFFLINE 离线 ONLINE 在线',
    `sttp`             varchar(20) NOT NULL COMMENT '测站类型',
    `wq_situation`     varchar(20) COMMENT '水质情况',
    "OPERATION_STATUS" varchar(20) COMMENT '操作状态',
    PRIMARY KEY (`id`)
);
DROP TABLE IF EXISTS `wr_wq_alarm`;
CREATE TABLE `wr_wq_alarm`
(
    `id`               bigint      NOT NULL COMMENT 'id',
    `st_id`            bigint      NOT NULL COMMENT '测站id',
    `stcd`             varchar(64) NOT NULL COMMENT '测站编码',
    `mot`              TIMESTAMP   NOT NULL COMMENT '监测时间',
    `wq_appraise`      varchar(50) COMMENT '水质评价',
    `alarm_id`         bigint COMMENT '报警id',
    `d0_threshold`     varchar(20) COMMENT '溶解氧报警阈值',
    `nh4n_threshold`   varchar(20) COMMENT '氨氮报警阈值',
    `codcr_threshold`  varchar(20) COMMENT '化学需氧量报警阈值',
    `tn_threshold`     varchar(20) COMMENT '总氮报警阈值',
    `tp_threshold`     varchar(20) COMMENT '总磷报警阈值',
    `alarm_level`      varchar(20) COMMENT '报警级别',
    `d0_level`         varchar(20) COMMENT '溶解氧报警级别',
    `nh4n_level`       varchar(20) COMMENT '氨氮报警级别',
    `codcr_level`      varchar(20) COMMENT '化学需氧量报警级别',
    `tn_level`         varchar(20) COMMENT '总氮报警级别',
    `tp_level`         varchar(20) COMMENT '总磷报警级别',
    `wt`               decimal(9, 1) COMMENT '水温',
    `ph`               decimal(9, 1) COMMENT 'PH值',
    `nh4n`             decimal(10, 3) COMMENT '氨氮',
    `d0`               decimal(9, 3) COMMENT '溶解氧',
    `codcr`            decimal(9, 3) COMMENT '化学需氧量',
    `cond`             decimal(9, 3) COMMENT '电导率',
    `turb`             decimal(9, 3) COMMENT '浑浊度',
    `tp`               decimal(9, 3) COMMENT '总磷',
    `tn`               decimal(9, 3) COMMENT '总氮',
    `sttp`             varchar(20) COMMENT '测站类型',
    PRIMARY KEY (`id`)
);
