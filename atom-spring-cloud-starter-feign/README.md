# Atom Spring Cloud Starter Feign

Atom Feign 声明式 HTTP 客户端模块，提供请求日志、异常处理、响应解码和负载均衡。

## 功能特性

- **声明式 HTTP 客户端** — 基于 OpenFeign，通过接口定义即可调用远程服务
- **请求日志** — 自动记录 Feign 请求和响应日志
- **异常处理** — 自动将 Feign 调用异常转换为 `ThirdException`
- **响应解码** — 自定义解码器，小于 400 的异常状态码自动抛出异常
- **负载均衡** — 集成 Spring Cloud LoadBalancer + Caffeine 缓存

## 快速开始

### 添加依赖

```xml
<dependency>
  <groupId>cn.mindit</groupId>
  <artifactId>atom-spring-cloud-starter-feign</artifactId>
</dependency>
```

### 配置

```yaml
atom:
  feign:
    enabled: true
    logging: true             # 启用请求响应日志
    exception-handler: true   # 启用异常处理
    decode: true              # 启用自定义解码器
```

### 使用示例

#### 定义 Feign 客户端

```java
@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping("/users/{id}")
    Result<UserVO> getUser(@PathVariable Long id);

    @PostMapping("/users")
    Result<IdVO> createUser(@RequestBody UserDTO dto);
}
```

#### 调用远程服务

```java
@RequiredArgsConstructor
@Service
public class OrderService {

    private final UserClient userClient;

    public UserVO getOrderUser(Long userId) {
        Result<UserVO> result = userClient.getUser(userId);
        return result.getData();
    }
}
```

启用日志和异常处理后，Feign 调用异常会自动转换为 `ThirdException`，并被 Web 模块的全局异常处理器捕获。

## 配置项

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `atom.feign.enabled` | Boolean | `true` | 是否启用 Feign 模块 |
| `atom.feign.logging` | Boolean | `true` | 是否启用请求响应日志 |
| `atom.feign.exception-handler` | Boolean | `true` | 是否启用异常处理 |
| `atom.feign.decode` | Boolean | `true` | 是否启用自定义解码器 |

## 开源协议

[木兰宽松许可证 v2 (MulanPSL-2.0)](https://license.coscl.org.cn/MulanPSL2)
