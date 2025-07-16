# 定时任务模块

## 使用

```java

@RequiredArgsConstructor
@Configuration
public class LightingConfig {

	private final TaskService taskService;

	@PostConstruct
	public void init() {
		taskService.setTasks(List.of(
			new Task("Task1", "0/5 * * * * ?", "cn.mindit.iot.auth.service.LightingService", "turnOn", ""),
			new Task("Task2", "0/5 * * * * ?", "cn.mindit.iot.auth.service.LightingService", "turnOff", "")
		));

		// taskService.addTasks(List.of(
		// 	new Task("Task1","0/5 * * * * ?","cn.mindit.iot.auth.service.LightingService","turnOn",""),
		// 	new Task("Task2","0/5 * * * * ?","cn.mindit.iot.auth.service.LightingService","turnOff",""),
		// ));

		// taskService.removeTask("Task1");
		//
		// taskService.removeTasks(List.of("Task1","Task2"));
	}

}

@Slf4j
@RequiredArgsConstructor
@Service
public class LightingService {

	public void turnOn() {
		log.info("turn on");
	}

	public void turnOff() {
		log.info("turn off");
	}

}
```