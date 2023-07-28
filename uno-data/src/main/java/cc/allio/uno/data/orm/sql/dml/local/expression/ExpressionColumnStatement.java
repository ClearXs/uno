package cc.allio.uno.data.orm.sql.dml.local.expression;

import cc.allio.uno.data.orm.sql.ColumnStatement;
import cc.allio.uno.data.orm.sql.RuntimeColumn;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Set;

/**
 * expression column statement
 *
 * @author jiangwei
 * @date 2023/1/9 18:30
 * @since 1.1.4
 */
public abstract class ExpressionColumnStatement<T extends ExpressionColumnStatement<T>> extends ExpressionStatement<T> implements ColumnStatement<T> {

    /**
     * 当前语句的Column
     */
    private final Set<RuntimeColumn> columns;

    protected ExpressionColumnStatement(ExpressionContext expressionContext) {
        super(expressionContext);
        this.columns = Sets.newHashSet();
    }

    @Override
    public Collection<RuntimeColumn> getColumns() {
        return expressionContext.getColumnGroup();
    }

    /**
     * 添加当前语句私有化的{@link RuntimeColumn}
     *
     * @param column RuntimeColumn实例
     */
    protected void addPrivatizationColumn(RuntimeColumn column) {
        columns.add(column);
    }
}
