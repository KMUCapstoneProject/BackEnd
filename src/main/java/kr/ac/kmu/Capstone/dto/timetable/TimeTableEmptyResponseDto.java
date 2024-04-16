package kr.ac.kmu.Capstone.dto.timetable;

import lombok.Getter;

import java.time.LocalTime;

@Getter
public class TimeTableEmptyResponseDto {

    String classNum;
    LocalTime nextStartTime;

    public TimeTableEmptyResponseDto(String classNum, LocalTime nextStartTime) {
        this.classNum = classNum;
        this.nextStartTime = nextStartTime;
    }
}
