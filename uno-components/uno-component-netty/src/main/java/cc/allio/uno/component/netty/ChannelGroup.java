package cc.allio.uno.component.netty;

import io.netty.channel.Channel;

/**
 * 连接的管道组
 * @author jiangw
 * @date 2020/11/28 12:08
 * @since 1.0
 */
public interface ChannelGroup {

    /**
     * 轮询获取通道组中的某一个通道
     * @return
     */
    Channel next();

    /**
     * 添加通道
     * @param channel
     * @return
     */
    boolean add(Channel channel);

    /**
     * 移除通道
     * @param channel
     */
    void remove(Channel channel);

    /**
     * 移除全部通道
     */
    void removeAll() throws InterruptedException;

    /**
     * 当前通道组是否是可用的
     * @return
     */
    boolean isAvailable();

    /**
     * 通道的数量
     * @return
     */
    int size();
}
