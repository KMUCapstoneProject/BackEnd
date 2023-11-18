package kr.ac.kmu.Capstone.dto.timetable;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.ac.kmu.Capstone.entity.TimeTable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
public class TimetableSaveDto {

    private DayOfWeek week1;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss", timezone = "Asia/Seoul")
    private LocalTime starttime1;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss", timezone = "Asia/Seoul")
    private LocalTime endtime1;


    private DayOfWeek week2;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss", timezone = "Asia/Seoul")
    private LocalTime starttime2;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss", timezone = "Asia/Seoul")
    private LocalTime endtime2;


    private int roomNum;

    private String building;

    public TimeTable toEntity() {
        return TimeTable.builder()
                .week1(week1)
                .starttime1(starttime1)
                .endtime1(endtime1)
                .week2(week2)
                .starttime2(starttime2)
                .endtime2(endtime2)
                .roomNum(roomNum)
                .building(building)
                .build();
    }
}
