# Atom Spring Boot Starter OPC UA

Atom OPC UA 工业协议模块，基于 Eclipse Milo 提供 OPC UA 服务器连接和数据读写，支持多实例和注解式订阅。

## 功能特性

- **多实例连接** — 支持同时连接多个 OPC UA 服务器
- **注解式订阅** — 通过 `@OpcUaListener` / `@OpcUaConsumer` 注解声明式监听数据变化
- **数据读写** — 通过 `OpcUaService` 读取和写入节点数据，支持超时控制
- **证书认证** — 支持用户名密码和证书两种认证方式
- **自动配置** — 基于 Spring Boot AutoConfiguration，添加依赖即生效

## 快速开始

### 添加依赖

```xml
<dependency>
  <groupId>cn.mindit</groupId>
  <artifactId>atom-spring-boot-starter-opc-ua</artifactId>
</dependency>
```

### 配置

```yaml
atom:
  opc:
    ua:
      enabled: true
      id: default
      url:                    # 服务器地址，如 opc.tcp://localhost:62541/milo
      username:               # 用户名
      password:               # 密码
      certificate-path:       # 证书路径
      instances:              # 多实例配置
        - id: opcua1
          enabled: true
          url: opc.tcp://milo.digitalpetri.com:62541/milo
          username:
          password:
```

### 使用示例

#### 订阅数据变化

```java
@Component
public class OpcUaDataHandler {

    @OpcUaListener(id = "opcua1")
    public void onDataChange(String nodeId, Object value) {
        log.info("节点: {}, 值: {}", nodeId, value);
    }
}
```

#### 读写节点数据

```java
@RequiredArgsConstructor
@Service
public class OpcUaDeviceService {

    private final OpcUaService opcUaService;

    // 读取节点
    public Object readTemperature() {
        return opcUaService.read("opcua1", "ns=2;s=Temperature");
    }

    // 写入节点
    public void setTargetTemperature(double value) {
        opcUaService.write("opcua1", "ns=2;s=TargetTemperature", value);
    }
}
```

## 配置项

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `atom.opc.ua.enabled` | Boolean | `true` | 是否启用 OPC UA 模块 |
| `atom.opc.ua.id` | String | `default` | 默认实例 ID |
| `atom.opc.ua.url` | String | — | 服务器地址 |
| `atom.opc.ua.username` | String | — | 用户名 |
| `atom.opc.ua.password` | String | — | 密码 |
| `atom.opc.ua.certificate-path` | String | — | 证书路径 |
| `atom.opc.ua.instances` | List | — | 多实例配置列表 |

## 开源协议

[木兰宽松许可证 v2 (MulanPSL-2.0)](https://license.coscl.org.cn/MulanPSL2)
