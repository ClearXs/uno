package cc.allio.uno.component.netty;

import cc.allio.uno.component.netty.exception.ConnectionException;
import cc.allio.uno.component.netty.transport.UnresolvedAddress;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

/**
 * Netty连接器
 *
 * @author jiangw
 * @date 2020/12/2 14:06
 * @since 1.0
 */
public abstract class NettyAbstractConnector implements Connector {

    /**
     * 连接的地址
     */
    private final UnresolvedAddress unresolvedAddress;

    /**
     * 连接器存放的通道组
     */
    private final ChannelGroup channelGroup;

    private ChannelFuture channelFuture;

    protected NettyAbstractConnector(UnresolvedAddress unresolvedAddress, ChannelGroup channelGroup) {
        this.unresolvedAddress = unresolvedAddress;
        this.channelGroup = channelGroup;
    }

    @Override
    public UnresolvedAddress address() {
        return unresolvedAddress;
    }

    @Override
    public void setChannelFuture(ChannelFuture channelFuture) {
        this.channelFuture = channelFuture;
    }

    @Override
    public Channel createChannel() throws ConnectionException {
        if (channelFuture == null) {
            throw new ConnectionException("can't create channel");
        }
        if (channelGroup != null) {
            if (!channelGroup.isAvailable()) {
                Channel channel = channelFuture.channel();
                channelGroup.add(channel);
                return channel;
            } else {
                return channelGroup.next();
            }
        } else {
            return channelFuture.channel();
        }
    }

    @Override
    public ChannelGroup channelGroup() {
        return channelGroup;
    }

    @Override
    public void close() throws InterruptedException {
        channelGroup.removeAll();
    }

    @Override
    public int compareTo(Connector o) {
        return Integer.compare(weight(), o.weight());
    }

}
