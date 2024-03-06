package cc.allio.uno.netty.handler;

import io.netty.channel.ChannelHandler;

public interface ChannelHandlerHolder {

    /**
     * @see ConnectorWatchDog
     */
    ChannelHandler[] channelHandlers();
}
