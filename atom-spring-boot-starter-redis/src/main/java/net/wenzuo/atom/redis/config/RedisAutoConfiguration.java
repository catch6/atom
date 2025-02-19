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

package net.wenzuo.atom.redis.config;

import lombok.RequiredArgsConstructor;
import net.wenzuo.atom.redis.service.impl.CacheServiceImpl;
import net.wenzuo.atom.redis.service.impl.RedisServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Catch
 * @since 2023-06-14
 */
@Import({RedisConfiguration.class, RedisServiceImpl.class, CacheServiceImpl.class})
@RequiredArgsConstructor
@EnableConfigurationProperties(RedisProperties.class)
@PropertySource("classpath:application-redis.properties")
@ConditionalOnProperty(value = "atom.redis.enabled", matchIfMissing = true)
public class RedisAutoConfiguration {

}
