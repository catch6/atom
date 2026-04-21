# Atom Spring Boot Starter MQTT

Atom MQTT 消息模块，支持 MQTT v3/v5 协议，提供多实例连接、注解式消息订阅和异步消息分发。

## 功能特性

- **双协议支持** — 同时支持 MQTT v3.1.1 和 MQTT v5 协议
- **多实例连接** — 支持同时连接多个 MQTT Broker，各实例独立配置
- **注解式订阅** — 通过 `@MqttListener` 注解声明式订阅 Topic
- **接口式订阅** — 实现 `MqttSubscriber` 接口订阅消息
- **异步分发** — 可选线程池异步处理消息，避免阻塞
- **错误处理** — 可自定义 `MqttListenerErrorHandler` 处理消费异常
- **自动重连** — 内置断线自动重连机制

## 快速开始

### 添加依赖

```xml
<dependency>
  <groupId>cn.mindit</groupId>
  <artifactId>atom-spring-boot-starter-mqtt</artifactId>
</dependency>
```

### 配置

```yaml
atom:
  mqtt:
    enabled: true
    id: default
    url: tcp://broker.emqx.io:1883
    username:
    password:
    client-id:
    instances:              # 多实例配置
      - id: emqx1
        enabled: true
        url: tcp://broker.emqx.io:1883
        username:
        password:
        client-id:
```

### 使用示例

#### 订阅消息（注解方式）

```java
@Component
public class MqttMessageHandler {

    @MqttListener(topics = "testtopic/#")
    public void onMessage(String topic, String message) {
        log.info("收到消息: topic={}, message={}", topic, message);
    }

    // 指定实例 ID
    @MqttListener(id = "emqx1", topics = {"sensor/temperature", "sensor/humidity"})
    public void onSensorData(String topic, String message) {
        log.info("传感器数据: topic={}, message={}", topic, message);
    }
}
```

#### 订阅消息（接口方式）

```java
@Component
public class CustomSubscriber implements MqttSubscriber {

    @Override
    public void onMessage(String topic, String message) {
        log.info("收到消息: {} {}", topic, message);
    }
}
```

#### 发布消息

```java
@RequiredArgsConstructor
@Service
public class DeviceService {

    private final MqttService mqttService;

    public void sendCommand(String deviceId, String command) {
        mqttService.send("device/" + deviceId + "/command", command);
    }

    // 指定实例发送
    public void sendToInstance(String message) {
        mqttService.send("emqx1", "topic/test", message, 1);
    }
}
```

## 配置项

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `atom.mqtt.enabled` | Boolean | `true` | 是否启用 MQTT 模块 |
| `atom.mqtt.id` | String | `default` | 默认实例 ID |
| `atom.mqtt.url` | String | — | 服务器地址 |
| `atom.mqtt.username` | String | — | 用户名 |
| `atom.mqtt.password` | String | — | 密码 |
| `atom.mqtt.client-id` | String | — | 客户端 ID |
| `atom.mqtt.instances` | List | — | 多实例配置列表 |

## 开源协议

[木兰宽松许可证 v2 (MulanPSL-2.0)](https://license.coscl.org.cn/MulanPSL2)
