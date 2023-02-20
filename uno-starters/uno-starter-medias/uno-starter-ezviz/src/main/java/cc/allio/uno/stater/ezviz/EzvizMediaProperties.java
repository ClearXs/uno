package cc.allio.uno.stater.ezviz;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @author heitianzhen
 * @date 2022/4/12 9:42
 */
@Data
@ConfigurationProperties("automic.uno.media")
public class EzvizMediaProperties implements EzvizMediaProperty{
    /**
     * 获取播放地址url
     */
    private String obtainPlayUrl = "";

    /**
     * 云台控制地址url
     */
    private String deviceControlUrl = "";

    /**
     * 是否开启回调端点
     */
    private boolean endpoint = true;

    /**
     * api地址
     */
    private Map<String, String> apiPaths = new HashMap<>();

    /**
     * OAuth配置
     */
    private OAuth oAuth = new UnoMediaOAuthProperties();

    @Override
    public String getObtainPlayUrl() {
        return obtainPlayUrl;
    }

    @Override
    public String getDeviceControlUrl() {
        return deviceControlUrl;
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
         * 获取萤石云token地址
         */
        private String accessTokenUrl = "https://open.ys7.com/api/lapp/token/get";

        /**
         * 萤石云app密钥Key
         */
        private String appKey = "6b9a4c8acfa1494d89414a6ec67569bf";

        /**
         * 萤石云app密钥密码Secret
         */
        private String appSecret = "7f24bff5699f52584b99a91df99eadbe";


        @Override
        public String getAccessTokenUrl() {
            return accessTokenUrl;
        }

        @Override
        public String getAppKey() {
            return appKey;
        }

        @Override
        public String getAppSecret() {
            return appSecret;
        }
    }
}
