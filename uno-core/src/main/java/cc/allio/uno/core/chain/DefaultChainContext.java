package cc.allio.uno.core.chain;

import com.google.common.collect.Maps;
import lombok.Getter;
import org.springframework.context.ApplicationContext;

import java.util.Map;
import java.util.Optional;

/**
 * the {@link ChainContext} default implementation.
 *
 * @author j.x
 * @since 1.2.2
 */
public class DefaultChainContext<IN> implements ChainContext<IN> {

    private final IN in;
    private final Map<String, Object> attributes = Maps.newConcurrentMap();

    public DefaultChainContext(IN in) {
        this.in = in;
    }

    @Override
    public IN getIN() {
        return in;
    }

    @Override
    public Optional<Object> get(String s) {
        return Optional.ofNullable(attributes.get(s));
    }

    @Override
    public Optional<ApplicationContext> getApplicationContext() {
        return Optional.empty();
    }

    @Override
    public Map<String, Object> getAll() {
        return attributes;
    }

    @Override
    public void put(String s, Object obj) {
        attributes.put(s, obj);
    }

    @Override
    public boolean remove(String s) {
        return attributes.remove(s) != null;
    }
}
