package cc.allio.uno.data.orm.dsl.mongodb.dml;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.core.StringPool;
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
import lombok.extern.slf4j.Slf4j;
import org.bson.conversions.Bson;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.UnaryOperator;

/**
 * mongodb query document operator implementation
 *
 * @author j.x
 * @since 1.1.7
 */
@Slf4j
@AutoService(QueryOperator.class)
@Operator.Group(OperatorKey.MONGODB_LITERAL)
public class MongodbQueryOperator extends MongodbWhereOperatorImpl<MongodbQueryOperator> implements QueryOperator<MongodbQueryOperator> {

    private Table fromColl;
    private List<Bson> orders;
    @Getter
    private Bson bsonOrder;
    @Getter
    private Bson bsonWindow;
    @Getter
    private Distinct distinct;

    @Getter
    private boolean count = false;

    public MongodbQueryOperator() {
        super();
        this.orders = Lists.newArrayList();
    }

    @Override
    public String getDSL() {
        return StringPool.EMPTY;
    }

    @Override
    public MongodbQueryOperator parse(String dsl) {
        // nothing to do
        return self();
    }

    @Override
    public MongodbQueryOperator customize(UnaryOperator<MongodbQueryOperator> operatorFunc) {
        return operatorFunc.apply(new MongodbQueryOperator());
    }

    @Override
    public void reset() {
        clear();
        this.fromColl = null;
        this.orders = Lists.newArrayList();
        this.bsonOrder = null;
        this.bsonWindow = null;
        this.count = false;
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
        return List.of();
    }

    @Override
    public MongodbQueryOperator from(Table table) {
        this.fromColl = table;
        return self();
    }

    @Override
    public Table getTable() {
        return fromColl;
    }

    @Override
    public MongodbQueryOperator select(DSLName dslName) {
        // nothing to do
        return self();
    }

    @Override
    public MongodbQueryOperator select(DSLName dslName, String alias) {
        // nothing to do
        return self();
    }

    @Override
    public MongodbQueryOperator selects(Collection<DSLName> dslNames) {
        // nothing to do
        return self();
    }

    @Override
    public List<String> obtainSelectColumns() {
        return Collections.emptyList();
    }

    @Override
    public MongodbQueryOperator distinct() {
        // nothing to do
        return self();
    }

    @Override
    public MongodbQueryOperator distinctOn(DSLName dslName, String alias) {
        this.distinct = new Distinct(dslName);
        return self();
    }

    @Override
    public MongodbQueryOperator aggregate(Func syntax, DSLName dslName, String alias, Distinct distinct) {
        // TODO improve aggregate function
        switch (syntax) {
            case AVG_FUNCTION -> Accumulators.avg(dslName.format(), null);
            case COUNT_FUNCTION -> count = true;
        }

        return self();
    }

    @Override
    public MongodbQueryOperator from(QueryOperator<?> fromTable, String alias) {
        throw Exceptions.unOperate("from(QueryOperator fromTable, String alias)");
    }

    @Override
    public MongodbQueryOperator join(Table left, JoinType joinType, Table right, BinaryCondition condition) {
        throw Exceptions.unOperate("join");
    }

    @Override
    public MongodbQueryOperator orderBy(DSLName dslName, OrderCondition orderCondition) {
        Bson order = toBsonOrder(dslName, orderCondition);
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
    public MongodbQueryOperator limit(Long limit, Long offset) {
        this.bsonWindow = Windows.range(offset, offset + limit);
        return self();
    }

    @Override
    public MongodbQueryOperator groupByOnes(Collection<DSLName> fieldNames) {
        if (log.isDebugEnabled()) {
            log.debug("mongodb query operate 'groupByOnes' nothing to do. ");
        }
        return self();
    }

    @Override
    public MongodbQueryOperator tree(QueryOperator<?> query) {
        return self();
    }

    @Override
    public MongodbQueryOperator tree(QueryOperator<?> baseQuery, QueryOperator<?> subQuery) {
        return self();
    }
}
