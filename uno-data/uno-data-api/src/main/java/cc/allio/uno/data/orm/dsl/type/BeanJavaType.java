package cc.allio.uno.data.orm.dsl.type;

import lombok.AllArgsConstructor;

/**
 * java bean type
 *
 * @author j.x
 * @since 1.1.8
 */
@AllArgsConstructor
public class BeanJavaType<T> extends JavaTypeImpl<T> {

    private final Class<T> beanType;

    @Override
    public boolean equalsTo(Class<?> other) {
        return beanType.isAssignableFrom(other);
    }

    @Override
    public Class<T> getJavaType() {
        return beanType;
    }
}
