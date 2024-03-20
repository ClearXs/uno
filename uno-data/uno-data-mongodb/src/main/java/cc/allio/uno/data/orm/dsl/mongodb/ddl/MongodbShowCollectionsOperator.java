package cc.allio.uno.data.orm.dsl.mongodb.ddl;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.core.exception.Exceptions;
import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.ddl.ShowTablesOperator;
import cc.allio.uno.data.orm.dsl.dml.QueryOperator;
import cc.allio.uno.data.orm.dsl.type.DBType;
import com.google.common.collect.Lists;
import lombok.Getter;

import java.util.List;

/**
 * mongodb show collections operator
 *
 * @author j.x
 * @date 2024/3/12 01:11
 * @since 1.1.7
 */
@AutoService(ShowTablesOperator.class)
@Operator.Group(OperatorKey.MONGODB_LITERAL)
public class MongodbShowCollectionsOperator implements ShowTablesOperator {

    @Getter
    private Database fromDb;

    @Getter
    private final List<Table> tables = Lists.newArrayList();

    @Override
    public ShowTablesOperator database(Database database) {
        this.fromDb = database;
        return self();
    }

    @Override
    public String getDSL() {
        throw Exceptions.unOperate("getDSL");
    }

    @Override
    public ShowTablesOperator parse(String dsl) {
        reset();
        return self();
    }

    @Override
    public void reset() {
        this.fromDb = null;
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
        throw Exceptions.unOperate("getPrepareDSL");
    }

    @Override
    public List<PrepareValue> getPrepareValues() {
        throw Exceptions.unOperate("getPrepareValues");
    }

    @Override
    public Table getTable() {
        throw Exceptions.unOperate("getTable");
    }

    @Override
    public QueryOperator toQueryOperator() {
        throw Exceptions.unOperate("toQueryOperator");
    }

    @Override
    public ShowTablesOperator schema(String schema) {
        // nothing to do
        return self();
    }

    @Override
    public ShowTablesOperator from(Table table) {
        this.tables.add(table);
        return self();
    }
}
