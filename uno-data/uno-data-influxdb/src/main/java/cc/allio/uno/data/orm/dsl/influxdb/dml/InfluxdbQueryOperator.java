package cc.allio.uno.data.orm.dsl.influxdb.dml;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.core.exception.Exceptions;
import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.dml.QueryOperator;
import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.data.orm.dsl.word.Distinct;
import lombok.Getter;

import java.util.Collection;
import java.util.List;
import java.util.function.UnaryOperator;

/**
 * influx query operator use by <a href="https://docs.influxdata.com/influxdb/v2/query-data/influxql/">InfluxQL</a>
 *
 * @author j.x
 * @since 1.1.8
 */
@AutoService(QueryOperator.class)
@Operator.Group(OperatorKey.INFLUXDB_LITERAL)
public class InfluxdbQueryOperator extends InfluxdbSQLWhereOperatorImpl<InfluxdbQueryOperator> implements QueryOperator<InfluxdbQueryOperator> {

    private Table measurement;

    @Getter
    private boolean count = false;

    public InfluxdbQueryOperator() {
        super();
    }

    /**
     * like <a href="https://docs.influxdata.com/influxdb/v2/query-data/influxql/explore-data/select/">official website</a> said: support:
     * <ul>
     *     <li>field key</li>
     *     <li>tag key</li>
     *     <li>measurement</li>
     *     <li>...</li>
     * </ul>
     */
    @Override
    public InfluxdbQueryOperator select(DSLName dslName) {
        sqlQueryOperator.select(dslName);
        return self();
    }

    @Override
    public InfluxdbQueryOperator select(DSLName dslName, String alias) {
        sqlQueryOperator.select(dslName, alias);
        return self();
    }

    @Override
    public InfluxdbQueryOperator selects(Collection<DSLName> dslNames) {
        sqlQueryOperator.selects(dslNames);
        return self();
    }

    @Override
    public List<String> obtainSelectColumns() {
        return sqlQueryOperator.obtainSelectColumns();
    }

    @Override
    public InfluxdbQueryOperator distinct() {
        throw Exceptions.unOperate("distinct");
    }

    @Override
    public InfluxdbQueryOperator distinctOn(DSLName dslName, String alias) {
        throw Exceptions.unOperate("distinct");
    }

    @Override
    public InfluxdbQueryOperator aggregate(Func syntax, DSLName dslName, String alias, Distinct distinct) {
        if (Func.COUNT_FUNCTION == syntax) {
            count = true;
        }
        return self();
    }

    @Override
    public InfluxdbQueryOperator from(QueryOperator<?> fromTable, String alias) {
        sqlQueryOperator.from(fromTable, alias);
        return self();
    }

    @Override
    public InfluxdbQueryOperator join(Table left, JoinType joinType, Table right, BinaryCondition condition) {
        throw Exceptions.unOperate("join(Table left, JoinType joinType, Table right, BinaryCondition condition)");
    }

    @Override
    public InfluxdbQueryOperator orderBy(DSLName dslName, OrderCondition orderCondition) {
        sqlQueryOperator.orderBy(dslName, orderCondition);
        return self();
    }

    /**
     * limit and slimit (maybe exist compatibility)
     *
     * @see <a href="https://docs.influxdata.com/influxdb/v2/query-data/influxql/explore-data/limit-and-slimit/">LIMIT and SLIMIT clauses</a>
     */
    @Override
    public InfluxdbQueryOperator limit(Long limit, Long offset) {
        sqlQueryOperator.limit(limit, offset);
        return self();
    }

    /**
     * group by tags
     *
     * @see <a href="https://docs.influxdata.com/influxdb/v2/query-data/influxql/explore-data/group-by/">GROUP BY clause</a>
     */
    @Override
    public InfluxdbQueryOperator groupByOnes(Collection<DSLName> fieldNames) {
        sqlQueryOperator.groupByOnes(fieldNames);
        return self();
    }

    @Override
    public InfluxdbQueryOperator tree(QueryOperator<?> query) {
        return self();
    }

    @Override
    public InfluxdbQueryOperator tree(QueryOperator<?> baseQuery, QueryOperator<?> subQuery) {
        throw Exceptions.unOperate("tree(QueryOperator<?> baseQuery, QueryOperator<?> subQuery)");
    }

    @Override
    public String getPrepareDSL() {
        return sqlQueryOperator.getPrepareDSL();
    }

    @Override
    public List<PrepareValue> getPrepareValues() {
        return sqlQueryOperator.getPrepareValues();
    }

    @Override
    public String getDSL() {
        return sqlQueryOperator.getDSL();
    }

    @Override
    public InfluxdbQueryOperator parse(String dsl) {
        sqlQueryOperator = sqlQueryOperator.parse(dsl);
        return self();
    }

    @Override
    public InfluxdbQueryOperator customize(UnaryOperator<InfluxdbQueryOperator> operatorFunc) {
        return operatorFunc.apply(new InfluxdbQueryOperator());
    }

    @Override
    public void reset() {
        this.measurement = null;
        this.count = false;
        this.sqlQueryOperator.reset();
    }

    @Override
    public void setDBType(DBType dbType) {
        // nothing todo
    }

    @Override
    public DBType getDBType() {
        return DBType.INFLUXDB;
    }

    @Override
    public InfluxdbQueryOperator from(Table table) {
        this.measurement = table;
        this.sqlQueryOperator.from(table);
        return self();
    }

    @Override
    public Table getTable() {
        return measurement;
    }
}
