package cc.allio.uno.data.orm.dsl.dialect;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.data.orm.dsl.dialect.type.H2SQLType;
import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.data.orm.dsl.type.DSLType;

/**
 * h2
 *
 * @author jiangwei
 * @date 2024/1/8 19:50
 * @since 1.1.6
 */
@AutoService(TypeTranslator.class)
public class H2TypeTranslator implements TypeTranslator {
    @Override
    public DSLType translate(DSLType sqlType) {
        return new H2SQLType(sqlType);
    }

    @Override
    public DSLType translate(DSLType sqlType, Integer precision, Integer scale) {
        return new H2SQLType(sqlType, precision, scale);
    }

    @Override
    public DBType getDBType() {
        return DBType.H2;
    }
}
