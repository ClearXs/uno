package cc.allio.uno.data.orm.dsl.type;

import cc.allio.uno.core.util.CollectionUtils;
import cc.allio.uno.data.orm.dsl.dialect.TypeTranslator;
import cc.allio.uno.data.orm.dsl.dialect.TypeTranslatorHolder;
import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.sql.ast.SQLDataTypeImpl;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.*;
import com.alibaba.druid.sql.ast.statement.SQLCharacterDataType;

import java.util.List;
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
            dataType = DataType.createCharType(DefaultDSLType.VARCHAR, 64);
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

        String dataTypeName = sqlDataType.getName();
        TypeTranslator typeTranslator = TypeTranslatorHolder.getTypeTranslator(dbType);
        DSLType dslType = typeTranslator.reserve(dataTypeName);

        if (sqlDataType instanceof SQLDataTypeImpl) {
            List<SQLExpr> arguments = sqlDataType.getArguments();
            Integer precision = null;
            Integer scale = null;

            if (CollectionUtils.isNotEmpty(arguments)) {
                // precision
                SQLExpr precisionExpr = arguments.getFirst();
                if (precisionExpr instanceof SQLIntegerExpr sqlIntegerExpr) {
                    precision = sqlIntegerExpr.getNumber().intValue();
                }
                // scale
                if (arguments.size() > 1) {
                    SQLExpr scaleExpr = arguments.get(1);
                    if (scaleExpr instanceof SQLIntegerExpr sqlIntegerExpr) {
                        scale = sqlIntegerExpr.getNumber().intValue();
                    }
                }
            }

            DataType dataType = DataType.create(dslType);
            dataType.setPrecision(precision);
            dataType.setScale(scale);
            return dataType;
        }

        if (sqlDataType instanceof SQLCharacterDataType sqlCharacterDataType) {
            // parse character type
            Integer length = null;
            List<SQLExpr> arguments = sqlCharacterDataType.getArguments();

            if (CollectionUtils.isNotEmpty(arguments)) {
                SQLExpr lengthExpr = arguments.get(0);
                if (lengthExpr instanceof SQLIntegerExpr sqlIntegerExpr) {
                    length = sqlIntegerExpr.getNumber().intValue();
                }
            }
            return DataType.createCharType(dslType, length);
        }

        // no match default return varcahr
        return DataType.create(DSLType.VARCHAR);
    }
}
