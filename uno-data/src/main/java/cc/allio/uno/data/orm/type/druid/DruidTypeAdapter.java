package cc.allio.uno.data.orm.type.druid;

import cc.allio.uno.data.orm.type.DataType;
import cc.allio.uno.data.orm.type.DataTypeAdapter;
import cc.allio.uno.data.orm.type.GenericSQLType;
import cc.allio.uno.data.orm.type.SQLType;
import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.sql.ast.SQLDataTypeImpl;
import com.alibaba.druid.sql.ast.expr.*;

/**
 * druid的类型转换器
 *
 * @author jiangwei
 * @date 2023/4/12 20:06
 * @since 1.1.4
 */
public class DruidTypeAdapter implements DataTypeAdapter<SQLDataType> {
    private static final DruidTypeAdapter INSTANCE = new DruidTypeAdapter();

    @Override
    public SQLDataType get(DataType o) {
        DataType dataType = o;
        // dataType为null，赋值于VARCHAR
        if (dataType == null) {
            dataType = DataType.createCharType(GenericSQLType.VARCHAR, 64);
        }
        // 通用的做分组比较
        SQLType sqlType = dataType.getSqlType();
        // 每个数据库类型的做创建
        GenericSQLType sqlTypeConstant = GenericSQLType.getByJdbcCode(sqlType.getJdbcType());
        // scale 随着 precision值进行赋予，precision没值时，scale没值
        Integer precision = dataType.getPrecision();
        Integer scale = dataType.getScale();
        switch (sqlTypeConstant) {
            case BIGINT:
            case SMALLINT:
            case TINYINT:
            case BIT:
            case INTEGER:
            case DOUBLE:
            case NUMBER:
            case FLOAT:
                if (precision == null) {
                    return new SQLDataTypeImpl(sqlType.getName());
                } else if (scale == null) {
                    return new SQLDataTypeImpl(sqlType.getName(), precision);
                } else {
                    return new SQLDataTypeImpl(sqlType.getName(), precision, scale);
                }
            case DECIMAL:
                SQLDataTypeImpl decimalDataType = new SQLDataTypeImpl(sqlType.getName());
                if (precision != null) {
                    decimalDataType.addArgument(new SQLIntegerExpr(dataType.getPrecision()));
                }
                if (scale != null) {
                    decimalDataType.addArgument(new SQLIntegerExpr(dataType.getPrecision()));
                }
                return decimalDataType;
            case DATE:
            case TIME:
            case TIMESTAMP:
                SQLDataTypeImpl dateDataType = new SQLDataTypeImpl(sqlType.getName());
                if (precision != null) {
                    dateDataType.addArgument(new SQLIntegerExpr(precision));
                }
                return dateDataType;
            case CHAR:
            case VARCHAR:
            case NVARCHAR:
            case VARBINARY:
            case LONGVARCHAR:
            case LONGNVARCHAR:
            case LONGVARBINARY:
            default:
                SQLDataTypeImpl charDataType = new SQLDataTypeImpl(sqlType.getName());
                charDataType.addArgument(new SQLIntegerExpr(dataType.getPrecision()));
                return charDataType;
        }
    }

    @Override
    public DataType reversal(SQLDataType sqlDataType) {
        return null;
    }

    public static DruidTypeAdapter getInstance() {
        return INSTANCE;
    }
}
