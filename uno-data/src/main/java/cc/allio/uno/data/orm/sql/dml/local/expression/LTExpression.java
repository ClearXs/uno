package cc.allio.uno.data.orm.sql.dml.local.expression;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.data.orm.sql.RuntimeColumn;
import cc.allio.uno.data.orm.sql.Statement;
import com.google.common.collect.Lists;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.List;
import java.util.function.Function;

/**
 * lt expression
 *
 * @author jiangwei
 * @date 2023/1/5 19:11
 * @since 1.1.4
 */
public class LTExpression extends ColumnExpression implements ConditionExpression {

    public LTExpression(RuntimeColumn column, ExpressionContext context) {
        super(column, context);
    }

    @Override
    protected Function<ValuePlaceholder, List<Tuple2<String, ExpressionValue>>> getValuePlaceholder() {
        return parameter -> {
            int index = 0;
            List<Tuple2<String, ExpressionValue>> valuePlaceholders = Lists.newLinkedList();
            valuePlaceholders.add(
                    Tuples.of(
                            context.getTokenizer().createTokenString(column + StringPool.UNDERSCORE + Statement.LT + StringPool.UNDERSCORE + index),
                            ExpressionValue.of(getSingleValue(StringPool.EMPTY))
                    )
            );
            return valuePlaceholders;
        };
    }

    @Override
    public String getSyntax() {
        return Statement.LT_SYMBOL;
    }

    @Override
    public String getSQL() {
        // column < placeholder
        return column +
                StringPool.SPACE + getSyntax() + StringPool.SPACE +
                valuePlaceholder.get(0).getT1();
    }
}
