<h1 align="center">Atom OPC DA Client</h1>

```xml
<!-- 指定parent -->
<dependency>
	<groupId>cn.mindit</groupId>
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
      id: default # 实例 ID
      host: # 实例主机
      domain: # 实例域
      user:  # 实例用户
      password: # 实例密码
      prog-id: # 实例 ProgID
      cls-id: # 实例 ClsID
      period: 1000 # 刷新间隔
      async: true # 是否异步执行
      initialRefresh: false # 初始化获取全量数据, 仅在 async 为 true 时有效
      instances: # OPC DA 实例配置, 可以有多个
        - id: opcda1 # 实例 ID
          host: 127.0.0.1 # 实例主机
          domain: # 实例域
          user: opc # 实例用户
          password: opc123 # 实例密码
          prog-id: # 实例 ProgID
          cls-id: # 实例 ClsID
          period: 1000 # 刷新间隔
          async: true # 是否异步执行
          initialRefresh: false # 初始化获取全量数据, 仅在 async 为 true 时有效
```

## 使用

订阅消息

```java

import org.openscada.opc.lib.da.Item;
import org.openscada.opc.lib.da.ItemState;

@OpcDaListener(id = "opcda1", items = {"A.B.C", "D.E.F"})
public void message(Item item, ItemState value) {
	log.info("item: {}, value: {}", item, value);
}

```

发布消息

```java

import cn.mindit.atom.opc.da.OpcDaService;

@RequiredArgsConstructor
@RequestMapping("/")
@RestController
public class TestController {

	private final OpcDaService opcDaService;

	@RequestMapping("")
	public String test() {
		opcDaService.updateItem("opcda1", "A.B.C", 1);
		return "test";
	}

}
```