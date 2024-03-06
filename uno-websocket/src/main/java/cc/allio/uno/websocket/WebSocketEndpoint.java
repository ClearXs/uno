package cc.allio.uno.websocket;

import cc.allio.uno.core.util.*;
import cc.allio.uno.core.util.template.ExpressionTemplate;
import cc.allio.uno.core.util.template.Tokenizer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 定义WebSocket-Endpoint抽象方法
 *
 * @author jiangwei
 * @date 2021/12/30 23:48
 * @since 1.0
 */
public interface WebSocketEndpoint {

    /**
     * 客户端定时发送的心跳数据
     */
    String PING = "ping";

    /**
     * 服务器心跳返回数据
     */
    String PONG = "pong";

    /**
     * websocket初始操作
     * 1.初始化
     * 2.初始化心跳
     *
     * @param session 连接的session会话
     * @throws ParseException cron表达式解析失败时抛出
     */
    void init(Session session) throws ParseException;

    /**
     * 通过Websocket向订阅者发布数据
     *
     * @param source
     */
    void publish(Object source);

    /**
     * 是否开启心跳
     *
     * @return true 开启， false 不开启
     */
    boolean enableHeartbeat();

    /**
     * 获取当前WebSocket会话实例
     *
     * @return Session实例对象
     */
    Session getSession();

    /**
     * 获取WebSocket配置实例
     *
     * @return WebSocketProperties实例对象
     * @throws NoSuchBeanDefinitionException 没有找到{@link WebSocketProperties}的定义抛出
     * @throws NullPointerException          {@link BeanUtils#getContext()}为空时抛出
     */
    default WebSocketProperties getProperties() {
        return BeanUtils.getBean(WebSocketProperties.class);
    }


    /**
     * 获取当前Websocket {@link EndpointKey}实例对象
     *
     * @return {@link EndpointKey}实例对象
     * @throws IllegalArgumentException 如果当前对象没有被{@link ServerEndpoint}注解的话抛出
     */
    default EndpointKey getEndpointKey() {
        ServerEndpoint annotation = ClassUtils.getAnnotation(this.getClass(), ServerEndpoint.class);
        if (ObjectUtils.isEmpty(annotation)) {
            throw new IllegalArgumentException(String.format("Current Class %s need @ServerEndpoint", this.getClass().getName()));
        }
        // 如果有占位符则进行替换
        ExpressionTemplate expressionTemplate = ExpressionTemplate.createTemplate(Tokenizer.BRACE);
        String endpoint = expressionTemplate.parseTemplate(annotation.value(), getSession().getPathParameters());
        return new EndpointKey(endpoint);
    }

    /**
     * 合并集合中所有数据（里面数据可能是array）
     *
     * @param buffer 缓存数据
     * @return 数组数据
     */
    default <T> Optional<String> combine(List<T> buffer) {
        return Optional.ofNullable(JsonUtils.getJsonMapper())
                .flatMap(objectMapper -> {
                    if (CollectionUtils.isEmpty(buffer)) {
                        return Optional.empty();
                    } else if (buffer.size() == 1) {
                        return Optional.of(JsonUtils.toJson(buffer.get(0)));
                    }
                    ArrayNode arrayNode = objectMapper.createArrayNode();
                    buffer.forEach(maybeJson -> {
                        String json;
                        if (maybeJson instanceof String) {
                            json = (String) maybeJson;
                        } else {
                            json = JsonUtils.toJson(maybeJson);
                        }
                        try {
                            JsonNode jsonNode = objectMapper.readTree(json);
                            if (jsonNode instanceof ArrayNode) {
                                arrayNode.addAll((ArrayNode) jsonNode);
                            } else {
                                arrayNode.add(jsonNode);
                            }
                        } catch (Throwable ex) {
                            throw new RuntimeException(ex);
                        }
                    });
                    return Optional.of(arrayNode.toPrettyString());
                });
    }

    /**
     * 获取websocket session中map数据
     * <ul>
     *     <li>首先从{@link Session#getPathParameters()}中获取数据</li>
     *  <li>获取不到在从{@link Session#getRequestParameterMap()}获取</li>
     * </ul>
     *
     * @param session websocket session对象
     * @param key     key
     * @return 如果key不存在与session中返回""，否则返回拼接的String
     */
    static String getSessionString(Session session, String key) {
        Map<String, String> pathParameters = session.getPathParameters();
        return Optional.ofNullable(pathParameters.get(key))
                .orElseGet(() ->
                        Optional.ofNullable(session.getRequestParameterMap().get(key))
                                .map(multiValue -> String.join("", multiValue))
                                .orElse("")
                );
    }
}
