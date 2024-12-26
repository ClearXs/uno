package cc.allio.uno.core.metadata.mapping;

/**
 * 基础MappingField转换器
 *
 * @author j.x
 * @since 1.1.3
 */
public abstract class BaseMappingFieldConverter<T> implements MappingFieldConverter<T> {

    private MappingField mappingField;

    protected BaseMappingFieldConverter() {
    }

    protected BaseMappingFieldConverter(MappingField mappingField) {
        this.mappingField = mappingField;
    }

    @Override
    public MappingField getMappingField() {
        return mappingField;
    }
}
