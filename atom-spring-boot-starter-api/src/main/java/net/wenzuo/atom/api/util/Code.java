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

package net.wenzuo.atom.api.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Catch
 * @since 2023-08-05
 */
public interface Code {

	Map<Class<? extends Code>, Map<Integer, ? extends Code>> CLASS_CODE_MAP = new ConcurrentHashMap<>();

	@SuppressWarnings("unchecked")
	static <T extends Enum<T> & Code> T getInstance(Class<T> clazz, Integer code) {
		Map<Integer, T> codeMap = (Map<Integer, T>) CLASS_CODE_MAP.computeIfAbsent(clazz, k -> {
			Map<Integer, T> newMap = new ConcurrentHashMap<>();
			for (T value : clazz.getEnumConstants()) {
				newMap.put(value.getCode(), value);
			}
			return newMap;
		});
		return codeMap.get(code);
	}

	Integer getCode();

}
