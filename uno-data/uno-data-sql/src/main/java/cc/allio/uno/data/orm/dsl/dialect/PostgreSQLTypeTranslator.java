package cc.allio.uno.data.orm.dsl.dialect;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.data.orm.dsl.type.PostgreSQLType;
import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.data.orm.dsl.type.DSLType;

@AutoService(TypeTranslator.class)
public class PostgreSQLTypeTranslator implements TypeTranslator {
    @Override
    public DSLType translate(DSLType sqlType) {
        return new PostgreSQLType(sqlType);
    }

    @Override
    public DBType getDBType() {
        return DBType.POSTGRESQL;
    }
}
