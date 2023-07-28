package cc.allio.uno.data.orm.sql.dml.local.expression;

import java.util.Objects;

/**
 * statement expression
 *
 * @author jiangwei
 * @date 2023/1/10 19:30
 * @since 1.1.4
 */
public class StatementExpression implements SingleExpression {

    private final String statementSyntax;

    public StatementExpression(String statementSyntax) {
        this.statementSyntax = statementSyntax;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatementExpression that = (StatementExpression) o;
        return Objects.equals(statementSyntax, that.statementSyntax);
    }

    @Override
    public int hashCode() {
        return Objects.hash(statementSyntax);
    }

    @Override
    public String getSQL() {
        return statementSyntax;
    }

}
