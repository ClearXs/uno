package cc.allio.uno.core.concurrent;

import cc.allio.uno.core.api.Single;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * 多受检查异常
 *
 * @author j.x
 * @date 2024/2/29 23:56
 * @since 1.1.7
 */
public class MultiCheckedException extends Exception {

    // 受检查的异常
    final transient Map<Class<? extends Throwable>, Throwable> checked = Maps.newHashMap();
    final transient Map<Class<? extends RuntimeException>, RuntimeException> unchecked = Maps.newHashMap();

    /**
     * 追加异常
     *
     * @param ex  ex
     * @param <T> 受检查的类型
     */
    public <T extends Throwable> void appendException(T ex) {
        if (ex instanceof RuntimeException runtimeException) {
            unchecked.put(runtimeException.getClass(), runtimeException);
        } else {
            checked.put(ex.getClass(), ex);
        }
    }

    /**
     * 根据错误类型获取受检查的异常
     *
     * @param errType errType
     * @param <T>     错误类型
     * @return Exception or null
     */
    public <T extends RuntimeException> T findUncheckedException(Class<T> errType) {
        if (hasUncheckedException(errType)) {
            return (T) unchecked.get(errType);
        }
        var err = Single.from(unchecked.entrySet())
                .forReturn(errEntry -> errType.isAssignableFrom(errEntry.getKey()))
                .map(Map.Entry::getValue)
                .orElse(null);

        return (T) err;
    }

    /**
     * 根据错误类型获取受检查的异常
     *
     * @param errType errType
     * @param <T>     错误类型
     * @return Exception or null
     */
    public <T extends Throwable> T findCheckedException(Class<T> errType) {
        if (hasCheckedException(errType)) {
            return (T) checked.get(errType);
        }
        var err = Single.from(checked.entrySet())
                .forReturn(errEntry -> errType.isAssignableFrom(errEntry.getKey()))
                .map(Map.Entry::getValue)
                .orElse(null);

        return (T) err;
    }

    /**
     * 判断给定的错误类型是否存在受检查异常
     *
     * @param errType errType
     * @param <T>     错误类型
     * @return true if exists
     */
    public <T extends Throwable> boolean hasCheckedException(Class<T> errType) {
        return checked.containsKey(errType);
    }

    /**
     * 判断给定的错误类型是否存在未受检查异常
     *
     * @param errType errType
     * @param <T>     错误类型
     * @return true if exists
     */
    public <T extends Throwable> boolean hasUncheckedException(Class<T> errType) {
        return unchecked.containsKey(errType);
    }

    /**
     * peek on unchecked exception
     *
     * @param <T> 错误类型
     * @return exception or null
     */
    public <T extends RuntimeException> T peekUncheckException() {
        return unchecked.values().stream().findFirst().map(ex -> (T) ex).orElse(null);
    }

    /**
     * peek on checked exception
     *
     * @param <T> 错误类型
     * @return exception or null
     */
    public <T extends Throwable> T peekCheckedException() {
        return checked.values().stream().findFirst().map(ex -> (T) ex).orElse(null);
    }

    /**
     * 判断是否存在错误
     *
     * @return error if true
     */
    public boolean hasErr() {
        return !checked.isEmpty() || !unchecked.isEmpty();
    }

    /**
     * 获取异常数量
     *
     * @return exceptions size
     */
    public int size() {
        return checked.size() + unchecked.size();
    }
}
