package cc.allio.uno.data.orm.dialect.type;

/**
 * string java type
 *
 * @author jiangwei
 * @date 2023/1/17 18:02
 * @since 1.1.4
 */
public class StringJavaType implements JavaType<String> {

    @Override
    public Class<String> getJavaType() {
        return String.class;
    }
}
