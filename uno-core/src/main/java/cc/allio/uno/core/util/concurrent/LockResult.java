package cc.allio.uno.core.util.concurrent;

import cc.allio.uno.core.api.OptionalContext;
import cc.allio.uno.core.api.Self;
import cc.allio.uno.core.exception.Exceptions;
import cc.allio.uno.core.function.lambda.ThrowingMethodBiConsumer;
import cc.allio.uno.core.function.lambda.ThrowingMethodConsumer;
import lombok.extern.slf4j.Slf4j;

import java.util.Queue;
import java.util.function.Supplier;

/**
 * 对{@link LockContext#release()}返回的结果集对象，提供类似Rust的Result封装。
 * <p><b>非常重要的是，如果通过{@link LockContext#release()}不是立即释放值的话，只有调用终止操作（即获取值的操作）才能获取值</b></p>
 *
 * @author j.x
 * @since 1.1.7
 */
@Slf4j
public class LockResult<T> implements Self<LockResult<T>> {

    T result;
    Supplier<T> delayer;
    MultiCheckedException multiCheckedException;
    LockContext lockContext;

    public LockResult(T result, LockContext lockContext) {
        this.result = result;
        init(lockContext);
    }

    public LockResult(Supplier<T> delayer, LockContext lockContext) {
        this.delayer = delayer;
        init(lockContext);
    }

    void init(LockContext lockContext) {
        this.multiCheckedException = new MultiCheckedException();
        this.lockContext = lockContext;
        loadError();
    }

    /**
     * 获取结果进行处理。
     * <b>terminate operator</b>
     *
     * @param acceptor acceptor
     * @return self
     */
    public LockResult<T> ok(ThrowingMethodBiConsumer<OptionalContext, T> acceptor) {
        tryReleaseValue();
        try {
            acceptor.accept(lockContext, result);
        } catch (Throwable ex) {
            // ignore
            log.error("lock result handle 'ok' method has err", ex);
        }
        return self();
    }

    /**
     * 如果执行过程发生异常，则对错误进行处理。
     * <b>terminate operator</b>
     *
     * @param inspector inspector
     * @return self
     */
    public LockResult<T> err(ThrowingMethodConsumer<MultiCheckedException> inspector) {
        tryReleaseValue();
        try {
            inspector.accept(multiCheckedException);
        } catch (Throwable ex) {
            // ignore
            log.error("lock result handle 'err' method has err", ex);
        }
        return self();
    }

    /**
     * 如果执行过程发生异常，则对错误进行处理。
     * <b>terminate operator</b>
     *
     * @param errType   errType
     * @param inspector inspector
     * @param <E>       错误类型
     * @return self
     */
    public <E extends Throwable> LockResult<T> err(Class<E> errType, ThrowingMethodConsumer<E> inspector) {
        tryReleaseValue();
        E checkedException = multiCheckedException.findCheckedException(errType);
        try {
            inspector.accept(checkedException);
        } catch (Throwable ex) {
            // ignore
            log.error("lock result handle 'err' method has err", ex);
        }
        return self();
    }

    /**
     * 获取结果，如果在执行过程中发生异常，先获取未受检查异常，如果没有则对错误进行报装抛出未受检查的异常。
     * <b>terminate operator</b>
     *
     * @return result
     * @throws RuntimeException 执行过程发生异常抛出
     */
    public T unchecked() throws RuntimeException {
        tryReleaseValue();
        if (multiCheckedException.hasErr()) {
            RuntimeException runtimeException = multiCheckedException.peekUncheckException();
            if (runtimeException != null) {
                throw runtimeException;
            }
            throw Exceptions.unchecked(multiCheckedException);
        }
        return result;
    }

    /**
     * 默认未包装的获取结果的方法，抛出MultiCheckedException
     * <b>terminate operator</b>
     *
     * @return result
     * @throws MultiCheckedException 如果有错误则抛出
     */
    public T unwrap() throws MultiCheckedException {
        tryReleaseValue();
        if (multiCheckedException.hasErr()) {
            throw multiCheckedException;
        }
        return result;
    }

    /**
     * 基于错误处理器返回结果
     * <b>terminate operator</b>
     *
     * @param errHandler 如果存在错误则调用
     * @return result
     */
    public T unwrapOrElse(ThrowingMethodConsumer<MultiCheckedException> errHandler) {
        tryReleaseValue();
        if (multiCheckedException.hasErr() && errHandler != null) {
            try {
                errHandler.accept(multiCheckedException);
            } catch (Throwable ex) {
                // ignore
                log.error("lock result handle 'unwrapOrElse' method has err", ex);
            }
        }
        return result;
    }

    /**
     * 未加任何包装的获取结果的方法，如果在执行过程发生错误，则进行抛出。
     * <b>terminate operator</b>
     *
     * @param errType 错误类型Class实例
     * @param <E>     错误类型
     * @return result
     * @throws E                如果找到指定的错误则抛出
     * @throws RuntimeException 如果没有找到的指定错误，则抛出该异常
     * @see #unchecked()
     */
    public <E extends Throwable> T unwrap(Class<E> errType) throws E {
        tryReleaseValue();
        if (multiCheckedException.hasErr()) {
            E checkedException = multiCheckedException.findCheckedException(errType);
            if (checkedException != null) {
                throw checkedException;
            }
            return unchecked();
        }
        return result;
    }

    /**
     * 如果给定的错误类型存在，则不进行抛出，否则抛出未受检查的异常。
     * <b>terminate operator</b>
     *
     * @param errType errType
     * @param <E>     异常类型
     * @return T
     */
    public <E extends Throwable> T except(Class<E> errType) {
        tryReleaseValue();
        if (multiCheckedException.hasErr() && !multiCheckedException.hasCheckedException(errType)) {
            return unchecked();
        }
        return result;
    }

    /**
     * 当发现result不存在时并且{@link LockContext}并未释放时则尝试调用{@link #delayer}获取值
     */
    void tryReleaseValue() {
        if (result == null && !lockContext.isReleased()) {
            this.result = delayer.get();
            // 在延迟执行过程中，可能存在的异常还未加入其中，故再次load err
            loadError();
        }
    }

    // 从lock context中加载error
    void loadError() {
        Queue<Throwable> errs = lockContext.getErrs();
        Throwable ex;
        while ((ex = errs.poll()) != null) {
            multiCheckedException.appendException(ex);
        }
    }
}
