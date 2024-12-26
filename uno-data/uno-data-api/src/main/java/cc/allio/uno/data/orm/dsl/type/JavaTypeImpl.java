package cc.allio.uno.data.orm.dsl.type;

/**
 * java type impl
 *
 * @author j.x
 * @since 1.1.4
 */
public abstract class JavaTypeImpl<T> implements JavaType<T> {

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return getJavaType().equals(((JavaTypeImpl<?>) o).getJavaType());
    }
}
