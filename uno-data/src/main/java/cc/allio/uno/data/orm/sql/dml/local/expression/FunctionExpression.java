package cc.allio.uno.data.orm.sql.dml.local.expression;

import cc.allio.uno.data.orm.dialect.Dialect;
import cc.allio.uno.data.orm.dialect.func.FuncDescriptor;
import cc.allio.uno.data.orm.sql.SQLException;

/**
 * aggregate expression.
 * <p>包含有MIN、MAX_FUNCTION、AVG等等函数</p>
 *
 * @author jiangwei
 * @date 2023/1/10 13:46
 * @see FuncDescriptor
 * @since 1.1.4
 */
public class FunctionExpression implements Expression {

    private final FuncDescriptor func;
    // 函数执行的参数
    private final Object[] arguments;

    public FunctionExpression(ExpressionContext expressionContext, String functionSyntax, Object[] arguments) {
        Dialect dialect = expressionContext.getDialect();
        FuncDescriptor func = dialect.getFuncRegistry().findFunc(functionSyntax);
        if (func == null) {
            throw new SQLException(String.format("expect func %s nonentity ", functionSyntax));
        }
        this.func = func;
        this.arguments = arguments;
    }

    @Override
    public String getSQL() {
        return func.render(arguments);
    }

}
