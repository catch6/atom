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

import cn.mindit.atom.core.util.LocalTimeUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.*;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * LocalTimeUtils 工具类测试
 *
 * @author Catch
 * @since 2023-08-08
 */
class LocalTimeUtilsTest {

    @Test
    @DisplayName("测试 now() 方法")
    void testNow() {
        LocalTime result = LocalTimeUtils.now();
        
        assertNotNull(result);
        assertTrue(result.isBefore(LocalTime.now().plusSeconds(1)));
        assertTrue(result.isAfter(LocalTime.now().minusSeconds(1)));
        
        // 验证时间被截断到秒
        assertEquals(0, result.getNano());
    }

    @Test
    @DisplayName("测试 now(ZoneId) 方法")
    void testNowWithZoneId() {
        ZoneId zone = ZoneId.of("Asia/Shanghai");
        LocalTime result = LocalTimeUtils.now(zone);
        
        assertNotNull(result);
        assertTrue(result.isBefore(LocalTime.now(zone).plusSeconds(1)));
        assertTrue(result.isAfter(LocalTime.now(zone).minusSeconds(1)));
        
        // 验证时间被截断到秒
        assertEquals(0, result.getNano());
    }

    @Test
    @DisplayName("测试 now(Clock) 方法")
    void testNowWithClock() {
        Clock fixedClock = Clock.fixed(Instant.parse("2023-08-08T10:15:30Z"), ZoneId.of("UTC"));
        LocalTime result = LocalTimeUtils.now(fixedClock);
        
        assertNotNull(result);
        assertEquals(LocalTime.of(10, 15, 30), result);
        
        // 验证时间被截断到秒
        assertEquals(0, result.getNano());
    }

