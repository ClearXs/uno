package cc.allio.uno.netty;

import io.netty.channel.group.ChannelGroup;

/**
 * screw
 * @author j.x
 * @since 1.0
 */
public abstract class AbstractInvoker implements Invoker {

    protected ChannelGroup channelGroup;

    protected AbstractInvoker(ChannelGroup channelGroup) {
        this.channelGroup = channelGroup;
    }
}
