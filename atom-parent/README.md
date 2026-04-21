# Atom Parent

Atom 公共父 POM，提供统一的编译配置与插件管理，可供下游项目直接继承使用。

## 说明

`atom-parent` 继承自 `atom-dependencies`，在统一版本管理的基础上提供了编译器配置和构建插件管理。推荐下游项目直接继承此模块，开箱获得完整的构建能力。

### 提供的构建能力

- **Java 17+** 编译配置
- **Lombok** 注解处理器自动配置
- **Spring Boot Configuration Processor** 注解处理器自动配置
- **Spring Boot Maven Plugin** 预配置（排除 Lombok）
- **flatten-maven-plugin** CI 友好版本支持

## 使用方式

```xml
<parent>
  <groupId>cn.mindit</groupId>
  <artifactId>atom-parent</artifactId>
  <version>4.0.0</version>
  <relativePath/>
</parent>
```

继承后即可直接引入 Atom 模块，无需声明 version：

```xml
<dependency>
  <groupId>cn.mindit</groupId>
  <artifactId>atom-spring-boot-starter-web</artifactId>
</dependency>
```

> 💡 如果项目已有自定义 parent，请改用 [atom-dependencies](../atom-dependencies) 的 BOM import 方式。

## 开源协议

[木兰宽松许可证 v2 (MulanPSL-2.0)](https://license.coscl.org.cn/MulanPSL2)
