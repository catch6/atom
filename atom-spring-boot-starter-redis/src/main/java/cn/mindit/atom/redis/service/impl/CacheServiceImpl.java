/*
 * Copyright (c) 2022-2025 Catch(catchlife6@163.com).
 * Atom is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package cn.mindit.atom.redis.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import cn.mindit.atom.core.util.JsonUtils;
import cn.mindit.atom.redis.config.RedisProperties;
import cn.mindit.atom.redis.service.CacheService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;
import java.util.Collection;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Catch
 * @since 2025-01-03
 */
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(value = "atom.redis.cache-service", matchIfMissing = true)
public class CacheServiceImpl implements CacheService {

    private static final String NULL_VALUE = "";

    private final StringRedisTemplate stringRedisTemplate;
    private final RedisProperties redisProperties;

    @Override
    public <T> T cache(String key, Supplier<T> supplier, Class<T> target) {
        return cache(key, supplier, null, json -> JsonUtils.toObject(json, target));
    }

    @Override
    public <T> T cache(String key, Supplier<T> supplier, Duration timeout, Class<T> target) {
        return cache(key, supplier, timeout, json -> JsonUtils.toObject(json, target));
    }

    @Override
    public <T> T cache(String key, Supplier<T> supplier, Class<?> wrapper, Class<?>... inners) {
        return cache(key, supplier, null, json -> JsonUtils.toObject(json, wrapper, inners));
    }

    @Override
    public <T> T cache(String key, Supplier<T> supplier, Duration timeout, Class<?> wrapper, Class<?>... inners) {
        return cache(key, supplier, timeout, json -> JsonUtils.toObject(json, wrapper, inners));
    }

    @Override
    public <T> T keep(String key, Supplier<T> supplier, Class<T> target) {
        return keep(key, supplier, json -> JsonUtils.toObject(json, target));
    }

    @Override
    public <T> T keep(String key, Supplier<T> supplier, Class<?> wrapper, Class<?>... inners) {
        return keep(key, supplier, json -> JsonUtils.toObject(json, wrapper, inners));
    }

    private <T> T cache(String key, Supplier<T> supplier, Duration timeout, Function<String, T> deserializer) {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        String json = ops.get(key);
        if (json != null) {
            return deserializer.apply(json);
        }
        T data = supplier.get();
        if (timeout == null) {
            timeout = redisProperties.getCacheServiceTimeout();
        }
        if (data == null) {
            ops.set(key, NULL_VALUE, timeout);
        } else {
            ops.set(key, JsonUtils.toJson(data), timeout);
        }
        return data;
    }

    private <T> T keep(String key, Supplier<T> supplier, Function<String, T> deserializer) {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        String json = ops.get(key);
        if (json != null) {
            return deserializer.apply(json);
        }
        T data = supplier.get();
        if (data == null) {
            ops.set(key, NULL_VALUE);
        } else {
            ops.set(key, JsonUtils.toJson(data));
        }
        return data;
    }

    @Override
    public void evict(String key) {
        stringRedisTemplate.delete(key);
    }

    @Override
    public void evict(Collection<String> keys) {
        stringRedisTemplate.delete(keys);
    }

    @Override
    public void evictAll(String pattern) {
        Set<String> keys = stringRedisTemplate.keys(pattern);
        stringRedisTemplate.delete(keys);
    }

}
