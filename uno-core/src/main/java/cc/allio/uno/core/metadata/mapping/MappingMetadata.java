package cc.allio.uno.core.metadata.mapping;

import cc.allio.uno.core.metadata.Metadata;
import cc.allio.uno.core.metadata.convert.Converter;

import java.util.Map;

/**
 * 映射关系元数据，定义模版方法
 *
 * @author j.x
 * @date 2022/9/13 13:27
 * @since 1.1.0
 */
public interface MappingMetadata extends Map<MappingField, MappingField> {

    /**
     * 添加映射关系
     *
     * @param source 原字段映射
     * @param target 目标字段映射
     */
    void addMapping(MappingField source, MappingField target);

    /**
     * 根据原字段名称获取目标字段
     *
     * @param sourceFieldName 原字段名称
     * @return 目标字段映射
     */
    MappingField get(String sourceFieldName);

    /**
     * 删除目标字段映射关系
     *
     * @param sourceFieldName 原字段名称
     */
    void remove(String sourceFieldName);

    /**
     * 判断是否包含原字段
     *
     * @param sourceFieldName 原字段名称
     * @return true：包含 false：不包含
     */
    boolean containsKey(String sourceFieldName);

    /**
     * 获取元数据映射转换器
     *
     * @return 转换器实例
     */
    Converter<? extends Metadata> getConverter();
}
