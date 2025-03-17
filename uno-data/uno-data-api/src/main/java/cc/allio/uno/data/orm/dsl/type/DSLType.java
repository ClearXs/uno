package cc.allio.uno.data.orm.dsl.type;

import cc.allio.uno.core.api.EqualsTo;
import cc.allio.uno.data.orm.dsl.dialect.TypeTranslatorHolder;
import com.google.common.base.Objects;
import lombok.*;

import java.sql.Types;
import java.util.List;
import java.util.Optional;

/**
 * DSL 类型定义，基于{@link DBType#MYSQL}作基础类型进行定义
 * <p><b>建议通过{@link TypeTranslatorHolder#getTypeTranslator()}在进行包装，否则获取的{@link DSLType}实例无法满足指定数据库方言</b></p>
 *
 * @author j.x
 * @see DataType
 * @since 1.1.4
 */
public interface DSLType extends EqualsTo<DSLType> {

    // ====================== number ======================

    DSLType BIGINT = DefaultDSLType.BIGINT;
    DSLType SMALLINT = DefaultDSLType.SMALLINT;
    DSLType INT = DefaultDSLType.INT;
    DSLType INTEGER = DefaultDSLType.INTEGER;
    DSLType BIT = DefaultDSLType.BIT;
    DSLType TINYINT = DefaultDSLType.TINYINT;
    DSLType NUMBER = DefaultDSLType.NUMBER;
    DSLType DOUBLE = DefaultDSLType.DOUBLE;
    DSLType FLOAT = DefaultDSLType.FLOAT;

    // ====================== time ======================

    DSLType TIME = DefaultDSLType.TIME;
    DSLType TIMESTAMP = DefaultDSLType.TIMESTAMP;
    DSLType DATE = DefaultDSLType.DATE;
    DSLType DECIMAL = DefaultDSLType.DECIMAL;

    // ====================== character ======================

    DSLType CHAR = DefaultDSLType.CHAR;
    DSLType VARCHAR = DefaultDSLType.VARCHAR;
    DSLType NVARCHAR = DefaultDSLType.NVARCHAR;
    DSLType LONGVARCHAR = DefaultDSLType.LONGVARCHAR;
    DSLType LONGNVARCHAR = DefaultDSLType.LONGNVARCHAR;
    DSLType VARBINARY = DefaultDSLType.VARBINARY;
    DSLType LONGVARBINARY = DefaultDSLType.LONGVARBINARY;
    DSLType TEXT = DefaultDSLType.TEXT;

    // ====================== boolean ======================

    DSLType BOOLEAN = DefaultDSLType.BOOLEAN;

    // ====================== 高级类型 ======================

    DSLType OBJECT = DefaultDSLType.OBJECT;
    DSLType ARRAY = DefaultDSLType.ARRAY;

    DSLType JSON = DefaultDSLType.JSON;
    DSLType JSONB = DefaultDSLType.JSONB;


    /**
     * 获取sql type name
     *
     * @return String
     */
    String getName();

    /**
     * 获取 sql type对应的jdbc type code
     *
     * @return jdbc code
     */
    int getJdbcType();

    /**
     * 获取的Precision
     *
     * @return Precision
     */
    Integer getPrecision();

    /**
     * 获取的Scala
     *
     * @return Scala
     */
    Integer getScale();

    /**
     * 根据jdbc code获取SQLType实例
     *
     * @param jdbcCode jdbcCode
     * @return SQLType
     */
    static DSLType getByJdbcCode(int jdbcCode) {
        for (DSLType value : DefaultDSLType.values()) {
            if (value.getJdbcType() == jdbcCode) {
                return value;
            }
        }
        return null;
    }

    /**
     * 比较其他的{@link DSLType}对象
     * <ol>
     * <li>比较名称</li>
     * <li>比较类型</li>
     * <li>比较precision</li>
     * <li>比较scale</li>
     * </ol>
     */
    @Override
    default boolean equalsTo(DSLType other) {
        return other != null && this.getJdbcType() == other.getJdbcType();
    }

