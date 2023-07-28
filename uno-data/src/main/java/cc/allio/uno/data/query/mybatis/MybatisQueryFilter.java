package cc.allio.uno.data.query.mybatis;

import cc.allio.uno.core.util.ObjectUtils;
import cc.allio.uno.data.orm.sql.From;
import cc.allio.uno.data.orm.sql.FromStatement;
import cc.allio.uno.data.orm.sql.dml.*;
import cc.allio.uno.data.orm.sql.dml.local.*;
import cc.allio.uno.data.orm.sql.dml.local.expression.DefaultExpressionContext;
import cc.allio.uno.data.orm.sql.dml.local.expression.Expression;
import cc.allio.uno.data.orm.sql.dml.local.expression.ExpressionContext;
import cc.allio.uno.data.orm.sql.dml.local.expression.StatementExpression;
import com.google.common.collect.Maps;
import com.google.common.collect.Streams;

import java.util.Map;
import java.util.stream.Stream;

/**
 * MybatisQueryFilter
 *
 * @author jiangwei
 * @date 2023/4/17 18:11
 * @since 1.1.4
 */
public class MybatisQueryFilter implements QueryFilter {

    // 查询
    private final SelectDelegate select;
    private final SelectFrom from;
    private final WhereDelegate where;
    private final OrderDelegate order;
    private final GroupDelegate group;
    private final LimitDelegate limit;
    private Map<String, Object> context;

    /**
     * 关联查询包装器
     */
    private QueryWrapper queryWrapper;

    public MybatisQueryFilter() {
        ExpressionContext expressionContext = new DefaultExpressionContext();
        SelectStatement select = new SelectStatement(expressionContext);
        FromStatement from = new FromStatement(expressionContext);
        WhereStatement where = new WhereStatement(expressionContext);
        GroupStatement group = new GroupStatement(expressionContext);
        OrderStatement order = new OrderStatement(expressionContext);
        LimitStatement limit = new LimitStatement();
        this.select = new SelectStatementDelegate(select, from, where, group, order, limit);
        this.from = new QueryFromStatement(select, from, where, group, order, limit);
        this.where = new WhereStatementDelegate(select, from, where, group, order, limit);
        this.order = new OrderStatementDelegate(select, from, where, group, order, limit);
        this.group = new GroupStatementDelegate(select, from, where, group, order, limit);
        this.limit = new LimitStatementDelegate(select, from, where, group, order, limit);
    }

    @Override
    public String getSQL() {
        throw new UnsupportedOperationException("mybatis query filter un support");
    }

    @Override
    public void setQueryWrapper(QueryWrapper queryWrapper) {
        this.queryWrapper = queryWrapper;
    }

    @Override
    public QueryWrapper getQueryWrapper() {
        return queryWrapper;
    }

    /**
     * select SQL语句
     *
     * @return SELECT实例对象
     * @see Select
     */
    public SelectDelegate selectSql() {
        return select;
    }

    /**
     * from SQL语句
     *
     * @return FROM实例
     * @see From
     */
    public SelectFrom fromSql() {
        return from;
    }

    /**
     * WHERE SQL语句
     *
     * @return Where实例对象
     * @see Where
     */
    public WhereDelegate whereSql() {
        return where;
    }

    /**
     * order SQL语句
     *
     * @return Order实例对象
     * @see Order
     */
    public OrderDelegate orderSql() {
        return order;
    }

    /**
     * group SQL语句
     *
     * @return Group实例对象
     * @see Group
     */
    public GroupDelegate groupSql() {
        return group;
    }

    /**
     * limit SQl语句
     *
     * @return Limit实例对象
     * @see Limit
     */
    public LimitDelegate limitSql() {
        return limit;
    }

    /**
     * 获取mybatis数据上下文
     *
     * @return the map
     * @see {@link Expression#getExpMapping()}
     */
    public Map<String, Object> getContext() {
        if (ObjectUtils.isEmpty(context)) {
            Stream<Expression> se = select.getExpressions().stream();
            Stream<Expression> fr = from.getExpressions().stream();
            Stream<Expression> wh = where.getExpressions().stream();
            Stream<Expression> or = order.getExpressions().stream();
            Stream<Expression> gr = group.getExpressions().stream();
            Stream<Expression> li = limit.getExpressions().stream();
            this.context = Streams.concat(se, fr, wh, or, gr, li)
                    .filter(p -> !StatementExpression.class.isAssignableFrom(p.getClass()))
                    .map(Expression::getExpMapping)
                    .reduce(Maps.newHashMap(), (o, i) -> {
                        o.putAll(i);
                        return o;
                    });
        }
        return context;
    }

}
