# Atom Spring Boot Starter Doc

Atom API 文档模块，集成 SpringDoc OpenAPI 自动生成 API 文档和 Swagger UI 交互式测试界面。

## 功能特性

- **自动文档生成** — 基于 SpringDoc OpenAPI，自动扫描 Controller 生成 API 文档
- **Swagger UI** — 内置交互式 API 测试界面
- **FQN Schema** — 使用全限定类名作为 Schema 名称，避免同名类冲突
- **开箱即用** — 添加依赖即生效，无需额外配置

## 快速开始

### 添加依赖

```xml
<dependency>
  <groupId>cn.mindit</groupId>
  <artifactId>atom-spring-boot-starter-doc</artifactId>
</dependency>
```

### 配置

```yaml
atom:
  doc:
    enabled: true # 是否启用文档模块，默认 true
```

### 访问文档

启动应用后访问：

- **Swagger UI**：`http://localhost:8080/swagger-ui/index.html`
- **OpenAPI JSON**：`http://localhost:8080/v3/api-docs`

### 使用示例

```java
@Tag(name = "用户管理")
@RestController
@RequestMapping("/users")
public class UserController {

    @Operation(summary = "查询用户列表")
    @GetMapping
    public Result<PageVO<UserVO>> list(UserQuery query) { ... }

    @Operation(summary = "创建用户")
    @PostMapping
    public Result<IdVO> create(@RequestBody UserDTO dto) { ... }
}
```

## 配置项

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `atom.doc.enabled` | Boolean | `true` | 是否启用文档模块 |

### 生产环境建议

生产环境建议关闭 API 文档：

```yaml
springdoc:
  api-docs:
    enabled: false
  swagger-ui:
    enabled: false
```

## 开源协议

[木兰宽松许可证 v2 (MulanPSL-2.0)](https://license.coscl.org.cn/MulanPSL2)
