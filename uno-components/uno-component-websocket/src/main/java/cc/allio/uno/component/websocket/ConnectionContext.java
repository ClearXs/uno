package cc.allio.uno.component.websocket;

import jakarta.websocket.EndpointConfig;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 连接上下文
 *
 * @author jiangwei
 * @date 2022/7/29 16:48
 * @since 1.0
 */
@Data
@AllArgsConstructor
public class ConnectionContext {

    private EndpointConfig config;

    private WebSocketEndpoint endpoint;
}
