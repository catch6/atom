# Atom Spring Cloud Starter Kafka

Atom Kafka 消息队列模块，提供消息发送服务和自动 Topic 创建，简化 Kafka 集成。

## 功能特性

- **消息发送** — `KafkaService` 封装消息发送，支持 JSON 序列化和异步发送
- **自动建 Topic** — 根据配置自动创建 Kafka Topic，指定分区数和副本数
- **开箱即用** — 添加依赖并配置 Kafka 地址即可使用

## 快速开始

### 添加依赖

```xml
<dependency>
  <groupId>cn.mindit</groupId>
  <artifactId>atom-spring-cloud-starter-kafka</artifactId>
</dependency>
```

### 配置

```yaml
atom:
  kafka:
    enabled: true

spring:
  kafka:
    bootstrap-servers: localhost:9092
```

### 使用示例

#### 发送消息

```java
@RequiredArgsConstructor
@Service
public class NotificationService {

    private final KafkaService kafkaService;

    public void sendNotification(String userId, String message) {
        Map<String, Object> payload = Map.of(
            "userId", userId,
            "message", message,
            "timestamp", System.currentTimeMillis()
        );
        kafkaService.send("notification-topic", payload);
    }
}
```

#### 消费消息

使用 Spring Kafka 原生的 `@KafkaListener` 注解：

```java
@Component
public class NotificationConsumer {

    @KafkaListener(topics = "notification-topic", groupId = "notification-group")
    public void onMessage(String message) {
        log.info("收到通知: {}", message);
    }
}
```

## 配置项

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `atom.kafka.enabled` | Boolean | `true` | 是否启用 Kafka 模块 |

> 更多 Kafka 配置项请参考 [Spring Kafka 文档](https://docs.spring.io/spring-kafka/reference/)。

## 开源协议

[木兰宽松许可证 v2 (MulanPSL-2.0)](https://license.coscl.org.cn/MulanPSL2)
