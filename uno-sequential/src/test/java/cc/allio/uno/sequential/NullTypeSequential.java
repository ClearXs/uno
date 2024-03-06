package cc.allio.uno.sequential;

import cc.allio.uno.core.metadata.mapping.DefaultMappingMetadata;
import cc.allio.uno.core.metadata.mapping.MappingMetadata;
import cc.allio.uno.core.type.Type;
import cc.allio.uno.sequential.convert.TestSequentialConvert;
import cc.allio.uno.sequnetial.Sequential;

import java.util.Map;
import java.util.function.Predicate;

public class NullTypeSequential implements Sequential {
    @Override
    public Type getType() {
        return null;
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
        return new DefaultMappingMetadata(new TestSequentialConvert(NullTypeSequential.class));
    }

    @Override
    public Map<String, Object> getValues() {
        return null;
    }
}
