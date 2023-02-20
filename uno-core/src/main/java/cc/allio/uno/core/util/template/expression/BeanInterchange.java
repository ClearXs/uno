package cc.allio.uno.core.util.template.expression;

import cc.allio.uno.core.bean.ObjectWrapper;
import cc.allio.uno.core.util.type.Types;

/**
 * Bean对象替换
 *
 * @author jiangwei
 * @date 2022/12/3 19:38
 * @since 1.1.2
 */
public class BeanInterchange extends BaseInterchange implements KeyInterchange {

    @Override
    protected Object onChange(String text, Object value) {
        ObjectWrapper wrapper = new ObjectWrapper(value);
        return wrapper.getForce(text);
    }

    @Override
    protected boolean onCheck(Object value) {
        return Types.isBean(value.getClass());
    }

}
