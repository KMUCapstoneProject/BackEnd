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
    // getDayOfWeek()
    private DayOfWeek week1;
    private LocalTime starttime1;
    private LocalTime endtime1;

    private DayOfWeek week2;
    private LocalTime starttime2;
    private LocalTime endtime2;

    private int roomNum;

    // entity로 만들어서 불러오기?
    private String building;

    @Builder
    public TimeTable(DayOfWeek week1, LocalTime starttime1, LocalTime endtime1, DayOfWeek week2, LocalTime starttime2, LocalTime endtime2, int roomNum, String building) {
        this.week1 = week1;
        this.starttime1 = starttime1;
        this.endtime1 = endtime1;
        this.week2 = week2;
        this.starttime2 = starttime2;
        this.endtime2 = endtime2;
        this.roomNum = roomNum;
        this.building = building;
    }
}
