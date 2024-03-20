package cc.allio.uno.data.orm.dsl.type;

import cc.allio.uno.core.type.Types;

import java.math.BigDecimal;

/**
 * big decimal java type
 *
 * @author j.x
 * @date 2023/1/13 18:16
 * @since 1.1.4
 */
public class BigDecimalJavaType extends JavaTypeImpl<BigDecimal> {

    @Override
    public Class<BigDecimal> getJavaType() {
        return Types.BIG_DECIMAL;
    }

    @Override
    public boolean equalsTo(Class<?> otherJavaType) {
        return BigDecimal.class.isAssignableFrom(otherJavaType);
    }
}
