package kr.ac.kmu.Capstone.dto.timetable;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.ac.kmu.Capstone.entity.PersonalTimeTable;
import kr.ac.kmu.Capstone.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;


@Getter
@Setter
@NoArgsConstructor
public class PersonalTimetableSaveDto {

    private DayOfWeek week;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
    private LocalTime starttime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
    private LocalTime endtime;

    private String classNum;
    private String building;

    private String lectureNum;
    private String lectureName;


    public PersonalTimeTable toEntity(User user) {
        return PersonalTimeTable.builder()
                .user(user)
                .week(week)
                .starttime(starttime)
                .endtime(endtime)
                .classNum(classNum)
                .building(building)
                .lectureNum(lectureNum)
                .lectureName(lectureName)
                .build();
    }

}
