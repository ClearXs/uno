package cc.allio.uno.websocket;

import jakarta.websocket.Session;

import java.util.function.Predicate;

/**
 * 消息发布认证器，当需要发布某个消息时，对它发布的消息作认证
 *
 * @author j.x
 * @see Authentication
 * @see Globe
 * @see GlobeAuthenticator
 * @since 1.0
 */
public interface MessagePublishAuthenticator {

    /**
     * 认证发布消息
     *
     * @param context 消息端点上下文
     * @return 认证是否成功
     */
    Predicate<Session> auth(EndpointContext<Object> context);
}
