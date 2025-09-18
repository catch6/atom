/*
 * Copyright (c) 2022-2025 Catch(catchlife6@163.com).
 * Atom is licensed under Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package cn.mindit.atom.test.core.utils;

import cn.mindit.atom.core.util.LocalDateTimeUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * LocalDateTimeUtils 工具类测试
 *
 * @author Catch
 * @since 2023-08-08
 */
class LocalDateTimeUtilsTest {

    @Test
    @DisplayName("测试 now() 方法")
    void testNow() {
        LocalDateTime result = LocalDateTimeUtils.now();

        assertNotNull(result);
        assertTrue(result.isBefore(LocalDateTime.now().plusSeconds(1)));
        assertTrue(result.isAfter(LocalDateTime.now().minusSeconds(1)));

        // 验证时间被截断到秒
        assertEquals(0, result.getNano());
    }

    @Test
    @DisplayName("测试 now(ZoneId) 方法")
    void testNowWithZoneId() {
        ZoneId zone = ZoneId.of("Asia/Shanghai");
        LocalDateTime result = LocalDateTimeUtils.now(zone);

        assertNotNull(result);
        assertTrue(result.isBefore(LocalDateTime.now(zone).plusSeconds(1)));
        assertTrue(result.isAfter(LocalDateTime.now(zone).minusSeconds(1)));

        // 验证时间被截断到秒
        assertEquals(0, result.getNano());
    }

    @Test
    @DisplayName("测试 now(Clock) 方法")
    void testNowWithClock() {
        Clock fixedClock = Clock.fixed(Instant.parse("2023-08-08T10:15:30Z"), ZoneId.of("UTC"));
        LocalDateTime result = LocalDateTimeUtils.now(fixedClock);

        assertNotNull(result);
        assertEquals(LocalDateTime.of(2023, 8, 8, 10, 15, 30), result);

        // 验证时间被截断到秒
        assertEquals(0, result.getNano());
    }

    @Test
    @DisplayName("测试 now(Clock) 方法 - 系统时钟")
    void testNowWithSystemClock() {
        Clock systemClock = Clock.systemUTC();
        LocalDateTime result = LocalDateTimeUtils.now(systemClock);

        assertNotNull(result);
        assertTrue(result.isBefore(LocalDateTime.now(systemClock).plusSeconds(1)));
        assertTrue(result.isAfter(LocalDateTime.now(systemClock).minusSeconds(1)));

        // 验证时间被截断到秒
        assertEquals(0, result.getNano());
    }

    @Test
    @DisplayName("测试 of(LocalDate, LocalTime) 方法")
    void testOf() {
        LocalDate date = LocalDate.of(2023, 8, 8);
        LocalTime time = LocalTime.of(10, 15, 30, 500_000_000);

        LocalDateTime result = LocalDateTimeUtils.of(date, time);

        assertNotNull(result);
        assertEquals(2023, result.getYear());
        assertEquals(8, result.getMonthValue());
        assertEquals(8, result.getDayOfMonth());
        assertEquals(10, result.getHour());
        assertEquals(15, result.getMinute());
        assertEquals(30, result.getSecond());

        // 验证纳秒被截断
        assertEquals(0, result.getNano());
    }

    @Test
    @DisplayName("测试 of(LocalDate, LocalTime) 方法 - 精确到秒的时间")
    void testOfWithExactSecondTime() {
        LocalDate date = LocalDate.of(2023, 8, 8);
        LocalTime time = LocalTime.of(10, 15, 30);

        LocalDateTime result = LocalDateTimeUtils.of(date, time);

        assertNotNull(result);
        assertEquals(LocalDateTime.of(2023, 8, 8, 10, 15, 30), result);
        assertEquals(0, result.getNano());
    }

    @Test
    @DisplayName("测试 of(LocalDate, LocalTime) 方法 - 空值")
    void testOfWithNullValues() {
        assertThrows(NullPointerException.class, () -> {
            LocalDateTimeUtils.of(null, LocalTime.now());
        });

        assertThrows(NullPointerException.class, () -> {
            LocalDateTimeUtils.of(LocalDate.now(), null);
        });

        assertThrows(NullPointerException.class, () -> {
            LocalDateTimeUtils.of(null, null);
        });
    }

