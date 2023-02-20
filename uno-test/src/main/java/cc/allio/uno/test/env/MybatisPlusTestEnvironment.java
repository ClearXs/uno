package cc.allio.uno.test.env;

import cc.allio.uno.test.BaseCoreTest;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;

import javax.annotation.Priority;
import java.util.Arrays;

/**
 * Mybatis-plus测试环境类
 *
 * @author jiangwei
 * @date 2022/2/14 14:36
 * @since 1.0
 */
@Priority(Integer.MAX_VALUE)
public class MybatisPlusTestEnvironment implements TestSpringEnvironment {

    static final MybatisPlusProperties DEFAULT_MYBATIS_PLUS_PROPERTIES = new MybatisPlusProperties();
    final MybatisPlusProperties properties;
    Class<?> mapperScan;

    public MybatisPlusTestEnvironment() {
        this.properties = DEFAULT_MYBATIS_PLUS_PROPERTIES;
    }

    public MybatisPlusTestEnvironment(Class<?> mapperScan) {
        this(DEFAULT_MYBATIS_PLUS_PROPERTIES, mapperScan);
    }

    public MybatisPlusTestEnvironment(MybatisPlusProperties properties, Class<?> mapperScan) {
        this.properties = properties;
        this.mapperScan = mapperScan;
    }

    @Override
    public void support(BaseCoreTest test) {
        test.addProperty("mybatis-plus.mapper-locations", test.getProperty("mybatis-plus.mapper-locations", Arrays.toString(properties.getMapperLocations())));
        test.addProperty("mybatis-plus.typeAliasesPackage", test.getProperty("mybatis-plus.typeAliasesPackage", properties.getTypeAliasesPackage()));
        test.addProperty("mybatis-plus.type-enums-package", test.getProperty("mybatis-plus.type-enums-package", properties.getTypeEnumsPackage()));
        test.registerComponent(MybatisPlusAutoConfiguration.class);
        if (mapperScan != null) {
            test.registerComponent(mapperScan);
        }
    }

}
