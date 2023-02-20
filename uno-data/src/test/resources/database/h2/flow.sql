DROP TABLE IF EXISTS `wr_mp_q_real`;
CREATE TABLE `wr_mp_q_real`
(
    `id`           bigint      NOT NULL COMMENT 'id',
    `st_id`        bigint      NOT NULL COMMENT '测站id',
    `stcd`         varchar(64) NOT NULL COMMENT '测站编码',
    `mot`          TIMESTAMP COMMENT '监测时间',
    `mp_q`         decimal(12, 3) COMMENT '瞬时流量',
    `acc_f`        decimal(12, 1) COMMENT '累计流量',
    `alarm_status` varchar(20) COMMENT '工况报警状态 NORMAL 正常 ALARM 报警',
    `comm_status`  varchar(20) COMMENT '通讯状态 OFFLINE 离线 ONLINE 在线',
    `sttp`         varchar(20) NOT NULL COMMENT '测站类型',
    PRIMARY KEY (`id`)
);
DROP TABLE IF EXISTS `wr_mp_q_his`;
CREATE TABLE `wr_mp_q_his`
(
    `id`               bigint      NOT NULL COMMENT 'id',
    `st_id`            bigint      NOT NULL COMMENT '测站id',
    `stcd`             varchar(64) NOT NULL COMMENT '测站编码',
    `mot`              TIMESTAMP COMMENT '监测时间',
    `mp_q`             decimal(12, 3) COMMENT '瞬时流量',
    `acc_f`            decimal(12, 1) COMMENT '累计流量',
    `alarm_status`     varchar(20) COMMENT '工况报警状态 NORMAL 正常 ALARM 报警',
    `comm_status`      varchar(20) COMMENT '通讯状态 OFFLINE 离线 ONLINE 在线',
    `sttp`             varchar(20) NOT NULL COMMENT '测站类型',
    `operation_status` varchar(20) COMMENT '操作状态',
    PRIMARY KEY (`id`)
);
DROP TABLE IF EXISTS `wr_mp_q_alarm`;
CREATE TABLE `wr_mp_q_alarm`
(
    `id`               bigint      NOT NULL COMMENT 'id',
    `st_id`            bigint      NOT NULL COMMENT '测站id',
    `stcd`             varchar(64) NOT NULL COMMENT '测站编码',
    `mot`              TIMESTAMP COMMENT '监测时间',
    `mp_q`             decimal(12, 3) COMMENT '瞬时流量',
    `acc_f`            decimal(12, 1) COMMENT '累计流量',
    `alarm_id`         bigint      NOT NULL COMMENT '报警id',
    `alarm_level`      varchar(20) COMMENT '报警级别',
    `mp_q_threshold`   varchar(32) COMMENT '瞬时流量阈值',
    `mp_q_level`       varchar(32) COMMENT '瞬时流量报警等级',
    `OPERATION_STATUS` varchar(20) COMMENT '操作状态',
    PRIMARY KEY (`id`)
);
