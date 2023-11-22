package kr.ac.kmu.Capstone.dto.timetable;

import lombok.Builder;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@Builder
public class TimeTableMakeDto {

    private DayOfWeek week;
    private LocalTime starttime;
    private LocalTime endtime;

    private String building;
    private String classNum;
}
