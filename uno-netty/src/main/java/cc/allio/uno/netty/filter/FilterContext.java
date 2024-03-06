package cc.allio.uno.netty.filter;

import cc.allio.uno.netty.RemoteService;
import cc.allio.uno.netty.model.RemoteTransporter;
import io.netty.channel.ChannelHandlerContext;

/**
 * screw
 * @author jiangw
 * @date 2020/12/8 17:18
 * @since 1.0
 */
public class FilterContext {

    /**
     * 远程调用的服务
     */
    private RemoteService remoteService;

    /**
     * 上下文
     */
    private ChannelHandlerContext ctx;

    /**
     * 传输
     */
    private RemoteTransporter transporter;

    public FilterContext(RemoteService remoteService, ChannelHandlerContext ctx, RemoteTransporter transporter) {
        this.remoteService = remoteService;
        this.ctx = ctx;
        this.transporter = transporter;
    }

    public RemoteService getRemoteService() {
        return remoteService;
    }

    public void setRemoteService(RemoteService remoteService) {
        this.remoteService = remoteService;
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public void setCtx(ChannelHandlerContext context) {
        this.ctx = context;
    }

    public RemoteTransporter getTransporter() {
        return transporter;
    }

    public void setTransporter(RemoteTransporter transporter) {
        this.transporter = transporter;
    }
}
