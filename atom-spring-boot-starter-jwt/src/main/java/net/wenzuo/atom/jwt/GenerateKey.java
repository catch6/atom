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

package net.wenzuo.atom.jwt;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * @author Catch
 * @since 2023-06-06
 */
public class GenerateKey {

	public static void main(String[] args) {
		// Generate random 256-bit (32-byte) shared secret
		SecureRandom random = new SecureRandom();
		byte[] sharedSecret = new byte[32];
		random.nextBytes(sharedSecret);
		System.out.println(Base64.getEncoder().encodeToString(sharedSecret));
	}

}
