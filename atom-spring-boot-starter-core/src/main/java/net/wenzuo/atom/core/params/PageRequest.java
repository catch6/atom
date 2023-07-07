/*
 * Copyright (c) 2022-2023 Catch
 * [Atom] is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package net.wenzuo.atom.core.params;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;

/**
 * @author Catch
 * @since 2022-09-08
 */
@Schema(description = "分页请求")
@Data
public class PageRequest {

	/**
	 * 页码
	 */
	@Schema(description = "页码", example = "1", defaultValue = "1")
	@Min(value = 1, message = "页码最小为 1")
	private long pageNumber = 1;
	/**
	 * 每页结果数
	 */
	@Schema(description = "每页结果数", example = "20", defaultValue = "20")
	@Min(value = 1, message = "每页结果数最小为 1")
	private long pageSize = 20;

}
