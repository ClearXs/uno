package cc.allio.uno.data.orm.dsl.mongodb.dml;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.core.exception.Exceptions;
import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.dml.QueryOperator;
import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.data.orm.dsl.word.Distinct;
import com.google.common.collect.Lists;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Windows;
import lombok.Getter;
import org.bson.conversions.Bson;

import java.util.Collection;
import java.util.List;

/**
 * mongodb query document operator implementation
 *
 * @author j.x
 * @date 2024/3/14 00:52
 * @since 1.1.7
 */
@AutoService(QueryOperator.class)
@Operator.Group(OperatorKey.MONGODB_LITERAL)
public class MongodbQueryOperator extends MongodbWhereOperatorImpl<QueryOperator> implements QueryOperator {

    private Table fromColl;
    private final List<Bson> orders;
    @Getter
    private Bson bsonOrder;
    @Getter
    private Bson bsonWindow;
    @Getter
    private Distinct distinct;

    public MongodbQueryOperator() {
        super();
        this.orders = Lists.newArrayList();
    }

    @Override
    public String getDSL() {

        return null;
    }

    @Override
    public QueryOperator parse(String dsl) {
        throw Exceptions.unOperate("parse");
    }

    @Override
    public void reset() {
        clear();
        this.fromColl = null;
        this.orders.clear();
        this.bsonOrder = null;
        this.bsonWindow = null;
    }

    @Override
    public void setDBType(DBType dbType) {
        // nothing to do
    }

    @Override
    public DBType getDBType() {
        return DBType.MONGODB;
    }

    @Override
    public String getPrepareDSL() {
        return getDSL();
    }

    @Override
    public List<PrepareValue> getPrepareValues() {
        throw Exceptions.unOperate("getPrepareValues");
    }

    @Override
    public QueryOperator from(Table table) {
        this.fromColl = table;
        return self();
    }

    @Override
    public Table getTable() {
        return fromColl;
    }

    @Override
    public QueryOperator select(DSLName sqlName) {
        // nothing to do
        return self();
    }

    @Override
    public QueryOperator select(DSLName sqlName, String alias) {
        // nothing to do
        return self();
    }

    @Override
    public QueryOperator selects(Collection<DSLName> sqlNames) {
        // nothing to do
        return self();
    }

    @Override
    public QueryOperator distinct() {
        // nothing to do
        return self();
    }

    @Override
    public QueryOperator distinctOn(DSLName sqlName, String alias) {
        this.distinct = new Distinct(sqlName);
        return self();
    }

    @Override
    public QueryOperator aggregate(Func syntax, DSLName sqlName, String alias, Distinct distinct) {
        switch (syntax) {
            case AVG_FUNCTION -> Accumulators.avg(sqlName.format(), null);
        }
        throw Exceptions.unOperate("aggregate");
    }

    @Override
    public QueryOperator from(QueryOperator fromTable, String alias) {
        throw Exceptions.unOperate("from(QueryOperator fromTable, String alias)");
    }

    @Override
    public QueryOperator join(Table left, JoinType joinType, Table right, BinaryCondition condition) {
        throw Exceptions.unOperate("join");
    }

    @Override
    public QueryOperator orderBy(DSLName sqlName, OrderCondition orderCondition) {
        Bson order = toBsonOrder(sqlName, orderCondition);
        this.orders.add(order);
        this.bsonOrder = Sorts.orderBy(orders);
        return self();
    }

    Bson toBsonOrder(DSLName sqlName, OrderCondition orderCondition) {
        if (orderCondition == OrderCondition.DESC) {
            return Sorts.descending(sqlName.format());
        } else {
            return Sorts.ascending(sqlName.format());
        }
    }

    @Override
    public QueryOperator limit(Long limit, Long offset) {
        this.bsonWindow = Windows.range(offset, offset + limit);
        return self();
    }

    @Override
    public QueryOperator groupByOnes(Collection<DSLName> fieldNames) {
        throw Exceptions.unOperate("groupByOnes");
    }
}
