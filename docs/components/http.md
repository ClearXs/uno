## 1.http

uno-component-http构建了一套类似于OkHttp链式反应请求的组件。它基于`spring-webflux`全链路响应式，提供高性能的HTTP请求，可以类似如下方式来进行请求：

```java
String url = "/user/{id}/{name}";
User user = new User();
user.setId(1);
user.setName("jw");
prepareResponse(res ->
        res.addHeader("Content-Type", "application/json")
                .setBody(JsonUtils.toJson(user)));
StepVerifier.create(
                HttpSwapper
                        .build(url, HttpMethod.GET)
                        .addParameter("id", String.valueOf(1))
                        .addParameter("name", "jw")
                        .setWebClient(buildWebClient())
                        .swap()
                        .flatMap(res -> res.toExpect(User.class)))
        .expectNext(user)
        .verifyComplete();
```

### 1.1 HttpSwapper

`HttpSwapper`是构建请求的入口，它提供链式编程模式，能够指定请求方式，请求参数，以及请求头的构建。

```java
HttpSwapper.build(url, HttpMethod.GET)
```

如果是`GET`请求，通过使用`HttpSwapper.addParameter(String name, String value)`为 **url**添加请求参数，如：

```java
String url = "/user/{id}/{name}";
HttpSwapper.build(url, HttpMethod.GET).addParameter("id", String.valueOf(1)).addParameter("name", "jw")
```

则该**url**会解析成`/user/1/jw`



`HttpSwapper`默认会添加`Content-Type=application/json`，也可以自定义，如下：

```java
String url = "http://localhost:8080/test/timeout";
HttpSwapper.build(url).addHeader("Content-Type", "text/plain");
```



`HttpSwapper`允许`multipart/form-data`、`application/x-www-form-urlencoded`、`application/json`。

```java
String url = "http://localhost:8080/test/timeout";
Map<String, Object> var = Maps.newHashMap();
var.put("t1", "t1");
HttpSwapper.build(url, HttpMethod.POST).setMediaType(MediaType.APPLICATION_FORM_URLENCODED);
```

### 1.2 构建GET请求

```java
String url = "/user/{id}/{name}";
User user = new User();
user.setId(1);
user.setName("jw");
prepareResponse(res ->
        res.addHeader("Content-Type", "application/json")
                .setBody(JsonUtils.toJson(user)));
StepVerifier.create(
                HttpSwapper
                        .build(url, HttpMethod.GET)
                        .addParameter("id", String.valueOf(1))
                        .addParameter("name", "jw")
                        .setWebClient(buildWebClient())
                        .swap()
                        .flatMap(res -> res.toExpect(User.class)))
        .expectNext(user)
        .verifyComplete();
```

### 1.3 构建POST请求

```java
String url = "/user/add";
User user = new User();
user.setId(1);
user.setName("");
prepareResponse(res ->
        res.addHeader("Content-Type", "application/json")
                .setBody(JsonUtils.toJson(user)));
StepVerifier.create(
                HttpSwapper
                        .build(url)
                        .addBody(user)
                        .setWebClient(buildWebClient())
                        .swap()
                        .flatMap(res -> res.toExpect(User.class)))
        .expectNext(user)
        .verifyComplete();
```

### 1.4 拦截器

拦截器（Interceptor）是链式请求中常用的方法。同样在uno-component-http中，也通过了这样的方式。它使用了uno-core的功能链作为实现，其API定义为：

```java
public interface Interceptor extends Node<HttpRequestMetadata, HttpResponseMetadata> {
}
```

默认提供了两个实现：

- `ExchangeInterceptor`：用于HTTP请求核心请求，实际做数据交换
- `TokenInterceptor`：用于做Token验证，并自动拼接`Token`



也可以实现自定义的：

```jade
@AutoService(Interceptor.class)
public class CustomInterceptor implements Interceptor {
    @Override
    public Mono<HttpResponseMetadata> execute(Chain<HttpRequestMetadata, HttpResponseMetadata> chain, ChainContext<HttpRequestMetadata> context) throws Throwable {
        return null;
    }
}
```

`@AutoService`的使用参考于`uno-auto`

或者：

```java
String url = "http://localhost:8080/test/timeout";
HttpSwapper.build(url).addInterceptor((chain, context) -> null);
```

### 1.5 HttpResponseMetadata

`HttpResponseMetadata`是响应结果，可以通过，把响应结果转换为实体类型

```
@Data
static class User {
  long id;
  String name;
}
HttpResponseMetadata.toExpect(User.class);
// ...
HttpResponseMetadata.expectString();
```

## 2.OpenAPI

通过解析open v2、open v3解析成对应的`HttpSwapper`



