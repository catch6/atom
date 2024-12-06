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

package net.wenzuo.atom.scheduling.service.impl;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import net.wenzuo.atom.scheduling.config.Task;
import net.wenzuo.atom.scheduling.service.TaskService;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.regex.Pattern;

/**
 * @author Catch
 * @since 2024-07-17
 */
@Slf4j
public class TaskServiceImpl implements TaskService {

	private final Validator validator;
	private final ApplicationContext applicationContext;
	private final TaskScheduler taskScheduler;

	private final Map<String, ScheduledFuture<?>> jobMap;

	public TaskServiceImpl(Validator validator, ApplicationContext applicationContext, TaskScheduler taskScheduler) {
		this.validator = validator;
		this.applicationContext = applicationContext;
		this.taskScheduler = taskScheduler;
		this.jobMap = new HashMap<>();
	}

	@Override
	public void setTask(Task task) {
		for (ScheduledFuture<?> future : jobMap.values()) {
			future.cancel(true);
		}
		jobMap.clear();
		check(task);
		ScheduledFuture<?> future = taskScheduler.schedule(getRunnable(task), getCronTrigger(task));
		jobMap.put(task.getId(), future);
	}

	@Override
	public void setTasks(List<Task> tasks) {
		for (ScheduledFuture<?> future : jobMap.values()) {
			future.cancel(true);
		}
		jobMap.clear();
		for (Task task : tasks) {
			check(task);
			ScheduledFuture<?> future = taskScheduler.schedule(getRunnable(task), getCronTrigger(task));
			jobMap.put(task.getId(), future);
		}
	}

	@Override
	public void addTask(Task task) {
		check(task);
		removeTask(task.getId());
		ScheduledFuture<?> future = taskScheduler.schedule(getRunnable(task), getCronTrigger(task));
		jobMap.put(task.getId(), future);
	}

	@Override
	public void addTasks(List<Task> tasks) {
		for (Task task : tasks) {
			check(task);
			removeTask(task.getId());
			ScheduledFuture<?> future = taskScheduler.schedule(getRunnable(task), getCronTrigger(task));
			jobMap.put(task.getId(), future);
		}
	}

	@Override
	public void removeTask(String taskId) {
		ScheduledFuture<?> future = jobMap.get(taskId);
		if (future != null) {
			future.cancel(true);
			jobMap.remove(taskId);
		}
	}

	@Override
	public void removeTasks(List<String> taskIds) {
		for (String taskId : taskIds) {
			ScheduledFuture<?> future = jobMap.get(taskId);
			if (future != null) {
				future.cancel(true);
				jobMap.remove(taskId);
			}
		}
	}

	@Override
	public void removeTasksPrefix(String prefix) {
		Iterator<Map.Entry<String, ScheduledFuture<?>>> iterator = jobMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, ScheduledFuture<?>> next = iterator.next();
			if (next.getKey().startsWith(prefix)) {
				next.getValue().cancel(true);
				iterator.remove();
			}
		}
	}

	@Override
	public void removeTasksSuffix(String suffix) {
		Iterator<Map.Entry<String, ScheduledFuture<?>>> iterator = jobMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, ScheduledFuture<?>> next = iterator.next();
			if (next.getKey().endsWith(suffix)) {
				next.getValue().cancel(true);
				iterator.remove();
			}
		}
	}

	@Override
	public void removeTasksContains(String contains) {
		Iterator<Map.Entry<String, ScheduledFuture<?>>> iterator = jobMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, ScheduledFuture<?>> next = iterator.next();
			if (next.getKey().contains(contains)) {
				next.getValue().cancel(true);
				iterator.remove();
			}
		}
	}

	@Override
	public void removeTasksRegex(String regex) {
		Pattern pattern = Pattern.compile(regex);
		Iterator<Map.Entry<String, ScheduledFuture<?>>> iterator = jobMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, ScheduledFuture<?>> next = iterator.next();
			if (pattern.matcher(next.getKey()).matches()) {
				next.getValue().cancel(true);
				iterator.remove();
			}
		}
	}

	@Override
	public void clear() {
		for (ScheduledFuture<?> future : jobMap.values()) {
			future.cancel(true);
		}
		jobMap.clear();
	}

	@Override
	public Map<String, ScheduledFuture<?>> getJobMap() {
		return jobMap;
	}

	private Runnable getRunnable(Task task) {
		return () -> {
			Class<?> clazz;
			try {
				clazz = Class.forName(task.getClazz());
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
			Object bean = applicationContext.getBean(clazz);
			Method method;
			try {
				method = bean.getClass().getMethod(task.getMethod(), String.class);
				method.invoke(bean, task.getParam());
			} catch (NoSuchMethodException e) {
				if (task.getParam() != null && !task.getParam().isEmpty()) {
					throw new RuntimeException(e);
				}
				try {
					method = bean.getClass().getMethod(task.getMethod());
					method.invoke(bean);
				} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ex) {
					throw new RuntimeException(ex);
				}
			} catch (InvocationTargetException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		};
	}

	private CronTrigger getCronTrigger(Task task) {
		return new CronTrigger(task.getCron(), ZoneId.systemDefault());
	}

	private void check(Task task) {
		Set<ConstraintViolation<Object>> constraintViolations = validator.validate(task);
		if (!constraintViolations.isEmpty()) {
			throw new ConstraintViolationException(constraintViolations);
		}
		Class<?> clazz;
		try {
			clazz = Class.forName(task.getClazz());
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		Object bean = applicationContext.getBean(clazz);
		try {
			bean.getClass().getMethod(task.getMethod(), String.class);
		} catch (NoSuchMethodException e) {
			if (task.getParam() != null && !task.getParam().isEmpty()) {
				throw new RuntimeException(e);
			}
			try {
				bean.getClass().getMethod(task.getMethod());
			} catch (NoSuchMethodException ex) {
				throw new RuntimeException(ex);
			}
		}
	}

}
