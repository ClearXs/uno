package cc.allio.uno.data.orm.executor.handler;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.data.orm.dsl.ColumnDef;
import cc.allio.uno.data.orm.dsl.DSLName;
import cc.allio.uno.data.orm.dsl.ddl.ShowColumnsOperator;
import cc.allio.uno.data.orm.dsl.dialect.TypeTranslator;
import cc.allio.uno.data.orm.dsl.dialect.TypeTranslatorHolder;
import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.data.orm.dsl.type.DataType;
import cc.allio.uno.data.orm.dsl.type.DSLType;
import cc.allio.uno.data.orm.executor.ResultGroup;
import cc.allio.uno.data.orm.executor.ResultSet;
import cc.allio.uno.data.orm.executor.options.ExecutorOptions;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * {@link ColumnDef}结果集处理器
 *
 * @author jiangwei
 * @date 2023/7/4 14:54
 * @since 1.1.4
 */
public class ColumnDefListResultSetHandler extends ExecutorOptionsAwareImpl implements ListResultSetHandler<ColumnDef> {

    protected static final String NULL_YES = "YES";
    protected static final String NULL_NO = "NO";

    @Override
    public List<ColumnDef> apply(ResultSet resultSet) {
        return Lists.newArrayList(resultSet)
                .stream()
                .map(r -> commonFieldBuilder(r).build())
                .toList();
    }

    protected ColumnDef.ColumnDefBuilder commonFieldBuilder(ResultGroup r) {
        ColumnDef.ColumnDefBuilder builder = ColumnDef.builder();
        // column name
        DSLName dslName = r.getStringValue(ShowColumnsOperator.COLUMN_NAME_FIELD, () -> StringPool.EMPTY, columnName -> DSLName.of(columnName, DSLName.PLAIN_FEATURE));
        builder.dslName(dslName);
        // column type
        ExecutorOptions executorOptions = obtainExecutorOptions();
        DBType dbType = executorOptions.getDbType();
        TypeTranslator typeTranslator = TypeTranslatorHolder.getTypeTranslator(dbType);
        DSLType dslType = typeTranslator.reserve(r.getStringValue(ShowColumnsOperator.DATA_TYPE_FIELD));
        if (dslType != null) {
            DataType dataType = buildDataType(dslType, r);
            builder.dataType(dataType);
        }
        // default
        Object defaultValue = r.getValue(ShowColumnsOperator.COLUMN_DEFAULT_FIELD);
        builder.defaultValue(defaultValue);
        // is null
        String nullable = r.getStringValue(ShowColumnsOperator.IS_NULLABLE_FIELD);
        builder.isNull(NULL_YES.equals(nullable));
        builder.isNonNull(NULL_NO.equals(nullable));
        return builder;
    }

    protected DataType buildDataType(DSLType dslType, ResultGroup r) {
        DataType dataType = DataType.create(dslType);
        Long characterMaximumLength = r.getLongValue(ShowColumnsOperator.CHARACTER_MAXIMUM_LENGTH_FIELD);
        if (characterMaximumLength != null) {
            dataType.setPrecision(characterMaximumLength.intValue());
        }
        Long numericPrecision = r.getLongValue(ShowColumnsOperator.NUMERIC_PRECISION_FIELD);
        if (numericPrecision != null) {
            dataType.setPrecision(numericPrecision.intValue());
        }
        Long numericScale = r.getLongValue(ShowColumnsOperator.NUMERIC_SCALE_FIELD);
        if (numericScale != null) {
            dataType.setScale(numericScale.intValue());
        }
        return dataType;
    }
}
