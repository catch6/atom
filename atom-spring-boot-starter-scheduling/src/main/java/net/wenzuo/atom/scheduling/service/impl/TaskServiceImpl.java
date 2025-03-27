/*
 * Copyright (c) 2022-2025 Catch(catchlife6@163.com).
 * Atom is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package net.wenzuo.atom.scheduling.service.impl;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.wenzuo.atom.scheduling.config.Task;
import net.wenzuo.atom.scheduling.service.TaskService;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledFuture;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * @author Catch
 * @since 2024-07-17
 */
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {

    private final Validator validator;
    private final ApplicationContext applicationContext;
    private final TaskScheduler taskScheduler;
    private final ConcurrentMap<String, ScheduledFuture<?>> jobMap = new ConcurrentHashMap<>();

    /**
     * 设置单个定时任务，并清除所有现有任务。
     *
     * @param task 要设置的定时任务
     */
    @Override
    public void setTask(Task task) {
        clear();
        internalAddTask(task);
    }

    /**
     * 设置多个定时任务，并清除所有现有任务。
     *
     * @param tasks 要设置的定时任务列表
     */
    @Override
    public void setTasks(List<Task> tasks) {
        clear();
        for (Task task : tasks) {
            internalAddTask(task);
        }
    }

    /**
     * 添加单个定时任务，不影响现有任务。
     *
     * @param task 要添加的定时任务
     */
    @Override
    public void addTask(Task task) {
        internalAddTask(task);
    }

    /**
     * 添加多个定时任务，不影响现有任务。
     *
     * @param tasks 要添加的定时任务列表
     */
    @Override
    public void addTasks(List<Task> tasks) {
        for (Task task : tasks) {
            internalAddTask(task);
        }
    }

    /**
     * 移除指定ID的定时任务。
     *
     * @param taskId 要移除的定时任务ID
     */
    @Override
    public void removeTask(String taskId) {
        internalRemoveTask(taskId);
    }

    /**
     * 移除多个指定ID的定时任务。
     *
     * @param taskIds 要移除的定时任务ID列表
     */
    @Override
    public void removeTasks(List<String> taskIds) {
        for (String taskId : taskIds) {
            internalRemoveTask(taskId);
        }
    }

    /**
     * 移除所有以指定前缀开头的定时任务。
     *
     * @param prefix 要匹配的前缀
     */
    @Override
    public void removeTasksPrefix(String prefix) {
        internalRemoveTasks(key -> key.startsWith(prefix));
    }

    /**
     * 移除所有以指定后缀结尾的定时任务。
     *
     * @param suffix 要匹配的后缀
     */
    @Override
    public void removeTasksSuffix(String suffix) {
        internalRemoveTasks(key -> key.endsWith(suffix));
    }

    /**
     * 移除所有包含指定字符串的定时任务。
     *
     * @param contains 要匹配的字符串
     */
    @Override
    public void removeTasksContains(String contains) {
        internalRemoveTasks(key -> key.contains(contains));
    }

    /**
     * 移除所有匹配指定正则表达式的定时任务。
     *
     * @param regex 要匹配的正则表达式
     */
    @Override
    public void removeTasksRegex(String regex) {
        Pattern pattern = Pattern.compile(regex);
        internalRemoveTasks(key -> pattern.matcher(key).matches());
    }

    /**
     * 清除所有定时任务。
     */
    @Override
    public void clear() {
        jobMap.values().forEach(future -> future.cancel(true));
        jobMap.clear();
    }

    /**
     * 获取当前所有的定时任务映射。
     *
     * @return 定时任务映射
     */
    @Override
    public Map<String, ScheduledFuture<?>> getJobMap() {
        return jobMap;
    }

    private void internalRemoveTasks(Predicate<String> filter) {
        jobMap.entrySet().removeIf(entry -> {
            if (filter.test(entry.getKey())) {
                entry.getValue().cancel(true);
                return true;
            }
            return false;
        });
    }

    private void internalAddTask(Task task) {
        try {
            validateTask(task);

            TaskAction taskAction = resolveTaskAction(task);

            Runnable runnable = createRunnable(task, taskAction);
            CronTrigger trigger = createCronTrigger(task);

            internalRemoveTask(task.getId());

            ScheduledFuture<?> future = taskScheduler.schedule(runnable, trigger);

            jobMap.put(task.getId(), future);
        } catch (ConstraintViolationException | IllegalArgumentException e) {
            log.error("Failed to schedule task '{}': Validation or configuration error - {}", task.getId(), e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error scheduling task '{}': {}", task.getId(), e.getMessage(), e);
        }
    }

    private void validateTask(Task task) {
        Set<ConstraintViolation<Task>> violations = validator.validate(task);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException("Task validation failed for id '" + task.getId() + "'", violations);
        }
    }

    private TaskAction resolveTaskAction(Task task) throws IllegalArgumentException {
        Class<?> clazz;
        try {
            clazz = Class.forName(task.getClazz());
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Task class not found: " + task.getClazz(), e);
        }

        Object bean = applicationContext.getBean(clazz);
        String methodName = task.getMethod();
        boolean expectsParam = StringUtils.hasText(task.getParam());

        Method methodToCall;

        try {
            methodToCall = bean.getClass().getMethod(methodName, String.class);
            return new TaskAction(bean, methodToCall, true);
        } catch (NoSuchMethodException e) {
            try {
                methodToCall = bean.getClass().getMethod(methodName);
                if (expectsParam) {
                    throw new IllegalArgumentException(String.format(
                        "Task '%s' provided parameter '%s', but method '%s' in class '%s' only exists without parameters.",
                        task.getId(), task.getParam(), methodName, task.getClazz()));
                }
                return new TaskAction(bean, methodToCall, false);
            } catch (NoSuchMethodException ex) {
                String errorMsg = String.format(
                    "Task method '%s' not found in class '%s' compatible with %s.",
                    methodName, task.getClazz(), expectsParam ? "String parameter" : "no parameters");
                throw new IllegalArgumentException(errorMsg, ex);
            }
        }
    }

    private Runnable createRunnable(Task task, TaskAction taskAction) {
        return () -> {
            try {
                if (taskAction.requiresParam) {
                    taskAction.method.invoke(taskAction.bean, task.getParam());
                } else {
                    taskAction.method.invoke(taskAction.bean);
                }
            } catch (InvocationTargetException e) {
                log.error("Exception occurred during execution of task: {} - Method: {}#{}",
                    task.getId(), task.getClazz(), task.getMethod(), e.getTargetException());
            } catch (IllegalAccessException e) {
                log.error("Illegal access during execution of task: {} - Method: {}#{}",
                    task.getId(), task.getClazz(), task.getMethod(), e);
            } catch (Exception e) {
                log.error("Unexpected error during execution logic of task: {} - Method: {}#{}",
                    task.getId(), task.getClazz(), task.getMethod(), e);
            }
        };
    }

    private CronTrigger createCronTrigger(Task task) throws IllegalArgumentException {
        try {
            return new CronTrigger(task.getCron(), ZoneId.systemDefault());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid cron expression '" + task.getCron() + "' for task '" + task.getId() + "'", e);
        }
    }

    private void internalRemoveTask(String taskId) {
        ScheduledFuture<?> future = jobMap.remove(taskId);
        if (future != null) {
            future.cancel(true);
        }
    }

    private record TaskAction(Object bean, Method method, boolean requiresParam) {

    }

}