    @Test
    @DisplayName("测试时区一致性")
    void testTimeZoneConsistency() {
        ZoneId utcZone = ZoneId.of("UTC");
        ZoneId shanghaiZone = ZoneId.of("Asia/Shanghai");

        LocalDateTime utcTime = LocalDateTimeUtils.now(utcZone);
        LocalDateTime shanghaiTime = LocalDateTimeUtils.now(shanghaiZone);

        // 验证两个时间戳的差异在合理范围内
        long difference = Math.abs(ChronoUnit.SECONDS.between(utcTime, shanghaiTime));
        assertTrue(difference == 28800, "时间差异应该在1秒内");
    }

    @Test
    @DisplayName("测试时间截断精度")
    void testTimeTruncationPrecision() {
        // 测试带纳秒的时间
        LocalDateTime originalTime = LocalDateTime.of(2023, 8, 8, 10, 15, 30, 999_999_999);
        LocalDate date = originalTime.toLocalDate();
        LocalTime time = originalTime.toLocalTime();

        LocalDateTime result = LocalDateTimeUtils.of(date, time);

        // 验证纳秒被截断为0
        assertEquals(0, result.getNano());
        assertEquals(2023, result.getYear());
        assertEquals(8, result.getMonthValue());
        assertEquals(8, result.getDayOfMonth());
        assertEquals(10, result.getHour());
        assertEquals(15, result.getMinute());
        assertEquals(30, result.getSecond());
    }

    @Test
    @DisplayName("测试不同时区的 now() 方法")
    void testNowWithDifferentTimeZones() {
        ZoneId[] timeZones = {
            ZoneId.of("UTC"),
            ZoneId.of("Asia/Shanghai"),
            ZoneId.of("America/New_York"),
            ZoneId.of("Europe/London")
        };

        for (ZoneId zone : timeZones) {
            LocalDateTime result = LocalDateTimeUtils.now(zone);
            assertNotNull(result);
            assertEquals(0, result.getNano());

            // 验证时间在合理范围内
            LocalDateTime expected = LocalDateTime.now(zone).truncatedTo(ChronoUnit.SECONDS);
            long difference = Math.abs(ChronoUnit.SECONDS.between(result, expected));
            assertTrue(difference <= 1, "时区 " + zone + " 的时间差异应该在1秒内");
        }
    }

    @Test
    @DisplayName("测试固定时钟的精确性")
    void testFixedClockPrecision() {
        Instant fixedInstant = Instant.parse("2023-08-08T10:15:30.123Z");
        Clock fixedClock = Clock.fixed(fixedInstant, ZoneId.of("UTC"));

        LocalDateTime result = LocalDateTimeUtils.now(fixedClock);

        // 验证结果精确到秒，纳秒被截断
        assertEquals(LocalDateTime.of(2023, 8, 8, 10, 15, 30), result);
        assertEquals(0, result.getNano());
    }

    @Test
    @DisplayName("测试偏移时钟")
    void testOffsetClock() {
        Clock baseClock = Clock.systemUTC();
        Clock offsetClock = Clock.offset(baseClock, Duration.ofHours(2));

        LocalDateTime baseTime = LocalDateTimeUtils.now(baseClock);
        LocalDateTime offsetTime = LocalDateTimeUtils.now(offsetClock);

        // 验证偏移时钟的时间差异在合理范围内
        long difference = ChronoUnit.SECONDS.between(baseTime, offsetTime);
        assertTrue(difference >= 7199 && difference <= 7201, "偏移2小时后的时间差异应该在7200秒左右");
    }

    @Test
    @DisplayName("测试 tick 时钟")
    void testTickClock() {
        Clock baseClock = Clock.systemUTC();
        Clock tickClock = Clock.tick(baseClock, Duration.ofSeconds(1));

        LocalDateTime result = LocalDateTimeUtils.now(tickClock);

        assertNotNull(result);
        assertEquals(0, result.getNano());

        // 验证时间与系统时钟的差异在合理范围内
        LocalDateTime systemTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        long difference = Math.abs(ChronoUnit.SECONDS.between(result, systemTime));
        assertTrue(difference == 28800, "Tick时钟与系统时钟的差异应该在1秒内");
    }

}