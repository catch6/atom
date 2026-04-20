package cn.mindit.atom.test.core.util;

import cn.mindit.atom.core.util.LocalDateTimeUtils;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;

class LocalDateTimeUtilsTest {

    @Test
    void nowIsTruncatedToSeconds() {
        LocalDateTime now = LocalDateTimeUtils.now();
        assertThat(now.getNano()).isZero();
    }

    @Test
    void nowWithZoneIsTruncatedToSeconds() {
        LocalDateTime now = LocalDateTimeUtils.now(ZoneId.of("UTC"));
        assertThat(now.getNano()).isZero();
    }

    @Test
    void nowWithClockUsesClockAndTruncates() {
        Instant fixed = Instant.parse("2024-01-15T10:30:45.999999999Z");
        Clock clock = Clock.fixed(fixed, ZoneId.of("UTC"));
        LocalDateTime now = LocalDateTimeUtils.now(clock);
        assertThat(now).isEqualTo(LocalDateTime.of(2024, 1, 15, 10, 30, 45));
        assertThat(now.getNano()).isZero();
    }

    @Test
    void ofCombinesDateAndTimeAndTruncates() {
        LocalDateTime result = LocalDateTimeUtils.of(
            LocalDate.of(2024, 6, 1),
            LocalTime.of(12, 30, 45, 999_999_999)
        );
        assertThat(result).isEqualTo(LocalDateTime.of(2024, 6, 1, 12, 30, 45));
        assertThat(result.getNano()).isZero();
    }

}
