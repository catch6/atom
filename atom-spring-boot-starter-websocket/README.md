# Atom Spring Boot Starter WebSocket

Atom WebSocket 客户端模块，提供多实例连接管理、自动重连、心跳检测和事件驱动的消息处理。

## 功能特性

- **多实例连接** — 支持同时管理多个独立的 WebSocket 客户端连接
- **自动重连** — 支持固定延迟和指数退避两种重连策略
- **心跳检测** — 可配置心跳间隔，自动发送 ping 保持连接
- **事件驱动** — 集成 Spring ApplicationEvent，连接/断开/消息均通过事件通知
- **消息类型** — 支持文本和二进制 WebSocket 消息
- **自定义请求头** — 支持 WebSocket 握手时携带自定义 HTTP 头

## 快速开始

### 添加依赖

```xml
<dependency>
  <groupId>cn.mindit</groupId>
  <artifactId>atom-spring-boot-starter-websocket</artifactId>
</dependency>
```

### 配置

```yaml
atom:
  websocket:
    enabled: true
    id: default
    url:                            # 目标 URL，如 ws://localhost:8080/ws
    auto-reconnect: true
    reconnect-strategy: FIXED_DELAY # FIXED_DELAY 或 EXPONENTIAL_BACKOFF
    reconnect-delay: 5s
    max-reconnect-attempts: 10
    heartbeat-interval: 10s
    instances:                      # 多实例配置
      - id: server1
        enabled: true
        url: ws://server1.example.com/ws
        auto-reconnect: true
        reconnect-strategy: EXPONENTIAL_BACKOFF
        reconnect-delay: 3s
        max-reconnect-attempts: 5
        heartbeat-interval: 20s
```

### 使用示例

#### 发送消息

```java
@RequiredArgsConstructor
@Service
public class WebSocketService {

    private final WebSocketClientManager clientManager;

    public void sendText(String message) {
        clientManager.sendMessage("server1", message);
    }

    public void sendBinary(byte[] data) {
        clientManager.sendMessage("server1", data);
    }

    public boolean isConnected(String id) {
        return clientManager.isConnected(id);
    }
}
```

#### 监听事件

```java
@Component
public class WebSocketEventHandler {

    @EventListener
    public void onConnected(WebSocketConnectedEvent event) {
        log.info("已连接: {} -> {}", event.getClientId(), event.getUri());
    }

    @EventListener
    public void onDisconnected(WebSocketDisconnectedEvent event) {
        log.warn("已断开: {} -> {}", event.getClientId(), event.getUri());
    }

    @EventListener
    public void onMessage(WebSocketMessageReceivedEvent event) {
        if (event.isText()) {
            log.info("收到消息: {} -> {}", event.getClientId(), event.getMessage());
        }
    }
}
```

## 配置项

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `atom.websocket.enabled` | Boolean | `true` | 是否启用 WebSocket 模块 |
| `atom.websocket.id` | String | `default` | 默认实例 ID |
| `atom.websocket.url` | String | — | 目标 URL |
| `atom.websocket.auto-reconnect` | Boolean | `true` | 是否自动重连 |
| `atom.websocket.reconnect-strategy` | Enum | `FIXED_DELAY` | 重连策略 |
| `atom.websocket.reconnect-delay` | Duration | `5s` | 重连延迟 |
| `atom.websocket.max-reconnect-attempts` | Integer | `10` | 最大重试次数 |
| `atom.websocket.heartbeat-interval` | Duration | `10s` | 心跳间隔 |
| `atom.websocket.instances` | List | — | 多实例配置列表 |

### 重连策略

| 策略 | 说明 |
|------|------|
| `FIXED_DELAY` | 固定延迟：每次重连间隔相同（`reconnect-delay`） |
| `EXPONENTIAL_BACKOFF` | 指数退避：每次重连间隔倍增（`reconnect-delay × 2^n`） |

## WebSocketClientManager API

| 方法 | 说明 |
|------|------|
| `getSession(id)` | 获取 WebSocket 会话 |
| `sendMessage(id, String)` | 发送文本消息 |
| `sendMessage(id, byte[])` | 发送二进制消息 |
| `isConnected(id)` | 检查连接状态 |
| `getClientIds()` | 获取所有客户端 ID |
| `connect(id)` | 手动连接 |
| `disconnect(id)` | 手动断开 |

## 开源协议

[木兰宽松许可证 v2 (MulanPSL-2.0)](https://license.coscl.org.cn/MulanPSL2)
