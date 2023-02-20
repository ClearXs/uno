package cc.allio.uno.component.websocket;

import java.lang.annotation.*;

/**
 * 标识于{@link ConnectionAuthenticator}或者{@link MessageReceiveAuthenticator}来表示当前认证器是否为全局
 *
 * @author jiangwei
 * @date 2022/7/29 16:51
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Globe {
}
