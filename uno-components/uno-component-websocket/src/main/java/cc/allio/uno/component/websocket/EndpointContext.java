package cc.allio.uno.component.websocket;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 端点上下文
 *
 * @author jiangwei
 * @date 2022/7/29 16:49
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
