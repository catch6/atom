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

package net.wenzuo.atom.api.param;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Catch
 * @since 2022-09-08
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PageDTO {

	/**
	 * 页码
	 */
	@Schema(description = "页码", example = "1", defaultValue = "1")
	private long pageNo = 1;

	/**
	 * 每页结果数(-1:查询全部)
	 */
	@Schema(description = "每页结果数(-1:查询全部)", example = "15", defaultValue = "15")
	private long pageSize = 15;

	public <T> Page<T> toPage() {
		return new Page<>(getPageNo(), getPageSize());
	}

	public static <T> PageDTO of() {
		return new PageDTO();
	}

	public static <T> PageDTO of(long pageNo, long pageSize) {
		return new PageDTO(pageNo, pageSize);
	}
}
