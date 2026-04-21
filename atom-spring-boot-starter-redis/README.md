# Atom Spring Boot Starter Redis

Atom Redis 缓存模块，提供 Jackson 序列化的 RedisTemplate、CacheManager 和便捷的 Redis 操作 API。

## 功能特性

- **RedisTemplate 增强** — 自动配置 `RedisTemplate<String, Object>`，使用 Jackson JSON 序列化
- **CacheManager** — 自动配置基于 Jackson 序列化的 Spring CacheManager
- **RedisService** — 封装常用 Redis 操作的服务接口（get/set、Hash、过期、自增等）
- **CacheService** — 简化缓存操作的服务接口
- **连接池** — 默认集成 commons-pool2 连接池

## 快速开始

### 添加依赖

```xml
<dependency>
  <groupId>cn.mindit</groupId>
  <artifactId>atom-spring-boot-starter-redis</artifactId>
</dependency>
```

### 配置

```yaml
spring:
  data:
    redis:
      host: localhost
      port: 6379
      password:
      database: 0

atom:
  redis:
    enabled: true
    redis-template: true  # 启用 RedisTemplate<String, Object>
    cache-manager: true   # 启用 CacheManager
```

### 使用示例

#### RedisService

```java
@RequiredArgsConstructor
@Service
public class UserCacheService {

    private final RedisService redisService;

    // 字符串操作
    public void cacheUser(String userId, UserVO user) {
        redisService.set("user:" + userId, user, 3600); // 1小时过期
    }

    public UserVO getUser(String userId) {
        return (UserVO) redisService.get("user:" + userId);
    }

    // Hash 操作
    public void cacheUserField(String userId, String field, Object value) {
        redisService.hSet("user:" + userId, field, value);
    }

    // 自增
    public Long incrementLoginCount(String userId) {
        return redisService.increment("login:count:" + userId);
    }

    // 过期管理
    public void setExpire(String key, long seconds) {
        redisService.expire(key, seconds);
    }
}
```

#### Spring Cache 注解

启用 `cache-manager: true` 后可直接使用 Spring Cache 注解：

```java
@Cacheable(value = "users", key = "#id")
public UserVO getUserById(Long id) { ... }

@CacheEvict(value = "users", key = "#id")
public void deleteUser(Long id) { ... }
```

## 配置项

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `atom.redis.enabled` | Boolean | `true` | 是否启用 Redis 模块 |
| `atom.redis.redis-template` | Boolean | `true` | 启用 RedisTemplate（Jackson 序列化） |
| `atom.redis.cache-manager` | Boolean | `true` | 启用 CacheManager（Jackson 序列化） |

## 开源协议

[木兰宽松许可证 v2 (MulanPSL-2.0)](https://license.coscl.org.cn/MulanPSL2)
