package cc.allio.uno.core.spi;

import cc.allio.uno.core.BaseTestCase;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import java.util.ServiceLoader;

public class ClassPathServiceLoaderTest extends BaseTestCase {

    @Test
    void testNoArgsConstructor() {
        ClassPathServiceLoader<User> load = ClassPathServiceLoader.load(User.class);
        User user = Lists.newArrayList(load)
                .stream()
                .map(ServiceLoader.Provider::get)
                .findFirst()
                .orElse(null);
        assertNotNull(user);
    }

    @Test
    void testArgsConstructor() {
        ClassPathServiceLoader<Product> load = ClassPathServiceLoader.load(Product.class, "123");
        Product product = Lists.newArrayList(load)
                .stream()
                .map(ServiceLoader.Provider::get)
                .findFirst()
                .orElse(null);
        assertNotNull(product);
        assertEquals("123", product.getName());
    }
}
