package cc.allio.uno.component.sequential;

import cc.allio.uno.component.sequential.convert.TestSequentialConvert;
import cc.allio.uno.core.metadata.mapping.DefaultMappingMetadata;
import cc.allio.uno.core.metadata.mapping.MappingMetadata;

import java.util.Map;
import java.util.function.Predicate;

public class UnknownSequential implements Sequential {
    @Override
    public String getType() {
        return "unknown";
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
    public Map<String, Object> getUndefinedValues() {
        return null;
    }
}
