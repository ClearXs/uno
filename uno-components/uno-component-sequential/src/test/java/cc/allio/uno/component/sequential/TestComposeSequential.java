package cc.allio.uno.component.sequential;

import cc.allio.uno.component.sequential.convert.TestSequentialConvert;
import cc.allio.uno.core.metadata.mapping.DefaultMappingMetadata;
import cc.allio.uno.core.metadata.mapping.MappingMetadata;
import cc.allio.uno.core.type.DefaultType;
import cc.allio.uno.core.type.Type;

import java.util.Map;
import java.util.function.Predicate;

public class TestComposeSequential extends BaseCompositeSequential {
    public TestComposeSequential() {
        super(TestSequential.class);
    }

    @SafeVarargs
    public TestComposeSequential(Class<? extends Sequential>... sequentialClass) {
        super(sequentialClass);
    }

    @Override
    public Type getType() {
        return DefaultType.of("test-compose");
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
        return new DefaultMappingMetadata(new TestSequentialConvert(TestComposeSequential.class));
    }

    @Override
    public Map<String, Object> getValues() {
        return null;
    }
}
