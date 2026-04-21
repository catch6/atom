# Atom Spring Cloud Starter Consul

Atom Consul 服务发现模块，集成 Spring Cloud Consul 提供服务注册与发现功能。

## 功能特性

- **服务注册** — 应用启动时自动注册到 Consul
- **服务发现** — 通过 Consul 动态发现其他服务实例
- **健康检查** — 集成 Actuator 健康检查端点
- **开箱即用** — 添加依赖并配置 Consul 地址即可使用

## 快速开始

### 添加依赖

```xml
<dependency>
  <groupId>cn.mindit</groupId>
  <artifactId>atom-spring-cloud-starter-consul</artifactId>
</dependency>
```

### 配置

```yaml
atom:
  consul:
    enabled: true

spring:
  cloud:
    consul:
      host: localhost
      port: 8500
      discovery:
        service-name: ${spring.application.name}
        health-check-path: /actuator/health
        health-check-interval: 10s
```

### 使用示例

配置完成后，服务会自动注册到 Consul。可通过 `DiscoveryClient` 发现其他服务：

```java
@RequiredArgsConstructor
@Service
public class ServiceDiscovery {

    private final DiscoveryClient discoveryClient;

    public List<ServiceInstance> getInstances(String serviceName) {
        return discoveryClient.getInstances(serviceName);
    }
}
```

## 配置项

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `atom.consul.enabled` | Boolean | `true` | 是否启用 Consul 模块 |

> 更多 Consul 配置项请参考 [Spring Cloud Consul 文档](https://docs.spring.io/spring-cloud-consul/reference/)。

## 开源协议

[木兰宽松许可证 v2 (MulanPSL-2.0)](https://license.coscl.org.cn/MulanPSL2)
