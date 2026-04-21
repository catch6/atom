# Atom Spring Boot Starter JWT

Atom JWT 认证模块，基于 Nimbus JOSE JWT 提供令牌生成、验证和管理功能，实现基于令牌的身份认证。

## 功能特性

- **令牌生成** — 基于 HMAC-SHA 算法签发 JWT 令牌，支持自定义 claims 和过期时间
- **令牌验证** — 自动解析和校验 JWT 令牌签名
- **密钥生成工具** — 内置 GenerateKey 工具类，一键生成 Base64 编码密钥
- **自动装配** — 自动配置 JWSSigner / JWSVerifier beans

## 快速开始

### 添加依赖

```xml
<dependency>
  <groupId>cn.mindit</groupId>
  <artifactId>atom-spring-boot-starter-jwt</artifactId>
</dependency>
```

### 生成密钥

运行 `GenerateKey` 工具类生成随机密钥：

```bash
mvn exec:java -Dexec.mainClass="cn.mindit.atom.jwt.GenerateKey"
```

### 配置

```yaml
atom:
  jwt:
    enabled: true
    secret: # 将生成的 Base64 密钥填入此处
```

### 使用示例

```java
@RequiredArgsConstructor
@Service
public class AuthService {

    private final JwtService jwtService;

    // 生成令牌
    public String login(String userId) {
        Map<String, Object> claims = Map.of(
            "userId", userId,
            "role", "ADMIN"
        );
        return jwtService.sign(claims, 3600); // 1小时过期
    }

    // 验证令牌
    public Map<String, Object> verify(String token) {
        return jwtService.parse(token);
    }
}
```

## 配置项

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `atom.jwt.enabled` | Boolean | `true` | 是否启用 JWT 模块 |
| `atom.jwt.secret` | String | — | JWT 密钥（Base64 编码），通过 GenerateKey 生成 |

## 开源协议

[木兰宽松许可证 v2 (MulanPSL-2.0)](https://license.coscl.org.cn/MulanPSL2)
