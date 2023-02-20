package cc.allio.uno.component.flink.function.sink;

import cc.allio.uno.component.flink.Output;
import cc.allio.uno.component.flink.function.BaseCountDownRichSinkFunction;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.configuration.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.io.Serializable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

/**
 * 通过{@link FluxSink#next(Object)}来输出数据,订阅{@link Flux#subscribe(Consumer)}来获取最终计算的结果</br>
 * 结果存入{@link BlockingQueue}中，它是容量只有1的阻塞队列，也就说，数据进入会被立即消耗，如果生成着速率过快将会导致生产者进行阻塞
 *
 * @author jiangwei
 * @date 2022/2/23 13:57
 * @since 1.0
 */
@Slf4j
@Deprecated
public class FluxSinkRichSinkFunction<C> extends BaseCountDownRichSinkFunction<C> implements Output<C>, Serializable {

    private static FluxSink downstream;
    private static BlockingQueue sinkOut;

    @Override
    public void afterOpen(Configuration parameters) {
        Flux<C> creator = Flux.create(sink -> downstream = sink);
        sinkOut = new LinkedBlockingQueue<>(1);
        creator.subscribe(v -> {
            try {
                sinkOut.put(v);
            } catch (InterruptedException e) {
                log.error("calculate result push blocking queue failed", e);
            }
        });
    }

    @Override
    public void invoke(C value, Context context) throws Exception {
        if (downstream != null) {
            downstream.next(value);
        }
    }

    @Override
    public C output() throws InterruptedException {
        return (C) sinkOut.take();
    }

}
