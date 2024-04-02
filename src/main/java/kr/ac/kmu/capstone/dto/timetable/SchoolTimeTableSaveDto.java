package kr.ac.kmu.Capstone.dto.timetable;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.ac.kmu.Capstone.entity.SchoolTimeTable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
public class SchoolTimeTableSaveDto {

    private DayOfWeek week;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
    private LocalTime starttime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
    private LocalTime endtime;

    private String classNum;
    private String building;

    private String lectureNum;
    private String lectureName;


    public SchoolTimeTable toEntity() {
        return SchoolTimeTable.builder()
                .week(week)
                .starttime(starttime)
                .endtime(endtime)
                .classNum(classNum)
                .building(building)
                .lectureNum(lectureNum)
                .lectureName(lectureName)
                .build();
    }

    @Builder
    public SchoolTimeTableSaveDto(DayOfWeek week, LocalTime starttime, LocalTime endtime, String classNum, String building, String lectureNum, String lectureName) {
        this.week = week;
        this.starttime = starttime;
        this.endtime = endtime;
        this.classNum = classNum;
        this.building = building;
        this.lectureNum = lectureNum;
        this.lectureName = lectureName;
    }
}
