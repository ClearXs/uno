package cc.allio.uno.data.orm.dsl;

import cc.allio.uno.core.api.EqualsTo;
import cc.allio.uno.core.api.Self;
import cc.allio.uno.core.type.TypeOperatorFactory;
import cc.allio.uno.data.orm.dsl.type.DataType;
import cc.allio.uno.data.orm.dsl.type.JavaType;
import cc.allio.uno.data.orm.dsl.type.TypeRegistry;
import lombok.*;

import java.util.Objects;
import java.util.Optional;

/**
 * DSL字段定义
 *
 * @author j.x
 * @see ColumnDefBuilder
 * @since 1.1.4
 */
@Data
@EqualsAndHashCode(of = {"dslName", "dataType"})
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ColumnDef implements EqualsTo<ColumnDef>, Meta<ColumnDef> {

    // 字段名称
    private DSLName dslName;
    // 字段注释
    private String comment;
    // 数据类型
    private DataType dataType;
    // 默认值
    private Object defaultValue;
    // 是否为主键
    private boolean isPk;
    // 是否为外键
    private boolean isFk;
    // 是否不为null
    private boolean isNonNull;
    // 是否为null
    private boolean isNull;
    // 是否唯一
    private boolean isUnique;
    // 是否是删除标识字段
    private boolean isDeleted;
    // 未删除值
    private Object undeleted;
    // 已删除值
    private Object deleted;

    /**
     * 给定字段值按照当前{@link cc.allio.uno.data.orm.dsl.type.DataType}进行转换
     *
     * @param ori ori
     * @return 转换后的值
     */
    public Object castValue(Object ori) {
        return Optional.ofNullable(dataType)
                .map(DataType::getDslType)
                .flatMap(dslType -> {
                    int jdbcType = dslType.getJdbcType();
                    JavaType<?> javaType = TypeRegistry.getInstance().findJavaType(jdbcType);
                    return Optional.ofNullable(javaType);
                })
                .map(j -> TypeOperatorFactory.translator(j.getJavaType()))
                .map(typeOperator -> typeOperator.convert(ori))
                .orElse(null);
    }


    @Override
    public boolean equalsTo(ColumnDef other) {
        return Objects.equals(this.getDslName(), other.getDslName())
                && dataType.equalsTo(other.getDataType());
    }

    /**
     * get {@link ColumnDefBuilder} builder
     *
     * @return the {@link ColumnDefBuilder} instance
     */
    public static ColumnDefBuilder builder() {
        return new ColumnDefBuilder();
    }

    /**
     * {@link ColumnDef} builder
     */
    public static class ColumnDefBuilder implements Self<ColumnDefBuilder> {
        // 字段名称
        private DSLName dslName;
        // 字段注释
        private String comment;
        // 数据类型
        private DataType dataType;
        // 默认值
        private Object defaultValue;
        // 是否为主键
        private boolean isPk;
        // 是否为外键
        private boolean isFk;
        // 是否不为null
        private boolean isNonNull;
        // 是否为null
        private boolean isNull;
        // 是否唯一
        private boolean isUnique;
        // 是否是删除标识字段
        private boolean isDeleted;
        // 未删除值
        private Object undeleted;
        // 已删除值
        private Object deleted;

        /**
         * build column name
         *
         * @param dslName the dsl name
         * @return {@link ColumnDefBuilder}
         */
        public ColumnDefBuilder dslName(DSLName dslName) {
            this.dslName = dslName;
            return self();
        }

        /**
         * build column comment
         *
         * @param comment the comment
         * @return {@link ColumnDefBuilder}
         */
        public ColumnDefBuilder comment(String comment) {
            this.comment = comment;
            return self();
        }

        /**
         * build column data type
         *
         * @param dataType the data type
         * @return {@link ColumnDefBuilder}
         */
        public ColumnDefBuilder dataType(DataType dataType) {
            this.dataType = dataType;
            return self();
        }

        /**
         * build column default value
         *
         * @param defaultValue the default value
         * @return {@link ColumnDefBuilder}
         */
        public ColumnDefBuilder defaultValue(Object defaultValue) {
            this.defaultValue = defaultValue;
            return self();
        }

        /**
         * build column is pk
         *
         * @param isPk the is pk
         * @return {@link ColumnDefBuilder}
         */
        public ColumnDefBuilder isPk(Boolean isPk) {
            this.isPk = isPk;
            return self();
        }

        /**
         * build column is fk
         *
         * @param isFk the is fk
         * @return {@link ColumnDefBuilder}
         */
        public ColumnDefBuilder isFk(Boolean isFk) {
            this.isFk = isFk;
            return self();
        }

        /**
         * build column is non null
         *
         * @param isNonNull the is non null
         * @return {@link ColumnDefBuilder}
         */
        public ColumnDefBuilder isNonNull(Boolean isNonNull) {
            this.isNonNull = isNonNull;
            return self();
        }

        /**
         * build column is null
         *
         * @param isNull the is null
         * @return {@link ColumnDefBuilder}
         */
        public ColumnDefBuilder isNull(Boolean isNull) {
            this.isNull = isNull;
            return self();
        }

        /**
         * build column is unique
         *
         * @param isUnique the is unique
         * @return {@link ColumnDefBuilder}
         */
        public ColumnDefBuilder isUnique(Boolean isUnique) {
            this.isUnique = isUnique;
            return self();
        }

        /**
         * build column is deleted
         *
         * @param isDeleted the is deleted
         * @return {@link ColumnDefBuilder}
         */
        public ColumnDefBuilder isDeleted(Boolean isDeleted) {
            this.isDeleted = isDeleted;
            return self();
        }

        /**
         * build undeleted
         *
         * @param undeleted the undeleted
         * @return {@link ColumnDefBuilder}
         */
        public ColumnDefBuilder undeleted(Object undeleted) {
            this.undeleted = undeleted;
            return self();
        }

        /**
         * build deleted
         *
         * @param deleted the deleted
         * @return {@link ColumnDefBuilder}
         */
        public ColumnDefBuilder deleted(Object deleted) {
            this.deleted = deleted;
            return self();
        }

        /**
         * build {@link ColumnDef}
         *
         * @return {@link ColumnDef}
         */
        public ColumnDef build() {
            return new ColumnDef(
                    dslName,
                    comment,
                    dataType,
                    defaultValue,
                    isPk,
                    isFk,
                    isNonNull,
                    isNull,
                    isUnique,
                    isDeleted,
                    undeleted,
                    deleted);
        }
    }
}
