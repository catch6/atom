# Atom Spring Boot Starter API

Atom API 开发模块，提供通用 DTO/VO、自定义参数校验注解和校验工具类，简化 RESTful API 开发流程。

## 功能特性

- **通用 DTO/VO** — 开箱即用的分页、ID、搜索等标准数据模型
- **自定义校验注解** — @Phone、@AnyOfString、@AnyOfInt、@AnyOfLong、@AnyOfEnum 等
- **校验分组** — Create、Update、Get、Delete 四组校验分组，支持不同场景的差异化校验
- **校验工具类** — ValidatorUtils 支持快速失败和全量校验两种模式

## 快速开始

### 添加依赖

```xml
<dependency>
  <groupId>cn.mindit</groupId>
  <artifactId>atom-spring-boot-starter-api</artifactId>
</dependency>
```

> 💡 引入 `atom-spring-boot-starter-web` 时会自动包含此模块，无需重复引入。

### 使用示例

#### 通用 DTO/VO

```java
// 分页请求
public class UserQuery extends PageDTO {
    private String keyword;
}

// 分页响应
PageVO<UserVO> pageVO = new PageVO<>(records, total, page, size);

// ID 请求
IdDTO idDTO = new IdDTO();  // 内置 @NotNull 校验
IdsDTO idsDTO = new IdsDTO(); // 批量 ID，内置 @NotEmpty 校验
```

#### 自定义校验注解

```java
public class UserDTO {

    @Phone
    private String phone;

    @AnyOfString({"ADMIN", "USER", "GUEST"})
    private String role;

    @AnyOfInt({0, 1})
    private Integer status;

    @AnyOfEnum(enumClass = Gender.class)
    private String gender;
}
```

#### 校验分组

```java
public class UserDTO {

    @NotNull(groups = Update.class)
    private Long id;

    @NotBlank(groups = Create.class)
    private String name;
}

// Controller 中使用
@PostMapping
public Result<Void> create(@Validated(Create.class) @RequestBody UserDTO dto) { ... }

@PutMapping
public Result<Void> update(@Validated(Update.class) @RequestBody UserDTO dto) { ... }
```

#### 手动校验

```java
// 快速失败模式（遇到第一个错误即返回）
ValidatorUtils.validate(dto, true);

// 全量校验模式（收集所有错误）
ValidatorUtils.validate(dto, false);
```

## 通用数据模型

| 类名 | 说明 |
|------|------|
| `PageDTO` | 分页请求参数（page, size） |
| `PageVO<T>` | 分页响应结果（records, total, page, size） |
| `IdDTO` | 单 ID 请求 |
| `IdVO` | 单 ID 响应 |
| `IdsDTO` | 多 ID 请求 |
| `KeywordsDTO` | 搜索关键字请求 |
| `CodeNameVO` | 编码-名称对响应 |
| `ItemsVO<T>` | 列表响应 |

## 开源协议

[木兰宽松许可证 v2 (MulanPSL-2.0)](https://license.coscl.org.cn/MulanPSL2)
