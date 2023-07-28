package cc.allio.uno.data.orm.sql.ddl.druid;

import cc.allio.uno.data.orm.sql.*;
import cc.allio.uno.data.orm.sql.dml.SQLQueryOperator;
import cc.allio.uno.data.orm.type.DBType;
import cc.allio.uno.data.orm.type.druid.DruidDbTypeAdapter;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.statement.SQLShowColumnsStatement;
import cc.allio.uno.data.orm.sql.ddl.SQLShowColumnsOperator;

import java.util.List;

/**
 * 表结构
 *
 * @author jiangwei
 * @date 2023/6/8 19:20
 * @since 1.1.4
 */
public class DruidSQLShowColumnsOperator extends SQLPrepareOperatorImpl<SQLShowColumnsOperator> implements SQLShowColumnsOperator {

    private final DbType druidDbType;
    private final SQLQueryOperator queryOperator;
    private final SQLShowColumnsStatement showColumnsStatement;

    public DruidSQLShowColumnsOperator(DBType dbType) {
        this.druidDbType = DruidDbTypeAdapter.getInstance().get(dbType);
        this.queryOperator = SQLOperatorFactory.getSQLOperator(SQLQueryOperator.class);
        this.showColumnsStatement = new SQLShowColumnsStatement();
    }

    @Override
    public String getSQL() {
        if (druidDbType == DbType.mysql) {
            return SQLUtils.toSQLString(showColumnsStatement);
        } else if (druidDbType == DbType.postgresql || druidDbType == DbType.db2 || druidDbType == DbType.h2) {
            return queryOperator.getSQL();
        }
        throw new UnsupportedOperationException(String.format("DruidSQLTableStructureOperator Unsupported db type for %s", druidDbType));
    }

    @Override
    public SQLShowColumnsOperator parse(String sql) {
        throw new UnsupportedOperationException("DruidSQLTableStructureOperator Unsupported");
    }

    @Override
    public String getPrepareSQL() {
        throw new UnsupportedOperationException("DruidSQLTableStructureOperator Unsupported");
    }

    @Override
    public List<PrepareValue> getPrepareValues() {
        throw new UnsupportedOperationException("DruidSQLTableStructureOperator Unsupported");
    }

    @Override
    public void reset() {
        super.reset();
        queryOperator.reset();
    }

    @Override
    public SQLShowColumnsOperator from(Table table) {
        if (druidDbType == DbType.mysql) {
            SQLPropertyExpr sqlPropertyExpr = new SQLPropertyExpr();
            sqlPropertyExpr.setName(table.getName().format());
            showColumnsStatement.setTable(sqlPropertyExpr);
        } else if (druidDbType == DbType.postgresql) {
            queryOperator.select(SQLName.of("column_name", SQLName.PLAIN_FEATURE), "field")
                    .select(SQLName.of("data_type", SQLName.PLAIN_FEATURE), "type")
                    .select(SQLName.of("is_nullable", SQLName.PLAIN_FEATURE), "nulls")
                    .select(SQLName.of("column_default", SQLName.PLAIN_FEATURE), "CDEFAULT")
                    .from(Table.of(SQLName.of("information_schema.columns", SQLName.PLAIN_FEATURE)))
                    .eq(SQLName.of("table_name", SQLName.PLAIN_FEATURE), table.getName().format());
        } else if (druidDbType == DbType.db2) {
            queryOperator.select(SQLName.of("COLNAME", SQLName.PLAIN_FEATURE), "field")
                    .select(SQLName.of("TYPENAME", SQLName.PLAIN_FEATURE), "type")
                    .select(SQLName.of("NULLS", SQLName.PLAIN_FEATURE), "nulls")
                    .select(SQLName.of("DEFAULT", SQLName.PLAIN_FEATURE), "CDEFAULT")
                    .from(Table.of(SQLName.of("SYSCAT.COLUMNS", SQLName.PLAIN_FEATURE)))
                    .eq(SQLName.of("TABNAME", SQLName.PLAIN_FEATURE), table.getName().format());
        } else if (druidDbType == DbType.h2) {
            queryOperator.select(SQLName.of("COLUMN_NAME", SQLName.PLAIN_FEATURE), "field")
                    .select(SQLName.of("DATA_TYPE", SQLName.PLAIN_FEATURE), "type")
                    .select(SQLName.of("IS_NULLABLE", SQLName.PLAIN_FEATURE), "nulls")
                    .select(SQLName.of("COLUMN_DEFAULT", SQLName.PLAIN_FEATURE), "CDEFAULT")
                    .from(Table.of(SQLName.of("INFORMATION_SCHEMA.COLUMNS", SQLName.PLAIN_FEATURE)))
                    .eq(SQLName.of("TABLE_NAME", SQLName.PLAIN_FEATURE), table.getName().format());
        }
        return self();
    }

    @Override
    public SQLQueryOperator toQueryOperator() {
        if (druidDbType == DbType.mysql) {
            queryOperator.reset();
            return queryOperator.parse(SQLUtils.toSQLString(showColumnsStatement));
        } else if (druidDbType == DbType.postgresql || druidDbType == DbType.db2 || druidDbType == DbType.h2) {
            return queryOperator;
        }
        throw new UnsupportedOperationException(String.format("DruidSQLTableStructureOperator Unsupported db type for %s", druidDbType));
    }
}
