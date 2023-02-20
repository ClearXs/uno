# 0 简介

当前部分介绍的是uno框架核心的部分，提供了：

- 常用的工具集合
- 功能链
- 消息总线
- 任务
- 简单的模板转换替换
- 类型
- 比较器
- 转换器
- 任务
- 缓存
- ...

从上面来看，core包含的内容很多，但每一个功能点都有对应的包名对应。在实现上严格遵守Alibaba编码规范，参考很多开源框架的设计实现（如<a href="https://github.com/netty/netty">netty</a>、<a href="https://github.com/spring-projects/spring-framework">spring</a>），按照SOILD设计五大原则进行设计，二十三中设计模式提供代码的复用、抽象、可读、拓展性。在主要讲解功能实现会具体谈及实现原理。

从单元测试覆盖率上来看，目前只达到*49.5%*，也就是说其中还有一些未知的bug。计划在接下来时间内使单元覆盖率达到80%以上。

> *包结构图，基于1.1.4.RELEASE版本：*
>
> ├── annotation   --- 注解
>
> │  └── document
>
> ├── aop		  --- aop
>
> ├── bean
>
> ├── bus		  --- 消息总线
>
> │  └── event
>
> ├── cache 	   --- 缓存
>
> ├── chain		--- 功能链
>
> ├── config
>
> ├── constant
>
> ├── exception
>
> ├── function
>
> ├── metadata      --- 转换元数据
>
> │  ├── convert
>
> │  ├── endpoint
>
> │  └── mapping
>
> ├── path
>
> ├── proxy
>
> ├── reactor
>
> ├── serializer
>
> ├── spi
>
> ├── task		  --- 任务
>
> ├── tx
>
> └── util
>
>   ├── calculate   --- 计算器
>
>   ├── comparator  --- 比较器
>
>   ├── convert	 --- 转换器
>
>   ├── id
>
>   ├── template	--- 模板
>
>   └── type	    --- 类型





# 1 功能链

## 1.1 功能描述

功能链实现了一个抽象的责任链模式，任何可以实现责任链的结构均可以拓展该结构。在uno框架中，拓展了处理器链、拦截器链，一个用于消息处理、一个用于http处理。

## 1.2 类图

![uno-功能链.drawio](/Users/jiangwei/Company/automic/project/uno/docs/core.assets/uno-功能链.drawio.png)

## 1.3 getting start

实现`ChainContext`与`Node`接口

`TestChainContext.java`

```java
public class TestChainContext implements ChainContext<String> {

    private String in;

    private final Map<String, Object> attribute = Maps.newHashMap();

    public TestChainContext() {
        in = "test";
    }

    @Override
    public String getIN() {
        return in;
    }

    @Override
    public Map<String, Object> getAttribute() {
        return attribute;
    }
}
```

`TestNode1.java`

```java
@Priority(1)
public class TestNode1 implements Node<String, String> {

    @Override
    public Mono<String> execute(Chain<String, String> chain, ChainContext<String> context) throws Throwable {
        context.getAttribute().put("TestNode1", "TestNode1");
        return chain.proceed(context);
    }

}
```

`TestNode2.java`

```java
@Priority(2)
public class TestNode2 implements Node<String, String> {

    @Override
    public Mono<String> execute(Chain<String, String> chain, ChainContext<String> context) throws Throwable {
        context.getAttribute().put("TestNode2", "TestNode2");
        return chain.proceed(context);
    }
}
```

测试

```java
class DefaultChainTest extends BaseTestCase {

    /**
     * Test Case: 测试链中结点数据
     */
    @Test
    void testNodeCount() {
        DefaultChain<String, String> chain = new DefaultChain<>(Arrays.asList(new TestNode1(), new TestNode2()));
        assertEquals(2, chain.getNodes().size());
    }

    @Test
    void testNodeOrder() {
        TestNode1 node1 = new TestNode1();
        TestNode2 node2 = new TestNode2();
        DefaultChain<String, String> chain = new DefaultChain<>(Arrays.asList(node2, node1));
        List<? extends Node<String, String>> nodes = chain.getNodes();
        assertEquals(node1, nodes.get(0));
        assertEquals(node2, nodes.get(1));
    }

    @Test
    void testNodeContext() throws Throwable {
        TestNode1 node1 = new TestNode1();
        TestNode2 node2 = new TestNode2();
        DefaultChain<String, String> chain = new DefaultChain<>(Arrays.asList(node2, node1));
        TestChainContext context = new TestChainContext();
        chain.proceed(context)
                .as(StepVerifier::create)
                .verifyComplete();
    }

}
```

## 1.4 源码简说

`DefaultChain.java`

```java
public class DefaultChain<IN, OUT> implements Chain<IN, OUT> {

    /**
     * 排好序的结点集合
     */
    private final List<? extends Node<IN, OUT>> nodes;

    /**
     * 记录当前链执行到的结点的索引
     */
    private final int index;

    public DefaultChain(List<? extends Node<IN, OUT>> nodes) {
        AnnotationAwareOrderComparator.sort(nodes);
        this.nodes = nodes;
        this.index = 0;
    }

    private DefaultChain(Chain<IN, OUT> parent, int index) {
        this.nodes = parent.getNodes();
        this.index = index;
    }

    @Override
    public Mono<OUT> proceed(ChainContext<IN> context) throws Throwable {
        return Mono.defer(() -> {
                    if (index < nodes.size()) {
                        Node<IN, OUT> node = nodes.get(index);
                        DefaultChain<IN, OUT> nextChain = new DefaultChain<>(this, this.index + 1);
                        try {
                            return node.execute(nextChain, context);
                        } catch (Throwable e) {
                            return Mono.error(e);
                        }
                    } else {
                        return Mono.empty();
                    }
                })
                .onErrorContinue((err, obj) -> log.info("Chain Proceed error", err));
    }

    @Override
    public List<? extends Node<IN, OUT>> getNodes() {
        return nodes;
    }
}
```

如果熟悉*spring-cloud-gateway*的实现可以发现，这个代码的实现类似于gateway中`DefaultGatewayFilterChain`的实现。这个类包含两个成员参数`Node`的`nodes`list列表，与当前执行的索引位置`index`。当每一次执行的时候，都会调用私有的构造器，把当前的`Node`位置传递进去获取下一个执行的链，并调用`Node#execute`方法，由`Node`实现者判断是否需要再次执行链。



# 2 消息总线

## 2.1 功能描述

消息总线基于**发布-订阅**模式，是观察者模式的变体。有参考自*guava*的事件总线。虽说是消息总线，但实现上确实依托于某个`Event`来进行（算是一种不好的设计）

- 在订阅端，构建了主题路径树来存储每一个消费者，树的每一个节点都可以包含订阅者信息。订阅者也可以基于通配符订阅某一类的消息。
- 在发送端，针对某一具体的主题发送消息，也可以使用通配符。

此外考虑到消息的负载、异步。采用了响应式编程范式，来达到高性能、异步非阻塞数据流的目的。该实现中使用spring推出的*project-reactor*框架



# 3 缓存

3.1 

