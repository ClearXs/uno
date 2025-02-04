## overview

uno ('u:no, pronounced wu no) provides the ability to build large and complex applications by offering foundational
components.

- <a href="./docs/core.md">core</a>：Core library providing common utilities, event bus, simple type system, metadata
  conversion, generic chained interface declaration, simple Task, and Cache mechanism.
- <a href="./docs/data.md">data</a>：Data processing including a generic query module and ORM framework.
- <a href="./docs/rule.md">rule</a>：Rule engine.
- <a href="./docs/web.md">web</a>：Simple web toolkit providing default HTTP request endpoints for the system.
- <a href="./docs/test.md">test</a>：Testing toolkit designed for convenient use within the jupiter-test environment.
- gis：Spatial data conversion for MyBatis, Jackson to standard GeoJSON conversion, and coordinate system conversion.
- auto：Automated SPI file generation, including spring.factories.
- bom：Project dependency collection.
- plugins (not implemented): General plugin tools including microkernel mode interface declaration, loading, and
  execution of third-party plugin packages such as JARs.
- <a href="./docs/components/http.md">http</a>: Construction of OpenAPI tools and reactive HTTP request-response
  framework.
- kafka：Kafka encapsulation based on reactive backpressure.
- netty： Netty encapsulation for building RPC framework without clustering mode.
- sequential：Processing of time-series data including data reception, conversion, and processing based on reactive,
  high-performance data processing with backpressure characteristics.
- websocket：Websocket encapsulation with batch data push, publish-subscribe mode, and client-side usage of topic path
  trees.

### roadmap

- 1.1.7 version: uno-data integrate mongodb. (plan to: 2024-03 finish)
- 1.1.8 version: uno-data integrate influxdb. (plan to: 2024-04 finish)
- 1.1.9 version: uno-data integrate redis by redssion. (plan to: 2024-04 finish)
- 1.2.x version:
    1. Improve rule engine. (maybe)
    2. Add workflow engine base on flowable. (maybe)
    3. Add AI. (maybe)
- 1.3.x version:
    1. Add IoT Gateway base on netty. realize TCP, UDP, MQTT, CoAP, HTTP...
    2. Add Plugin Modules. I guess it should be dynamically load JAR packages and execute dynamic language. like as groovy javascript...