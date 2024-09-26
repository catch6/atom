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
public class ItemsResponse<R> {

	@Schema(description = "列表")
	private List<R> items;

	public ItemsResponse() {
	}

	public ItemsResponse(List<R> items) {
		this.items = items;
	}

	public <T> ItemsResponse(List<T> items, Function<T, R> function) {
		if (items == null || items.isEmpty()) {
			this.items = new ArrayList<>();
		} else {
			this.items = items.stream().map(function).collect(Collectors.toList());
		}
	}

}
