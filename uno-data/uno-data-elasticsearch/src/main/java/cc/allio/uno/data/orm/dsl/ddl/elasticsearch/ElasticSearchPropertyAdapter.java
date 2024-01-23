package cc.allio.uno.data.orm.dsl.ddl.elasticsearch;

import cc.allio.uno.data.orm.dsl.type.DataType;
import cc.allio.uno.data.orm.dsl.type.DataTypeAdapter;
import cc.allio.uno.data.orm.dsl.type.DSLType;
import co.elastic.clients.elasticsearch._types.analysis.Analyzer;
import co.elastic.clients.elasticsearch._types.mapping.Property;
import cc.allio.uno.core.util.StringUtils;

import java.util.Objects;

import static cc.allio.uno.data.orm.dsl.type.DSLType.DSLTypeImpl.*;

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
    public Property adapt(DataType o) {
        DataType dataType = o;
        // dataType为null，赋值于VARCHAR
        if (dataType == null) {
            dataType = DataType.createCharType(DSLType.VARCHAR, 64);
        }
        // 通用的做分组比较
        DSLType sqlType = dataType.getSqlType();
        // 每个数据库类型的做创建
        DSLType sqlTypeConstant = Objects.requireNonNullElse(DSLType.getByJdbcCode(sqlType.getJdbcType()), DSLType.VARCHAR);
        return switch (sqlTypeConstant) {
            case BIGINT -> Property.of(p -> p.long_(l -> l));
            case SMALLINT, TINYINT, INTEGER -> Property.of(p -> p.integer(i -> i));
            case BIT -> Property.of(p -> p.boolean_(b -> b));
            case DOUBLE, NUMBER, DECIMAL -> Property.of(p -> p.double_(d -> d));
            case FLOAT -> Property.of(p -> p.float_(f -> f));
            case DATE, TIME, TIMESTAMP -> Property.of(p -> p.date(d -> d.locale("zh_CN")));
            case OBJECT -> Property.of(p -> p.object(obj -> obj));
            default ->
                    Property.of(p -> p.text(t -> t.analyzer(StringUtils.isBlank(analyzer) ? Analyzer.Kind.Standard.jsonValue() : analyzer)));
        };
    }

    @Override
    public DataType reverse(Property property) {
        return switch (property._kind()) {
            case Binary, Byte -> DataType.create(DSLType.BIT);
            case DateNanos, DateRange, Date -> DataType.create(DSLType.DATE);
            case Long -> DataType.create(DSLType.BIGINT);
            case Float -> DataType.create(DSLType.FLOAT);
            case Double -> DataType.create(DSLType.DOUBLE);
            case Integer -> DataType.create(DSLType.INTEGER);
            case Short -> DataType.create(DSLType.SMALLINT);
            case Object -> DataType.create(DSLType.OBJECT);
            default -> DataType.createCharType(DSLType.VARCHAR, 0);
        };
    }
}
