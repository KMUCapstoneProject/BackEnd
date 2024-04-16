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
@Table(name = "personaltimetable")
public class PersonalTimeTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="timetable_id")
    private Long timetableId; //시퀀스

    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER) //Many = timetable, One = user, 한 계정에 여러 개 timetable
    @JoinColumn(name = "user_id")
    private User user; // FK

    // 일요일(1), 월요일(2), 화요일(3), 수요일(4), 목요일(5), 금요일(6), 토요일(7)
    // 월요일(0), 화요일(1), 수요일(2), 목요일(3), 금요일(4), 토요일(5), 일요일(6)
    // getDayOfWeek()
    private DayOfWeek week;
    private LocalTime starttime;
    private LocalTime endtime;

    private String building;
    private String classNum;

    private String lectureNum;
    private String lectureName;


    @Builder
    public PersonalTimeTable(User user, DayOfWeek week, LocalTime starttime, LocalTime endtime, String building, String classNum, String lectureNum, String lectureName) {
        this.user = user;
        this.week = week;
        this.starttime = starttime;
        this.endtime = endtime;
        this.building = building;
        this.classNum = classNum;
        this.lectureNum = lectureNum;
        this.lectureName = lectureName;
    }
}
