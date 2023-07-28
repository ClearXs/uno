package cc.allio.uno.data.orm.sql.dml.local.expression;

/**
 * Condition Expression
 *
 * @author jiangwei
 * @date 2023/1/5 17:03
 * @since 1.1.4
 */
public interface ConditionExpression extends Expression {

    /**
     * 获取当前表达式语法
     *
     * @return 语法
     */
    String getSyntax();
}
