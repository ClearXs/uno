package cc.allio.uno.data.orm.dsl.dialect;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.data.orm.dsl.type.MySQLType;
import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.data.orm.dsl.type.DSLType;

@AutoService(TypeTranslator.class)
public class MySQLTypeTranslator implements TypeTranslator {
    @Override
    public DSLType translate(DSLType sqlType) {
        return new MySQLType(sqlType);
    }

    @Override
    public DBType getDBType() {
        return DBType.MYSQL;
    }
}
