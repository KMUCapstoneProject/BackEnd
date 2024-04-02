package kr.ac.kmu.Capstone.dto.timetable;

import lombok.Getter;

@Getter
public class TimeTableInit {
    private String lectureNum;
    private String lectureName;
    private String init;

    public TimeTableInit(String num, String name, String value) {
        this.lectureNum = num;
        this.lectureName = name;
        this.init = value;
    }
}
