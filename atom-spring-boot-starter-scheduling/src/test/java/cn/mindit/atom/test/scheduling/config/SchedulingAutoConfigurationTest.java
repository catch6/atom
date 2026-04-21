package cn.mindit.atom.test.scheduling.config;

import cn.mindit.atom.scheduling.config.SchedulingAutoConfiguration;
import cn.mindit.atom.scheduling.config.SchedulingProperties;
import cn.mindit.atom.scheduling.service.TaskService;
import cn.mindit.atom.scheduling.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.task.TaskSchedulingAutoConfiguration;
import org.springframework.boot.validation.autoconfigure.ValidationAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class SchedulingAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
        .withConfiguration(AutoConfigurations.of(
            ValidationAutoConfiguration.class,
            TaskSchedulingAutoConfiguration.class,
            SchedulingAutoConfiguration.class
        ));

    @Test
    void loadsAllBeansByDefault() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(SchedulingAutoConfiguration.class);
            assertThat(context).hasSingleBean(SchedulingProperties.class);
            assertThat(context).hasSingleBean(TaskServiceImpl.class);
            assertThat(context).hasSingleBean(TaskService.class);
        });
    }

    @Test
    void disabledByProperty() {
        contextRunner
            .withPropertyValues("atom.scheduling.enabled=false")
            .run(context -> {
                assertThat(context).doesNotHaveBean(SchedulingAutoConfiguration.class);
                assertThat(context).doesNotHaveBean(SchedulingProperties.class);
                assertThat(context).doesNotHaveBean(TaskService.class);
            });
    }

    @Test
    void enabledByDefault_whenPropertyMissing() {
        contextRunner.run(context ->
            assertThat(context).hasSingleBean(SchedulingAutoConfiguration.class)
        );
    }

}
