/*
 * Copyright (c) 2022-2023 Catch(catchlife6@163.com).
 * Atom is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package net.wenzuo.atom.redis.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.wenzuo.atom.core.util.JsonUtils;
import net.wenzuo.atom.redis.service.RedisService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Catch
 * @since 2023-06-15
 */
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(value = "atom.redis.redis-service", matchIfMissing = true)
@Service
public class RedisServiceImpl implements RedisService {

	private final StringRedisTemplate stringRedisTemplate;

	@Override
	public void set(String key, Object value) {
		stringRedisTemplate.opsForValue().set(key, JsonUtils.toJson(value));
	}

	@Override
	public void set(String key, Object value, long timeout, TimeUnit unit) {
		stringRedisTemplate.opsForValue().set(key, JsonUtils.toJson(value), timeout, unit);
	}

	@Override
	public void set(String key, Object value, Duration duration) {
		stringRedisTemplate.opsForValue().set(key, JsonUtils.toJson(value), duration);
	}

	@Override
	public Boolean setIfAbsent(String key, Object value) {
		return stringRedisTemplate.opsForValue().setIfAbsent(key, JsonUtils.toJson(value));
	}

	@Override
	public Boolean setIfAbsent(String key, Object value, long timeout, TimeUnit unit) {
		return stringRedisTemplate.opsForValue().setIfAbsent(key, JsonUtils.toJson(value), timeout, unit);
	}

	@Override
	public Boolean setIfAbsent(String key, Object value, Duration timeout) {
		return stringRedisTemplate.opsForValue().setIfAbsent(key, JsonUtils.toJson(value), timeout);
	}

	@Override
	public Boolean setIfPresent(String key, Object value) {
		return stringRedisTemplate.opsForValue().setIfPresent(key, JsonUtils.toJson(value));
	}

	@Override
	public Boolean setIfPresent(String key, Object value, long timeout, TimeUnit unit) {
		return stringRedisTemplate.opsForValue().setIfPresent(key, JsonUtils.toJson(value), timeout, unit);
	}

	@Override
	public Boolean setIfPresent(String key, Object value, Duration timeout) {
		return stringRedisTemplate.opsForValue().setIfPresent(key, JsonUtils.toJson(value), timeout);
	}

	@Override
	public void multiSet(Map<String, ?> map) {
		Map<String, String> stringMap = new HashMap<>();
		map.forEach((k, v) -> stringMap.put(k, JsonUtils.toJson(v)));
		stringRedisTemplate.opsForValue().multiSet(stringMap);
	}

	@Override
	public Boolean multiSetIfAbsent(Map<String, ?> map) {
		Map<String, String> stringMap = new HashMap<>();
		map.forEach((k, v) -> stringMap.put(k, JsonUtils.toJson(v)));
		return stringRedisTemplate.opsForValue().multiSetIfAbsent(stringMap);
	}

	@Override
	public <T> T get(String key, Class<T> clazz) {
		return JsonUtils.toObject(stringRedisTemplate.opsForValue().get(key), clazz);
	}

	@Override
	public <T> T get(String key, Class<?> wrapper, Class<?>... inners) {
		return JsonUtils.toObject(stringRedisTemplate.opsForValue().get(key), wrapper, inners);
	}

	@Override
	public <T> T getAndDelete(String key, Class<T> clazz) {
		return JsonUtils.toObject(stringRedisTemplate.opsForValue().getAndDelete(key), clazz);
	}

	@Override
	public <T> T getAndDelete(String key, Class<?> wrapper, Class<?>... inners) {
		return JsonUtils.toObject(stringRedisTemplate.opsForValue().getAndDelete(key), wrapper, inners);
	}

	@Override
	public <T> T getAndExpire(String key, long timeout, TimeUnit unit, Class<T> clazz) {
		return JsonUtils.toObject(stringRedisTemplate.opsForValue().getAndExpire(key, timeout, unit), clazz);
	}

	@Override
	public <T> T getAndExpire(String key, long timeout, TimeUnit unit, Class<?> wrapper, Class<?>... inners) {
		return JsonUtils.toObject(stringRedisTemplate.opsForValue().getAndExpire(key, timeout, unit), wrapper, inners);
	}

