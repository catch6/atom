<h1 align="center">⚛️ Atom</h1>

<p align="center">
  <strong>企业级 Spring Boot / Spring Cloud 组件库，简单实用，开箱即用</strong>
</p>

<p align="center">
	<a target="_blank" href="https://central.sonatype.com/artifact/cn.mindit/atom">
        <img alt="Atom" src="https://img.shields.io/maven-central/v/cn.mindit/atom?label=Atom">
	</a>
	<a target="_blank" href="https://www.oracle.com/technetwork/java/javase/downloads/index.html">
		<img alt="Jdk" src="https://img.shields.io/badge/Jdk-17+-blue.svg" />
	</a>
	<a target="_blank" href="https://central.sonatype.com/artifact/org.springframework.boot/spring-boot">
        <img alt="SpringBoot" src="https://img.shields.io/badge/SpringBoot-4.0+-73b839.svg?logo=springboot" />
	</a>
</p>

---

## ✨ 为什么选择 Atom？

- 🚀 **开箱即用** — 引入依赖即生效，零配置启动，告别重复造轮子
- 🧩 **模块化设计** — 按需引入，不引入不加载，不臃肿
- 🏭 **工业协议支持** — 内置 OPC DA / OPC UA / MQTT，工业物联网场景开箱可用
- 🔧 **统一规范** — 统一响应格式、异常体系、日志记录，团队协作更一致
- 📦 **版本无忧** — BOM 统一管理依赖版本，告别依赖冲突
- ☁️ **云原生就绪** — 无缝集成 Nacos、Consul、Feign、Kafka 等微服务组件

---

## 📋 版本

| Atom | JDK | Spring Boot | Spring Cloud | Spring Cloud Alibaba |
|------|-----|-------------|--------------|----------------------|
| 4.0+ | 17+ | 4.0+        | 2025.1.0     | 2025.1.0.0           |
| 3.5+ | 17+ | 3.5+        | 2025.0.0     | 2025.0.0.0           |
| 3.0+ | 17+ | 3.0+        | 2022.x       | 2022.x               |
| 1.0+ | 8+  | 2.7+        | 2021.x       | 2021.x               |

---

## 🧩 模块

### Spring Boot Starters

| 模块 | 说明 | 依赖 |
|------|------|------|
| [atom-spring-boot-starter-core](atom-spring-boot-starter-core) | 🔋 核心模块（异步、JSON、工具类） | - |
| [atom-spring-boot-starter-api](atom-spring-boot-starter-api) | 📐 API 模块（校验、OpenAPI、统一响应） | core |
| [atom-spring-boot-starter-web](atom-spring-boot-starter-web) | 🌐 Web 模块（AOP、异常处理、CORS、日志、Excel 导出） | core, api |
| [atom-spring-boot-starter-doc](atom-spring-boot-starter-doc) | 📖 API 文档模块 | core |
| [atom-spring-boot-starter-jwt](atom-spring-boot-starter-jwt) | 🔑 JWT 模块 | core |
| [atom-spring-boot-starter-mqtt](atom-spring-boot-starter-mqtt) | 📡 MQTT 模块（多实例、异步分发） | core |
| [atom-spring-boot-starter-mybatis-plus](atom-spring-boot-starter-mybatis-plus) | 🗃️ MyBatis Plus 模块（分页、自动填充） | core |
| [atom-spring-boot-starter-opc-da](atom-spring-boot-starter-opc-da) | 🏭 OPC DA 模块（经典工业协议） | core |
| [atom-spring-boot-starter-opc-ua](atom-spring-boot-starter-opc-ua) | 🏗️ OPC UA 模块（现代工业协议） | core |
| [atom-spring-boot-starter-redis](atom-spring-boot-starter-redis) | ⚡ Redis 模块 | core |
| [atom-spring-boot-starter-scheduling](atom-spring-boot-starter-scheduling) | ⏰ 定时任务模块（Spring Scheduling 增强） | core |
| [atom-spring-boot-starter-scheduling-shedlock](atom-spring-boot-starter-scheduling-shedlock) | 🔒 分布式调度锁模块（基于 ShedLock，防止集群重复执行） | core |
| [atom-spring-boot-starter-websocket](atom-spring-boot-starter-websocket) | 🔌 WebSocket 客户端模块（多实例、自动重连） | core |

