<h1 align="center">Atom</h1>

<p align="center">
	<a target="_blank" href="https://central.sonatype.com/artifact/net.wenzuo/atom">
        <img alt="Atom" src="https://img.shields.io/maven-central/v/net.wenzuo/atom?label=Atom">
	</a>
	<a target="_blank" href="https://www.oracle.com/technetwork/java/javase/downloads/index.html">
		<img alt="Jdk" src="https://img.shields.io/badge/Jdk-17+-blue.svg" />
	</a>
	<a target="_blank" href="https://central.sonatype.com/artifact/org.springframework.boot/spring-boot">
        <img alt="SpringBoot" src="https://img.shields.io/badge/SpringBoot-3.0+-73b839.svg?logo=springboot" />
	</a>
</p>

Atom 是一个基于 SpringBoot 和 SpringCloud 的企业级常用组件封装库，提供了丰富的功能和强大的扩展性，可以帮助开发者快速构建高效、稳定的应用，简单实用，开箱即用！

## 版本

此分支基于 JDK17+，SpringBoot 3.x，SpringCloud 2022.x，SpringCloud Alibaba 2022.x开发。

各分支版本号对应关系如下:

| Atom | JDK | SpringBoot | SpringCloud | SpringCloud Alibaba |
|------|-----|------------|-------------|---------------------|
| 2.0+ | 17+ | 3.0+       | 2022.x      | 2022.x              |
| 1.0+ | 8+  | 2.7+       | 2021.x      | 2021.x              |

## 模块

- [atom-spring-boot-starter-core](atom-spring-boot-starter-core) 核心模块
- [atom-spring-boot-starter-doc](atom-spring-boot-starter-doc) API 文档模块
- [atom-spring-boot-starter-jwt](atom-spring-boot-starter-jwt) JWT 模块
- [atom-spring-boot-starter-mqtt](atom-spring-boot-starter-mqtt) MQTT 模块
- [atom-spring-boot-starter-mybatis-plus](atom-spring-boot-starter-mybatis-plus) Mybatis Plus 模块
- [atom-spring-boot-starter-redis](atom-spring-boot-starter-redis) Redis 模块
- [atom-spring-boot-starter-web](atom-spring-boot-starter-web) Web 模块
- [atom-spring-cloud-starter-consul](atom-spring-cloud-starter-consul) Consul 模块
- [atom-spring-cloud-starter-feign](atom-spring-cloud-starter-feign) Feign 模块
- [atom-spring-cloud-starter-kafka](atom-spring-cloud-starter-kafka) Kafka 模块
- [atom-spring-cloud-starter-nacos-config](atom-spring-cloud-starter-nacos-config) Nacos Config 模块
- [atom-spring-cloud-starter-nacos-discovery](atom-spring-cloud-starter-nacos-discovery) Nacos Discovery 模块

## 快速开始

在 pom.xml 中指定 parent

```xml
<!-- 指定parent -->
<parent>
	<groupId>net.wenzuo</groupId>
	<artifactId>atom</artifactId>
	<version>2.4.9</version>
	<relativePath/>
</parent>
```

根据需要的模块引入相关依赖

```xml
<!-- 按需引入相关模块 -->
<dependency>
	<groupId>net.wenzuo</groupId>
	<artifactId>modules...</artifactId>
</dependency>
```

## 配置项

各个模块的配置项，可以在 application.yml 中覆盖

```yaml
atom:
  core:
    enabled: true # 是否启用core模块
    async: true # 是否启用异步处理
    json: true # 是否启用jackson处理
  doc:
    enabled: true # 是否启用doc模块
  jwt:
    enabled: true # 是否启用jwt模块
    secret: # JWT密钥, 可通过 net.wenzuo.atom.jwt.GenerateKey 生成随机密钥
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
    enabled: true # 是否启用mybatis-plus模块
    pagination: true # 是否启用mybatis-plus分页插件
    auto-fill: false # 是否启用创建时间,更新时间自动填充
    create-time-field: createTime # 创建时间字段名,此处为entity的属性名,非数据库字段名
    update-time-field: updateTime # 更新时间字段名,此处为entity的属性名,非数据库字段名
  opc:
    da:
      enabled: true # 是否启用opc-da模块
      instances: # OPC DA 实例配置, 可以有多个
        - id: opcda1 # 实例 ID
          enabled: true # 是否启用
          host: 127.0.0.1 # 实例主机
          domain: # 实例域
          user: opc # 实例用户
          password: opc123
          prog-id: # 实例 ProgID
          cls-id: # 实例 ClsID
  redis:
    enabled: true # 是否启用redis模块
    redis-template: true # 是否启用redisTemplate,启用后将自动配置RedisTemplate<String, Object>, 使用jackson序列化value
    cache-manager: true # 是否启用cacheManager,启用后将自动配置CacheManager, 使用jackson序列化value
  web:
    enabled: true # 是否启用web模块
    exception-handler: true # 是否启用异常拦截
    cors:
      enabled: true # 是否启用CORS
      configs: # CORS配置, 可配置多个
        - pattern: /**
          allow-credentials: true
          allowed-origins:
            - *
          allowed-origin-patterns:
          allowed-headers:
            - *
          allowed-methods:
            - *
          exposed-headers:
            - *
    logging:
      enabled: true # 是否启用请求响应日志记录
      include-path: # 包含路径
        - /**
      exclude-path: # 排除路径
  consul:
    enabled: true # 是否启用Consul模块
  feign:
    enabled: true # 是否启用Feign模块
    logging: true # 是否启用Feign的请求响应日志记录
    exception-handler: true # 是否启用Feign的异常处理,拦截第三方响应结果异常
    decode: true # 是否启用Feign的解码器, 解码响应结果,针对小于 400 的状态码抛出异常
  kafka:
    enabled: true # 是否启用Kafka模块
```

## 一些建议

- 生产环境中在网关侧如果配置了CORS,建议关闭服务的CORS配置
- 建议在生产环境关闭 doc 及其相关配置

```yaml
knife4j:
  production: true
springdoc:
  api-docs:
    enabled: false
  swagger-ui:
    enabled: false
```