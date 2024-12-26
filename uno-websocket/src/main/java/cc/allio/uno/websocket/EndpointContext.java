package cc.allio.uno.websocket;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 端点上下文
 *
 * @author j.x
 * @since 1.0
 */
@Data
@AllArgsConstructor
public class EndpointContext<T> {

    /**
     * 消息数据
     */
    private T message;

    private WebSocketEndpoint endpoint;
}
