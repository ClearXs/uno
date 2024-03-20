package cc.allio.uno.core.chain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 默认实现链，参考自Spring构建网关过滤器链
 *
 * @author j.x
 * @date 2022/8/22 18:51
 * @since 1.0
 */
@Slf4j
public class DefaultChain<IN, OUT> implements Chain<IN, OUT> {

    /**
     * 排好序的结点集合
     */
    private final List<? extends Node<IN, OUT>> nodes;

    /**
     * 记录当前链执行到的结点的索引
     */
    private final int index;

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
        return Mono.defer(() -> {
            if (index < nodes.size()) {
                Node<IN, OUT> node = nodes.get(index);
                DefaultChain<IN, OUT> nextChain = new DefaultChain<>(this, this.index + 1);
                Mono<OUT> out;
                try {
                    out = node.execute(nextChain, context);
                } catch (Throwable ex) {
                    if (log.isWarnEnabled()) {
                        log.warn("execute node error", ex);
                    }
                    // 避免后续结点不能执行
                    out = nextChain.proceed(context);
                }
                return out.onErrorContinue((err, o) -> {
                    if (log.isWarnEnabled()) {
                        log.warn("Chain execute error", err);
                    }
                });
            } else {
                return Mono.empty();
            }
        });
    }

    @Override
    public List<? extends Node<IN, OUT>> getNodes() {
        return nodes;
    }
}
