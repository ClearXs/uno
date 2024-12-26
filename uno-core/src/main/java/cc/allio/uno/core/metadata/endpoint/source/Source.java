package cc.allio.uno.core.metadata.endpoint.source;

import org.springframework.context.ApplicationContext;

import java.util.function.Consumer;

/**
 * Sequential数据源
 *
 * @author j.x
 * @since 1.1.0
 */
public interface Source<C> {

    /**
     * 订阅当前数据源
     */
    void subscribe(Consumer<C> next);

    /**
     * 把当前数据源注册到当前环境中
     */
    void register(ApplicationContext context);

}
