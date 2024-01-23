package cc.allio.uno.data.orm.executor.db;

import cc.allio.uno.data.orm.dsl.DSLName;
import cc.allio.uno.data.orm.dsl.type.JavaType;
import cc.allio.uno.data.orm.dsl.type.TypeRegistry;
import cc.allio.uno.data.orm.executor.ResultGroup;
import cc.allio.uno.data.orm.executor.ResultRow;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.result.DefaultResultContext;
import org.apache.ibatis.executor.resultset.DefaultResultSetHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.resultset.ResultSetWrapper;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.JDBCType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 解决值无法映射到一个抽象对象上的问题
 *
 * @author jiangwei
 * @date 2023/4/14 18:00
 * @since 1.1.4
 */
public class DbStatementSetHandler extends DefaultResultSetHandler implements ResultSetHandler {

    public DbStatementSetHandler(Executor executor, MappedStatement mappedStatement, ParameterHandler parameterHandler, ResultHandler<?> resultHandler, BoundSql boundSql, RowBounds rowBounds) {
        super(executor, mappedStatement, parameterHandler, resultHandler, boundSql, rowBounds);
    }

    @Override
    public void handleRowValues(ResultSetWrapper rsw, ResultMap resultMap, ResultHandler<?> resultHandler, RowBounds rowBounds, ResultMapping parentMapping) throws SQLException {
        if (resultMap.hasNestedResultMaps()) {
            throw new IllegalArgumentException("Nonsupport nested Result Maps");
        }
        DefaultResultContext<Object> resultContext = new DefaultResultContext<>();
        ResultSet resultSet = rsw.getResultSet();
        skipRows(resultSet, rowBounds);
        while (shouldProcessMoreRows(resultContext, rowBounds) && !resultSet.isClosed() && resultSet.next()) {
            ResultGroup resultGroup = new ResultGroup();
            List<String> columnNames = rsw.getColumnNames();
            for (int i = 0; i < columnNames.size(); i++) {
                ResultRow.ResultRowBuilder resultRowBuilder = ResultRow.builder();
                resultRowBuilder.index(i);
                String columnName = columnNames.get(i);
                resultRowBuilder.column(DSLName.of(columnName, DSLName.HUMP_FEATURE));
                JdbcType mybatisJdbcType = rsw.getJdbcType(columnName);
                JDBCType jdbcType = JDBCType.valueOf(mybatisJdbcType.TYPE_CODE);
                resultRowBuilder.jdbcType(jdbcType);
                JavaType<?> javaType = TypeRegistry.getInstance().findJavaType(jdbcType.getVendorTypeNumber());
                resultRowBuilder.javaType(javaType);
                TypeHandler<?> typeHandler = rsw.getTypeHandler(javaType.getJavaType(), columnName);
                Object value = typeHandler.getResult(rsw.getResultSet(), columnName);
                resultRowBuilder.value(value);
                resultGroup.addRow(resultRowBuilder.build());
            }
            resultContext.nextResultObject(resultGroup);
            ((ResultHandler<Object>) resultHandler).handleResult(resultContext);
        }
    }

    /**
     * @see DefaultResultSetHandler#skipRows(ResultSet, RowBounds)
     */
    private void skipRows(ResultSet rs, RowBounds rowBounds) throws SQLException {
        if (rs.getType() != ResultSet.TYPE_FORWARD_ONLY) {
            if (rowBounds.getOffset() != RowBounds.NO_ROW_OFFSET) {
                rs.absolute(rowBounds.getOffset());
            }
        } else {
            for (int i = 0; i < rowBounds.getOffset(); i++) {
                if (!rs.next()) {
                    break;
                }
            }
        }
    }

    /**
     * @see DefaultResultSetHandler#shouldProcessMoreRows(ResultContext, RowBounds)
     */
    private boolean shouldProcessMoreRows(ResultContext<?> context, RowBounds rowBounds) {
        return !context.isStopped() && context.getResultCount() < rowBounds.getLimit();
    }
}
