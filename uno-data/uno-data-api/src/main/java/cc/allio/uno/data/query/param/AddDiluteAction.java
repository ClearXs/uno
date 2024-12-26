package cc.allio.uno.data.query.param;

import cc.allio.uno.core.bean.BeanWrapper;
import cc.allio.uno.core.type.TypeOperatorFactory;
import cc.allio.uno.data.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;

/**
 * 数据求和
 *
 * @author j.x
 * @since 1.1.0
 */
@Slf4j
public class AddDiluteAction implements DiluteAction {

    @Override
    public void trigger(QueryWrapper queryWrapper, Object o, Object t) {
        String[] dataFields = queryWrapper.getDataFields();
        for (String dataField : dataFields) {
            try {
                BeanWrapper oWrapper = new BeanWrapper(o);
                Object oField = oWrapper.getForce(dataField);
                BeanWrapper tWrapper = new BeanWrapper(t);
                Object tField = tWrapper.getForce(dataField);
                Object result = TypeOperatorFactory.translator((Class<Object>) oField.getClass()).add(oField, tField);
                oWrapper.setForce(dataField, result);
            } catch (Throwable ex) {
                // ignore
            }
        }
    }
}
