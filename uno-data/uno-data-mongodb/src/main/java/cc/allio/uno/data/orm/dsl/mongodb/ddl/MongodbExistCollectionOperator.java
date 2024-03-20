package cc.allio.uno.data.orm.dsl.mongodb.ddl;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.core.exception.Exceptions;
import cc.allio.uno.data.orm.dsl.Operator;
import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.dsl.PrepareValue;
import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.data.orm.dsl.ddl.ExistTableOperator;
import cc.allio.uno.data.orm.dsl.type.DBType;

import java.util.List;

/**
 * mongodb exist collection operator
 *
 * @author j.x
 * @date 2024/3/12 01:11
 * @since 1.1.7
 */
@AutoService(ExistTableOperator.class)
@Operator.Group(OperatorKey.MONGODB_LITERAL)
public class MongodbExistCollectionOperator implements ExistTableOperator {

    private Table fromColl;

    @Override
    public String getDSL() {
        throw Exceptions.unOperate("getDSL");
    }

    @Override
    public ExistTableOperator parse(String dsl) {
        reset();
        return self();
    }

    @Override
    public void reset() {
        this.fromColl = null;
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
        throw Exceptions.unOperate("getDSL");
    }

    @Override
    public List<PrepareValue> getPrepareValues() {
        throw Exceptions.unOperate("getDSL");
    }

    @Override
    public ExistTableOperator from(Table table) {
        this.fromColl = table;
        return self();
    }

    @Override
    public Table getTable() {
        return fromColl;
    }
}
