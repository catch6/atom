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

package net.wenzuo.atom.api.param;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Catch
 * @since 2022-09-08
 */
@Data
public class PageResponse<T> {

	/**
	 * 页码
	 */
	@Schema(description = "页码", example = "1")
	private long pageNo = 1;
	/**
	 * 每页结果数(-1:查询全部)
	 */
	@Schema(description = "每页结果数", example = "15")
	private long pageSize = 15;
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

	public static <T> PageResponse<T> of() {
		return new PageResponse<>();
	}

	public static <T> PageResponse<T> of(Page<T> page) {
		PageResponse<T> pageResponse = new PageResponse<>();
		pageResponse.setPageNo(page.getCurrent());
		pageResponse.setPageSize(page.getSize());
		pageResponse.setTotalRow(page.getSize() < 0 ? page.getRecords().size() : page.getTotal());
		pageResponse.setTotalPage(page.getPages());
		pageResponse.setItems(page.getRecords());
		return pageResponse;
	}

	@NonNull
	public static <T, R> PageResponse<R> of(@NonNull Page<T> page, Function<T, R> function) {
		PageResponse<R> pageResponse = new PageResponse<>();
		pageResponse.setPageNo(page.getCurrent());
		pageResponse.setPageSize(page.getSize());
		pageResponse.setTotalRow(page.getSize() < 0 ? page.getRecords().size() : page.getTotal());
		pageResponse.setTotalPage(page.getPages());
		pageResponse.setItems(page.getRecords().stream().map(function).collect(Collectors.toList()));
		return pageResponse;
	}

}
