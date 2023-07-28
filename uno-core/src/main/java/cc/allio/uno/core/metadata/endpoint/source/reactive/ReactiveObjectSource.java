package cc.allio.uno.core.metadata.endpoint.source.reactive;

import org.springframework.context.ApplicationContext;

/**
 * 对象 source
 *
 * @author jiangwei
 * @date 2023/4/27 18:04
 * @since 1.1.4
 */
public class ReactiveObjectSource<C> extends ReactiveSinkSource<C> {

    @Override
    public void register(ApplicationContext context) {
    }
}
