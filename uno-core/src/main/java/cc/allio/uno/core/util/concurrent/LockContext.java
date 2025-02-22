package cc.allio.uno.core.util.concurrent;

import cc.allio.uno.core.util.map.OptionalMap;
import cc.allio.uno.core.api.Self;
import cc.allio.uno.core.exception.Exceptions;
import cc.allio.uno.core.function.VoidConsumer;
import cc.allio.uno.core.function.lambda.ThrowingMethodConsumer;
import cc.allio.uno.core.function.lambda.ThrowingMethodFunction;
import cc.allio.uno.core.function.lambda.ThrowingMethodSupplier;
import cc.allio.uno.core.function.lambda.ThrowingMethodVoid;
import cc.allio.uno.core.util.CollectionUtils;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

/**
 * 基于{@link java.util.concurrent.locks.ReentrantLock}的函数式锁上下文的类。
 * <p>在其作用域内部的的动作都会进行加锁</p>
 * <p>其内部的的action返回的参数都会放在进行存放</p>
 * <p>值得注意的是：{@link #thenApply(ThrowingMethodSupplier)}返回的值优先级是最高的</p>
 *
 * @author j.x
 * @since 1.1.7
 */
@Slf4j
public class LockContext implements Self<LockContext>, OptionalMap<String> {

    static final Lock PUBLICITY_LOCK = new ReentrantLock();

    private final InternalParameterContext optionalContext;
    private final Lock lock;

    // 延迟赋值
    private Queue<ThrowingMethodVoid> anonymous;
    private Queue<ThrowingMethodConsumer<OptionalMap<String>>> actions;
    private Queue<ThrowingMethodFunction<OptionalMap<String>, Object>> functions;
    // 提供值优先级最高
    private Queue<ThrowingMethodSupplier<Object>> suppliers;

    private ThrowingMethodConsumer<LockContext> startOf;
    private ThrowingMethodConsumer<LockContext> endOf;

    AtomicBoolean released = new AtomicBoolean(false);

    // 错误集合
    @Getter
    private final Queue<Throwable> errs;

    LockContext() {
        this(Collections.emptyMap());
    }

    LockContext(Lock lock) {
        this(Collections.emptyMap(), lock);
    }

    LockContext(Map<String, Object> parameter) {
        this(parameter, PUBLICITY_LOCK);
    }

    LockContext(Map<String, Object> parameter, Lock lock) {
        this.optionalContext = new InternalParameterContext();
        this.optionalContext.putAll(parameter);
        this.lock = Objects.requireNonNullElseGet(lock, ReentrantLock::new);
        this.errs = Queues.newConcurrentLinkedQueue();
    }

    /**
     * 创建 LockContext实例
     *
     * @return LockContext
     */
    public static LockContext lock() {
        return new LockContext();
    }

    /**
     * 创建 LockContext实例
     *
     * @return LockContext
     */
    public static LockContext lock(Map<String, Object> parameter) {
        return new LockContext(parameter);
    }


    /**
     * 创建 LockContext实例
     *
     * @return LockContext
     */
    public static LockContext lock(Lock lock) {
        return new LockContext(lock);
    }


    /**
     * 创建 LockContext实例
     *
     * @return LockContext
     */
    public static LockContext lock(Map<String, Object> parameter, Lock lock) {
        return new LockContext(parameter, lock);
    }

    /**
     * 创建 LockContext实例
     *
     * @return LockContext
     */
    public static LockContext lock(ThrowingMethodConsumer<LockContext> lockStatOf) {
        LockContext lockContext = new LockContext();
        lockContext.startOf = lockStatOf;
        return lockContext;
    }

    /**
     * 创建 LockContext实例
     *
     * @return LockContext
     */
    public static LockContext lock(ThrowingMethodConsumer<LockContext> lockStatOf, Lock lock) {
        LockContext lockContext = new LockContext(lock);
        lockContext.startOf = lockStatOf;
        return lockContext;
    }

    /**
     * 添加动作 <b>如果操作内发生异常则向外抛出</b>
     *
     * @param acceptor acceptor
     * @return LockContext
     */
    public LockContext then(ThrowingMethodVoid acceptor) {
        if (CollectionUtils.isEmpty(anonymous)) {
            this.anonymous = Queues.newConcurrentLinkedQueue();
        }
        this.anonymous.add(acceptor);
        return self();
    }

    /**
     * 添加动作 <b>如果操作内发生异常则向外抛出</b>
     *
     * @param acceptor acceptor
     * @return LockContext
     */
    public LockContext then(ThrowingMethodConsumer<OptionalMap<String>> acceptor) {
        if (CollectionUtils.isEmpty(actions)) {
            this.actions = Queues.newConcurrentLinkedQueue();
        }
        this.actions.add(acceptor);
        return self();
    }