	@Override
	public <T> T getAndExpire(String key, Duration timeout, Class<T> clazz) {
		return JsonUtils.toObject(stringRedisTemplate.opsForValue().getAndExpire(key, timeout), clazz);
	}

	@Override
	public <T> T getAndExpire(String key, Duration timeout, Class<?> wrapper, Class<?>... inners) {
		return JsonUtils.toObject(stringRedisTemplate.opsForValue().getAndExpire(key, timeout), wrapper, inners);
	}

	@Override
	public <T> T getAndPersist(String key, Class<T> clazz) {
		return JsonUtils.toObject(stringRedisTemplate.opsForValue().getAndPersist(key), clazz);
	}

	@Override
	public <T> T getAndPersist(String key, Class<?> wrapper, Class<?>... inners) {
		return JsonUtils.toObject(stringRedisTemplate.opsForValue().getAndPersist(key), wrapper, inners);
	}

	@Override
	public <T> T getAndSet(String key, Object value, Class<T> clazz) {
		return JsonUtils.toObject(stringRedisTemplate.opsForValue().getAndSet(key, JsonUtils.toJson(value)), clazz);
	}

	@Override
	public <T> T getAndSet(String key, Object value, Class<?> wrapper, Class<?>... inners) {
		return JsonUtils.toObject(stringRedisTemplate.opsForValue().getAndSet(key, JsonUtils.toJson(value)), wrapper, inners);
	}

	@Override
	public <T> List<T> multiGet(Collection<String> keys, Class<T> clazz) {
		List<String> list = stringRedisTemplate.opsForValue().multiGet(keys);
		if (list == null) {
			return null;
		}
		return list.stream().map(v -> JsonUtils.toObject(v, clazz)).collect(Collectors.toList());
	}

	@Override
	public Long increment(String key) {
		return stringRedisTemplate.opsForValue().increment(key);
	}

	@Override
	public Long increment(String key, long delta) {
		return stringRedisTemplate.opsForValue().increment(key, delta);
	}

	@Override
	public Double increment(String key, double delta) {
		return stringRedisTemplate.opsForValue().increment(key, delta);
	}

	@Override
	public Long decrement(String key) {
		return stringRedisTemplate.opsForValue().decrement(key);
	}

	@Override
	public Long decrement(String key, long delta) {
		return stringRedisTemplate.opsForValue().decrement(key, delta);
	}

	@Override
	public Boolean hasKey(String key) {
		return stringRedisTemplate.hasKey(key);
	}

	@Override
	public Long countExistingKeys(Collection<String> keys) {
		return stringRedisTemplate.countExistingKeys(keys);
	}

	@Override
	public Boolean delete(String key) {
		return stringRedisTemplate.delete(key);
	}

	@Override
	public Long delete(Collection<String> keys) {
		return stringRedisTemplate.delete(keys);
	}

	@Override
	public Boolean unlink(String key) {
		return stringRedisTemplate.unlink(key);
	}

	@Override
	public Long unlink(Collection<String> keys) {
		return stringRedisTemplate.unlink(keys);
	}

	@Override
	public Set<String> keys(String pattern) {
		return stringRedisTemplate.keys(pattern);
	}

	@Override
	public void rename(String oldKey, String newKey) {
		stringRedisTemplate.rename(oldKey, newKey);
	}

	@Override
	public Boolean renameIfAbsent(String oldKey, String newKey) {
		return stringRedisTemplate.renameIfAbsent(oldKey, newKey);
	}

	@Override
	public Boolean expire(String key, long timeout, TimeUnit unit) {
		return stringRedisTemplate.expire(key, timeout, unit);
	}

	@Override
	public Boolean expire(String key, Duration duration) {
		return stringRedisTemplate.expire(key, duration);
	}

	@Override
	public Boolean expireAt(String key, Date date) {
		return stringRedisTemplate.expireAt(key, date);
	}

	@Override
	public Boolean expireAt(String key, Instant expireAt) {
		return stringRedisTemplate.expireAt(key, expireAt);
	}

	@Override
	public Boolean persist(String key) {
		return stringRedisTemplate.persist(key);
	}

	@Override
	public Long getExpire(String key) {
		return stringRedisTemplate.getExpire(key);
	}

	@Override
	public Long getExpire(String key, TimeUnit timeUnit) {
		return stringRedisTemplate.getExpire(key, timeUnit);
	}

}
