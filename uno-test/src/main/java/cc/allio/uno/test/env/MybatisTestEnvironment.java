package cc.allio.uno.test.env;

import cc.allio.uno.test.BaseCoreTest;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.mybatis.spring.boot.autoconfigure.MybatisLanguageDriverAutoConfiguration;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;

import java.util.Arrays;

/**
 * Mybatis测试环境类
 *
 * @author jiangwei
 * @date 2022/2/14 14:27
 * @since 1.0
 */
public class MybatisTestEnvironment implements TestSpringEnvironment {

    static final MybatisProperties DEFAULT_MYBATIS_PROPERTIES = new MybatisProperties();
    final MybatisProperties mybatisProperties;
    Class<?> mapperScan;

    public MybatisTestEnvironment() {
        this.mybatisProperties = DEFAULT_MYBATIS_PROPERTIES;
    }

    public MybatisTestEnvironment(Class<?> mapperScan) {
        this(DEFAULT_MYBATIS_PROPERTIES, mapperScan);
    }

    public MybatisTestEnvironment(MybatisProperties mybatisProperties, Class<?> mapperScan) {
        this.mybatisProperties = mybatisProperties;
        this.mapperScan = mapperScan;
    }

    @Override
    public void support(BaseCoreTest test) {
        test.addProperty("mybatis.mapperLocations", test.getProperty("mybatis.mapperLocations", Arrays.toString(mybatisProperties.getMapperLocations())));
        test.registerComponent(MybatisAutoConfiguration.class, MybatisLanguageDriverAutoConfiguration.class);
        if (mapperScan != null) {
            test.registerComponent(mapperScan);
        }
    }

}