    @Test
    @DisplayName("测试 now(Clock) 方法 - 系统时钟")
    void testNowWithSystemClock() {
        Clock systemClock = Clock.systemUTC();
        LocalTime result = LocalTimeUtils.now(systemClock);
        
        assertNotNull(result);
        assertTrue(result.isBefore(LocalTime.now(systemClock).plusSeconds(1)));
        assertTrue(result.isAfter(LocalTime.now(systemClock).minusSeconds(1)));
        
        // 验证时间被截断到秒
        assertEquals(0, result.getNano());
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
            LocalTime result = LocalTimeUtils.now(zone);
            assertNotNull(result);
            assertEquals(0, result.getNano());
            
            // 验证时间在合理范围内
            LocalTime expected = LocalTime.now(zone).truncatedTo(ChronoUnit.SECONDS);
            long difference = Math.abs(ChronoUnit.SECONDS.between(result, expected));
            assertTrue(difference <= 1, "时区 " + zone + " 的时间差异应该在1秒内");
        }
    }

    @Test
    @DisplayName("测试固定时钟的精确性")
    void testFixedClockPrecision() {
        Instant fixedInstant = Instant.parse("2023-08-08T10:15:30.123Z");
        Clock fixedClock = Clock.fixed(fixedInstant, ZoneId.of("UTC"));
        
        LocalTime result = LocalTimeUtils.now(fixedClock);
        
        // 验证结果精确到秒，纳秒被截断
        assertEquals(LocalTime.of(10, 15, 30), result);
        assertEquals(0, result.getNano());
    }

    @Test
    @DisplayName("测试偏移时钟")
    void testOffsetClock() {
        Clock baseClock = Clock.systemUTC();
        Clock offsetClock = Clock.offset(baseClock, Duration.ofHours(2));
        
        LocalTime baseTime = LocalTimeUtils.now(baseClock);
        LocalTime offsetTime = LocalTimeUtils.now(offsetClock);
        
        // 验证偏移时钟的时间差异在合理范围内
        long difference = ChronoUnit.SECONDS.between(baseTime, offsetTime);
        // 由于LocalTime是时间，不是日期时间，差异应该在7200秒左右，但要考虑跨越午夜的情况
        assertTrue(difference >= 7199 || difference <= -7199, "偏移2小时后的时间差异应该在7200秒左右");
    }

    @Test
    @DisplayName("测试 tick 时钟")
    void testTickClock() {
        // 使用固定时钟避免时区问题
        Clock fixedClock = Clock.fixed(Instant.parse("2023-08-08T10:15:30.500Z"), ZoneId.of("UTC"));
        Clock tickClock = Clock.tick(fixedClock, Duration.ofSeconds(1));
        
        LocalTime result = LocalTimeUtils.now(tickClock);
        
        assertNotNull(result);
        assertEquals(0, result.getNano());
        
        // 验证tick时钟正确地向下取整到秒
        assertEquals(LocalTime.of(10, 15, 30), result);
    }

    @Test
    @DisplayName("测试时间截断精度")
    void testTimeTruncationPrecision() {
        // 测试带纳秒的时间
        LocalTime originalTime = LocalTime.of(10, 15, 30, 999_999_999);
        
        // 模拟截断操作
        LocalTime result = originalTime.truncatedTo(ChronoUnit.SECONDS);
        
        // 验证纳秒被截断为0
        assertEquals(0, result.getNano());
        assertEquals(10, result.getHour());
        assertEquals(15, result.getMinute());
        assertEquals(30, result.getSecond());
    }

    @Test
    @DisplayName("测试午夜时间")
    void testMidnightTime() {
        Clock midnightClock = Clock.fixed(Instant.parse("2023-08-08T00:00:00.999Z"), ZoneId.of("UTC"));
        LocalTime result = LocalTimeUtils.now(midnightClock);
        
        assertEquals(LocalTime.of(0, 0, 0), result);
        assertEquals(0, result.getNano());
    }

    @Test
    @DisplayName("测试接近午夜的时间")
    void testNearMidnightTime() {
        Clock nearMidnightClock = Clock.fixed(Instant.parse("2023-08-08T23:59:59.999Z"), ZoneId.of("UTC"));
        LocalTime result = LocalTimeUtils.now(nearMidnightClock);
        
        assertEquals(LocalTime.of(23, 59, 59), result);
        assertEquals(0, result.getNano());
    }

    @Test
    @DisplayName("测试空值 ZoneId")
    void testNullZoneId() {
        assertThrows(NullPointerException.class, () -> {
            LocalTimeUtils.now((ZoneId) null);
        });
    }

    @Test
    @DisplayName("测试空值 Clock")
    void testNullClock() {
        assertThrows(NullPointerException.class, () -> {
            LocalTimeUtils.now((Clock) null);
        });
    }

    @Test
    @DisplayName("测试时间的一致性")
    void testTimeConsistency() {
        ZoneId zone = ZoneId.of("Asia/Shanghai");
        Clock clock = Clock.system(zone);
        
        LocalTime timeFromZone = LocalTimeUtils.now(zone);
        LocalTime timeFromClock = LocalTimeUtils.now(clock);
        
        // 验证两种方式获取的时间应该一致（差异在1秒内）
        long difference = Math.abs(ChronoUnit.SECONDS.between(timeFromZone, timeFromClock));
        assertTrue(difference <= 1, "从ZoneId和Clock获取的时间应该一致");
    }

    @Test
    @DisplayName("测试时间的边界值")
    void testTimeBoundaryValues() {
        // 测试一天的开始
        Clock startOfDayClock = Clock.fixed(Instant.parse("2023-08-08T00:00:00.999Z"), ZoneId.of("UTC"));
        LocalTime startOfDay = LocalTimeUtils.now(startOfDayClock);
        assertEquals(LocalTime.of(0, 0, 0), startOfDay);
        
        // 测试一天的结束前
        Clock endOfDayClock = Clock.fixed(Instant.parse("2023-08-08T23:59:59.999Z"), ZoneId.of("UTC"));
        LocalTime endOfDay = LocalTimeUtils.now(endOfDayClock);
        assertEquals(LocalTime.of(23, 59, 59), endOfDay);
        
        // 测试中午
        Clock noonClock = Clock.fixed(Instant.parse("2023-08-08T12:00:00.999Z"), ZoneId.of("UTC"));
        LocalTime noon = LocalTimeUtils.now(noonClock);
        assertEquals(LocalTime.of(12, 0, 0), noon);
    }
}