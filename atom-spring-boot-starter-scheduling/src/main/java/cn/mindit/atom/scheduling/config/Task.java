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

package cn.mindit.atom.scheduling.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author Catch
 * @since 2024-07-17
 */
@Data
public class Task {

    public Task() {
    }

    public Task(String id, String cron, String clazz, String method) {
        this.id = id;
        this.cron = cron;
        this.clazz = clazz;
        this.method = method;
    }

    public Task(String id, String cron, String clazz, String method, String param) {
        this.id = id;
        this.cron = cron;
        this.clazz = clazz;
        this.method = method;
        this.param = param;
    }

    /**
     * 任务名称
     */
    @NotBlank(message = "任务ID不能为空")
    private String id;

    /**
     * 任务执行时间
     */
    @NotBlank(message = "任务执行时间不能为空")
    private String cron;

    /**
     * 任务类
     */
    @NotBlank(message = "任务类不能为空")
    private String clazz;

    /**
     * 任务方法
     */
    @NotBlank(message = "任务方法不能为空")
    private String method;

    /**
     * 任务参数
     */
    private String param;

}
