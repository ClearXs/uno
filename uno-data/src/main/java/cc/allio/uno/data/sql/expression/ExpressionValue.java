package cc.allio.uno.data.sql.expression;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.util.type.Types;
import lombok.Getter;
import lombok.NonNull;

/**
 * column value
 *
 * @author jiangwei
 * @date 2023/1/6 11:12
 * @see Appender
 * @since 1.1.4
 */
@Getter
public class ExpressionValue {

    private ExpressionValue() {
    }

    /**
     * 值
     */
    private Object value;

    /**
     * 值的类型
     */
    private Class<?> type;

    /**
     * 追加策略
     */
    private Appender appender;

    /**
     * 基于当前值策略，返回修改后的值
     *
     * @return 修改后的值
     */
    public Object thenRun() {
        if (appender != null && Types.isString(type)) {
            String valueString = (String) value;
            switch (appender) {
                case LEFT_QUOTE:
                    valueString = StringPool.SINGLE_QUOTE + valueString;
                    break;
                case RIGHT_QUOTE:
                    valueString = value + StringPool.SINGLE_QUOTE;
                    break;
                case QUOTE:
                    valueString = StringPool.SINGLE_QUOTE + valueString + StringPool.SINGLE_QUOTE;
                    break;
                default:
                    break;
            }
            return valueString;
        }
        return value;
    }

    /**
     * 创建ExpressionValue
     *
     * @param value 值 非空
     * @return ExpressionValue
     */
    public static ExpressionValue of(@NonNull Object value) {
        Appender appender = Appender.NONE;
        if (Types.isString(value.getClass())) {
            appender = Appender.QUOTE;
        }
        return of(value, appender);
    }

    /**
     * 创建ExpressionValue
     *
     * @param value    值 非空
     * @param appender 追加策略
     * @return ExpressionValue实例
     */
    public static ExpressionValue of(@NonNull Object value, Appender appender) {
        ExpressionValue expressionValue = new ExpressionValue();
        expressionValue.value = value;
        expressionValue.type = value.getClass();
        expressionValue.appender = appender;
        return expressionValue;
    }

    /**
     * value注解策略。
     * <p>如：为值添加''</p>
     */
    public enum Appender {
        NONE,
        // 添加'value
        LEFT_QUOTE,
        // 添加value'
        RIGHT_QUOTE,
        // 添加'value'
        QUOTE
    }
}
