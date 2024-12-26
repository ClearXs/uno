package cc.allio.uno.core.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import cc.allio.uno.core.BaseTestCase;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;

/**
 * 字符串工具集测试
 *
 * @author j.x
 */
class StringUtilBaseTest extends BaseTestCase {

    /**
     * 测试裁剪普通的url地址
     */
    @Test
    void testCropNormalUrl() {
        String url = "https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=271657503,3457309198&fm=179&app=35&f=PNG";
        String realUrl = StringUtils.cropUrl(url);
        assertEquals("https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=271657503,3457309198&fm=179&app=35&f=PNG", realUrl);
    }

    /**
     * 测试裁剪时对请求参数的消费
     */
    @Test
    void testCuttingParameterConsumer() {
        String url = "https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=271657503,3457309198&fm=179&app=35&f=PNG?w=96";
        String realUrl = StringUtils.cropUrl(url, tuple2 -> {
            assertEquals("w", tuple2.getT1());
            assertEquals("96", tuple2.getT2().get(0));
        });
        assertEquals("https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=271657503,3457309198&fm=179&app=35&f=PNG", realUrl);
    }

    /**
     * 测试裁剪时发送参数
     */
    @Test
    void testCuttingEmitterParameter() {
        String url = "https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=271657503,3457309198&fm=179&app=35&f=PNG?w=96";
        AtomicReference<FluxSink<Tuple2<String, List<String>>>> emitter = new AtomicReference<>();
        Flux.create(emitter::set)
                .subscribe(tuple2 -> {
                    assertEquals("w", tuple2.getT1());
                    assertEquals("96", tuple2.getT2().get(0));
                });
        StringUtils.cropUrl(url, emitter.get());
    }

    /**
     * 测试裁剪placeholder
     */
    @Test
    void testCuttingPlaceholderUrl() {
        String url = "https://www.json.cn/{id}";
        String cropUrl = StringUtils.cropUrl(url, tuple2 -> {
            assertEquals("id", tuple2.getT1());
            assertEquals("id", tuple2.getT2().get(0));
        });
        assertEquals(url, cropUrl);
    }

    /**
     * 测试多个裁剪的placeholder
     */
    @Test
    void testCuttingMultiPlaceholderUrl() {
        AtomicInteger count = new AtomicInteger(1);
        String url = "https://www.json.cn/{id}/{name}";
        String cropUrl = StringUtils.cropUrl(url, tuple2 -> {
            int i = count.getAndIncrement();
            if (i == 1) {
                assertEquals("id", tuple2.getT1());
                assertEquals("id", tuple2.getT2().get(0));
            } else if (i == 2) {
                assertEquals("name", tuple2.getT1());
                assertEquals("name", tuple2.getT2().get(0));
            }
        });
        assertEquals(url, cropUrl);
    }

    /**
     * 测试url中既有placeholder与参数
     */
    @Test
    void testComposeCutting() {
        AtomicInteger count = new AtomicInteger(1);
        String url = "https://www.json.cn/{id}/{name}?w=96";
        String cropUrl = StringUtils.cropUrl(url, tuple2 -> {
            int i = count.getAndIncrement();
            if (i == 1) {
                assertEquals("w", tuple2.getT1());
                assertEquals("96", tuple2.getT2().get(0));

            } else if (i == 2) {
                assertEquals("id", tuple2.getT1());
                assertEquals("id", tuple2.getT2().get(0));

            } else if (i == 3) {
                assertEquals("name", tuple2.getT1());
                assertEquals("name", tuple2.getT2().get(0));
            }
        });
        assertEquals("https://www.json.cn/{id}/{name}", cropUrl);
    }

    /**
     * 测试不拼接字符串
     */
    @Test
    void testNonJoin() {
        String url = "https://www.json.cn/index.php";
        String joinUrl = StringUtils.joinUrl(url, Collections.emptyMap());
        assertEquals(url, joinUrl);
    }

    /**
     * 测试拼接一个参数
     */
    @Test
    void testJoinOneParameter() {
        String url = "https://www.json.cn/index.php";
        Map<String, String> vars = new HashMap<>();
        vars.put("s", "api");
        String joinUrl = StringUtils.joinUrl(url, vars);
        assertEquals("https://www.json.cn/index.php?s=api", joinUrl);
    }

    /**
     * 测试拼接多个参数
     */
    @Test
    void testJoinMultiParameter() {
        String url = "https://www.json.cn/index.php";
        Map<String, String> vars = new HashMap<>();
        vars.put("s", "api");
        vars.put("app", "blog");
        vars.put("c", "tran");
        vars.put("m", "get_user_status");
        String joinUrl = StringUtils.joinUrl(url, vars);
        assertEquals("https://www.json.cn/index.php?app=blog&s=api&c=tran&m=get_user_status", joinUrl);
    }

