# Atom Spring Boot Starter Web

Atom Web 应用模块，提供全局异常处理、CORS 配置、请求日志、链路追踪、Excel 导出等 Web 层能力。

## 功能特性

- **全局异常处理** — 自动捕获 BusinessException / ServiceException / 参数校验异常，统一转为 Result 响应
- **CORS 配置** — 支持多路径 CORS 规则配置
- **请求日志** — HTTP 请求/响应日志记录，支持路径包含/排除
- **链路追踪** — 自动为每个请求生成 TraceId，贯穿日志上下文
- **Excel 导出** — 基于 FastExcel 的 Excel 文件导出工具
- **IP 工具** — 客户端真实 IP 获取工具
- **Actuator 集成** — 内置 Spring Boot Actuator 监控支持

> 💡 引入 web 模块会自动包含 `atom-spring-boot-starter-api`，无需重复引入。

## 快速开始

### 添加依赖

```xml
<dependency>
  <groupId>cn.mindit</groupId>
  <artifactId>atom-spring-boot-starter-web</artifactId>
</dependency>
```

### 配置

```yaml
atom:
  web:
    enabled: true
    exception-handler: true # 全局异常拦截
    cors:
      enabled: true
      configs:
        - pattern: /**
          allow-credentials: true
          allowed-origins:
            - "*"
          allowed-headers:
            - "*"
          allowed-methods:
            - "*"
          exposed-headers:
            - "*"
    logging:
      enabled: true
      include-path:
        - /**
      exclude-path:
```

### 使用示例

#### 全局异常处理

无需额外代码，模块启用后自动生效：

```java
// 抛出业务异常 → 自动返回 {"code": 400, "message": "用户不存在"}
throw new BusinessException("用户不存在");

// 抛出服务异常 → 自动返回 {"code": 500, "message": "服务内部错误"} 并记录 error 日志
throw new ServiceException("数据库连接失败");

// 参数校验失败 → 自动返回 {"code": 400, "message": "名称不能为空"}
```

#### Excel 导出

```java
@GetMapping("/export")
public void exportUsers(HttpServletResponse response) {
    List<UserExcel> data = userService.listAll();
    ExcelUtils.export(response, "用户列表", UserExcel.class, data);
}
```

#### IP 工具

```java
String clientIp = IpUtils.getClientIp(request);
```

## 配置项

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `atom.web.enabled` | Boolean | `true` | 是否启用 Web 模块 |
| `atom.web.exception-handler` | Boolean | `true` | 是否启用全局异常拦截 |
| `atom.web.cors.enabled` | Boolean | `true` | 是否启用 CORS |
| `atom.web.cors.configs` | List | — | CORS 规则列表 |
| `atom.web.logging.enabled` | Boolean | `true` | 是否启用请求日志 |
| `atom.web.logging.include-path` | List | `/**` | 日志包含路径 |
| `atom.web.logging.exclude-path` | List | — | 日志排除路径 |

## 开源协议

[木兰宽松许可证 v2 (MulanPSL-2.0)](https://license.coscl.org.cn/MulanPSL2)
