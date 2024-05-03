package cc.allio.uno.core.util.template.internal;

import cc.allio.uno.core.bean.BeanWrapper;
import cc.allio.uno.core.type.Types;

/**
 * Bean对象替换
 *
 * @author j.x
 * @date 2022/12/3 19:38
 * @since 1.1.2
 */
public class BeanInterchange extends BaseInterchange implements KeyInterchange {

    @Override
    protected Object onChange(String text, Object value, boolean langsym) {
        BeanWrapper wrapper = new BeanWrapper(value);
        return reValue(wrapper.getForce(text), langsym);
    }

    @Override
    protected boolean onCheck(Object value) {
        return Types.isBean(value.getClass());
    }
}