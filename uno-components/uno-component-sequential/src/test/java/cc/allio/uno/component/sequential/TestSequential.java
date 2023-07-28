package cc.allio.uno.component.sequential;

import cc.allio.uno.component.sequential.convert.TestSequentialConvert;
import cc.allio.uno.core.metadata.mapping.DefaultMappingMetadata;
import cc.allio.uno.core.metadata.mapping.MappingField;
import cc.allio.uno.core.metadata.mapping.MappingMetadata;
import cc.allio.uno.core.type.DefaultType;
import cc.allio.uno.core.type.Type;
import lombok.Data;

import java.util.Date;
import java.util.Map;
import java.util.function.Predicate;

@Data
public class TestSequential extends BaseSequential {

    private String toText;

    private Date unknownDate;

    private String excludeField;

    MappingMetadata mappingMetadata;

    public TestSequential() {
        this.mappingMetadata = new DefaultMappingMetadata(new TestSequentialConvert(TestSequential.class));
        mappingMetadata.addMapping(MappingField.builder().name("toText").build(), MappingField.builder().name("toText").build());
        mappingMetadata.addMapping(MappingField.builder().name("unknownDate").build(), MappingField.builder().name("unknownDate").build());
    }

    @Override
    public Type getType() {
        return DefaultType.of("test");
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
    public boolean computed() {
        return false;
    }

    @Override
    public MappingMetadata getMapping() {
        return mappingMetadata;
    }

    @Override
    public Map<String, Object> getValues() {
        return null;
    }
}
