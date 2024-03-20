package cc.allio.uno.data.orm.dsl;

import cc.allio.uno.core.api.EqualsTo;
import cc.allio.uno.core.type.TypeOperatorFactory;
import cc.allio.uno.data.orm.dsl.type.DataType;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Objects;
import java.util.Optional;

/**
 * SQL字段定义
 *
 * @author j.x
 * @date 2023/4/12 19:35
 * @since 1.1.4
 */
@Data
@Builder
@EqualsAndHashCode(of = {"dslName", "dataType"})
public class ColumnDef implements EqualsTo<ColumnDef> {

    // 字段名称
    private DSLName dslName;
    // 字段注释
    private String comment;
    // 数据类型
    private cc.allio.uno.data.orm.dsl.type.DataType dataType;
    // 默认值
    private Object defaultValue;
    // 是否为主键
    private boolean isPk;
    // 是否为外键
    private boolean isFk;
    // 是否不为null
    private boolean isNonNull;
    // 是否为null
    private boolean isNull;
    // 是否唯一
    private boolean isUnique;
    // 是否是删除标识字段
    private boolean isDeleted;
    // 未删除值
    private Object undeleted;
    // 已删除值
    private Object deleted;

    @Override
    public boolean equalsTo(ColumnDef other) {
        return Objects.equals(this.getDslName(), other.getDslName())
                && dataType.equalsTo(other.getDataType());
    }

    /**
     * 给定字段值按照当前{@link cc.allio.uno.data.orm.dsl.type.DataType}进行转换
     *
     * @param ori ori
     * @return 转换后的值
     */
    public Object castValue(Object ori) {
        return Optional.ofNullable(dataType)
                .map(DataType::getDslType)
                .flatMap(dslType -> {
                    int jdbcType = dslType.getJdbcType();
                    cc.allio.uno.data.orm.dsl.type.JavaType<?> javaType = cc.allio.uno.data.orm.dsl.type.TypeRegistry.getInstance().findJavaType(jdbcType);
                    return Optional.ofNullable(javaType);
                })
                .map(j -> TypeOperatorFactory.translator(j.getJavaType()))
                .map(typeOperator -> typeOperator.convert(ori))
                .orElse(null);
    }
}
