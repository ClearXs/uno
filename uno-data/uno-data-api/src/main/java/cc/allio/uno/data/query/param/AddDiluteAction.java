package cc.allio.uno.data.query.param;

import cc.allio.uno.core.bean.ObjectWrapper;
import cc.allio.uno.core.type.TypeOperatorFactory;
import cc.allio.uno.data.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;

/**
 * 数据求和
 *
 * @author jiangwei
 * @date 2022/10/10 16:54
 * @since 1.1.0
 */
@Slf4j
public class AddDiluteAction implements DiluteAction {

    @Override
    public void trigger(QueryWrapper queryWrapper, Object o, Object t) {
        String[] dataFields = queryWrapper.getDataFields();
        for (String dataField : dataFields) {
            try {
                ObjectWrapper oWrapper = new ObjectWrapper(o);
                Object oField = oWrapper.getForce(dataField);
                ObjectWrapper tWrapper = new ObjectWrapper(t);
                Object tField = tWrapper.getForce(dataField);
                Object result = TypeOperatorFactory.translator((Class<Object>) oField.getClass()).add(oField, tField);
                oWrapper.setForce(dataField, result);
            } catch (Throwable ex) {
                // ignore
            }
        }
    }
}
