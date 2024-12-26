package cc.allio.uno.data.query.db.type;

import cc.allio.uno.data.query.param.DateDimension;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.*;
import java.util.Date;

/**
 * {@link DateDimension}类型处理器
 * <b>注意使用时候需要在Mybatis配置文件中配置typeHandlerPackage = cc/allio/uno/data/query/db/type...</b>
 *
 * @author j.x
 * @since 1.1.0
 */
@MappedTypes(Object.class)
public class DateDimensionTypeHandler extends BaseTypeHandler<DateDimension> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, DateDimension parameter, JdbcType jdbcType) throws SQLException {
        Date date = parameter.getDimension().getToDate().apply(parameter.getDate());
        ps.setTimestamp(i, new Timestamp(date.getTime()));
    }

    @Override
    public DateDimension getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return null;
    }

    @Override
    public DateDimension getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return null;
    }

    @Override
    public DateDimension getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return null;
    }
}