    @Test
    void testJoinPlaceholder() {
        String url = "https://www.json.cn/{id}";
        Map<String, String> vars = new HashMap<>();
        vars.put("id", "1");
        String joinUrl = StringUtils.joinUrl(url, vars);
        assertEquals("https://www.json.cn/1", joinUrl);
    }

    @Test
    void testJoinMultiPlaceholder() {
        String url = "https://www.json.cn/{id}/{name}";
        Map<String, String> vars = new HashMap<>();
        vars.put("id", "1");
        vars.put("name", "jw");
        String joinUrl = StringUtils.joinUrl(url, vars);
        assertEquals("https://www.json.cn/1/jw", joinUrl);
    }

    @Test
    void testComposeJoin() {
        String url = "https://www.json.cn/{id}/{name}";
        Map<String, String> vars = new HashMap<>();
        vars.put("id", "1");
        vars.put("name", "jw");
        vars.put("s", "api");
        vars.put("app", "blog");
        vars.put("c", "tran");
        vars.put("m", "get_user_status");
        String joinUrl = StringUtils.joinUrl(url, vars);
        assertEquals("https://www.json.cn/1/jw?app=blog&s=api&c=tran&m=get_user_status", joinUrl);
    }

    /**
     * 测试从链接中获取基本域名端口地址
     */
    @Test
    void testGetBaseUrl() {
        // 1.https
        String templateUrl = "https://blog.csdn.net:8081/gerald2008/article/details/107116526/";
        StepVerifier.create(StringUtils.getBaseUrl(templateUrl))
                .expectNext("blog.csdn.net:8081")
                .verifyComplete();
        // 2.http
        templateUrl = "http://blog.csdn.net:8081/gerald2008/article/details/107116526/";
        StepVerifier.create(StringUtils.getBaseUrl(templateUrl))
                .expectNext("blog.csdn.net:8081")
                .verifyComplete();
        // 3.mqtt
        templateUrl = "mqtt://blog.csdn.net:8081/gerald2008/article/details/107116526/";
        StepVerifier.create(StringUtils.getBaseUrl(templateUrl))
                .expectNext("blog.csdn.net:8081")
                .verifyComplete();
        // 4.tcp
        templateUrl = "tcp://blog.csdn.net:8081/gerald2008/article/details/107116526/";
        StepVerifier.create(StringUtils.getBaseUrl(templateUrl))
                .expectNext("blog.csdn.net:8081")
                .verifyComplete();
        // 5.两者皆没有
        templateUrl = "blog.csdn.net:8081/gerald2008/article/details/107116526/";
        StepVerifier.create(StringUtils.getBaseUrl(templateUrl))
                .expectNext("blog.csdn.net:8081")
                .verifyComplete();
    }

    /**
     * 测试请求链接api接口的地址
     */
    @Test
    void testGetApiPath() {
        String templateUrl = "https://blog.csdn.net:8081/gerald2008/article/details/107116526/";
        StepVerifier.create(StringUtils.getApiUrl(templateUrl))
                .expectNext("/gerald2008/article/details/107116526/")
                .verifyComplete();

        templateUrl = "https://blog.csdn.net:8081/gerald2008/article/details/107116526/get?id=1&name=2";
        StepVerifier.create(StringUtils.getApiUrl(templateUrl))
                .expectNext("/gerald2008/article/details/107116526/getValue?id=1&name=2")
                .verifyComplete();

        templateUrl = "";
        StepVerifier.create(StringUtils.getApiUrl(templateUrl))
                .expectNext("")
                .verifyComplete();

        // 如果不是http、https等存在的将返回
        templateUrl = "wasd://blog.csdn.net:8081/gerald2008/article/details/107116526/getValue?id=1&name=2";
        StepVerifier.create(StringUtils.getApiUrl(templateUrl))
                .expectNext("//blog.csdn.net:8081/gerald2008/article/details/107116526/getValue?id=1&name=2")
                .verifyComplete();
    }

    @Test
    void testGetHost() {
        String templateUrl = "https://blog.csdn.net:8081/gerald2008/article/details/107116526/";
        StepVerifier.create(StringUtils.getHost(templateUrl))
                .expectNext("blog.csdn.net")
                .verifyComplete();

        templateUrl = "https://127.0.0.1:8081/gerald2008/article/details/107116526/";
        StepVerifier.create(StringUtils.getHost(templateUrl))
                .expectNext("127.0.0.1")
                .verifyComplete();
    }

    @Test
    void testGetPort() {
        String templateUrl = "https://blog.csdn.net:8081/gerald2008/article/details/107116526/";
        StepVerifier.create(StringUtils.getPort(templateUrl))
                .expectNext(8081)
                .verifyComplete();
        templateUrl = "https://blog.csdn.net/gerald2008/article/details/107116526/";
        StepVerifier.create(StringUtils.getPort(templateUrl))
                .expectNext(0)
                .verifyComplete();
    }
}
