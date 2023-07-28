package cc.allio.uno.test.env;

import cn.easyes.starter.config.EsAutoConfiguration;
import cn.easyes.starter.factory.IndexStrategyFactory;
import cn.easyes.starter.service.impl.AutoProcessIndexSmoothlyServiceImpl;
import cc.allio.uno.test.CoreTest;

import java.lang.annotation.Annotation;

/**
 * easy-es测试环境
 *
 * @author jiangwei
 * @date 2022/7/5 15:56
 * @since 1.0.6
 */
public class EasyEsEnvironment extends VisitorEnvironment {

    @Override
    protected void onSupport(CoreTest coreTest) throws Throwable {
        coreTest.registerAutoConfiguration(
                EsAutoConfiguration.class,
                IndexStrategyFactory.class,
                AutoProcessIndexSmoothlyServiceImpl.class);
    }

    @Override
    public Class<? extends Annotation>[] getPropertiesAnnotation() {
        return null;
    }
}
