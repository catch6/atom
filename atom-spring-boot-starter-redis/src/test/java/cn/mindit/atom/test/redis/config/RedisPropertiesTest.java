package cn.mindit.atom.test.redis.config;

import cn.mindit.atom.redis.config.RedisProperties;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class RedisPropertiesTest {

    @Test
    void defaultValues() {
        RedisProperties properties = new RedisProperties();
        assertThat(properties.getEnabled()).isTrue();
        assertThat(properties.getRedisTemplate()).isTrue();
        assertThat(properties.getRedisService()).isTrue();
        assertThat(properties.getCacheService()).isTrue();
        assertThat(properties.getCacheServiceTimeout()).isEqualTo(Duration.ofMinutes(30));
        assertThat(properties.getDelimiter()).isEqualTo(":");
    }

    @Test
    void prefixReturnsEmptyWhenNull() {
        RedisProperties properties = new RedisProperties();
        assertThat(properties.getPrefix()).isEmpty();
    }

    @Test
    void prefixAppendsDelimiter() {
        RedisProperties properties = new RedisProperties();
        properties.setPrefix("myapp");
        assertThat(properties.getPrefix()).isEqualTo("myapp:");
    }

    @Test
    void prefixDoesNotDuplicateDelimiter() {
        RedisProperties properties = new RedisProperties();
        properties.setPrefix("myapp:");
        assertThat(properties.getPrefix()).isEqualTo("myapp:");
    }

    @Test
    void prefixWithCustomDelimiter() {
        RedisProperties properties = new RedisProperties();
        properties.setDelimiter("-");
        properties.setPrefix("myapp");
        assertThat(properties.getPrefix()).isEqualTo("myapp-");
    }

    @Test
    void prefixAlreadyEndsWithCustomDelimiter() {
        RedisProperties properties = new RedisProperties();
        properties.setDelimiter("-");
        properties.setPrefix("myapp-");
        assertThat(properties.getPrefix()).isEqualTo("myapp-");
    }
}
