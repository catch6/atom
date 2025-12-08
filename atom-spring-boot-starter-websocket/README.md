# atom-spring-boot-starter-websocket

Atom WebSocket模块，提供WebSocket客户端连接管理和消息处理，支持多实例连接和自动重连机制。

## 功能特性

- 支持多个独立的WebSocket客户端连接
- 自动重连机制，支持固定延迟和指数退避策略
- 心跳检测机制
- 集成Spring ApplicationEvent事件机制
- 完全通过`application.yml`或`application.properties`配置
- 支持自定义请求头
- 提供完整的API用于发送消息和管理连接

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>cn.mindit</groupId>
    <artifactId>atom-spring-boot-starter-websocket</artifactId>
    <version>${atom.version}</version>
</dependency>
```

### 2. 配置文件

在`application.yml`中配置WebSocket连接：

```yaml
atom:
  websocket:
    enabled: true
    # 单个连接配置
    id: "default"
    uri: "ws://localhost:8080/websocket"
    auto-reconnect: true
    reconnect-strategy: FIXED_DELAY
    reconnect-delay: 5s
    max-reconnect-attempts: 10
    heartbeat-interval: 30s
    headers:
      Authorization: "Bearer your-token"
      Custom-Header: "custom-value"
    
    # 多个连接配置
    instances:
      - id: "server1"
        enabled: true
        uri: "ws://server1.example.com/websocket"
        auto-reconnect: true
        reconnect-strategy: EXPONENTIAL_BACKOFF
        reconnect-delay: 3s
        max-reconnect-attempts: 5
        heartbeat-interval: 20s
        headers:
          Authorization: "Bearer token1"
      
      - id: "server2"
        enabled: true
        uri: "ws://server2.example.com/websocket"
        auto-reconnect: false
        reconnect-strategy: FIXED_DELAY
        reconnect-delay: 10s
        max-reconnect-attempts: 3
        heartbeat-interval: 60s
        headers:
          Authorization: "Bearer token2"
```

### 3. 使用WebSocketClientManager

```java
@Service
public class WebSocketService {
    
    @Autowired
    private WebSocketClientManager webSocketClientManager;
    
    public void sendMessageToServer1(String message) {
        try {
            webSocketClientManager.sendMessage("server1", message);
        } catch (Exception e) {
            // 处理发送失败
        }
    }
    
    public void sendBinaryMessage(String id, byte[] data) {
        try {
            webSocketClientManager.sendMessage(id, data);
        } catch (Exception e) {
            // 处理发送失败
        }
    }
    
    public boolean isServerConnected(String id) {
        return webSocketClientManager.isConnected(id);
    }
    
    public void reconnectServer(String id) {
        try {
            webSocketClientManager.connect(id);
        } catch (Exception e) {
            // 处理连接失败
        }
    }
}
```

### 4. 监听WebSocket事件

```java
@Component
public class WebSocketEventListener {
    
    @EventListener
    public void onConnected(WebSocketConnectedEvent event) {
        System.out.println("WebSocket connected: " + event.getClientId() + " -> " + event.getUri());
    }
    
    @EventListener
    public void onDisconnected(WebSocketDisconnectedEvent event) {
        System.out.println("WebSocket disconnected: " + event.getClientId() + " -> " + event.getUri());
        if (event.getException() != null) {
            event.getException().printStackTrace();
        }
    }
    
    @EventListener
    public void onMessageReceived(WebSocketMessageReceivedEvent event) {
        if (event.isText()) {
            System.out.println("Received text message from " + event.getClientId() + ": " + event.getMessage());
        } else {
            System.out.println("Received binary message from " + event.getClientId() + ": " + event.getMessage().length() + " bytes");
        }
    }
}
```

## 配置参数说明

### 全局配置

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `atom.websocket.enabled` | Boolean | true | 是否启用WebSocket模块 |
| `atom.websocket.order` | Integer | Ordered.LOWEST_PRECEDENCE | 加载顺序 |
| `atom.websocket.id` | String | "default" | 默认实例ID |
| `atom.websocket.uri` | String | - | 默认实例URI |
| `atom.websocket.auto-reconnect` | Boolean | true | 是否自动重连 |
| `atom.websocket.reconnect-strategy` | Enum | FIXED_DELAY | 重连策略：FIXED_DELAY或EXPONENTIAL_BACKOFF |
| `atom.websocket.reconnect-delay` | Duration | 5s | 重连基础延迟时间 |
| `atom.websocket.max-reconnect-attempts` | Integer | 10 | 最大重试次数 |
| `atom.websocket.heartbeat-interval` | Duration | 30s | 心跳间隔 |
| `atom.websocket.headers` | Map | {} | 连接请求头 |

### 实例配置

每个实例支持与全局配置相同的参数，可以覆盖全局设置。

## API文档

### WebSocketClientManager

#### 方法说明

- `WebSocketSession getSession(String id)`: 获取指定ID的WebSocket会话
- `void sendMessage(String id, String message)`: 发送文本消息
- `void sendMessage(String id, byte[] message)`: 发送二进制消息
- `boolean isConnected(String id)`: 检查连接状态
- `Set<String> getClientIds()`: 获取所有客户端ID
- `void connect(String id)`: 手动连接
- `void disconnect(String id)`: 手动断开连接

### 事件类

#### WebSocketConnectedEvent

连接建立时触发的事件。

- `String getClientId()`: 客户端ID
- `String getUri()`: 连接URI

#### WebSocketDisconnectedEvent

连接断开时触发的事件。

- `String getClientId()`: 客户端ID
- `String getUri()`: 连接URI
- `Exception getException()`: 异常信息（如果有）

#### WebSocketMessageReceivedEvent

接收到消息时触发的事件。

- `String getClientId()`: 客户端ID
- `String getUri()`: 连接URI
- `String getMessage()`: 消息内容
- `boolean isText()`: 是否为文本消息

## 重连策略

### FIXED_DELAY（固定延迟）

每次重连使用固定的延迟时间：
```
delay = reconnectDelay
```

### EXPONENTIAL_BACKOFF（指数退避）

每次重连延迟时间指数增长：
```
delay = reconnectDelay * 2^attempts
```

## 注意事项

1. 心跳消息格式为JSON：`{"type":"ping","timestamp":1234567890}`
2. 自动重连会在连接断开时触发，达到最大重试次数后停止
3. 所有WebSocket连接在应用启动时自动建立
4. 应用关闭时会自动断开所有连接
5. 发送消息前建议检查连接状态

## 示例项目

完整的使用示例请参考`src/test`目录下的测试代码。