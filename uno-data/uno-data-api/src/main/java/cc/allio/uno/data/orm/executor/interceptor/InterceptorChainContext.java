package cc.allio.uno.data.orm.executor.interceptor;

import cc.allio.uno.core.chain.DefaultChainContext;

/**
 * 拦截器链上下文
 *
 * @author j.x
 * @since 1.1.7
 */
public class InterceptorChainContext extends DefaultChainContext<InterceptorAttributes> {

    public InterceptorChainContext(InterceptorAttributes attributes) {
        super(attributes);
    }
}
