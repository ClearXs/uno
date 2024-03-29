package cc.allio.uno.http.metadata.interceptor;

/**
 * Token管理器
 *
 * @author j.x
 * @date 2022/8/25 09:26
 * @since 1.0
 */
public interface TokenManager {

    /**
     * 获取外部系统Token
     *
     * @return
     */
    Token get();

    /**
     * 设置外部系统Token
     *
     * @param token token实例
     */
    void set(Token token);
}
