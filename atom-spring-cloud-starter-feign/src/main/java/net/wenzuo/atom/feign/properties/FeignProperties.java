/*
 * Copyright (c) 2022-2023 Catch(catchlife6@163.com).
 * Atom is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package net.wenzuo.atom.feign.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Catch
 * @since 2021-06-29
 */
@Data
@ConfigurationProperties(prefix = "atom.feign")
public class FeignProperties {

	/**
	 * 是否启用 Mono Feign 模块功能
	 */
	private Boolean enabled = true;
	/**
	 * 是否启用Feign请求响应日志记录
	 */
	private Boolean logging = true;
	/**
	 * 是否启用 Feign 第三方响应结果异常处理
	 */
	private Boolean exceptionHandler = true;

}
