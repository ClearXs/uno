package cc.allio.uno.websocket;

import com.google.auto.service.AutoService;
import jakarta.websocket.Session;

import java.util.function.Predicate;

/**
 * 单播消息验证器
 *
 * @author j.x
 * @date 2022/8/2 15:48
 * @since 1.0
 */
@Authentication(endpointClasses = UnicastWebSocketEndpoint.class)
@AutoService(MessagePublishAuthenticator.class)
public class UnicastMessageAuthenticator implements MessagePublishAuthenticator {

    @Override
    public Predicate<Session> auth(EndpointContext<Object> context) {
        WebSocketEndpoint endpoint = context.getEndpoint();
        if (endpoint instanceof UnicastWebSocketEndpoint) {
            try {
                UnicastWebSocketEndpoint.UnicastMessage unicastMessage = ((UnicastWebSocketEndpoint<?>) endpoint).convert(context.getMessage().toString());
                return session -> session.getId().equals(unicastMessage.getSessionKey());
            } catch (Throwable e) {
                return session -> false;
            }
        }
        return session -> true;
    }
}
