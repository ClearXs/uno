package cc.allio.uno.test;

import org.junit.jupiter.api.Test;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;

class PropertySourceLoaderTest extends BaseTestCase {

    @Test
    void testLoadClasspathYaml() throws IOException {
        URL url = ResourceUtils.getURL("classpath:whatever.yaml");
        YamlPropertySourceLoader loader = new YamlPropertySourceLoader();
        List<PropertySource<?>> load = loader.load("whatever.yaml", new UrlResource(url));
        PropertySource<?> propertySource = load.get(0);
        assertEquals("test", propertySource.getProperty("allio.uno"));
    }

    @Override
    protected void onInit() throws Throwable {

    }

    @Override
    protected void onDown() throws Throwable {

    }
}
