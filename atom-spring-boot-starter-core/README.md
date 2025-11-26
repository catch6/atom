# Atom Spring Boot Starter Core

[![License](https://img.shields.io/badge/License-Mulan%20PSL%20v2-blue.svg)](http://license.coscl.org.cn/MulanPSL2)
[![Java](https://img.shields.io/badge/Java-17+-green.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green.svg)](https://spring.io/projects/spring-boot)

Atom 核心基础模块，提供通用工具类、异常处理、基础配置等核心功能，为其他 Atom 模块提供基础支持。

## 🚀 特性

- **🛠️ 通用工具类集合** - 提供开发中常用的工具方法
- **🔍 参数校验断言** - 支持业务异常和业务断言的统一处理
- **📊 统一响应格式** - 标准化的 API 响应结果封装
- **🆔 唯一 ID 生成** - 高性能的 NanoId 和 Base 编码 ID 生成工具
- **📅 时间工具类** - 日期时间处理的便捷工具
- **🌲 树形结构处理** - 树形数据的构建和操作工具
- **⚙️ 自动配置** - Spring Boot 自动配置支持
- **🔧 JSON 序列化增强** - 自定义 JSON 序列化和反序列化规则
- **📋 日志追踪** - 分布式链路追踪 ID 支持

## 📦 依赖

本模块依赖以下核心库：

```xml

<dependencies>
  <!-- Spring Boot 核心依赖 -->
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter</artifactId>
  </dependency>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-logging</artifactId>
  </dependency>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-json</artifactId>
  </dependency>

  <!-- 开发工具 -->
  <dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
  </dependency>

  <!-- Hutool 工具库 -->
  <dependency>
    <groupId>cn.hutool</groupId>
    <artifactId>hutool-all</artifactId>
  </dependency>
</dependencies>
```

## 🛠️ 使用方式

### Maven 依赖

```xml

<dependency>
  <groupId>cn.mindit</groupId>
  <artifactId>atom-spring-boot-starter-core</artifactId>
  <version>${atom.version}</version>
</dependency>
```

### 启用自动配置

在 Spring Boot 应用中，只需添加依赖即可自动启用所有核心功能。如需禁用，可在配置文件中设置：

```yaml
atom:
  core:
    enabled: false
```

## 📚 核心功能

### 1. 工具类集合

#### BaseUtils - Base 编码工具

提供多种 Base 编码转换功能：

```java
// Base32 编码
String base32 = BaseUtils.idToBase32(12345L);
long id = BaseUtils.base32ToId(base32);

// 自定义字符集编码
String custom = BaseUtils.idToBase("0123456789ABCDEF", 255L);
```

#### NanoIdUtils - 短 ID 生成器

生成高性能的短 ID，适合作为 traceId 等场景：

```java
// 生成默认长度(15位)的 NanoId
String nanoId = NanoIdUtils.nanoId();

// 生成指定长度的 NanoId
String shortId = NanoIdUtils.nanoId(10);
```

#### 时间工具类

提供便捷的日期时间操作：

```java
// LocalDateTimeUtils - 日期时间工具
LocalDateTime now = LocalDateTimeUtils.now();
String formatted = LocalDateTimeUtils.format(now, "yyyy-MM-dd HH:mm:ss");

// LocalTimeUtils - 时间工具
LocalTime time = LocalTimeUtils.now();
boolean isWorkingTime = LocalTimeUtils.isWorkingHours(time);
```

### 2. 统一异常处理

#### BusinessException - 业务异常

```java
// 抛出标准业务异常
throw new BusinessException("操作失败");

// 抛出带错误码的业务异常
throw new

BusinessException(4001,"参数错误");

// 使用 ResultProvider 构建异常
throw new

BusinessException(Code.INVALID_PARAMETER);
```

#### ServiceException - 服务异常

用于服务内部错误，会记录 error 日志：

```java
// 服务调用失败
throw new ServiceException("第三方服务调用失败");

// 关键业务失败
throw new

ServiceException("短信发送失败",5001);
```

### 3. 参数校验断言

#### Must - 服务数据校验断言

用于关键业务数据校验，失败时抛出 ServiceException：

```java
// 基础校验
Must.notNull(user, "用户信息不能为空");
Must.

notBlank(phone, "手机号不能为空");
Must.

isTrue(age >0, "年龄必须大于0");

// 集合校验
Must.

notEmpty(list, "列表不能为空");
Must.

noNullElements(array, "数组不能包含空元素");

// 业务逻辑校验
Must.

isEquals(password, confirmPassword, "两次密码输入不一致");
Must.

notContains(email, "@spam.com","不支持该邮箱域名");
```

#### Should - 用户参数校验断言

用于用户输入参数校验，失败时抛出 BusinessException：

```java
// 参数校验
Should.notEmpty(username, "用户名不能为空");
Should.

isTrue(password.length() >=6,"密码长度不能少于6位");
    Should.

matches(email, "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$","邮箱格式不正确");
```

### 4. 统一响应格式

#### Result - 标准响应结果

```java
// 成功响应
Result<String> success = Result.ok("操作成功");
Result<Void> successNoData = Result.ok();

// 失败响应
Result<Void> fail = Result.fail(4001, "参数错误");

// 使用 ResultProvider
Result<Void> fail2 = Result.fail(Code.INVALID_PARAMETER);
```

### 5. JSON 序列化增强

#### JsonUtils - JSON 工具类

```java
// 对象序列化
String json = JsonUtils.toJson(user);

// JSON 反序列化
User user = JsonUtils.fromJson(json, User.class);

// List 反序列化
List<User> users = JsonUtils.fromJsonList(json, User.class);
```

#### 自定义序列化注解

```java
public class UserDTO {

    // 数字格式化
    @JsonDecimalFormat("#,##0.00")
    private BigDecimal balance;

    // 数据脱敏
    @JsonMask(MaskType.PHONE)
    private String phone;

    // 自定义序列化
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

}
```

### 6. 树形结构处理

#### TreeNode 和 TreeUtils

```java
// 构建树形结构
List<TreeNode> treeNodes = Arrays.asList(
        TreeNode.builder().id(1L).parentId(0L).name("根节点").build(),
        TreeNode.builder().id(2L).parentId(1L).name("子节点1").build(),
        TreeNode.builder().id(3L).parentId(1L).name("子节点2").build()
    );

List<TreeNode> tree = TreeUtils.buildTree(treeNodes, 0L);
```

### 7. 链路追踪

#### TraceIdTaskDecorator

自动为异步任务传递 TraceId：

```java

@Component
public class AsyncTaskService {

    @Async
    public CompletableFuture<Void> asyncTask() {
        // 自动获取和传递 TraceId
        String traceId = MDC.get("traceId");
        // 异步任务逻辑
        return CompletableFuture.completedFuture(null);
    }

}
```

## ⚙️ 配置说明

### 核心配置属性

```yaml
atom:
  core:
    # 是否启用核心模块，默认为 true
    enabled: true
```

### 自动配置的默认值

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
    scheduling:
      pool:
        size: 8
      thread-name-prefix: scheduled-

# 强制 UTF-8 编码
server:
  servlet:
    encoding:
      force: true

# 启用虚拟线程
spring:
  threads:
    virtual:
      enabled: true
```

## 🏗️ 架构设计

### 模块结构

```
atom-spring-boot-starter-core/
├── src/main/java/cn/mindit/atom/core/
│   ├── core/                      # 核心配置类
│   │   ├── CoreAutoConfiguration.java
│   │   ├── CoreProperties.java
│   │   ├── CoreJsonConfiguration.java
│   │   └── CoreTraceConfiguration.java
│   └── util/                      # 工具类集合
│       ├── BaseUtils.java
│       ├── NanoIdUtils.java
│       ├── LocalDateTimeUtils.java
│       ├── LocalTimeUtils.java
│       ├── JsonUtils.java
│       ├── TreeUtils.java
│       ├── Result.java
│       ├── BusinessException.java
│       ├── ServiceException.java
│       ├── Must.java
│       └── Should.java
└── src/main/resources/
    ├── application-core.properties    # 默认配置
    ├── META-INF/spring/
    │   └── org.springframework.boot.autoconfigure.AutoConfiguration.imports
    └── logback-spring.xml             # 日志配置
```

### 设计原则

1. **零配置启动** - 开箱即用，遵循 Spring Boot 约定优于配置
2. **模块化设计** - 功能职责单一，便于按需引入
3. **向后兼容** - 保持 API 稳定性，升级平滑
4. **性能优先** - 工具类设计考虑性能影响
5. **安全可靠** - 完善的异常处理和参数校验

## 🤝 贡献

欢迎提交 Issue 和 Pull Request 来完善这个模块。

## 📞 支持

如有问题或建议，请通过以下方式联系：

- 邮箱: catchlife6@163.com
- GitHub: [https://github.com/catch6/atom](https://github.com/catch6/atom)

---

**注意**: 此模块为 Atom 项目的基础核心模块，其他 Atom 模块都依赖于此。在使用其他 Atom 模块时，会自动引入此核心模块。