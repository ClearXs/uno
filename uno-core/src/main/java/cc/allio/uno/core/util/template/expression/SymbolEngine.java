package cc.allio.uno.core.util.template.expression;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.util.type.Types;

import java.util.Optional;

/**
 * 使用指定的'symbol'如（',' '.'）对表达式进行切分，使其进行分层级构建{@link Interchange}对象，并调用其{@link Interchange#change(String, Object)}方法获取当前层级的Value值
 *
 * @author jiangwei
 * @date 2022/12/3 20:13
 * @since 1.1.2
 */
public class SymbolEngine implements Engine {

    private final String symbol;

    public SymbolEngine(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String run(String expression, Object value) throws Throwable {
        String[] texts = expression.split(symbol);
        Object layerValue = value;
        for (String layer : texts) {
            // 寻找是否是List
            int startListableIndex = layer.indexOf(StringPool.LEFT_SQ_BRACKET);
            int endListableIndex = layer.lastIndexOf(StringPool.RIGHT_SQ_BRACKET);
            Interchange interchange = null;
            if (startListableIndex > 0 && endListableIndex > 0) {
                // 在取赋予之前先确定当前的值是否为Map Or Object，存在lists[0]的情况
                String mapLists = layer.substring(0, startListableIndex);
                if (Types.isMap(value.getClass())) {
                    layerValue = new MapInterchange().onChange(mapLists, layerValue);
                }
                if (Types.isBean(value.getClass())) {
                    layerValue = new BeanInterchange().onChange(mapLists, layerValue);
                }
                interchange = new ListInterchange();
            }
            // 寻找KeyInterchange
            if (interchange == null) {
                if (Types.isMap(layerValue.getClass())) {
                    interchange = new MapInterchange();
                }
                if (Types.isBean(layerValue.getClass())) {
                    interchange = new BeanInterchange();
                }
            }
            if (interchange != null) {
                layerValue = interchange.change(layer, layerValue);
            } else {
                return Optional.of(layerValue).map(Object::toString).orElse(expression);
            }

        }
        if (layerValue instanceof Optional) {
            return ((Optional<?>) layerValue).map(Object::toString).orElse(expression);
        }
        return Optional.ofNullable(layerValue).map(Object::toString).orElse(expression);
    }

}
