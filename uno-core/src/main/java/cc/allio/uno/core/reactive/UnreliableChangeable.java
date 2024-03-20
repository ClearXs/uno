package cc.allio.uno.core.reactive;

import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.Comparator;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 指定两个数据进行比较，判断值是否发生变化，它是不可靠的.他会创建一个原子变量来表示这个数据发生变化与否
 *
 * @author j.x
 * @date 2022/7/7 19:31
 * @since 1.0
 */
@Slf4j
public class UnreliableChangeable<T> {

    /**
     * 不可靠变化
     */
    private final AtomicBoolean unreliable;

    private FluxSink<T> emmit;

    private final Disposable disposable;

    private UnreliableChangeable(Comparator<T> comparator) {
        this.unreliable = new AtomicBoolean(false);
        this.disposable = Flux.<T>create(sink -> emmit = sink)
                .window(2, 1)
                .flatMap(Flux::collectList)
                .subscribe(accumulate -> {
                    int compare;
                    try {
                        compare = comparator.compare(accumulate.get(0), accumulate.get(1));
                    } catch (NullPointerException e) {
                        log.error("Comparator exist Null data", e);
                        compare = -1;
                    }
                    unreliable.set(compare != 0);
                });
        emmit.onDispose(disposable::dispose);
    }

    /**
     * 获取是否发生变化
     *
     * @return
     */
    public Boolean get() {
        return unreliable.get();
    }

    /**
     * 发布数据
     *
     * @param data 数据源
     */
    public void publish(T data) {
        emmit.next(data);
    }

    /**
     * 停止
     */
    public void onStop() {
        disposable.dispose();
    }

    /**
     * 创建AutoBetweenChangeableTask实例
     *
     * @param comparator 比较器实例
     * @return 新的实例对象
     */
    static <T> UnreliableChangeable<T> newInstance(Comparator<T> comparator) {
        return new UnreliableChangeable<>(comparator);
    }
}
