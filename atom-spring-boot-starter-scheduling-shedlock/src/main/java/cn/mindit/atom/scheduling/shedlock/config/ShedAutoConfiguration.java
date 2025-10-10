package cn.mindit.atom.scheduling.shedlock.config;

import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.redis.spring.RedisLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Catch
 * @since 2025-09-18
 */
@EnableScheduling
@EnableSchedulerLock(defaultLockAtLeastFor = "1s", defaultLockAtMostFor = "10m")
@RequiredArgsConstructor
@EnableConfigurationProperties(SchedulingShedlockProperties.class)
@ConditionalOnProperty(value = "atom.scheduling.shedlock.enabled", matchIfMissing = true)
public class ShedAutoConfiguration {

    @Bean
    public LockProvider lockProvider(RedisConnectionFactory connectionFactory) {
        return new RedisLockProvider(connectionFactory);
    }

}
