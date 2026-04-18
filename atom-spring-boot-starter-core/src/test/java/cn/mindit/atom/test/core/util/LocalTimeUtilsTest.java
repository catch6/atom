/*
 * Copyright (c) 2022-2026 Catch(catchlife6@163.com).
 * Atom is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package cn.mindit.atom.test.core.util;

import cn.mindit.atom.core.util.LocalTimeUtils;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;

class LocalTimeUtilsTest {

    @Test
    void nowIsTruncatedToSeconds() {
        assertThat(LocalTimeUtils.now().getNano()).isZero();
    }

    @Test
    void nowWithZoneIsTruncatedToSeconds() {
        assertThat(LocalTimeUtils.now(ZoneId.of("UTC")).getNano()).isZero();
    }

    @Test
    void nowWithClockUsesClockAndTruncates() {
        Instant fixed = Instant.parse("2024-01-15T10:30:45.999999999Z");
        Clock clock = Clock.fixed(fixed, ZoneId.of("UTC"));
        LocalTime now = LocalTimeUtils.now(clock);
        assertThat(now).isEqualTo(LocalTime.of(10, 30, 45));
        assertThat(now.getNano()).isZero();
    }

}
