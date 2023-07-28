package cc.allio.uno.data.orm.sql.ddl.elasticsearch;

import cc.allio.uno.data.orm.type.DataType;
import cc.allio.uno.data.orm.type.DataTypeAdapter;
import cc.allio.uno.data.orm.type.GenericSQLType;
import cc.allio.uno.data.orm.type.SQLType;
import co.elastic.clients.elasticsearch._types.analysis.Analyzer;
import co.elastic.clients.elasticsearch._types.mapping.Property;
import cc.allio.uno.core.util.StringUtils;

/**
 * JDBC数据类型转换为es类型
 *
 * @author jiangwei
 * @date 2023/7/4 15:25
 * @since 1.1.4
 */
public class ElasticSearchPropertyAdapter implements DataTypeAdapter<Property> {

    private final String analyzer;

    public ElasticSearchPropertyAdapter() {
        this(null);
    }

    public ElasticSearchPropertyAdapter(String analyzer) {
        this.analyzer = analyzer;
    }

    @Override
    public Property get(DataType o) {
        DataType dataType = o;
        // dataType为null，赋值于VARCHAR
        if (dataType == null) {
            dataType = DataType.createCharType(GenericSQLType.VARCHAR, 64);
        }
        // 通用的做分组比较
        SQLType sqlType = dataType.getSqlType();
        // 每个数据库类型的做创建
        GenericSQLType sqlTypeConstant = GenericSQLType.getByJdbcCode(sqlType.getJdbcType());
        switch (sqlTypeConstant) {
            case BIGINT:
                return Property.of(p -> p.long_(l -> l));
            case SMALLINT:
            case TINYINT:
            case INTEGER:
                return Property.of(p -> p.integer(i -> i));
            case BIT:
                return Property.of(p -> p.boolean_(b -> b));
            case DOUBLE:
            case NUMBER:
            case DECIMAL:
                return Property.of(p -> p.double_(d -> d));
            case FLOAT:
                return Property.of(p -> p.float_(f -> f));
            case DATE:
            case TIME:
            case TIMESTAMP:
                return Property.of(p -> p.date(d -> d.locale("zh_CN")));
            case OBJECT:
                return Property.of(p -> p.object(obj -> obj));
            case ARRAY:
            case CHAR:
            case VARCHAR:
            case NVARCHAR:
            case VARBINARY:
            case LONGVARCHAR:
            case LONGNVARCHAR:
            case LONGVARBINARY:
            default:
                return Property.of(p -> p.text(t -> t.analyzer(StringUtils.isBlank(analyzer) ? Analyzer.Kind.Standard.jsonValue() : analyzer)));
        }
    }

    @Override
    public DataType reversal(Property property) {
        switch (property._kind()) {
            case Binary:
            case Byte:
                return DataType.create(GenericSQLType.BIT);
            case DateNanos:
            case DateRange:
            case Date:
                return DataType.create(GenericSQLType.DATE);
            case Long:
                return DataType.create(GenericSQLType.BIGINT);
            case Float:
                return DataType.create(GenericSQLType.FLOAT);
            case Double:
                return DataType.create(GenericSQLType.DOUBLE);
            case Integer:
                return DataType.create(GenericSQLType.INTEGER);
            case Short:
                return DataType.create(GenericSQLType.SMALLINT);
            case Object:
                return DataType.create(GenericSQLType.OBJECT);
            case Text:
            default:
                return DataType.createCharType(GenericSQLType.VARCHAR, 0);

        }
    }
}
