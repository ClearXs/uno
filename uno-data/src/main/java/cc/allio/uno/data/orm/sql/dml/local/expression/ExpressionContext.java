package cc.allio.uno.data.orm.sql.dml.local.expression;

import cc.allio.uno.core.util.template.ExpressionTemplate;
import cc.allio.uno.core.util.template.Tokenizer;
import cc.allio.uno.data.orm.dialect.Dialect;
import cc.allio.uno.data.orm.sql.ColumnGroup;
import cc.allio.uno.data.orm.sql.Table;

import java.util.Map;

/**
 * 表达式上下文
 *
 * @author jiangwei
 * @date 2023/1/6 09:16
 * @since 1.1.4
 */
public interface ExpressionContext {

    /**
     * 获取当前执行表达式数据库方言对象
     *
     * @return Dialect实例
     */
    Dialect getDialect();

    /**
     * 获取{@link ExpressionTemplate}实例对象
     *
     * @return ExpressionTemplate实例
     */
    ExpressionTemplate getExpressionTemplate();

    /**
     * 获取{@link ExpressionTemplate}构建变量对象
     *
     * @return ExpressionVariables对象
     */
    Map<String, ExpressionValue> getExpressionVariables();

    /**
     * 获取Tokenizer
     *
     * @return Tokenizer实例
     */
    Tokenizer getTokenizer();

    /**
     * 获取ColumnGroup实例
     *
     * @return ColumnGroup
     */
    ColumnGroup getColumnGroup();

    /**
     * 获取Table实例对象
     *
     * @return
     */
    Table getTable();

    /**
     * 设置Table实例对象
     *
     * @param table table实例对象
     */
    void setTable(Table table);

    /**
     * empty 的上下文对象。所有的操作抛出{@link UnsupportedOperationException}
     */
    ExpressionContext EMPTY = new ExpressionContext() {
        @Override
        public Dialect getDialect() {
            throw new UnsupportedOperationException("unsupported operations");
        }

        @Override
        public ExpressionTemplate getExpressionTemplate() {
            throw new UnsupportedOperationException("unsupported operations");
        }

        @Override
        public Map<String, ExpressionValue> getExpressionVariables() {
            throw new UnsupportedOperationException("unsupported operations");
        }

        @Override
        public Tokenizer getTokenizer() {
            throw new UnsupportedOperationException("unsupported operations");
        }

        @Override
        public ColumnGroup getColumnGroup() {
            throw new UnsupportedOperationException("unsupported operations");
        }

        @Override
        public Table getTable() {
            throw new UnsupportedOperationException("unsupported operations");
        }

        @Override
        public void setTable(Table table) {

        }
    };
}
