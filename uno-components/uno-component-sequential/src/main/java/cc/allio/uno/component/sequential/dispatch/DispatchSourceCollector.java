package cc.allio.uno.component.sequential.dispatch;

import cc.allio.uno.component.sequential.Sequential;
import cc.allio.uno.core.metadata.endpoint.source.SourceCollector;

/**
 * 基于{@link Dispatcher}分派数据源
 *
 * @author jiangwei
 * @date 2022/11/22 11:26
 * @since 1.1.2
 */
public class DispatchSourceCollector implements SourceCollector<Sequential> {
    private final Dispatcher dispatcher;

    public DispatchSourceCollector(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void collect(Sequential element) {
        try {
            dispatcher.dispatch(element);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
