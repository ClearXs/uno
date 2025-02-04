package cc.allio.uno.websocket;

import com.google.auto.service.AutoService;
import jakarta.websocket.Session;

import java.util.function.Predicate;

/**
 * 连接认证器，实现类通过带上{@link AutoService}进行注入SPI。
 *
 * @author j.x
 * @since 1.0
 */
@FunctionalInterface
public interface ConnectionAuthenticator {

    /**
     * 连接认证
     *
     * @param connectionContext 连接上下文信息
     * @return 断言实例
     */
    Predicate<Session> auth(ConnectionContext connectionContext);
}
