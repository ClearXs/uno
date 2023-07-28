package cc.allio.uno.data.orm.type;

/**
 * java type impl
 *
 * @author jiangwei
 * @date 2023/1/13 15:58
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
