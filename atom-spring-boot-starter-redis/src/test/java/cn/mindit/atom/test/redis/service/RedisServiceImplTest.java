package cn.mindit.atom.test.redis.service;

import cn.mindit.atom.core.util.JsonUtils;
import cn.mindit.atom.redis.service.RedisService;
import cn.mindit.atom.redis.service.impl.RedisServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RedisServiceImplTest {

    @Mock
    private StringRedisTemplate stringRedisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @Mock
    private HashOperations<String, Object, Object> hashOperations;

    private RedisService redisService;

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
        lenient().when(stringRedisTemplate.opsForHash()).thenReturn(hashOperations);
        redisService = new RedisServiceImpl(stringRedisTemplate);
    }

    // JsonUtils.toJson 对 CharSequence/Number 直接返回 toString()，对象才 JSON 序列化

    @Test
    void setString() {
        redisService.set("key", "value");
        verify(valueOperations).set("key", "value");
    }

    @Test
    void setObject() {
        User user = new User("Catch", 30);
        redisService.set("user", user);
        verify(valueOperations).set(eq("user"), contains("Catch"));
    }

    @Test
    void setWithTimeoutAndUnit() {
        redisService.set("key", "value", 60, TimeUnit.SECONDS);
        verify(valueOperations).set("key", "value", 60L, TimeUnit.SECONDS);
    }

    @Test
    void setWithDuration() {
        redisService.set("key", "value", Duration.ofMinutes(5));
        verify(valueOperations).set("key", "value", Duration.ofMinutes(5));
    }

    @Test
    void setIfAbsent() {
        when(valueOperations.setIfAbsent("key", "value")).thenReturn(true);
        Boolean result = redisService.setIfAbsent("key", "value");
        assertThat(result).isTrue();
    }

    @Test
    void setIfAbsentWithTimeout() {
        when(valueOperations.setIfAbsent("key", "value", 60, TimeUnit.SECONDS)).thenReturn(true);
        Boolean result = redisService.setIfAbsent("key", "value", 60, TimeUnit.SECONDS);
        assertThat(result).isTrue();
    }

    @Test
    void setIfAbsentWithDuration() {
        when(valueOperations.setIfAbsent("key", "value", Duration.ofSeconds(60))).thenReturn(false);
        Boolean result = redisService.setIfAbsent("key", "value", Duration.ofSeconds(60));
        assertThat(result).isFalse();
    }

    @Test
    void setIfPresent() {
        when(valueOperations.setIfPresent("key", "value")).thenReturn(true);
        Boolean result = redisService.setIfPresent("key", "value");
        assertThat(result).isTrue();
    }

    @Test
    void setIfPresentWithTimeout() {
        when(valueOperations.setIfPresent("key", "value", 60, TimeUnit.SECONDS)).thenReturn(false);
        Boolean result = redisService.setIfPresent("key", "value", 60, TimeUnit.SECONDS);
        assertThat(result).isFalse();
    }

    @Test
    void setIfPresentWithDuration() {
        when(valueOperations.setIfPresent("key", "value", Duration.ofMinutes(1))).thenReturn(true);
        Boolean result = redisService.setIfPresent("key", "value", Duration.ofMinutes(1));
        assertThat(result).isTrue();
    }

    @Test
    void multiSet() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("k1", "v1");
        map.put("k2", 123);
        redisService.multiSet(map);
        verify(valueOperations).multiSet(argThat(m ->
            "v1".equals(m.get("k1")) && "123".equals(m.get("k2"))
        ));
    }

    @Test
    void multiSetIfAbsent() {
        when(valueOperations.multiSetIfAbsent(anyMap())).thenReturn(true);
        Map<String, Object> map = Map.of("k1", "v1");
        Boolean result = redisService.multiSetIfAbsent(map);
        assertThat(result).isTrue();
    }

    @Test
    void getReturnsDeserializedObject() {
        User user = new User("Catch", 30);
        when(valueOperations.get("user")).thenReturn(JsonUtils.toJson(user));
        User result = redisService.get("user", User.class);
        assertThat(result.getName()).isEqualTo("Catch");
        assertThat(result.getAge()).isEqualTo(30);
    }

    @Test
    void getReturnsNullWhenKeyNotFound() {
        when(valueOperations.get("missing")).thenReturn(null);
        User result = redisService.get("missing", User.class);
        assertThat(result).isNull();
    }

    @Test
    void getWithGenericType() {
        List<User> users = List.of(new User("A", 1), new User("B", 2));
        when(valueOperations.get("users")).thenReturn(JsonUtils.toJson(users));
        List<User> result = redisService.get("users", List.class, User.class);
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("A");
    }

    @Test
    void getStringValue() {
        when(valueOperations.get("key")).thenReturn("hello");
        String result = redisService.get("key", String.class);
        assertThat(result).isEqualTo("hello");
    }

    @Test
    void getAndDelete() {
        when(valueOperations.getAndDelete("key")).thenReturn("hello");
        String result = redisService.getAndDelete("key", String.class);
        assertThat(result).isEqualTo("hello");
    }

    @Test
    void getAndDeleteWithGenericType() {
        List<String> list = List.of("a", "b");
        when(valueOperations.getAndDelete("key")).thenReturn(JsonUtils.toJson(list));
        List<String> result = redisService.getAndDelete("key", List.class, String.class);
        assertThat(result).containsExactly("a", "b");
    }

    @Test
    void getAndExpireWithUnit() {
        User user = new User("Catch", 30);
        when(valueOperations.getAndExpire("key", 60, TimeUnit.SECONDS)).thenReturn(JsonUtils.toJson(user));
        User result = redisService.getAndExpire("key", 60, TimeUnit.SECONDS, User.class);
        assertThat(result.getName()).isEqualTo("Catch");
    }

    @Test
    void getAndExpireWithUnitAndGenericType() {
        List<String> list = List.of("a", "b");
        when(valueOperations.getAndExpire("key", 60, TimeUnit.SECONDS)).thenReturn(JsonUtils.toJson(list));
        List<String> result = redisService.getAndExpire("key", 60, TimeUnit.SECONDS, List.class, String.class);
        assertThat(result).containsExactly("a", "b");
    }

    @Test
    void getAndExpireWithDuration() {
        when(valueOperations.getAndExpire("key", Duration.ofMinutes(1))).thenReturn("val");
        String result = redisService.getAndExpire("key", Duration.ofMinutes(1), String.class);
        assertThat(result).isEqualTo("val");
    }

    @Test
    void getAndExpireWithDurationAndGenericType() {
        List<String> list = List.of("x");
        when(valueOperations.getAndExpire("key", Duration.ofMinutes(1))).thenReturn(JsonUtils.toJson(list));
        List<String> result = redisService.getAndExpire("key", Duration.ofMinutes(1), List.class, String.class);
        assertThat(result).containsExactly("x");
    }

    @Test
    void getAndPersist() {
        when(valueOperations.getAndPersist("key")).thenReturn("val");
        String result = redisService.getAndPersist("key", String.class);
        assertThat(result).isEqualTo("val");
    }

    @Test
    void getAndPersistWithGenericType() {
        List<String> list = List.of("a");
        when(valueOperations.getAndPersist("key")).thenReturn(JsonUtils.toJson(list));
        List<String> result = redisService.getAndPersist("key", List.class, String.class);
        assertThat(result).containsExactly("a");
    }

    @Test
    void getAndSet() {
        User oldUser = new User("Old", 20);
        when(valueOperations.getAndSet(eq("key"), anyString())).thenReturn(JsonUtils.toJson(oldUser));
        User result = redisService.getAndSet("key", new User("New", 30), User.class);
        assertThat(result.getName()).isEqualTo("Old");
    }

    @Test
    void getAndSetWithGenericType() {
        List<String> oldList = List.of("old");
        when(valueOperations.getAndSet(eq("key"), anyString())).thenReturn(JsonUtils.toJson(oldList));
        List<String> result = redisService.getAndSet("key", List.of("new"), List.class, String.class);
        assertThat(result).containsExactly("old");
    }

    @Test
    void multiGet() {
        List<String> keys = List.of("k1", "k2");
        List<String> values = List.of(JsonUtils.toJson(new User("A", 1)), JsonUtils.toJson(new User("B", 2)));
        when(valueOperations.multiGet(keys)).thenReturn(values);
        List<User> result = redisService.multiGet(keys, User.class);
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("A");
    }

    @Test
    void multiGetReturnsNullWhenEmpty() {
        when(valueOperations.multiGet(anyCollection())).thenReturn(null);
        List<User> result = redisService.multiGet(List.of("k1"), User.class);
        assertThat(result).isNull();
    }

    @Test
    void increment() {
        when(valueOperations.increment("counter")).thenReturn(1L);
        assertThat(redisService.increment("counter")).isEqualTo(1L);
    }

    @Test
    void incrementByDelta() {
        when(valueOperations.increment("counter", 5L)).thenReturn(5L);
        assertThat(redisService.increment("counter", 5L)).isEqualTo(5L);
    }

    @Test
    void incrementByDoubleDelta() {
        when(valueOperations.increment("counter", 1.5)).thenReturn(1.5);
        assertThat(redisService.increment("counter", 1.5)).isEqualTo(1.5);
    }

    @Test
    void decrement() {
        when(valueOperations.decrement("counter")).thenReturn(-1L);
        assertThat(redisService.decrement("counter")).isEqualTo(-1L);
    }

    @Test
    void decrementByDelta() {
        when(valueOperations.decrement("counter", 3L)).thenReturn(-3L);
        assertThat(redisService.decrement("counter", 3L)).isEqualTo(-3L);
    }

    @Test
    void hasKey() {
        when(stringRedisTemplate.hasKey("key")).thenReturn(true);
        assertThat(redisService.hasKey("key")).isTrue();
    }

    @Test
    void hasKeyReturnsFalse() {
        when(stringRedisTemplate.hasKey("missing")).thenReturn(false);
        assertThat(redisService.hasKey("missing")).isFalse();
    }

    @Test
    void countExistingKeys() {
        when(stringRedisTemplate.countExistingKeys(anyCollection())).thenReturn(2L);
        assertThat(redisService.countExistingKeys(List.of("k1", "k2", "k3"))).isEqualTo(2L);
    }

    @Test
    void delete() {
        when(stringRedisTemplate.delete("key")).thenReturn(true);
        assertThat(redisService.delete("key")).isTrue();
    }

    @Test
    void deleteBatch() {
        when(stringRedisTemplate.delete(anyCollection())).thenReturn(3L);
        assertThat(redisService.delete(List.of("k1", "k2", "k3"))).isEqualTo(3L);
    }

    @Test
    void unlink() {
        when(stringRedisTemplate.unlink("key")).thenReturn(true);
        assertThat(redisService.unlink("key")).isTrue();
    }

    @Test
    void unlinkBatch() {
        when(stringRedisTemplate.unlink(anyCollection())).thenReturn(2L);
        assertThat(redisService.unlink(List.of("k1", "k2"))).isEqualTo(2L);
    }

    @Test
    void rename() {
        redisService.rename("old", "new");
        verify(stringRedisTemplate).rename("old", "new");
    }

    @Test
    void renameIfAbsent() {
        when(stringRedisTemplate.renameIfAbsent("old", "new")).thenReturn(true);
        assertThat(redisService.renameIfAbsent("old", "new")).isTrue();
    }

    @Test
    void expireWithUnit() {
        when(stringRedisTemplate.expire("key", 60, TimeUnit.SECONDS)).thenReturn(true);
        assertThat(redisService.expire("key", 60, TimeUnit.SECONDS)).isTrue();
    }

    @Test
    void expireWithDuration() {
        when(stringRedisTemplate.expire("key", Duration.ofMinutes(1))).thenReturn(true);
        assertThat(redisService.expire("key", Duration.ofMinutes(1))).isTrue();
    }

    @Test
    void expireAtWithDate() {
        Date date = new Date();
        when(stringRedisTemplate.expireAt("key", date)).thenReturn(true);
        assertThat(redisService.expireAt("key", date)).isTrue();
    }

    @Test
    void persist() {
        when(stringRedisTemplate.persist("key")).thenReturn(true);
        assertThat(redisService.persist("key")).isTrue();
    }

    @Test
    void getExpire() {
        when(stringRedisTemplate.getExpire("key")).thenReturn(120L);
        assertThat(redisService.getExpire("key")).isEqualTo(120L);
    }

    @Test
    void getExpireWithUnit() {
        when(stringRedisTemplate.getExpire("key", TimeUnit.MINUTES)).thenReturn(2L);
        assertThat(redisService.getExpire("key", TimeUnit.MINUTES)).isEqualTo(2L);
    }

    // Hash 操作

    @Test
    void hPut() {
        User user = new User("Catch", 30);
        String json = JsonUtils.toJson(user);
        redisService.hPut("hash", "user", user);
        verify(hashOperations).put("hash", "user", json);
    }

    @Test
    void hPutString() {
        redisService.hPut("hash", "field", "value");
        verify(hashOperations).put("hash", "field", "value");
    }

    @Test
    void hPutIfAbsent() {
        when(hashOperations.putIfAbsent(eq("hash"), eq("field"), anyString())).thenReturn(true);
        Boolean result = redisService.hPutIfAbsent("hash", "field", "value");
        assertThat(result).isTrue();
    }

    @Test
    void hPutAll() {
        Map<String, Object> kv = Map.of("f1", "v1", "f2", 123);
        redisService.hPutAll("hash", kv);
        verify(hashOperations).putAll(eq("hash"), argThat(m -> m.size() == 2));
    }

    @Test
    void hGet() {
        User user = new User("Catch", 30);
        when(hashOperations.get("hash", "field")).thenReturn(JsonUtils.toJson(user));
        User result = redisService.hGet("hash", "field", User.class);
        assertThat(result.getName()).isEqualTo("Catch");
    }

    @Test
    void hGetReturnsNullWhenMissing() {
        when(hashOperations.get("hash", "missing")).thenReturn(null);
        User result = redisService.hGet("hash", "missing", User.class);
        assertThat(result).isNull();
    }

    @Test
    void hDelete() {
        when(hashOperations.delete(eq("hash"), any())).thenReturn(2L);
        Long result = redisService.hDelete("hash", "f1", "f2");
        assertThat(result).isEqualTo(2L);
    }

    @Test
    void hHasKey() {
        when(hashOperations.hasKey("hash", "field")).thenReturn(true);
        assertThat(redisService.hHasKey("hash", "field")).isTrue();
    }

    @Test
    void hIncrement() {
        redisService.hIncrement("hash", "counter");
        verify(hashOperations).increment("hash", "counter", 1);
    }

    @Test
    void hIncrementByLong() {
        redisService.hIncrement("hash", "counter", 5L);
        verify(hashOperations).increment("hash", "counter", 5L);
    }

    @Test
    void hIncrementByDouble() {
        redisService.hIncrement("hash", "counter", 1.5);
        verify(hashOperations).increment("hash", "counter", 1.5);
    }

    @Test
    void hDecrement() {
        redisService.hDecrement("hash", "counter");
        verify(hashOperations).increment("hash", "counter", -1);
    }

    @Test
    void hDecrementByLong() {
        redisService.hDecrement("hash", "counter", 3L);
        verify(hashOperations).increment("hash", "counter", -3L);
    }

    @Test
    void hDecrementByDouble() {
        redisService.hDecrement("hash", "counter", 2.5);
        verify(hashOperations).increment("hash", "counter", -2.5);
    }

    @Test
    void hRandomKey() {
        when(hashOperations.randomKey("hash")).thenReturn("field1");
        assertThat(redisService.hRandomKey("hash")).isEqualTo("field1");
    }

    @Test
    void hRandomEntry() {
        Map.Entry<Object, Object> entry = new AbstractMap.SimpleEntry<>("field", "value");
        when(hashOperations.randomEntry("hash")).thenReturn(entry);
        Map.Entry<String, String> result = redisService.hRandomEntry("hash");
        assertThat(result.getKey()).isEqualTo("field");
        assertThat(result.getValue()).isEqualTo("value");
    }

    @Test
    void hRandomEntryReturnsNullForEmptyHash() {
        when(hashOperations.randomEntry("hash")).thenReturn(null);
        assertThat(redisService.hRandomEntry("hash")).isNull();
    }

    @Test
    void hRandomKeys() {
        when(hashOperations.randomKeys("hash", 2)).thenReturn(List.of("f1", "f2"));
        List<String> result = redisService.hRandomKeys("hash", 2);
        assertThat(result).containsExactly("f1", "f2");
    }

    @Test
    void hRandomKeysReturnsNullWhenEmpty() {
        when(hashOperations.randomKeys("hash", 2)).thenReturn(null);
        assertThat(redisService.hRandomKeys("hash", 2)).isNull();
    }

    @Test
    void hRandomEntries() {
        Map<Object, Object> entries = new LinkedHashMap<>();
        entries.put("f1", "v1");
        entries.put("f2", "v2");
        when(hashOperations.randomEntries("hash", 2)).thenReturn(entries);
        Map<String, String> result = redisService.hRandomEntries("hash", 2);
        assertThat(result).containsEntry("f1", "v1").containsEntry("f2", "v2");
    }

    @Test
    void hRandomEntriesReturnsNullWhenEmpty() {
        when(hashOperations.randomEntries("hash", 2)).thenReturn(null);
        assertThat(redisService.hRandomEntries("hash", 2)).isNull();
    }

    @Test
    void hKeys() {
        when(hashOperations.keys("hash")).thenReturn(Set.of("f1", "f2"));
        Set<String> result = redisService.hKeys("hash");
        assertThat(result).containsExactlyInAnyOrder("f1", "f2");
    }

    @Test
    void hSize() {
        when(hashOperations.size("hash")).thenReturn(3L);
        assertThat(redisService.hSize("hash")).isEqualTo(3L);
    }

    @Test
    void hLengthOfValue() {
        when(hashOperations.lengthOfValue("hash", "field")).thenReturn(10L);
        assertThat(redisService.hLengthOfValue("hash", "field")).isEqualTo(10L);
    }

    @Test
    void hValues() {
        when(hashOperations.values("hash")).thenReturn(List.of("v1", "v2"));
        List<String> result = redisService.hValues("hash");
        assertThat(result).containsExactly("v1", "v2");
    }

    @Test
    void hEntries() {
        Map<Object, Object> entries = new LinkedHashMap<>();
        entries.put("f1", "v1");
        entries.put("f2", "v2");
        when(hashOperations.entries("hash")).thenReturn(entries);
        Map<String, String> result = redisService.hEntries("hash");
        assertThat(result).containsEntry("f1", "v1").containsEntry("f2", "v2");
    }

    @Test
    void hMultiGet() {
        when(hashOperations.multiGet(eq("hash"), anyCollection())).thenReturn(List.of("v1", "v2"));
        List<String> result = redisService.hMultiGet("hash", List.of("f1", "f2"));
        assertThat(result).containsExactly("v1", "v2");
    }
}
