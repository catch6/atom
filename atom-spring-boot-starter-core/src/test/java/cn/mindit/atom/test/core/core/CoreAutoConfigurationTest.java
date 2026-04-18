/*
 * Copyright (c) 2022-2026 Catch(catchlife6@163.com).
 * Atom is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

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
