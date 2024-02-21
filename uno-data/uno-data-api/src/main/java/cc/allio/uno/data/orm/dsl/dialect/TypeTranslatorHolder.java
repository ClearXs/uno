package cc.allio.uno.data.orm.dsl.dialect;

import cc.allio.uno.data.orm.dsl.exception.DSLException;
import cc.allio.uno.data.orm.dsl.type.DBType;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.ServiceLoader;

public final class TypeTranslatorHolder {
    private static final Map<String, TypeTranslator> TRANSLATORS = Maps.newConcurrentMap();

    static {
        for (TypeTranslator translator : ServiceLoader.load(TypeTranslator.class)) {
            TRANSLATORS.put(translator.getDBType().getName(), translator);
        }
    }

    public static TypeTranslator getTypeTranslator() {
        return getTypeTranslator(DBType.getSystemDbType());
    }

    public static TypeTranslator getTypeTranslator(DBType dbType) {
        TypeTranslator translator = TRANSLATORS.get(dbType.getName());
        if (translator == null) {
            throw new DSLException("DBType " + dbType.getName() + " not supported");
        }
        return translator;
    }
}
