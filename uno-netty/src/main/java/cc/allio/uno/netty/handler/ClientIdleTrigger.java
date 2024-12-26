package cc.allio.uno.netty.handler;

import cc.allio.uno.netty.model.HeartBeats;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @author j.x
 */
@ChannelHandler.Sharable
@Slf4j
public class ClientIdleTrigger extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state() == IdleState.WRITER_IDLE) {
                if (log.isDebugEnabled()) {
                    log.debug("client write idle... started send heart beats data package");
                }
                ctx.channel().writeAndFlush(HeartBeats.heartbeat());
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
