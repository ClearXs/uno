package cc.allio.uno.sequential;

import cc.allio.uno.core.metadata.mapping.DefaultMappingMetadata;
import cc.allio.uno.core.metadata.mapping.MappingMetadata;
import cc.allio.uno.core.type.DefaultType;
import cc.allio.uno.core.type.Type;
import cc.allio.uno.sequential.convert.TestSequentialConvert;
import cc.allio.uno.sequnetial.Sequential;

import java.util.Map;
import java.util.function.Predicate;

public class UnknownSequential implements Sequential {
    @Override
    public Type getType() {
        return DefaultType.of("unknown");
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
        return new DefaultMappingMetadata(new TestSequentialConvert(UnknownSequential.class));
    }

    @Override
    public Map<String, Object> getValues() {
        return null;
    }
}
