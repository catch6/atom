/*
 * Copyright (c) 2022-2023 Catch (catchlife6@163.com)
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
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Catch
 * @since 2023-06-14
 */
@RequiredArgsConstructor
@ComponentScan("net.wenzuo.atom.redis")
@ConfigurationPropertiesScan("net.wenzuo.atom.redis.properties")
@PropertySource("classpath:application-redis.properties")
@ConditionalOnProperty(value = "atom.redis.enabled", matchIfMissing = true)
public class RedisAutoConfiguration {

}
