package cc.allio.uno.data.sql.query;

import cc.allio.uno.data.sql.FromStatement;
import cc.allio.uno.data.sql.Statement;
import cc.allio.uno.data.sql.From;
import lombok.Getter;

/**
 * 抽象的SQL语句代理，实现相关的SQL集联操作
 *
 * @author jiangwei
 * @date 2022/9/30 14:02
 * @since 1.1.0
 */
@Getter
public abstract class AbstractStatementDelegate implements QuerySQLAssociation {

    private final Select<SelectStatement> select;
    private final From<FromStatement> from;
    private final Where<WhereStatement> where;
    private final Group<GroupStatement> group;
    private final Order<OrderStatement> order;
    private final Limit<LimitStatement> limit;
    private final Statement<?> parent;

    protected AbstractStatementDelegate(Select<SelectStatement> select, From<FromStatement> from, Where<WhereStatement> where, Group<GroupStatement> group, Order<OrderStatement> order, Limit<LimitStatement> limit) {
        this(select, from, where, group, order, limit, null);
    }

    protected AbstractStatementDelegate(Select<SelectStatement> select, From<FromStatement> from, Where<WhereStatement> where, Group<GroupStatement> group, Order<OrderStatement> order, Limit<LimitStatement> limit, Statement<?> parent) {
        this.select = select;
        this.from = from;
        this.where = where;
        this.group = group;
        this.order = order;
        this.parent = parent;
        this.limit = limit;
    }

    @Override
    public SelectDelegate thenSelect() {
        return then(new SelectStatementDelegate(select, from, where, group, order, limit, parent));
    }

    @Override
    public QueryFrom thenFrom() {
        return then(new QueryFromStatement(select, from, where, group, order, limit, parent));
    }

    @Override
    public WhereDelegate thenWhere() {
        return then(new WhereStatementDelegate(select, from, where, group, order, limit, parent));
    }

    @Override
    public OrderDelegate thenOrder() {
        return then(new OrderStatementDelegate(select, from, where, group, order, limit, parent));
    }

    @Override
    public GroupDelegate thenGroup() {
        return then(new GroupStatementDelegate(select, from, where, group, order, limit, parent));
    }

    @Override
    public LimitDelegate thenLimit() {
        return then(new LimitStatementDelegate(select, from, where, group, order, limit, parent));
    }
}
