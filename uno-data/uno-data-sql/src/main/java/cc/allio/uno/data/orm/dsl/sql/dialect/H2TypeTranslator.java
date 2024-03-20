package cc.allio.uno.data.orm.dsl.sql.dialect;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.data.orm.dsl.dialect.TypeTranslator;
import cc.allio.uno.data.orm.dsl.sql.dialect.type.H2SQLTypeDelegate;
import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.data.orm.dsl.type.DSLType;

/**
 * h2
 *
 * @author j.x
 * @date 2024/1/8 19:50
 * @since 1.1.7
 */
@AutoService(TypeTranslator.class)
public class H2TypeTranslator implements TypeTranslator {

    @Override
    public DSLType translate(DSLType dslType) {
        return new H2SQLTypeDelegate(dslType);
    }

    @Override
    public DSLType translate(DSLType dslType, Integer precision, Integer scale) {
        return new H2SQLTypeDelegate(dslType, precision, scale);
    }

    @Override
    public DBType getDBType() {
        return DBType.H2;
    }
}
