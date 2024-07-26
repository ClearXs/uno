package cc.allio.uno.data.orm.dsl.sql;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.data.orm.dsl.Operator;
import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.dsl.UnrecognizedOperator;
import cc.allio.uno.data.orm.dsl.type.DBType;

import java.util.function.UnaryOperator;

@AutoService(UnrecognizedOperator.class)
@Operator.Group(OperatorKey.SQL_LITERAL)
public class SQLUnrecognizedOperator implements UnrecognizedOperator<SQLUnrecognizedOperator> {

    private DBType dbType;
    private String dsl;

    @Override
    public String getDSL() {
        return dsl;
    }

    @Override
    public SQLUnrecognizedOperator parse(String dsl) {
        this.dsl = dsl;
        return self();
    }

    @Override
    public SQLUnrecognizedOperator customize(UnaryOperator<SQLUnrecognizedOperator> operatorFunc) {
        return operatorFunc.apply(new SQLUnrecognizedOperator());
    }

    @Override
    public void reset() {
        this.dsl = null;
    }

    @Override
    public void setDBType(DBType dbType) {
        this.dbType = dbType;
    }

    @Override
    public DBType getDBType() {
        return dbType;
    }
}
