package cc.allio.uno.core.chain;

import cc.allio.uno.core.api.Self;
import cc.allio.uno.core.function.lambda.ThrowingMethodBiFunction;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;

/**
 * 默认实现链，参考自Spring构建网关过滤器链
 *
 * @author j.x
 * @since 1.0
 */
@Slf4j
public class DefaultChain<IN, OUT> implements Chain<IN, OUT> {

    /**
     * 排好序的结点集合
     */
    final List<? extends Node<IN, OUT>> nodes;

    /**
     * 记录当前链执行到的结点的索引
     */
    final int index;

    public DefaultChain(List<? extends Node<IN, OUT>> nodes) {
        AnnotationAwareOrderComparator.sort(nodes);
        this.nodes = nodes;
        this.index = 0;
    }

    private DefaultChain(Chain<IN, OUT> parent, int index) {
        this.nodes = parent.getNodes();
        this.index = index;
    }

    @Override
    public Mono<OUT> proceed(ChainContext<IN> context) {
        Publisher<OUT> publisher =
                Process.from(this)
                        .onHandle((node, nextChain) -> node.execute(nextChain, context))
                        .onEmpty(Mono.empty())
                        .onErrorContinue(chain -> chain.proceed(context))
                        .onProcess();
        return Mono.from(publisher)
                .onErrorContinue((err, o) -> {
                    if (log.isWarnEnabled()) {
                        log.warn("Chain execute error", err);
                    }
                });
    }

    @Override
    public Flux<OUT> processMany(ChainContext<IN> context) {
        Publisher<OUT> publisher =
                Process.from(this)
                        .onHandle((node, nextChain) -> node.executeMany(nextChain, context))
                        .onEmpty(Flux.empty())
                        .onErrorContinue(chain -> chain.processMany(context))
                        .onProcess();
        return Flux.from(publisher)
                .onErrorContinue((err, o) -> {
                    if (log.isWarnEnabled()) {
                        log.warn("Chain execute error", err);
                    }
                });
    }

    /**
     * Adapt reactor framework {@link Mono} And {@link Flux} to {@link Chain}
     *
     * @param <IN>  the chain input type
     * @param <OUT> the chain out type
     */
    static class Process<IN, OUT> implements Self<Process<IN, OUT>> {

        private final DefaultChain<IN, OUT> chain;

        OnHandle<IN, OUT> onHandle;
        OnErrorContinue<IN, OUT> onErrorContinue;
        Publisher<OUT> empty;

        public Process(DefaultChain<IN, OUT> chain) {
            this.chain = chain;
        }

        /**
         * handle {@link Node#execute(Chain, ChainContext)} or {@link Node#executeMany(Chain, ChainContext)}
         *
         * @param onHandle the {@link OnHandle} instance
         * @return self
         * @see OnHandle
         */
        public Process<IN, OUT> onHandle(@NonNull OnHandle<IN, OUT> onHandle) {
            this.onHandle = onHandle;
            return self();
        }

        /**
         * on existing throwing exception invoking. the invoker should be {@link Chain#proceed(ChainContext)} or {@link Chain#processMany(ChainContext)}
         *
         * @param onErrorContinue the {@link OnErrorContinue} instance
         * @return self
         */
        public Process<IN, OUT> onErrorContinue(@NonNull OnErrorContinue<IN, OUT> onErrorContinue) {
            this.onErrorContinue = onErrorContinue;
            return self();
        }

        /**
         * if Chain nodes is empty return current {@link Publisher}
         *
         * @param empty the {@link Publisher} instance
         * @return self
         */
        public Process<IN, OUT> onEmpty(@NonNull Publisher<OUT> empty) {
            this.empty = empty;
            return self();
        }

        /**
         * trigger process and return {@link Publisher}
         *
         * @return the {@link Publisher} instance
         */
        public Publisher<OUT> onProcess() {
            int index = chain.index;
            List<? extends Node<IN, OUT>> nodes = chain.nodes;
            if (index < nodes.size()) {
                Node<IN, OUT> node = nodes.get(index);
                DefaultChain<IN, OUT> nextChain = new DefaultChain<>(chain, index + 1);
                Publisher<OUT> out;
                try {
                    out = onHandle.apply(node, nextChain);
                } catch (Throwable ex) {
                    if (log.isWarnEnabled()) {
                        log.warn("execute node error", ex);
                    }
                    // 避免后续结点不能执行
                    out = onErrorContinue.apply(nextChain);
                }
                return out;
            } else {
                return empty;
            }
        }

        /**
         * static method create {@link Process} instance
         *
         * @param chain the {@link Chain} instance. not null
         * @param <IN>  the chain in type
         * @param <OUT> the chain out type
         * @return the {@link Process} instance
         */
        public static <IN, OUT> Process<IN, OUT> from(@NonNull DefaultChain<IN, OUT> chain) {
            return new Process<>(chain);
        }
    }

    @FunctionalInterface
    public interface OnHandle<IN, OUT> extends ThrowingMethodBiFunction<Node<IN, OUT>, Chain<IN, OUT>, Publisher<OUT>> {
    }

    @FunctionalInterface
    public interface OnErrorContinue<IN, OUT> extends Function<Chain<IN, OUT>, Publisher<OUT>> {
    }

    @Override
    public List<? extends Node<IN, OUT>> getNodes() {
        return nodes;
    }
}
