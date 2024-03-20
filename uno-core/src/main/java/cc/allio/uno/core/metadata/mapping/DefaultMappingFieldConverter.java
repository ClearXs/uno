package cc.allio.uno.core.metadata.mapping;

import org.springframework.context.ApplicationContext;

/**
 * 通用映射字段转换器，直接返回原始对象
 *
 * @author j.x
 * @date 2022/12/14 13:00
 * @since 1.1.2
 */
public class DefaultMappingFieldConverter extends BaseMappingFieldConverter<Object> {

    public DefaultMappingFieldConverter() {
    }

    public DefaultMappingFieldConverter(MappingField mappingField) {
        super(mappingField);
    }

    @Override
    public Object execute(ApplicationContext context, Object value) throws Throwable {
        return value;
    }

    @Override
    public String keyConverter() {
        return null;
    }
}
