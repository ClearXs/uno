package cc.allio.uno.data.orm.dsl.sql.ddl;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.dml.QueryOperator;
import cc.allio.uno.data.orm.dsl.exception.DSLException;
import cc.allio.uno.data.orm.dsl.sql.SQLSupport;
import cc.allio.uno.data.orm.dsl.type.DBType;
import com.alibaba.druid.DbType;
import cc.allio.uno.data.orm.dsl.ddl.ExistTableOperator;

import java.util.List;

/**
 * druid
 *
 * @author j.x
 * @date 2023/4/17 09:47
 * @since 1.1.4
 */
@AutoService(ExistTableOperator.class)
@Operator.Group(OperatorKey.SQL_LITERAL)
public class SQLExistTableOperator extends PrepareOperatorImpl<ExistTableOperator> implements ExistTableOperator {

    private DBType dbType;
    private DbType druidDbType;
    private Table table;
    private final QueryOperator queryOperator;

    public SQLExistTableOperator() {
        this(DBType.getSystemDbType());
    }

    public SQLExistTableOperator(DBType dbType) {
        this.dbType = dbType;
        this.druidDbType = SQLSupport.translateDb(dbType);
        this.queryOperator = OperatorGroup.getOperator(QueryOperator.class, OperatorKey.SQL, dbType);
    }

    @Override
    public String getDSL() {
        return queryOperator.getDSL();
    }

    @Override
    public ExistTableOperator parse(String dsl) {
        throw SQLSupport.on(this).onNonsupport("parse").<DSLException>execute();
    }

    @Override
    public String getPrepareDSL() {
        return queryOperator.getPrepareDSL();
    }

    @Override
    public ExistTableOperator from(Table table) {
        Object obj = SQLSupport.on(this)
                .onDb(druidDbType)
                .then(() ->
                        queryOperator.count()
                                .from("INFORMATION_SCHEMA.TABLES")
                                .$like$("TABLE_NAME", table.getName().format()))
                .execute();
        if (obj instanceof DSLException ex) {
            throw ex;
        }
        this.table = table;
        return self();
    }

    @Override
    public Table getTable() {
        return table;
    }

    @Override
    protected void addPrepareValue(String column, Object value) {
        throw SQLSupport.on(this).onNonsupport("addPrepareValue").<DSLException>execute();
    }

    @Override
    public List<PrepareValue> getPrepareValues() {
        return queryOperator.getPrepareValues();
    }

    @Override
    public void reset() {
        super.reset();
        queryOperator.reset();
    }

    @Override
    public void setDBType(DBType dbType) {
        this.dbType = dbType;
        this.druidDbType = SQLSupport.translateDb(dbType);
        this.queryOperator.setDBType(dbType);
    }

    @Override
    public DBType getDBType() {
        return dbType;
    }
}
