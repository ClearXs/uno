package cc.allio.uno.component.netty;

import cc.allio.uno.core.util.Collections;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class NettyChannelGroup implements ChannelGroup {

    /**
     * 通道组容器
     */
    private CopyOnWriteArrayList<Channel> channels;

    /**
     * 下一取走通道的序列位置
     */
    private final AtomicInteger index;

    /**
     * 实现通道可用的判断
     */
    private final ReentrantLock lock = new ReentrantLock();

    private final Condition condition = lock.newCondition();

    ChannelFutureListener channelRemovedListener = future -> remove(future.channel());

    public NettyChannelGroup() {
        channels = new CopyOnWriteArrayList<>();
        index = new AtomicInteger(0);
    }

    @Override
    public Channel next() {
        int size = size();
        for (; ; ) {
            if (size == 0) {
                // 可能出现假连接的情况
                if (waitForAvailable(1000)) {
                    continue;
                }
                throw new IllegalArgumentException("channels group is empty");
            }
            if (size == 1) {
                return channels.get(0);
            }
            int offset = index.getAndIncrement() & Integer.MAX_VALUE;
            return channels.get(offset % size);
        }
    }

    @Override
    public boolean add(Channel channel) {
        boolean added = channels.add(channel);
        if (added) {
            // 当前通道添加成功，添加通道关闭事件，通知等待的线程结束等待
            channel.closeFuture().addListener(channelRemovedListener);
            lock.lock();
            try {
                condition.signalAll();
            } finally {
                lock.unlock();
            }
        }
        return added;
    }

    @Override
    public void remove(Channel channel) {
        if (channels != null) {
            channels.remove(channel);
        }
    }

    @Override
    public void removeAll() throws InterruptedException {
        if (Collections.isNotEmpty(channels)) {
            for (Channel channel : channels) {
                // 阻塞关闭
                channel.closeFuture();
            }
            // help 'GC'
            channels = null;
        }
    }

    @Override
    public boolean isAvailable() {
        return !channels.isEmpty();
    }

    @Override
    public int size() {
        return channels.size();
    }

    /**
     * 等待指定时间通道的可用
     *
     * @param millis
     */
    private boolean waitForAvailable(long millis) {
        boolean available = isAvailable();
        if (available) {
            return true;
        }
        long waitNanos = TimeUnit.MILLISECONDS.toNanos(millis);
        lock.lock();
        try {
            // 当前线程超时等待，直到由通道可用，或者超时
            while (!(available = isAvailable())) {
                if ((waitNanos = condition.awaitNanos(waitNanos)) <= 0) {
                    break;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return available;
    }
}
