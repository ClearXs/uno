package cc.allio.uno.component.sequential;

import cc.allio.uno.core.metadata.mapping.MappingMetadata;
import cc.allio.uno.core.type.DefaultType;
import cc.allio.uno.core.type.Type;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.function.Predicate;

public class UnCodeSequential implements Sequential {

    @Override
    public String getCode() {
        return null;
    }

    @Override
    public Type getType() {
        return DefaultType.of("123");
    }

    @Override
    public Long processTime() {
        return null;
    }

    @Override
    public Long eventTime() {
        return null;
    }

    @Override
    public Predicate<Sequential> selfChanged() {
        return null;
    }

    @Override
    public MappingMetadata getMapping() {
        return null;
    }

    @Override
    public Map<String, Object> getValues() {
        Map<String, Object> map = Maps.newHashMap();
        map.put("1", 1);
        return map;
    }
}
