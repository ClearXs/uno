package cc.allio.uno.http.metadata;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 请求参数
 *
 * @author jiangwei
 * @date 2022/8/24 18:08
 * @since 1.0
 */
@Data
@AllArgsConstructor
public class Parameter<T> {

    private String key;

    private T value;

    /**
     * 构建Parameter参数
     *
     * @param key 参数key
     * @param val 参数val
     * @param <T> 值范型
     * @return Parameter实例对象
     */
    public static <T> Parameter<T> build(String key, T val) {
        return new Parameter<>(key, val);
    }
}
