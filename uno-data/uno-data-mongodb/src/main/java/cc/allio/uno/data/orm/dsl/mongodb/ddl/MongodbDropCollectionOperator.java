package cc.allio.uno.data.orm.dsl.mongodb.ddl;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.core.exception.Exceptions;
import cc.allio.uno.data.orm.dsl.Operator;
import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.data.orm.dsl.ddl.DropTableOperator;
import cc.allio.uno.data.orm.dsl.type.DBType;

/**
 * mongodb drop collection operator
 *
 * @author j.x
 * @date 2024/3/12 00:58
 * @since 1.1.7
 */
@AutoService(DropTableOperator.class)
@Operator.Group(OperatorKey.MONGODB_LITERAL)
public class MongodbDropCollectionOperator implements DropTableOperator {

    private Table fromColl;

    @Override
    public String getDSL() {
        throw Exceptions.unOperate("getDSL");
    }

    @Override
    public DropTableOperator parse(String dsl) {
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
    public DropTableOperator from(Table table) {
        this.fromColl = table;
        return self();
    }

    @Override
    public Table getTable() {
        return fromColl;
    }

    @Override
    public DropTableOperator ifExist(Boolean ifExist) {
        // nothing to do
        return self();
    }
}
