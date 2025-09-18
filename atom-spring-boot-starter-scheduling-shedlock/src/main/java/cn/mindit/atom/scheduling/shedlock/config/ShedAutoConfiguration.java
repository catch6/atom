package cn.mindit.atom.scheduling.shedlock.config;

import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Catch
 * @since 2025-09-18
 */
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "50s")
@RequiredArgsConstructor
@EnableConfigurationProperties(SchedulingShedlockProperties.class)
@ConditionalOnProperty(value = "atom.scheduling.shedlock.enabled", matchIfMissing = true)
public class ShedAutoConfiguration {

}
