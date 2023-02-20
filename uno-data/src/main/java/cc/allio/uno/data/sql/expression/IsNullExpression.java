package cc.allio.uno.data.sql.expression;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.data.sql.RuntimeColumn;
import cc.allio.uno.data.sql.Statement;
import reactor.util.function.Tuple2;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * is null expression
 *
 * @author jiangwei
 * @date 2023/1/6 13:46
 * @since 1.1.4
 */
public class IsNullExpression extends ColumnExpression implements ConditionExpression {

    public IsNullExpression(RuntimeColumn column, ExpressionContext context) {
        super(column, context);
    }

    @Override
    public String getSQL() {
        return column + StringPool.SPACE + getSyntax();
    }

    @Override
    public String getSyntax() {
        return Statement.IS_NULL;
    }

    @Override
    protected Function<ValuePlaceholder, List<Tuple2<String, ExpressionValue>>> getValuePlaceholder() {
        return t -> Collections.emptyList();
    }
}
