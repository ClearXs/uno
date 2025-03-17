package cc.allio.uno.http.metadata.interceptor;

import com.google.common.collect.Maps;
import lombok.Data;

import java.util.Map;

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

    /**
     * 存在token有关的其余信息
     */
    private Map<String, Object> others = Maps.newHashMap();
}
