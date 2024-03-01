package mouse.project.termverseweb.utils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class DateUtils {
    public static LocalDateTime timeNowToSeconds() {
        return LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }
}
