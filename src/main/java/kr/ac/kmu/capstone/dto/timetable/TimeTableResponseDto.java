package kr.ac.kmu.Capstone.dto.timetable;

import lombok.Getter;

import java.time.LocalTime;

@Getter
public class TimeTableResponseDto {

    String classNum;
    LocalTime nextStartTime;

    public TimeTableResponseDto(String classNum, LocalTime nextStartTime) {
        this.classNum = classNum;
        this.nextStartTime = nextStartTime;
    }
}
