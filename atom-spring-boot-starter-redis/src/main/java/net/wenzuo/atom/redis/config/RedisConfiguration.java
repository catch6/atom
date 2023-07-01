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

package net.wenzuo.atom.redis.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import net.wenzuo.atom.core.utils.JsonUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author Catch
 * @since 2023-06-14
 */
@Configuration
public class RedisConfiguration {

	@ConditionalOnProperty(value = "atom.redis.redis-template", matchIfMissing = true)
	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(factory);

		StringRedisSerializer stringSerializer = new StringRedisSerializer();
		RedisSerializer<Object> jacksonSerializer = redisSerializer();

		redisTemplate.setKeySerializer(stringSerializer);
		redisTemplate.setHashKeySerializer(stringSerializer);

		redisTemplate.setValueSerializer(jacksonSerializer);
		redisTemplate.setHashValueSerializer(jacksonSerializer);

		redisTemplate.setEnableTransactionSupport(true);
		redisTemplate.afterPropertiesSet();

		RedisCacheConfiguration.defaultCacheConfig()
							   .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jacksonSerializer));
		return redisTemplate;
	}

	@ConditionalOnProperty(value = "atom.redis.cache-manager", matchIfMissing = true)
	@Bean
	public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
		RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);
		RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
																				 .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer()));
		return new RedisCacheManager(redisCacheWriter, redisCacheConfiguration);
	}

	private RedisSerializer<Object> redisSerializer() {
		ObjectMapper objectMapper = JsonUtils.objectMapper();
		objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
		return new GenericJackson2JsonRedisSerializer(objectMapper);
	}

}
