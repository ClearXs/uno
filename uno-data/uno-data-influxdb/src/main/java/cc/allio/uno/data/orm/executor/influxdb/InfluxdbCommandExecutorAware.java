package cc.allio.uno.data.orm.executor.influxdb;

import cc.allio.uno.data.orm.executor.CommandExecutorAware;

/**
 * a marked interface use for obtain {@link InfluxdbCommandExecutor}
 *
 * @author j.x
 * @since 1.1.8
 */
public interface InfluxdbCommandExecutorAware extends CommandExecutorAware<InfluxdbCommandExecutor> {
}
