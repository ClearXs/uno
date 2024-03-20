package cc.allio.uno.core.metadata.endpoint.source;

import cc.allio.uno.core.util.JsonUtils;
import org.springframework.context.ApplicationContext;

/**
 * 对象数据源
 *
 * @author j.x
 * @date 2022/10/12 16:52
 * @since 1.1.0
 */
public class ObjectSource extends JsonSource {

    /**
     * 搜集对象数据
     */
    private final Object object;

    public ObjectSource(Object o) {
        this.object = o;
    }

    @Override
    public void register(ApplicationContext context) {
        next(JsonUtils.toJson(object));
    }
}
