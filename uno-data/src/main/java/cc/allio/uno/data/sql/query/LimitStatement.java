package cc.allio.uno.data.sql.query;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.data.sql.SQLException;
import cc.allio.uno.data.sql.expression.Expression;

import java.util.Collection;
import java.util.Collections;

/**
 * limit statement
 *
 * @author jiangwei
 * @date 2023/1/11 17:51
 * @since 1.1.4
 */
public class LimitStatement implements Limit<LimitStatement> {

    // 赋予默认值
    private Integer limit = 0;
    private Integer offset = 0;

    @Override
    public Collection<Expression> getExpressions() {
        return Collections.emptyList();
    }

    @Override
    public void syntaxCheck() throws SQLException {
        if (limit == null) {
            throw new SQLException("Limit Statement must exist [limit data]");
        }
    }

    @Override
    public int order() {
        return LIMIT_ORDER;
    }

    @Override
    public LimitStatement limit(Integer number) {
        this.limit = number;
        return self();
    }

    @Override
    public LimitStatement offset(Integer number) {
        this.offset = number;
        return self();
    }

    @Override
    public String getSQL() throws SQLException {
        StringBuilder limitSQL = new StringBuilder();
        limitSQL.append(LIMIT).append(StringPool.SPACE).append(limit);
        if (offset != null) {
            limitSQL.append(StringPool.SPACE).append(OFFSET).append(StringPool.SPACE).append(offset);
        }
        return limitSQL.toString();
    }

}
