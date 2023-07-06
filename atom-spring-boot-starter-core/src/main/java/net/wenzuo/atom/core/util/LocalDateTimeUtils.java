/*
 * Copyright (c) 2022-2023 Catch
 * [Atom] is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package net.wenzuo.atom.core.util;

import java.time.*;
import java.time.temporal.ChronoUnit;

/**
 * @author Catch
 * @since 2022-11-02
 */
public abstract class LocalDateTimeUtils {

	public static LocalDateTime now() {
		return LocalDateTime.now()
							.truncatedTo(ChronoUnit.SECONDS);
	}

	public static LocalDateTime now(ZoneId zone) {
		return LocalDateTime.now(zone)
							.truncatedTo(ChronoUnit.SECONDS);
	}

	public static LocalDateTime now(Clock clock) {
		return LocalDateTime.now(clock)
							.truncatedTo(ChronoUnit.SECONDS);
	}

	public static LocalDateTime of(LocalDate date, LocalTime time) {
		return LocalDateTime.of(date, time)
							.truncatedTo(ChronoUnit.SECONDS);
	}

}
