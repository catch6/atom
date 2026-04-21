# Atom Spring Cloud Starter Nacos Config

Atom Nacos 配置中心模块，提供分布式配置管理，支持配置动态刷新。

## 功能特性

- **分布式配置** — 基于 Nacos 统一管理应用配置
- **动态刷新** — 配置变更自动推送，无需重启应用
- **多环境支持** — 通过 namespace 和 group 管理不同环境配置
- **日志优化** — 默认降低 Nacos 客户端日志级别，减少干扰

## 快速开始

### 添加依赖

```xml
<dependency>
  <groupId>cn.mindit</groupId>
  <artifactId>atom-spring-cloud-starter-nacos-config</artifactId>
</dependency>
```

### 配置

> ⚠️ Nacos Config 配置需写在 `bootstrap.yml` 中。

```yaml
# bootstrap.yml
spring:
  application:
    name: my-service
  cloud:
    nacos:
      config:
        server-addr: localhost:8848
        namespace:                    # 命名空间 ID
        group: DEFAULT_GROUP
        file-extension: yml           # 配置文件格式
```

### 使用示例

在 Nacos 控制台创建配置后，应用会自动加载。使用 `@Value` 或 `@ConfigurationProperties` 读取配置：

```java
@RefreshScope
@RestController
public class ConfigController {

    @Value("${app.feature.enabled:false}")
    private Boolean featureEnabled;

    @GetMapping("/config")
    public Boolean getFeatureEnabled() {
        return featureEnabled;
    }
}
```

添加 `@RefreshScope` 后，配置变更时会自动刷新 Bean。

## 开源协议

[木兰宽松许可证 v2 (MulanPSL-2.0)](https://license.coscl.org.cn/MulanPSL2)
