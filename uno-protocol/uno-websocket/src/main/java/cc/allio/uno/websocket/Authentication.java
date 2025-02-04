package cc.allio.uno.websocket;

import java.lang.annotation.*;

/**
 * 标识当前认证器作用于哪一个端点
 *
 * @author j.x
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Authentication {

    /**
     * 端点路径
     *
     * @return
     */
    String[] endpoint() default {};

    /**
     * 端点Class对象
     *
     * @return
     */
    Class<? extends WsEndpoint>[] endpointClasses() default {};
}
