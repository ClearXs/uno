package cc.allio.uno.data.sql.expression;

import cc.allio.uno.core.util.template.ExpressionTemplate;

/**
 * <b>SQL表达式。</b>
 * 每个实现类衍生自SQL关键字，如FROM、IN、NOT IN... 表达式具备模版站位的能力
 *
 * @author jiangwei
 * @date 2023/1/5 10:02
 * @see ExpressionTemplate
 * @since 1.1.4
 */
public interface Expression {

    /**
     * 返回当前表达式SQL
     *
     * @return SQL字符串
     */
    String getSQL();
}
