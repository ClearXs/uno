package cc.allio.uno.starter.automic;

import cc.allio.uno.component.media.MediaProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("allio.uno.media")
public class AutomicMediaProperties implements MediaProperty {

    /**
     * 视频平台地址
     */
    private String url;

    private Authorize authorize;

    /**
     * 授权
     *
     * @author jiangwei
     * @date 2022/6/16 16:53
     * @since 1.0
     */
    @Data
    public static class Authorize {
        /**
         * 用户名
         */
        private String username;

        /**
         * 密码
         */
        private String password;
    }
}
