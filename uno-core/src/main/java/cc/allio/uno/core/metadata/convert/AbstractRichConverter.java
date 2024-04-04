package cc.allio.uno.core.metadata.convert;

import cc.allio.uno.core.bean.ObjectWrapper;
import cc.allio.uno.core.metadata.mapping.MappingField;
import cc.allio.uno.core.metadata.mapping.MappingFieldConverter;
import cc.allio.uno.core.metadata.mapping.MappingMetadata;
import cc.allio.uno.core.type.TypeValue;
import cc.allio.uno.core.metadata.Metadata;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * 抽象
 *
 * @author j.x
 * @date 2022/9/13 11:25
 * @since 1.1.0
 */
@Slf4j
public abstract class AbstractRichConverter<T extends Metadata> implements RichConverter<T> {

    private Class<? extends T> convertType;

    public void setConvertType(Class<? extends T> convertType) {
        this.convertType = convertType;
    }

    @Override
    public Class<? extends T> getConvertType() {
        return convertType;
    }

    /**
     * 执行赋值的动作
     *
     * @param name                      外部数据名称
     * @param expected                  期望的值，为null时不进行赋值
     * @param metadata                  元数据实例
     * @param excludeNotNecessaryFilter 强制进行赋值，即使映射关系不存在{@link MappingMetadata}
     * @see Metadata
     */
    protected Mono<Object> executeAssignmentAction(String name,
                                                   Object expected,
                                                   T metadata,
                                                   boolean excludeNotNecessaryFilter) {
        return executeAssignmentAction(name, expected, metadata, new ObjectWrapper(metadata), excludeNotNecessaryFilter);
    }

    /**
     * 执行赋值的动作
     *
     * @param name                      外部数据名称
     * @param expected                  期望的值，
     * @param metadata                  元数据实例
     * @param sequentialWrapper         增强BeanInfo的操作
     * @param excludeNotNecessaryFilter 强制进行赋值，即使映射关系不存在{@link MappingMetadata}
     * @see Metadata
     */
    protected Mono<Object> executeAssignmentAction(String name,
                                                   Object expected,
                                                   T metadata,
                                                   ObjectWrapper sequentialWrapper,
                                                   boolean excludeNotNecessaryFilter) {
        Mono<String> mono = Mono.just(name);
        if (!excludeNotNecessaryFilter) {
            return mono.filterWhen(nodeName -> Mono.just(metadata.getMapping().containsKey(nodeName)))
                    // 强制覆盖
                    .flatMap(nodeName -> {
                        Object setter = expected;
                        MappingField mappingField = metadata.getMapping().get(name);
                        MappingFieldConverter<?> converter = mappingField.getConverter();
                        if (converter != null) {
                            try {
                                setter = converter.execute(null, expected);
                            } catch (Throwable e) {
                                // ignore
                            }
                        }
                        // 赋予默认值
                        if (setter == null) {
                            setter = mappingField.getDefaultValue();
                        }
                        Class<?> type = mappingField.getType();
                        // 尝试进行类型转换
                        if (type != null) {
                            setter = new TypeValue(type, setter).tryConvert();
                        }
                        // 设置值在values
                        Object setValue = setter;
                        return Mono.justOrEmpty(Optional.ofNullable(metadata.getValues()))
                                .doOnNext(values -> values.put(mappingField.getName(), setValue))
                                .then(sequentialWrapper.setCoverage(mappingField.getName(), false, setValue));
                    })
                    .onErrorContinue((err, o) -> err.printStackTrace());
        } else {
            return sequentialWrapper.set(name, expected);
        }
    }
}
