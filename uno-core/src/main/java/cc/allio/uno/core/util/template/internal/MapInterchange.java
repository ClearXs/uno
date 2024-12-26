package cc.allio.uno.core.util.template.internal;

import cc.allio.uno.core.type.Types;

import java.util.Map;

/**
 * Map替换。
 *
 * @author j.x
 * @since 1.1.2
 */
public class MapInterchange extends BaseInterchange implements KeyInterchange {

    @Override
    protected Object onChange(String text, Object value, boolean langsym) {
        Map<?, ?> map = (Map<?, ?>) value;
        return reValue(map.get(text), langsym);
    }

    @Override
    protected boolean onCheck(Object value) {
        return Types.isMap(value.getClass());
    }
}
