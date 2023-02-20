package cc.allio.uno.test.env;

import cn.easyes.starter.config.EsAutoConfiguration;
import cn.easyes.starter.factory.IndexStrategyFactory;
import cn.easyes.starter.service.impl.AutoProcessIndexSmoothlyServiceImpl;
import cc.allio.uno.test.BaseCoreTest;

/**
 * easy-es测试环境
 *
 * @author jiangwei
 * @date 2022/7/5 15:56
 * @since 1.0.6
 */
public class EasyEsTestEnvironment implements TestSpringEnvironment {

    Class<?> mapperScanClass;

    public EasyEsTestEnvironment() {
    }

    public EasyEsTestEnvironment(Class<?> mapperScanClass) {
        this.mapperScanClass = mapperScanClass;
    }

    @Override
    public void support(BaseCoreTest test) {
        test.registerComponent(EsAutoConfiguration.class, IndexStrategyFactory.class, AutoProcessIndexSmoothlyServiceImpl.class);
    }

}
