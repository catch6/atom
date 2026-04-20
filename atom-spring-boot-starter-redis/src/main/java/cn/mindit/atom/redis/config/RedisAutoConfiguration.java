package cn.mindit.atom.redis.config;

import lombok.RequiredArgsConstructor;
import cn.mindit.atom.redis.service.impl.CacheServiceImpl;
import cn.mindit.atom.redis.service.impl.RedisServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.boot.autoconfigure.AutoConfiguration;

/**
 * @author Catch
 * @since 2023-06-14
 */
@Import({RedisConfiguration.class, RedisServiceImpl.class, CacheServiceImpl.class})
@RequiredArgsConstructor
@EnableConfigurationProperties(RedisProperties.class)
@PropertySource("classpath:application-redis.properties")
@ConditionalOnProperty(value = "atom.redis.enabled", matchIfMissing = true)
@AutoConfiguration
public class RedisAutoConfiguration {

}
