# Atom Spring Boot Starter Scheduling

Atom 动态定时任务模块，支持运行时动态添加、删除和管理定时任务，基于 Spring Scheduling 增强。

## 功能特性

- **动态管理** — 运行时添加、删除、替换定时任务，无需重启应用
- **Cron 表达式** — 基于 cron 表达式调度，支持运行时校验
- **反射调用** — 通过类名 + 方法名配置任务，支持有参/无参方法
- **灵活删除** — 支持按名称、前缀、后缀、包含、正则表达式批量删除任务

## 快速开始

### 添加依赖

```xml
<dependency>
  <groupId>cn.mindit</groupId>
  <artifactId>atom-spring-boot-starter-scheduling</artifactId>
</dependency>
```

### 配置

```yaml
atom:
  scheduling:
    enabled: true
```

### 使用示例

#### 动态注册任务

```java
@RequiredArgsConstructor
@Configuration
public class TaskConfig {

    private final TaskService taskService;

    @PostConstruct
    public void init() {
        // 批量设置任务（会替换所有已有任务）
        taskService.setTasks(List.of(
            new Task("开灯任务", "0 0 8 * * ?", "cn.example.service.LightingService", "turnOn", ""),
            new Task("关灯任务", "0 0 20 * * ?", "cn.example.service.LightingService", "turnOff", "")
        ));

        // 追加任务
        taskService.addTask(new Task("心跳检测", "0/30 * * * * ?", "cn.example.service.HeartbeatService", "check", ""));
    }
}
```

#### Task 参数说明

```java
new Task(
    "任务名称",           // name: 唯一标识
    "0/5 * * * * ?",     // cron: cron 表达式
    "cn.example.Service", // className: 全限定类名（需为 Spring Bean）
    "methodName",         // methodName: 方法名
    ""                    // args: 参数（可选）
)
```

#### 删除任务

```java
// 按名称删除
taskService.removeTask("开灯任务");

// 批量删除
taskService.removeTasks(List.of("开灯任务", "关灯任务"));

// 按前缀删除
taskService.removeByPrefix("Lighting");

// 按正则删除
taskService.removeByRegex(".*灯.*");
```

## 配置项

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `atom.scheduling.enabled` | Boolean | `true` | 是否启用定时任务模块 |

## 开源协议

[木兰宽松许可证 v2 (MulanPSL-2.0)](https://license.coscl.org.cn/MulanPSL2)
