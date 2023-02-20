package cc.allio.uno.component.http.metadata.interceptor;

import lombok.Data;

@Data
public class Token {

    /**
     * 访问Token
     */
    private String accessToken;

    /**
     * 刷新Token
     */
    private String refreshToken;

    /**
     * 放行用Token
     */
    private String freeUpToken;
}
