package cc.allio.uno.data.orm.dsl;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.data.orm.dsl.ddl.ShowColumnsOperator;
import cc.allio.uno.data.orm.dsl.dml.QueryOperator;
import cc.allio.uno.data.orm.dsl.type.DBType;

import java.util.List;
import java.util.function.UnaryOperator;

@AutoService(ShowColumnsOperator.class)
@Operator.Group(OperatorKey.REDIS_LITERAL)
public class RedisShowColumnsOperator implements ShowColumnsOperator<RedisShowColumnsOperator> {

    @Override
    public String getDSL() {
        return null;
    }

    @Override
    public RedisShowColumnsOperator parse(String dsl) {
        return null;
    }

    @Override
    public RedisShowColumnsOperator customize(UnaryOperator<RedisShowColumnsOperator> operatorFunc) {
        return null;
    }

    @Override
    public void reset() {

    }

    @Override
    public void setDBType(DBType dbType) {

    }

    @Override
    public DBType getDBType() {
        return null;
    }

    @Override
    public String getPrepareDSL() {
        return null;
    }

    @Override
    public List<PrepareValue> getPrepareValues() {
        return null;
    }

    @Override
    public RedisShowColumnsOperator from(Table table) {
        return null;
    }

    @Override
    public Table getTable() {
        return null;
    }

    @Override
    public QueryOperator<?> toQueryOperator() {
        return null;
    }

    @Override
    public RedisShowColumnsOperator database(Database database) {
        return null;
    }
}
