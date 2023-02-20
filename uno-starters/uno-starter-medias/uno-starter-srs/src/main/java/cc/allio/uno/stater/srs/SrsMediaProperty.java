package cc.allio.uno.stater.srs;

import cc.allio.uno.component.media.MediaProperty;

import java.util.Map;

/**
 * @author heitianzhen
 * @date 2022/4/11 16:08
 */
public interface SrsMediaProperty extends MediaProperty {

    /**
     * 获取多媒体服务器地址
     *
     * @return 地址数据
     */
    String getMediaUrl();

    /**
     * 获取Api路径集合
     *
     * @return Api-Path Map结构
     */
    Map<String, String> getApiPaths();

    /**
     * 获取多媒体云平台OAUTH对象
     *
     * @return OAUTH实体对象
     */
    OAuth getOAuth();

    /**
     * 云平台OAuth认证配置
     *
     * @author jiangwei
     * @date 2022/3/30 20:07
     * @since 1.0.6
     */
    interface OAuth {

        /**
         * 获取Token地址
         *
         * @return 访问地址或者localhost
         */
        String getAccessTokenUrl();

        /**
         * 获取认证客户端Id
         *
         * @return 客户端Id或者空
         */
        String getClientId();

        /**
         * 获取认证客户端密钥
         *
         * @return 客户端密钥或者空
         */
        String getClientSecret();
    }
}
