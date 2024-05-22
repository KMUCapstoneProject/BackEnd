package kr.ac.kmu.Capstone.OpenApi.Utils;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

public class DateUtils {
    public static String getDailyTypeCode() {
        DayOfWeek dayOfWeek = LocalDateTime.now().getDayOfWeek();
        switch (dayOfWeek) {
            case SATURDAY:
                return "토요일"; // 토요일
            case SUNDAY:
                return "일요일"; // 일요일
            default:
                return "평일"; // 평일
        }
    }
}
