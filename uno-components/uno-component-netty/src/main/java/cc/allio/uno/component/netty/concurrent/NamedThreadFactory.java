package cc.allio.uno.component.netty.concurrent;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * screw
 *
 * @author jiangw
 * @date 2020/12/10 17:28
 * @since 1.0
 */
public class NamedThreadFactory implements ThreadFactory {

    private static final AtomicInteger THREAD_COUNT = new AtomicInteger(0);

    private final String name;

    private final boolean isDemon;

    public NamedThreadFactory(String name) {
        this(name, false);
    }

    public NamedThreadFactory(String name, boolean isDemon) {
        this.name = name;
        this.isDemon = isDemon;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r, name + "-" + THREAD_COUNT.getAndIncrement());
        thread.setDaemon(isDemon);
        return thread;
    }
}
