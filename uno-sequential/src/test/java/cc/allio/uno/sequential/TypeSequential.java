package cc.allio.uno.sequential;

import cc.allio.uno.core.metadata.mapping.DefaultMappingMetadata;
import cc.allio.uno.core.metadata.mapping.MappingMetadata;
import cc.allio.uno.core.type.DefaultType;
import cc.allio.uno.core.type.Type;
import cc.allio.uno.sequential.convert.TestSequentialConvert;
import cc.allio.uno.sequnetial.Sequential;

import java.util.Map;
import java.util.function.Predicate;

/**
 * 类型为type的时序数据
 *
 * @author j.x
 * @since 1.0
 */
public class TypeSequential implements Sequential {
    private Long id;

    @Override
    public Type getType() {
        return DefaultType.of("type");
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
    public Long getSeqId() {
        return id;
    }

    public void setSequentialId(Long id) {
        this.id = id;
    }

    @Override
    public MappingMetadata getMapping() {
        return new DefaultMappingMetadata(new TestSequentialConvert(TestSequential.class));
    }

    @Override
    public Map<String, Object> getValues() {
        return null;
    }
}
