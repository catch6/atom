# Atom Spring Boot Starter OPC DA

Atom OPC DA 工业协议模块，提供 OPC DA 服务器连接和数据读写，支持多实例和注解式数据订阅。

## 功能特性

- **多实例连接** — 支持同时连接多个 OPC DA 服务器
- **注解式订阅** — 通过 `@OpcDaListener` 注解声明式监听数据变化
- **接口式订阅** — 实现 `OpcDaSubscriber` 接口订阅数据
- **同步/异步** — 支持同步和异步两种数据访问模式
- **数据写入** — 通过 `OpcDaService` 向 OPC 点位写入数据
- **质量过滤** — 仅处理质量值为 Good (192) 的数据
- **自动重连** — 内置连接管理和自动重连

## 快速开始

### 添加依赖

```xml
<dependency>
  <groupId>cn.mindit</groupId>
  <artifactId>atom-spring-boot-starter-opc-da</artifactId>
</dependency>
```

### 配置

```yaml
atom:
  opc:
    da:
      enabled: true
      id: default
      host:                  # OPC 服务器主机
      domain:                # 域
      user:                  # 用户名
      password:              # 密码
      prog-id:               # ProgID
      cls-id:                # ClsID
      period: 1000           # 刷新间隔（ms）
      async: true            # 是否异步执行
      initialRefresh: false  # 初始化获取全量数据（仅 async=true 时有效）
      instances:             # 多实例配置
        - id: opcda1
          host: 127.0.0.1
          user: opc
          password: opc123
          prog-id:
          cls-id:
          period: 1000
          async: true
          initialRefresh: false
```

### 使用示例

#### 订阅数据变化

```java
@Component
public class OpcDaDataHandler {

    @OpcDaListener(id = "opcda1", items = {"A.B.C", "D.E.F"})
    public void onDataChange(Item item, ItemState value) {
        log.info("点位: {}, 值: {}", item.getId(), value.getValue());
    }
}
```

#### 写入数据

```java
@RequiredArgsConstructor
@Service
public class DeviceControlService {

    private final OpcDaService opcDaService;

    public void setTemperature(int value) {
        opcDaService.updateItem("opcda1", "A.B.C", value);
    }
}
```

## 配置项

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `atom.opc.da.enabled` | Boolean | `true` | 是否启用 OPC DA 模块 |
| `atom.opc.da.id` | String | `default` | 默认实例 ID |
| `atom.opc.da.host` | String | — | OPC 服务器主机 |
| `atom.opc.da.domain` | String | — | 域 |
| `atom.opc.da.user` | String | — | 用户名 |
| `atom.opc.da.password` | String | — | 密码 |
| `atom.opc.da.prog-id` | String | — | ProgID |
| `atom.opc.da.cls-id` | String | — | ClsID |
| `atom.opc.da.period` | Integer | `1000` | 刷新间隔（ms） |
| `atom.opc.da.async` | Boolean | `true` | 是否异步执行 |
| `atom.opc.da.initialRefresh` | Boolean | `false` | 初始化获取全量数据 |
| `atom.opc.da.instances` | List | — | 多实例配置列表 |

## 开源协议

[木兰宽松许可证 v2 (MulanPSL-2.0)](https://license.coscl.org.cn/MulanPSL2)
