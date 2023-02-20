package cc.allio.uno.data.sql.expression;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.data.sql.RuntimeColumn;
import cc.allio.uno.data.sql.Statement;
import com.google.common.collect.Lists;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * between expression
 *
 * @author jiangwei
 * @date 2023/1/6 13:26
 * @since 1.1.4
 */
public class BetweenExpression extends ColumnExpression implements ConditionExpression {

    public static final String STRATEGY_KEY = "strategy";

    public BetweenExpression(RuntimeColumn column, ExpressionContext context, Strategy strategy) {
        super(column, context, column.getValue());
        valuePlaceholder.putAttribute(STRATEGY_KEY, strategy);
    }

    @Override
    public String getSQL() {
        String syntax = getSyntax();
        Object[] paris = valuePlaceholder.getList().stream().map(Tuple2::getT1).toArray(Object[]::new);
        if (paris.length == 2) {
            syntax = String.format(syntax, paris);
        }
        return column + StringPool.SPACE + syntax;
    }

    @Override
    protected Function<ValuePlaceholder, List<Tuple2<String, ExpressionValue>>> getValuePlaceholder() {
        return valuePlaceholder -> {
            List<Tuple2<String, ExpressionValue>> valuePlaceholders = Lists.newLinkedList();
            Optional<Tuple2<Object, Object>> doubleValue = getDoubleValue();
            doubleValue.ifPresent(pairs -> {
                for (int index = 0; index < 2; index++) {
                    Object value = pairs.get(index);
                    valuePlaceholders.add(
                            Tuples.of(
                                    context.getTokenizer().createTokenString(column + StringPool.UNDERSCORE + "bt" + index),
                                    ExpressionValue.of(value)
                            ));
                }
            });
            return valuePlaceholders;
        };
    }

    @Override
    public String getSyntax() {
        Strategy strategy = valuePlaceholder.get(STRATEGY_KEY, Strategy.class).orElse(Strategy.IN);
        switch (strategy) {
            case IN:
                // BETWEEN %s AND %s
                return Statement.BETWEEN + StringPool.SPACE + StringPool.FormatterConversion.STRING + StringPool.SPACE + Statement.AND + StringPool.SPACE + StringPool.FormatterConversion.STRING;
            case NOT:
                // NOT BETWEEN %s AND %s
                return Statement.NOT_BETWEEN + StringPool.SPACE + StringPool.FormatterConversion.STRING + StringPool.SPACE + Statement.AND + StringPool.SPACE + StringPool.FormatterConversion.STRING;
            default:
                return StringPool.EMPTY;
        }
    }

    public enum Strategy {
        NOT,
        IN
    }
}
