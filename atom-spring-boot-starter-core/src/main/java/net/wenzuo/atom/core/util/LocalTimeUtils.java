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

package net.wenzuo.atom.core.util;

import java.time.Clock;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

/**
 * @author Catch
 * @since 2022-11-02
 */
public abstract class LocalTimeUtils {

	public static LocalTime now() {
		return LocalTime.now().truncatedTo(ChronoUnit.SECONDS);
	}

	public static LocalTime now(ZoneId zone) {
		return LocalTime.now(zone).truncatedTo(ChronoUnit.SECONDS);
	}

	public static LocalTime now(Clock clock) {
		return LocalTime.now(clock).truncatedTo(ChronoUnit.SECONDS);
	}

}
