## overview

uno ('u:no, pronounced wu no) provides the ability to build large and complex applications by offering foundational
components.

- <a href="./docs/core.md">core</a>：Core library providing common utilities, event bus, simple type system, metadata conversion, generic chained interface declaration, simple Task, and Cache mechanism.
- <a href="./docs/data.md">data</a>：Data processing including a generic query module and ORM framework.
- <a href="./docs/rule.md">rule</a>：Rule engine.
- <a href="./docs/web.md">web</a>：Simple web toolkit providing default HTTP request endpoints for the system.
- <a href="./docs/test.md">test</a>：Testing toolkit designed for convenient use within the jupiter-test environment.
- gis：Spatial data conversion for MyBatis, Jackson to standard GeoJSON conversion, and coordinate system conversion.
- auto：Automated SPI file generation, including spring.factories.
- bom：Project dependency collection.
- plugins (not implemented): General plugin tools including microkernel mode interface declaration, loading, and execution of third-party plugin packages such as JARs.
- <a href="./docs/components/http.md">http</a>: Construction of OpenAPI tools and reactive HTTP request-response framework.
- kafka：Kafka encapsulation based on reactive backpressure.
- netty： Netty encapsulation for building RPC framework without clustering mode.
- sequential：Processing of time-series data including data reception, conversion, and processing based on reactive, high-performance data processing with backpressure characteristics.
- websocket：Websocket encapsulation with batch data push, publish-subscribe mode, and client-side usage of topic path trees.