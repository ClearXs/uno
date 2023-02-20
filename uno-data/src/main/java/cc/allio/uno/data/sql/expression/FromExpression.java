package cc.allio.uno.data.sql.expression;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.data.sql.SQLException;
import cc.allio.uno.data.sql.Statement;
import cc.allio.uno.data.sql.Table;
import com.google.common.collect.Lists;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.List;
import java.util.function.Function;

/**
 * from expression
 *
 * @author jiangwei
 * @date 2023/1/5 19:45
 * @since 1.1.4
 */
public class FromExpression extends ValueExpression implements SingleExpression {

    /**
     * @see ValuePlaceholder#putAttribute(String, Object)
     */
    static final String TABLE_KEY = "table";

    public FromExpression(Table table, ExpressionContext context) {
        super(context, new Object[]{table.getName()});
        valuePlaceholder.putAttribute(TABLE_KEY, table);
    }

    @Override
    public String getSQL() {
        return valuePlaceholder.get(0).getT1();
    }

    @Override
    protected Function<ValuePlaceholder, List<Tuple2<String, ExpressionValue>>> getValuePlaceholder() {
        return valuePlaceholder -> {
            Table table = valuePlaceholder.get(TABLE_KEY, Table.class).orElseThrow(() -> new SQLException("Table instance not exist"));
            int index = 0;
            List<Tuple2<String, ExpressionValue>> valuePlaceholders = Lists.newArrayList();
            valuePlaceholders.add(
                    Tuples.of(
                            context.getTokenizer().createTokenString(table.getName() + StringPool.UNDERSCORE + Statement.FROM + StringPool.UNDERSCORE + index),
                            ExpressionValue.of(getSingleValue())
                    )
            );
            return valuePlaceholders;
        };
    }
}
