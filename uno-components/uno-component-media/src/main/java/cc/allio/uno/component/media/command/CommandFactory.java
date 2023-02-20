package cc.allio.uno.component.media.command;

import cc.allio.uno.component.media.MediaException;
import cc.allio.uno.core.proxy.IncapacityCglibInvocationInterceptor;
import cc.allio.uno.core.proxy.ProxyFactory;
import cc.allio.uno.core.spi.FactoriesLoader;
import cc.allio.uno.core.util.Collections;

import java.util.ArrayList;
import java.util.Set;


/**
 * 指令生产工厂
 *
 * @author jiangwei
 * @date 2022/3/31 16:27
 * @since 1.0.6
 */
public class CommandFactory {

    /**
     * 创建指令
     *
     * @param commandType 指令类型，需要时接口类型
     * @param args        创建参数
     * @return Command实例对象
     */
    public static Command createCommand(Class<? extends Command> commandType, Object... args) throws MediaException {
        FactoriesLoader factoriesLoader = new FactoriesLoader("META-INF/media.factories");
        try {
            Set<? extends Class<? extends Command>> commandClasses = factoriesLoader.loadFactoriesByType(commandType, Thread.currentThread().getContextClassLoader());
            // 默认取第一个
            if (Collections.isEmpty(commandClasses)) {
                throw new NullPointerException(String.format("Find Type %s", commandType.getName()));
            }
            ArrayList<? extends Class<? extends Command>> classes = new ArrayList<>(commandClasses);
            return ProxyFactory.proxy().newProxyInstance(classes.get(0), new IncapacityCglibInvocationInterceptor(), args);
        } catch (Throwable e) {
            throw new MediaException(String.format("Load Type %s failed, Reason %s", commandType.getName(), e.getMessage()), e);
        }
    }
}