    /**
     * 加锁后继续执行，不立即执行。<b>如果操作内发生异常则向外抛出</b>
     *
     * @param supplier supplier
     * @return LockContext
     */
    public LockContext thenApply(ThrowingMethodSupplier<Object> supplier) {
        if (CollectionUtils.isEmpty(suppliers)) {
            this.suppliers = Queues.newConcurrentLinkedQueue();
        }
        this.suppliers.add(supplier);
        return self();
    }

    /**
     * 加锁后继续执行，不立即执行。<b>如果操作内发生异常则向外抛出</b>
     *
     * @param func func
     * @return LockContext
     */
    public LockContext thenApply(ThrowingMethodFunction<OptionalMap<String>, Object> func) {
        if (CollectionUtils.isEmpty(functions)) {
            this.functions = Queues.newConcurrentLinkedQueue();
        }
        this.functions.add(func);
        return self();
    }

    /**
     * @see #lockReturn(ThrowingMethodSupplier, boolean)
     */
    public <V> LockResult<V> lockReturn(ThrowingMethodSupplier<V> supplier) {
        return lockReturn(supplier, false);
    }

    /**
     * 加锁后直接返回。在内部调用{@link #doRelease()}操作，把缓存的操作执行。
     *
     * @param supplier supplier
     * @param <V>      返回值类型
     * @return v
     */
    public <V> LockResult<V> lockReturn(ThrowingMethodSupplier<V> supplier, boolean immediate) {
        if (startOf != null) {
            tryLockAndUnlock(() -> catchingThat(startOf));
        }
        Supplier<V> delayer =
                () -> tryLockAndUnlock(
                        () -> {
                            doRelease();
                            return catching(supplier);
                        },
                        () -> {
                            if (endOf != null) {
                                catchingThat(endOf);
                            }
                            released.set(true);
                        });
        if (immediate) {
            V result = delayer.get();
            return new LockResult<>(result, this);
        } else {
            return new LockResult<>(delayer, this);
        }
    }

    @Override
    public boolean remove(String key) {
        return optionalContext.remove(key);
    }

    /**
     * 执行完操作之后的执行
     *
     * @param endOf endOf
     * @return LockContext
     */
    public LockContext lockEnd(ThrowingMethodConsumer<LockContext> endOf) {
        this.endOf = endOf;
        return self();
    }

    /**
     * 释放
     *
     * @return {@link LockResult}实例
     * @see #release()
     */
    public <V> LockResult<V> release() {
        return release(() -> {
        });
    }

    /**
     * release
     *
     * @see #release(ThrowingMethodConsumer, boolean)
     */
    public <V> LockResult<V> release(ThrowingMethodVoid endOf) {
        return release(context -> endOf.accept(), false);
    }

    /**
     * release
     *
     * @see #release(ThrowingMethodConsumer, boolean)
     */
    public <V> LockResult<V> release(ThrowingMethodConsumer<LockContext> endOf) {
        return release(endOf, false);
    }

    /**
     * 按照 start - {@link #doRelease()}（根据参数判定是否立即需要执行） - end顺序执行，并最后基于{@link InternalParameterContext#getPreviousValue()}获取结果集。
     *
     * @param endOf     last execute
     * @param immediate 是否立即执行
     * @param <V>       期望的返回值类型
     * @return {@link LockResult}实例
     */
    public <V> LockResult<V> release(ThrowingMethodConsumer<LockContext> endOf, boolean immediate) {
        if (endOf != null) {
            lockEnd(endOf);
        }

        if (startOf != null) {
            tryLockAndUnlock(() -> {
                try {
                    startOf.accept(this);
                } catch (Throwable ex) {
                    errs.offer(ex);
                }
            });
        }
        // 延迟器
        Supplier<V> delayer =
                () -> {
                    if (endOf != null) {
                        tryLockAndUnlock(() -> catchingThat(endOf), this::doRelease);
                    } else {
                        doRelease();
                    }
                    V result = null;
                    Object previousValue = optionalContext.getPreviousValue();
                    if (previousValue != null) {
                        result = catching(() -> (V) previousValue);
                    }
                    released.set(true);
                    return result;
                };
        if (immediate) {
            V result = delayer.get();
            return new LockResult<>(result, this);
        } else {
            return new LockResult<>(delayer, this);
        }
    }

    /**
     * 触发缓存的{@link #anonymous}、{@link #actions}、{@link #functions}、{@link #suppliers}操作
     */
    void doRelease() {
        // 非值锁操作
        poll(anonymous, this::catching);
        poll(actions, this::catching);
        // 值锁操作
        // function
        poll(functions, action -> {
            Object value = catching(action);
            if (value != null) {
                optionalContext.put(value);
                optionalContext.putPreviousValue(value);
            }
        });
        // supplier
        poll(suppliers, action -> {
            Object value = catching(action);
            if (value != null) {
                optionalContext.put(value);
                optionalContext.putPreviousValue(value);
            }
        });
    }

