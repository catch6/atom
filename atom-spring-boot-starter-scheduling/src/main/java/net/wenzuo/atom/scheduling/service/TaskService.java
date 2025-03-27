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

    /**
     * 设置单个定时任务，并清除所有现有任务。
     *
     * @param task 要设置的定时任务
     */
    void setTask(Task task);

    /**
     * 设置多个定时任务，并清除所有现有任务。
     *
     * @param tasks 要设置的定时任务列表
     */
    void setTasks(List<Task> tasks);

    /**
     * 添加单个定时任务，不影响现有任务。
     *
     * @param task 要添加的定时任务
     */
    void addTask(Task task);

    /**
     * 添加多个定时任务，不影响现有任务。
     *
     * @param tasks 要添加的定时任务列表
     */
    void addTasks(List<Task> tasks);

    /**
     * 移除指定ID的定时任务。
     *
     * @param taskId 要移除的定时任务ID
     */
    void removeTask(String taskId);

    /**
     * 移除多个指定ID的定时任务。
     *
     * @param taskIds 要移除的定时任务ID列表
     */
    void removeTasks(List<String> taskIds);

    /**
     * 移除所有以指定前缀开头的定时任务。
     *
     * @param prefix 要匹配的前缀
     */
    void removeTasksPrefix(String prefix);

    /**
     * 移除所有以指定后缀结尾的定时任务。
     *
     * @param suffix 要匹配的后缀
     */
    void removeTasksSuffix(String suffix);

    /**
     * 移除所有包含指定字符串的定时任务。
     *
     * @param contains 要匹配的字符串
     */
    void removeTasksContains(String contains);

    /**
     * 移除所有匹配指定正则表达式的定时任务。
     *
     * @param regex 要匹配的正则表达式
     */
    void removeTasksRegex(String regex);

    /**
     * 清除所有定时任务。
     */
    void clear();

    /**
     * 获取当前所有的定时任务映射。
     *
     * @return 定时任务映射
     */
    Map<String, ScheduledFuture<?>> getJobMap();

}