/*
 * Copyright (c) 2022-2023 Catch
 * [Atom] is licensed under Mulan PSL v2.
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
import java.util.Collection;

/**
 * @author Catch
 * @since 2023-06-15
 */
public interface RedisService {

	/**
	 * 保存 value 及过期时间
	 *
	 * @param key      键
	 * @param value    值
	 * @param duration 超时时间
	 */
	void set(String key, Object value, Duration duration);

	/**
	 * 保存 value
	 *
	 * @param key   键
	 * @param value 值
	 */
	void set(String key, Object value);

	/**
	 * 获取 value
	 *
	 * @param key   键
	 * @param clazz 类型
	 * @param <T>   泛型
	 * @return 属性值
	 */
	<T> T get(String key, Class<T> clazz);

	/**
	 * 获取泛型 value
	 * <p>
	 * 如：{@code List<Object> dtos = get("key", List.class, Object.class)}
	 *
	 * @param key     键
	 * @param wrapper 包装类
	 * @param inners  内部类
	 * @param <T>     泛型
	 * @return 对象
	 */
	<T> T get(String key, Class<?> wrapper, Class<?>... inners);

	/**
	 * 删除 key-value
	 *
	 * @param key 键
	 * @return 是否删除成功
	 */
	Boolean del(String key);

	/**
	 * 批量删除 key-value
	 *
	 * @param keys 键集合
	 * @return 删除的数量
	 */
	Long del(Collection<String> keys);

	/**
	 * 设置过期时间
	 *
	 * @param key      键
	 * @param duration 超时时间
	 * @return 是否设置成功
	 */
	Boolean expire(String key, Duration duration);

	/**
	 * 获取过期时间
	 *
	 * @param key 键
	 * @return 过期时间
	 */
	Long getExpire(String key);

	/**
	 * 判断是否有该属性
	 *
	 * @param key 键
	 * @return 是否存在
	 */
	Boolean hasKey(String key);

	/**
	 * 按delta递增
	 *
	 * @param key   键
	 * @param delta 递增步长
	 * @return 递增后的值
	 */
	Long incr(String key, long delta);

	/**
	 * 按delta递减
	 *
	 * @param key   键
	 * @param delta 递减步长
	 * @return 递减后的值
	 */
	Long decr(String key, long delta);

}
