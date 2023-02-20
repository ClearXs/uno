package cc.allio.uno.starter.automic.command;

import cc.allio.uno.component.http.metadata.HttpSwapper;
import cc.allio.uno.starter.automic.AutomicHttpResponseAdapter;
import cc.allio.uno.starter.automic.AutomicMediaProperties;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;

/**
 * Automic视频平台请求
 *
 * @author jiangwei
 * @date 2022/6/16 16:52
 * @since 1.0
 */
public class AutomicBaseRequest {
    private AutomicMediaProperties properties;

    public AutomicBaseRequest(AutomicMediaProperties automicMediaProperties) {
        this.properties = automicMediaProperties;
    }

    /**
     * 包装原始swapper，返回带有授权的Http交换器
     *
     * @param swapper 原始Http对象
     * @return 包装后带有授权的Http
     */
    public Mono<HttpSwapper> authSwapper(HttpSwapper swapper) {
        return auth()
                .flatMap(token -> {
                    swapper.addAuth("ticket", token);
                    return Mono.just(swapper);
                });
    }

    /**
     * 发起登陆授权:/v1/api/login
     *
     * @return token
     */
    public Mono<String> auth() {
        return new AutomicHttpResponseAdapter(
                HttpSwapper.build(properties.getUrl() + "/v1/api/login", HttpMethod.GET)
                        .addParameter("username", properties.getAuthorize().getUsername())
                        .addParameter("password", properties.getAuthorize().getPassword())
                        .swap()
        )
                .repsInterceptor(() -> "code", code -> code == HttpStatus.OK.value())
                .map(json -> json.asString("ticket"));
    }

}
