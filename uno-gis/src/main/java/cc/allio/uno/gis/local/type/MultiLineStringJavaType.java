package cc.allio.uno.gis.local.type;

import cc.allio.uno.data.orm.dsl.type.JavaTypeImpl;
import cc.allio.uno.gis.GeometryTypes;
import org.locationtech.jts.geom.MultiLineString;

/**
 * MultiLineString
 *
 * @author j.x
 * @since 1.1.4
 */
public class MultiLineStringJavaType extends JavaTypeImpl<MultiLineString> {

    @Override
    public Class<MultiLineString> getJavaType() {
        return GeometryTypes.MULTI_LINE_STRING;
    }

    @Override
    public boolean equalsTo(Class<?> otherJavaType) {
        return GeometryTypes.MULTI_LINE_STRING.isAssignableFrom(otherJavaType);
    }
}
