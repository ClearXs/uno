package cc.allio.uno.core.cache;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 缓存key对象
 *
 * @author j.x
 * @since 1.0
 */
@Data
@AllArgsConstructor(staticName = "of")
public class CacheKey {

    public static final String COMPANY_PREFIX = "allio";

    /**
     * 字符串标识
     */
    private String key;
}
