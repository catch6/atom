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
