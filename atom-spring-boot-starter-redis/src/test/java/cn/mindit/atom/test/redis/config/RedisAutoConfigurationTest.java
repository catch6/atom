package cn.mindit.atom.test.redis.config;

import cn.mindit.atom.redis.config.RedisAutoConfiguration;
import cn.mindit.atom.redis.config.RedisConfiguration;
import cn.mindit.atom.redis.config.RedisProperties;
import cn.mindit.atom.redis.service.CacheService;
import cn.mindit.atom.redis.service.RedisService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.data.redis.core.StringRedisTemplate;

import static org.assertj.core.api.Assertions.assertThat;

class RedisAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
        .withConfiguration(AutoConfigurations.of(
            org.springframework.boot.data.redis.autoconfigure.DataRedisAutoConfiguration.class,
            RedisAutoConfiguration.class
        ));

    @Test
    void loadsAllBeansByDefault() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(RedisAutoConfiguration.class);
            assertThat(context).hasSingleBean(RedisProperties.class);
            assertThat(context).hasSingleBean(RedisConfiguration.class);
            assertThat(context).hasSingleBean(RedisService.class);
            assertThat(context).hasSingleBean(CacheService.class);
        });
    }

    @Test
    void disabledByProperty() {
        contextRunner
            .withPropertyValues("atom.redis.enabled=false")
            .run(context -> {
                assertThat(context).doesNotHaveBean(RedisAutoConfiguration.class);
                assertThat(context).doesNotHaveBean(RedisProperties.class);
                assertThat(context).doesNotHaveBean(RedisService.class);
                assertThat(context).doesNotHaveBean(CacheService.class);
            });
    }

    @Test
    void redisServiceDisabledByProperty() {
        contextRunner
            .withPropertyValues("atom.redis.redis-service=false")
            .run(context -> {
                assertThat(context).hasSingleBean(RedisAutoConfiguration.class);
                assertThat(context).doesNotHaveBean(RedisService.class);
                assertThat(context).hasSingleBean(CacheService.class);
            });
    }

    @Test
    void cacheServiceDisabledByProperty() {
        contextRunner
            .withPropertyValues("atom.redis.cache-service=false")
            .run(context -> {
                assertThat(context).hasSingleBean(RedisAutoConfiguration.class);
                assertThat(context).hasSingleBean(RedisService.class);
                assertThat(context).doesNotHaveBean(CacheService.class);
            });
    }

    @Test
    void createsStringRedisTemplateBean() {
        contextRunner.run(context ->
            assertThat(context).hasSingleBean(StringRedisTemplate.class));
    }

    @Test
    void createsRedisTemplateBean() {
        contextRunner.run(context ->
            assertThat(context).hasBean("redisTemplate"));
    }

    @Test
    void propertiesFileIsImported() {
        contextRunner.run(context -> {
            assertThat(context.getEnvironment().getProperty("spring.data.redis.connect-timeout"))
                .isEqualTo("3s");
            assertThat(context.getEnvironment().getProperty("spring.data.redis.timeout"))
                .isEqualTo("3s");
        });
    }

    @Test
    void defaultPropertyValues() {
        contextRunner.run(context -> {
            RedisProperties properties = context.getBean(RedisProperties.class);
            assertThat(properties.getEnabled()).isTrue();
            assertThat(properties.getRedisTemplate()).isTrue();
            assertThat(properties.getRedisService()).isTrue();
            assertThat(properties.getCacheService()).isTrue();
        });
    }

    @Test
    void stringRedisTemplateDisabledByProperty() {
        contextRunner
            .withPropertyValues("atom.redis.string-redis-template=false")
            .run(context -> {
                assertThat(context).hasSingleBean(RedisAutoConfiguration.class);
                // Spring Boot 自己也会注册一个 StringRedisTemplate，但 atom 的不再注册
            });
    }

    @Test
    void redisTemplateDisabledByProperty() {
        contextRunner
            .withPropertyValues("atom.redis.redis-template=false")
            .run(context -> assertThat(context).hasSingleBean(RedisAutoConfiguration.class));
    }

    @Test
    void prefixPropertyApplied() {
        contextRunner
            .withPropertyValues("atom.redis.prefix=test")
            .run(context -> {
                RedisProperties properties = context.getBean(RedisProperties.class);
                assertThat(properties.getPrefix()).isEqualTo("test:");
            });
    }
}
