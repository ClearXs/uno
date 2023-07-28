package cc.allio.uno.data.query.mybatis;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 自定义注入SQl
 *
 * @author jiangwei
 * @date 2022/12/14 22:57
 * @since 1.1.3
 */
public class UnoSqlInjector extends DefaultSqlInjector {

    private List<AbstractMethod> otherMethod;

    public UnoSqlInjector(List<AbstractMethod> otherMethod) {
        this.otherMethod = otherMethod;
    }

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
        List<AbstractMethod> methodList = new ArrayList<>();
        methodList.add(new InsertBatchSomeColumn(i -> i.getFieldFill() != FieldFill.UPDATE));
        methodList.addAll(super.getMethodList(mapperClass));
        methodList.addAll(otherMethod);
        return Collections.unmodifiableList(methodList);
    }
}
