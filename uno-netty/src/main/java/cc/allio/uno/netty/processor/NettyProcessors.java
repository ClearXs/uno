package cc.allio.uno.netty.processor;

import cc.allio.uno.netty.concurrent.NamedThreadFactory;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * netty处理器的常量池
 *
 * @author j.x
 * @since 1.0
 */
public class NettyProcessors {

    private NettyProcessors() {
    }

    private static final ExecutorService DEFAULT_EXECUTORS = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS, new SynchronousQueue<>(), new NamedThreadFactory("business"));

    public static ExecutorService defaultExec() {
        return DEFAULT_EXECUTORS;
    }

    public static Tuple2<NettyProcessor, ExecutorService> simpleProcess() {
        return Tuples.of(new SimpleNettyProcessor(), DEFAULT_EXECUTORS);
    }

    public static Tuple2<NettyProcessor, ExecutorService> failProcess(Throwable cause) {
        return Tuples.of(new NettyFastFailProcessor(cause), DEFAULT_EXECUTORS);
    }

}
