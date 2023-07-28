package cc.allio.uno.component.sequential.dispatch;

import cc.allio.uno.component.sequential.BaseCompositeSequential;
import cc.allio.uno.component.sequential.Sequential;
import cc.allio.uno.core.util.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * 对{@link Dispatcher}进行调度的调度器
 *
 * @author jiangwei
 * @date 2022/2/26 22:55
 * @since 1.0
 */
@Slf4j
public class DispatchDispatcher implements Dispatcher {

    private final List<Dispatcher> dispatchers;

    public DispatchDispatcher(Dispatcher... dispatcher) {
        dispatchers = CollectionUtils.newArrayList(dispatcher);
    }

    @Override
    public void dispatch(Sequential sequential) throws Throwable {
        if (Objects.isNull(sequential)) {
            throw new NullPointerException("sequential is null can't dispatch");
        }
        if (sequential instanceof BaseCompositeSequential) {
            List<Sequential> composeSequential = ((BaseCompositeSequential) sequential).getCompositeMetadata();
            composeSequential.forEach(this::dispatch0);
        } else {
            dispatch0(sequential);
        }
    }

    /**
     * 执行调度动作
     *
     * @param sequential 时序对象
     */
    private void dispatch0(Sequential sequential) {
        Flux.fromIterable(dispatchers)
                .filter(dispatcher -> {
                    Predicate<Sequential> assign = (Predicate<Sequential>) dispatcher.isAssign();
                    return assign.test(sequential);
                })
                .subscribe(dispatcher -> {
                    try {
                        dispatcher.dispatch(sequential);
                    } catch (Throwable e) {
                        log.error("dispatcher: [{}] failed", dispatcher.getClass().getSimpleName(), e);
                    }
                });
    }

    @Override
    @Deprecated
    public Predicate<? extends Sequential> isAssign() {
        return null;
    }
}
