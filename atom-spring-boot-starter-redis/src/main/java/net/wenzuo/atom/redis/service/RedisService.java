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

package net.wenzuo.atom.redis.service;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Catch
 * @since 2023-06-15
 */
public interface RedisService {

	/**
	 * 保存 value
	 *
	 * @param key   键
	 * @param value 值
	 */
	void set(String key, Object value);

	/**
	 * 保存 value 并设置过期时间
	 *
	 * @param key     键
	 * @param value   值
	 * @param timeout 超时时间
	 * @param unit    时间单位
	 */
	void set(String key, Object value, long timeout, TimeUnit unit);

	/**
	 * 保存 value 并设置过期时间
	 *
	 * @param key     键
	 * @param value   值
	 * @param timeout 超时时间
	 */
	void set(String key, Object value, Duration timeout);

	/**
	 * 如果 key 不存在则保存 value, 反之不保存
	 *
	 * @param key   键
	 * @param value 值
	 * @return 是否保存成功
	 */
	Boolean setIfAbsent(String key, Object value);

	/**
	 * 如果 key 不存在则保存 value 并设置过期时间, 反之不保存
	 *
	 * @param key     键
	 * @param value   值
	 * @param timeout 超时时间
	 * @param unit    时间单位
	 * @return 是否保存成功
	 */
	Boolean setIfAbsent(String key, Object value, long timeout, TimeUnit unit);

	/**
	 * 如果 key 不存在则保存 value 并设置过期时间, 反之不保存
	 *
	 * @param key     键
	 * @param value   值
	 * @param timeout 超时时间
	 * @return 是否保存成功
	 */
	Boolean setIfAbsent(String key, Object value, Duration timeout);

	/**
	 * 如果 key 存在则保存 value, 反之不保存
	 *
	 * @param key   键
	 * @param value 值
	 * @return 是否保存成功
	 */
	Boolean setIfPresent(String key, Object value);

	/**
	 * 如果 key 存在则保存 value 并设置过期时间, 反之不保存
	 *
	 * @param key     键
	 * @param value   值
	 * @param timeout 超时时间
	 * @param unit    时间单位
	 * @return 是否保存成功
	 */
	Boolean setIfPresent(String key, Object value, long timeout, TimeUnit unit);

	/**
	 * 如果 key 存在则保存 value 并设置过期时间, 反之不保存
	 *
	 * @param key     键
	 * @param value   值
	 * @param timeout 超时时间
	 * @return 是否保存成功
	 */
	Boolean setIfPresent(String key, Object value, Duration timeout);

	/**
	 * 批量设置 key-value ,使用 redis mset 命令
	 *
	 * @param map key-value
	 */
	void multiSet(Map<String, ?> map);

	/**
	 * 批量设置 key-value ,使用 redis mset 命令, 如果 key 不存在则保存 value, 反之不保存
	 *
	 * @param map key-value
	 */
	Boolean multiSetIfAbsent(Map<String, ?> map);

	/**
	 * 获取 value
	 *
	 * @param key   键
	 * @param clazz 类型
	 * @param <T>   泛型
	 * @return 值
	 */
	<T> T get(String key, Class<T> clazz);

	/**
	 * 获取泛型 value
	 * <p>
	 * 如：{@code List<Object> list = redisService.get(key, List.class, Object.class)}
	 *
	 * @param key     键
	 * @param wrapper 包装类
	 * @param inners  内部类
	 * @param <T>     泛型
	 * @return 值
	 */
	<T> T get(String key, Class<?> wrapper, Class<?>... inners);

	/**
	 * 获取 value 并删除 key
	 *
	 * @param key   键
	 * @param clazz 类型
	 * @param <T>   泛型
	 * @return 值
	 */
	<T> T getAndDelete(String key, Class<T> clazz);

	/**
	 * 获取泛型 value 并删除 key
	 * <p>
	 * 如：{@code List<Object> list = redisService.getAndDelete(key, List.class, Object.class)}
	 *
	 * @param key     键
	 * @param wrapper 包装类
	 * @param inners  内部类
	 * @param <T>     泛型
	 * @return 值
	 */
	<T> T getAndDelete(String key, Class<?> wrapper, Class<?>... inners);

