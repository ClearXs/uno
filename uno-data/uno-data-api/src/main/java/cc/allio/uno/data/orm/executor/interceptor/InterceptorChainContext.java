package cc.allio.uno.data.orm.executor.interceptor;

import cc.allio.uno.core.chain.ChainContext;

/**
 * 拦截器链上下文
 *
 * @author jiangwei
 * @date 2024/1/8 10:59
 * @since 1.1.6
 */
public class InterceptorChainContext implements ChainContext<InterceptorAttributes> {

    private final InterceptorAttributes attributes;

    public InterceptorChainContext(InterceptorAttributes attributes) {
        this.attributes = attributes;
    }

    @Override
    public InterceptorAttributes getIN() {
        return attributes;
    }

}
