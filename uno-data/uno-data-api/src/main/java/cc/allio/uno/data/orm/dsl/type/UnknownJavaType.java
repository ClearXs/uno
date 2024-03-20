package cc.allio.uno.data.orm.dsl.type;

/**
 * unknown java type
 *
 * @author j.x
 * @date 2023/4/16 16:03
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
