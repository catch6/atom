package cn.mindit.atom.core.util;

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
