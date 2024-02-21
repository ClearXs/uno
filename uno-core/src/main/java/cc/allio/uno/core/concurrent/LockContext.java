package cc.allio.uno.core.concurrent;

import cc.allio.uno.core.api.OptionalContext;
import cc.allio.uno.core.api.Self;
import cc.allio.uno.core.exception.Exceptions;
import cc.allio.uno.core.function.ConsumerAction;
import cc.allio.uno.core.function.VoidConsumer;
import com.google.common.collect.Maps;
import org.springframework.context.ApplicationContext;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 基于{@link java.util.concurrent.locks.ReentrantLock}的函数式锁上下文的类。
 * <p>在其作用域内部的的动作都会进行加锁</p>
 * <p>其内部的的action返回的参数都会放在进行存放</p>
 *
 * @author jiangwei
 * @date 2024/2/6 20:17
 * @since 1.1.6
 */
public class LockContext implements Self<LockContext>, OptionalContext {

    private final InternalParameterContext optionalContext;
    private final Lock lock;

    private final LinkedList<ConsumerAction<OptionalContext>> actions;
    private final LinkedList<Function<OptionalContext, Object>> functions;

    private Consumer<LockContext> startOf;
    private Consumer<LockContext> endOf;

    LockContext() {
        this(Collections.emptyMap());
    }

    LockContext(Map<String, Object> parameter) {
        this.optionalContext = new InternalParameterContext();
        this.optionalContext.putAll(parameter);
        this.lock = new ReentrantLock();
        this.actions = new LinkedList<>();
        this.functions = new LinkedList<>();
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
    public static LockContext lock(Consumer<LockContext> lockStatOf) {
        LockContext lockContext = new LockContext();
        lockContext.startOf = lockStatOf;
        return lockContext;
    }

    /**
     * 添加动作 <b>如果操作内发生异常则向外抛出</b>
     *
     * @param acceptor acceptor
     * @return LockContext
     */
    public LockContext then(ConsumerAction<OptionalContext> acceptor) {
        this.actions.add(acceptor);
        return self();
    }

    /**
     * 加锁后继续执行，不返回任何值。<b>如果操作内发生异常则向外抛出</b>
     *
     * @param func func
     * @return LockContext
     */
    public LockContext then(Function<OptionalContext, Object> func) {
        this.functions.add(func);
        return self();
    }

    /**
     * 加锁后直接返回，该操作是一个终止操作，会把之前加入缓存的操作一并执行。<b>如果操作内发生异常则向外抛出</b>
     *
     * @param func func
     * @param <V>  返回值类型
     * @return v
     */
    public <V> V lockReturn(Function<OptionalContext, V> func) {
        // 初始
        if (startOf != null) {
            tryLockAndUnlock(() -> startOf.accept(this));
        }
        return tryLockAndUnlock(
                () -> {
                    doRelease();
                    return func.apply(optionalContext);
                },
                () -> {
                    if (endOf != null) {
                        endOf.accept(this);
                    }
                });
    }

    /**
     * 设置参数
     *
     * @param key   key
     * @param value value
     * @return LockContext
     */
    public LockContext put(String key, Object value) {
        this.actions.add(context -> this.putAttribute(key, value));
        return self();
    }

    /**
     * 执行完操作之后的执行
     *
     * @param endOf endOf
     * @return LockContext
     */
    public LockContext lockEnd(Consumer<LockContext> endOf) {
        this.endOf = endOf;
        return self();
    }

    /**
     * 释放
     */
    public void release() {
        release(null);
    }

    /**
     * 释放
     */
    public void release(Consumer<LockContext> endOf) {
        lockEnd(endOf);
        if (startOf != null) {
            tryLockAndUnlock(() -> startOf.accept(this));
        }
        if (endOf != null) {
            tryLockAndUnlock(() -> endOf.accept(this), this::doRelease);
        } else {
            doRelease();
        }
    }

    /**
     * 触发缓存的{@link #actions}、{@link #functions}操作
     */
    private void doRelease() {
        // 非值锁操作
        tryLockAndUnlock(() -> {
            ConsumerAction<OptionalContext> action;
            while ((action = actions.pollFirst()) != null) {
                action.accept(optionalContext);
            }
        });
        // 值锁操作
        tryLockAndUnlock(() -> {
            Function<OptionalContext, Object> func;
            while ((func = functions.pollFirst()) != null) {
                Object value = func.apply(optionalContext);
                optionalContext.putPreviousValue(value);
            }
        });
    }

    /**
     * 优化 try-lock-finally-unlock
     *
     * @param acceptor acceptor
     */
    private void tryLockAndUnlock(VoidConsumer acceptor) {
        tryLockAndUnlock(acceptor, null);
    }

    /**
     * 优化 try-lock-finally-unlock
     *
     * @param actor    actor
     * @param finisher 在finally语句执行的语句
     */
    private void tryLockAndUnlock(VoidConsumer actor, VoidConsumer finisher) {
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
    private <V> V tryLockAndUnlock(Supplier<V> actor, VoidConsumer finisher) {
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

    @Override
    public Optional<Object> get(String key) {
        return optionalContext.get(key);
    }

    @Override
    public void putAttribute(String key, Object obj) {
        optionalContext.putAttribute(key, obj);
    }

    @Override
    public Optional<ApplicationContext> getApplicationContext() {
        return optionalContext.getApplicationContext();
    }

    @Override
    public Map<String, Object> getAll() {
        return optionalContext.getAll();
    }

    public static class InternalParameterContext implements OptionalContext {

        private final Map<String, Object> parameter = Maps.newConcurrentMap();
        // 记录上一步执行的函数名称
        private Object previousObject;

        @Override
        public Optional<Object> get(String key) {
            return Optional.ofNullable(parameter.get(key));
        }

        @Override
        public void putAttribute(String key, Object obj) {
            parameter.put(key, obj);
        }

        @Override
        public Optional<ApplicationContext> getApplicationContext() {
            throw Exceptions.unOperate("getApplicationContext");
        }

        @Override
        public Map<String, Object> getAll() {
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
