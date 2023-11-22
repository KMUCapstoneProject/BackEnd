package kr.ac.kmu.Capstone.dto.timetable;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.ac.kmu.Capstone.entity.TimeTable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;


@Getter
@Setter
@NoArgsConstructor
public class TimetableSaveDto {

    private DayOfWeek week;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
    private LocalTime starttime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
    private LocalTime endtime;

    private String classNum;

    private String building;

    public TimeTable toEntity() {
        return TimeTable.builder()
                .week(week)
                .starttime(starttime)
                .endtime(endtime)
                .classNum(classNum)
                .building(building)
                .build();
    }

    @Builder
    public TimetableSaveDto(DayOfWeek week, LocalTime starttime, LocalTime endtime, String classNum, String building) {
        this.week = week;
        this.starttime = starttime;
        this.endtime = endtime;
        this.classNum = classNum;
        this.building = building;
    }
}
