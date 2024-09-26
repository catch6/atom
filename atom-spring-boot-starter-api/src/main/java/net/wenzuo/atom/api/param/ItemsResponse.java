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

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Catch
 * @since 2024-01-18
 */
@Data
public class ItemsResponse<T> {

	@Schema(description = "列表")
	private List<T> items = new ArrayList<>();

	public static <T> ItemsResponse<T> of() {
		return new ItemsResponse<>();
	}

	public static <T> ItemsResponse<T> of(List<T> items) {
		ItemsResponse<T> response = new ItemsResponse<>();
		response.setItems(items);
		return response;
	}

	public static <T, R> ItemsResponse<T> of(List<R> items, Function<R, T> function) {
		ItemsResponse<T> response = new ItemsResponse<>();
		response.setItems(items.stream().map(function).collect(Collectors.toList()));
		return response;
	}

}
