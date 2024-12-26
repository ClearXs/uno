package cc.allio.uno.core.type;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * BigDecimal类型转换器，可能抛出NumberFormatException异常
 *
 * @author j.x
 * @since 1.0
 */
public class BigDecimalTypeOperator implements TypeOperator<BigDecimal> {

    @Override
    public BigDecimal convert(Object target, Class<?> maybeType) {
        return Types.parseBigDecimal(target);
    }

    @Override
    public int signum(Object target) {
        return convert(target).signum();
    }

    @Override
    public String fromString(Object target) {
        return convert(target).toString();
    }

    @Override
    public BigDecimal add(BigDecimal origin, BigDecimal passive) {
        return origin.add(passive).setScale(2, RoundingMode.UP);
    }

    @Override
    public BigDecimal subtract(BigDecimal origin, BigDecimal passive) {
        return origin.subtract(passive).setScale(2, RoundingMode.UP);
    }

    @Override
    public BigDecimal multiply(BigDecimal origin, BigDecimal passive) {
        return origin.multiply(passive).setScale(2, RoundingMode.UP);
    }

    @Override
    public BigDecimal divide(BigDecimal origin, BigDecimal passive) {
        return origin.divide(passive, 2, RoundingMode.UP);
    }

    @Override
    public BigDecimal defaultValue() {
        return BigDecimal.ZERO;
    }

    @Override
    public Class<?> getType() {
        return Types.BIG_DECIMAL;
    }
}
