package cc.allio.uno.data.orm.dsl.type;

/**
 * string java type
 *
 * @author j.x
 * @since 1.1.4
 */
public class StringJavaType implements JavaType<String> {

    @Override
    public Class<String> getJavaType() {
        return String.class;
    }

    @Override
    public boolean equalsTo(Class<?> otherJavaType) {
        return String.class.isAssignableFrom(otherJavaType);
    }
}
