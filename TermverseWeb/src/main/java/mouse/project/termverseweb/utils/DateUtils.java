package mouse.project.termverseweb.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateUtils {
    public static LocalDateTime timeNowToSeconds() {
        return toSeconds(LocalDateTime.now());
    }

    public static LocalDateTime fromString(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dateString, formatter);
    }

    public static LocalDateTime toSeconds(LocalDateTime dateTime) {
        return dateTime.truncatedTo(ChronoUnit.SECONDS);
    }
}
