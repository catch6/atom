/*
 * Copyright (c) 2022-2023 Catch (catchlife6@163.com)
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
import net.wenzuo.atom.core.param.PageRequest;
import net.wenzuo.atom.core.param.PageResponse;
import org.springframework.lang.Nullable;

import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Catch
 * @since 2023-07-06
 */
public abstract class PageUtils {

	@Nullable
	public static <T> Page<T> convert(@Nullable PageRequest source) {
		if (source == null) {
			return null;
		}
		return new Page<>(source.getPageNumber(), source.getPageSize());
	}

	@Nullable
	public static <T> PageResponse<T> convert(@Nullable Page<T> source) {
		if (source == null) {
			return null;
		}
		PageResponse<T> pageResponse = new PageResponse<>();
		pageResponse.setPageNumber(source.getCurrent());
		pageResponse.setPageSize(source.getSize());
		pageResponse.setTotalRow(source.getTotal());
		pageResponse.setTotalPage(source.getPages());
		pageResponse.setItems(source.getRecords());
		return pageResponse;
	}

	@Nullable
	public static <T, R> PageResponse<R> convert(@Nullable Page<T> source, Function<T, R> function) {
		if (source == null) {
			return null;
		}
		PageResponse<R> pageResponse = new PageResponse<>();
		pageResponse.setPageNumber(source.getCurrent());
		pageResponse.setPageSize(source.getSize());
		pageResponse.setTotalRow(source.getTotal());
		pageResponse.setTotalPage(source.getPages());
		pageResponse.setItems(source.getRecords().stream().map(function).collect(Collectors.toList()));
		return pageResponse;
	}

}
