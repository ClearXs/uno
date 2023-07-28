package cc.allio.uno.data.orm.sql.dml.local;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.util.StringUtils;
import cc.allio.uno.core.util.template.Tokenizer;
import cc.allio.uno.data.orm.sql.FromStatement;
import cc.allio.uno.data.orm.sql.Operator;
import cc.allio.uno.data.orm.sql.SQLException;
import cc.allio.uno.data.orm.sql.Statement;
import cc.allio.uno.data.orm.sql.dml.*;
import cc.allio.uno.data.orm.sql.dml.local.expression.DefaultExpressionContext;
import cc.allio.uno.data.orm.sql.dml.local.expression.Expression;
import cc.allio.uno.data.orm.sql.dml.local.expression.ExpressionContext;

import java.util.Collection;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

/**
 * 查询实现层
 *
 * @author jiangwei
 * @date 2023/1/5 10:25
 * @since 1.1.4
 */
public class QueryOperator implements
        Operator<QueryOperator>, QuerySQLAssociation, Statement<QueryOperator> {

    private final SelectDelegate selectDelegate;
    private final SelectFrom fromDelegate;
    private final WhereDelegate whereDelegate;
    private final OrderDelegate orderDelegate;
    private final GroupDelegate groupDelegate;
    private final LimitDelegate limitDelegate;
    private final ExpressionContext expressionContext;
    private final PriorityQueue<Statement<?>> sortStatements;

    public QueryOperator() {
        this.expressionContext = new DefaultExpressionContext();
        this.sortStatements = new PriorityQueue<>(ORDER_COMPARATOR);
        SelectStatement select = new SelectStatement(expressionContext);
        sortStatements.add(select);
        FromStatement from = new FromStatement(expressionContext);
        sortStatements.add(from);
        WhereStatement where = new WhereStatement(expressionContext);
        sortStatements.add(where);
        OrderStatement order = new OrderStatement(expressionContext);
        sortStatements.add(order);
        GroupStatement group = new GroupStatement(expressionContext);
        sortStatements.add(group);
        LimitStatement limit = new LimitStatement();
        sortStatements.add(limit);
        this.selectDelegate = new SelectStatementDelegate(select, from, where, group, order, limit, this);
        this.fromDelegate = new QueryFromStatement(select, from, where, group, order, limit, this);
        this.whereDelegate = new WhereStatementDelegate(select, from, where, group, order, limit, this);
        this.orderDelegate = new OrderStatementDelegate(select, from, where, group, order, limit, this);
        this.groupDelegate = new GroupStatementDelegate(select, from, where, group, order, limit, this);
        this.limitDelegate = new LimitStatementDelegate(select, from, where, group, order, limit, this);
    }

    @Override
    public SelectDelegate thenSelect() {
        return then(selectDelegate);
    }

    @Override
    public SelectFrom thenFrom() {
        return then(fromDelegate);
    }

    @Override
    public WhereDelegate thenWhere() {
        return then(whereDelegate);
    }

    @Override
    public OrderDelegate thenOrder() {
        return then(orderDelegate);
    }

    @Override
    public GroupDelegate thenGroup() {
        return then(groupDelegate);
    }

    @Override
    public LimitDelegate thenLimit() {
        return then(limitDelegate);
    }

    @Override
    public String getSQL() throws SQLException {
        String templateSQL = sortStatements.stream()
                .map(Statement::getSQL)
                // 过滤为空字符串SQL
                .filter(StringUtils::isNotBlank)
                // ' '进行拼接
                .collect(Collectors.joining(StringPool.SPACE));
        // 构建模板解析变量
        Map<String, Object> runVariables = expressionContext.getExpressionVariables()
                .entrySet()
                .stream()
                // key 去除token value 调用thenRun
                .collect(Collectors.toMap(
                        k -> {
                            String tokenKey = k.getKey();
                            Tokenizer.TokenSymbol symbol = expressionContext.getTokenizer().getSymbol();
                            return tokenKey.replace(symbol.getOpen(), StringPool.EMPTY).replace(symbol.getClose(), StringPool.EMPTY);
                        },
                        v -> v.getValue().thenRun()));
        // 未解析SQL
        return expressionContext.getExpressionTemplate().parseTemplate(templateSQL, runVariables);
    }

    @Override
    public String getCondition() {
        return null;
    }

    @Override
    public Collection<Expression> getExpressions() {
        return sortStatements
                .stream()
                .flatMap(statement -> statement.getExpressions().stream())
                .collect(Collectors.toList());
    }

    @Override
    public void syntaxCheck() throws SQLException {
        for (Statement<?> statement : sortStatements) {
            statement.syntaxCheck();
        }
    }

    @Override
    public int order() {
        return Integer.MIN_VALUE;
    }

    @Override
    public QueryOperator self() {
        return this;
    }
}
