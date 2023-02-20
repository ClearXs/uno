package cc.allio.uno.data.sql.expression;

import cc.allio.uno.core.util.StringUtils;
import cc.allio.uno.data.sql.RuntimeColumn;

import java.util.Collections;

/**
 * Column
 *
 * @author jiangwei
 * @date 2023/1/5 17:00
 * @since 1.1.4
 */
public abstract class ColumnExpression extends ValueExpression {

    protected final String column;
    protected final ExpressionContext context;
    protected final RuntimeColumn runtimeColumn;

    /**
     * @see ValuePlaceholder
     */
    public static final String COLUMN = "column";

    protected ColumnExpression(RuntimeColumn column, ExpressionContext context) {
        this(column, context, null);
    }

    protected ColumnExpression(RuntimeColumn column, ExpressionContext context, Object[] values) {
        super(context, values, Collections.emptyMap());
        this.column = StringUtils.camelToUnderline(column.getName());
        this.context = context;
        this.runtimeColumn = column;
        context.getColumnGroup().add(runtimeColumn);
        valuePlaceholder.putAttribute(COLUMN, column);
    }
}
