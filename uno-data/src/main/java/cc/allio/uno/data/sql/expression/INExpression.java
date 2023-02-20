package cc.allio.uno.data.sql.expression;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.data.sql.RuntimeColumn;
import cc.allio.uno.data.sql.Statement;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * in expression
 *
 * @author jiangwei
 * @date 2023/1/5 19:48
 * @since 1.1.4
 */
public class INExpression extends ColumnExpression implements ConditionExpression {

    public INExpression(RuntimeColumn column, ExpressionContext context) {
        super(column, context, column.getValue());
    }

    @Override
    public String getSyntax() {
        return Statement.IN;
    }

    @Override
    public String getSQL() {
        // column in (...)
        return column +
                StringPool.SPACE + getSyntax() + StringPool.SPACE
                + StringPool.LEFT_BRACKET
                + valuePlaceholder.getList().stream().map(Tuple2::getT1).collect(Collectors.joining(StringPool.COMMA))
                + StringPool.RIGHT_BRACKET;
    }

    @Override
    protected Function<ValuePlaceholder, List<Tuple2<String, ExpressionValue>>> getValuePlaceholder() {
        return parameter -> {
            AtomicInteger index = new AtomicInteger();
            return getValues().map(Arrays::stream).orElse(Stream.empty())
                    .map(value -> {
                        Tuple2<String, ExpressionValue> pairs = Tuples.of(
                                context.getTokenizer().createTokenString(column + StringPool.UNDERSCORE + getSyntax() + StringPool.UNDERSCORE + index),
                                ExpressionValue.of(value)
                        );
                        index.getAndIncrement();
                        return pairs;
                    })
                    .collect(Collectors.toList());
        };
    }
}
