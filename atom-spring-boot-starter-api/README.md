# Atom Spring Boot Starter API

[![License](https://img.shields.io/badge/License-Mulan%20PSL%20v2-blue.svg)](http://license.coscl.org.cn/MulanPSL2)
[![Java](https://img.shields.io/badge/Java-17+-green.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green.svg)](https://spring.io/projects/spring-boot)
[![OpenAPI](https://img.shields.io/badge/OpenAPI-3.0-blue.svg)](https://www.openapis.org/)

Atom API 开发模块，提供统一响应格式、参数验证、OpenAPI 文档支持，简化 RESTful API 开发流程。

## 🚀 特性

- **📝 统一请求响应格式** - 标准化的 API 请求和响应数据传输对象
- **✅ 增强参数验证** - 自定义验证注解和验证分组支持
- **📖 OpenAPI 文档集成** - 自动生成和维护 API 文档
- **🔍 验证工具类** - 提供便捷的参数验证工具方法
- **📄 分页支持** - 统一的分页请求和响应封装
- **🎯 验证分组** - 支持不同操作场景的参数验证
- **📱 手机号验证** - 内置手机号格式验证
- **🔢 枚举值验证** - 支持指定枚举值范围的参数验证
- **🛠️ MyBatis Plus 集成** - 无缝集成 MyBatis Plus 分页功能

## 📦 依赖

本模块依赖以下核心库：

```xml
<dependencies>
    <!-- Atom 核心模块 -->
    <dependency>
        <groupId>cn.mindit</groupId>
        <artifactId>atom-spring-boot-starter-core</artifactId>
    </dependency>

    <!-- 参数验证 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>

    <!-- OpenAPI 文档 -->
    <dependency>
        <groupId>org.springdoc</groupId>
        <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    </dependency>

    <!-- MyBatis Plus 集成 -->
    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus-extension</artifactId>
        <optional>true</optional>
    </dependency>

    <!-- 开发工具 -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
</dependencies>
```

## 🛠️ 使用方式

### Maven 依赖

```xml
<dependency>
    <groupId>cn.mindit</groupId>
    <artifactId>atom-spring-boot-starter-api</artifactId>
    <version>${atom.version}</version>
</dependency>
```

## 📚 核心功能

### 1. 统一请求响应格式

#### PageDTO - 分页请求参数
标准化的分页请求参数封装：

```java
@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping
    public Result<PageVO<UserVO>> getUsers(PageDTO pageDTO) {
        // 转换为 MyBatis Plus 的 Page 对象
        Page<User> page = pageDTO.toPage();

        // 执行分页查询
        Page<User> userPage = userService.page(page);

        // 转换为响应对象
        return Result.ok(PageVO.of(userPage, UserVO::fromEntity));
    }
}
```

#### PageVO - 分页响应格式
标准化的分页响应格式：

```java
// 创建分页响应
PageVO<UserVO> pageVO = PageVO.of(userPage, UserVO::fromEntity);

// 手动构建分页响应
PageVO<UserVO> pageVO = new PageVO<>();
pageVO.setRecords(userVOList);
pageVO.setTotal(total);
pageVO.setPageNo(pageNo);
pageVO.setPageSize(pageSize);
```

#### 基础数据传输对象

```java
// ID 参数封装
@IdDTO
private Long id;

// ID 响应封装
@IdVO
private Long id;

// 多 ID 参数封装
@IdsDTO
private List<Long> ids;

// 关键词搜索封装
@KeywordsDTO
private String keywords;
```

### 2. 自定义验证注解

#### @Phone - 手机号验证
```java
public class UserCreateDTO {
    @Phone(message = "手机号格式不正确")
    private String phone;
}
```

#### @AnyOfInt - 整数枚举值验证
```java
public class OrderDTO {
    @AnyOfInt(value = {1, 2, 3}, message = "订单状态只能是: 1-待支付, 2-已支付, 3-已完成")
    private Integer status;
}
```

#### @AnyOfLong - 长整数枚举值验证
```java
public class ProductDTO {
    @AnyOfLong(value = {1001L, 1002L, 1003L}, message = "产品类型不正确")
    private Long categoryId;
}
```

#### @AnyOfString - 字符串枚举值验证
```java
public class ConfigDTO {
    @AnyOfString(value = {"ENABLE", "DISABLE"}, message = "配置状态只能是 ENABLE 或 DISABLE")
    private String status;
}
```

#### @AnyOfEnum - 枚举类型验证
```java
public class TaskDTO {
    @AnyOfEnum(enumClass = TaskPriority.class, message = "任务优先级不正确")
    private String priority;
}
```

### 3. 验证分组

#### 预定义验证分组
```java
// 创建操作验证
@Valid @GroupSequence({Create.class, Default.class})
UserCreateDTO createDTO;

// 更新操作验证
@Valid @GroupSequence({Update.class, Default.class})
UserUpdateDTO updateDTO;

// 删除操作验证
@Valid @GroupSequence({Delete.class, Default.class})
IdDTO deleteDTO;

// 查询操作验证
@Valid @GroupSequence({Get.class, Default.class})
IdQueryDTO queryDTO;
```

#### 自定义验证分组使用
```java
public class UserDTO {
    @Null(groups = Create.class, message = "创建时ID必须为空")
    @NotNull(groups = {Update.class, Delete.class}, message = "更新/删除时ID不能为空")
    private Long id;

    @NotBlank(groups = {Create.class, Update.class}, message = "用户名不能为空")
    private String username;
}
```

### 4. 验证工具类

#### ValidatorUtils 使用
```java
@Service
public class UserService {

    @Autowired
    private Validator validator;

    public void createUser(UserDTO userDTO) {
        // 使用注入的验证器进行验证
        ValidatorUtils.validate(validator, userDTO, Create.class);

        // 验证单个属性
        ValidatorUtils.validateProperty(validator, userDTO, "username", Create.class);

        // 验证属性值
        ValidatorUtils.validateValue(validator, UserDTO.class, "email", "test@example.com", Create.class);
    }

    public void validateWithFastFail(UserDTO userDTO) {
        // 使用快速失败验证器
        ValidatorUtils.validate(ValidatorUtils.VALIDATOR_FAST, userDTO);
    }

    public void validateWithAllErrors(UserDTO userDTO) {
        // 使用完整验证器，收集所有错误
        ValidatorUtils.validate(ValidatorUtils.VALIDATOR_ALL, userDTO);
    }
}
```

### 5. OpenAPI 文档集成

#### 控制器文档注解
```java
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "用户管理", description = "用户相关接口")
public class UserController {

    @GetMapping("/{id}")
    @Operation(summary = "获取用户详情", description = "根据用户ID获取用户详细信息")
    @ApiResponse(responseCode = "200", description = "成功",
                 content = @Content(schema = @Schema(implementation = Result.class)))
    public Result<UserVO> getUser(
        @Parameter(description = "用户ID", required = true, example = "1")
        @PathVariable Long id) {
        // 实现逻辑
    }

    @PostMapping
    @Operation(summary = "创建用户", description = "创建新用户")
    public Result<IdVO> createUser(
        @Parameter(description = "用户信息", required = true)
        @Valid @RequestBody UserCreateDTO userCreateDTO) {
        // 实现逻辑
    }
}
```

#### DTO 文档注解
```java
@Schema(description = "用户创建请求")
@Data
public class UserCreateDTO {

    @Schema(description = "用户名", example = "zhangsan", required = true)
    @NotBlank(message = "用户名不能为空")
    @Size(min = 2, max = 20, message = "用户名长度必须在2-20个字符之间")
    private String username;

    @Schema(description = "手机号", example = "13800138000", required = true)
    @Phone(message = "手机号格式不正确")
    private String phone;

    @Schema(description = "用户状态", example = "ACTIVE", allowableValues = {"ACTIVE", "INACTIVE"})
    @AnyOfString(value = {"ACTIVE", "INACTIVE"}, message = "用户状态只能是 ACTIVE 或 INACTIVE")
    private String status;
}
```

### 6. 完整示例

#### 实体定义
```java
@Entity
@Table(name = "sys_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    private String phone;

    private String status;

    // getter/setter...
}
```

#### DTO 定义
```java
@Schema(description = "用户创建请求")
@Data
public class UserCreateDTO {

    @Schema(description = "用户名", example = "zhangsan", required = true)
    @NotBlank(groups = Create.class, message = "用户名不能为空")
    @Size(min = 2, max = 20, groups = {Create.class, Update.class},
          message = "用户名长度必须在2-20个字符之间")
    private String username;

    @Schema(description = "手机号", example = "13800138000")
    @Phone(groups = {Create.class, Update.class}, message = "手机号格式不正确")
    private String phone;

    @Schema(description = "用户状态", example = "ACTIVE", allowableValues = {"ACTIVE", "INACTIVE"})
    @AnyOfString(value = {"ACTIVE", "INACTIVE"},
                  groups = {Create.class, Update.class},
                  message = "用户状态只能是 ACTIVE 或 INACTIVE")
    private String status = "ACTIVE";
}

@Schema(description = "用户响应")
@Data
public class UserVO {

    @Schema(description = "用户ID", example = "1")
    private Long id;

    @Schema(description = "用户名", example = "zhangsan")
    private String username;

    @Schema(description = "手机号", example = "138****8000")
    private String phone;

    @Schema(description = "用户状态", example = "ACTIVE")
    private String status;

    @Schema(description = "创建时间", example = "2023-12-01 10:30:00")
    private LocalDateTime createTime;

    public static UserVO fromEntity(User user) {
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        // 数据脱敏处理
        vo.setPhone(maskPhone(user.getPhone()));
        return vo;
    }

    private String maskPhone(String phone) {
        if (StringUtils.hasText(phone) && phone.length() == 11) {
            return phone.substring(0, 3) + "****" + phone.substring(7);
        }
        return phone;
    }
}
```

#### 控制器实现
```java
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "用户管理", description = "用户相关接口")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @Operation(summary = "分页查询用户", description = "分页查询用户列表")
    public Result<PageVO<UserVO>> getUsers(
        @Parameter(description = "分页参数")
        @ModelAttribute PageDTO pageDTO,

        @Parameter(description = "搜索关键词")
        @ModelAttribute KeywordsDTO keywordsDTO) {

        Page<User> page = userService.page(pageDTO.toPage(),
            buildQueryWrapper(keywordsDTO.getKeywords()));

        return Result.ok(PageVO.of(page, UserVO::fromEntity));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取用户详情", description = "根据用户ID获取用户详细信息")
    public Result<UserVO> getUser(
        @Parameter(description = "用户ID", required = true, example = "1")
        @PathVariable @Valid @IdDTO IdDTO idDTO) {

        User user = userService.getById(idDTO.getId());
        Should.notNull(user, "用户不存在");

        return Result.ok(UserVO.fromEntity(user));
    }

    @PostMapping
    @Operation(summary = "创建用户", description = "创建新用户")
    public Result<IdVO> createUser(
        @Parameter(description = "用户信息", required = true)
        @Valid @GroupSequence({Create.class, Default.class})
        @RequestBody UserCreateDTO userCreateDTO) {

        User user = new User();
        BeanUtils.copyProperties(userCreateDTO, user);
        userService.save(user);

        return Result.ok(IdVO.of(user.getId()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新用户", description = "更新用户信息")
    public Result<Void> updateUser(
        @Parameter(description = "用户ID", required = true, example = "1")
        @PathVariable @Valid @IdDTO IdDTO idDTO,

        @Parameter(description = "用户信息", required = true)
        @Valid @GroupSequence({Update.class, Default.class})
        @RequestBody UserCreateDTO userUpdateDTO) {

        User user = userService.getById(idDTO.getId());
        Should.notNull(user, "用户不存在");

        BeanUtils.copyProperties(userUpdateDTO, user);
        user.setId(idDTO.getId());
        userService.updateById(user);

        return Result.ok();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户", description = "根据用户ID删除用户")
    public Result<Void> deleteUser(
        @Parameter(description = "用户ID", required = true, example = "1")
        @PathVariable @Valid @IdDTO IdDTO idDTO) {

        User user = userService.getById(idDTO.getId());
        Should.notNull(user, "用户不存在");

        userService.removeById(idDTO.getId());

        return Result.ok();
    }

    private LambdaQueryWrapper<User> buildQueryWrapper(String keywords) {
        return new LambdaQueryWrapper<User>()
            .like(StringUtils.hasText(keywords), User::getUsername, keywords)
            .or()
            .like(StringUtils.hasText(keywords), User::getPhone, keywords)
            .orderByDesc(User::getCreateTime);
    }
}
```

## ⚙️ 配置说明

### OpenAPI 配置
```yaml
springdoc:
  # API 文档路径
  api-docs:
    path: /api-docs
  # Swagger UI 路径
  swagger-ui:
    path: /swagger-ui.html
  # 接口信息
  info:
    title: ${spring.application.name:API} 接口文档
    description: 基于 Atom 框架的 RESTful API 接口文档
    version: ${spring.application.version:1.0.0}
    contact:
      name: Catch
      email: catchlife6@163.com
      url: https://github.com/catch6/atom
```

### 验证配置
```yaml
spring:
  # 验证配置
  mvc:
    # 开启参数验证
    throw-exception-if-no-handler-found: true
  # 国际化配置
  messages:
    basename: messages
    encoding: UTF-8
    cache-duration: 3600
```

## 🏗️ 架构设计

### 模块结构
```
atom-spring-boot-starter-api/
├── src/main/java/cn/mindit/atom/api/
│   ├── param/                      # 请求响应数据传输对象
│   │   ├── PageDTO.java           # 分页请求参数
│   │   ├── PageVO.java            # 分页响应格式
│   │   ├── IdDTO.java             # ID 参数封装
│   │   ├── IdVO.java              # ID 响应封装
│   │   ├── IdsDTO.java            # 多 ID 参数封装
│   │   ├── KeywordsDTO.java       # 关键词搜索封装
│   │   └── ItemsVO.java           # 列表数据封装
│   ├── validator/                  # 自定义验证注解
│   │   ├── Phone.java             # 手机号验证注解
│   │   ├── PhoneValidator.java    # 手机号验证器
│   │   ├── AnyOfInt.java          # 整数枚举验证注解
│   │   ├── AnyOfIntValidator.java # 整数枚举验证器
│   │   ├── AnyOfLong.java         # 长整数枚举验证注解
│   │   ├── AnyOfString.java       # 字符串枚举验证注解
│   │   └── AnyOfEnum.java         # 枚举类型验证注解
│   ├── validator/group/            # 验证分组
│   │   ├── Create.java            # 创建操作分组
│   │   ├── Update.java            # 更新操作分组
│   │   ├── Delete.java            # 删除操作分组
│   │   └── Get.java               # 查询操作分组
│   └── util/                       # 工具类
│       └── ValidatorUtils.java    # 验证工具类
└── src/test/java/                  # 测试代码
    └── cn/mindit/atom/api/
        ├── validator/              # 验证器测试
        ├── param/                  # DTO 测试
        └── util/                   # 工具类测试
```

### 设计原则

1. **标准化** - 提供统一的 API 开发标准和规范
2. **可扩展** - 支持自定义验证注解和验证分组
3. **易用性** - 简化常见的 API 开发任务
4. **文档化** - 自动生成和维护 API 文档
5. **类型安全** - 强类型的参数和响应定义

## 🧪 测试

运行测试用例：

```bash
mvn test
```

测试覆盖范围：
- 验证器功能测试
- DTO 转换测试
- 分页功能测试
- 工具类方法测试

## 📖 API 文档访问

启动应用后，可以通过以下地址访问 API 文档：

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api-docs

## 📄 版本说明

- **Spring Boot**: 3.x
- **Java**: 17+
- **SpringDoc OpenAPI**: 2.x
- **License**: Mulan PSL v2

## 🤝 贡献

欢迎提交 Issue 和 Pull Request 来完善这个模块。

## 📞 支持

如有问题或建议，请通过以下方式联系：

- 邮箱: catchlife6@163.com
- GitHub: [https://github.com/catch6/atom](https://github.com/catch6/atom)

---

**注意**: 此模块依赖于 `atom-spring-boot-starter-core` 核心模块，使用时会自动引入核心模块的所有功能。