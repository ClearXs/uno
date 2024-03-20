package cc.allio.uno.data.orm.dsl.mongodb.ddl;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.core.exception.Exceptions;
import cc.allio.uno.data.orm.dsl.ColumnDef;
import cc.allio.uno.data.orm.dsl.Operator;
import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.data.orm.dsl.ddl.CreateTableOperator;
import cc.allio.uno.data.orm.dsl.type.DBType;
import lombok.Getter;

/**
 * mongodb create collection operator
 *
 * @author j.x
 * @date 2024/3/12 00:57
 * @since 1.1.7
 */
@AutoService(CreateTableOperator.class)
@Operator.Group(OperatorKey.MONGODB_LITERAL)
public class MongodbCreateCollectionOperator implements CreateTableOperator {

    @Getter
    private Table fromColl;

    @Override
    public String getDSL() {
        throw Exceptions.unOperate("getDSL");
    }

    @Override
    public CreateTableOperator parse(String dsl) {
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
    public CreateTableOperator from(Table table) {
        this.fromColl = table;
        return self();
    }

    @Override
    public Table getTable() {
        return this.fromColl;
    }

    @Override
    public CreateTableOperator column(ColumnDef columnDef) {
        // nothing to do
        return self();
    }

    @Override
    public CreateTableOperator comment(String comment) {
        // nothing to do
        return self();
    }
}
