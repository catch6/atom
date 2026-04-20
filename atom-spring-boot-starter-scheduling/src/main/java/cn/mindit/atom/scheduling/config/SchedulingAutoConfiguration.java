package cn.mindit.atom.scheduling.config;

import lombok.RequiredArgsConstructor;
import cn.mindit.atom.scheduling.service.impl.TaskServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.boot.autoconfigure.AutoConfiguration;

/**
 * @author Catch
 * @since 2024-07-17
 */
@Import(TaskServiceImpl.class)
@EnableScheduling
@RequiredArgsConstructor
@EnableConfigurationProperties(SchedulingProperties.class)
@ConditionalOnProperty(value = "atom.scheduling.enabled", matchIfMissing = true)
@AutoConfiguration
public class SchedulingAutoConfiguration {

}
