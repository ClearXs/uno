package cc.allio.uno.data.query;

import cc.allio.uno.data.sql.From;
import cc.allio.uno.data.sql.FromStatement;
import cc.allio.uno.data.sql.expression.DefaultExpressionContext;
import cc.allio.uno.data.sql.expression.ExpressionContext;
import cc.allio.uno.data.sql.query.*;
import lombok.Getter;

@Getter
public class QueryFilter {

    // 查询
    private final SelectDelegate selectDelegate;
    private final QueryFrom fromDelegate;
    private final WhereDelegate whereDelegate;
    private final OrderDelegate orderDelegate;
    private final GroupDelegate groupDelegate;
    private final LimitDelegate limitDelegate;

    /**
     * 关联查询包装器
     */
    private QueryWrapper queryWrapper;

    public QueryFilter() {
        ExpressionContext expressionContext = new DefaultExpressionContext();
        SelectStatement select = new SelectStatement(expressionContext);
        FromStatement from = new FromStatement(expressionContext);
        WhereStatement where = new WhereStatement(expressionContext);
        GroupStatement group = new GroupStatement(expressionContext);
        OrderStatement order = new OrderStatement(expressionContext);
        LimitStatement limit = new LimitStatement();
        this.selectDelegate = new SelectStatementDelegate(select, from, where, group, order, limit);
        this.fromDelegate = new QueryFromStatement(select, from, where, group, order, limit);
        this.whereDelegate = new WhereStatementDelegate(select, from, where, group, order, limit);
        this.orderDelegate = new OrderStatementDelegate(select, from, where, group, order, limit);
        this.groupDelegate = new GroupStatementDelegate(select, from, where, group, order, limit);
        this.limitDelegate = new LimitStatementDelegate(select, from, where, group, order, limit);

    }

    /**
     * select SQL语句
     *
     * @return SELECT实例对象
     * @see Select
     */
    public SelectDelegate selectSql() {
        return selectDelegate;
    }

    /**
     * from SQL语句
     *
     * @return FROM实例
     * @see From
     */
    public QueryFrom fromSql() {
        return fromDelegate;
    }

    /**
     * WHERE SQL语句
     *
     * @return Where实例对象
     * @see Where
     */
    public WhereDelegate whereSql() {
        return whereDelegate;
    }

    /**
     * order SQL语句
     *
     * @return Order实例对象
     * @see Order
     */
    public OrderDelegate orderSql() {
        return orderDelegate;
    }

    /**
     * group SQL语句
     *
     * @return Group实例对象
     * @see Group
     */
    public GroupDelegate groupSql() {
        return groupDelegate;
    }

    /**
     * limit SQl语句
     *
     * @return Limit实例对象
     * @see Limit
     */
    public LimitDelegate limitSql() {
        return limitDelegate;
    }

    /**
     * 添加QueryWrapper对象
     *
     * @param queryWrapper QueryWrapper实例
     * @return QueryFilter实例
     */
    public QueryFilter addQueryWrapper(QueryWrapper queryWrapper) {
        this.queryWrapper = queryWrapper;
        return this;
    }
}
