package cc.allio.uno.component.websocket;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

/**
 * WebSocket端点管理器
 *
 * @author jiangwei
 * @date 2022/6/28 16:04
 * @since 1.0
 */
@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EndpointManager {

	/**
	 * 端点实例
	 * key: 端点路径
	 * value: 端点实例
	 */
	private static final MultiValueMap<String, WebSocketEndpoint> ENDPOINTS = new LinkedMultiValueMap<>();

	/**
	 * 根据路径寻找Websocket实例
	 *
	 * @param path 路径
	 * @return 实例或者空
	 */
	public static List<WebSocketEndpoint> find(String path) {
		return ENDPOINTS.get(path);
	}

	/**
	 * 注册WebSocketEndpoint
	 *
	 * @param endpoint          注册端点
	 * @param webSocketEndpoint 端点实例
	 */
	public static synchronized void registry(String endpoint, WebSocketEndpoint webSocketEndpoint) {
		ENDPOINTS.add(endpoint, webSocketEndpoint);
	}

	/**
	 * 移除WebSocketEndpoint
	 *
	 * @param endpoint          注册端点
	 * @param webSocketEndpoint 端点实例
	 */
	public static synchronized void unRegistry(String endpoint, WebSocketEndpoint webSocketEndpoint) {
		ENDPOINTS.get(endpoint).remove(webSocketEndpoint);
	}

	public static synchronized void clear() {
		ENDPOINTS.clear();
	}
}
