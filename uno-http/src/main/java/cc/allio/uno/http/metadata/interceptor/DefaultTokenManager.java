package cc.allio.uno.http.metadata.interceptor;

/**
 * 基于内存默认Token管理器
 *
 * @author j.x
 * @since 1.0
 */
public class DefaultTokenManager implements TokenManager {

    private Token token;

    @Override
    public Token get() {
        return token;
    }

    @Override
    public void set(Token token) {
        this.token = token;
    }
}
