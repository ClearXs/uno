package cc.allio.uno.data.orm.sql.dml.local.expression;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.util.StringUtils;
import cc.allio.uno.data.orm.sql.Condition;
import cc.allio.uno.data.orm.sql.RuntimeColumn;
import cc.allio.uno.data.orm.sql.Statement;
import reactor.util.function.Tuple2;

import java.util.List;
import java.util.function.Function;

/**
 * column expression
 *
 * @author jiangwei
 * @date 2023/1/6 10:10
 * @since 1.1.4
 */
public class PlainExpression extends ColumnExpression {

    private String alias;

    public PlainExpression(RuntimeColumn column, ExpressionContext context) {
        super(column, context);
    }

    public void alias(String alias) {
        this.alias = alias;
    }

    @Override
    public String getSQL() {
        StringBuilder sql = new StringBuilder();
        sql.append(column);
        // 添加alias
        if (StringUtils.isNotBlank(alias)) {
            sql.append(StringPool.SPACE).append(Statement.AS).append(StringPool.SPACE).append(alias);
        }
        // 添加 ASC DESC等等条件
        Condition condition = runtimeColumn.getCondition();
        if (condition != null) {
            sql.append(StringPool.SPACE).append(condition.getName());
        }
        return sql.toString();
    }

    @Override
    protected Function<ValuePlaceholder, List<Tuple2<String, ExpressionValue>>> getValuePlaceholder() {
        return null;
    }
}
