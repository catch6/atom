package cn.mindit.atom.test.redis.service;

import cn.mindit.atom.core.util.JsonUtils;
import cn.mindit.atom.redis.config.RedisProperties;
import cn.mindit.atom.redis.service.CacheService;
import cn.mindit.atom.redis.service.impl.CacheServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CacheServiceImplTest {

    private static final String NULL_VALUE = "__NULL__";

    @Mock
    private StringRedisTemplate stringRedisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    private RedisProperties redisProperties;
    private CacheService cacheService;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class User {

        private String name;
        private Integer age;

    }

    @BeforeEach
    void setUp() {
        lenient().when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
        redisProperties = new RedisProperties();
        cacheService = new CacheServiceImpl(stringRedisTemplate, redisProperties);
    }

    @Test
    void cacheReturnsCachedValueWhenPresent() {
        User user = new User("Catch", 30);
        when(valueOperations.get("key")).thenReturn(JsonUtils.toJson(user));
        User result = cacheService.cache("key", () -> new User("Other", 0), User.class);
        assertThat(result.getName()).isEqualTo("Catch");
        verify(valueOperations, never()).set(anyString(), anyString(), any(Duration.class));
    }

    @Test
    void cacheCallsSupplierAndStoresWhenMissing() {
        when(valueOperations.get("key")).thenReturn(null);
        User user = new User("Catch", 30);
        User result = cacheService.cache("key", () -> user, User.class);
        assertThat(result.getName()).isEqualTo("Catch");
        verify(valueOperations).set(eq("key"), contains("Catch"), eq(Duration.ofMinutes(30)));
    }

    @Test
    void cacheStoresNullMarkerWhenSupplierReturnsNull() {
        when(valueOperations.get("key")).thenReturn(null);
        User result = cacheService.cache("key", () -> null, User.class);
        assertThat(result).isNull();
        verify(valueOperations).set("key", NULL_VALUE, Duration.ofMinutes(30));
    }

    @Test
    void cacheReturnsNullWhenNullMarkerIsCached() {
        when(valueOperations.get("key")).thenReturn(NULL_VALUE);
        User result = cacheService.cache("key", () -> new User("Other", 0), User.class);
        assertThat(result).isNull();
    }

    @Test
    void cacheWithCustomTimeout() {
        when(valueOperations.get("key")).thenReturn(null);
        User user = new User("Catch", 30);
        Duration timeout = Duration.ofHours(1);
        cacheService.cache("key", () -> user, timeout, User.class);
        verify(valueOperations).set(eq("key"), contains("Catch"), eq(timeout));
    }

    @Test
    void cacheWithNullTimeoutUsesDefault() {
        when(valueOperations.get("key")).thenReturn(null);
        cacheService.cache("key", () -> new User("X", 1), null, User.class);
        verify(valueOperations).set(eq("key"), anyString(), eq(Duration.ofMinutes(30)));
    }

    @Test
    void cacheWithGenericType() {
        List<User> users = List.of(new User("A", 1), new User("B", 2));
        when(valueOperations.get("key")).thenReturn(JsonUtils.toJson(users));
        List<User> result = cacheService.cache("key", List::of, List.class, User.class);
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("A");
    }

    @Test
    void cacheWithGenericTypeAndTimeout() {
        when(valueOperations.get("key")).thenReturn(null);
        List<User> users = List.of(new User("A", 1));
        Duration timeout = Duration.ofMinutes(10);
        cacheService.cache("key", () -> users, timeout, List.class, User.class);
        verify(valueOperations).set(eq("key"), anyString(), eq(timeout));
    }

    @Test
    void keepReturnsCachedValueWhenPresent() {
        User user = new User("Catch", 30);
        when(valueOperations.get("key")).thenReturn(JsonUtils.toJson(user));
        User result = cacheService.keep("key", () -> new User("Other", 0), User.class);
        assertThat(result.getName()).isEqualTo("Catch");
        verify(valueOperations, never()).set(anyString(), anyString());
    }

    @Test
    void keepCallsSupplierAndStoresWithoutExpiration() {
        when(valueOperations.get("key")).thenReturn(null);
        User user = new User("Catch", 30);
        User result = cacheService.keep("key", () -> user, User.class);
        assertThat(result.getName()).isEqualTo("Catch");
        verify(valueOperations).set(eq("key"), contains("Catch"));
        verify(valueOperations, never()).set(anyString(), anyString(), any(Duration.class));
    }

    @Test
    void keepStoresNullMarkerWithoutExpiration() {
        when(valueOperations.get("key")).thenReturn(null);
        User result = cacheService.keep("key", () -> null, User.class);
        assertThat(result).isNull();
        verify(valueOperations).set("key", NULL_VALUE);
    }

    @Test
    void keepReturnsNullWhenNullMarkerIsCached() {
        when(valueOperations.get("key")).thenReturn(NULL_VALUE);
        User result = cacheService.keep("key", () -> new User("Other", 0), User.class);
        assertThat(result).isNull();
    }

    @Test
    void keepWithGenericType() {
        when(valueOperations.get("key")).thenReturn(null);
        List<User> users = List.of(new User("A", 1));
        cacheService.keep("key", () -> users, List.class, User.class);
        verify(valueOperations).set(eq("key"), contains("A"));
    }

    @Test
    void evict() {
        cacheService.evict("key");
        verify(stringRedisTemplate).delete("key");
    }

    @Test
    void evictBatch() {
        List<String> keys = List.of("k1", "k2", "k3");
        cacheService.evict(keys);
        verify(stringRedisTemplate).delete(keys);
    }

    @Test
    void customCacheServiceTimeoutIsUsed() {
        redisProperties.setCacheServiceTimeout(Duration.ofHours(2));
        when(valueOperations.get("key")).thenReturn(null);
        cacheService.cache("key", () -> "value", String.class);
        verify(valueOperations).set(eq("key"), anyString(), eq(Duration.ofHours(2)));
    }

}
