package cc.allio.uno.data.orm.sql.dml.local.expression;

import cc.allio.uno.core.util.template.ExpressionTemplate;
import cc.allio.uno.core.util.template.Tokenizer;
import cc.allio.uno.data.orm.dialect.Dialect;
import cc.allio.uno.data.orm.sql.ColumnGroup;
import cc.allio.uno.data.orm.sql.Table;
import com.google.common.collect.Maps;
import lombok.Getter;

import java.util.Map;

/**
 * 表达式上下文对象，作用多个表达式，复用模板表达式
 *
 * @author jiangwei
 * @date 2023/1/5 18:26
 * @since 1.0
 */
@Getter
public class DefaultExpressionContext implements ExpressionContext {
    // 数据库方言对象
    private final Dialect dialect;
    // 表达式模板
    private final ExpressionTemplate expressionTemplate;
    // 表达式变量L
    private final Map<String, ExpressionValue> expressionVariables;
    private final Tokenizer tokenizer;
    private final ColumnGroup columnGroup;

    private Table table;

    public DefaultExpressionContext() {
        this(null);
    }

    public DefaultExpressionContext(Dialect dialect) {
        this(dialect, Tokenizer.HASH_BRACE);
    }

    public DefaultExpressionContext(Dialect dialect, Tokenizer tokenizer) {
        this.dialect = dialect;
        this.tokenizer = tokenizer;
        this.expressionTemplate = ExpressionTemplate.createTemplate(tokenizer);
        this.expressionVariables = Maps.newHashMap();
        this.columnGroup = new ColumnGroup();
    }

    @Override
    public Table getTable() {
        return table;
    }

    @Override
    public void setTable(Table table) {
        this.table = table;
    }
}
