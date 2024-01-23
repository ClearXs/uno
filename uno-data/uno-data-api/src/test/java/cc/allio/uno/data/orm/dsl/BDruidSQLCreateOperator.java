package cc.allio.uno.data.orm.dsl;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.data.orm.dsl.ddl.CreateTableOperator;

@AutoService(CreateTableOperator.class)
@Operator.Group(OperatorKey.SQL_LITERAL)
public class BDruidSQLCreateOperator implements CreateTableOperator {
    @Override
    public String getDSL() {
        return null;
    }

    @Override
    public CreateTableOperator parse(String dsl) {
        return null;
    }

    @Override
    public void reset() {

    }

    @Override
    public CreateTableOperator from(Table table) {
        return null;
    }

    @Override
    public Table getTable() {
        return null;
    }

    @Override
    public CreateTableOperator column(ColumnDef columnDef) {
        return null;
    }

    @Override
    public CreateTableOperator comment(String comment) {
        return null;
    }
}
