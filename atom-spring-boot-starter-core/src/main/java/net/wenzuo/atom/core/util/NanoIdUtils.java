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

package net.wenzuo.atom.core.util;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 用于生成短 ID
 * 适合对 id 唯一性要求不高, 又不想要太长的 id 的场景(UUID为 36 位)
 * 可通过此网站 <a href="https://zelark.github.io/nano-id-cc/">https://zelark.github.io/nano-id-cc/</a>
 * 查看出现重复 id 所需要的时间
 * 对于此类使用的 ALPHABET 生成 12 位的 id, 按照 5000qps, 至少需要21天的时间，才有1% 的概率发生至少一次碰撞
 * 这个碰撞概率对于某些场景足够使用了, 比如作为 log 的 traceId
 *
 * @author Catch
 * @since 2021-06-30
 */
public abstract class NanoIdUtils {

	private static final char[] ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz_".toCharArray();

	private static final int MASK = ALPHABET.length - 1;

	private static final int DEFAULT_LENGTH = 15;

	public static String nanoId() {
		return nanoId(DEFAULT_LENGTH);
	}

	public static String nanoId(int size) {
		final StringBuilder id = new StringBuilder();
		final byte[] bytes = new byte[size];
		ThreadLocalRandom.current()
						 .nextBytes(bytes);
		for (int i = 0; i < size; ++i) {
			id.append(ALPHABET[bytes[i] & MASK]);
		}
		return id.toString();
	}

}
