package cc.allio.uno.test;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.classreading.MetadataReader;

import java.io.IOException;
import java.util.Set;

/**
 * 用于{@link TestComponentScan}的类路径扫描器
 *
 * @author j.x
 * @date 2023/4/20 17:16
 * @since 1.1.4
 */
public class TestComponentScanner extends ClassPathBeanDefinitionScanner {
    public TestComponentScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        return super.doScan(basePackages);
    }

    @Override
    protected boolean isCandidateComponent(MetadataReader metadataReader) throws IOException {
        return true;
    }
}
