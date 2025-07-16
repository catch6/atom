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

package cn.mindit.atom.core.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.AsyncConfigurer;

/**
 * @author Catch
 * @since 2022-11-10
 */
@Slf4j
// @EnableAsync
// @ConditionalOnProperty(value = "atom.core.mdc", matchIfMissing = true)
// @RequiredArgsConstructor
// @Configuration
public class CoreAsyncConfiguration implements AsyncConfigurer {

    // private final TaskExecutionProperties taskExecutionProperties;
    //
    // private static final RejectedExecutionHandler REJECTED_EXECUTION_HANDLER = new ThreadPoolExecutor.DiscardOldestPolicy();
    //
    // /**
    //  * 异步任务线程池
    //  * 用于执行普通的异步请求，父线程带有请求链路的MDC标志
    //  */
    // @Bean
    // public Executor commonThreadPool() {
    //     TaskExecutionProperties.Pool pool = taskExecutionProperties.getPool();
    //     TaskExecutionProperties.Shutdown shutdown = taskExecutionProperties.getShutdown();
    //     String prefix = taskExecutionProperties.getThreadNamePrefix();
    //
    //     MdcTaskExecutor executor = new MdcTaskExecutor();
    //
    //     executor.setCorePoolSize(pool.getCoreSize());
    //     executor.setMaxPoolSize(pool.getMaxSize());
    //     executor.setQueueCapacity(pool.getQueueCapacity());
    //     executor.setAllowCoreThreadTimeOut(pool.isAllowCoreThreadTimeout());
    //     executor.setKeepAliveSeconds((int) pool.getKeepAlive().getSeconds());
    //
    //     executor.setWaitForTasksToCompleteOnShutdown(shutdown.isAwaitTermination());
    //     executor.setAwaitTerminationSeconds((int) shutdown.getAwaitTerminationPeriod().getSeconds());
    //
    //     executor.setThreadNamePrefix(prefix);
    //
    //     executor.setRejectedExecutionHandler(REJECTED_EXECUTION_HANDLER);
    //     executor.initialize();
    //     return executor;
    // }
    //
    // /**
    //  * 定时任务线程池
    //  * 用于执行自启动的任务执行，父线程不带有MDC标志，不需要传递，直接设置新的MDC
    //  */
    // @Bean
    // public Executor scheduleThreadPool() {
    //     // ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(8, REJECTED_EXECUTION_HANDLER);
    //     log.info("start init schedule thread pool");
    //     MdcTaskExecutor executor = new MdcTaskExecutor();
    //     executor.setCorePoolSize(10);
    //     executor.setMaxPoolSize(20);
    //     executor.setQueueCapacity(3000);
    //     executor.setKeepAliveSeconds(120);
    //     executor.setThreadNamePrefix("schedule-thread-pool-");
    //     executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());
    //     executor.initialize();
    //     return executor;
    // }
    //
    // @Override
    // public Executor getAsyncExecutor() {
    //     TaskExecutionProperties.Pool pool = taskExecutionProperties.getPool();
    //     TaskExecutionProperties.Shutdown shutdown = taskExecutionProperties.getShutdown();
    //     TaskDecorator taskDecorator = (runnable) -> {
    //         Map<String, String> contextMap = MDC.getCopyOfContextMap();
    //         if (contextMap == null) {
    //             contextMap = new HashMap<>();
    //         }
    //         if (!contextMap.containsKey("tid")) {
    //             String traceId = NanoIdUtils.nanoId();
    //             contextMap.put("tid", traceId);
    //         }
    //         Map<String, String> ctxMap = contextMap;
    //         return () -> {
    //             try {
    //                 MDC.setContextMap(ctxMap);
    //                 runnable.run();
    //             } finally {
    //                 MDC.clear();
    //             }
    //         };
    //     };
    //
    //     ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutorBuilder().queueCapacity(pool.getQueueCapacity())
    //                                                                          .corePoolSize(pool.getCoreSize())
    //                                                                          .maxPoolSize(pool.getMaxSize())
    //                                                                          .allowCoreThreadTimeOut(pool.isAllowCoreThreadTimeout())
    //                                                                          .keepAlive(pool.getKeepAlive())
    //                                                                          .awaitTermination(shutdown.isAwaitTermination())
    //                                                                          .awaitTerminationPeriod(shutdown.getAwaitTerminationPeriod())
    //                                                                          .threadNamePrefix(taskExecutionProperties.getThreadNamePrefix())
    //                                                                          .taskDecorator(taskDecorator)
    //                                                                          .build();
    //
    //     executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    //     executor.initialize();
    //     return executor;
    // }
    //
    // @Override
    // public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
    //     return (t, method, params) -> log.error("Async exception: " + t.getMessage() + ", method: " + method.getName() + ", params: " + JsonUtils.toJson(params), t);
    // }

}
