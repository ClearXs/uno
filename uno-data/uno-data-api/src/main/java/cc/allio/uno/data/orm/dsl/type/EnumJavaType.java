package cc.allio.uno.data.orm.dsl.type;

/**
 * enum java type
 *
 * @author j.x
 * @date 2023/7/10 18:49
 * @since 1.1.4
 */
public class EnumJavaType extends JavaTypeImpl<Enum> {
    @Override
    public Class<Enum> getJavaType() {
        return Enum.class;
    }

    @Override
    public boolean equalsTo(Class<?> otherJavaType) {
        return Enum.class.isAssignableFrom(otherJavaType);
    }
}
