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

package net.wenzuo.atom.mybatisplus.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.wenzuo.atom.api.param.PageRequest;
import net.wenzuo.atom.api.param.PageResponse;
import org.springframework.lang.NonNull;

import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Catch
 * @since 2023-07-06
 */
public abstract class PageUtils {

	@NonNull
	public static <T> Page<T> toPage(@NonNull PageRequest request) {
		return new Page<>(request.getPageNo(), request.getPageSize());
	}

	@NonNull
	public static <T> PageResponse<T> toPageResponse(@NonNull Page<T> page) {
		PageResponse<T> pageResponse = new PageResponse<>();
		pageResponse.setPageNo(page.getCurrent());
		pageResponse.setPageSize(page.getSize());
		pageResponse.setTotalRow(page.getTotal());
		pageResponse.setTotalPage(page.getPages());
		pageResponse.setItems(page.getRecords());
		return pageResponse;
	}

	@NonNull
	public static <T, R> PageResponse<R> toPageResponse(@NonNull Page<T> page, Function<T, R> function) {
		PageResponse<R> pageResponse = new PageResponse<>();
		pageResponse.setPageNo(page.getCurrent());
		pageResponse.setPageSize(page.getSize());
		pageResponse.setTotalRow(page.getTotal());
		pageResponse.setTotalPage(page.getPages());
		pageResponse.setItems(page.getRecords().stream().map(function).collect(Collectors.toList()));
		return pageResponse;
	}

}
