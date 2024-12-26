package cc.allio.uno.netty;

import cc.allio.uno.netty.model.MessageNonAck;
import cc.allio.uno.netty.model.RemoteTransporter;
import cc.allio.uno.netty.transport.body.Body;
import cc.allio.uno.netty.transport.body.MonitorBody;
import cc.allio.uno.netty.transport.body.OfflineBody;
import cc.allio.uno.netty.transport.body.RegisterBody;
import cc.allio.uno.core.util.CollectionUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;

import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author j.x
 */
public class NonAckScanner implements Runnable {

    ConcurrentMap<Long, MessageNonAck> nonAck;

    public NonAckScanner(ConcurrentMap<Long, MessageNonAck> nonAck) {
        this.nonAck = nonAck;
    }

    @Override
    public void run() {
        ConcurrentHashMap<Long, MessageNonAck> unActiveMessage = new ConcurrentHashMap<>();
        for (MessageNonAck messageNonAck : nonAck.values()) {
            long unique = messageNonAck.getUnique();
            // 如果移除为空，说明被其他线程移除，或者响应Ack时移除
            if (nonAck.remove(unique) == null) {
                continue;
            }
            // 重新构建transport
            Body body = messageNonAck.getBody();
            byte code = Protocol.Code.FAILED;
            if (body instanceof RegisterBody) {
                code = Protocol.Code.RESPONSE_SUBSCRIBE;
            } else if (body instanceof OfflineBody) {
                code = Protocol.Code.SERVICE_OFFLINE;
            } else if (body instanceof MonitorBody) {
                code = Protocol.Code.UNICAST;
            }
            RemoteTransporter transporter = RemoteTransporter.createRemoteTransporter(code
                    , body, messageNonAck.getUnique());
            Channel channel = messageNonAck.getChannel();
            if (channel.isActive()) {
                messageNonAck.getChannel()
                        .writeAndFlush(transporter)
                        .addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
            } else {
                unActiveMessage.put(unique, messageNonAck);
            }
        }
        // 移除这个message
        if (CollectionUtils.isNotEmpty(unActiveMessage)) {
            Enumeration<Long> keys = unActiveMessage.keys();
            while (keys.hasMoreElements()){
                Long key = keys.nextElement();
                nonAck.remove(key);
            }
        }
    }
}
