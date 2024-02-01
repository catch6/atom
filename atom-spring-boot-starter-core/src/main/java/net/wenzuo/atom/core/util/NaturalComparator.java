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

package net.wenzuo.atom.core.util;

import java.util.Comparator;

/**
 * 自然排序比较器, 如 aka-2.jpg 应该排在 aka-11.jpg 之前
 * 实现原理: 首先将字符串切分，分为全数字字符串与其他，再各部分一一对应比较，
 * 若皆为全数字字符串，则比较其值，否则按字符比较。
 *
 * @author Catch
 * @since 2023-09-12
 */
public class NaturalComparator implements Comparator<String> {

	private static boolean isDigit(String str) {
		char ch = str.charAt(0);
		return ch >= '0' && ch <= '9';
	}

	static String nextSlice(String str, int index) {
		int length = str.length();
		if (index == length) {
			return null;
		}

		char ch = str.charAt(index);
		if (ch == '.' || ch == ' ') {
			return str.substring(index, index + 1);
		} else if (ch >= '0' && ch <= '9') {
			return str.substring(index, nextNumberBound(str, index + 1));
		} else {
			return str.substring(index, nextOtherBound(str, index + 1));
		}
	}

	private static int nextNumberBound(String str, int index) {
		for (int length = str.length(); index < length; index++) {
			char ch = str.charAt(index);
			if (ch < '0' || ch > '9') {
				break;
			}
		}
		return index;
	}

	private static int nextOtherBound(String str, int index) {
		for (int length = str.length(); index < length; index++) {
			char ch = str.charAt(index);
			if (ch == '.' || ch == ' ' || (ch >= '0' && ch <= '9')) {
				break;
			}
		}
		return index;
	}

	@Override
	public int compare(String o1, String o2) {
		int index1 = 0;
		int index2 = 0;
		while (true) {
			String data1 = nextSlice(o1, index1);
			String data2 = nextSlice(o2, index2);

			if (data1 == null && data2 == null) {
				return 0;
			}
			if (data1 == null) {
				return -1;
			}
			if (data2 == null) {
				return 1;
			}

			index1 += data1.length();
			index2 += data2.length();

			int result;
			if (isDigit(data1) && isDigit(data2)) {
				result = Long.compare(Long.parseLong(data1), Long.parseLong(data2));
				if (result == 0) {
					result = -Integer.compare(data1.length(), data2.length());
				}
			} else {
				result = data1.compareToIgnoreCase(data2);
			}

			if (result != 0) {
				return result;
			}
		}
	}

}
