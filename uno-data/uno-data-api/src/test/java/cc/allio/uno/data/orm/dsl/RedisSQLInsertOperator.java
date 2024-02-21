package cc.allio.uno.data.orm.dsl;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.data.orm.dsl.dml.InsertOperator;
import cc.allio.uno.data.orm.dsl.type.DBType;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

@AutoService(InsertOperator.class)
@Operator.Group(OperatorKey.REDIS_LITERAL)
public class RedisSQLInsertOperator implements InsertOperator {
    @Override
    public String getDSL() {
        return null;
    }

    @Override
    public InsertOperator parse(String dsl) {
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
    public InsertOperator from(Table table) {
        return null;
    }

    @Override
    public Table getTable() {
        return null;
    }

    @Override
    public InsertOperator strictFill(String f, Supplier<Object> v) {
        return null;
    }

    @Override
    public InsertOperator columns(Collection<DSLName> columns) {
        return null;
    }

    @Override
    public InsertOperator values(List<Object> values) {
        return null;
    }

    @Override
    public boolean isBatched() {
        return false;
    }
}
