package cc.allio.uno.data.orm.sql.dml.local.expression;

import cc.allio.uno.data.orm.dialect.Dialect;
import cc.allio.uno.data.orm.dialect.H2Dialect;

public class TestExpressionContext extends DefaultExpressionContext {
    public static final DefaultExpressionContext INSTANCE = new TestExpressionContext(new H2Dialect());

    public TestExpressionContext(Dialect dialect) {
        super(dialect);
    }
}