	/**
	 * 获取 value 并设置过期时间
	 *
	 * @param key     键
	 * @param timeout 超时时间
	 * @param unit    时间单位
	 * @param clazz   类型
	 * @param <T>     泛型
	 * @return 值
	 */
	<T> T getAndExpire(String key, long timeout, TimeUnit unit, Class<T> clazz);

	/**
	 * 获取泛型 value 并设置过期时间
	 * <p>
	 * 如：{@code List<Object> list = redisService.getAndExpire(key, 1, TimeUnit.MINUTES, List.class, Object.class)}
	 *
	 * @param key     键
	 * @param timeout 超时时间
	 * @param unit    时间单位
	 * @param wrapper 包装类
	 * @param inners  内部类
	 * @param <T>     泛型
	 * @return 值
	 */
	<T> T getAndExpire(String key, long timeout, TimeUnit unit, Class<?> wrapper, Class<?>... inners);

	/**
	 * 获取 value 并设置过期时间
	 *
	 * @param key     键
	 * @param timeout 超时时间
	 * @param clazz   类型
	 * @return 值
	 */
	<T> T getAndExpire(String key, Duration timeout, Class<T> clazz);

	/**
	 * 获取泛型 value 并设置过期时间
	 * <p>
	 * 如：{@code List<Object> list = redisService.getAndExpire(key, Duration.ofMinutes(1), List.class, Object.class)}
	 *
	 * @param key     键
	 * @param timeout 超时时间
	 * @param wrapper 包装类
	 * @param inners  内部类
	 * @param <T>     泛型
	 * @return 值
	 */
	<T> T getAndExpire(String key, Duration timeout, Class<?> wrapper, Class<?>... inners);

	/**
	 * 获取 value 并持久化 key, 此操作会移除 key 的过期时间
	 *
	 * @param key   键
	 * @param clazz 类型
	 * @param <T>   泛型
	 * @return 值
	 */
	<T> T getAndPersist(String key, Class<T> clazz);

	/**
	 * 获取泛型 value 并持久化 key, 此操作会移除 key 的过期时间
	 * <p>
	 * 如：{@code List<Object> list = redisService.getAndPersist(key, List.class, Object.class)}
	 *
	 * @param key     键
	 * @param wrapper 包装类
	 * @param inners  内部类
	 * @param <T>     泛型
	 * @return 值
	 */
	<T> T getAndPersist(String key, Class<?> wrapper, Class<?>... inners);

	/**
	 * 保存 value 并返回旧的 value
	 *
	 * @param key   键
	 * @param value 新值
	 * @param clazz 类型
	 * @param <T>   泛型
	 * @return 旧值
	 */
	<T> T getAndSet(String key, Object value, Class<T> clazz);

	/**
	 * 保存泛型 value 并返回旧的 value
	 * <p>
	 * 如：{@code List<Object> list = redisService.getAndSet(key, value, List.class, Object.class)}
	 *
	 * @param key     键
	 * @param value   新值
	 * @param wrapper 包装类
	 * @param inners  内部类
	 * @param <T>     泛型
	 * @return 旧值
	 */
	<T> T getAndSet(String key, Object value, Class<?> wrapper, Class<?>... inners);

	/**
	 * 批量获取 value, 按键的顺序排列,不存在则用 null 填充
	 *
	 * @param keys 键集合
	 * @return 值集合
	 */
	<T> List<T> multiGet(Collection<String> keys, Class<T> clazz);

	/**
	 * 按步长为 1 递增 value
	 *
	 * @param key 键
	 * @return 递增后的值
	 */
	Long increment(String key);

	/**
	 * 按步长为 delta 递增 value
	 *
	 * @param key 键
	 * @return 递增后的值
	 */
	Long increment(String key, long delta);

	/**
	 * 按步长为 delta 递增 value
	 *
	 * @param key 键
	 * @return 递增后的值
	 */
	Double increment(String key, double delta);

	/**
	 * 按步长为 delta 递减 value
	 *
	 * @param key 键
	 * @return 递减后的值
	 */
	Long decrement(String key);

	/**
	 * 按步长为 delta 递减 value
	 *
	 * @param key 键
	 * @return 递减后的值
	 */
	Long decrement(String key, long delta);

	/**
	 * 是否存在 key
	 *
	 * @param key 键
	 * @return 是否存在
	 */
	Boolean hasKey(String key);