### Spring Cloud Starters

| 模块 | 说明 | 依赖 |
|------|------|------|
| [atom-spring-cloud-starter-consul](atom-spring-cloud-starter-consul) | 🏥 Consul 模块 | core |
| [atom-spring-cloud-starter-feign](atom-spring-cloud-starter-feign) | 🪶 Feign 模块（日志、异常处理、解码） | core |
| [atom-spring-cloud-starter-kafka](atom-spring-cloud-starter-kafka) | 📨 Kafka 模块 | core |
| [atom-spring-cloud-starter-nacos-config](atom-spring-cloud-starter-nacos-config) | ⚙️ Nacos Config 模块 | core |
| [atom-spring-cloud-starter-nacos-discovery](atom-spring-cloud-starter-nacos-discovery) | 🔍 Nacos Discovery 模块 | core |

> 💡 **web 模块说明**：`atom-spring-boot-starter-web` 内部依赖 `atom-spring-boot-starter-api`，引入 web 即自动获得参数校验、统一响应等能力，无需重复引入。
>
> 💡 **scheduling 模块说明**：`scheduling` 提供 Spring Scheduling 增强配置；`scheduling-shedlock` 基于 ShedLock 提供分布式调度锁，适用于集群环境防止任务重复执行，两者可独立使用。

---

## 🚀 快速开始

提供两种接入方式，根据项目需要任选其一。

### 方式一：继承 atom-parent（推荐）

开箱即用，自动获得编译器配置、注解处理器、插件管理等构建能力。

```xml
<parent>
  <groupId>cn.mindit</groupId>
  <artifactId>atom-parent</artifactId>
  <version>4.0.2</version>
  <relativePath/>
</parent>
```

### 方式二：BOM 导入

适用于已有自定义 parent 或使用 `spring-boot-starter-parent` 的项目，不占用 parent 槽位。

```xml
<!-- 在 dependencyManagement 中 import atom-dependencies -->
<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>cn.mindit</groupId>
      <artifactId>atom-dependencies</artifactId>
      <version>4.0.2</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
  </dependencies>
</dependencyManagement>
```

### 引入模块

两种接入方式引入模块的写法一致，无需声明 version：

```xml
<dependency>
  <groupId>cn.mindit</groupId>
  <artifactId>atom-spring-boot-starter-web</artifactId>
</dependency>
```

---

## ⚙️ 配置项

各模块配置项均可在 `application.yml` 中覆盖，以下为完整参考：

