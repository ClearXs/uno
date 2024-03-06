package cc.allio.uno.data.test.model;

import cc.allio.uno.data.orm.dsl.ColumnDef;
import cc.allio.uno.data.orm.dsl.DSLName;
import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.data.orm.dsl.type.DSLType;
import cc.allio.uno.data.orm.dsl.type.DataType;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * 常用的数据集
 *
 * @author jiangwei
 * @date 2024/2/8 14:10
 * @since 1.1.7
 */
public final class DataSets {

    public static final Table DUAL = Table.of("dual");

    public static final ColumnDef ID =
            ColumnDef.builder()
                    .dslName(DSLName.of("id"))
                    .isPk(true)
                    .isNonNull(true)
                    .dataType(DataType.createNumberType(DSLType.BIGINT, 64))
                    .build();
    public static final ColumnDef CREATE_BY =
            cc.allio.uno.data.orm.dsl.ColumnDef.builder()
                    .dslName(DSLName.of("create_by"))
                    .defaultValue(0)
                    .dataType(DataType.createNumberType(DSLType.BIGINT, 64))
                    .build();
    public static final ColumnDef CREATE_TIME =
            cc.allio.uno.data.orm.dsl.ColumnDef.builder()
                    .dslName(DSLName.of("create_time"))
                    .dataType(DataType.create(DSLType.TIMESTAMP))
                    .build();
    public static final ColumnDef UPDATE_BY =
            ColumnDef.builder()
                    .dslName(DSLName.of("update_by"))
                    .dataType(DataType.createNumberType(DSLType.BIGINT, 64))
                    .build();
    public static final ColumnDef UPDATE_TIME =
            ColumnDef.builder()
                    .dslName(DSLName.of("update_time"))
                    .dataType(DataType.create(DSLType.TIMESTAMP))
                    .build();
    public static final ColumnDef VERSION =
            ColumnDef.builder()
                    .dslName(DSLName.of("version"))
                    .dataType(DataType.create(DSLType.INTEGER))
                    .build();
    public static final ColumnDef IS_DELETED =
            ColumnDef.builder()
                    .dslName(DSLName.of("create_time"))
                    .dataType(DataType.create(DSLType.SMALLINT))
                    .build();
    public static final ColumnDef TENANT_ID =
            ColumnDef.builder()
                    .dslName(DSLName.of("create_time"))
                    .dataType(DataType.createCharType(DSLType.VARCHAR, 32))
                    .build();

    public static final List<ColumnDef> COLUMN_DEFS = Lists.newArrayList(ID, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, VERSION, IS_DELETED, TENANT_ID);
}
