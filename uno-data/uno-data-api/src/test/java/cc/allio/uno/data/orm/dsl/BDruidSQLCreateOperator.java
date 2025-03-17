package cc.allio.uno.data.orm.dsl;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.data.orm.dsl.ddl.CreateTableOperator;
import cc.allio.uno.data.orm.dsl.type.DBType;

import java.util.List;
import java.util.function.UnaryOperator;

@AutoService(CreateTableOperator.class)
@Operator.Group(OperatorKey.SQL_LITERAL)
public class BDruidSQLCreateOperator implements CreateTableOperator<BDruidSQLCreateOperator> {
    @Override
    public String getDSL() {
        return null;
    }

    @Override
    public BDruidSQLCreateOperator parse(String dsl) {
        return null;
    }

    @Override
    public BDruidSQLCreateOperator customize(UnaryOperator<BDruidSQLCreateOperator> operatorFunc) {
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
    public BDruidSQLCreateOperator from(Table table) {
        return null;
    }

    @Override
    public Table getTable() {
        return null;
    }

    @Override
    public BDruidSQLCreateOperator column(ColumnDef columnDef) {
        return null;
    }

    @Override
    public BDruidSQLCreateOperator comment(String comment) {
        return null;
    }

    @Override
    public List<ColumnDef> getColumns() {
        return List.of();
    }
}
