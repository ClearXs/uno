package cc.allio.uno.data.mybatis.injector;

import cc.allio.uno.data.query.QueryFilter;
import cc.allio.uno.data.mybatis.mapper.QueryMapper;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * 构建{@link QueryMapper#queryList(QueryFilter)}SQL语句
 *
 * @author jiangwei
 * @date 2022/9/30 16:37
 * @since 1.1.0
 */
public class QueryList extends AbstractMethod {

    private static final String QUERY_LIST = "queryList";

    String sql = "<script>\nSELECT %s FROM %s %s %s %s\n</script>";

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String formatSql = String.format(sql, sqlSelectColumns(tableInfo, true), tableInfo.getTableName(),
                sqlWhereEntityWrapper(true, tableInfo),
                sqlOrderWrapper(),
                sqlGroupWrapper());
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, formatSql, modelClass);
        return this.addSelectMappedStatementForTable(mapperClass, QUERY_LIST, sqlSource, tableInfo);
    }

    @Override
    protected String sqlSelectColumns(TableInfo table, boolean queryWrapper) {
        /* 假设存在用户自定义的 resultMap 映射返回 */
        String selectColumns = ASTERISK;
        if (table.getResultMap() == null || table.isAutoInitResultMap()) {
            /* 未设置 resultMap 或者 resultMap 是自动构建的,视为属于mp的规则范围内 */
            selectColumns = table.getAllSqlSelect();
        }
        if (!queryWrapper) {
            return selectColumns;
        }
        return SqlScriptUtils.convertChoose(
                String.format("%s != null and %s != null and %s != ''", "select", "select.sql", "select.sql"),
                SqlScriptUtils.unSafeParam("select.sql"),
                selectColumns);
    }

    @Override
    protected String sqlWhereEntityWrapper(boolean newLine, TableInfo table) {
        String whereCondition = SqlScriptUtils.unSafeParam("where.sql");
        whereCondition += NEWLINE + table.getLogicDeleteSql(true, true) + NEWLINE;
        whereCondition = SqlScriptUtils.convertChoose(
                String.format("%s != null and %s != null and %s != ''", "where", "where.sql", "where.sql"),
                whereCondition,
                table.getLogicDeleteSql(false, true)
        );
        return SqlScriptUtils.convertWhere(whereCondition);
    }

    protected String sqlOrderWrapper() {
        String orderBy = SqlScriptUtils.unSafeParam("order.sql");
        orderBy = QuerySqlScriptUtil.orderSql(orderBy);
        return SqlScriptUtils.convertChoose(
                String.format("%s != null and %s != null and %s != ''", "order", "order.sql", "order.sql"),
                orderBy,
                ""
        );
    }

    protected String sqlGroupWrapper() {
        String groupBy = SqlScriptUtils.unSafeParam("group.sql");
        groupBy = QuerySqlScriptUtil.orderSql(groupBy);
        return SqlScriptUtils.convertChoose(
                String.format("%s != null and %s != null and %s != ''", "group", "group.sql", "group.sql"),
                groupBy,
                ""
        );
    }
}
