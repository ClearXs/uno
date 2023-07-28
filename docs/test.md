为什么会出现uno-test，spring不是已经有一个spring-boot-test了吗？

痛点：

- @SpringBootTest是项目环境的集成测试，并不是单元测试。
- 某一个测试只希望测试某一个问题点，而@SpringBootTest加载许多不需要的组件。

为了解决这个问题，并且提供一套有用有效的单元测试框架，uno-test就孕育而生。具有如下特性：

- 针对单元测试而生
- 适配于常见的测试场景，如数据插入、查询测试...
- 基于spring环境，依赖少，启动快。
- 全注解化，使用简单。

## 1.简单运用

### 1.1 mybatis环境

```java
@RunTest
@MybatisEnv
public class MybatisEnvTest {

    @Autowired
    @Resource
    @Inject
    private TestMapper testMapper;

    @Test
    void testGetBean() {
        Assertions.assertNotNull(testMapper);
    }
}
```

```yaml
spring:
  datasource:
    url: jdbc:postgresql://xxx.xx.xx.xx:5432/xx?stringtype=unspecified
    username: postgres
    password: postgres
    driver-class-name: org.postgresql.Driver

```

通过在测试类上加入`@RunTest`、`@MybatisEnv`注解就可以直接构建Mybatis应用环境。他会自动的读取配置文件的数据库配置、按照当前测试类所在的目录查找编写完成的`.mapper.xml`文件。

或者也可以使用mybatis-plus环境

```java
@RunTest
@MybatisPlusEnv
public class MybatisEnvTest {

    @Autowired
    @Resource
    @Inject
    private TestMapper testMapper;

    @Test
    void testGetBean() {
        Assertions.assertNotNull(testMapper);
    }
}
```

如果需要配置指定的配置可以使用`@MybatisProperties`或者`@MybatisPlusProperties`放到测试类上

### 1.2 数据源环境

```java
@RunTest
@DataSourceEnv
public class DataSourceEnvTest {

    @Inject
    private  JdbcTemplate jdbcTemplate;

    @Test
    void test() {
        Assertions.assertNotNull(jdbcTemplate);
    }
}
```

在测试类加上`@RunTest`、`@DataSourceEnv`注解就可以构建数据环境。

如果需要指定配置可以使用`@DataSourceProperties`放到测试类上。

### 1.3 redis环境

```java
@RunTest
@RedisEnv
@RedisProperties(host = "192.168.2.29", password = "123456")
public class RedisEnvTest extends BaseTestCase {

    @Inject
    private StringRedisTemplate redisTemplate;

    @Test
    void testGetAndPut(@Parameter CoreTest coreTest) {
        assertNotNull(redisTemplate);
        redisTemplate.opsForValue().set("s", "test");
        Object r = redisTemplate.opsForValue().get("s");
        assertEquals("test", r);
    }
}
```

在测试类上加上`@RunTest`、`@RedisEnv`、`@RedisProperties`构建Redis环境



## 2.环境

通过第一章的介绍，uno-test通过细分不同组件构建测试环境为核心而设计，并且这些环境可以进行组合，如`RedisEnv` + `MybatisEnv`。目前已经支持如下环境的构建：

| 环境名        | 注解名            | 配置类名                 | 备注 |
| ------------- | ----------------- | ------------------------ | ---- |
| Redis         | `@RedisEnv`       | `@RedisProperties`       |      |
| DataSource    | `@DataSourceEnv`  | `@DataSourceProperties`  |      |
| ElasticSearch | `@EsEnv`          | `@EsProperties`          |      |
| Feign         | `@FeignEnv`       |                          |      |
| Mybatis       | `@MybatisEnv`     | `@MybatisProperties`     |      |
| MybatisPlus   | `@MybatisPlusEnv` | `@MybatisPlusProperties` |      |
| ...           |                   |                          |      |

## 3 @RunTest

`@RunTest`是uno-test的核心注解，用于注解在测试类上，基于junit-jupiter5。

```java
public @interface RunTest {

    /**
     * 配置文件名称
     *
     */
    String profile() default "uno";

    /**
     * 配置文件对应环境
     *
     */
    String active() default "test";

    /**
     * 提供spring的组件，使其注册为Spring-Bean
     *
     * @return 被spring @Component注解的class对象
     */
    Class<?>[] components() default {};

    /**
     * string类型的key-value对。如{server.port=222}。放入spring environment
     */
    String[] properties() default {};

    /**
     * 根据给定的参数判断是否创建Web应用
     */
    WebEnvironment webEnvironment() default WebEnvironment.NONE;

    /**
     * 提供在spring环境中测试不同阶段的回调
     *
     * @return Runner
     */
    Class<? extends Runner>[] runner() default {};

    /**
     * 环境构建时，提供拓展能力。
     *
     * @return Visitor
     */
    Class<? extends Visitor>[] visitor() default {};

    /**
     * 提供Test Listener
     *
     * @return TestListener
     */
    Class<? extends TestListener>[] listeners() default {};
}
```

### 3.1 profile

配置文件名称，用于