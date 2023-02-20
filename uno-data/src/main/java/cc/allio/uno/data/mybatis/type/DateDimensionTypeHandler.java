package cc.allio.uno.data.mybatis.type;

import cc.allio.uno.data.query.param.DateDimension;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.*;
import java.util.Date;

/**
 * {@link DateDimension}类型处理器
 *
 * @author jiangwei
 * @date 2022/9/30 17:01
 * @since 1.1.0
 */
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
