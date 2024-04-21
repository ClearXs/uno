package cc.allio.uno.data.orm.executor.influxdb;

import cc.allio.uno.core.exception.Exceptions;
import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.data.orm.executor.BaseCommandExecutorLoader;
import cc.allio.uno.data.orm.executor.CommandExecutorLoader;
import cc.allio.uno.data.orm.executor.interceptor.Interceptor;
import cc.allio.uno.data.orm.executor.options.ExecutorOptions;
import com.google.auto.service.AutoService;

import java.util.List;

/**
 * load influxdb command executor
 *
 * @author j.x
 * @date 2024/4/1 17:21
 * @since 1.1.8
 */
@AutoService(CommandExecutorLoader.class)
public class InfluxdbCommandExecutorLoader extends BaseCommandExecutorLoader<InfluxdbCommandExecutor> {

    @Override
    public InfluxdbCommandExecutor onLoad(List<Interceptor> interceptors) {
        throw Exceptions.unOperate("load by interceptors");
    }

    @Override
    public InfluxdbCommandExecutor onLoad(ExecutorOptions executorOptions) {
        return new InfluxdbCommandExecutor(executorOptions);
    }

    @Override
    public boolean match(DBType dbType) {
        return DBType.INFLUXDB == dbType;
    }
}
