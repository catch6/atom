# Atom Spring Cloud Starter Nacos Discovery

Atom Nacos 服务发现模块，提供服务注册与发现功能，支持动态服务发现和健康检查。

## 功能特性

- **服务注册** — 应用启动时自动注册到 Nacos
- **服务发现** — 动态发现其他服务实例，支持负载均衡
- **健康检查** — 自动上报心跳，维护服务健康状态
- **日志优化** — 默认降低 Nacos 客户端日志级别，减少干扰

## 快速开始

### 添加依赖

```xml
<dependency>
  <groupId>cn.mindit</groupId>
  <artifactId>atom-spring-cloud-starter-nacos-discovery</artifactId>
</dependency>
```

### 配置

```yaml
spring:
  application:
    name: my-service
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
        namespace:                    # 命名空间 ID
        group: DEFAULT_GROUP
```

### 使用示例

配置完成后，服务会自动注册到 Nacos。可通过 `DiscoveryClient` 或 `LoadBalancerClient` 发现其他服务：

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

配合 Feign 或 RestTemplate 使用时，会自动基于 Nacos 进行服务发现和负载均衡。

## 开源协议

[木兰宽松许可证 v2 (MulanPSL-2.0)](https://license.coscl.org.cn/MulanPSL2)
