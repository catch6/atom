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

package cn.mindit.atom.redis.config;

import lombok.RequiredArgsConstructor;
import cn.mindit.atom.core.util.JsonUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author Catch
 * @since 2023-06-14
 */
@RequiredArgsConstructor
@Configuration
public class RedisConfiguration {

    private final RedisProperties redisProperties;

    @ConditionalOnProperty(value = "atom.redis.string-redis-template", matchIfMissing = true)
    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(factory);

        StringRedisSerializer keySerializer = prefixStringRedisSerializer();
        RedisSerializer<String> stringSerializer = RedisSerializer.string();

        stringRedisTemplate.setKeySerializer(keySerializer);
        stringRedisTemplate.setHashKeySerializer(stringSerializer);

        stringRedisTemplate.setValueSerializer(stringSerializer);
        stringRedisTemplate.setHashValueSerializer(stringSerializer);

        stringRedisTemplate.afterPropertiesSet();

        return stringRedisTemplate;
    }

    @ConditionalOnProperty(value = "atom.redis.redis-template", matchIfMissing = true)
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);

        StringRedisSerializer keySerializer = prefixStringRedisSerializer();
        RedisSerializer<String> hashKeySerializer = RedisSerializer.string();
        RedisSerializer<Object> valueSerializer = jacksonRedisSerializer();

        redisTemplate.setKeySerializer(keySerializer);
        redisTemplate.setHashKeySerializer(hashKeySerializer);

        redisTemplate.setValueSerializer(valueSerializer);
        redisTemplate.setHashValueSerializer(valueSerializer);

        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

    @Bean
    public RedisCacheConfiguration redisCacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                                      .computePrefixWith(name -> name + ":")
                                      .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(prefixStringRedisSerializer()))
                                      .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jacksonRedisSerializer()));
    }

    private RedisSerializer<Object> jacksonRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer(JsonUtils.objectMapper);
    }

    private PrefixStringRedisSerializer prefixStringRedisSerializer() {
        return new PrefixStringRedisSerializer(redisProperties.getPrefix());
    }

}
