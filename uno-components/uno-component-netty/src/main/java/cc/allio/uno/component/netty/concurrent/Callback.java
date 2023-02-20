package cc.allio.uno.component.netty.concurrent;

import cc.allio.uno.component.netty.exception.ConnectionException;

/**
 * 回调接口
 * 做这个目的是提供一个执行代码时机的策略，即做成消息事件
 * @author jiangw
 * @date 2021/4/15 10:41
 * @since 1.0
 */
public interface Callback {

    /**
     * 当代码回调处代码是正常可执行的状态时，那么调用此方法
     * @param accept 传递的参数
     * @throws ConnectionException e
     */
    void acceptable(Object accept) throws ConnectionException;

    /**
     * 当发生异常时，调用此方法
     * @param ex 异常的实例
     */
    void rejected(Throwable ex);

}
