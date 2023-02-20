package cc.allio.uno.data.mybatis.injector;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;

import java.util.Collections;
import java.util.List;

/**
 * 新增{@link QueryList}方法进行注入
 *
 * @author jiangwei
 * @date 2022/9/30 16:38
 * @since 1.1.0
 */
public class QuerySqlInjector extends DefaultSqlInjector {

    List<AbstractMethod> otherMethod;

    public QuerySqlInjector(List<AbstractMethod> otherMethod) {
        this.otherMethod = otherMethod;
    }

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass);
        methodList.add(new QueryList());
        methodList.addAll(otherMethod);
        return Collections.unmodifiableList(methodList);
    }
}
