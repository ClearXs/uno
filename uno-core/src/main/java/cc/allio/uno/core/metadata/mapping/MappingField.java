package cc.allio.uno.core.metadata.mapping;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(of = "name")
public class MappingField {

    /**
     * 字段名称
     */
    private String name;

    /**
     * 默认值
     */
    private Object defaultValue;

    /**
     * 字段转换器
     */
    private MappingFieldConverter<?> converter;

    /**
     * 映射字段type对应的class
     */
    private Class<?> type;
}
