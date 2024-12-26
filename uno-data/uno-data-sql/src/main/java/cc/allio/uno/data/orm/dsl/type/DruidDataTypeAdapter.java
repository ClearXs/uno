package cc.allio.uno.data.orm.dsl.type;

import cc.allio.uno.data.orm.dsl.dialect.TypeTranslator;
import cc.allio.uno.data.orm.dsl.dialect.TypeTranslatorHolder;
import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.sql.ast.SQLDataTypeImpl;
import com.alibaba.druid.sql.ast.expr.*;

import java.util.Objects;

import static cc.allio.uno.data.orm.dsl.type.DSLType.DSLTypeImpl.*;

/**
 * druid的类型转换器
 *
 * @author j.x
 * @since 1.1.4
 */
public class DruidDataTypeAdapter implements DataTypeAdapter<SQLDataType> {

    private final DBType dbType;

    private DruidDataTypeAdapter(DBType dbType) {
        this.dbType = dbType;
    }

    public static DruidDataTypeAdapter getInstance(DBType dbType) {
        return new DruidDataTypeAdapter(dbType);
    }

    @Override
    public SQLDataType adapt(DataType o) {
        DataType dataType = o;
        // dataType为null，赋值于VARCHAR
        if (dataType == null) {
            dataType = DataType.createCharType(DSLType.DefaultDSLType.VARCHAR, 64);
        }
        // 通用的做分组比较
        DSLType sqlType = dataType.getDslType();
        // 每个数据库类型的做创建
        DSLType sqlTypeConstant = Objects.requireNonNullElse(DSLType.getByJdbcCode(sqlType.getJdbcType()), DSLType.VARCHAR);
        TypeTranslator typeTranslator = TypeTranslatorHolder.getTypeTranslator(dbType);
        DSLType dbSQLType = typeTranslator.translate(sqlType, dataType.getPrecision(), dataType.getScale());
        // scale 随着 precision值进行赋予，precision没值时，scale没值
        Integer precision = dbSQLType.getPrecision();
        Integer scale = dbSQLType.getScale();
        switch (sqlTypeConstant) {
            case DefaultDSLType.BIGINT,
                 DefaultDSLType.SMALLINT,
                 DefaultDSLType.TINYINT,
                 DefaultDSLType.BIT,
                 DefaultDSLType.INTEGER,
                 DefaultDSLType.INT,
                 DefaultDSLType.DOUBLE,
                 DefaultDSLType.NUMBER,
                 DefaultDSLType.FLOAT:
                if (precision == null) {
                    return new SQLDataTypeImpl(dbSQLType.getName());
                } else if (scale == null) {
                    return new SQLDataTypeImpl(dbSQLType.getName(), precision);
                } else {
                    return new SQLDataTypeImpl(dbSQLType.getName(), precision, scale);
                }
            case DefaultDSLType.DECIMAL:
                SQLDataTypeImpl decimalDataType = new SQLDataTypeImpl(dbSQLType.getName());
                if (precision != null) {
                    decimalDataType.addArgument(new SQLIntegerExpr(precision));
                }
                if (scale != null) {
                    decimalDataType.addArgument(new SQLIntegerExpr(precision));
                }
                return decimalDataType;
            case DefaultDSLType.DATE,
                 DefaultDSLType.TIME,
                 DefaultDSLType.TIMESTAMP:
                SQLDataTypeImpl dateDataType = new SQLDataTypeImpl(dbSQLType.getName());
                if (precision != null) {
                    dateDataType.addArgument(new SQLIntegerExpr(precision));
                }
                return dateDataType;
            default:
                SQLDataTypeImpl charDataType = new SQLDataTypeImpl(dbSQLType.getName());
                if (precision != null) {
                    charDataType.addArgument(new SQLIntegerExpr(precision));
                }
                return charDataType;
        }
    }

    @Override
    public DataType reverse(SQLDataType sqlDataType) {
        return null;
    }
}
