package cc.allio.uno.core.util.template;

import cc.allio.uno.core.api.OptionalContext;
import cc.allio.uno.core.type.Types;
import com.google.common.collect.Maps;
import lombok.Getter;
import org.springframework.context.ApplicationContext;

import java.util.Map;
import java.util.Optional;

/**
 * parse template for context
 *
 * @author j.x
 * @date 2024/5/3 20:12
 * @since 1.1.9
 */
public class TemplateContext implements OptionalContext {

    final Map<String, Object> vars = Maps.newConcurrentMap();

    @Getter
    final Map<String, Class> inputs = Maps.newConcurrentMap();

    @Override
    public Optional<Object> get(String key) {
        return Optional.ofNullable(vars.get(key));
    }

    @Override
    public Optional<ApplicationContext> getApplicationContext() {
        return Optional.empty();
    }

    @Override
    public Map<String, Object> getAll() {
        return vars;
    }

    @Override
    public void putAttribute(String key, Object obj) {
        if (obj != null) {
            Class<?> valueClass = obj.getClass();
            vars.put(key, obj);
            if (Types.isBean(valueClass)) {
                inputs.put(key, valueClass);
            }
        }
    }
}