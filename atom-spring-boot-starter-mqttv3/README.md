<h1 align="center">Atom MQTTv3</h1>

```xml
<!-- 指定parent -->
<dependency>
	<groupId>net.wenzuo</groupId>
	<artifactId>atom-spring-boot-starter-mqttv3</artifactId>
</dependency>
```

## 配置项

各个模块的配置项，可以在 application.yml 中覆盖

```yaml
atom:
  mqttv3:
    enabled: true # 是否启用 MQTT 模块
    instances: # MQTT 实例配置, 可以有多个
      - id: emqx1 # 实例 ID
        url: tcp://broker.emqx.io:1883 # MQTT 服务器地址
        username: # MQTT 服务器用户名
        password: # MQTT 服务器密码
        client-id: # MQTT 客户端 ID
```

## 使用

订阅消息

```java

@Mqttv3Listener(id = "emqx", topics = "testtopic/#")
public void message(String topic, String message) {
	log.info("mqttv3 message: {} {}", topic, message);
}

```

发布消息

```java

@RequiredArgsConstructor
@RequestMapping("/")
@RestController
public class TestController {

	private final Mqttv3Service mqttv3Service;

	@RequestMapping("")
	public String test() {
		mqttv3Service.send("emqx", "testtopic/test", "test");
		return "test";
	}

}
```