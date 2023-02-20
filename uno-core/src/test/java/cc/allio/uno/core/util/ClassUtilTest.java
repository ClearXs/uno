package cc.allio.uno.core.util;

import cc.allio.uno.core.BaseTestCase;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Map;

@Slf4j
class ClassUtilTest extends BaseTestCase {
    @Override
    protected void onInit() throws Throwable {

    }

    /**
     * Test Case: 测试方法上泛形
     */
    @Test
    void testMethodGeneric() {
        Generic<String> generic = new Generic<>();
        Method returnMap = ClassUtil.getMethod(generic.getClass(), "returnMap");
        Class<?> returnType = returnMap.getReturnType();
        Map<String, String> stringStringMap = generic.returnMap();
        log.info(returnType.getSimpleName());
    }

    @Override
    protected void onDown() throws Throwable {

    }

    static class Generic<T> {

        public <K, V> Map<K, V> returnMap() {
            return Maps.newHashMap();
        }
    }
}