    /**
     * 从给定队列里面加锁获取值
     * <p>safe method</p>
     * <p>如果发生结果则理解结束</p>
     *
     * @param queue    queue
     * @param acceptor 如果队列有值的化进行回调
     * @param <V>      队列值的类型
     */
    public <V> void poll(Queue<V> queue, ThrowingMethodConsumer<V> acceptor) {
        // 非值锁操作
        if (CollectionUtils.isNotEmpty(queue)) {
            tryLockAndUnlock(() -> {
                V action;
                while ((action = queue.poll()) != null) {
                    try {
                        acceptor.accept(action);
                    } catch (Throwable ex) {
                        // ignore
                        log.error("from queue handle each value has err", ex);
                        break;
                    }
                }
            });
        }
    }

    /**
     * 优化 try-lock-finally-unlock
     *
     * @param acceptor acceptor
     */
    void tryLockAndUnlock(VoidConsumer acceptor) {
        tryLockAndUnlock(acceptor, null);
    }

    /**
     * 优化 try-lock-finally-unlock
     *
     * @param actor    actor
     * @param finisher 在finally语句执行的语句
     */
    void tryLockAndUnlock(VoidConsumer actor, VoidConsumer finisher) {
        if (actor != null) {
            lock.lock();
            try {
                actor.doAccept();
            } finally {
                if (finisher != null) {
                    finisher.doAccept();
                }
                lock.unlock();
            }
        }
    }

    /**
     * 优化 try-lock-finally-unlock
     *
     * @param actor    actor
     * @param finisher 在finally语句执行的语句
     */
    <V> V tryLockAndUnlock(Supplier<V> actor, VoidConsumer finisher) {
        if (actor != null) {
            lock.lock();
            try {
                return actor.get();
            } finally {
                if (finisher != null) {
                    finisher.doAccept();
                }
                lock.unlock();
            }
        }
        return null;
    }

    /**
     * 捕获当Acceptor发生的异常
     */
    <V> V catching(ThrowingMethodSupplier<V> throwingSupplier) {
        try {
            return throwingSupplier.get();
        } catch (Throwable ex) {
            errs.offer(ex);
        }
        return null;
    }

    /**
     * 捕获当Acceptor发生的异常
     */
    void catching(ThrowingMethodVoid throwingAcceptor) {
        try {
            throwingAcceptor.accept();
        } catch (Throwable ex) {
            errs.offer(ex);
        }
    }

    /**
     * 捕获当Acceptor发生的异常
     */
    void catching(ThrowingMethodConsumer<OptionalMap<String>> throwingAcceptor) {
        try {
            throwingAcceptor.accept(optionalContext);
        } catch (Throwable ex) {
            errs.offer(ex);
        }
    }

    /**
     * 捕获当Func发生的异常
     */
    <K> K catching(ThrowingMethodFunction<OptionalMap<String>, K> throwingFunc) {
        try {
            return throwingFunc.apply(optionalContext);
        } catch (Throwable ex) {
            errs.offer(ex);
        }
        return null;
    }

    /**
     * 捕获当前对象的异常
     */
    void catchingThat(ThrowingMethodConsumer<LockContext> throwingAcceptor) {
        try {
            throwingAcceptor.accept(this);
        } catch (Throwable ex) {
            errs.offer(ex);
        }
    }

    @Override
    public Optional<Object> get(String key) {
        return optionalContext.get(key);
    }

    @Override
    public void put(String key, Object obj) {
        optionalContext.put(key, obj);
    }

    @Override
    public Optional<ApplicationContext> getApplicationContext() {
        return optionalContext.getApplicationContext();
    }

    @Override
    public Map<String, Object> getAll() {
        return optionalContext.getAll();
    }

    /**
     * 是否进行释放值
     */
    boolean isReleased() {
        return released.get();
    }

    public static class InternalParameterContext implements OptionalMap<String> {

        private final Map<String, Object> parameter = Maps.newConcurrentMap();
        // 记录上一步执行的函数名称
        private Object previousObject;

        @Override
        public Optional<Object> get(String key) {
            return Optional.ofNullable(parameter.get(key));
        }

        @Override
        public void put(String key, Object obj) {
            parameter.put(key, obj);
        }

        @Override
        public boolean remove(String key) {
            return parameter.remove(key) != null;
        }

        @Override
        public Optional<ApplicationContext> getApplicationContext() {
            throw Exceptions.unOperate("getApplicationContext");
        }

        @Override
        public Map getAll() {
            return parameter;
        }

        void putPreviousValue(Object value) {
            this.previousObject = value;
        }

        /**
         * 获取上一步的操作的返回参数
         *
         * @return value
         */
        public Object getPreviousValue() {
            return previousObject;
        }
    }
}
