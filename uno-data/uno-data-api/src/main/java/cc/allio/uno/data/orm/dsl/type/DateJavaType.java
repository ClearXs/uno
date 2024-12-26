package cc.allio.uno.data.orm.dsl.type;

import cc.allio.uno.core.type.Types;

import java.util.Date;

/**
 * date java type
 *
 * @author j.x
 * @since 1.1.4
 */
public class DateJavaType extends JavaTypeImpl<Date> {
    @Override
    public Class<Date> getJavaType() {
        return Types.DATE;
    }

    @Override
    public boolean equalsTo(Class<?> otherJavaType) {
        return Date.class.isAssignableFrom(otherJavaType);
    }
}
