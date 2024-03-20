package cc.allio.uno.data.orm.dsl.sql.dialect;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.data.orm.dsl.dialect.TypeTranslator;
import cc.allio.uno.data.orm.dsl.sql.dialect.type.PostgreSQLTypeDelegate;
import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.data.orm.dsl.type.DSLType;

@AutoService(TypeTranslator.class)
public class PostgreSQLTypeTranslator implements TypeTranslator {

    @Override
    public DSLType translate(DSLType dslType) {
        return new PostgreSQLTypeDelegate(dslType);
    }

    @Override
    public DSLType translate(DSLType dslType, Integer precision, Integer scale) {
        return new PostgreSQLTypeDelegate(dslType, precision, scale);
    }

    @Override
    public DSLType reserve(String dslTypeName) {
        DSLType dslType = TypeTranslator.super.reserve(dslTypeName);
        if (dslType == null) {
            for (PostgreSQLTypeDelegate.PostgreSQLLinkType type : PostgreSQLTypeDelegate.PostgreSQLLinkType.values()) {
                if (type.getName().equalsIgnoreCase(dslTypeName)) {
                    return DSLType.create(type, null, null);
                }
            }
        }
        return dslType;
    }

    @Override
    public DBType getDBType() {
        return DBType.POSTGRESQL;
    }
}
