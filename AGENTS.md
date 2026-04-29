# CLAUDE.md

## Build Commands

```bash
# Build all modules (skip tests)
mvn clean install -DskipTests

# Build a single module
mvn clean install -DskipTests -pl atom-spring-boot-starter-core

# Run tests for a specific module
mvn test -pl atom-spring-boot-starter-mqtt

# Run a single test class
mvn test -pl atom-spring-boot-starter-core -Dtest=NanoIdUtilsTest

# Release build (signs, generates javadoc/source, publishes to Maven Central)
mvn clean deploy -P release
```

## Architecture

Atom 是一个企业级 Spring Boot/Spring Cloud Starter 组件库框架，基于 Spring Boot 4.0.6 + Spring Cloud 2025.1.0 + JDK 17+。

### 模块层次

```
atom-build          ← 发布配置（GPG签名、Maven Central发布）
  ↑
atom-parent         ← 下游项目父POM（编译器、插件管理）
  ↑
atom-dependencies   ← BOM（统一依赖版本管理，继承 spring-boot-starter-parent）
  ↑
各功能模块
```

### 模块依赖关系

- `atom-spring-boot-starter-core` — 基础模块，所有其他模块依赖它
- `atom-spring-boot-starter-api` — 依赖 core，提供校验、OpenAPI、统一响应
- `atom-spring-boot-starter-web` — 依赖 core + api，提供 Web/MVC 特性（AOP、异常处理、Excel导出）
- Cloud 模块（consul、feign、kafka、nacos-*）— 依赖 core，彼此独立
- 专业模块（mqtt、redis、mybatis-plus、opc-da/ua）— 依赖 core

### 版本管理

使用 `${revision}` 属性 + flatten-maven-plugin 实现 CI 友好版本。修改版本只需改根 pom.xml 中的 `<revision>` 属性。

## Code Conventions

- **包路径**: `cn.mindit.atom.{module}`，子包通常为 `core`、`util`、`config`、`param`、`validator`
- **测试包路径**: `cn.mindit.atom.test.{module}`
- **API 模型**: DTO（入参）/ VO（出参）模式，统一 `Result<T>` 响应包装
- **自动配置**: 使用 `@AutoConfiguration` + `@ConditionalOnProperty`，注册在 `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`
- **配置前缀**: `atom.*`（如 `atom.jwt.*`、`atom.mqtt.*`）
- **Lombok**: 广泛使用 `@Data`、`@AllArgsConstructor`、`@NoArgsConstructor`
- **异常体系**: `BusinessException`（业务异常）、`ServiceException`（服务异常）、`ThirdException`（第三方异常）
- **License**: 木兰宽松许可证 v2（MulanPSL-2.0），Copyright 2022-2026，文件不添加 Copyright 头注释

## Key Dependencies

- Spring Boot 4.0.6 / Spring Cloud 2025.1.0 / Spring Cloud Alibaba 2025.1.0.0
- MyBatis Plus 3.5.16 / Hutool 5.8.44 / MapStruct 1.6.3
- Nimbus JOSE JWT 10.9 / SpringDoc OpenAPI 3.0.3
- Eclipse Paho 1.2.5 (MQTT) / Eclipse Milo 0.6.16 (OPC UA)
- FastExcel 1.3.0 / ShedLock 7.7.0
