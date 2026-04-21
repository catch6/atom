# Atom Spring Boot Starter Core

Atom 核心基础模块，提供统一异常体系、响应封装、参数校验断言、JSON 增强、工具类集合和链路追踪，为所有 Atom 模块提供基础支持。

> 此模块为所有 Atom 模块的基础依赖，使用其他 Atom 模块时会自动引入，通常无需单独添加。

## 功能特性

- **统一异常体系** — BusinessException（业务异常）、ServiceException（服务异常），层次分明
- **统一响应封装** — `Result<T>` 标准响应格式，支持 `ResultProvider` / `Code` 接口扩展
- **参数校验断言** — `Must`（服务级，抛 ServiceException）、`Should`（用户级，抛 BusinessException）
- **JSON 序列化增强** — `@JsonDecimalFormat` 数字格式化、`@JsonMask` 数据脱敏
- **工具类集合** — NanoIdUtils、UUIDv7、BaseUtils、JsonUtils、TreeUtils、时间工具类等
- **链路追踪** — TraceIdTaskDecorator，自动为异步任务传递 traceId
- **自动配置** — 异步线程池、日期格式、文件上传限制、UTF-8 编码、虚拟线程

## 快速开始

### 添加依赖

```xml
<dependency>
  <groupId>cn.mindit</groupId>
  <artifactId>atom-spring-boot-starter-core</artifactId>
</dependency>
```

### 配置

```yaml
atom:
  core:
    enabled: true  # 是否启用核心模块
    async: true    # 是否启用异步处理
    json: true     # 是否启用 Jackson 增强
```

### 使用示例

#### 统一响应

```java
// 成功响应
Result<String> success = Result.ok("操作成功");
Result<Void> ok = Result.ok();

// 失败响应
Result<Void> fail = Result.fail(4001, "参数错误");
Result<Void> fail2 = Result.fail(Code.INVALID_PARAMETER);
```

#### 异常处理

```java
// 业务异常（用户可见错误）
throw new BusinessException("用户不存在");
throw new BusinessException(4001, "参数错误");

// 服务异常（内部错误，记录 error 日志）
throw new ServiceException("数据库连接失败");
```

#### 参数校验断言

```java
// Must — 服务数据校验，失败抛 ServiceException
Must.notNull(user, "用户信息不能为空");
Must.notBlank(phone, "手机号不能为空");
Must.isTrue(age > 0, "年龄必须大于0");

// Should — 用户参数校验，失败抛 BusinessException
Should.notEmpty(username, "用户名不能为空");
Should.isTrue(password.length() >= 6, "密码长度不能少于6位");
```

#### JSON 增强

```java
public class UserVO {

    @JsonDecimalFormat("#,##0.00")
    private BigDecimal balance;

    @JsonMask(MaskType.PHONE)
    private String phone;

    @JsonMask(MaskType.ID_CARD)
    private String idCard;
}
```

#### 工具类

```java
// NanoId — 短 ID 生成
String id = NanoIdUtils.nanoId();      // 默认 15 位
String shortId = NanoIdUtils.nanoId(10);

// UUIDv7 — 时间有序 UUID
String uuid = UUIDv7.randomUUID().toString();

// Base 编码
String base32 = BaseUtils.idToBase32(12345L);
long id = BaseUtils.base32ToId(base32);

// JSON 工具
String json = JsonUtils.toJson(user);
User user = JsonUtils.fromJson(json, User.class);

// 树形结构
List<TreeNode> tree = TreeUtils.buildTree(nodes, 0L);
```

## 自动配置的默认值

```yaml
# 日期时间格式
spring:
  mvc:
    format:
      date-time: yyyy-MM-dd HH:mm:ss
      date: yyyy-MM-dd
      time: HH:mm:ss

# 文件上传限制
  servlet:
    multipart:
      max-file-size: 10GB
      max-request-size: 10GB

# 异步任务线程池
  task:
    execution:
      thread-name-prefix: async-
      pool:
        core-size: 8
        max-size: 16
        keep-alive: 60s
        queue-capacity: 10000

# 虚拟线程
  threads:
    virtual:
      enabled: true

# UTF-8 编码
server:
  servlet:
    encoding:
      force: true
```

## 配置项

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `atom.core.enabled` | Boolean | `true` | 是否启用核心模块 |
| `atom.core.async` | Boolean | `true` | 是否启用异步处理 |
| `atom.core.json` | Boolean | `true` | 是否启用 Jackson 增强 |

## 开源协议

[木兰宽松许可证 v2 (MulanPSL-2.0)](https://license.coscl.org.cn/MulanPSL2)
