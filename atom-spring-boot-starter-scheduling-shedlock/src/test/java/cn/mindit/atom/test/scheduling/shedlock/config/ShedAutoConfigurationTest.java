package cn.mindit.atom.test.scheduling.shedlock.config;

import cn.mindit.atom.scheduling.shedlock.config.ShedAutoConfiguration;
import cn.mindit.atom.scheduling.shedlock.config.SchedulingShedlockProperties;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.redis.spring.RedisLockProvider;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.task.TaskSchedulingAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class ShedAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
        .withConfiguration(AutoConfigurations.of(
            TaskSchedulingAutoConfiguration.class,
            org.springframework.boot.data.redis.autoconfigure.DataRedisAutoConfiguration.class,
            ShedAutoConfiguration.class
        ));

    @Test
    void loadsAllBeansByDefault() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(ShedAutoConfiguration.class);
            assertThat(context).hasSingleBean(SchedulingShedlockProperties.class);
            assertThat(context).hasSingleBean(LockProvider.class);
        });
    }

    @Test
    void disabledByProperty() {
        contextRunner
            .withPropertyValues("atom.scheduling.shedlock.enabled=false")
            .run(context -> {
                assertThat(context).doesNotHaveBean(ShedAutoConfiguration.class);
                assertThat(context).doesNotHaveBean(SchedulingShedlockProperties.class);
                assertThat(context).doesNotHaveBean(LockProvider.class);
            });
    }

    @Test
    void enabledByDefault_whenPropertyMissing() {
        contextRunner.run(context ->
            assertThat(context).hasSingleBean(ShedAutoConfiguration.class)
        );
    }

    @Test
    void createsRedisLockProviderBean() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(LockProvider.class);
            assertThat(context.getBean(LockProvider.class)).isInstanceOf(RedisLockProvider.class);
        });
    }

    @Test
    void defaultPropertyValues() {
        contextRunner.run(context -> {
            SchedulingShedlockProperties properties = context.getBean(SchedulingShedlockProperties.class);
            assertThat(properties.getEnabled()).isTrue();
        });
    }

}
