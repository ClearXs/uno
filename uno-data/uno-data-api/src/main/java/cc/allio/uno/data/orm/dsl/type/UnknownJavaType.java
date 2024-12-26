package cc.allio.uno.data.orm.dsl.type;

/**
 * unknown java type
 *
 * @author j.x
 * @since 1.1.4
 */
public class UnknownJavaType extends JavaTypeImpl<Object> {
    @Override
    public Class getJavaType() {
        throw new UnsupportedOperationException("unknown non support operator");
    }

    @Override
    public boolean equalsTo(Class<?> otherJavaType) {
        throw new UnsupportedOperationException("unknown non support operator");
    }
}
