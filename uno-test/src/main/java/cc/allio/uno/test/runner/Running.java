package cc.allio.uno.test.runner;

import java.lang.annotation.*;

/**
 * 给定于测试注解中，标识使用何种{@link Runner}进行测试环境构建
 *
 * @author jiangwei
 * @date 2022/10/28 16:32
 * @since 1.1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Running {

    /**
     * 给定于具体实现{@link Runner}的class对象
     *
     * @return class对象实例
     */
    Class<? extends Runner> value();
}
