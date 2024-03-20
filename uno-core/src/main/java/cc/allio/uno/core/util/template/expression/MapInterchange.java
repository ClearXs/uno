package cc.allio.uno.core.util.template.expression;

import cc.allio.uno.core.type.Types;

import java.util.Map;

/**
 * Map替换。
 *
 * @author j.x
 * @date 2022/12/3 19:38
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
