package cc.allio.uno.component.websocket;

import cc.allio.uno.core.util.JsonUtil;
import lombok.Data;

import javax.websocket.Session;

/**
 * 单播推送WebSocket
 *
 * @author jiangwei
 * @date 2022/8/2 15:23
 * @since 1.0
 */
public abstract class UnicastWebSocketEndpoint<R> extends BaseWebsocketEndpoint<R> {


    @Override
    protected void doOnOpen(Session session) throws Throwable {
        // 向客户度发送唯一标识，当客户端需要时根据这个唯一标识来获取数据
        UnicastMessage unicastMessage = new UnicastMessage();
        unicastMessage.setSessionKey(session.getId());
        session.getBasicRemote().sendText(JsonUtil.toJson(unicastMessage));
    }

    /**
     * 将消息转换为单播消息
     *
     * @param message 原始消息
     * @return 单薄消息实例
     * @throws Throwable 解析过程中发生错误时抛出
     */
    protected abstract UnicastMessage convert(String message) throws Throwable;

    /**
     * 单播消息
     */
    @Data
    public static class UnicastMessage {
        /**
         * sessionKey
         */
        private String sessionKey;

        /**
         * 原始数据
         */
        private String source;
    }
}
