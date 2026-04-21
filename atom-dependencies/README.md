# Atom Dependencies

Atom 依赖 BOM（Bill of Materials），统一管理所有 Atom 模块及常用第三方库的版本，消除依赖冲突。

## 说明

`atom-dependencies` 继承 `spring-boot-starter-parent`，在 Spring Boot 依赖管理的基础上统一声明了 Atom 各模块和第三方库的版本。适用于已有自定义 parent 的项目，通过 BOM import 方式引入。

### 管理的核心版本

| 依赖 | 版本 |
|------|------|
| Spring Boot | 4.0.5 |
| Spring Cloud | 2025.1.0 |
| Spring Cloud Alibaba | 2025.1.0.0 |
| MyBatis Plus | 3.5.16 |
| Hutool | 5.8.44 |
| Nimbus JOSE JWT | 10.9 |
| Eclipse Milo (OPC UA) | 0.6.16 |
| FastExcel | 1.3.0 |
| ShedLock | 7.7.0 |
| SpringDoc OpenAPI | 3.0.3 |

## 使用方式

在 `dependencyManagement` 中 import：

```xml
<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>cn.mindit</groupId>
      <artifactId>atom-dependencies</artifactId>
      <version>4.0.0</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
  </dependencies>
</dependencyManagement>
```

之后引入 Atom 模块无需声明 version：

```xml
<dependency>
  <groupId>cn.mindit</groupId>
  <artifactId>atom-spring-boot-starter-web</artifactId>
</dependency>
```

## 开源协议

[木兰宽松许可证 v2 (MulanPSL-2.0)](https://license.coscl.org.cn/MulanPSL2)
