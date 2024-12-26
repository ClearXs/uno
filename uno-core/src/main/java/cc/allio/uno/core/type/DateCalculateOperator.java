package cc.allio.uno.core.type;

import cc.allio.uno.core.util.DateUtil;
import org.springframework.util.ObjectUtils;

import java.util.Date;

/**
 * Date类型转换器，以yyyy-MM-dd HH:mm:ss解析
 *
 * @author j.x
 * @since 1.0
 */
public class DateCalculateOperator extends UnsupportedCalculateOperator<Date> {

    @Override
    public Date convert(Object target, Class<?> maybeType) {
        if (Types.isDate(target.getClass())) {
            return (Date) target;
        }
        if (ObjectUtils.isEmpty(target)) {
            return defaultValue();
        } else {
            return DateUtil.parse(target.toString());
        }
    }

    @Override
    public String fromString(Object target) {
        return DateUtil.format(convert(target, Date.class), DateUtil.PATTERN_DATETIME);
    }

    @Override
    public Date defaultValue() {
        return new Date();
    }

    @Override
    public Class<?> getType() {
        return Types.DATE;
    }
}
