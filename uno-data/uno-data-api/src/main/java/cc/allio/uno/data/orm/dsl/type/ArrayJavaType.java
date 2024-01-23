package cc.allio.uno.data.orm.dsl.type;

import java.util.Objects;

/**
 * java array
 *
 * @author jiangwei
 * @date 2023/4/14 18:58
 * @since 1.1.4
 */
public class ArrayJavaType extends JavaTypeImpl<Object> {
    @Override
    public Class getJavaType() {
        throw new UnsupportedOperationException("array non support operator");
    }

    @Override
    public boolean equalsTo(Class<?> otherJavaType) {
        return otherJavaType.isArray();
    }

    @Override
    public boolean equals(Object o) {
        return Objects.equals(this, o);
    }
}
