package cc.allio.uno.websocket;

import jakarta.websocket.Session;

import java.util.List;
import java.util.function.Predicate;

/**
 * 消息认证器，消息可能是一个对象或者集合，需要对这些消息进行进行认证，认证成功的消息才会调用{@link BaseWsEndpoint#doOnMessage(Session, List)}
 *
 * @param <T> 消息实体
 * @author j.x
 * @since 1.0
 */
@FunctionalInterface
public interface MessageReceiveAuthenticator<T> {

    /**
     * 消息认证
     *
     * @param endpointContext 端点上下文对象
     * @return 断言实例
     */
    Predicate<Session> auth(EndpointContext<T> endpointContext);

}
