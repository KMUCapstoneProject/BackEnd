package kr.ac.kmu.Capstone.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "timetable")
public class TimeTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="timetable_id")
    private Long timetableId; //시퀀스

    // 일요일(1), 월요일(2), 화요일(3), 수요일(4), 목요일(5), 금요일(6), 토요일(7)
    // 월요일(0), 화요일(1), 수요일(2), 목요일(3), 금요일(4), 토요일(5), 일요일(6)
    // getDayOfWeek()
    private DayOfWeek week;
    private LocalTime starttime;
    private LocalTime endtime;

    // entity로 만들어서 불러오기?
    private String building;
    private String classNum;


    @Builder
    public TimeTable(DayOfWeek week, LocalTime starttime, LocalTime endtime, String classNum, String building) {
        this.week = week;
        this.starttime = starttime;
        this.endtime = endtime;
        this.classNum = classNum;
        this.building = building;
    }
}
