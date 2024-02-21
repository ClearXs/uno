package cc.allio.uno.data.orm.dsl;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.data.orm.dsl.type.JavaType;
import cc.allio.uno.data.orm.dsl.type.JdbcType;
import cc.allio.uno.data.orm.dsl.type.TypeRegistry;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collection;

/**
 * 预处理SQL值对象
 *
 * @author jiangwei
 * @date 2023/4/16 15:47
 * @since 1.1.4
 */
@Data
@AllArgsConstructor(staticName = "of")
public class PrepareValue {

    /**
     * index
     */
    private int index;
    /**
     * 对应的column，变换为column_index
     */
    private String column;
    /**
     * value
     */
    private Object value;
    /**
     * javaType
     */
    private JavaType<?> javaType;
    /**
     * jdbcType
     */
    private Collection<JdbcType> jdbcType;

    /**
     * 获取原始的column（去掉column_index）
     *
     * @return column
     */
    public String getOriginColumn() {
        return column.substring(column.indexOf(StringPool.UNDERSCORE));
    }

    /**
     * 根据index和value创建PrepareValue实例
     *
     * @param index index
     * @param value value
     * @return PrepareValue
     */
    public static PrepareValue of(int index, String column, Object value) {
        JavaType<?> javaType = TypeRegistry.getInstance().guessJavaType(value);
        Collection<JdbcType> jdbcTypes = TypeRegistry.getInstance().findJdbcTypeFindJavaType(javaType);
        return of(index, column + StringPool.UNDERSCORE + index, value, javaType, jdbcTypes);
    }
}
