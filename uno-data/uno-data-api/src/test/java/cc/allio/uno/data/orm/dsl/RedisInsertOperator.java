package cc.allio.uno.data.orm.dsl;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.data.orm.dsl.dml.InsertOperator;
import cc.allio.uno.data.orm.dsl.type.DBType;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

@AutoService(InsertOperator.class)
@Operator.Group(OperatorKey.REDIS_LITERAL)
public class RedisInsertOperator implements InsertOperator<RedisInsertOperator> {
    @Override
    public String getDSL() {
        return null;
    }

    @Override
    public RedisInsertOperator parse(String dsl) {
        return null;
    }

    @Override
    public RedisInsertOperator customize(UnaryOperator<RedisInsertOperator> operatorFunc) {
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
    public RedisInsertOperator from(Table table) {
        return null;
    }

    @Override
    public Table getTable() {
        return null;
    }

    @Override
    public RedisInsertOperator strictFill(String f, Supplier<Object> v) {
        return null;
    }

    @Override
    public RedisInsertOperator columns(Collection<DSLName> columns) {
        return null;
    }

    @Override
    public RedisInsertOperator values(List<Object> values) {
        return null;
    }

    @Override
    public boolean isBatched() {
        return false;
    }
}
