package cc.allio.uno.data.orm.sql.dml.druid;

import cc.allio.uno.data.orm.sql.dml.local.expression.Expression;
import cc.allio.uno.data.orm.sql.word.Distinct;
import cc.allio.uno.data.orm.type.DBType;
import cc.allio.uno.data.orm.type.druid.DruidDbTypeAdapter;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.util.StringUtils;
import cc.allio.uno.data.orm.sql.RuntimeColumn;
import cc.allio.uno.data.orm.sql.SQLException;
import cc.allio.uno.data.orm.sql.dml.Select;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Druid 适配器
 *
 * @author jiangwei
 * @date 2023/2/23 16:28
 * @since 1.1.4
 * @deprecated 1.1.4版本删除
 */
@Deprecated
public class DruidSelect implements Select<DruidSelect> {

    private final List<SQLSelectItem> selectItems;
    private final DbType dbType;

    public DruidSelect(DBType dbType) {
        this.selectItems = Lists.newLinkedList();
        this.dbType = DruidDbTypeAdapter.getInstance().get(dbType);
    }

    @Override
    public Collection<RuntimeColumn> getColumns() {
        throw new UnsupportedOperationException("druid select un support operation");
    }

    @Override
    public String getSQL() throws SQLException {
        return selectItems.stream().map(SQLUtils::toSQLString).collect(Collectors.joining());
    }

    @Override
    public String getCondition() {
        return null;
    }

    @Override
    public Collection<Expression> getExpressions() {
        throw new UnsupportedOperationException("druid select un support operation");
    }

    @Override
    public void syntaxCheck() throws SQLException {

    }

    @Override
    public DruidSelect select(String fieldName) {
        SQLExpr sqlExpr = SQLUtils.toSQLExpr(fieldName, dbType);
        SQLSelectItem selectItem = new SQLSelectItem(sqlExpr);
        selectItems.add(selectItem);
        return self();
    }

    @Override
    public DruidSelect select(String fieldName, String alias) {
        SQLExpr sqlExpr = SQLUtils.toSQLExpr(fieldName, dbType);
        SQLSelectItem selectItem = new SQLSelectItem(sqlExpr, alias);
        selectItems.add(selectItem);
        return self();
    }

    @Override
    public DruidSelect select(String[] fieldNames) {
        return select(Lists.newArrayList(fieldNames));
    }

    @Override
    public DruidSelect select(Collection<String> fieldNames) {
        for (String fieldName : fieldNames) {
            SQLSelectItem selectItem = SQLUtils.toSelectItem(fieldName, dbType);
            selectItems.add(selectItem);
        }
        return self();
    }

    @Override
    public DruidSelect distinct() {
        throw new UnsupportedOperationException("druid select un support operation");
    }

    @Override
    public DruidSelect distinctOn(String fieldName, String alias) {
        throw new UnsupportedOperationException("druid select un support operation");
    }

    @Override
    public DruidSelect aggregate(String syntax, String fieldName, String alias, Distinct distinct) {
        String aggFunc = syntax + StringPool.LEFT_BRACKET + fieldName + StringPool.RIGHT_BRACKET;
        SQLExpr sqlExpr = SQLUtils.toSQLExpr(aggFunc);
        SQLSelectItem selectItem;
        if (StringUtils.isBlank(alias)) {
            selectItem = new SQLSelectItem(sqlExpr);
        } else {
            selectItem = new SQLSelectItem(sqlExpr, alias);
        }
        selectItems.add(selectItem);
        return self();
    }

    public List<SQLSelectItem> getSelectItems() {
        return selectItems;
    }
}
