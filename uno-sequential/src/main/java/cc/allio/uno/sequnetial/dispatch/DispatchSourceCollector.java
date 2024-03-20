package cc.allio.uno.sequnetial.dispatch;

import cc.allio.uno.core.metadata.endpoint.source.SourceCollector;
import cc.allio.uno.sequnetial.Sequential;

/**
 * 基于{@link Dispatcher}分派数据源
 *
 * @author j.x
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
