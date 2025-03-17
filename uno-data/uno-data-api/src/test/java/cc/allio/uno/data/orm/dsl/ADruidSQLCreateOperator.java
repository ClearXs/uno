package cc.allio.uno.data.orm.dsl;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.data.orm.dsl.ddl.CreateTableOperator;
import cc.allio.uno.data.orm.dsl.type.DBType;

import java.util.List;
import java.util.function.UnaryOperator;

@AutoService(CreateTableOperator.class)
@Operator.Group(OperatorKey.SQL_LITERAL)
public class ADruidSQLCreateOperator implements CreateTableOperator<ADruidSQLCreateOperator> {

    @Override
    public String getDSL() {
        return null;
    }

    @Override
    public ADruidSQLCreateOperator parse(String dsl) {
        return null;
    }

    @Override
    public ADruidSQLCreateOperator customize(UnaryOperator<ADruidSQLCreateOperator> operatorFunc) {
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
    public ADruidSQLCreateOperator from(Table table) {
        return null;
    }

    @Override
    public Table getTable() {
        return null;
    }

    @Override
    public ADruidSQLCreateOperator column(ColumnDef columnDef) {
        return null;
    }

    @Override
    public ADruidSQLCreateOperator comment(String comment) {
        return null;
    }

    @Override
    public List<ColumnDef> getColumns() {
        return List.of();
    }
}
