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

package net.wenzuo.atom.scheduling.service;

import net.wenzuo.atom.scheduling.config.Task;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

/**
 * @author Catch
 * @since 2024-07-17
 */
public interface TaskService {

	void setTask(Task task);

	void setTasks(List<Task> tasks);

	void addTask(Task task);

	void addTasks(List<Task> tasks);

	void removeTask(String taskId);

	void removeTasks(List<String> taskIds);

	void removeTasksPrefix(String prefix);

	void removeTasksSuffix(String suffix);

	void removeTasksContains(String contains);

	void removeTasksRegex(String regex);

	void clear();

	Map<String, ScheduledFuture<?>> getJobMap();

}
