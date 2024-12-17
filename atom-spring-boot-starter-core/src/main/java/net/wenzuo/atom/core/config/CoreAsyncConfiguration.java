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

package net.wenzuo.atom.core.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.wenzuo.atom.core.util.JsonUtils;
import net.wenzuo.atom.core.util.NanoIdUtils;
import org.slf4j.MDC;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.boot.task.ThreadPoolTaskExecutorBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Catch
 * @since 2022-11-10
 */
@Slf4j
@EnableAsync
@ConditionalOnProperty(value = "atom.core.async", matchIfMissing = true)
@RequiredArgsConstructor
@Configuration
public class CoreAsyncConfiguration implements AsyncConfigurer {

    private final TaskExecutionProperties taskExecutionProperties;

    @Override
    public Executor getAsyncExecutor() {
        TaskExecutionProperties.Pool pool = taskExecutionProperties.getPool();
        TaskExecutionProperties.Shutdown shutdown = taskExecutionProperties.getShutdown();
        TaskDecorator taskDecorator = (runnable) -> {
            Map<String, String> contextMap = MDC.getCopyOfContextMap();
            final Map<String, String> ctxMap;
            if (contextMap != null) {
                ctxMap = new HashMap<>(contextMap);
            } else {
                ctxMap = new HashMap<>();
            }
            if (!ctxMap.containsKey("Trace-Id")) {
                String traceId = NanoIdUtils.nanoId();
                ctxMap.put("Trace-Id", traceId);
            }
            return () -> {
                try {
                    MDC.setContextMap(ctxMap);
                    runnable.run();
                } finally {
                    MDC.clear();
                }
            };
        };

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutorBuilder().queueCapacity(pool.getQueueCapacity())
                                                                             .corePoolSize(pool.getCoreSize())
                                                                             .maxPoolSize(pool.getMaxSize())
                                                                             .allowCoreThreadTimeOut(pool.isAllowCoreThreadTimeout())
                                                                             .keepAlive(pool.getKeepAlive())
                                                                             .awaitTermination(shutdown.isAwaitTermination())
                                                                             .awaitTerminationPeriod(shutdown.getAwaitTerminationPeriod())
                                                                             .threadNamePrefix(taskExecutionProperties.getThreadNamePrefix())
                                                                             .taskDecorator(taskDecorator)
                                                                             .build();

        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (t, method, params) -> log.error("Async exception: " + t.getMessage() + ", method: " + method.getName() + ", params: " + JsonUtils.toJson(params), t);
    }

}
