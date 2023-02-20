package cc.allio.uno.component.websocket;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.websocket.EndpointConfig;

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
