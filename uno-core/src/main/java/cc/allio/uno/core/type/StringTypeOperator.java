package cc.allio.uno.core.type;

/**
 * String类型转换器
 *
 * @author jiangwei
 * @date 2021/12/23 20:14
 * @since 1.0
 */
public class StringTypeOperator extends UnsupportedCalculateOperator<String> {
    @Override
    public String convert(Object target, Class<?> maybeType) {
        return target.toString();
    }

    @Override
    public String fromString(Object target) {
        return convert(target);
    }

    @Override
    public Class<?> getType() {
        return Types.STRING;
    }
}
