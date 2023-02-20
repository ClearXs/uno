DROP TABLE IF EXISTS `wr_st_alarm_threshold`;
CREATE TABLE `wr_st_alarm_threshold`
(
    `id`            INT        NOT NULL,
    `st_id`         INT        NOT NULL,
    `business_type` varchar(20) NOT NULL,
    `target`        varchar(20) NOT NULL,
    `target_name`   varchar(30),
    `level`         varchar(20) NOT NULL,
    `level_name`    varchar(30),
    `is_interval`   INT        NOT NULL,
    `maximum`       decimal(7, 3),
    `max_symbol`    varchar(10),
    `minimun`       decimal(7, 3),
    `min_symbol`    varchar(10),
    `single_num`    decimal(7, 3),
    `single_symbol` varchar(10),
    `status`        varchar(20),
    `create_user`   INT,
    `create_dept`   INT,
    `create_time`   date,
    `update_user`   INT,
    `update_time`   date,
    `is_deleted`    INT,
    `warning`       INT
);
