package cc.allio.uno.component.netty;

import io.netty.channel.group.ChannelGroup;

/**
 * screw
 * @author jiangw
 * @date 2020/12/8 17:18
 * @since 1.0
 */
public abstract class AbstractInvoker implements Invoker {

    protected ChannelGroup channelGroup;

    protected AbstractInvoker(ChannelGroup channelGroup) {
        this.channelGroup = channelGroup;
    }
}
