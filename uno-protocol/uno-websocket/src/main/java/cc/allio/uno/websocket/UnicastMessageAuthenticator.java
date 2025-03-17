package cc.allio.uno.websocket;

import com.google.auto.service.AutoService;
import jakarta.websocket.Session;

import java.util.function.Predicate;

/**
 * 单播消息验证器
 *
 * @author j.x
 * @since 1.0
 */
@Authentication(endpointClasses = UnicastWsEndpoint.class)
@AutoService(MessagePublishAuthenticator.class)
public class UnicastMessageAuthenticator implements MessagePublishAuthenticator {

    @Override
    public Predicate<Session> auth(EndpointContext<Object> context) {
        WsEndpoint endpoint = context.getEndpoint();
        if (endpoint instanceof UnicastWsEndpoint) {
            try {
                UnicastWsEndpoint.UnicastMessage unicastMessage = ((UnicastWsEndpoint<?>) endpoint).convert(context.getMessage().toString());
                return session -> session.getId().equals(unicastMessage.getSessionKey());
            } catch (Throwable e) {
                return session -> false;
            }
        }
        return session -> true;
    }
}
