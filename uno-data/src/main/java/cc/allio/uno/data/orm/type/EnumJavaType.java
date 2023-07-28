package cc.allio.uno.data.orm.type;

/**
 * enum java type
 *
 * @author jiangwei
 * @date 2023/7/10 18:49
 * @since 1.1.4
 */
public class EnumJavaType extends JavaTypeImpl<Enum> {
    @Override
    public Class<Enum> getJavaType() {
        return Enum.class;
    }
}