	/**
	 * 获取 key 集合中存在的 key 数量
	 *
	 * @param keys 键集合
	 * @return 存在的 key 数量
	 */
	Long countExistingKeys(Collection<String> keys);

	/**
	 * 删除 key
	 *
	 * @param key 键
	 * @return 是否删除成功
	 */
	Boolean delete(String key);

	/**
	 * 批量删除 key
	 *
	 * @param keys 键集合
	 * @return 删除的数量
	 */
	Long delete(Collection<String> keys);

	/**
	 * 从 key 空间中取消链接. 与 delete 不同，这里的内存回收是异步发生的
	 *
	 * @param key 键
	 * @return 是否成功
	 */
	Boolean unlink(String key);

	/**
	 * 批量从 key 空间中取消链接. 与 delete 不同，这里的内存回收是异步发生的
	 *
	 * @param keys 键集合
	 * @return 取消链接的数量
	 */
	Long unlink(Collection<String> keys);

	/**
	 * 找到与模式匹配的所有键
	 * <p>
	 * 匹配示例:
	 * <p>
	 * h?llo matches hello, hallo and hxllo
	 * <p>
	 * h*llo matches hllo and heeeello
	 * <p>
	 * h[ae]llo matches hello and hallo, but not hillo
	 * <p>
	 * h[^e]llo matches hallo, hbllo, ... but not hello
	 * <p>
	 * h[a-b]llo matches hallo and hbllo
	 *
	 * @param pattern 模式
	 * @return 匹配的所有键
	 */
	Set<String> keys(String pattern);

	/**
	 * 将 key 重命名为 newKey
	 *
	 * @param oldKey 旧键
	 * @param newKey 新键
	 */
	void rename(String oldKey, String newKey);

	/**
	 * 当 newKey 不存在时，将 key 重命名为 newKey
	 *
	 * @param oldKey 旧键
	 * @param newKey 新键
	 * @return 是否重命名成功
	 */
	Boolean renameIfAbsent(String oldKey, String newKey);

	/**
	 * 设置 key 过期时间
	 *
	 * @param key     键
	 * @param timeout 超时时间
	 * @param unit    时间单位
	 * @return 是否设置成功
	 */
	Boolean expire(String key, long timeout, TimeUnit unit);

	/**
	 * 设置 key 过期时间
	 *
	 * @param key      键
	 * @param duration 超时时间
	 * @return 是否设置成功
	 */
	Boolean expire(String key, Duration duration);

	/**
	 * 设置 key 在指定的时间过期
	 *
	 * @param key  键
	 * @param date 指定的时间过期
	 * @return 是否设置成功
	 */
	Boolean expireAt(String key, Date date);

	/**
	 * 设置 key 在指定的时间过期
	 *
	 * @param key      键
	 * @param expireAt 指定的时间过期
	 * @return 是否设置成功
	 */
	Boolean expireAt(String key, Instant expireAt);

	/**
	 * 设置 key 永不过期
	 *
	 * @param key 键
	 * @return 是否设置成功
	 */
	Boolean persist(String key);

	/**
	 * 获取过期时间, 单位: 秒
	 *
	 * @param key 键
	 * @return 过期时间, 单位: 秒
	 */
	Long getExpire(String key);

	/**
	 * 获取过期时间
	 *
	 * @param key      键
	 * @param timeUnit 时间单位
	 * @return 过期时间
	 */
	Long getExpire(String key, TimeUnit timeUnit);

	/**
	 * Hash 操作, 批量删除 hashKey
	 *
	 * @param key      键
	 * @param hashKeys hash 键集合
	 * @return 删除的数量
	 */
	Long hDelete(String key, String... hashKeys);

	/**
	 * Hash 操作, 判断 hash 键, 是否存在
	 *
	 * @param key     键
	 * @param hashKey hash 键
	 * @return 是否存在
	 */
	Boolean hHasKey(String key, String hashKey);

	/**
	 * Hash 操作, 获取 value
	 *
	 * @param key     键
	 * @param hashKey hash 键
	 * @param clazz   值类型
	 * @param <T>     泛型
	 * @return 值
	 */
	<T> T hGet(String key, String hashKey, Class<T> clazz);

