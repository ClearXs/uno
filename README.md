

# 概述

uno（'u:no，读wu no）提供基础的组件，来构建大型复杂应用的能力，截止于2023-07-06目前包含如下组件库：

- <a href="./core.html">core</a>：核心库，提供通用的工具、事件总线、简易的类型系统、元数据转换，通用链式接口声明、简易的Task、Cache机制。
- <a href="./data.html">data</a>：数据处理，包含通用查询模块、orm框架。
- <a href="rule.html">rule</a>：规则引擎。
- <a href="./web.html">web</a>：简易的web工具集，提供系统默认的HTTP请求端点。
- <a href="./test.html">test</a>：基于spring环境方便使用的测试工具集。
- gis：构建mybatis的空间数据转换、jackson -> 标准geojson的转换、坐标系转换。
- auto：自动生成spi文件、还包括spring.factories。
- bom：项目包依赖集。
- plugins（未实现）：通用的插件工具，包含微内核模式接口声明、jar包等第三方插件包加载与执行。
- components: 组件集合。
    - <a href="./components/http.html">http</a>: 构建openapi工具，响应式的http请求响应框架。
    - kafka：基于响应式背压封装的kafka。
    - media：多媒体组件，基于命令模式提供通用api。
    - netty：封装netty，构建无集群模式的RPC框架。
    - sequential：时序数据的处理，包含数据接收、转换、处理，基于响应式，背压特性高性能的数据处理。
    - websocket：封装websocket，具有数据批推，发布订阅模式，客户端可以使用Topic的路径树化。

- starter：为各个组件编写spring-starter包。



