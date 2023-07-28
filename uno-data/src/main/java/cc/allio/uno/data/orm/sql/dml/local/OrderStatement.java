package cc.allio.uno.data.orm.sql.dml.local;

import cc.allio.uno.data.orm.sql.dml.local.expression.ExpressionColumnStatement;
import cc.allio.uno.data.orm.sql.dml.local.expression.ExpressionContext;
import cc.allio.uno.data.orm.sql.dml.local.expression.PlainExpression;
import cc.allio.uno.data.orm.sql.SQLException;
import cc.allio.uno.data.orm.sql.dml.Order;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

/**
 * 排序语句
 *
 * @author jiangwei
 * @date 2022/9/30 13:42
 * @since 1.1.0
 */
public class OrderStatement extends ExpressionColumnStatement<OrderStatement> implements Order<OrderStatement> {

    public OrderStatement(ExpressionContext expressionContext) {
        super(expressionContext);
    }

    @Override
    public OrderStatement byAsc(String fieldName) {
        return orderBy(fieldName, ASC);
    }

    @Override
    public OrderStatement byDesc(String fieldName) {
        return orderBy(fieldName, DESC);
    }

    @Override
    public OrderStatement orderBy(String fieldName, String order) {
        return orderBy(fieldName, OrderCondition.valueOf(order));
    }

    @Override
    public OrderStatement orderBy(String fieldName, OrderCondition orderCondition) {
        OrderColumn orderColumn = new OrderColumn(StringUtils.camelToUnderline(fieldName), null, orderCondition);
        PlainExpression plainExpression = new PlainExpression(orderColumn, expressionContext);
        lazyOffer(plainExpression, StringPool.COMMA);
        addPrivatizationColumn(orderColumn);
        return self();
    }

    @Override
    public void syntaxCheck() throws SQLException {

    }

    @Override
    public int order() {
        return ORDER_ORDER;
    }

    @Override
    protected String getStatementSyntax() {
        return ORDER;
    }
}
