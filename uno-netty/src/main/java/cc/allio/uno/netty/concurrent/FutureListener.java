package cc.allio.uno.netty.concurrent;

/**
 * TDD
 * @author j.x
 * @since 1.0
 */
public interface FutureListener<V> {

    /**
     * 一个完成后调用。
     * @param result
     * @param throwable
     */
    void completed(V result, Throwable throwable) throws Exception;

}
