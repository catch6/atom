/*
 * Copyright (c) 2022-2024 Catch(catchlife6@163.com).
 * Atom is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package net.wenzuo.atom.core.core;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.task.TaskSchedulingProperties;
import org.springframework.boot.task.ThreadPoolTaskSchedulerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Catch
 * @since 2022-11-10
 */
@Slf4j
@EnableAsync
@EnableScheduling
@ConditionalOnProperty(value = "atom.core.trace", matchIfMissing = true)
@RequiredArgsConstructor
@Configuration
public class CoreTraceConfiguration {

    private final TaskSchedulingProperties taskSchedulingProperties;
    private final TraceIdTaskDecorator traceIdTaskDecorator;

    /**
     * 定时任务线程池
     */
    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        TaskSchedulingProperties.Pool pool = taskSchedulingProperties.getPool();
        TaskSchedulingProperties.Shutdown shutdown = taskSchedulingProperties.getShutdown();
        String prefix = taskSchedulingProperties.getThreadNamePrefix();

        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskSchedulerBuilder()
            .poolSize(pool.getSize())
            .awaitTermination(shutdown.isAwaitTermination())
            .awaitTerminationPeriod(shutdown.getAwaitTerminationPeriod())
            .threadNamePrefix(prefix)
            .build();
        scheduler.setTaskDecorator(traceIdTaskDecorator);
        scheduler.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());

        scheduler.initialize();
        return scheduler;
    }

}