	/**
	 * Hash 操作, 批量获取 value
	 * 返回的 value 顺序与 hashKeys 顺序一致, 不存在则用 null 填充, 存在则返回的是 JsonUtils.toJson() 的结果, 需要自行转换
	 *
	 * @param key      键
	 * @param hashKeys hash 键集合
	 * @return 值集合
	 */
	List<String> hMultiGet(String key, Collection<String> hashKeys);

	/**
	 * Hash 操作, value + 1
	 *
	 * @param key     键
	 * @param hashKey hash 键
	 */
	void hIncrement(String key, String hashKey);

	/**
	 * Hash 操作, value + delta
	 *
	 * @param key     键
	 * @param hashKey hash 键
	 * @param delta   步长
	 */
	void hIncrement(String key, String hashKey, long delta);

	/**
	 * Hash 操作, value + delta
	 *
	 * @param key     键
	 * @param hashKey hash 键
	 * @param delta   步长
	 */
	void hIncrement(String key, String hashKey, double delta);

	/**
	 * Hash 操作, value - 1
	 *
	 * @param key     键
	 * @param hashKey hash 键
	 */
	void hDecrement(String key, String hashKey);

	/**
	 * Hash 操作, value - delta
	 *
	 * @param key     键
	 * @param hashKey hash 键
	 * @param delta   步长
	 */
	void hDecrement(String key, String hashKey, long delta);

	/**
	 * Hash 操作, value - delta
	 *
	 * @param key     键
	 * @param hashKey hash 键
	 * @param delta   步长
	 */
	void hDecrement(String key, String hashKey, double delta);

	/**
	 * Hash 操作, 随机获取 hash 键
	 *
	 * @param key 键
	 * @return 随机 hash 键
	 */
	String hRandomKey(String key);

	/**
	 * Hash 操作, 随机获取 hash 键值对, hash 值返回的是 JsonUtils.toJson() 的结果, 需要自行转换
	 *
	 * @param key 键
	 * @return 随机 hash 键值对
	 */
	Map.Entry<String, String> hRandomEntry(String key);

	/**
	 * Hash 操作, 随机获取指定数量的 hash 键
	 *
	 * @param key   键
	 * @param count hash 键数量
	 * @return hash 键集合
	 */
	List<String> hRandomKeys(String key, long count);

	/**
	 * Hash 操作, 随机获取指定数量的 hash 键值对, hash 值返回的是 JsonUtils.toJson() 的结果, 需要自行转换
	 *
	 * @param key   键
	 * @param count hash 键值对数量
	 * @return hash 键值对集合
	 */
	Map<String, String> hRandomEntries(String key, long count);

	/**
	 * Hash 操作, 获取 hash 键集合
	 *
	 * @param key 键
	 * @return hash 键集合
	 */
	Set<String> hKeys(String key);

	/**
	 * Hash 操作, 获取 value 长度
	 *
	 * @param key     键
	 * @param hashKey hash 键
	 * @return value 长度
	 */
	Long hLengthOfValue(String key, String hashKey);

	/**
	 * Hash 操作, 获取 hash 键数量
	 *
	 * @param key 键
	 * @return hash 键数量
	 */
	Long hSize(String key);

	/**
	 * Hash 操作, 批量保存 hash 键值对
	 *
	 * @param key 键
	 * @param kv  hash 键值对
	 */
	void hPutAll(String key, Map<String, Object> kv);

	/**
	 * Hash 操作, 保存 value
	 *
	 * @param key     键
	 * @param hashKey hash 键
	 * @param value   值
	 * @param <T>     泛型
	 */
	<T> void hPut(String key, String hashKey, Object value);

	/**
	 * Hash 操作, 仅当 hashKey 不存在时，才设置 hashKey 的值
	 *
	 * @param key     键
	 * @param hashKey hash 键
	 * @param value   值
	 * @return 是否保存成功
	 */
	Boolean hPutIfAbsent(String key, String hashKey, Object value);

	/**
	 * Hash 操作, 获取所有 hash 值, hash 值返回的是 JsonUtils.toJson() 的结果, 需要自行转换
	 *
	 * @param key 键
	 * @return hash 值集合
	 */
	List<String> hValues(String key);

	/**
	 * Hash 操作, 获取所有 hash 键值对
	 * 返回的 Map 的 value 是 JsonUtils.toJson() 的结果, 需要自行转换
	 *
	 * @param key 键
	 * @return hash 键值对
	 */
	Map<String, String> hEntries(String key);

}
