package cn.mindit.atom.core.util;

import java.time.*;
import java.time.temporal.ChronoUnit;

/**
 * @author Catch
 * @since 2022-11-02
 */
public abstract class LocalDateTimeUtils {

    public static LocalDateTime now() {
        return LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }

    public static LocalDateTime now(ZoneId zone) {
        return LocalDateTime.now(zone).truncatedTo(ChronoUnit.SECONDS);
    }

    public static LocalDateTime now(Clock clock) {
        return LocalDateTime.now(clock).truncatedTo(ChronoUnit.SECONDS);
    }

    public static LocalDateTime of(LocalDate date, LocalTime time) {
        return LocalDateTime.of(date, time).truncatedTo(ChronoUnit.SECONDS);
    }

}
