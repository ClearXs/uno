package cc.allio.uno.netty;

import cc.allio.uno.netty.exception.ConnectionException;
import cc.allio.uno.netty.transport.UnresolvedAddress;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

/**
 * screw连接器
 * @author jiangw
 * @date 2020/12/22 13:54
 * @since 1.0
 */
public interface Connector extends Comparable<Connector>{

    /**
     * 获取连接地址
     * @return
     */
    UnresolvedAddress address();

    /**
     * 设置是否重连
     * @param reConnect
     */
    void setReConnect(boolean reConnect);

    /**
     * 设置channelFuture
     * @param channelFuture
     */
    void setChannelFuture(ChannelFuture channelFuture);

    /**
     * 创建一个channel
     * @return {@link Channel}
     * @throws ConnectionException
     */
    Channel createChannel() throws ConnectionException;

    /**
     * 返回当前连接器的通道组
     * @return {@link ChannelGroup}
     */
    ChannelGroup channelGroup();

    /**
     * <p>
     *     1.关闭{@link ChannelGroup}中所有的{@link Channel}
     * </p>
     * <p>
     *     2.移除remoteChannels的通道缓存数据
     * </p>
     * @throws InterruptedException 中断异常
     */
    void close() throws InterruptedException;

    /**
     * 权重，用于负载均衡
     * @return
     */
    int weight();
}
