package cc.allio.uno.core.cache;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 缓存key对象
 *
 * @author jiangwei
 * @date 2022/2/10 17:04
 * @since 1.0
 */
@Data
@AllArgsConstructor(staticName = "of")
public class CacheKey {

    public static final String COMPANY_PREFIX = "automic";

    /**
     * 字符串标识
     */
    private String key;
}
