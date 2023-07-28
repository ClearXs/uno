package cc.allio.uno.data.orm.sql.dml.druid;

import cc.allio.uno.data.orm.sql.dml.local.expression.Expression;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import cc.allio.uno.data.orm.sql.JoinType;
import cc.allio.uno.data.orm.sql.SQLException;
import cc.allio.uno.data.orm.sql.dml.JoinFrom;
import cc.allio.uno.data.orm.sql.dml.SelectFrom;
import cc.allio.uno.data.orm.sql.dml.SubQueryFrom;
import org.springframework.core.annotation.AnnotationUtils;

import javax.persistence.Table;
import java.util.Collection;

/**
 * DruidSelectFrom
 *
 * @author jiangwei
 * @date 2023/4/13 15:01
 * @see DruidSQLQueryOperator
 * @since 1.1.4
 * @deprecated 1.1.4 之后删除
 */
@Deprecated
public class DruidSelectFrom implements SelectFrom<DruidSelectFrom> {

    private final SQLExprTableSource tableSource = new SQLExprTableSource();

    @Override
    public DruidSelectFrom from(Class<?> tableEntity) throws SQLException {
        Table table = AnnotationUtils.findAnnotation(tableEntity, Table.class);
        String tableName = table.name();
        return from(tableName);
    }

    @Override
    public DruidSelectFrom from(String table) throws SQLException {
        return null;
    }

    @Override
    public DruidSelectFrom from(String table, String alias) {

        return null;
    }

    @Override
    public String getSQL() throws SQLException {
        return null;
    }

    @Override
    public String getCondition() {
        return null;
    }

    @Override
    public Collection<Expression> getExpressions() {
        return null;
    }

    @Override
    public void syntaxCheck() throws SQLException {

    }

    @Override
    public int order() {
        return 0;
    }

    @Override
    public SubQueryFrom subQuery() {
        return null;
    }

    @Override
    public JoinFrom join(JoinType joinType) {
        return new DruidJoinFrom(this, joinType);
    }
}
