DROP TABLE IF EXISTS `wr_ri_real`;
CREATE TABLE `wr_ri_real`
(
    `id`           bigint      NOT NULL COMMENT 'id',
    `st_id`        bigint      NOT NULL COMMENT '测站id',
    `stcd`         varchar(64) NOT NULL COMMENT '测站编码',
    `mot`          TIMESTAMP COMMENT '时间',
    `z`            decimal(7, 3) COMMENT '水位',
    `d`            decimal(9, 3) COMMENT '埋深',
    `alarm_status` varchar(20) COMMENT '工况报警状态 NORMAL 正常 ALARM 报警',
    `comm_status`  varchar(20) COMMENT '通讯状态 OFFLINE 离线 ONLINE 在线',
    `sttp`         varchar(20) NOT NULL COMMENT '测站类型',
    `wptn`         varchar(20) COMMENT '水势 涨 UP 落 DOWN 平 KEEP',
    `create_user`  bigint,
    `create_dept`  bigint,
    `create_time`  TIMESTAMP,
    `update_user`  bigint,
    `update_time`  TIMESTAMP,
    `status`       bigint,
    `is_deleted`   bigint,
    PRIMARY KEY (`id`)
);
DROP TABLE IF EXISTS `wr_ri_his`;
CREATE TABLE `wr_ri_his`
(
    `id`               bigint      NOT NULL COMMENT 'id',
    `st_id`            bigint      NOT NULL COMMENT '测站id',
    `stcd`             varchar(64) NOT NULL COMMENT '测站编码',
    `mot`              TIMESTAMP COMMENT '时间',
    `z`                decimal(7, 3) COMMENT '水位',
    `d`                decimal(9, 3) COMMENT '埋深',
    `alarm_status`     varchar(20) COMMENT '工况报警状态 NORMAL 正常 ALARM 报警',
    `comm_status`      varchar(20) COMMENT '通讯状态 OFFLINE 离线 ONLINE 在线',
    `sttp`             varchar(20) NOT NULL COMMENT '测站类型',
    `wptn`             varchar(20) COMMENT '水势 涨 UP 落 DOWN 平 KEEP',
    `OPERATION_STATUS` varchar(20) COMMENT '操作状态',
    `create_user`      bigint,
    `create_dept`      bigint,
    `create_time`      TIMESTAMP,
    `update_user`      bigint,
    `update_time`      TIMESTAMP,
    `status`           bigint,
    `is_deleted`       bigint,
    PRIMARY KEY (`id`)
);
DROP TABLE IF EXISTS `wr_ri_alarm`;
CREATE TABLE `wr_ri_alarm`
(
    `id`          bigint NOT NULL COMMENT 'id',
    `st_id`       bigint NOT NULL COMMENT '测站id',
    `stcd`        varchar(64) COMMENT '测站编码',
    `mot`         TIMESTAMP COMMENT '时间',
    `z`           decimal(7, 3) COMMENT '水位',
    `d`           decimal(9, 3) COMMENT '埋深',
    `alarm_id`    bigint NOT NULL COMMENT '测站报警id',
    `alarm_level` varchar(20) COMMENT '报警级别',
    `z_threshold` varchar(32) COMMENT '水位报警阈值',
    `z_level`     varchar(32) COMMENT '水位报警级别',
    `wptn`        varchar(20) COMMENT '水势 涨 UP 落 DOWN 平 KEEP',
    PRIMARY KEY (`id`)
);



INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601803251786305537, 1540542229418909697, '4173630016', '2022-12-11 13:00:00', '0.178', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 12:57:09.948', NULL,
        '2022-12-11 12:57:09.948', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601803300540895234, 1540542229402132481, '4173630000', '2022-12-11 13:00:00', '-0.389', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 12:57:21.572', NULL,
        '2022-12-11 12:57:21.572', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601803611737280513, 1540542229347606530, '4173630014', '2022-12-11 13:00:00', '0.089', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 12:58:35.767', NULL,
        '2022-12-11 12:58:35.767', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601803654594678785, 1546097849947701249, '4173630013', '2022-12-11 13:00:00', '0.735', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 12:58:45.985', NULL,
        '2022-12-11 12:58:45.985', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601803775831035906, 1540542229423104001, '4173630006', '2022-12-11 13:00:00', '0.313', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 12:59:14.89', NULL, '2022-12-11 12:59:14.89',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601803809385467906, 1540542229427298305, '4173630010', '2022-12-11 13:00:00', '0.241', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 12:59:22.89', NULL, '2022-12-11 12:59:22.89',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601803819476963330, 1540542229414715394, '4173630012', '2022-12-11 13:00:00', '0.016', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 12:59:25.296', NULL,
        '2022-12-11 12:59:25.296', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601803876796321793, 1540542229376966658, '4173630004', '2022-12-11 13:00:00', '0.032', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 12:59:38.962', NULL,
        '2022-12-11 12:59:38.962', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601803889639280642, 1540542229431492610, '4173630017', '2022-12-11 13:00:00', '0.100', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 12:59:42.024', NULL,
        '2022-12-11 12:59:42.024', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601803994291359746, 1542497336240947201, '4173630011', '2022-12-11 13:00:00', '-0.437', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 13:00:06.975', NULL,
        '2022-12-11 13:00:06.975', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601803999249027074, 1540542229423104002, '4173630001', '2022-12-11 13:00:00', '0.520', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 13:00:08.157', NULL,
        '2022-12-11 13:00:08.157', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601804069335846914, 1540542229330829313, '4173630015', '2022-12-11 13:00:00', '0.043', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 13:00:24.867', NULL,
        '2022-12-11 13:00:24.867', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601804112855945217, 1540542229406326787, '4173630008', '2022-12-11 13:00:00', '0.414', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 13:00:35.243', NULL,
        '2022-12-11 13:00:35.243', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601810336032870401, 1540542229330829313, '4173630015', '2022-12-11 13:25:00', '0.039', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 13:25:18.964', NULL,
        '2022-12-11 13:25:18.964', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601812999134892034, 1540542229397938178, '4173630007', '2022-12-11 13:40:00', '0.031', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 13:35:53.897', NULL,
        '2022-12-11 13:35:53.897', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601818129137782785, 1540542229414715395, '4173630009', '2022-12-11 14:00:00', '0.775', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 13:56:16.985', NULL,
        '2022-12-11 13:56:16.985', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601818200428367874, 1540542229397938178, '4173630007', '2022-12-11 14:00:00', '0.024', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 13:56:33.982', NULL,
        '2022-12-11 13:56:33.982', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601818208984748034, 1540542229410521090, '4173630002', '2022-12-11 14:00:00', '0.001', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 13:56:36.022', NULL,
        '2022-12-11 13:56:36.022', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601818295697788930, 1540542229372772353, '4173630003', '2022-12-11 14:00:00', '-0.201', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 13:56:56.696', NULL,
        '2022-12-11 13:56:56.696', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601818304270946306, 1540542229418909698, '4173630005', '2022-12-11 14:00:00', '0.489', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 13:56:58.74', NULL, '2022-12-11 13:56:58.74',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601818352148926466, 1540542229418909697, '4173630016', '2022-12-11 14:00:00', '0.178', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 13:57:10.155', NULL,
        '2022-12-11 13:57:10.155', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601818399842357249, 1540542229402132481, '4173630000', '2022-12-11 14:00:00', '-0.387', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 13:57:21.526', NULL,
        '2022-12-11 13:57:21.526', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601818754403651586, 1546097849947701249, '4173630013', '2022-12-11 14:00:00', '0.749', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 13:58:46.06', NULL, '2022-12-11 13:58:46.06',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601818854119034882, 1540542229423104001, '4173630006', '2022-12-11 14:00:00', '0.316', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 13:59:09.834', NULL,
        '2022-12-11 13:59:09.834', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601818921953513473, 1540542229414715394, '4173630012', '2022-12-11 14:00:00', '0.005', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 13:59:26.007', NULL,
        '2022-12-11 13:59:26.007', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601818989540528130, 1540542229431492610, '4173630017', '2022-12-11 14:00:00', '0.099', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 13:59:42.12', NULL, '2022-12-11 13:59:42.12',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601819084176609281, 1542497336240947201, '4173630011', '2022-12-11 14:00:00', '-0.436', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 14:00:04.683', NULL,
        '2022-12-11 14:00:04.683', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601819147460268034, 1540542229330829313, '4173630015', '2022-12-11 14:00:00', '0.037', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 14:00:19.772', NULL,
        '2022-12-11 14:00:19.772', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601819213545721857, 1540542229406326787, '4173630008', '2022-12-11 14:00:00', '0.413', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 14:00:35.527', NULL,
        '2022-12-11 14:00:35.527', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601819292662878210, 1540542229423104002, '4173630001', '2022-12-11 14:00:00', '0.512', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 14:00:54.391', NULL,
        '2022-12-11 14:00:54.391', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601819550109257730, 1540542229322440706, '4173630019', '2022-12-11 14:00:00', '0.179', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 14:01:55.771', NULL,
        '2022-12-11 14:01:55.771', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601820438831943681, 1540542229376966658, '4173630004', '2022-12-11 14:05:00', '0.029', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 14:05:27.659', NULL,
        '2022-12-11 14:05:27.659', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601825898637152258, 1540542229418909697, '4173630016', '2022-12-11 14:30:00', '0.177', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 14:27:09.378', NULL,
        '2022-12-11 14:27:09.378', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601829761394003970, 1540542229376966658, '4173630004', '2022-12-11 14:45:00', '0.033', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 14:42:30.331', NULL,
        '2022-12-11 14:42:30.331', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601830783315202050, 1540542229397938178, '4173630007', '2022-12-11 14:50:00', '0.019', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 14:46:33.976', NULL,
        '2022-12-11 14:46:33.976', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601833042157621250, 1540542229372772353, '4173630003', '2022-12-11 15:00:00', '-0.195', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 14:55:32.526', NULL,
        '2022-12-11 14:55:32.526', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601833192968015873, 1540542229376966658, '4173630004', '2022-12-11 15:00:00', '0.036', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 14:56:08.482', NULL,
        '2022-12-11 14:56:08.482', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601833222680465409, 1540542229410521090, '4173630002', '2022-12-11 15:00:00', '0.010', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 14:56:15.566', NULL,
        '2022-12-11 14:56:15.566', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601833228472799233, 1540542229414715395, '4173630009', '2022-12-11 15:00:00', '0.781', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 14:56:16.946', NULL,
        '2022-12-11 14:56:16.946', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601833396685361153, 1540542229418909698, '4173630005', '2022-12-11 15:00:00', '0.486', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 14:56:57.051', NULL,
        '2022-12-11 14:56:57.051', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601833451915956226, 1540542229418909697, '4173630016', '2022-12-11 15:00:00', '-0.164', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 14:57:10.22', NULL, '2022-12-11 14:57:10.22',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601833499991068674, 1540542229402132481, '4173630000', '2022-12-11 15:00:00', '-0.391', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 14:57:21.682', NULL,
        '2022-12-11 14:57:21.682', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601833853923217410, 1546097849947701249, '4173630013', '2022-12-11 15:00:00', '0.753', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 14:58:46.066', NULL,
        '2022-12-11 14:58:46.066', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601833953856704513, 1540542229423104001, '4173630006', '2022-12-11 15:00:00', '0.317', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 14:59:09.892', NULL,
        '2022-12-11 14:59:09.892', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601834021007511554, 1540542229414715394, '4173630012', '2022-12-11 15:00:00', '-0.040', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 14:59:25.902', NULL,
        '2022-12-11 14:59:25.902', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601834089026539521, 1540542229431492610, '4173630017', '2022-12-11 15:00:00', '0.098', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 14:59:42.119', NULL,
        '2022-12-11 14:59:42.119', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601834182844731394, 1542497336240947201, '4173630011', '2022-12-11 15:00:00', '-0.433', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 15:00:04.487', NULL,
        '2022-12-11 15:00:04.487', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601834256685453314, 1540542229427298305, '4173630010', '2022-12-11 15:00:00', '0.234', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 15:00:22.092', NULL,
        '2022-12-11 15:00:22.092', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601834264356835329, 1540542229330829313, '4173630015', '2022-12-11 15:00:00', '0.034', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 15:00:23.92', NULL, '2022-12-11 15:00:23.92',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601834313228865538, 1540542229406326787, '4173630008', '2022-12-11 15:00:00', '0.411', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 15:00:35.573', NULL,
        '2022-12-11 15:00:35.573', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601834706943987713, 1540542229418909697, '4173630016', '2022-12-11 15:05:00', '0.172', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 15:02:09.442', NULL,
        '2022-12-11 15:02:09.442', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601834901559693314, 1540542229322440706, '4173630019', '2022-12-11 15:00:00', '0.179', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 15:02:55.842', NULL,
        '2022-12-11 15:02:55.842', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601838333125316609, 1540542229397938178, '4173630007', '2022-12-11 15:20:00', '0.015', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 15:16:33.991', NULL,
        '2022-12-11 15:16:33.991', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601838559361880066, 1540542229418909698, '4173630005', '2022-12-11 15:20:00', '0.484', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 15:17:27.93', NULL, '2022-12-11 15:17:27.93',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601839670328152065, 1540542229397938178, '4173630007', '2022-12-11 15:25:00', '0.014', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 15:21:52.805', NULL,
        '2022-12-11 15:21:52.805', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601840770477314050, 1540542229397938178, '4173630007', '2022-12-11 15:30:00', '0.013', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 15:26:15.101', NULL,
        '2022-12-11 15:26:15.101', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601842026683944962, 1540542229397938178, '4173630007', '2022-12-11 15:35:00', '0.012', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 15:31:14.604', NULL,
        '2022-12-11 15:31:14.604', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601843284975144961, 1540542229397938178, '4173630007', '2022-12-11 15:40:00', '0.011', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 15:36:14.604', NULL,
        '2022-12-11 15:36:14.604', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601844543404756994, 1540542229397938178, '4173630007', '2022-12-11 15:45:00', '0.009', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 15:41:14.637', NULL,
        '2022-12-11 15:41:14.637', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601844773827235842, 1540542229418909697, '4173630016', '2022-12-11 15:45:00', '0.181', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 15:42:09.574', NULL,
        '2022-12-11 15:42:09.574', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601844932799746049, 1540542229376966658, '4173630004', '2022-12-11 15:45:00', '0.038', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 15:42:47.476', NULL,
        '2022-12-11 15:42:47.476', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601845801553350657, 1540542229397938178, '4173630007', '2022-12-11 15:50:00', '0.008', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 15:46:14.603', NULL,
        '2022-12-11 15:46:14.603', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601847059966185473, 1540542229397938178, '4173630007', '2022-12-11 15:55:00', '0.006', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 15:51:14.631', NULL,
        '2022-12-11 15:51:14.631', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601848141677187074, 1540542229372772353, '4173630003', '2022-12-11 16:00:00', '-0.193', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 15:55:32.532', NULL,
        '2022-12-11 15:55:32.532', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601848319234658306, 1540542229397938178, '4173630007', '2022-12-11 16:00:00', '0.005', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 15:56:14.865', NULL,
        '2022-12-11 15:56:14.865', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601848328130777090, 1540542229414715395, '4173630009', '2022-12-11 16:00:00', '0.776', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 15:56:16.986', NULL,
        '2022-12-11 15:56:16.986', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601848387106885634, 1540542229376966658, '4173630004', '2022-12-11 16:00:00', '0.044', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 15:56:31.047', NULL,
        '2022-12-11 15:56:31.047', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601848405360496642, 1540542229410521090, '4173630002', '2022-12-11 16:00:00', '0.003', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 15:56:35.398', NULL,
        '2022-12-11 15:56:35.398', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601848496200732673, 1540542229418909698, '4173630005', '2022-12-11 16:00:00', '0.476', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 15:56:57.057', NULL,
        '2022-12-11 15:56:57.057', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601848598885683201, 1540542229402132481, '4173630000', '2022-12-11 16:00:00', '-0.398', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 15:57:21.538', NULL,
        '2022-12-11 15:57:21.538', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601848720524693506, 1540542229418909697, '4173630016', '2022-12-11 16:00:00', '-0.160', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 15:57:50.54', NULL, '2022-12-11 15:57:50.54',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601848953375674370, 1546097849947701249, '4173630013', '2022-12-11 16:00:00', '0.742', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 15:58:46.056', NULL,
        '2022-12-11 15:58:46.056', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601849053267218434, 1540542229423104001, '4173630006', '2022-12-11 16:00:00', '0.320', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 15:59:09.871', NULL,
        '2022-12-11 15:59:09.871', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601849120489328642, 1540542229414715394, '4173630012', '2022-12-11 16:00:00', '0.006', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 15:59:25.898', NULL,
        '2022-12-11 15:59:25.898', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601849187342340097, 1540542229431492610, '4173630017', '2022-12-11 16:00:00', '0.107', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 15:59:41.838', NULL,
        '2022-12-11 15:59:41.838', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601849261782847490, 1540542229330829313, '4173630015', '2022-12-11 16:00:00', '0.039', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 15:59:59.586', NULL,
        '2022-12-11 15:59:59.586', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601849309300117506, 1540542229322440706, '4173630019', '2022-12-11 16:00:00', '0.179', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 16:00:10.915', NULL,
        '2022-12-11 16:00:10.915', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601849356137910274, 1540542229427298305, '4173630010', '2022-12-11 16:00:00', '0.231', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 16:00:22.082', NULL,
        '2022-12-11 16:00:22.082', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601849412018622466, 1540542229406326787, '4173630008', '2022-12-11 16:00:00', '0.411', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 16:00:35.405', NULL,
        '2022-12-11 16:00:35.405', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601849476405383170, 1542497336240947201, '4173630011', '2022-12-11 16:00:00', '-0.429', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 16:00:50.756', NULL,
        '2022-12-11 16:00:50.756', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601849576745717762, 1540542229397938178, '4173630007', '2022-12-11 16:05:00', '0.004', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 16:01:14.679', NULL,
        '2022-12-11 16:01:14.679', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601850814832955394, 1540542229397938178, '4173630007', '2022-12-11 16:10:00', '0.004', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 16:06:09.862', NULL,
        '2022-12-11 16:06:09.862', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601851988428574721, 1540542229397938178, '4173630007', '2022-12-11 16:15:00', '0.004', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 16:10:49.669', NULL,
        '2022-12-11 16:10:49.669', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601853331734122497, 1540542229397938178, '4173630007', '2022-12-11 16:20:00', '0.005', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 16:16:09.938', NULL,
        '2022-12-11 16:16:09.938', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601853580372463618, 1540542229418909697, '4173630016', '2022-12-11 16:20:00', '0.173', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 16:17:09.218', NULL,
        '2022-12-11 16:17:09.218', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601854506143436802, 1540542229397938178, '4173630007', '2022-12-11 16:25:00', '0.006', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 16:20:49.939', NULL,
        '2022-12-11 16:20:49.939', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601855763566415873, 1540542229397938178, '4173630007', '2022-12-11 16:30:00', '0.006', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 16:25:49.732', NULL,
        '2022-12-11 16:25:49.732', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601857266154852353, 1540542229397938178, '4173630007', '2022-12-11 16:35:00', '0.006', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 16:31:47.977', NULL,
        '2022-12-11 16:31:47.977', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601858279939100673, 1540542229397938178, '4173630007', '2022-12-11 16:40:00', '0.006', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 16:35:49.682', NULL,
        '2022-12-11 16:35:49.682', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601859782242324482, 1540542229397938178, '4173630007', '2022-12-11 16:45:00', '0.006', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 16:41:47.859', NULL,
        '2022-12-11 16:41:47.859', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601860796576026625, 1540542229397938178, '4173630007', '2022-12-11 16:50:00', '0.006', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 16:45:49.695', NULL,
        '2022-12-11 16:45:49.695', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601862298707283969, 1540542229397938178, '4173630007', '2022-12-11 16:55:00', '0.005', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 16:51:47.831', NULL,
        '2022-12-11 16:51:47.831', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601863241242890241, 1540542229372772353, '4173630003', '2022-12-11 17:00:00', '-0.205', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 16:55:32.549', NULL,
        '2022-12-11 16:55:32.549', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601863313254895617, 1540542229397938178, '4173630007', '2022-12-11 17:00:00', '0.004', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 16:55:49.718', NULL,
        '2022-12-11 16:55:49.718', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601863427604205570, 1540542229414715395, '4173630009', '2022-12-11 17:00:00', '0.776', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 16:56:16.981', NULL,
        '2022-12-11 16:56:16.981', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601863505144303617, 1540542229410521090, '4173630002', '2022-12-11 17:00:00', '0.012', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 16:56:35.467', NULL,
        '2022-12-11 16:56:35.467', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601863571787599874, 1540542229376966658, '4173630004', '2022-12-11 17:00:00', '0.054', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 16:56:51.357', NULL,
        '2022-12-11 16:56:51.357', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601863595988733953, 1540542229418909698, '4173630005', '2022-12-11 17:00:00', '0.471', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 16:56:57.127', NULL,
        '2022-12-11 16:56:57.127', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601863649906511874, 1540542229418909697, '4173630016', '2022-12-11 17:00:00', '0.185', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 16:57:09.982', NULL,
        '2022-12-11 16:57:09.982', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601863698627547137, 1540542229402132481, '4173630000', '2022-12-11 17:00:00', '-0.399', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 16:57:21.598', NULL,
        '2022-12-11 16:57:21.598', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601864053054623745, 1546097849947701249, '4173630013', '2022-12-11 17:00:00', '0.740', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 16:58:46.1', NULL, '2022-12-11 16:58:46.1',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601864153097162753, 1540542229423104001, '4173630006', '2022-12-11 17:00:00', '0.318', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 16:59:09.952', NULL,
        '2022-12-11 16:59:09.952', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601864220830978050, 1540542229414715394, '4173630012', '2022-12-11 17:00:00', '-0.009', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 16:59:26.101', NULL,
        '2022-12-11 16:59:26.101', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601864288497684481, 1540542229431492610, '4173630017', '2022-12-11 17:00:00', '0.094', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 16:59:42.234', NULL,
        '2022-12-11 16:59:42.234', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601864361277247489, 1540542229330829313, '4173630015', '2022-12-11 17:00:00', '0.034', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 16:59:59.586', NULL,
        '2022-12-11 16:59:59.586', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601864399307001858, 1540542229423104002, '4173630001', '2022-12-11 17:00:00', '0.505', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 17:00:08.653', NULL,
        '2022-12-11 17:00:08.653', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601864408836460546, 1540542229322440706, '4173630019', '2022-12-11 17:00:00', '0.179', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 17:00:10.925', NULL,
        '2022-12-11 17:00:10.925', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601864511945035777, 1540542229406326787, '4173630008', '2022-12-11 17:00:00', '0.411', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 17:00:35.508', NULL,
        '2022-12-11 17:00:35.508', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601864706241974274, 1540542229427298305, '4173630010', '2022-12-11 17:00:00', '0.230', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 17:01:21.832', NULL,
        '2022-12-11 17:01:21.832', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601864815281295361, 1540542229397938178, '4173630007', '2022-12-11 17:05:00', '0.003', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 17:01:47.829', NULL,
        '2022-12-11 17:01:47.829', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601864822726184961, 1542497336240947201, '4173630011', '2022-12-11 17:00:00', '-0.427', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 17:01:49.604', NULL,
        '2022-12-11 17:01:49.604', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601865829996679169, 1540542229397938178, '4173630007', '2022-12-11 17:10:00', '0.002', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 17:05:49.756', NULL,
        '2022-12-11 17:05:49.756', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601867332618670081, 1540542229397938178, '4173630007', '2022-12-11 17:15:00', '0.001', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 17:11:48.009', NULL,
        '2022-12-11 17:11:48.009', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601868346465832961, 1540542229397938178, '4173630007', '2022-12-11 17:20:00', '0.000', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 17:15:49.729', NULL,
        '2022-12-11 17:15:49.729', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601872365884133378, 1540542229397938178, '4173630007', '2022-12-11 17:35:00', '-0.002', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 17:31:48.033', NULL,
        '2022-12-11 17:31:48.033', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601873380272361474, 1540542229397938178, '4173630007', '2022-12-11 17:40:00', '-0.002', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 17:35:49.882', NULL,
        '2022-12-11 17:35:49.882', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601873713237184514, 1540542229418909697, '4173630016', '2022-12-11 17:40:00', '0.173', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 17:37:09.267', NULL,
        '2022-12-11 17:37:09.267', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601874882034520066, 1540542229397938178, '4173630007', '2022-12-11 17:45:00', '-0.002', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 17:41:47.93', NULL, '2022-12-11 17:41:47.93',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601875896267558914, 1540542229397938178, '4173630007', '2022-12-11 17:50:00', '-0.003', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 17:45:49.741', NULL,
        '2022-12-11 17:45:49.741', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601877398323318786, 1540542229397938178, '4173630007', '2022-12-11 17:55:00', '-0.003', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 17:51:47.86', NULL, '2022-12-11 17:51:47.86',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601878340955394049, 1540542229372772353, '4173630003', '2022-12-11 18:00:00', '-0.213', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 17:55:32.601', NULL,
        '2022-12-11 17:55:32.601', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601878412740907010, 1540542229397938178, '4173630007', '2022-12-11 18:00:00', '-0.003', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 17:55:49.715', NULL,
        '2022-12-11 17:55:49.715', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601878424048750593, 1540542229410521090, '4173630002', '2022-12-11 18:00:00', '0.004', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 17:55:52.412', NULL,
        '2022-12-11 17:55:52.412', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601878527241211905, 1540542229414715395, '4173630009', '2022-12-11 18:00:00', '0.784', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 17:56:17.015', NULL,
        '2022-12-11 17:56:17.015', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601878584556376065, 1540542229376966658, '4173630004', '2022-12-11 18:00:00', '0.057', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 17:56:30.68', NULL, '2022-12-11 17:56:30.68',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601878695516688386, 1540542229418909698, '4173630005', '2022-12-11 18:00:00', '0.466', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 17:56:57.134', NULL,
        '2022-12-11 17:56:57.134', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601878790312153090, 1540542229418909697, '4173630016', '2022-12-11 18:00:00', '0.197', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 17:57:19.736', NULL,
        '2022-12-11 17:57:19.736', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601878796607803394, 1540542229402132481, '4173630000', '2022-12-11 18:00:00', '-0.400', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 17:57:21.237', NULL,
        '2022-12-11 17:57:21.237', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601879113890123778, 1540542229427298305, '4173630010', '2022-12-11 18:00:00', '0.231', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 17:58:36.883', NULL,
        '2022-12-11 17:58:36.883', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601879152540635137, 1546097849947701249, '4173630013', '2022-12-11 18:00:00', '0.745', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 17:58:46.098', NULL,
        '2022-12-11 17:58:46.098', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601879211256696834, 1540542229423104001, '4173630006', '2022-12-11 18:00:00', '0.319', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 17:59:00.097', NULL,
        '2022-12-11 17:59:00.097', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601879320237297665, 1540542229414715394, '4173630012', '2022-12-11 18:00:00', '-0.073', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 17:59:26.079', NULL,
        '2022-12-11 17:59:26.079', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601879386805096449, 1540542229431492610, '4173630017', '2022-12-11 18:00:00', '0.094', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 17:59:41.951', NULL,
        '2022-12-11 17:59:41.951', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601879460826173441, 1540542229330829313, '4173630015', '2022-12-11 18:00:00', '0.032', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 17:59:59.599', NULL,
        '2022-12-11 17:59:59.599', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601879498180644865, 1540542229423104002, '4173630001', '2022-12-11 18:00:00', '0.502', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 18:00:08.505', NULL,
        '2022-12-11 18:00:08.505', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601879508091785217, 1540542229322440706, '4173630019', '2022-12-11 18:00:00', '0.179', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 18:00:10.868', NULL,
        '2022-12-11 18:00:10.868', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601879608327262209, 1540542229347606530, '4173630014', '2022-12-11 18:00:00', '0.087', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 18:00:34.766', NULL,
        '2022-12-11 18:00:34.766', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601879617072386050, 1540542229406326787, '4173630008', '2022-12-11 18:00:00', '0.410', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 18:00:36.851', NULL,
        '2022-12-11 18:00:36.851', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601879914842804226, 1540542229397938178, '4173630007', '2022-12-11 18:05:00', '-0.003', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 18:01:47.845', NULL,
        '2022-12-11 18:01:47.845', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601879923839586305, 1542497336240947201, '4173630011', '2022-12-11 18:00:00', '-0.426', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 18:01:49.99', NULL, '2022-12-11 18:01:49.99',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601880930074087426, 1540542229397938178, '4173630007', '2022-12-11 18:10:00', '-0.003', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 18:05:49.895', NULL,
        '2022-12-11 18:05:49.895', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601882431391649793, 1540542229397938178, '4173630007', '2022-12-11 18:15:00', '-0.004', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 18:11:47.837', NULL,
        '2022-12-11 18:11:47.837', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601883445897318401, 1540542229397938178, '4173630007', '2022-12-11 18:20:00', '-0.004', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 18:15:49.714', NULL,
        '2022-12-11 18:15:49.714', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601884948062130177, 1540542229397938178, '4173630007', '2022-12-11 18:25:00', '-0.004', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 18:21:47.858', NULL,
        '2022-12-11 18:21:47.858', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601885962739765249, 1540542229397938178, '4173630007', '2022-12-11 18:30:00', '-0.004', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 18:25:49.776', NULL,
        '2022-12-11 18:25:49.776', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601887464728416258, 1540542229397938178, '4173630007', '2022-12-11 18:35:00', '-0.004', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 18:31:47.878', NULL,
        '2022-12-11 18:31:47.878', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601888479498326017, 1540542229397938178, '4173630007', '2022-12-11 18:40:00', '-0.004', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 18:35:49.817', NULL,
        '2022-12-11 18:35:49.817', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601890995946508290, 1540542229397938178, '4173630007', '2022-12-11 18:50:00', '0.002', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 18:45:49.785', NULL,
        '2022-12-11 18:45:49.785', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601891330228342785, 1540542229418909697, '4173630016', '2022-12-11 18:50:00', '0.195', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 18:47:09.485', NULL,
        '2022-12-11 18:47:09.485', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601891623892537345, 1540542229418909698, '4173630005', '2022-12-11 18:50:00', '0.466', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 18:48:19.499', NULL,
        '2022-12-11 18:48:19.499', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601892498035822593, 1540542229397938178, '4173630007', '2022-12-11 18:55:00', '0.003', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 18:51:47.912', NULL,
        '2022-12-11 18:51:47.912', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601893440584011778, 1540542229372772353, '4173630003', '2022-12-11 19:00:00', '-0.214', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 18:55:32.633', NULL,
        '2022-12-11 18:55:32.633', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601893512344358914, 1540542229397938178, '4173630007', '2022-12-11 19:00:00', '0.005', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 18:55:49.742', NULL,
        '2022-12-11 18:55:49.742', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601893523652202498, 1540542229410521090, '4173630002', '2022-12-11 19:00:00', '0.009', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 18:55:52.438', NULL,
        '2022-12-11 18:55:52.438', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601893626748194818, 1540542229414715395, '4173630009', '2022-12-11 19:00:00', '0.782', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 18:56:17.018', NULL,
        '2022-12-11 18:56:17.018', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601893770986115073, 1540542229376966658, '4173630004', '2022-12-11 19:00:00', '0.057', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 18:56:51.407', NULL,
        '2022-12-11 18:56:51.407', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601893796223242242, 1540542229418909698, '4173630005', '2022-12-11 19:00:00', '0.463', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 18:56:57.423', NULL,
        '2022-12-11 18:56:57.423', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601893896567771137, 1540542229402132481, '4173630000', '2022-12-11 19:00:00', '-0.401', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 18:57:21.348', NULL,
        '2022-12-11 18:57:21.348', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601894016336121858, 1540542229347606530, '4173630014', '2022-12-11 19:00:00', '0.087', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 18:57:49.903', NULL,
        '2022-12-11 18:57:49.903', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601894222943342594, 1540542229427298305, '4173630010', '2022-12-11 19:00:00', '0.232', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 18:58:39.162', NULL,
        '2022-12-11 18:58:39.162', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601894252253138945, 1546097849947701249, '4173630013', '2022-12-11 19:00:00', '0.745', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 18:58:46.15', NULL, '2022-12-11 18:58:46.15',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601894310734319617, 1540542229423104001, '4173630006', '2022-12-11 19:00:00', '0.317', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 18:59:00.093', NULL,
        '2022-12-11 18:59:00.093', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601894419702337537, 1540542229414715394, '4173630012', '2022-12-11 19:00:00', '-0.081', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 18:59:26.073', NULL,
        '2022-12-11 18:59:26.073', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601894486458880001, 1540542229431492610, '4173630017', '2022-12-11 19:00:00', '0.100', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 18:59:41.989', NULL,
        '2022-12-11 18:59:41.989', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601894560639340545, 1540542229330829313, '4173630015', '2022-12-11 19:00:00', '0.030', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 18:59:59.675', NULL,
        '2022-12-11 18:59:59.675', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601894581480837122, 1542497336240947201, '4173630011', '2022-12-11 19:00:00', '-0.425', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 19:00:04.644', NULL,
        '2022-12-11 19:00:04.644', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601894597763125249, 1540542229423104002, '4173630001', '2022-12-11 19:00:00', '0.507', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 19:00:08.526', NULL,
        '2022-12-11 19:00:08.526', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601894608190164994, 1540542229322440706, '4173630019', '2022-12-11 19:00:00', '0.179', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 19:00:11.012', NULL,
        '2022-12-11 19:00:11.012', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601894710740897793, 1540542229406326787, '4173630008', '2022-12-11 19:00:00', '0.410', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 19:00:35.462', NULL,
        '2022-12-11 19:00:35.462', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601895014366564353, 1540542229397938178, '4173630007', '2022-12-11 19:05:00', '0.006', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 19:01:47.852', NULL,
        '2022-12-11 19:01:47.852', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601895104237916162, 1540542229418909697, '4173630016', '2022-12-11 19:05:00', '0.201', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 19:02:09.279', NULL,
        '2022-12-11 19:02:09.279', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601896029119696897, 1540542229397938178, '4173630007', '2022-12-11 19:10:00', '0.007', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 19:05:49.788', NULL,
        '2022-12-11 19:05:49.788', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601896790998241281, 1540542229418909698, '4173630005', '2022-12-11 19:10:00', '0.465', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 19:08:51.434', NULL,
        '2022-12-11 19:08:51.434', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601897531804602369, 1540542229397938178, '4173630007', '2022-12-11 19:15:00', '0.008', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 19:11:48.056', NULL,
        '2022-12-11 19:11:48.056', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601898282681487362, 1542497336240947201, '4173630011', '2022-12-11 19:14:46.868', NULL, NULL, 'NORMAL',
        'OFFLINE', 'PZ', 'KEEP', 'OFFLINE', NULL, NULL, '2022-12-11 19:14:47.078', NULL, '2022-12-11 19:14:47.078',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601898546272522242, 1540542229397938178, '4173630007', '2022-12-11 19:20:00', '0.008', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 19:15:49.924', NULL,
        '2022-12-11 19:15:49.924', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601900047728496641, 1540542229397938178, '4173630007', '2022-12-11 19:25:00', '0.009', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 19:21:47.899', NULL,
        '2022-12-11 19:21:47.899', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601901062880088065, 1540542229397938178, '4173630007', '2022-12-11 19:30:00', '0.010', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 19:25:49.93', NULL, '2022-12-11 19:25:49.93',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601902110512369665, 1540542229410521090, '4173630002', '2022-12-11 19:30:00', '0.005', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 19:29:59.705', NULL,
        '2022-12-11 19:29:59.705', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601902564642246658, 1540542229397938178, '4173630007', '2022-12-11 19:35:00', '0.010', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 19:31:47.978', NULL,
        '2022-12-11 19:31:47.978', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601903579705757697, 1540542229397938178, '4173630007', '2022-12-11 19:40:00', '0.011', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 19:35:49.988', NULL,
        '2022-12-11 19:35:49.988', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601905081069457410, 1540542229397938178, '4173630007', '2022-12-11 19:45:00', '0.011', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 19:41:47.94', NULL, '2022-12-11 19:41:47.94',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601906095432519681, 1540542229397938178, '4173630007', '2022-12-11 19:50:00', '0.011', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 19:45:49.784', NULL,
        '2022-12-11 19:45:49.784', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601907597383421954, 1540542229397938178, '4173630007', '2022-12-11 19:55:00', '0.012', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 19:51:47.877', NULL,
        '2022-12-11 19:51:47.877', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601908540439121921, 1540542229372772353, '4173630003', '2022-12-11 20:00:00', '-0.216', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 19:55:32.719', NULL,
        '2022-12-11 19:55:32.719', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601908612635676673, 1540542229397938178, '4173630007', '2022-12-11 20:00:00', '0.012', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 19:55:49.932', NULL,
        '2022-12-11 19:55:49.932', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601908624253898753, 1540542229410521090, '4173630002', '2022-12-11 20:00:00', '0.015', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 19:55:52.702', NULL,
        '2022-12-11 19:55:52.702', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601908726825603073, 1540542229414715395, '4173630009', '2022-12-11 20:00:00', '0.781', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 19:56:17.157', NULL,
        '2022-12-11 19:56:17.157', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601908871084494849, 1540542229376966658, '4173630004', '2022-12-11 20:00:00', '0.059', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 19:56:51.55', NULL, '2022-12-11 19:56:51.55',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601908895537287170, 1540542229418909698, '4173630005', '2022-12-11 20:00:00', '0.421', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 19:56:57.381', NULL,
        '2022-12-11 19:56:57.381', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601908947798315009, 1540542229418909697, '4173630016', '2022-12-11 20:00:00', '0.189', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 19:57:09.841', NULL,
        '2022-12-11 19:57:09.841', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601909081596612609, 1540542229402132481, '4173630000', '2022-12-11 20:00:00', '-0.401', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 19:57:41.741', NULL,
        '2022-12-11 19:57:41.741', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601909116002488322, 1540542229347606530, '4173630014', '2022-12-11 20:00:00', '0.087', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 19:57:49.944', NULL,
        '2022-12-11 19:57:49.944', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601909313885556738, 1540542229427298305, '4173630010', '2022-12-11 20:00:00', '0.232', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 19:58:37.123', NULL,
        '2022-12-11 19:58:37.123', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601909352510902274, 1546097849947701249, '4173630013', '2022-12-11 20:00:00', '0.747', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 19:58:46.332', NULL,
        '2022-12-11 19:58:46.332', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601909410945945602, 1540542229423104001, '4173630006', '2022-12-11 20:00:00', '0.315', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 19:59:00.264', NULL,
        '2022-12-11 19:59:00.264', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601909520392114177, 1540542229414715394, '4173630012', '2022-12-11 20:00:00', '0.039', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 19:59:26.358', NULL,
        '2022-12-11 19:59:26.358', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601909586347544578, 1540542229431492610, '4173630017', '2022-12-11 20:00:00', '0.099', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 19:59:42.083', NULL,
        '2022-12-11 19:59:42.083', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601909661090041858, 1540542229330829313, '4173630015', '2022-12-11 20:00:00', '0.037', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 19:59:59.903', NULL,
        '2022-12-11 19:59:59.903', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601909697530155009, 1540542229423104002, '4173630001', '2022-12-11 20:00:00', '0.508', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 20:00:08.591', NULL,
        '2022-12-11 20:00:08.591', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601909708003332097, 1540542229322440706, '4173630019', '2022-12-11 20:00:00', '0.179', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 20:00:11.087', NULL,
        '2022-12-11 20:00:11.087', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601909810969300993, 1540542229406326787, '4173630008', '2022-12-11 20:00:00', '0.410', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 20:00:35.637', NULL,
        '2022-12-11 20:00:35.637', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601910114506887169, 1540542229397938178, '4173630007', '2022-12-11 20:05:00', '0.012', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 20:01:48.006', NULL,
        '2022-12-11 20:01:48.006', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601911128521822209, 1540542229397938178, '4173630007', '2022-12-11 20:10:00', '0.012', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 20:05:49.766', NULL,
        '2022-12-11 20:05:49.766', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601912630913126401, 1540542229397938178, '4173630007', '2022-12-11 20:15:00', '0.013', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 20:11:47.964', NULL,
        '2022-12-11 20:11:47.964', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601913645234245633, 1540542229397938178, '4173630007', '2022-12-11 20:20:00', '0.012', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 20:15:49.797', NULL,
        '2022-12-11 20:15:49.797', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601913979662880769, 1540542229418909697, '4173630016', '2022-12-11 20:20:00', '0.194', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 20:17:09.531', NULL,
        '2022-12-11 20:17:09.531', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601915147642327042, 1540542229397938178, '4173630007', '2022-12-11 20:25:00', '0.011', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 20:21:47.999', NULL,
        '2022-12-11 20:21:47.999', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601916161837617154, 1540542229397938178, '4173630007', '2022-12-11 20:30:00', '0.011', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 20:25:49.802', NULL,
        '2022-12-11 20:25:49.802', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601917664614797314, 1540542229397938178, '4173630007', '2022-12-11 20:35:00', '0.010', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 20:31:48.092', NULL,
        '2022-12-11 20:31:48.092', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601918678331936769, 1540542229397938178, '4173630007', '2022-12-11 20:40:00', '0.008', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 20:35:49.781', NULL,
        '2022-12-11 20:35:49.781', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601920180828098561, 1540542229397938178, '4173630007', '2022-12-11 20:45:00', '0.007', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 20:41:48.004', NULL,
        '2022-12-11 20:41:48.004', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601921195124051969, 1540542229397938178, '4173630007', '2022-12-11 20:50:00', '0.006', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 20:45:49.831', NULL,
        '2022-12-11 20:45:49.831', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601922697402109954, 1540542229397938178, '4173630007', '2022-12-11 20:55:00', '0.006', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 20:51:48.002', NULL,
        '2022-12-11 20:51:48.002', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601922808769269761, 1540542229410521090, '4173630002', '2022-12-11 20:55:00', '0.017', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 20:52:14.554', NULL,
        '2022-12-11 20:52:14.554', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601923639757361154, 1540542229372772353, '4173630003', '2022-12-11 21:00:00', '-0.216', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 20:55:32.677', NULL,
        '2022-12-11 20:55:32.677', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601923711639343106, 1540542229397938178, '4173630007', '2022-12-11 21:00:00', '0.005', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 20:55:49.815', NULL,
        '2022-12-11 20:55:49.815', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601923825669885954, 1540542229414715395, '4173630009', '2022-12-11 21:00:00', '0.780', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 20:56:17.002', NULL,
        '2022-12-11 20:56:17.002', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601923967995203585, 1540542229376966658, '4173630004', '2022-12-11 21:00:00', '0.060', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 20:56:50.935', NULL,
        '2022-12-11 20:56:50.935', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601923974387322881, 1540542229410521090, '4173630002', '2022-12-11 21:00:00', '0.016', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 20:56:52.459', NULL,
        '2022-12-11 20:56:52.459', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601923994645811201, 1540542229418909698, '4173630005', '2022-12-11 21:00:00', '0.457', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 20:56:57.289', NULL,
        '2022-12-11 20:56:57.289', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601924047498235905, 1540542229418909697, '4173630016', '2022-12-11 21:00:00', '0.195', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 20:57:09.89', NULL, '2022-12-11 20:57:09.89',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601924180373786626, 1540542229402132481, '4173630000', '2022-12-11 21:00:00', '-0.401', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 20:57:41.57', NULL, '2022-12-11 20:57:41.57',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601924215215869953, 1540542229347606530, '4173630014', '2022-12-11 21:00:00', '0.087', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 20:57:49.877', NULL,
        '2022-12-11 20:57:49.877', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601924412926971905, 1540542229427298305, '4173630010', '2022-12-11 21:00:00', '0.233', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 20:58:37.015', NULL,
        '2022-12-11 20:58:37.015', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601924452043051010, 1546097849947701249, '4173630013', '2022-12-11 21:00:00', '0.751', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 20:58:46.341', NULL,
        '2022-12-11 20:58:46.341', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601924510037692417, 1540542229423104001, '4173630006', '2022-12-11 21:00:00', '0.314', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 20:59:00.168', NULL,
        '2022-12-11 20:59:00.168', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601924619857154050, 1540542229414715394, '4173630012', '2022-12-11 21:00:00', '-0.061', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 20:59:26.351', NULL,
        '2022-12-11 20:59:26.351', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601924686739525634, 1540542229431492610, '4173630017', '2022-12-11 21:00:00', '0.086', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 20:59:42.297', NULL,
        '2022-12-11 20:59:42.297', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601924760773185537, 1540542229330829313, '4173630015', '2022-12-11 21:00:00', '0.041', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 20:59:59.948', NULL,
        '2022-12-11 20:59:59.948', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601924796932280321, 1540542229423104002, '4173630001', '2022-12-11 21:00:00', '0.510', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 21:00:08.569', NULL,
        '2022-12-11 21:00:08.569', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601924807124439041, 1540542229322440706, '4173630019', '2022-12-11 21:00:00', '0.179', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 21:00:10.999', NULL,
        '2022-12-11 21:00:10.999', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601924910237208578, 1540542229406326787, '4173630008', '2022-12-11 21:00:00', '0.407', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 21:00:35.583', NULL,
        '2022-12-11 21:00:35.583', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601925213858680834, 1540542229397938178, '4173630007', '2022-12-11 21:05:00', '0.004', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 21:01:47.972', NULL,
        '2022-12-11 21:01:47.972', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601926228607619073, 1540542229397938178, '4173630007', '2022-12-11 21:10:00', '0.003', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 21:05:49.907', NULL,
        '2022-12-11 21:05:49.907', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601927730306863106, 1540542229397938178, '4173630007', '2022-12-11 21:15:00', '0.001', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 21:11:47.94', NULL, '2022-12-11 21:11:47.94',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601928745018052609, 1540542229397938178, '4173630007', '2022-12-11 21:20:00', '0.001', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 21:15:49.865', NULL,
        '2022-12-11 21:15:49.865', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601930247270944770, 1540542229397938178, '4173630007', '2022-12-11 21:25:00', '0.000', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 21:21:48.031', NULL,
        '2022-12-11 21:21:48.031', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601936294844944386, 1540542229397938178, '4173630007', '2022-12-11 21:50:00', '-0.002', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 21:45:49.885', NULL,
        '2022-12-11 21:45:49.885', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601937796825206786, 1540542229397938178, '4173630007', '2022-12-11 21:55:00', '-0.002', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 21:51:47.985', NULL,
        '2022-12-11 21:51:47.985', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601938738786193410, 1540542229372772353, '4173630003', '2022-12-11 22:00:00', '-0.216', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 21:55:32.566', NULL,
        '2022-12-11 21:55:32.566', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601938811188269057, 1540542229397938178, '4173630007', '2022-12-11 22:00:00', '-0.002', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 21:55:49.828', NULL,
        '2022-12-11 21:55:49.828', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601938925290115074, 1540542229414715395, '4173630009', '2022-12-11 22:00:00', '0.783', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 21:56:17.032', NULL,
        '2022-12-11 21:56:17.032', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601938985721647106, 1540542229376966658, '4173630004', '2022-12-11 22:00:00', '0.056', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 21:56:31.44', NULL, '2022-12-11 21:56:31.44',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601939094144405506, 1540542229418909698, '4173630005', '2022-12-11 22:00:00', '0.452', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 21:56:57.29', NULL, '2022-12-11 21:56:57.29',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601939099542474753, 1540542229402132481, '4173630000', '2022-12-11 22:00:00', '-0.401', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 21:56:58.577', NULL,
        '2022-12-11 21:56:58.577', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601939146883584002, 1540542229418909697, '4173630016', '2022-12-11 22:00:00', '0.188', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 21:57:09.864', NULL,
        '2022-12-11 21:57:09.864', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601939167389536257, 1540542229410521090, '4173630002', '2022-12-11 22:00:00', '0.015', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 21:57:14.753', NULL,
        '2022-12-11 21:57:14.753', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601939314307616769, 1540542229347606530, '4173630014', '2022-12-11 22:00:00', '0.086', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 21:57:49.781', NULL,
        '2022-12-11 21:57:49.781', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601939512844996609, 1540542229427298305, '4173630010', '2022-12-11 22:00:00', '0.233', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 21:58:37.116', NULL,
        '2022-12-11 21:58:37.116', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601939551046717441, 1546097849947701249, '4173630013', '2022-12-11 22:00:00', '0.755', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 21:58:46.224', NULL,
        '2022-12-11 21:58:46.224', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601939609670504450, 1540542229423104001, '4173630006', '2022-12-11 22:00:00', '0.310', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 21:59:00.201', NULL,
        '2022-12-11 21:59:00.201', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601939718751768577, 1540542229414715394, '4173630012', '2022-12-11 22:00:00', '-0.060', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 21:59:26.208', NULL,
        '2022-12-11 21:59:26.208', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601939810208567298, 1540542229431492610, '4173630017', '2022-12-11 22:00:00', '0.109', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 21:59:48.013', NULL,
        '2022-12-11 21:59:48.013', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601939859864932354, 1540542229330829313, '4173630015', '2022-12-11 22:00:00', '0.033', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 21:59:59.852', NULL,
        '2022-12-11 21:59:59.852', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601939897261346818, 1540542229423104002, '4173630001', '2022-12-11 22:00:00', '0.511', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 22:00:08.767', NULL,
        '2022-12-11 22:00:08.767', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601939913401028609, 1540542229322440706, '4173630019', '2022-12-11 22:00:00', '0.179', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 22:00:12.616', NULL,
        '2022-12-11 22:00:12.616', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601940010411085826, 1540542229406326787, '4173630008', '2022-12-11 22:00:00', '0.406', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 22:00:35.745', NULL,
        '2022-12-11 22:00:35.745', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601940313290166274, 1540542229397938178, '4173630007', '2022-12-11 22:05:00', '-0.002', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 22:01:47.957', NULL,
        '2022-12-11 22:01:47.957', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601941328735358978, 1540542229397938178, '4173630007', '2022-12-11 22:10:00', '-0.002', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 22:05:50.058', NULL,
        '2022-12-11 22:05:50.058', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601942830212304898, 1540542229397938178, '4173630007', '2022-12-11 22:15:00', '-0.002', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 22:11:48.038', NULL,
        '2022-12-11 22:11:48.038', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601947863179972610, 1540542229397938178, '4173630007', '2022-12-11 22:35:00', '-0.002', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 22:31:47.99', NULL, '2022-12-11 22:31:47.99',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601948877530451969, 1540542229397938178, '4173630007', '2022-12-11 22:40:00', '-0.002', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 22:35:49.831', NULL,
        '2022-12-11 22:35:49.831', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601950379749789698, 1540542229397938178, '4173630007', '2022-12-11 22:45:00', '-0.002', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 22:41:47.988', NULL,
        '2022-12-11 22:41:47.988', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601951394981072898, 1540542229397938178, '4173630007', '2022-12-11 22:50:00', '-0.003', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 22:45:50.037', NULL,
        '2022-12-11 22:45:50.037', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601952896495767553, 1540542229397938178, '4173630007', '2022-12-11 22:55:00', '-0.003', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 22:51:48.027', NULL,
        '2022-12-11 22:51:48.027', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601953838926516226, 1540542229372772353, '4173630003', '2022-12-11 23:00:00', '-0.099', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 22:55:32.72', NULL, '2022-12-11 22:55:32.72',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601953910892384258, 1540542229397938178, '4173630007', '2022-12-11 23:00:00', '-0.003', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 22:55:49.878', NULL,
        '2022-12-11 22:55:49.878', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601953922502217729, 1540542229410521090, '4173630002', '2022-12-11 23:00:00', '0.014', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 22:55:52.646', NULL,
        '2022-12-11 22:55:52.646', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601954024839041025, 1540542229414715395, '4173630009', '2022-12-11 23:00:00', '0.783', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 22:56:17.045', NULL,
        '2022-12-11 22:56:17.045', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601954169446060034, 1540542229376966658, '4173630004', '2022-12-11 23:00:00', '0.052', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 22:56:51.522', NULL,
        '2022-12-11 22:56:51.522', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601954193965961218, 1540542229418909698, '4173630005', '2022-12-11 23:00:00', '0.456', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 22:56:57.368', NULL,
        '2022-12-11 22:56:57.368', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601954199250784257, 1540542229402132481, '4173630000', '2022-12-11 23:00:00', '-0.402', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 22:56:58.628', NULL,
        '2022-12-11 22:56:58.628', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601954247497863169, 1540542229418909697, '4173630016', '2022-12-11 23:00:00', '-0.190', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 22:57:10.131', NULL,
        '2022-12-11 22:57:10.131', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601954415240663041, 1540542229347606530, '4173630014', '2022-12-11 23:00:00', '0.086', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 22:57:50.124', NULL,
        '2022-12-11 22:57:50.124', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601954612343590914, 1540542229427298305, '4173630010', '2022-12-11 23:00:00', '0.234', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 22:58:37.117', NULL,
        '2022-12-11 22:58:37.117', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601954650817941505, 1546097849947701249, '4173630013', '2022-12-11 23:00:00', '0.752', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 22:58:46.29', NULL, '2022-12-11 22:58:46.29',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601954709525614594, 1540542229423104001, '4173630006', '2022-12-11 23:00:00', '0.310', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 22:59:00.287', NULL,
        '2022-12-11 22:59:00.287', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601954818996948993, 1540542229414715394, '4173630012', '2022-12-11 23:00:00', '-0.050', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 22:59:26.387', NULL,
        '2022-12-11 22:59:26.387', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601954886034509826, 1540542229431492610, '4173630017', '2022-12-11 23:00:00', '0.115', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 22:59:42.37', NULL, '2022-12-11 22:59:42.37',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601954997108068354, 1540542229423104002, '4173630001', '2022-12-11 23:00:00', '0.510', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 23:00:08.851', NULL,
        '2022-12-11 23:00:08.851', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601955006725607425, 1540542229322440706, '4173630019', '2022-12-11 23:00:00', '0.179', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 23:00:11.145', NULL,
        '2022-12-11 23:00:11.145', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601955109452500994, 1540542229406326787, '4173630008', '2022-12-11 23:00:00', '0.407', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 23:00:35.637', NULL,
        '2022-12-11 23:00:35.637', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601955413044613121, 1540542229397938178, '4173630007', '2022-12-11 23:05:00', '-0.002', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 23:01:48.019', NULL,
        '2022-12-11 23:01:48.019', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601956300471259138, 1540542229330829313, '4173630015', '2022-12-11 23:05:00', '0.036', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 23:05:19.598', NULL,
        '2022-12-11 23:05:19.598', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601956427470589954, 1540542229397938178, '4173630007', '2022-12-11 23:10:00', '-0.002', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 23:05:49.877', NULL,
        '2022-12-11 23:05:49.877', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601957929673150466, 1540542229397938178, '4173630007', '2022-12-11 23:15:00', '-0.003', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 23:11:48.029', NULL,
        '2022-12-11 23:11:48.029', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601958460059668482, 1540542229418909697, '4173630016', '2022-12-11 23:15:00', '0.190', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 23:13:54.484', NULL,
        '2022-12-11 23:13:54.484', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601958944199790593, 1540542229397938178, '4173630007', '2022-12-11 23:20:00', '-0.002', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 23:15:49.912', NULL,
        '2022-12-11 23:15:49.912', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601962963563565058, 1540542229397938178, '4173630007', '2022-12-11 23:35:00', '0.000', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 23:31:48.203', NULL,
        '2022-12-11 23:31:48.203', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601963977876295681, 1540542229397938178, '4173630007', '2022-12-11 23:40:00', '0.000', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 23:35:50.034', NULL,
        '2022-12-11 23:35:50.034', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601965479365824513, 1540542229397938178, '4173630007', '2022-12-11 23:45:00', '0.000', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 23:41:48.016', NULL,
        '2022-12-11 23:41:48.016', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601968085588889601, 1540542229418909697, '4173630016', '2022-12-11 23:55:00', '0.185', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 23:52:09.388', NULL,
        '2022-12-11 23:52:09.388', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601968819390763009, 1540542229330829313, '4173630015', '2022-12-11 23:55:00', '0.035', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 23:55:04.341', NULL,
        '2022-12-11 23:55:04.341', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601969010588110849, 1540542229397938178, '4173630007', '2022-12-12 00:00:00', '-0.002', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 23:55:49.926', NULL,
        '2022-12-11 23:55:49.926', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601969022059532289, 1540542229410521090, '4173630002', '2022-12-12 00:00:00', '0.011', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 23:55:52.661', NULL,
        '2022-12-11 23:55:52.661', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601969123914010625, 1540542229414715395, '4173630009', '2022-12-12 00:00:00', '0.794', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 23:56:16.945', NULL,
        '2022-12-11 23:56:16.945', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601969268848185346, 1540542229376966658, '4173630004', '2022-12-12 00:00:00', '0.050', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 23:56:51.5', NULL, '2022-12-11 23:56:51.5',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601969293716213762, 1540542229418909698, '4173630005', '2022-12-12 00:00:00', '0.438', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 23:56:57.429', NULL,
        '2022-12-11 23:56:57.429', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601969298808098817, 1540542229402132481, '4173630000', '2022-12-12 00:00:00', '-0.403', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 23:56:58.643', NULL,
        '2022-12-11 23:56:58.643', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601969514269495297, 1540542229347606530, '4173630014', '2022-12-12 00:00:00', '0.085', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 23:57:50.013', NULL,
        '2022-12-11 23:57:50.013', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601969711804436482, 1540542229427298305, '4173630010', '2022-12-12 00:00:00', '0.233', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 23:58:37.109', NULL,
        '2022-12-11 23:58:37.109', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601969750299758593, 1546097849947701249, '4173630013', '2022-12-12 00:00:00', '0.742', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 23:58:46.287', NULL,
        '2022-12-11 23:58:46.287', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601969808982265858, 1540542229423104001, '4173630006', '2022-12-12 00:00:00', '0.305', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 23:59:00.278', NULL,
        '2022-12-11 23:59:00.278', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601969918239690753, 1540542229414715394, '4173630012', '2022-12-12 00:00:00', '-0.055', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-11 23:59:26.327', NULL,
        '2022-12-11 23:59:26.327', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601970096883486722, 1540542229423104002, '4173630001', '2022-12-12 00:00:00', '0.505', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 00:00:08.919', NULL,
        '2022-12-12 00:00:08.919', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601970106660409346, 1540542229322440706, '4173630019', '2022-12-12 00:00:00', '0.179', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 00:00:11.25', NULL, '2022-12-12 00:00:11.25',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601970165598769153, 1540542229330829313, '4173630015', '2022-12-12 00:00:00', '0.034', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 00:00:25.302', NULL,
        '2022-12-12 00:00:25.302', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601970209513132033, 1540542229406326787, '4173630008', '2022-12-12 00:00:00', '0.405', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 00:00:35.772', NULL,
        '2022-12-12 00:00:35.772', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601970512635482113, 1540542229397938178, '4173630007', '2022-12-12 00:05:00', '-0.002', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 00:01:48.042', NULL,
        '2022-12-12 00:01:48.042', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601971527090819074, 1540542229397938178, '4173630007', '2022-12-12 00:10:00', '-0.002', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 00:05:49.907', NULL,
        '2022-12-12 00:05:49.907', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601971860898697218, 1540542229418909697, '4173630016', '2022-12-12 00:10:00', '0.185', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 00:07:09.493', NULL,
        '2022-12-12 00:07:09.493', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601973029238853634, 1540542229397938178, '4173630007', '2022-12-12 00:15:00', '-0.002', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 00:11:48.047', NULL,
        '2022-12-12 00:11:48.047', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601974043744522242, 1540542229397938178, '4173630007', '2022-12-12 00:20:00', '-0.002', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 00:15:49.924', NULL,
        '2022-12-12 00:15:49.924', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601975582051651585, 1540542229397938178, '4173630007', '2022-12-12 00:25:00', '-0.002', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 00:21:56.685', NULL,
        '2022-12-12 00:21:56.685', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601976560373059586, 1540542229397938178, '4173630007', '2022-12-12 00:30:00', '-0.002', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 00:25:49.935', NULL,
        '2022-12-12 00:25:49.935', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601978062365904897, 1540542229397938178, '4173630007', '2022-12-12 00:35:00', '-0.002', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 00:31:48.038', NULL,
        '2022-12-12 00:31:48.038', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601979076863184898, 1540542229397938178, '4173630007', '2022-12-12 00:40:00', '-0.002', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 00:35:49.913', NULL,
        '2022-12-12 00:35:49.913', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601980597319356417, 1540542229397938178, '4173630007', '2022-12-12 00:45:00', '-0.002', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 00:41:52.418', NULL,
        '2022-12-12 00:41:52.418', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601981593583996929, 1540542229397938178, '4173630007', '2022-12-12 00:50:00', '-0.002', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 00:45:49.946', NULL,
        '2022-12-12 00:45:49.946', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601983096059187201, 1540542229397938178, '4173630007', '2022-12-12 00:55:00', '-0.002', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 00:51:48.164', NULL,
        '2022-12-12 00:51:48.164', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601984046773686273, 1540542229372772353, '4173630003', '2022-12-12 01:00:00', '-0.095', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 00:55:34.832', NULL,
        '2022-12-12 00:55:34.832', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601984282648760322, 1540542229397938178, '4173630007', '2022-12-12 01:00:00', '0.001', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 00:56:31.069', NULL,
        '2022-12-12 00:56:31.069', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601984299824435202, 1540542229410521090, '4173630002', '2022-12-12 01:00:00', '0.003', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 00:56:35.164', NULL,
        '2022-12-12 00:56:35.164', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601984414446374913, 1540542229414715395, '4173630009', '2022-12-12 01:00:00', '0.780', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 00:57:02.491', NULL,
        '2022-12-12 00:57:02.491', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601984433945694210, 1540542229376966658, '4173630004', '2022-12-12 01:00:00', '0.050', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 00:57:07.141', NULL,
        '2022-12-12 00:57:07.141', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601984439796748289, 1540542229418909698, '4173630005', '2022-12-12 01:00:00', '0.423', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 00:57:08.536', NULL,
        '2022-12-12 00:57:08.536', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601984448772558849, 1540542229418909697, '4173630016', '2022-12-12 01:00:00', '0.188', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 00:57:10.676', NULL,
        '2022-12-12 00:57:10.676', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601984613386407938, 1540542229347606530, '4173630014', '2022-12-12 01:00:00', '0.085', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 00:57:49.922', NULL,
        '2022-12-12 00:57:49.922', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601984749894225922, 1540542229402132481, '4173630000', '2022-12-12 01:00:00', '-0.403', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 00:58:22.469', NULL,
        '2022-12-12 00:58:22.469', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601984812083171330, 1540542229427298305, '4173630010', '2022-12-12 01:00:00', '0.232', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 00:58:37.295', NULL,
        '2022-12-12 00:58:37.295', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601984850154868738, 1546097849947701249, '4173630013', '2022-12-12 01:00:00', '0.740', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 00:58:46.373', NULL,
        '2022-12-12 00:58:46.373', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601984908543774722, 1540542229423104001, '4173630006', '2022-12-12 01:00:00', '0.303', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 00:59:00.294', NULL,
        '2022-12-12 00:59:00.294', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601985018430345218, 1540542229414715394, '4173630012', '2022-12-12 01:00:00', '-0.050', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 00:59:26.493', NULL,
        '2022-12-12 00:59:26.493', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601985196629544962, 1540542229423104002, '4173630001', '2022-12-12 01:00:00', '0.505', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 01:00:08.979', NULL,
        '2022-12-12 01:00:08.979', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601985206872035329, 1540542229322440706, '4173630019', '2022-12-12 01:00:00', '0.179', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 01:00:11.421', NULL,
        '2022-12-12 01:00:11.421', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601985243911933953, 1540542229330829313, '4173630015', '2022-12-12 01:00:00', '0.031', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 01:00:20.252', NULL,
        '2022-12-12 01:00:20.252', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601985309758312450, 1540542229406326787, '4173630008', '2022-12-12 01:00:00', '0.400', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 01:00:35.951', NULL,
        '2022-12-12 01:00:35.951', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601986642796204033, 1540542229397938178, '4173630007', '2022-12-12 01:10:00', '-0.002', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 01:05:53.772', NULL,
        '2022-12-12 01:05:53.772', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601988134680776706, 1540542229397938178, '4173630007', '2022-12-12 01:15:00', '-0.002', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 01:11:49.465', NULL,
        '2022-12-12 01:11:49.465', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601988224124309505, 1540542229418909697, '4173630016', '2022-12-12 01:15:00', '0.200', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 01:12:10.79', NULL, '2022-12-12 01:12:10.79',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601989143280865281, 1540542229397938178, '4173630007', '2022-12-12 01:20:00', '-0.002', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 01:15:49.933', NULL,
        '2022-12-12 01:15:49.933', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601990645705723906, 1540542229397938178, '4173630007', '2022-12-12 01:25:00', '-0.003', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 01:21:48.14', NULL, '2022-12-12 01:21:48.14',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601991659871653889, 1540542229397938178, '4173630007', '2022-12-12 01:30:00', '-0.003', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 01:25:49.936', NULL,
        '2022-12-12 01:25:49.936', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601993162288123905, 1540542229397938178, '4173630007', '2022-12-12 01:35:00', '-0.002', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 01:31:48.14', NULL, '2022-12-12 01:31:48.14',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601995678765666305, 1540542229397938178, '4173630007', '2022-12-12 01:45:00', '0.000', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 01:41:48.115', NULL,
        '2022-12-12 01:41:48.115', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601996693275529218, 1540542229397938178, '4173630007', '2022-12-12 01:50:00', '0.000', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 01:45:49.993', NULL,
        '2022-12-12 01:45:49.993', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601998195536809985, 1540542229397938178, '4173630007', '2022-12-12 01:55:00', '0.001', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 01:51:48.16', NULL, '2022-12-12 01:51:48.16',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601999137208389634, 1540542229372772353, '4173630003', '2022-12-12 02:00:00', '-0.092', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 01:55:32.672', NULL,
        '2022-12-12 01:55:32.672', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601999209765654530, 1540542229397938178, '4173630007', '2022-12-12 02:00:00', '0.001', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 01:55:49.97', NULL, '2022-12-12 01:55:49.97',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601999221786529793, 1540542229410521090, '4173630002', '2022-12-12 02:00:00', '-0.003', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 01:55:52.837', NULL,
        '2022-12-12 01:55:52.837', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601999323464847362, 1540542229414715395, '4173630009', '2022-12-12 02:00:00', '0.779', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 01:56:17.079', NULL,
        '2022-12-12 01:56:17.079', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601999383036547074, 1540542229376966658, '4173630004', '2022-12-12 02:00:00', '0.051', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 01:56:31.282', NULL,
        '2022-12-12 01:56:31.282', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601999493430628354, 1540542229418909698, '4173630005', '2022-12-12 02:00:00', '0.446', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 01:56:57.602', NULL,
        '2022-12-12 01:56:57.602', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601999498346352642, 1540542229402132481, '4173630000', '2022-12-12 02:00:00', '-0.403', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 01:56:58.774', NULL,
        '2022-12-12 01:56:58.774', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601999712947916801, 1540542229347606530, '4173630014', '2022-12-12 02:00:00', '0.086', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 01:57:49.939', NULL,
        '2022-12-12 01:57:49.939', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601999911221055490, 1540542229427298305, '4173630010', '2022-12-12 02:00:00', '0.231', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 01:58:37.211', NULL,
        '2022-12-12 01:58:37.211', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1601999949762514946, 1546097849947701249, '4173630013', '2022-12-12 02:00:00', '0.744', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 01:58:46.4', NULL, '2022-12-12 01:58:46.4',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602000008596017153, 1540542229423104001, '4173630006', '2022-12-12 02:00:00', '0.302', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 01:59:00.427', NULL,
        '2022-12-12 01:59:00.427', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602000118063157250, 1540542229414715394, '4173630012', '2022-12-12 02:00:00', '0.048', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 01:59:26.526', NULL,
        '2022-12-12 01:59:26.526', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602000296207831041, 1540542229423104002, '4173630001', '2022-12-12 02:00:00', '0.516', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 02:00:08.999', NULL,
        '2022-12-12 02:00:08.999', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602000306538401794, 1540542229322440706, '4173630019', '2022-12-12 02:00:00', '0.179', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 02:00:11.462', NULL,
        '2022-12-12 02:00:11.462', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602000344308109313, 1540542229330829313, '4173630015', '2022-12-12 02:00:00', '0.038', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 02:00:20.467', NULL,
        '2022-12-12 02:00:20.467', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602000408854253569, 1540542229406326787, '4173630008', '2022-12-12 02:00:00', '0.407', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 02:00:35.856', NULL,
        '2022-12-12 02:00:35.856', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602000712056295426, 1540542229397938178, '4173630007', '2022-12-12 02:05:00', '0.000', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 02:01:48.145', NULL,
        '2022-12-12 02:01:48.145', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602002595202322433, 1540542229410521090, '4173630002', '2022-12-12 02:09:16.897', NULL, NULL, 'NORMAL',
        'OFFLINE', 'PZ', 'KEEP', 'OFFLINE', NULL, NULL, '2022-12-12 02:09:17.122', NULL, '2022-12-12 02:09:17.122',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602003318535213057, 1540542229418909697, '4173630016', '2022-12-12 02:15:00', '0.190', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 02:12:09.578', NULL,
        '2022-12-12 02:12:09.578', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602004243106615297, 1540542229397938178, '4173630007', '2022-12-12 02:20:00', '-0.002', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 02:15:50.013', NULL,
        '2022-12-12 02:15:50.013', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602005745288204290, 1540542229397938178, '4173630007', '2022-12-12 02:25:00', '-0.002', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 02:21:48.161', NULL,
        '2022-12-12 02:21:48.161', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602006759546408962, 1540542229397938178, '4173630007', '2022-12-12 02:30:00', '-0.003', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 02:25:49.979', NULL,
        '2022-12-12 02:25:49.979', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602008261866409986, 1540542229397938178, '4173630007', '2022-12-12 02:35:00', '-0.003', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 02:31:48.16', NULL, '2022-12-12 02:31:48.16',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602009276216889345, 1540542229397938178, '4173630007', '2022-12-12 02:40:00', '-0.003', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 02:35:50', NULL, '2022-12-12 02:35:50', NULL,
        0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602010778293620738, 1540542229397938178, '4173630007', '2022-12-12 02:45:00', '-0.003', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 02:41:48.123', NULL,
        '2022-12-12 02:41:48.123', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602011792761540610, 1540542229397938178, '4173630007', '2022-12-12 02:50:00', '-0.003', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 02:45:49.991', NULL,
        '2022-12-12 02:45:49.991', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602012126946906114, 1540542229418909697, '4173630016', '2022-12-12 02:50:00', '0.198', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 02:47:09.667', NULL,
        '2022-12-12 02:47:09.667', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602013294934740994, 1540542229397938178, '4173630007', '2022-12-12 02:55:00', '-0.003', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 02:51:48.137', NULL,
        '2022-12-12 02:51:48.137', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602014236602126337, 1540542229372772353, '4173630003', '2022-12-12 03:00:00', '-0.202', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 02:55:32.647', NULL,
        '2022-12-12 02:55:32.647', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602014309406855169, 1540542229397938178, '4173630007', '2022-12-12 03:00:00', '-0.002', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 02:55:50.006', NULL,
        '2022-12-12 02:55:50.006', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602014422917304321, 1540542229414715395, '4173630009', '2022-12-12 03:00:00', '0.775', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 02:56:17.069', NULL,
        '2022-12-12 02:56:17.069', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602014568023445505, 1540542229376966658, '4173630004', '2022-12-12 03:00:00', '0.052', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 02:56:51.665', NULL,
        '2022-12-12 02:56:51.665', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602014592824365058, 1540542229418909698, '4173630005', '2022-12-12 03:00:00', '0.452', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 02:56:57.578', NULL,
        '2022-12-12 02:56:57.578', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602014597865918466, 1540542229402132481, '4173630000', '2022-12-12 03:00:00', '-0.403', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 02:56:58.78', NULL, '2022-12-12 02:56:58.78',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602014646037499905, 1540542229418909697, '4173630016', '2022-12-12 03:00:00', '0.198', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 02:57:10.265', NULL,
        '2022-12-12 02:57:10.265', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602014812765278210, 1540542229347606530, '4173630014', '2022-12-12 03:00:00', '0.086', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 02:57:50.016', NULL,
        '2022-12-12 02:57:50.016', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602015011193606145, 1540542229427298305, '4173630010', '2022-12-12 03:00:00', '0.230', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 02:58:37.325', NULL,
        '2022-12-12 02:58:37.325', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602015049512767489, 1546097849947701249, '4173630013', '2022-12-12 03:00:00', '0.745', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 02:58:46.461', NULL,
        '2022-12-12 02:58:46.461', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602015108266577922, 1540542229423104001, '4173630006', '2022-12-12 03:00:00', '0.303', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 02:59:00.468', NULL,
        '2022-12-12 02:59:00.468', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602015217779855361, 1540542229414715394, '4173630012', '2022-12-12 03:00:00', '-0.043', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 02:59:26.579', NULL,
        '2022-12-12 02:59:26.579', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602015395836448769, 1540542229423104002, '4173630001', '2022-12-12 03:00:00', '0.510', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 03:00:09.031', NULL,
        '2022-12-12 03:00:09.031', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602015405630148610, 1540542229322440706, '4173630019', '2022-12-12 03:00:00', '0.179', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 03:00:11.366', NULL,
        '2022-12-12 03:00:11.366', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602015443534073857, 1540542229330829313, '4173630015', '2022-12-12 03:00:00', '0.031', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 03:00:20.403', NULL,
        '2022-12-12 03:00:20.403', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602015508264767489, 1540542229406326787, '4173630008', '2022-12-12 03:00:00', '0.386', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 03:00:35.836', NULL,
        '2022-12-12 03:00:35.836', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602015811491975169, 1540542229397938178, '4173630007', '2022-12-12 03:05:00', '-0.003', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 03:01:48.131', NULL,
        '2022-12-12 03:01:48.131', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602016826190581762, 1540542229397938178, '4173630007', '2022-12-12 03:10:00', '-0.003', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 03:05:50.054', NULL,
        '2022-12-12 03:05:50.054', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602018328154066946, 1540542229397938178, '4173630007', '2022-12-12 03:15:00', '-0.003', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 03:11:48.15', NULL, '2022-12-12 03:11:48.15',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602019342592626690, 1540542229397938178, '4173630007', '2022-12-12 03:20:00', '-0.004', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 03:15:50.011', NULL,
        '2022-12-12 03:15:50.011', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602020844950376450, 1540542229397938178, '4173630007', '2022-12-12 03:25:00', '-0.004', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 03:21:48.201', NULL,
        '2022-12-12 03:21:48.201', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602021859183415298, 1540542229397938178, '4173630007', '2022-12-12 03:30:00', '-0.004', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 03:25:50.013', NULL,
        '2022-12-12 03:25:50.013', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602023361465667585, 1540542229397938178, '4173630007', '2022-12-12 03:35:00', '-0.003', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 03:31:48.185', NULL,
        '2022-12-12 03:31:48.185', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602024375828729857, 1540542229397938178, '4173630007', '2022-12-12 03:40:00', '-0.002', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 03:35:50.028', NULL,
        '2022-12-12 03:35:50.028', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602025878039678977, 1540542229397938178, '4173630007', '2022-12-12 03:45:00', '-0.002', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 03:41:48.183', NULL,
        '2022-12-12 03:41:48.183', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602026892411129857, 1540542229397938178, '4173630007', '2022-12-12 03:50:00', '-0.002', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 03:45:50.028', NULL,
        '2022-12-12 03:45:50.028', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602028394647244801, 1540542229397938178, '4173630007', '2022-12-12 03:55:00', '-0.002', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 03:51:48.189', NULL,
        '2022-12-12 03:51:48.189', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602029336184606722, 1540542229372772353, '4173630003', '2022-12-12 04:00:00', '-0.204', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 03:55:32.669', NULL,
        '2022-12-12 03:55:32.669', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602029409148719105, 1540542229397938178, '4173630007', '2022-12-12 04:00:00', '-0.002', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 03:55:50.065', NULL,
        '2022-12-12 03:55:50.065', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602029522529144833, 1540542229414715395, '4173630009', '2022-12-12 04:00:00', '0.775', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 03:56:17.097', NULL,
        '2022-12-12 03:56:17.097', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602029667538817025, 1540542229376966658, '4173630004', '2022-12-12 04:00:00', '0.052', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 03:56:51.67', NULL, '2022-12-12 03:56:51.67',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602029692574617602, 1540542229418909698, '4173630005', '2022-12-12 04:00:00', '0.456', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 03:56:57.639', NULL,
        '2022-12-12 03:56:57.639', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602029701407821825, 1540542229402132481, '4173630000', '2022-12-12 04:00:00', '-0.404', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 03:56:59.745', NULL,
        '2022-12-12 03:56:59.745', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602029744621735938, 1540542229418909697, '4173630016', '2022-12-12 04:00:00', '0.198', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 03:57:10.048', NULL,
        '2022-12-12 03:57:10.048', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602029912184180738, 1540542229347606530, '4173630014', '2022-12-12 04:00:00', '0.086', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 03:57:49.998', NULL,
        '2022-12-12 03:57:49.998', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602030111375872001, 1540542229427298305, '4173630010', '2022-12-12 04:00:00', '0.229', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 03:58:37.489', NULL,
        '2022-12-12 03:58:37.489', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602030149476929537, 1546097849947701249, '4173630013', '2022-12-12 04:00:00', '0.744', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 03:58:46.573', NULL,
        '2022-12-12 03:58:46.573', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602030207970693122, 1540542229423104001, '4173630006', '2022-12-12 04:00:00', '0.303', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 03:59:00.519', NULL,
        '2022-12-12 03:59:00.519', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602030317727240194, 1540542229414715394, '4173630012', '2022-12-12 04:00:00', '-0.025', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 03:59:26.686', NULL,
        '2022-12-12 03:59:26.686', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602030495955800065, 1540542229423104002, '4173630001', '2022-12-12 04:00:00', '0.486', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 04:00:09.18', NULL, '2022-12-12 04:00:09.18',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602030506089238529, 1540542229322440706, '4173630019', '2022-12-12 04:00:00', '0.179', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 04:00:11.596', NULL,
        '2022-12-12 04:00:11.596', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602030543854751746, 1540542229330829313, '4173630015', '2022-12-12 04:00:00', '0.032', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 04:00:20.6', NULL, '2022-12-12 04:00:20.6',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602030608220540929, 1540542229406326787, '4173630008', '2022-12-12 04:00:00', '0.401', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 04:00:35.946', NULL,
        '2022-12-12 04:00:35.946', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602030911250616321, 1540542229397938178, '4173630007', '2022-12-12 04:05:00', '-0.003', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 04:01:48.194', NULL,
        '2022-12-12 04:01:48.194', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602031925622067201, 1540542229397938178, '4173630007', '2022-12-12 04:10:00', '-0.004', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 04:05:50.039', NULL,
        '2022-12-12 04:05:50.039', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602033427908513794, 1540542229397938178, '4173630007', '2022-12-12 04:15:00', '-0.004', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 04:11:48.212', NULL,
        '2022-12-12 04:11:48.212', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602034442347073538, 1540542229397938178, '4173630007', '2022-12-12 04:20:00', '-0.004', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 04:15:50.073', NULL,
        '2022-12-12 04:15:50.073', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602035416369319938, 1540542229418909698, '4173630005', '2022-12-12 04:20:00', '0.454', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 04:19:42.297', NULL,
        '2022-12-12 04:19:42.297', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602035944503496706, 1540542229397938178, '4173630007', '2022-12-12 04:25:00', '-0.004', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 04:21:48.215', NULL,
        '2022-12-12 04:21:48.215', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602036958753312769, 1540542229397938178, '4173630007', '2022-12-12 04:30:00', '-0.004', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 04:25:50.031', NULL,
        '2022-12-12 04:25:50.031', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602038461568241665, 1540542229397938178, '4173630007', '2022-12-12 04:35:00', '-0.005', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 04:31:48.33', NULL, '2022-12-12 04:31:48.33',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602039475499290625, 1540542229397938178, '4173630007', '2022-12-12 04:40:00', '-0.005', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 04:35:50.07', NULL, '2022-12-12 04:35:50.07',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602040104888160257, 1540542229418909698, '4173630005', '2022-12-12 04:40:00', '0.440', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 04:38:20.128', NULL,
        '2022-12-12 04:38:20.128', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602040977794125825, 1540542229397938178, '4173630007', '2022-12-12 04:45:00', '-0.006', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 04:41:48.245', NULL,
        '2022-12-12 04:41:48.245', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602041992106856450, 1540542229397938178, '4173630007', '2022-12-12 04:50:00', '-0.006', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 04:45:50.076', NULL,
        '2022-12-12 04:45:50.076', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602043494506549250, 1540542229397938178, '4173630007', '2022-12-12 04:55:00', '-0.006', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 04:51:48.276', NULL,
        '2022-12-12 04:51:48.276', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602044508798308354, 1540542229397938178, '4173630007', '2022-12-12 05:00:00', '-0.006', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 04:55:50.102', NULL,
        '2022-12-12 04:55:50.102', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602044622241648641, 1540542229414715395, '4173630009', '2022-12-12 05:00:00', '0.776', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 04:56:17.149', NULL,
        '2022-12-12 04:56:17.149', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602044767150657537, 1540542229376966658, '4173630004', '2022-12-12 05:00:00', '0.054', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 04:56:51.697', NULL,
        '2022-12-12 04:56:51.697', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602044796850524161, 1540542229402132481, '4173630000', '2022-12-12 05:00:00', '-0.405', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 04:56:58.779', NULL,
        '2022-12-12 04:56:58.779', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602044844229382145, 1540542229418909697, '4173630016', '2022-12-12 05:00:00', '0.192', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 04:57:10.075', NULL,
        '2022-12-12 04:57:10.075', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602045011917656065, 1540542229347606530, '4173630014', '2022-12-12 05:00:00', '0.086', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 04:57:50.055', NULL,
        '2022-12-12 04:57:50.055', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602045139986534401, 1540542229418909698, '4173630005', '2022-12-12 05:00:00', '0.428', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 04:58:20.589', NULL,
        '2022-12-12 04:58:20.589', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602045210383732737, 1540542229427298305, '4173630010', '2022-12-12 05:00:00', '0.229', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 04:58:37.372', NULL,
        '2022-12-12 04:58:37.372', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602045248841306114, 1546097849947701249, '4173630013', '2022-12-12 05:00:00', '0.743', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 04:58:46.542', NULL,
        '2022-12-12 04:58:46.542', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602045307557367809, 1540542229423104001, '4173630006', '2022-12-12 05:00:00', '0.305', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 04:59:00.541', NULL,
        '2022-12-12 04:59:00.541', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602045417590738946, 1540542229414715394, '4173630012', '2022-12-12 05:00:00', '-0.061', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 04:59:26.775', NULL,
        '2022-12-12 04:59:26.775', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602045595840270338, 1540542229423104002, '4173630001', '2022-12-12 05:00:00', '0.522', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 05:00:09.273', NULL,
        '2022-12-12 05:00:09.273', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602045605252288513, 1540542229322440706, '4173630019', '2022-12-12 05:00:00', '0.179', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 05:00:11.517', NULL,
        '2022-12-12 05:00:11.517', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602045643135242241, 1540542229330829313, '4173630015', '2022-12-12 05:00:00', '0.038', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 05:00:20.549', NULL,
        '2022-12-12 05:00:20.549', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602045708004347906, 1540542229406326787, '4173630008', '2022-12-12 05:00:00', '0.399', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 05:00:36.014', NULL,
        '2022-12-12 05:00:36.014', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602046010900205570, 1540542229397938178, '4173630007', '2022-12-12 05:05:00', '-0.007', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 05:01:48.231', NULL,
        '2022-12-12 05:01:48.231', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602047025301016578, 1540542229397938178, '4173630007', '2022-12-12 05:10:00', '-0.007', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 05:05:50.083', NULL,
        '2022-12-12 05:05:50.083', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602048527692320770, 1540542229397938178, '4173630007', '2022-12-12 05:15:00', '-0.007', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 05:11:48.281', NULL,
        '2022-12-12 05:11:48.281', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602049542109908993, 1540542229397938178, '4173630007', '2022-12-12 05:20:00', '-0.007', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 05:15:50.137', NULL,
        '2022-12-12 05:15:50.137', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602049875007623170, 1540542229418909697, '4173630016', '2022-12-12 05:20:00', '0.197', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 05:17:09.506', NULL,
        '2022-12-12 05:17:09.506', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602051044383772673, 1540542229397938178, '4173630007', '2022-12-12 05:25:00', '-0.008', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 05:21:48.307', NULL,
        '2022-12-12 05:21:48.307', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602052058654560258, 1540542229397938178, '4173630007', '2022-12-12 05:30:00', '-0.008', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 05:25:50.128', NULL,
        '2022-12-12 05:25:50.128', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602053560722903041, 1540542229397938178, '4173630007', '2022-12-12 05:35:00', '-0.008', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 05:31:48.249', NULL,
        '2022-12-12 05:31:48.249', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602054575215988737, 1540542229397938178, '4173630007', '2022-12-12 05:40:00', '-0.009', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 05:35:50.123', NULL,
        '2022-12-12 05:35:50.123', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602056077410160642, 1540542229397938178, '4173630007', '2022-12-12 05:45:00', '-0.008', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 05:41:48.274', NULL,
        '2022-12-12 05:41:48.274', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602057091823554562, 1540542229397938178, '4173630007', '2022-12-12 05:50:00', '-0.008', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 05:45:50.129', NULL,
        '2022-12-12 05:45:50.129', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602058593967394818, 1540542229397938178, '4173630007', '2022-12-12 05:55:00', '-0.008', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 05:51:48.268', NULL,
        '2022-12-12 05:51:48.268', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602059534904971265, 1540542229372772353, '4173630003', '2022-12-12 06:00:00', '-0.206', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 05:55:32.605', NULL,
        '2022-12-12 05:55:32.605', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602059608674390018, 1540542229397938178, '4173630007', '2022-12-12 06:00:00', '-0.008', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 05:55:50.193', NULL,
        '2022-12-12 05:55:50.193', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602059721585053698, 1540542229414715395, '4173630009', '2022-12-12 06:00:00', '0.802', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 05:56:17.113', NULL,
        '2022-12-12 05:56:17.113', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602059866817024001, 1540542229376966658, '4173630004', '2022-12-12 06:00:00', '0.058', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 05:56:51.739', NULL,
        '2022-12-12 05:56:51.739', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602059892091899905, 1540542229418909698, '4173630005', '2022-12-12 06:00:00', '0.450', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 05:56:57.765', NULL,
        '2022-12-12 05:56:57.765', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602059900920909826, 1540542229402132481, '4173630000', '2022-12-12 06:00:00', '-0.405', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 05:56:59.87', NULL, '2022-12-12 05:56:59.87',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602059943828639745, 1540542229418909697, '4173630016', '2022-12-12 06:00:00', '0.201', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 05:57:10.1', NULL, '2022-12-12 05:57:10.1',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602060111571439618, 1540542229347606530, '4173630014', '2022-12-12 06:00:00', '0.085', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 05:57:50.092', NULL,
        '2022-12-12 05:57:50.092', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602060310222065666, 1540542229427298305, '4173630010', '2022-12-12 06:00:00', '0.228', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 05:58:37.455', NULL,
        '2022-12-12 05:58:37.455', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602060348448952322, 1546097849947701249, '4173630013', '2022-12-12 06:00:00', '0.749', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 05:58:46.569', NULL,
        '2022-12-12 05:58:46.569', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602060407651553282, 1540542229423104001, '4173630006', '2022-12-12 06:00:00', '0.305', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 05:59:00.684', NULL,
        '2022-12-12 05:59:00.684', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602060517353574402, 1540542229414715394, '4173630012', '2022-12-12 06:00:00', '0.034', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 05:59:26.839', NULL,
        '2022-12-12 05:59:26.839', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602060695565357057, 1540542229423104002, '4173630001', '2022-12-12 06:00:00', '0.534', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 06:00:09.328', NULL,
        '2022-12-12 06:00:09.328', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602060705585549313, 1540542229322440706, '4173630019', '2022-12-12 06:00:00', '0.179', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 06:00:11.717', NULL,
        '2022-12-12 06:00:11.717', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602060742927437826, 1540542229330829313, '4173630015', '2022-12-12 06:00:00', '0.033', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 06:00:20.62', NULL, '2022-12-12 06:00:20.62',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602060807670714370, 1540542229406326787, '4173630008', '2022-12-12 06:00:00', '0.398', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 06:00:36.056', NULL,
        '2022-12-12 06:00:36.056', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602061110390411265, 1540542229397938178, '4173630007', '2022-12-12 06:05:00', '-0.008', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 06:01:48.23', NULL, '2022-12-12 06:01:48.23',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602062124975771650, 1540542229397938178, '4173630007', '2022-12-12 06:10:00', '-0.009', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 06:05:50.126', NULL,
        '2022-12-12 06:05:50.126', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602063627069280258, 1540542229397938178, '4173630007', '2022-12-12 06:15:00', '-0.009', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 06:11:48.253', NULL,
        '2022-12-12 06:11:48.253', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602064641834995714, 1540542229397938178, '4173630007', '2022-12-12 06:20:00', '-0.009', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 06:15:50.192', NULL,
        '2022-12-12 06:15:50.192', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602066144406654977, 1540542229397938178, '4173630007', '2022-12-12 06:25:00', '-0.009', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 06:21:48.433', NULL,
        '2022-12-12 06:21:48.433', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602067158169931777, 1540542229397938178, '4173630007', '2022-12-12 06:30:00', '-0.010', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 06:25:50.133', NULL,
        '2022-12-12 06:25:50.133', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602068660317966337, 1540542229397938178, '4173630007', '2022-12-12 06:35:00', '-0.010', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 06:31:48.273', NULL,
        '2022-12-12 06:31:48.273', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602069675582803970, 1540542229397938178, '4173630007', '2022-12-12 06:40:00', '-0.010', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 06:35:50.331', NULL,
        '2022-12-12 06:35:50.331', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602071177231716354, 1540542229397938178, '4173630007', '2022-12-12 06:45:00', '-0.010', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 06:41:48.352', NULL,
        '2022-12-12 06:41:48.352', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602072191619944449, 1540542229397938178, '4173630007', '2022-12-12 06:50:00', '-0.010', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 06:45:50.201', NULL,
        '2022-12-12 06:45:50.201', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602073693566652417, 1540542229397938178, '4173630007', '2022-12-12 06:55:00', '-0.010', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 06:51:48.293', NULL,
        '2022-12-12 06:51:48.293', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602074634659418114, 1540542229372772353, '4173630003', '2022-12-12 07:00:00', '-0.207', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 06:55:32.667', NULL,
        '2022-12-12 06:55:32.667', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602074708252676098, 1540542229397938178, '4173630007', '2022-12-12 07:00:00', '-0.011', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 06:55:50.213', NULL,
        '2022-12-12 06:55:50.213', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602074821280780290, 1540542229414715395, '4173630009', '2022-12-12 07:00:00', '0.785', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 06:56:17.161', NULL,
        '2022-12-12 06:56:17.161', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602074966328201218, 1540542229376966658, '4173630004', '2022-12-12 07:00:00', '0.062', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 06:56:51.743', NULL,
        '2022-12-12 06:56:51.743', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602074992152530946, 1540542229418909698, '4173630005', '2022-12-12 07:00:00', '0.454', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 06:56:57.899', NULL,
        '2022-12-12 06:56:57.899', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602075001090592769, 1540542229402132481, '4173630000', '2022-12-12 07:00:00', '-0.406', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 06:57:00.031', NULL,
        '2022-12-12 06:57:00.031', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602075041947308034, 1540542229418909697, '4173630016', '2022-12-12 07:00:00', '0.203', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 06:57:09.772', NULL,
        '2022-12-12 06:57:09.772', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602075211309109249, 1540542229347606530, '4173630014', '2022-12-12 07:00:00', '0.085', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 06:57:50.151', NULL,
        '2022-12-12 06:57:50.151', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602075409888432129, 1540542229427298305, '4173630010', '2022-12-12 07:00:00', '0.226', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 06:58:37.496', NULL,
        '2022-12-12 06:58:37.496', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602075448140484609, 1546097849947701249, '4173630013', '2022-12-12 07:00:00', '0.753', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 06:58:46.616', NULL,
        '2022-12-12 06:58:46.616', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602075507355668481, 1540542229423104001, '4173630006', '2022-12-12 07:00:00', '0.306', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 06:59:00.734', NULL,
        '2022-12-12 06:59:00.734', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602075617347096577, 1540542229414715394, '4173630012', '2022-12-12 07:00:00', '0.065', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 06:59:26.958', NULL,
        '2022-12-12 06:59:26.958', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602075795017814018, 1540542229423104002, '4173630001', '2022-12-12 07:00:00', '0.491', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 07:00:09.318', NULL,
        '2022-12-12 07:00:09.318', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602075805981724674, 1540542229322440706, '4173630019', '2022-12-12 07:00:00', '0.179', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 07:00:11.932', NULL,
        '2022-12-12 07:00:11.932', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602075842790936578, 1540542229330829313, '4173630015', '2022-12-12 07:00:00', '0.034', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 07:00:20.708', NULL,
        '2022-12-12 07:00:20.708', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602075908159164417, 1540542229406326787, '4173630008', '2022-12-12 07:00:00', '0.398', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 07:00:36.293', NULL,
        '2022-12-12 07:00:36.293', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602076210488791041, 1540542229397938178, '4173630007', '2022-12-12 07:05:00', '-0.011', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 07:01:48.374', NULL,
        '2022-12-12 07:01:48.374', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602077224767967233, 1540542229397938178, '4173630007', '2022-12-12 07:10:00', '-0.012', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 07:05:50.197', NULL,
        '2022-12-12 07:05:50.197', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602078726978916354, 1540542229397938178, '4173630007', '2022-12-12 07:15:00', '-0.013', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 07:11:48.352', NULL,
        '2022-12-12 07:11:48.352', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602079741321007106, 1540542229397938178, '4173630007', '2022-12-12 07:20:00', '-0.013', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 07:15:50.19', NULL, '2022-12-12 07:15:50.19',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602081243448070146, 1540542229397938178, '4173630007', '2022-12-12 07:25:00', '-0.013', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 07:21:48.325', NULL,
        '2022-12-12 07:21:48.325', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602082257936961537, 1540542229397938178, '4173630007', '2022-12-12 07:30:00', '-0.013', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 07:25:50.197', NULL,
        '2022-12-12 07:25:50.197', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602083564559126529, 1540542229397938178, '4173630007', '2022-12-12 07:35:00', '-0.013', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 07:31:01.721', NULL,
        '2022-12-12 07:31:01.721', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602083885477908481, 1540542229410521090, '4173630002', '2022-12-12 07:32:18.018', NULL, NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'ONLINE', NULL, NULL, '2022-12-12 07:32:18.234', NULL, '2022-12-12 07:32:18.234', NULL,
        0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602084795486687233, 1540542229397938178, '4173630007', '2022-12-12 07:40:00', '-0.013', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 07:35:55.197', NULL,
        '2022-12-12 07:35:55.197', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602085323650224130, 1542497336240947201, '4173630011', '2022-12-12 07:38:00.909', NULL, NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'ONLINE', NULL, NULL, '2022-12-12 07:38:01.121', NULL, '2022-12-12 07:38:01.121', NULL,
        0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602086297823465473, 1540542229397938178, '4173630007', '2022-12-12 07:45:00', '-0.014', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 07:41:53.382', NULL,
        '2022-12-12 07:41:53.382', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602087312257830914, 1540542229397938178, '4173630007', '2022-12-12 07:50:00', '-0.014', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 07:45:55.242', NULL,
        '2022-12-12 07:45:55.242', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602088815232143361, 1540542229397938178, '4173630007', '2022-12-12 07:55:00', '-0.014', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 07:51:53.579', NULL,
        '2022-12-12 07:51:53.579', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602089807407337474, 1540542229372772353, '4173630003', '2022-12-12 08:00:00', '-0.206', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 07:55:50.131', NULL,
        '2022-12-12 07:55:50.131', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602089828739567617, 1540542229397938178, '4173630007', '2022-12-12 08:00:00', '-0.014', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 07:55:55.218', NULL,
        '2022-12-12 07:55:55.218', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602089894103601154, 1540542229410521090, '4173630002', '2022-12-12 08:00:00', '0.005', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 07:56:10.802', NULL,
        '2022-12-12 07:56:10.802', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602089954824540162, 1540542229376966658, '4173630004', '2022-12-12 08:00:00', '0.063', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 07:56:25.279', NULL,
        '2022-12-12 07:56:25.279', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602089989725343746, 1540542229414715395, '4173630009', '2022-12-12 08:00:00', '0.770', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 07:56:33.6', NULL, '2022-12-12 07:56:33.6',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602090202426888193, 1540542229402132481, '4173630000', '2022-12-12 08:00:00', '-0.406', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 07:57:24.312', NULL,
        '2022-12-12 07:57:24.312', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602090211000045570, 1540542229418909698, '4173630005', '2022-12-12 08:00:00', '0.456', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 07:57:26.356', NULL,
        '2022-12-12 07:57:26.356', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602090378516353025, 1540542229418909697, '4173630016', '2022-12-12 08:00:00', '0.209', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 07:58:06.295', NULL,
        '2022-12-12 07:58:06.295', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602090569852112898, 1537784416470888450, '4173630071', '2022-12-12 07:58:51.887', NULL, NULL, 'NORMAL',
        'ONLINE', 'ZG', 'KEEP', 'ONLINE', NULL, NULL, '2022-12-12 07:58:51.913', NULL, '2022-12-12 07:58:51.913', NULL,
        0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602090581726187521, 1537784416978399234, '4173630073', '2022-12-12 07:58:52.695', NULL, NULL, 'NORMAL',
        'ONLINE', 'ZG', 'KEEP', 'ONLINE', NULL, NULL, '2022-12-12 07:58:54.744', NULL, '2022-12-12 07:58:54.744', NULL,
        0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602090582921564161, 1540542229347606530, '4173630014', '2022-12-12 08:00:00', '0.084', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 07:58:55.029', NULL,
        '2022-12-12 07:58:55.029', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602090658578419714, 1546097849947701249, '4173630013', '2022-12-12 08:00:00', '0.752', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 07:59:13.067', NULL,
        '2022-12-12 07:59:13.067', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602090717307064321, 1540542229423104001, '4173630006', '2022-12-12 08:00:00', '0.306', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 07:59:27.069', NULL,
        '2022-12-12 07:59:27.069', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602090748361691137, 1540542229427298305, '4173630010', '2022-12-12 08:00:00', '0.223', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 07:59:34.473', NULL,
        '2022-12-12 07:59:34.473', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602090926518947842, 1540542229330829313, '4173630015', '2022-12-12 08:00:00', '0.025', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 08:00:16.949', NULL,
        '2022-12-12 08:00:16.949', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602090989857132546, 1540542229414715394, '4173630012', '2022-12-12 08:00:00', '0.062', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 08:00:32.05', NULL, '2022-12-12 08:00:32.05',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602091056156495874, 1540542229431492610, '4173630017', '2022-12-12 08:00:00', '0.107', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 08:00:47.857', NULL,
        '2022-12-12 08:00:47.857', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602091150419283969, 1542497336240947201, '4173630011', '2022-12-12 08:00:00', '-0.425', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 08:01:10.331', NULL,
        '2022-12-12 08:01:10.331', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602091166009511938, 1540542229423104002, '4173630001', '2022-12-12 08:00:00', '0.510', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 08:01:14.048', NULL,
        '2022-12-12 08:01:14.048', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602091176889536513, 1540542229322440706, '4173630019', '2022-12-12 08:00:00', '0.179', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 08:01:16.642', NULL,
        '2022-12-12 08:01:16.642', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602091279205388290, 1540542229406326787, '4173630008', '2022-12-12 08:00:00', '0.399', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 08:01:41.036', NULL,
        '2022-12-12 08:01:41.036', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602091330799521794, 1540542229397938178, '4173630007', '2022-12-12 08:05:00', '-0.014', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 08:01:53.337', NULL,
        '2022-12-12 08:01:53.337', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602092345372299265, 1540542229397938178, '4173630007', '2022-12-12 08:10:00', '-0.013', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 08:05:55.23', NULL, '2022-12-12 08:05:55.23',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602093848371777537, 1540542229397938178, '4173630007', '2022-12-12 08:15:00', '-0.013', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 08:11:53.573', NULL,
        '2022-12-12 08:11:53.573', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602093946497519618, 1537784416470888450, '4173630071', '2022-12-12 08:12:16.918', NULL, NULL, 'NORMAL',
        'OFFLINE', 'ZG', 'KEEP', 'OFFLINE', NULL, NULL, '2022-12-12 08:12:16.968', NULL, '2022-12-12 08:12:16.968',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602094827196502017, 1537784416978399234, '4173630073', '2022-12-12 08:15:46.919', NULL, NULL, 'NORMAL',
        'OFFLINE', 'ZG', 'KEEP', 'OFFLINE', NULL, NULL, '2022-12-12 08:15:46.943', NULL, '2022-12-12 08:15:46.943',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602094861958893570, 1540542229397938178, '4173630007', '2022-12-12 08:20:00', '-0.013', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 08:15:55.231', NULL,
        '2022-12-12 08:15:55.231', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602096363972710401, 1540542229397938178, '4173630007', '2022-12-12 08:25:00', '-0.012', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 08:21:53.339', NULL,
        '2022-12-12 08:21:53.339', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602097379178827777, 1540542229397938178, '4173630007', '2022-12-12 08:30:00', '-0.011', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 08:25:55.383', NULL,
        '2022-12-12 08:25:55.383', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602098880676745218, 1540542229397938178, '4173630007', '2022-12-12 08:35:00', '-0.011', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 08:31:53.368', NULL,
        '2022-12-12 08:31:53.368', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602099895526346754, 1540542229397938178, '4173630007', '2022-12-12 08:40:00', '-0.011', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 08:35:55.327', NULL,
        '2022-12-12 08:35:55.327', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602101397376585730, 1540542229397938178, '4173630007', '2022-12-12 08:45:00', '-0.010', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 08:41:53.396', NULL,
        '2022-12-12 08:41:53.396', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602102411815145473, 1540542229397938178, '4173630007', '2022-12-12 08:50:00', '-0.010', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 08:45:55.257', NULL,
        '2022-12-12 08:45:55.257', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602103914135146498, 1540542229397938178, '4173630007', '2022-12-12 08:55:00', '-0.009', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 08:51:53.438', NULL,
        '2022-12-12 08:51:53.438', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602104907287613442, 1540542229372772353, '4173630003', '2022-12-12 09:00:00', '-0.210', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 08:55:50.224', NULL,
        '2022-12-12 08:55:50.224', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602104928733089794, 1540542229397938178, '4173630007', '2022-12-12 09:00:00', '-0.007', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 08:55:55.337', NULL,
        '2022-12-12 08:55:55.337', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602104992075468802, 1540542229410521090, '4173630002', '2022-12-12 09:00:00', '-0.005', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 08:56:10.439', NULL,
        '2022-12-12 08:56:10.439', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602105003874045954, 1540542229414715395, '4173630009', '2022-12-12 09:00:00', '0.765', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 08:56:13.252', NULL,
        '2022-12-12 08:56:13.252', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602105053517828098, 1540542229376966658, '4173630004', '2022-12-12 09:00:00', '0.063', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 08:56:25.088', NULL,
        '2022-12-12 08:56:25.088', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602105301246005250, 1540542229402132481, '4173630000', '2022-12-12 09:00:00', '-0.409', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 08:57:24.151', NULL,
        '2022-12-12 08:57:24.151', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602105309835939842, 1540542229418909698, '4173630005', '2022-12-12 09:00:00', '0.460', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 08:57:26.199', NULL,
        '2022-12-12 08:57:26.199', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602105357198020609, 1540542229418909697, '4173630016', '2022-12-12 09:00:00', '0.213', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 08:57:37.491', NULL,
        '2022-12-12 08:57:37.491', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602105672991363073, 1546097849947701249, '4173630013', '2022-12-12 09:00:00', '0.745', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 08:58:52.782', NULL,
        '2022-12-12 08:58:52.782', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602105731724201986, 1540542229423104001, '4173630006', '2022-12-12 09:00:00', '0.299', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 08:59:06.785', NULL,
        '2022-12-12 08:59:06.785', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602105933851906049, 1540542229347606530, '4173630014', '2022-12-12 09:01:00', '0.080', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 08:59:54.976', NULL,
        '2022-12-12 08:59:54.976', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602105941158383617, 1540542229330829313, '4173630015', '2022-12-12 09:00:00', '0.023', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 08:59:56.718', NULL,
        '2022-12-12 08:59:56.718', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602106089737408514, 1540542229414715394, '4173630012', '2022-12-12 09:00:00', '-0.054', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 09:00:32.142', NULL,
        '2022-12-12 09:00:32.142', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602106100185419777, 1540542229427298305, '4173630010', '2022-12-12 09:01:00', '0.224', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 09:00:34.633', NULL,
        '2022-12-12 09:00:34.633', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602106406679990273, 1540542229431492610, '4173630017', '2022-12-12 09:01:00', '0.088', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 09:01:47.707', NULL,
        '2022-12-12 09:01:47.707', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602106430491054081, 1540542229397938178, '4173630007', '2022-12-12 09:05:00', '-0.004', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 09:01:53.384', NULL,
        '2022-12-12 09:01:53.384', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602106501693558786, 1542497336240947201, '4173630011', '2022-12-12 09:01:00', '-0.430', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 09:02:10.36', NULL, '2022-12-12 09:02:10.36',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602106517124403202, 1540542229423104002, '4173630001', '2022-12-12 09:01:00', '0.508', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 09:02:14.039', NULL,
        '2022-12-12 09:02:14.039', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602106528239308801, 1540542229322440706, '4173630019', '2022-12-12 09:01:00', '0.179', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 09:02:16.689', NULL,
        '2022-12-12 09:02:16.689', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602106630320279554, 1540542229406326787, '4173630008', '2022-12-12 09:01:00', '0.398', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 09:02:41.027', NULL,
        '2022-12-12 09:02:41.027', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602107445013499905, 1540542229397938178, '4173630007', '2022-12-12 09:10:00', '0.000', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 09:05:55.265', NULL,
        '2022-12-12 09:05:55.265', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602108947199283201, 1540542229397938178, '4173630007', '2022-12-12 09:15:00', '0.008', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 09:11:53.414', NULL,
        '2022-12-12 09:11:53.414', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602109962581561346, 1540542229397938178, '4173630007', '2022-12-12 09:20:00', '0.006', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 09:15:55.5', NULL, '2022-12-12 09:15:55.5',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602111463823626242, 1540542229397938178, '4173630007', '2022-12-12 09:25:00', '0.001', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 09:21:53.424', NULL,
        '2022-12-12 09:21:53.424', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602112478484484097, 1540542229397938178, '4173630007', '2022-12-12 09:30:00', '-0.004', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 09:25:55.338', NULL,
        '2022-12-12 09:25:55.338', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602113980615741441, 1540542229397938178, '4173630007', '2022-12-12 09:35:00', '-0.008', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 09:31:53.474', NULL,
        '2022-12-12 09:31:53.474', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602114995507286017, 1540542229397938178, '4173630007', '2022-12-12 09:40:00', '-0.011', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 09:35:55.443', NULL,
        '2022-12-12 09:35:55.443', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602116497495937025, 1540542229397938178, '4173630007', '2022-12-12 09:45:00', '-0.012', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 09:41:53.545', NULL,
        '2022-12-12 09:41:53.545', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602117511791890433, 1540542229397938178, '4173630007', '2022-12-12 09:50:00', '-0.013', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 09:45:55.372', NULL,
        '2022-12-12 09:45:55.372', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602119013705043969, 1540542229397938178, '4173630007', '2022-12-12 09:55:00', '-0.014', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 09:51:53.456', NULL,
        '2022-12-12 09:51:53.456', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602119920702312450, 1540542229372772353, '4173630003', '2022-12-12 10:00:00', '-0.215', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 09:55:29.701', NULL,
        '2022-12-12 09:55:29.701', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602120028223295490, 1540542229397938178, '4173630007', '2022-12-12 10:00:00', '-0.014', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 09:55:55.336', NULL,
        '2022-12-12 09:55:55.336', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602120070233444354, 1540542229376966658, '4173630004', '2022-12-12 10:00:00', '0.060', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 09:56:05.352', NULL,
        '2022-12-12 09:56:05.352', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602120103397806082, 1540542229414715395, '4173630009', '2022-12-12 10:00:00', '0.773', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 09:56:13.259', NULL,
        '2022-12-12 09:56:13.259', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602120316200013826, 1540542229402132481, '4173630000', '2022-12-12 10:00:00', '-0.406', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 09:57:03.995', NULL,
        '2022-12-12 09:57:03.995', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602120324773171201, 1540542229418909698, '4173630005', '2022-12-12 10:00:00', '0.463', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 09:57:06.039', NULL,
        '2022-12-12 09:57:06.039', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602120532156338177, 1540542229347606530, '4173630014', '2022-12-12 10:00:00', '0.080', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 09:57:55.483', NULL,
        '2022-12-12 09:57:55.483', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602120697810374657, 1540542229427298305, '4173630010', '2022-12-12 10:00:00', '0.231', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 09:58:34.978', NULL,
        '2022-12-12 09:58:34.978', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602120772661923841, 1546097849947701249, '4173630013', '2022-12-12 10:00:00', '0.744', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 09:58:52.824', NULL,
        '2022-12-12 09:58:52.824', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602120831231184897, 1540542229423104001, '4173630006', '2022-12-12 10:00:00', '0.287', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 09:59:06.787', NULL,
        '2022-12-12 09:59:06.787', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602120944930377730, 1540542229414715394, '4173630012', '2022-12-12 10:00:00', '-0.060', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 09:59:33.896', NULL,
        '2022-12-12 09:59:33.896', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602121004430774273, 1540542229431492610, '4173630017', '2022-12-12 10:00:00', '0.103', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 09:59:48.082', NULL,
        '2022-12-12 09:59:48.082', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602121040237547521, 1540542229330829313, '4173630015', '2022-12-12 10:00:00', '0.024', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 09:59:56.619', NULL,
        '2022-12-12 09:59:56.619', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602121073540321282, 1540542229423104002, '4173630001', '2022-12-12 10:00:00', '0.510', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 10:00:04.559', NULL,
        '2022-12-12 10:00:04.559', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602121084512620545, 1540542229322440706, '4173630019', '2022-12-12 10:00:00', '0.179', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 10:00:07.175', NULL,
        '2022-12-12 10:00:07.175', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602121100392255490, 1542497336240947201, '4173630011', '2022-12-12 10:00:00', '-0.433', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 10:00:10.961', NULL,
        '2022-12-12 10:00:10.961', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602121228951867394, 1540542229406326787, '4173630008', '2022-12-12 10:00:00', '0.351', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 10:00:41.612', NULL,
        '2022-12-12 10:00:41.612', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602121530908200962, 1540542229397938178, '4173630007', '2022-12-12 10:05:00', '-0.015', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 10:01:53.603', NULL,
        '2022-12-12 10:01:53.603', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602121906986274817, 1540542229418909697, '4173630016', '2022-12-12 10:06:00', '0.206', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 10:03:23.268', NULL,
        '2022-12-12 10:03:23.268', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602122545128656898, 1540542229397938178, '4173630007', '2022-12-12 10:10:00', '-0.016', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 10:05:55.412', NULL,
        '2022-12-12 10:05:55.412', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602124047385743362, 1540542229397938178, '4173630007', '2022-12-12 10:15:00', '-0.016', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 10:11:53.579', NULL,
        '2022-12-12 10:11:53.579', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602125061350346754, 1540542229397938178, '4173630007', '2022-12-12 10:20:00', '-0.015', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 10:15:55.327', NULL,
        '2022-12-12 10:15:55.327', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602126564060418049, 1540542229397938178, '4173630007', '2022-12-12 10:25:00', '-0.013', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 10:21:53.601', NULL,
        '2022-12-12 10:21:53.601', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602127577651728385, 1540542229397938178, '4173630007', '2022-12-12 10:30:00', '-0.004', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 10:25:55.26', NULL, '2022-12-12 10:25:55.26',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602129080684761089, 1540542229397938178, '4173630007', '2022-12-12 10:35:00', '0.009', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 10:31:53.61', NULL, '2022-12-12 10:31:53.61',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602130094427066370, 1540542229397938178, '4173630007', '2022-12-12 10:40:00', '0.020', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 10:35:55.306', NULL,
        '2022-12-12 10:35:55.306', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602131596663181313, 1540542229397938178, '4173630007', '2022-12-12 10:45:00', '0.028', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 10:41:53.467', NULL,
        '2022-12-12 10:41:53.467', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602132610778779650, 1540542229397938178, '4173630007', '2022-12-12 10:50:00', '0.033', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 10:45:55.251', NULL,
        '2022-12-12 10:45:55.251', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602134112905842689, 1540542229397938178, '4173630007', '2022-12-12 10:55:00', '0.037', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 10:51:53.386', NULL,
        '2022-12-12 10:51:53.386', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602135020737777666, 1540542229372772353, '4173630003', '2022-12-12 11:00:00', '-0.215', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 10:55:29.83', NULL, '2022-12-12 10:55:29.83',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602135127692529665, 1540542229397938178, '4173630007', '2022-12-12 11:00:00', '0.040', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 10:55:55.33', NULL, '2022-12-12 10:55:55.33',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602135202065928194, 1540542229410521090, '4173630002', '2022-12-12 11:00:00', '-0.007', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 10:56:13.062', NULL,
        '2022-12-12 10:56:13.062', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602135210643279874, 1540542229414715395, '4173630009', '2022-12-12 11:00:00', '0.771', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 10:56:15.107', NULL,
        '2022-12-12 10:56:15.107', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602135263143383041, 1540542229376966658, '4173630004', '2022-12-12 11:00:00', '0.056', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 10:56:27.624', NULL,
        '2022-12-12 10:56:27.624', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602135425051906049, 1540542229418909697, '4173630016', '2022-12-12 11:00:00', '0.202', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 10:57:06.226', NULL,
        '2022-12-12 10:57:06.226', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602135511257436161, 1540542229402132481, '4173630000', '2022-12-12 11:00:00', '-0.399', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 10:57:26.778', NULL,
        '2022-12-12 10:57:26.778', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602135519834787842, 1540542229418909698, '4173630005', '2022-12-12 11:00:00', '0.465', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 10:57:28.824', NULL,
        '2022-12-12 10:57:28.824', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602135630291783681, 1540542229347606530, '4173630014', '2022-12-12 11:00:00', '0.083', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 10:57:55.159', NULL,
        '2022-12-12 10:57:55.159', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602135796621103106, 1540542229427298305, '4173630010', '2022-12-12 11:00:00', '0.239', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 10:58:34.815', NULL,
        '2022-12-12 10:58:34.815', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602135872189878274, 1546097849947701249, '4173630013', '2022-12-12 11:00:00', '0.735', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 10:58:52.832', NULL,
        '2022-12-12 10:58:52.832', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602135930914328578, 1540542229423104001, '4173630006', '2022-12-12 11:00:00', '0.296', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 10:59:06.833', NULL,
        '2022-12-12 10:59:06.833', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602136044525441026, 1540542229414715394, '4173630012', '2022-12-12 11:00:00', '-0.066', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 10:59:33.92', NULL, '2022-12-12 10:59:33.92',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602136103161810945, 1540542229431492610, '4173630017', '2022-12-12 11:00:00', '0.093', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 10:59:47.9', NULL, '2022-12-12 10:59:47.9',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602136140067491841, 1540542229330829313, '4173630015', '2022-12-12 11:00:00', '0.031', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 10:59:56.699', NULL,
        '2022-12-12 10:59:56.699', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602136172460101634, 1540542229423104002, '4173630001', '2022-12-12 11:00:00', '0.510', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 11:00:04.422', NULL,
        '2022-12-12 11:00:04.422', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602136183235268610, 1540542229322440706, '4173630019', '2022-12-12 11:00:00', '0.179', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 11:00:06.991', NULL,
        '2022-12-12 11:00:06.991', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602136200083787777, 1542497336240947201, '4173630011', '2022-12-12 11:00:00', '-0.434', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 11:00:11.008', NULL,
        '2022-12-12 11:00:11.008', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602136327863259137, 1540542229406326787, '4173630008', '2022-12-12 11:00:00', '0.371', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 11:00:41.473', NULL,
        '2022-12-12 11:00:41.473', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602136630222245889, 1540542229397938178, '4173630007', '2022-12-12 11:05:00', '0.042', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 11:01:53.561', NULL,
        '2022-12-12 11:01:53.561', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602137644140711938, 1540542229397938178, '4173630007', '2022-12-12 11:10:00', '0.042', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 11:05:55.298', NULL,
        '2022-12-12 11:05:55.298', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602139146351661058, 1540542229397938178, '4173630007', '2022-12-12 11:15:00', '0.043', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 11:11:53.453', NULL,
        '2022-12-12 11:11:53.453', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602140160823775233, 1540542229397938178, '4173630007', '2022-12-12 11:20:00', '0.044', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 11:15:55.322', NULL,
        '2022-12-12 11:15:55.322', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602140785313697793, 1540542229376966658, '4173630004', '2022-12-12 11:20:00', '0.056', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 11:18:24.212', NULL,
        '2022-12-12 11:18:24.212', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602141662820814849, 1540542229397938178, '4173630007', '2022-12-12 11:25:00', '0.046', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 11:21:53.426', NULL,
        '2022-12-12 11:21:53.426', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602142304096342017, 1540542229423104001, '4173630006', '2022-12-12 11:25:00', '0.297', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 11:24:26.318', NULL,
        '2022-12-12 11:24:26.318', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602142677372620802, 1540542229397938178, '4173630007', '2022-12-12 11:30:00', '0.046', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 11:25:55.314', NULL,
        '2022-12-12 11:25:55.314', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602144179424186369, 1540542229397938178, '4173630007', '2022-12-12 11:35:00', '0.045', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 11:31:53.431', NULL,
        '2022-12-12 11:31:53.431', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602145188905078785, 1540542229427298305, '4173630010', '2022-12-12 11:35:00', '0.238', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 11:35:54.11', NULL, '2022-12-12 11:35:54.11',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602145193841774594, 1540542229397938178, '4173630007', '2022-12-12 11:40:00', '0.045', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 11:35:55.287', NULL,
        '2022-12-12 11:35:55.287', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602146695939477505, 1540542229397938178, '4173630007', '2022-12-12 11:45:00', '0.046', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 11:41:53.415', NULL,
        '2022-12-12 11:41:53.415', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602147710331899905, 1540542229397938178, '4173630007', '2022-12-12 11:50:00', '0.044', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 11:45:55.265', NULL,
        '2022-12-12 11:45:55.265', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602149212509294594, 1540542229397938178, '4173630007', '2022-12-12 11:55:00', '0.042', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 11:51:53.412', NULL,
        '2022-12-12 11:51:53.412', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602150119800164353, 1540542229372772353, '4173630003', '2022-12-12 12:00:00', '-0.210', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 11:55:29.727', NULL,
        '2022-12-12 11:55:29.727', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602150269272576001, 1540542229376966658, '4173630004', '2022-12-12 12:00:00', '0.055', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 11:56:05.364', NULL,
        '2022-12-12 11:56:05.364', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602150301388361730, 1540542229410521090, '4173630002', '2022-12-12 12:00:00', '-0.003', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 11:56:13.021', NULL,
        '2022-12-12 11:56:13.021', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602150309965713410, 1540542229414715395, '4173630009', '2022-12-12 12:00:00', '0.780', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 11:56:15.066', NULL,
        '2022-12-12 11:56:15.066', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602150610504372225, 1540542229402132481, '4173630000', '2022-12-12 12:00:00', '-0.393', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 11:57:26.72', NULL, '2022-12-12 11:57:26.72',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602150619111084033, 1540542229418909698, '4173630005', '2022-12-12 12:00:00', '0.465', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 11:57:28.772', NULL,
        '2022-12-12 11:57:28.772', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602150716884504577, 1540542229418909697, '4173630016', '2022-12-12 12:00:00', '0.170', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 11:57:52.083', NULL,
        '2022-12-12 11:57:52.083', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602150972183400449, 1546097849947701249, '4173630013', '2022-12-12 12:00:00', '0.732', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 11:58:52.95', NULL, '2022-12-12 11:58:52.95',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602151115225944065, 1540542229423104001, '4173630006', '2022-12-12 12:00:00', '0.300', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 11:59:27.055', NULL,
        '2022-12-12 11:59:27.055', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602151136411373569, 1540542229414715394, '4173630012', '2022-12-12 12:00:00', '0.003', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 11:59:32.106', NULL,
        '2022-12-12 11:59:32.106', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602151171194736642, 1540542229347606530, '4173630014', '2022-12-12 12:00:00', '0.086', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 11:59:40.399', NULL,
        '2022-12-12 11:59:40.399', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602151239482200065, 1540542229330829313, '4173630015', '2022-12-12 12:00:00', '0.032', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 11:59:56.68', NULL, '2022-12-12 11:59:56.68',
        NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602151248315404289, 1540542229427298305, '4173630010', '2022-12-12 12:00:00', '0.240', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 11:59:58.786', NULL,
        '2022-12-12 11:59:58.786', NULL, 0);

INSERT INTO `wr_ri_his` (`id`, `st_id`, `stcd`, `mot`, `z`, `d`, `alarm_status`, `comm_status`, `sttp`, `wptn`,
                         `OPERATION_STATUS`, `create_user`, `create_dept`, `create_time`, `update_user`, `update_time`,
                         `status`, `is_deleted`)
VALUES (1602151272571064322, 1540542229423104002, '4173630001', '2022-12-12 12:00:00', '0.516', NULL, 'NORMAL',
        'ONLINE', 'PZ', 'KEEP', 'REPORT_PROPERTY', NULL, NULL, '2022-12-12 12:00:04.569', NULL,
        '2022-12-12 12:00:04.569', NULL, 0);
