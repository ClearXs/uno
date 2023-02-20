package cc.allio.uno.component.netty.processor;

import cc.allio.uno.component.netty.model.RemoteTransporter;
import io.netty.channel.ChannelHandlerContext;

public class NettyFastFailProcessor implements NettyProcessor {

    private Throwable cause;

    public NettyFastFailProcessor(Throwable cause) {
        this.cause = cause;
    }

    @Override
    public RemoteTransporter process(ChannelHandlerContext ctx, RemoteTransporter request) {
        return RemoteTransporter.failedResponse(request.getUnique(), cause);
    }
}
