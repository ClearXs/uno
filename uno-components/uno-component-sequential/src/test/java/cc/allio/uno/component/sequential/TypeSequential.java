package cc.allio.uno.component.sequential;

import cc.allio.uno.component.sequential.convert.TestSequentialConvert;
import cc.allio.uno.core.metadata.mapping.DefaultMappingMetadata;
import cc.allio.uno.core.metadata.mapping.MappingMetadata;
import cc.allio.uno.core.type.DefaultType;
import cc.allio.uno.core.type.Type;

import java.util.Map;
import java.util.function.Predicate;

/**
 * 类型为type的时序数据
 *
 * @author jiangwei
 * @date 2022/5/20 13:33
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