```yaml
atom:
  core:
    enabled: true # 是否启用 core 模块
    async: true # 是否启用异步处理
    json: true # 是否启用 jackson 处理
  doc:
    enabled: true # 是否启用 doc 模块
  jwt:
    enabled: true # 是否启用 jwt 模块
    secret: # JWT 密钥, 可通过 cn.mindit.atom.jwt.GenerateKey 生成随机密钥
  mqtt:
    enabled: true # 是否启用 MQTT 模块
    id: default # 实例 ID
    url: tcp://broker.emqx.io:1883 # 服务器地址
    username: # 服务器用户名
    password: # 服务器密码
    client-id: # 客户端 ID
    instances: # MQTT 多实例配置
      - id: emqx1 # 实例 ID
        enabled: true # 是否启用
        url: tcp://broker.emqx.io:1883 # 服务器地址
        username: # 服务器用户名
        password: # 服务器密码
        client-id: # 客户端 ID
  mybatis-plus:
    enabled: true # 是否启用 mybatis-plus 模块
    pagination: true # 是否启用分页插件
    auto-fill: false # 是否启用创建时间、更新时间自动填充
    create-time-field: createTime # 创建时间字段名（entity 属性名）
    update-time-field: updateTime # 更新时间字段名（entity 属性名）
  opc:
    da:
      enabled: true # 是否启用 OPC DA 模块
      id: default # 实例 ID
      host: # 实例主机
      domain: # 实例域
      user: # 实例用户
      password: # 实例密码
      prog-id: # 实例 ProgID
      cls-id: # 实例 ClsID
      period: 1000 # 刷新间隔（ms）
      async: true # 是否异步执行
      initialRefresh: false # 初始化获取全量数据, 仅在 async 为 true 时有效
      instances: # OPC DA 多实例配置
        - id: opcda1
          host: 127.0.0.1
          domain:
          user: opc
          password: opc123
          prog-id:
          cls-id:
          period: 1000
          async: true
          initialRefresh: false
    ua:
      enabled: true # 是否启用 OPC UA 模块
      id: default # 实例 ID
      url: # 服务器地址, 如: opc.tcp://milo.digitalpetri.com:62541/milo
      username: # 服务器用户名
      password: # 服务器密码
      certificate-path: # 证书路径
      instances: # OPC UA 多实例配置
        - id: opcua1
          enabled: true
          url:
          username:
          password:
  websocket:
    enabled: true # 是否启用 WebSocket 模块
    id: default # 实例 ID
    url: # 目标 URL, 如: ws://localhost:8080/websocket
    auto-reconnect: true # 是否自动重连
    reconnect-strategy: FIXED_DELAY # 重连策略: FIXED_DELAY / EXPONENTIAL_BACKOFF
    reconnect-delay: 5s # 重连延迟
    max-reconnect-attempts: 10 # 最大重试次数
    heartbeat-interval: 10s # 心跳间隔
    instances: # WebSocket 多实例配置
      - id: ws1
        enabled: true
        url:
        auto-reconnect: true
        reconnect-strategy: FIXED_DELAY
        reconnect-delay: 5s
        max-reconnect-attempts: 10
        heartbeat-interval: 30s
  scheduling:
    enabled: true # 是否启用 Scheduling 模块
    shedlock:
      enabled: true # 是否启用 ShedLock 分布式调度锁
  redis:
    enabled: true # 是否启用 redis 模块
    redis-template: true # 启用 RedisTemplate<String, Object>，jackson 序列化
    cache-manager: true # 启用 CacheManager，jackson 序列化
  web:
    enabled: true # 是否启用 web 模块
    exception-handler: true # 是否启用异常拦截
    cors:
      enabled: true # 是否启用 CORS
      configs: # CORS 配置, 可配置多个
        - pattern: /**
          allow-credentials: true
          allowed-origins:
            - "*"
          allowed-origin-patterns:
          allowed-headers:
            - "*"
          allowed-methods:
            - "*"
          exposed-headers:
            - "*"
    logging:
      enabled: true # 是否启用请求响应日志记录
      include-path: # 包含路径
        - /**
      exclude-path: # 排除路径
  consul:
    enabled: true # 是否启用 Consul 模块
  feign:
    enabled: true # 是否启用 Feign 模块
    logging: true # 是否启用请求响应日志
    exception-handler: true # 是否启用异常处理
    decode: true # 是否启用解码器（小于 400 的异常状态码抛出异常）
  kafka:
    enabled: true # 是否启用 Kafka 模块
```

---

## 💡 最佳实践

- 🌐 生产环境在网关侧已配置 CORS 时，建议关闭服务的 CORS 配置
- 📖 生产环境建议关闭 API 文档：

```yaml
springdoc:
  api-docs:
    enabled: false
  swagger-ui:
    enabled: false
```

---

## 📄 开源协议

[木兰宽松许可证 v2 (MulanPSL-2.0)](https://license.coscl.org.cn/MulanPSL2)
