DROP TABLE IF EXISTS `wr_ra_real`;
CREATE TABLE `wr_ra_real`
(
    `id`                  BIGINT      NOT NULL COMMENT 'id',
    `st_id`               BIGINT      NOT NULL COMMENT '测站id',
    `stcd`                VARCHAR(32) NOT NULL,
    `cumulative_rainfall` DECIMAL(10, 1) COMMENT '累计雨量',
    `current_rainfall`    DECIMAL(10, 1) COMMENT '当日雨量',
    `day_rainfall`        DECIMAL(10, 1) COMMENT '日雨量',
    `mot`                 TIMESTAMP COMMENT '上报时间',
    `alarm_status`        VARCHAR(20) COMMENT '工况报警状态 NORMAL 正常 ALARM 报警',
    `comm_status`         VARCHAR(20) COMMENT '通讯状态 OFFLINE 离线 ONLINE 在线',
    `sttp`                VARCHAR(20) NOT NULL COMMENT '测站类型',
    `ten_minute`          DECIMAL(10, 1) COMMENT '10分钟雨量',
    `thirty_minute`       DECIMAL(10, 1) COMMENT '30分钟雨量',
    `one_hour`            DECIMAL(10, 1) COMMENT '1小时雨量',
    `two_hour`            DECIMAL(10, 1) COMMENT '2小时雨量',
    `three_hour`          DECIMAL(10, 1) COMMENT '3小时雨量',
    `six_hour`            DECIMAL(10, 1) COMMENT '6小时雨量',
    `twelve_hour`         DECIMAL(10, 1) COMMENT '12小时雨量',
    `twenty_four_hour`    DECIMAL(10, 1) COMMENT '24小时雨量',
    PRIMARY KEY (`id`)
);
DROP TABLE IF EXISTS `wr_ra_his`;
CREATE TABLE `wr_ra_his`
(
    `id`                  bigint      NOT NULL COMMENT 'id',
    `st_id`               bigint      NOT NULL COMMENT '测站id',
    `stcd`                varchar(32) NOT NULL COMMENT '测站编码',
    `cumulative_rainfall` DECIMAL(10, 1) COMMENT '累计雨量',
    `current_rainfall`    DECIMAL(10, 1) COMMENT '当日雨量',
    `day_rainfall`        DECIMAL(10, 1) COMMENT '日雨量',
    `mot`                 TIMESTAMP COMMENT '上报时间',
    `alarm_status`        varchar(20) COMMENT '工况报警状态 NORMAL 正常 ALARM 报警',
    `comm_status`         varchar(20) COMMENT '通讯状态 OFFLINE 离线 ONLINE 在线',
    `sttp`                varchar(20) NOT NULL COMMENT '测站类型',
    `operation_status` varchar(20) COMMENT '操作状态',
    PRIMARY KEY (`id`)
);
DROP TABLE IF EXISTS `wr_ra_alarm`;
CREATE TABLE `wr_ra_alarm`
(
    `id`                         bigint      NOT NULL COMMENT '主键id',
    `st_id`                      bigint      NOT NULL COMMENT '测站id',
    `stcd`                       varchar(20) NOT NULL COMMENT '测站编码',
    `mot`                        TIMESTAMP   NOT NULL COMMENT '上报时间',
    `local_dt`                   TIMESTAMP COMMENT '计算时间',
    `ten_minute`                 decimal(65, 30) COMMENT '10分钟雨量',
    `ten_minute_level`           varchar(32) COMMENT '10分钟报警级别',
    `ten_minute_threshold`       varchar(32) COMMENT '10分钟报警阈值',
    `thirty_minute`              decimal(65, 30) COMMENT '30分钟雨量',
    `thirty_minute_level`        varchar(32) COMMENT '30分钟报警级别',
    `thirty_minute_threshold`    varchar(32) COMMENT '30分钟报警阈值',
    `one_hour`                   decimal(65, 30) COMMENT '1小时雨量',
    `one_hour_level`             varchar(32) COMMENT '1小时报警级别',
    `one_hour_threshold`         varchar(32) COMMENT '1小时报警阈值',
    `two_hour`                   decimal(65, 30) COMMENT '2小时雨量',
    `two_hour_level`             varchar(32) COMMENT '2小时报警级别',
    `two_hour_threshold`         varchar(12) COMMENT '2小时报警阈值',
    `three_hour`                 decimal(65, 30) COMMENT '3小时雨量',
    `three_hour_level`           varchar(32) COMMENT '3小时报警级别',
    `three_hour_threshold`       varchar(32) COMMENT '3小时报警阈值',
    `six_hour`                   decimal(65, 30) COMMENT '6小时雨量',
    `six_hour_level`             varchar(32) COMMENT '6小时报警级别',
    `six_hour_threshold`         varchar(32) COMMENT '6小时报警阈值',
    `twelve_hour`                decimal(65, 30) COMMENT '12小时雨量',
    `twelve_hour_level`          varchar(32) COMMENT '12小时报警级别',
    `twelve_hour_threshold`      varchar(32) COMMENT '12小时报警阈值',
    `twenty_four_hour`           decimal(65, 30) COMMENT '24小时雨量',
    `twenty_four_hour_level`     varchar(32) COMMENT '24小时报警级别',
    `twenty_four_hour_threshold` varchar(32) COMMENT '24小时报警阈值',
    `tm`                         varchar(20) COMMENT '记录存储时服务器时钟（微秒）',
    `alarm_id`                   bigint COMMENT '报警记录ID',
    `alarm_level`                varchar(32) COMMENT '报警级别',
    PRIMARY KEY (`id`)
)
