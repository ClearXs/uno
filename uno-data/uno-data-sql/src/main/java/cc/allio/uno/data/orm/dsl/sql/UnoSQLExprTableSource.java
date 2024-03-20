package cc.allio.uno.data.orm.dsl.sql;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;

/**
 * 基于{@link SQLExprTableSource}，拓展当设置schema，判断数据库类型决定是否设置
 *
 * @author j.x
 * @date 2024/1/5 16:15
 * @since 1.1.7
 */
public class UnoSQLExprTableSource extends SQLExprTableSource {

    private final DbType dbType;

    public UnoSQLExprTableSource(DbType dbType) {
        this.dbType = dbType;
    }

    @Override
    public void setSchema(String schema) {
        if (dbType == null) {
            super.setSchema(schema);
        }
        if (!(dbType == DbType.mysql || dbType == DbType.mariadb)) {
            super.setSchema(schema);
        }
    }
}
