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

package cn.mindit.atom.web.core;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Catch
 * @since 2023-06-28
 */
@Data
@ConfigurationProperties(prefix = "atom.web.logging")
public class LoggingProperties {

    /**
     * 是否启用请求响应日志记录
     */
    private Boolean enabled = true;

    /**
     * 日志过滤包含的路径
     */
    private String[] includePath = {"/**"};

    /**
     * 日志过滤排除的路径
     */
    private String[] excludePath = {};

    /**
     * 日志过滤内部排除的路径, 优先级高于 excludePath, 一般不建议修改
     */
    private String[] internalExcludePath = {"/favicon.ico", "/actuator/**", "/error", "/v3/api-docs/**", "/swagger-ui*/**", "/webjars/**"};

}
