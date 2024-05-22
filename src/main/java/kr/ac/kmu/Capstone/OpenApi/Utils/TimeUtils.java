package kr.ac.kmu.Capstone.OpenApi.Utils;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeUtils {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HHmmss");

    public static boolean isTimeAfterNow(String time) {
        LocalTime scheduleTime = LocalTime.parse(time, TIME_FORMATTER);
        return scheduleTime.isAfter(LocalTime.now());
    }

    public static LocalTime parseTime(String time) {
        return LocalTime.parse(time, TIME_FORMATTER);
    }

    public static long calculateTravelTime(String depTime, String arrTime) {
        LocalTime departureTime = parseTime(depTime);
        LocalTime arrivalTime = parseTime(arrTime);
        return Duration.between(departureTime, arrivalTime).toMinutes();
    }
}
