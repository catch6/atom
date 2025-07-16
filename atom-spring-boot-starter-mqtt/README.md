<h1 align="center">Atom MQTT</h1>

```xml
<!-- 指定parent -->
<dependency>
	<groupId>cn.mindit</groupId>
	<artifactId>atom-spring-boot-starter-mqtt</artifactId>
</dependency>
```

## 配置项

各个模块的配置项，可以在 application.yml 中覆盖

```yaml
atom:
  mqtt:
    enabled: true # 是否启用 MQTT 模块
    id: default # 实例 ID
    url: tcp://broker.emqx.io:1883 # 服务器地址
    username: # 服务器用户名
    password: # 服务器密码
    client-id: # 客户端 ID
    instances: # MQTT 多实例配置
      - id: emqx1 # 实例 ID
        enabled: true # 是否启用
        url: tcp://broker.emqx.io:1883 # 服务器地址
        username: # 服务器用户名
        password: # 服务器密码
        client-id: # 客户端 ID
```

## 使用

### 订阅消息

1. 使用注解

```java

@MqttListener(topics = "testtopic/#")
public void message(String topic, String message) {
	log.info("mqtt message: {} {}", topic, message);
}
```

2. 实现`MqttSubscriber`接口

发布消息

```java

@RequiredArgsConstructor
@RequestMapping("/")
@RestController
public class TestController {

	private final MqttService mqttService;

	@RequestMapping("")
	public String test() {
		mqttService.send("testtopic/test", "test");
		return "test";
	}

}
```