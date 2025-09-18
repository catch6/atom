# 分布式定时任务模块

## 使用

```java

import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@RequiredArgsConstructor
@Service
public class LightingService {

    @Scheduled(cron = "0 0 8 * * *")
    @SchedulerLock(name = "LightingService:turnOn")
    public void turnOn() {
        log.info("turn on");
    }

    @Scheduled(cron = "0 0 20 * * *")
    @SchedulerLock(name = "LightingService:turnOff")
    public void turnOff() {
        log.info("turn off");
    }

}
```