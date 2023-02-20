package cc.allio.uno.starter.websocket;

import cc.allio.uno.component.websocket.WebSocketProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * Uno WebSocket自动配置类
 *
 * @author jiangwei
 * @date 2022/7/29 13:47
 * @since 1.0
 */
@EnableWebSocket
@EnableConfigurationProperties(WebSocketProperties.class)
@Configuration
public class WebSocketAutoConfiguration {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
