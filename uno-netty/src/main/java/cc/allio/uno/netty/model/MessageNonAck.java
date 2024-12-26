package cc.allio.uno.netty.model;

import cc.allio.uno.netty.transport.body.Body;
import io.netty.channel.Channel;
import lombok.Data;

/**
 * 还未确认的消息
 * @author j.x
 * @since 1.0
 */
@Data
public class MessageNonAck {

    private long unique;

    /**
     * 注册消息、消费消息、提供消息
     */
    private Body body;

    /**
     * 远端连接的通道
     */
    private Channel channel;
}

