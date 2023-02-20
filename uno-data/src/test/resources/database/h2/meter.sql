DROP TABLE IF EXISTS `wr_meter_real`;
CREATE TABLE `wr_meter_real`
(
    `id`           bigint      NOT NULL,
    `st_id`        bigint      NOT NULL COMMENT '测站id',
    `stcd`         varchar(64) NOT NULL COMMENT '测站编码',
    `mot`          TIMESTAMP   NOT NULL COMMENT '上报时间',
    `comm_status`  varchar(20) COMMENT '通讯状态 OFFLINE 离线 ONLINE 在线',
    `alarm_status` varchar(20) COMMENT '工况报警状态 NORMAL 正常 ALARM 报警',
    `sttp`         varchar(20) NOT NULL COMMENT '测站类型',
    `icc_id`       varchar(20) COMMENT '表类型',
    `meter_no`     varchar(64) COMMENT '表号',
    `cw`           decimal(10, 2) COMMENT '当前累计流量',
    `crf`          decimal(10, 2) COMMENT '当前累计逆向累计流量',
    `dmf`          decimal(10, 2) COMMENT '日最高流量',
    `dmf_duration` bigint COMMENT '日最大流量持续时间',
    `dmf_time`     TIMESTAMP COMMENT '日最大流量时间',
    `pressure`     decimal(10, 2) COMMENT '管道压力',
    `iml30`        text COMMENT '30分间隔计量流量',
    `running_time` TIMESTAMP COMMENT '日激活时间/运行时间',
    `iml_time`     TIMESTAMP COMMENT '间隔计量流量起始时间',
    `wms`          varchar(64) COMMENT '水表状态',
    `fen_q`        text COMMENT '密集采集流量',
    `fen_q_time`   TIMESTAMP COMMENT '密集采集流量起始时间',
    `oC`           decimal(10, 2),
    PRIMARY KEY (`id`)
);
DROP TABLE IF EXISTS `wr_meter_his`;
CREATE TABLE `wr_meter_his`
(
    `id`               bigint      NOT NULL,
    `st_id`            bigint      NOT NULL COMMENT '测站id',
    `stcd`             varchar(64) NOT NULL COMMENT '测站编码',
    `mot`              TIMESTAMP   NOT NULL COMMENT '上报时间',
    `comm_status`      varchar(20) COMMENT '通讯状态 OFFLINE 离线 ONLINE 在线',
    `alarm_status`     varchar(20) COMMENT '工况报警状态 NORMAL 正常 ALARM 报警',
    `sttp`             varchar(20) NOT NULL COMMENT '测站类型',
    `icc_id`           varchar(20) COMMENT '表类型',
    `meter_no`         varchar(64) COMMENT '表号',
    `cw`               decimal(10, 2) COMMENT '当前累计流量',
    `crf`              decimal(10, 2) COMMENT '当前累计逆向累计流量',
    `dmf`              decimal(10, 2) COMMENT '日最高流量',
    `dmf_duration`     bigint COMMENT '日最大流量持续时间',
    `dmf_time`         TIMESTAMP COMMENT '日最大流量时间',
    `pressure`         decimal(10, 2) COMMENT '管道压力',
    `iml30`            text COMMENT '30分间隔计量流量',
    `running_time`     TIMESTAMP COMMENT '日激活时间/运行时间',
    `iml_time`         TIMESTAMP COMMENT '间隔计量流量起始时间',
    `wms`              varchar(64) COMMENT '水表状态',
    `fen_q`            text COMMENT '密集采集流量',
    `fen_q_time`       TIMESTAMP COMMENT '密集采集流量起始时间',
    `oC`               decimal(10, 2),
    `OPERATION_STATUS` varchar(20) COMMENT '操作状态',
    PRIMARY KEY (`id`)
);
DROP TABLE IF EXISTS `wr_meter_alarm`;
CREATE TABLE `wr_meter_alarm`
(
    `id`                bigint      NOT NULL,
    `st_id`             bigint      NOT NULL COMMENT '测站id',
    `stcd`              varchar(64) NOT NULL COMMENT '测站编码',
    `mot`               TIMESTAMP   NOT NULL COMMENT '上报时间',
    `day_w`             decimal(12, 3) COMMENT '日用水量',
    `day_w_level`       varchar(32) COMMENT '日用水量报警等级',
    `day_w_threshold`   varchar(32) COMMENT '日用水量阈值',
    `month_w`           decimal(12, 3) COMMENT '月用水量',
    `month_w_level`     varchar(32) COMMENT '月用水量报警等级',
    `month_w_threshold` varchar(32) COMMENT '月用水量阈值',
    `year_w`            decimal(12, 3) COMMENT '年用水量',
    `year_w_level`      varchar(32) COMMENT '年用水量报警等级',
    `year_w_threshold`  varchar(32) COMMENT '年用水量阈值',
    `alarm_id`          bigint      NOT NULL COMMENT '报警id',
    `alarm_level`       varchar(20) COMMENT '报警级别',
    PRIMARY KEY (`id`)
);
