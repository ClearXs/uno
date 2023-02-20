package cc.allio.uno.data.sql.expression;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.data.sql.RuntimeColumn;
import cc.allio.uno.data.sql.Statement;
import com.google.common.collect.Lists;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.List;
import java.util.function.Function;

/**
 * like expression
 *
 * @author jiangwei
 * @date 2023/1/6 10:17
 * @since 1.1.4
 */
public class LikeExpression extends ColumnExpression implements ConditionExpression {

    static final String STRATEGY_KEY = "strategy";

    public LikeExpression(RuntimeColumn column, ExpressionContext context, Strategy likeStrategy) {
        super(column, context);
        valuePlaceholder.putAttribute(STRATEGY_KEY, likeStrategy);
    }

    @Override
    public String getSQL() {
        // column LIKE %{{..}}
        return column +
                StringPool.SPACE + Statement.LIKE + StringPool.SPACE +
                // 添加 ''
                StringPool.SINGLE_QUOTE + valuePlaceholder.get(0).getT1() + StringPool.SINGLE_QUOTE;
    }

    @Override
    protected Function<ValuePlaceholder, List<Tuple2<String, ExpressionValue>>> getValuePlaceholder() {
        return valuePlaceholder -> {
            Strategy strategy = valuePlaceholder.get(STRATEGY_KEY, Strategy.class).orElse(Strategy.ALL);
            int index = 0;
            List<Tuple2<String, ExpressionValue>> valuePlaceholders = Lists.newLinkedList();
            String token = context.getTokenizer().createTokenString(column + StringPool.UNDERSCORE + Statement.LIKE + StringPool.UNDERSCORE + index);
            switch (strategy) {
                case ALL:
                    token = getSyntax() + token + getSyntax();
                    break;
                case LEFT:
                    token = getSyntax() + token;
                    break;
                case RIGHT:
                    token = token + getSyntax();
                    break;
            }
            valuePlaceholders.add(
                    Tuples.of(token, ExpressionValue.of(getSingleValue(StringPool.EMPTY)))
            );
            return valuePlaceholders;
        };
    }

    @Override
    public String getSyntax() {
        return Statement.$LIKE;
    }

    public enum Strategy {
        LEFT,
        RIGHT,
        ALL
    }
}
