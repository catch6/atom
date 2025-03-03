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

package net.wenzuo.atom.redis.service;

import java.time.Duration;
import java.util.Collection;
import java.util.function.Supplier;

/**
 * @author Catch
 * @since 2025-01-03
 */
public interface CacheService {

    /**
     * 缓存指定 key 的值，如果缓存中不存在，则使用 supplier 函数获取并缓存
     *
     * @param key      缓存键
     * @param supplier 缓存值的提供者
     * @param target   缓存值的类型
     * @return 缓存的值
     */
    <T> T cache(String key, Supplier<T> supplier, Class<T> target);

    /**
     * 缓存指定 key 的值，如果缓存中不存在，则使用 supplier 函数获取并缓存
     *
     * @param key      缓存键
     * @param supplier 缓存值的提供者
     * @param timeout  缓存值的过期时间, 为 null 则使用默认过期时间
     * @param target   缓存值的类型
     * @return 缓存的值
     */
    <T> T cache(String key, Supplier<T> supplier, Duration timeout, Class<T> target);

    /**
     * 缓存指定 key 的值，如果缓存中不存在，则使用 supplier 函数获取并缓存
     * 此方法支持嵌套类型的缓存, 如 {@code List<User>}
     *
     * @param key      缓存键
     * @param supplier 缓存值的提供者
     * @param wrapper  缓存值的包装类
     * @param inners   缓存值的内部类
     * @return 缓存的值
     */
    <T> T cache(String key, Supplier<T> supplier, Class<?> wrapper, Class<?>... inners);

    /**
     * 缓存指定 key 的值，如果缓存中不存在，则使用 supplier 函数获取并缓存
     * 此方法支持嵌套类型的缓存, 如 {@code List<User>}
     *
     * @param key      缓存键
     * @param supplier 缓存值的提供者
     * @param timeout  缓存值的过期时间, 为 null 则使用默认过期时间
     * @param wrapper  缓存值的包装类
     * @param inners   缓存值的内部类
     * @return 缓存的值
     */
    <T> T cache(String key, Supplier<T> supplier, Duration timeout, Class<?> wrapper, Class<?>... inners);

    /**
     * 缓存指定 key 的值，如果缓存中不存在，则使用 supplier 函数获取并缓存, 此方法中 key 用不过期
     *
     * @param key      缓存键
     * @param supplier 缓存值的提供者
     * @param target   缓存值的类型
     * @return 缓存的值
     */
    <T> T keep(String key, Supplier<T> supplier, Class<T> target);

    /**
     * 缓存指定 key 的值，如果缓存中不存在，则使用 supplier 函数获取并缓存, 此方法中 key 用不过期
     * 此方法支持嵌套类型的缓存, 如 {@code List<User>}
     *
     * @param key      缓存键
     * @param supplier 缓存值的提供者
     * @param wrapper  缓存值的包装类
     * @param inners   缓存值的内部类
     * @return 缓存的值
     */
    <T> T keep(String key, Supplier<T> supplier, Class<?> wrapper, Class<?>... inners);

    /**
     * 从缓存中移除指定 key 的值
     *
     * @param key 缓存键
     */
    void evict(String key);

    /**
     * 从缓存中移除指定多个 key 的值
     *
     * @param keys 缓存键集合
     */
    void evict(Collection<String> keys);

    /**
     * 从缓存中移除所有匹配指定模式的 key 的值
     *
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
     * @param pattern 匹配模式
     */
    void evictAll(String pattern);

}