    /**
     * 从给定的SQLType创建一个SQLType，该方法将会一个{@link DSLType}实例
     *
     * @param dslType   dslType
     * @param precision precision
     * @param scale     scale
     * @return DSLTypeImpl
     * @see DSLTypeImpl
     */
    static DSLTypeImpl create(DSLType dslType, Integer precision, Integer scale) {
        DSLTypeImpl.DSLTypeImplBuilder builder = DSLTypeImpl.builder()
                .name(dslType.getName())
                .jdbcType(dslType.getJdbcType());
        Integer definitePrecision = Optional.ofNullable(precision).orElse(dslType.getPrecision());
        builder.precision(definitePrecision);
        Integer definiteScale = Optional.ofNullable(scale).orElse(dslType.getScale());
        builder.scale(definiteScale);
        return builder.build();
    }

    /**
     * 关联于某一个SQLType
     */
    interface DSLLinkType extends DSLType {

        /**
         * 关联的SQL Type
         *
         * @return SQLType
         */
        List<DSLType> getParent();
    }

    @Data
    @Builder
    @EqualsAndHashCode(of = {"name", "jdbcType"})
    class DSLTypeImpl implements DSLType {
        private final String name;
        private final int jdbcType;
        private final Integer precision;
        private final Integer scale;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DSLTypeImpl dslType = (DSLTypeImpl) o;
            return jdbcType == dslType.jdbcType && Objects.equal(name, dslType.name) && Objects.equal(precision, dslType.precision) && Objects.equal(scale, dslType.scale);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(name, jdbcType, precision, scale);
        }
    }

    @Getter
    @AllArgsConstructor
    enum DefaultDSLType implements DSLType {
        // ====================== 数字型 ======================
        BIGINT("bigint", Types.BIGINT, 64, null),
        SMALLINT("smallint", Types.SMALLINT, 32, null),
        INT("int", Types.INTEGER, 64, null),
        INTEGER("integer", Types.INTEGER, 64, null),
        BIT("bit", Types.BIT, 4, null),
        TINYINT("tinyint", Types.TINYINT, 16, null),
        NUMBER("number", Types.NUMERIC, 12, 2),
        DOUBLE("double", Types.DOUBLE, 12, 2),
        FLOAT("float", Types.FLOAT, 12, 2),
        DECIMAL("decimal", Types.DECIMAL, 6, null),

        // ====================== 时间型 ======================,
        TIME("time", Types.TIME, 6, null),
        TIMESTAMP("timestamp", Types.TIMESTAMP, null, null),
        DATE("date", Types.DATE, 6, null),

        // ====================== 字符型 ======================
        CHAR("char", Types.CHAR, 64, null),
        CHARACTER("character", Types.CHAR, 64, null),
        VARCHAR("varchar", Types.VARCHAR, 64, null),
        NVARCHAR("nvarchar", Types.NVARCHAR, 64, null),
        LONGVARCHAR("longvarchar", Types.LONGVARCHAR, 1024, null),
        LONGNVARCHAR("longnvarchar", Types.LONGNVARCHAR, 1024, null),
        VARBINARY("varbinary", Types.VARBINARY, 1024, null),
        LONGVARBINARY("longvarchar", Types.LONGVARBINARY, 2048, null),
        TEXT("text", Types.LONGNVARCHAR, null, null),

        // ====================== 其他类型 ======================
        BOOLEAN("boolean", Types.BOOLEAN, 0, null),

        // ====================== 高级类型 ======================
        OBJECT("object", Types.JAVA_OBJECT, null, null),
        ARRAY("array", Types.ARRAY, null, null),

        // json
        JSON("json", Types.OTHER, null, null),
        JSONB("jsonb", Types.OTHER, null, null);

        private final String name;
        private final int jdbcType;
        private final Integer precision;
        private final Integer scale;
    }
}
