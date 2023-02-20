package cc.allio.uno.component.websocket;

import cc.allio.uno.core.util.ClassUtil;
import cc.allio.uno.core.util.Collections;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import javax.websocket.Session;
import java.util.Arrays;
import java.util.List;
import java.util.ServiceLoader;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * WebSocket端点认证器
 *
 * @author jiangwei
 * @date 2022/7/29 17:00
 * @since 1.0
 */
public class EndpointAuthenticator {

    /**
     * WebSocket端点实例
     */
    private final WebSocketEndpoint webSocketEndpoint;

    /**
     * 连接认证器集合
     */
    private final List<ConnectionAuthenticator> connectionAuthenticators;

    /**
     * 消息接收认证器
     */
    private final List<MessageReceiveAuthenticator> receiveAuthenticators;

    /**
     * 消息发布认证器
     */
    private final List<MessagePublishAuthenticator> publishAuthenticators;


    public EndpointAuthenticator(WebSocketEndpoint webSocketEndpoint) {
        this.webSocketEndpoint = webSocketEndpoint;

        // 构建连接认证器
        this.connectionAuthenticators = Collections.newArrayList(
                        ServiceLoader.load(
                                ConnectionAuthenticator.class,
                                Thread.currentThread().getContextClassLoader()))
                .stream()
                .filter(authFilter())
                .collect(Collectors.toList());
        AnnotationAwareOrderComparator.sort(connectionAuthenticators);

        // 构建消息接收认证器
        this.receiveAuthenticators = Collections.newArrayList(Collections.newArrayList(
                        ServiceLoader.load(
                                MessageReceiveAuthenticator.class,
                                Thread.currentThread().getContextClassLoader())))
                .stream()
                .filter(authFilter())
                .collect(Collectors.toList());
        AnnotationAwareOrderComparator.sort(receiveAuthenticators);

        // 构建消息发布认证器
        this.publishAuthenticators = Collections.newArrayList(Collections.newArrayList(
                        ServiceLoader.load(
                                MessagePublishAuthenticator.class,
                                Thread.currentThread().getContextClassLoader())))
                .stream()
                .filter(authFilter())
                .collect(Collectors.toList());
        AnnotationAwareOrderComparator.sort(receiveAuthenticators);
    }

    <T> Predicate<? super T> authFilter() {
        return authenticator -> {
            // 判断当前认证器是否是全局认证器
            Globe globe = ClassUtil.getAnnotation(authenticator.getClass(), Globe.class);
            if (globe != null || authenticator instanceof GlobeAuthenticator) {
                return true;
            }
            // 如果不是全局认证器则判断是否是属于当前WebSocket的认证器
            Authentication authentication = ClassUtil.getAnnotation(authenticator.getClass(), Authentication.class);
            return authentication != null &&
                    // 1.验证当前websocketEndpoint实例是否数据@Authentication#endpoint标识的
                    (Arrays.asList(authentication.endpoint()).contains(webSocketEndpoint.getEndpointKey().getKey())
                            // 2.验证当前websocketEndpoint实例Class对象是否是被@Authentication#endpointClasses标识的
                            || Arrays.stream(authentication.endpointClasses()).anyMatch(a -> a.isAssignableFrom(webSocketEndpoint.getClass())));
        };
    }

    /**
     * 认证连接
     *
     * @return 断言实例
     */
    public Predicate<Session> authConnection(ConnectionContext connectionContext) {
        return connectionAuthenticators
                .stream()
                .map(authenticator -> authenticator.auth(connectionContext))
                .reduce(Predicate::and)
                .orElse(session -> true);
    }


    /**
     * 认证每一个接收的消息
     *
     * @param endpointContext 端点上下文
     * @return 断言实例
     */
    public Predicate<Session> authReceiveMessage(EndpointContext<?> endpointContext) {
        return (Predicate<Session>) receiveAuthenticators
                .stream()
                .map(authenticator -> authenticator.auth(endpointContext))
                .reduce(Predicate::and)
                .orElse(session -> true);
    }

    /**
     * 认证每一个发布消息
     *
     * @param endpointContext 端点上下文
     * @return 断言实例
     */
    public Predicate<Session> authPublishMessage(EndpointContext<Object> endpointContext) {
        return publishAuthenticators
                .stream()
                .map(authenticator -> authenticator.auth(endpointContext))
                .reduce(Predicate::and)
                .orElse(session -> true);
    }

}
