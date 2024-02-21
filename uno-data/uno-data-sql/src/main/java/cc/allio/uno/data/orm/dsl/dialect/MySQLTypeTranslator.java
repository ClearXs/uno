package cc.allio.uno.data.orm.dsl.dialect;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.data.orm.dsl.dialect.type.MySQLTypeDelegate;
import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.data.orm.dsl.type.DSLType;

@AutoService(TypeTranslator.class)
public class MySQLTypeTranslator implements TypeTranslator {

    @Override
    public DSLType translate(DSLType dslType) {
        return new MySQLTypeDelegate(dslType);
    }

    @Override
    public DSLType translate(DSLType dslType, Integer precision, Integer scale) {
        return new MySQLTypeDelegate(dslType, precision, scale);
    }

    @Override
    public DBType getDBType() {
        return DBType.MYSQL;
    }
}
