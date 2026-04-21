# Atom Spring Boot Starter Scheduling ShedLock

Atom 分布式调度锁模块，基于 ShedLock + Redis 确保定时任务在集群环境中不重复执行。

## 功能特性

- **分布式锁** — 基于 Redis 的分布式锁，集群环境下保证任务唯一执行
- **注解驱动** — 与 Spring `@Scheduled` + `@SchedulerLock` 无缝集成
- **死锁防护** — 通过 `lockAtMostFor` 自动释放死锁，防止任务永久阻塞
- **开箱即用** — 添加依赖并配置 Redis 即可使用，零代码侵入

## 快速开始

### 添加依赖

```xml
<dependency>
  <groupId>cn.mindit</groupId>
  <artifactId>atom-spring-boot-starter-scheduling-shedlock</artifactId>
</dependency>
```

### 配置 Redis

```yaml
spring:
  data:
    redis:
      host: localhost
      port: 6379
      password:
      database: 0

atom:
  scheduling:
    shedlock:
      enabled: true
```

### 使用示例

```java
@Slf4j
@Service
public class ScheduledTaskService {

    @Scheduled(cron = "0 0/30 * * * ?")
    @SchedulerLock(name = "DataSyncTask", lockAtMostFor = "25m")
    public void syncData() {
        log.info("数据同步任务执行");
    }

    @Scheduled(cron = "0 0 1 * * ?")
    @SchedulerLock(
        name = "ReportTask",
        lockAtMostFor = "2h",
        lockAtLeastFor = "30s"
    )
    public void generateReport() {
        log.info("报表生成任务执行");
    }
}
```

### @SchedulerLock 参数

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| `name` | String | ✅ | — | 锁名称，必须全局唯一 |
| `lockAtMostFor` | String | ❌ | `10m` | 锁最大持有时间，防止死锁 |
| `lockAtLeastFor` | String | ❌ | `1s` | 锁最小持有时间，防止重复执行 |

### 锁时间配置建议

- **lockAtMostFor**：设置为任务正常执行时间的 2-3 倍
- **lockAtLeastFor**：设置为任务执行间隔的 50%-80%

### 锁命名规范

建议使用 `{ServiceName}:{MethodName}` 格式：

```java
@SchedulerLock(name = "UserService:syncData")       // ✅ 推荐
@SchedulerLock(name = "ReportService:generateDaily") // ✅ 推荐
@SchedulerLock(name = "task1")                       // ❌ 不推荐
```

## 配置项

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `atom.scheduling.shedlock.enabled` | Boolean | `true` | 是否启用 ShedLock |

## 故障排除

**任务没有执行？**
- 检查 `@Scheduled` 表达式是否正确
- 确认已添加 `@EnableScheduling`
- 验证 Redis 连接是否正常

**任务重复执行？**
- 检查 `@SchedulerLock` 注解是否已添加
- 确认锁名称是否全局唯一
- 验证 `lockAtLeastFor` 设置是否合理

**调试日志：**

```yaml
logging:
  level:
    net.javacrumbs.shedlock: DEBUG
```

## 开源协议

[木兰宽松许可证 v2 (MulanPSL-2.0)](https://license.coscl.org.cn/MulanPSL2)
