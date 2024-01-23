package cc.allio.uno.data.orm.dsl.type;

import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.sql.ast.SQLDataTypeImpl;
import com.alibaba.druid.sql.ast.expr.*;

import java.util.Objects;

import static cc.allio.uno.data.orm.dsl.type.DSLType.DSLTypeImpl.*;

/**
 * druid的类型转换器
 *
 * @author jiangwei
 * @date 2023/4/12 20:06
 * @since 1.1.4
 */
public class DruidDataTypeAdapter implements DataTypeAdapter<SQLDataType> {
    private static final DruidDataTypeAdapter INSTANCE = new DruidDataTypeAdapter();

    private DruidDataTypeAdapter() {
    }

    public static DruidDataTypeAdapter getInstance() {
        return INSTANCE;
    }

    @Override
    public SQLDataType adapt(DataType o) {
        DataType dataType = o;
        // dataType为null，赋值于VARCHAR
        if (dataType == null) {
            dataType = DataType.createCharType(DSLTypeImpl.VARCHAR, 64);
        }
        // 通用的做分组比较
        DSLType sqlType = dataType.getSqlType();
        // 每个数据库类型的做创建
        DSLType sqlTypeConstant = Objects.requireNonNullElse(DSLType.getByJdbcCode(sqlType.getJdbcType()), DSLType.VARCHAR);
        // scale 随着 precision值进行赋予，precision没值时，scale没值
        Integer precision = dataType.getPrecision();
        Integer scale = dataType.getScale();
        switch (sqlTypeConstant) {
            case DSLTypeImpl.BIGINT, DSLTypeImpl.SMALLINT, DSLTypeImpl.TINYINT, DSLTypeImpl.BIT, DSLTypeImpl.INTEGER, DSLTypeImpl.DOUBLE, DSLTypeImpl.NUMBER, DSLTypeImpl.FLOAT:
                if (precision == null) {
                    return new SQLDataTypeImpl(sqlType.getName());
                } else if (scale == null) {
                    return new SQLDataTypeImpl(sqlType.getName(), precision);
                } else {
                    return new SQLDataTypeImpl(sqlType.getName(), precision, scale);
                }
            case DSLTypeImpl.DECIMAL:
                SQLDataTypeImpl decimalDataType = new SQLDataTypeImpl(sqlType.getName());
                if (precision != null) {
                    decimalDataType.addArgument(new SQLIntegerExpr(dataType.getPrecision()));
                }
                if (scale != null) {
                    decimalDataType.addArgument(new SQLIntegerExpr(dataType.getPrecision()));
                }
                return decimalDataType;
            case DSLTypeImpl.DATE, DSLTypeImpl.TIME, DSLTypeImpl.TIMESTAMP:
                SQLDataTypeImpl dateDataType = new SQLDataTypeImpl(sqlType.getName());
                if (precision != null) {
                    dateDataType.addArgument(new SQLIntegerExpr(precision));
                }
                return dateDataType;
            default:
                SQLDataTypeImpl charDataType = new SQLDataTypeImpl(sqlType.getName());
                charDataType.addArgument(new SQLIntegerExpr(dataType.getPrecision()));
                return charDataType;
        }
    }

    @Override
    public DataType reverse(SQLDataType sqlDataType) {
        return null;
    }
}
