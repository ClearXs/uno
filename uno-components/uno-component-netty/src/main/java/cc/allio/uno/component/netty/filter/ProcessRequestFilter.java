package cc.allio.uno.component.netty.filter;


import cc.allio.uno.component.netty.RemoteService;
import cc.allio.uno.component.netty.transport.body.Body;

/**
 * 作为处理过滤请求的最后一环
 *
 * @author jiangw
 * @date 2020/12/24 11:46
 * @since 1.0
 */
public class ProcessRequestFilter extends AbstractFilter {

    @Override
    public <T extends FilterContext> void doFilter(Body body, T context, FilterChain next) throws Throwable {
        // 作为server端，处理client的请求，比如说，注册请求，订阅请求，远程调用请求
        RemoteService remoteService = context.getRemoteService();
        if (remoteService != null) {
            remoteService.processRemoteRequest(context.getCtx(), context.getTransporter());
        }
        if (next != null) {
            next.process(body, context);
        }
    }

    @Override
    public Integer weight() {
        return Integer.MAX_VALUE;
    }
}
