<h1 align="center">Atom OPC DA Client</h1>

```xml
<!-- 指定parent -->
<dependency>
	<groupId>net.wenzuo</groupId>
	<artifactId>atom-spring-boot-starter-opc-da</artifactId>
</dependency>
```

## 配置项

各个模块的配置项，可以在 application.yml 中覆盖

```yaml
atom:
  opc:
    da:
      enabled: true # 是否启用 OPC DA 模块
      instances: # OPC DA 实例配置, 可以有多个
        - id: opcda1 # 实例 ID
          host: 127.0.0.1 # 实例主机
          domain: # 实例域
          user: opc # 实例用户
          password: opc123 # 实例密码
          prog-id: # 实例 ProgID
          cls-id: # 实例 ClsID
```

## 使用

订阅消息

```java

@OpcDaListener(id = "opcda1", tags = {"A.B.C", "D.E.F"}, period = 3000, async = false)
public void message(String tag, String value) {
	log.info("tag: {}, value: {}", tag, value);
}

```

发布消息

```java

@RequiredArgsConstructor
@RequestMapping("/")
@RestController
public class TestController {

	private final OpcDaService opcDaService;

	@RequestMapping("")
	public String test() {
		opcDaService.write("opcda1", "A.B.C", 1);
		return "test";
	}

}
```