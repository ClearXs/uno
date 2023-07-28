package cc.allio.uno.core.metadata.endpoint.source.reactive;

import org.springframework.context.ApplicationContext;
import reactor.core.publisher.Flux;

/**
 * reactive for source.
 * 如果不产生订阅数据源时不会进行产生
 *
 * @author jiangwei
 * @date 2023/4/27 17:43
 * @since 1.1.4
 */
public interface ReactiveSource<C> {

    /**
     * 订阅当前数据源
     *
     * @return reactive mono
     */
    Flux<C> subscribe();

    /**
     * 把当前数据源注册到当前环境中
     */
    void register(ApplicationContext context);
}
