package cn.mindit.atom.test.core.core;

import cn.mindit.atom.core.core.CoreAutoConfiguration;
import cn.mindit.atom.core.core.CoreJsonConfiguration;
import cn.mindit.atom.core.core.CoreProperties;
import cn.mindit.atom.core.core.CoreTraceConfiguration;
import cn.mindit.atom.core.core.TraceIdTaskDecorator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.task.TaskSchedulingAutoConfiguration;
import org.springframework.boot.jackson.autoconfigure.JacksonAutoConfiguration;
import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import static org.assertj.core.api.Assertions.assertThat;

class CoreAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
        .withConfiguration(AutoConfigurations.of(
            JacksonAutoConfiguration.class,
            TaskSchedulingAutoConfiguration.class,
            CoreAutoConfiguration.class
        ));

    @Test
    void loadsAllBeansByDefault() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(CoreAutoConfiguration.class);
            assertThat(context).hasSingleBean(CoreProperties.class);
            assertThat(context).hasSingleBean(CoreJsonConfiguration.class);
            assertThat(context).hasSingleBean(CoreTraceConfiguration.class);
            assertThat(context).hasSingleBean(TraceIdTaskDecorator.class);
            // Spring Boot 提供 standardJsonMapperBuilderCustomizer，atom 再注册一个
            assertThat(context.getBeanNamesForType(JsonMapperBuilderCustomizer.class))
                .contains("jsonMapperBuilderCustomizer");
            assertThat(context).hasSingleBean(ThreadPoolTaskScheduler.class);
        });
    }

    @Test
    void disabledByProperty() {
        contextRunner
            .withPropertyValues("atom.core.enabled=false")
            .run(context -> {
                assertThat(context).doesNotHaveBean(CoreAutoConfiguration.class);
                assertThat(context).doesNotHaveBean(CoreProperties.class);
            });
    }

    @Test
    void jsonConfigurationDisabledByProperty() {
        contextRunner
            .withPropertyValues("atom.core.json=false")
            .run(context -> {
                assertThat(context).hasSingleBean(CoreAutoConfiguration.class);
                assertThat(context).doesNotHaveBean(CoreJsonConfiguration.class);
                assertThat(context.getBeanNamesForType(JsonMapperBuilderCustomizer.class))
                    .doesNotContain("jsonMapperBuilderCustomizer");
            });
    }

    @Test
    void traceConfigurationDisabledByProperty() {
        contextRunner
            .withPropertyValues("atom.core.trace=false")
            .run(context -> {
                assertThat(context).hasSingleBean(CoreAutoConfiguration.class);
                assertThat(context).doesNotHaveBean(CoreTraceConfiguration.class);
                assertThat(context).doesNotHaveBean(ThreadPoolTaskScheduler.class);
            });
    }

    @Test
    void propertiesFileIsImported() {
        contextRunner.run(context -> {
            // application-core.properties 中定义的值
            assertThat(context.getEnvironment().getProperty("spring.task.execution.thread-name-prefix"))
                .isEqualTo("async-");
            assertThat(context.getEnvironment().getProperty("spring.task.scheduling.thread-name-prefix"))
                .isEqualTo("scheduled-");
        });
    }

}
