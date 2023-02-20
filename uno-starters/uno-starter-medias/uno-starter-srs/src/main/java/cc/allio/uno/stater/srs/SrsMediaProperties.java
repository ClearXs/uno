package cc.allio.uno.stater.srs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @author heitianzhen
 * @date 2022/4/11 16:09
 */
@Data
@ConfigurationProperties("automic.uno.media")
public class SrsMediaProperties implements SrsMediaProperty {

    /**
     * 多媒体地址，根地址
     */
    private String url = "";

    /**
     * 是否开启回调端点
     */
    private boolean endpoint = false;

    /**
     * api地址
     */
    private Map<String, String> apiPaths = new HashMap<>();

    /**
     * OAuth配置
     */
    private OAuth oAuth = new UnoMediaOAuthProperties();

    @Override
    public String getMediaUrl() {
        return url;
    }

    @Override
    public Map<String, String> getApiPaths() {
        return apiPaths;
    }

    @Override
    public OAuth getOAuth() {
        return oAuth;
    }

    /**
     * uno-OAuth配置类
     *
     * @author jiangwei
     * @date 2022/3/30 20:13
     * @since 1.0.6
     */
    @Data
    static class UnoMediaOAuthProperties implements OAuth {

        /**
         * 访问token地址
         */
        private String accessTokenUrl = "localhost";

        /**
         * OAuth客户端id
         */
        private String clientId = "";

        /**
         * OAuth客户端密钥
         */
        private String clientSecret = "";


        @Override
        public String getAccessTokenUrl() {
            return accessTokenUrl;
        }

        @Override
        public String getClientId() {
            return clientId;
        }

        @Override
        public String getClientSecret() {
            return clientSecret;
        }
    }
}
