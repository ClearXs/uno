package cc.allio.uno.core.metadata.endpoint.source;

import cc.allio.uno.core.util.JsonUtils;
import org.springframework.context.ApplicationContext;

import java.util.Collection;

/**
 * 集合数据源
 *
 * @author j.x
 * @date 2022/9/27 18:16
 * @since 1.1.0
 */
public class CollectionSource extends JsonSource {

    private final Collection<?> sources;

    public CollectionSource(Collection<?> sources) {
        this.sources = sources;
    }

    @Override
    public void register(ApplicationContext context) {
        sources.forEach(source -> next(JsonUtils.toJson(source)));
    }
}
