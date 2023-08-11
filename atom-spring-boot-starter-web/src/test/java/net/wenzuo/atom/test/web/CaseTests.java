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

package net.wenzuo.atom.test.web;

import org.junit.jupiter.api.Test;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

/**
 * @author Catch
 * @since 2023-06-28
 */
class CaseTests {

	private static final PathMatcher PATH_MATCHER = new AntPathMatcher();

	@Test
	void patternTest() {
		String pattern1 = "/api/**/user";
		String pattern2 = "/web/**/user";
		String path = "/api/user";
		String combine = PATH_MATCHER.combine(pattern1, pattern2);
		System.out.println(combine);
		boolean match = PATH_MATCHER.match(pattern1, path);
		System.out.println(match);
	}

}
