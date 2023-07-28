package cc.allio.uno.data.orm.sql.ddl.druid;

import cc.allio.uno.data.orm.sql.dml.SQLQueryOperator;
import cc.allio.uno.data.orm.type.DBType;
import cc.allio.uno.data.orm.type.druid.DruidDbTypeAdapter;
import com.alibaba.druid.DbType;
import cc.allio.uno.data.orm.sql.PrepareValue;
import cc.allio.uno.data.orm.sql.SQLOperatorFactory;
import cc.allio.uno.data.orm.sql.SQLPrepareOperatorImpl;
import cc.allio.uno.data.orm.sql.Table;
import cc.allio.uno.data.orm.sql.ddl.SQLExistTableOperator;

import java.util.List;

/**
 * druid
 *
 * @author jiangwei
 * @date 2023/4/17 09:47
 * @since 1.1.4
 */
public class DruidSQLExistTableOperator extends SQLPrepareOperatorImpl<SQLExistTableOperator> implements SQLExistTableOperator {

    private final DbType druidDbType;
    private final SQLQueryOperator queryOperator;

    public DruidSQLExistTableOperator(DBType dbType) {
        this.druidDbType = DruidDbTypeAdapter.getInstance().get(dbType);
        this.queryOperator = SQLOperatorFactory.getSQLOperator(SQLQueryOperator.class);
    }

    @Override
    public String getSQL() {
        return queryOperator.getSQL();
    }

    @Override
    public SQLExistTableOperator parse(String sql) {
        throw new UnsupportedOperationException("DruidSQLExistTableOperator Unsupported");
    }

    @Override
    public String getPrepareSQL() {
        return queryOperator.getPrepareSQL();
    }

    @Override
    public SQLExistTableOperator from(Table table) {
        if (DbType.mysql == druidDbType) {
            queryOperator.count()
                    .from("information._schema.TABLES")
                    .$like$("TABLE_NAME", table.getName().format());
        } else if (DbType.postgresql == druidDbType) {
            queryOperator.count()
                    .from("pg_statio_user_tables")
                    .eq("relname", table.getName().format());
        }
        return self();
    }

    @Override
    protected void addPrepareValue(String column, Object value) {
        throw new UnsupportedOperationException("DruidSQLExistTableOperator Unsupported");
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
}
