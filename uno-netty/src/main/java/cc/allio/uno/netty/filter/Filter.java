package cc.allio.uno.netty.filter;

import cc.allio.uno.netty.transport.body.Body;

/**
 * screw
 *
 * @author jiangw
 * @date 2020/12/8 17:18
 * @since 1.0
 */
public interface Filter extends Comparable<Filter> {

    /**
     * 调用过滤器
     *
     * @param body    请求body
     * @param context 过滤器上下文对象，过滤器可以从里面取一些有用的对象
     * @param next    下一个过滤链
     * @throws Throwable
     */
    <T extends FilterContext> void doFilter(Body body, T context, FilterChain next) throws Throwable;

    /**
     * 设置当前过滤器的权重，权重越低越优先执行
     *
     * @return
     */
    Integer weight();

    /**
     * 过滤器名称
     */
    String name();
}
