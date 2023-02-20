package cc.allio.uno.data.sql.expression;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.data.sql.RuntimeColumn;
import cc.allio.uno.data.sql.Statement;
import reactor.util.function.Tuple2;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * not null expression
 *
 * @author jiangwei
 * @date 2023/1/6 13:45
 * @since 1.1.4
 */
public class NotNullExpression extends ColumnExpression implements ConditionExpression {

    public NotNullExpression(RuntimeColumn column, ExpressionContext context) {
        super(column, context);
    }

    @Override
    public String getSQL() {
        return column + StringPool.SPACE + getSyntax();
    }

    @Override
    public String getSyntax() {
        return Statement.NOT_NULL;
    }

    @Override
    protected Function<ValuePlaceholder, List<Tuple2<String, ExpressionValue>>> getValuePlaceholder() {
        return obj -> Collections.emptyList();
    }
}
