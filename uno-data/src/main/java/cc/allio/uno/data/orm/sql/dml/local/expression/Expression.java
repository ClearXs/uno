package cc.allio.uno.data.orm.sql.dml.local.expression;

import cc.allio.uno.core.util.template.ExpressionTemplate;

import java.util.Collections;
import java.util.Map;

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

    /**
     * 获取占位符SQL
     *
     * @return 占位符SQL
     */
    default String getPrepareSQL() {
        return getSQL();
    }

    /**
     * 获取字面量SQL
     *
     * @return 字面量
     */
    default String getLiteralSQL() {
        return getSQL();
    }

    /**
     * 获取表达式value与key的映射
     *
     * @return key 表达式占位符， value值
     */
    default Map<String, Object> getExpMapping() {
        return Collections.emptyMap();
    }
}
