package cc.allio.uno.netty.filter;

import cc.allio.uno.netty.RemoteService;
import cc.allio.uno.netty.transport.body.Body;

/**
 * 作为处理过滤响应的最后一环
 *
 * @author j.x
 * @since 1.0
 */
public class ProcessResponseFilter extends AbstractFilter {

    @Override
    public <T extends FilterContext> void doFilter(Body body, T context, FilterChain next) throws Throwable {
        // 作为client端，处理server的响应，比如说远程调用请求的响应
        RemoteService remoteService = context.getRemoteService();
        if (remoteService != null) {
            remoteService.processRemoteResponse(context.getCtx(), context.getTransporter());
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
