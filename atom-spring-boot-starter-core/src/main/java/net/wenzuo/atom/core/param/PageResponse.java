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

package net.wenzuo.atom.core.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Catch
 * @since 2022-09-08
 */
@Schema(description = "分页响应")
@Data
public class PageResponse<T> {

	/**
	 * 页码
	 */
	@Schema(description = "页码", example = "1")
	private long pageNumber = 1;
	/**
	 * 每页结果数
	 */
	@Schema(description = "每页结果数", example = "20")
	private long pageSize = 20;
	/**
	 * 总页数
	 */
	@Schema(description = "总页数", example = "0")
	private long totalPage = 0;
	/**
	 * 总结果数
	 */
	@Schema(description = "总结果数", example = "0")
	private long totalRow = 0;
	/**
	 * 数据
	 */
	@Schema(description = "数据", example = "[]")
	private List<T> items = new ArrayList<>();

}
