DROP TABLE IF EXISTS `wr_soil_real`;
CREATE TABLE `wr_soil_real`
(
    `stcd`          char(8)     NOT NULL COMMENT '测站编码',
    `mot`           TIMESTAMP COMMENT '时间',
    `slm10`         decimal(5, 1) COMMENT '土壤含水率(10cm)',
    `slm20`         decimal(5, 1) COMMENT '土壤含水率(20cm)',
    `slm30`         decimal(5, 1) COMMENT '土壤含水率(30cm)',
    `slm40`         decimal(5, 1) COMMENT '土壤含水率(40cm)',
    `oC`            decimal(3, 1) COMMENT '温度 ℃',
    `alarm_status`  varchar(32) COMMENT '工况报警状态 NORMAL 正常 ALARM 报警',
    `comm_status`   varchar(32) COMMENT '通讯状态 OFFLINE 离线 ONLINE 在线',
    `st_id`         bigint      NOT NULL COMMENT '测站ID',
    `id`            bigint      NOT NULL COMMENT '主键',
    `rh10`          decimal(5, 1) COMMENT '土壤相对湿度(10cm)',
    `rh20`          decimal(5, 1) COMMENT '土壤相对湿度(20cm)',
    `rh30`          decimal(5, 1) COMMENT '土壤相对湿度(30cm)',
    `rh40`          decimal(5, 1) COMMENT '土壤相对湿度(40cm)',
    `slm_average`   decimal(5, 1) COMMENT '土壤含水率平均值',
    `rh_average`    decimal(5, 1) COMMENT '土壤相对湿度平均值',
    `sttp`          varchar(20) NOT NULL COMMENT '测站类型',
    `drought_level` varchar(20) COMMENT '干旱等级',
    PRIMARY KEY (`id`)
);
DROP TABLE IF EXISTS `wr_soil_his`;
CREATE TABLE `wr_soil_his`
(
    `stcd`             varchar(20) NOT NULL COMMENT '测站编码',
    `mot`              TIMESTAMP COMMENT '时间',
    `slm10`            decimal(5, 1) COMMENT '土壤含水率(10cm)',
    `slm20`            decimal(5, 1) COMMENT '土壤含水率(20cm)',
    `slm30`            decimal(5, 1) COMMENT '土壤含水率(30cm)',
    `slm40`            decimal(5, 1) COMMENT '土壤含水率(40cm)',
    `oC`               decimal(3, 1) COMMENT '温度 ℃',
    `alarm_status`     varchar(20) COMMENT '工况报警状态 NORMAL 正常 ALARM 报警',
    `comm_status`      varchar(20) COMMENT '通讯状态 OFFLINE 离线 ONLINE 在线',
    `st_id`            bigint      NOT NULL COMMENT '测站ID',
    `id`               bigint      NOT NULL COMMENT '主键',
    `rh10`             decimal(5, 1) COMMENT '土壤相对湿度(10cm)',
    `rh20`             decimal(5, 1) COMMENT '土壤相对湿度(20cm)',
    `rh30`             decimal(5, 1) COMMENT '土壤相对湿度(30cm)',
    `rh40`             decimal(5, 1) COMMENT '土壤相对湿度(40cm)',
    `slm_average`      decimal(5, 1) COMMENT '土壤含水率平均值',
    `rh_average`       decimal(5, 1) COMMENT '土壤相对湿度平均值',
    `sttp`             varchar(20) NOT NULL COMMENT '测站类型',
    `drought_level`    varchar(20) COMMENT '干旱等级',
    `OPERATION_STATUS` varchar(20) COMMENT '操作状态',
    PRIMARY KEY (`id`)
);
DROP TABLE IF EXISTS `wr_soil_alarm`;
CREATE TABLE `wr_soil_alarm`
(
    `id`                   bigint NOT NULL COMMENT '主键',
    `st_id`                bigint NOT NULL COMMENT '测站ID',
    `stcd`                 char(32) COMMENT '测站编码',
    `mot`                  TIMESTAMP COMMENT '时间',
    `slm10`                decimal(5, 1) COMMENT '土壤含水率(10cm)',
    `slm20`                decimal(5, 1) COMMENT '土壤含水率(20cm)',
    `slm30`                decimal(5, 1) COMMENT '土壤含水率(30cm)',
    `slm40`                decimal(5, 1) COMMENT '土壤含水率(40cm)',
    `oC`                   decimal(3, 1) COMMENT '温度 ℃',
    `rh10`                 decimal(5, 1) COMMENT '土壤相对湿度(10cm)',
    `rh20`                 decimal(5, 1) COMMENT '土壤相对湿度(20cm)',
    `rh30`                 decimal(5, 1) COMMENT '土壤相对湿度(30cm)',
    `rh40`                 decimal(5, 1) COMMENT '土壤相对湿度(40cm)',
    `slm_average`          decimal(5, 1) COMMENT '土壤含水率平均值',
    `rh_average`           decimal(9, 3) COMMENT '土壤相对湿度平均值',
    `alarm_id`             bigint NOT NULL COMMENT '测站报警id',
    `alarm_level`          varchar(20) COMMENT '报警等级',
    `drought_level`        varchar(20) COMMENT '干旱等级',
    `rh_average_level`     varchar(32) COMMENT '土壤相对湿度平均值等级',
    `rh_average_threshold` varchar(32) COMMENT '土壤相对湿度平均值阈值'
);
