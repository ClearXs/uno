package cc.allio.uno.data.orm.dsl.type;

import cc.allio.uno.core.api.EqualsTo;
import lombok.Data;

/**
 * 数据类型
 *
 * @author j.x
 * @date 2023/4/12 19:54
 * @since 1.1.4
 */
@Data
public class DataType implements EqualsTo<DataType> {

    // SQL类型
    private DSLType dslType;
    // 精度
    private Integer precision;
    // 范围
    private Integer scale;

    @Override
    public boolean equalsTo(DataType other) {
        if (other == null) {
            return false;
        }
        if (this == other) {
            return true;
        }
        if (this.getDslType() == null) {
            return false;
        }
        return dslType.equalsTo(other.getDslType());
    }

    /**
     * 创建Data type
     *
     * @param sqlType sqlType
     * @return DataType
     */
    public static DataType create(DSLType sqlType) {
        DataType dataType = new DataType();
        dataType.setDslType(sqlType);
        dataType.setPrecision(sqlType.getPrecision());
        dataType.setScale(sqlType.getScale());
        return dataType;
    }

    /**
     * 创建数字数据类型
     *
     * @param sqlType   SQLType
     * @param precision 精度
     * @return DataType
     */
    public static DataType createNumberType(DSLType sqlType, Integer precision) {
        return createNumberType(sqlType, precision, null);
    }

    /**
     * 创建数字数据类型
     *
     * @param sqlType   SQLType
     * @param precision 精度
     * @param scale     范围
     * @return DataType
     */
    public static DataType createNumberType(DSLType sqlType, Integer precision, Integer scale) {
        DataType dataType = new DataType();
        dataType.setDslType(sqlType);
        dataType.setPrecision(precision);
        dataType.setScale(scale);
        return dataType;
    }

    /**
     * 创建字符数据类型
     *
     * @param sqlType   SQLType
     * @param precision precision
     * @return DataType
     */
    public static DataType createCharType(DSLType sqlType, Integer precision) {
        DataType dataType = new DataType();
        dataType.setDslType(sqlType);
        dataType.setPrecision(precision);
        return dataType;
    }

    /**
     * 创建时间型的数据类型
     *
     * @param sqlType SQLType
     * @return DataType
     */
    public static DataType createTimeType(DSLType sqlType) {
        DataType dataType = new DataType();
        dataType.setDslType(sqlType);
        return dataType;
    }
}
