package cc.allio.uno.core.util.template.expression;

import cc.allio.uno.core.util.type.Types;

import java.util.Map;

/**
 * Map替换。
 * <p>required: </p>
 * <ol>
 *     <li>需要包含泛型</li>
 * </ol>
 *
 * @author jiangwei
 * @date 2022/12/3 19:38
 * @since 1.1.2
 */
public class MapInterchange extends BaseInterchange implements KeyInterchange {

    @Override
    protected Object onChange(String text, Object value) {
        Map<?, ?> map = (Map<?, ?>) value;
        return map.get(text);
    }

    @Override
    protected boolean onCheck(Object value) {
        return Types.isMap(value.getClass());
    }
}
