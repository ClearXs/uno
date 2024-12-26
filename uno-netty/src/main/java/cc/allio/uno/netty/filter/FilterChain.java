package cc.allio.uno.netty.filter;


import cc.allio.uno.netty.RemoteService;
import cc.allio.uno.netty.transport.body.Body;

/**
 * 过滤器调用链，对请求进行过滤。基于责任链模式
 * 链中handler是FilterChain，由{@link Filter}包装
 * <p>
 *     一个链的处理是：
 *     chain.process() -> filter.filter(chain) -> goon? -> next chain.process() -> or terminate
 * </p>
 * <p>
 *     一个调用链，类似于netty的流水线，所以具体的{@link Filter}分为入站过滤器与出站过滤器，由每个{@link RemoteService}进行动态添加
 * </p>
 * <p>
 *     过滤链由{@link FilterChainLoader#loadChain(Filter...)}与{@link FilterChainLoader#loadPriority(FilterChain)}创建
 * </p>
 * @author j.x
 * @since 1.0
 */
public interface FilterChain {

    /**
     * 设置{@link Filter}
     * @param filter
     */
    void setFilter(Filter filter);

    /**
     * @return {@link Filter}
     */
    Filter getFilter();

    /**
     * 下一个过滤链
     */
    FilterChain next();

    /**
     * 设置下一个过滤器
     * @param filterChain
     */
    void setNext(FilterChain filterChain);

    /**
     * 在链中对请求进行过滤
     */
    <T extends FilterContext> void process(Body body, T context) throws Throwable;
}
