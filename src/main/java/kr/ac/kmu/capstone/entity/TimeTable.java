package kr.ac.kmu.Capstone.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "timetable")
public class TimeTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long timetableId; //시퀀스

    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER) //Many = timetable, One = user, 한 계정에 여러 개 timetable
    @JoinColumn(name = "user_id")
    private User user; // FK

    @ManyToOne(targetEntity = SchoolTimeTable.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "timetable_id")
    private SchoolTimeTable schoolTimeTable; // FK

    @Builder
    public TimeTable(User user, SchoolTimeTable schoolTimeTable) {
        this.user = user;
        this.schoolTimeTable = schoolTimeTable;
    }
}
