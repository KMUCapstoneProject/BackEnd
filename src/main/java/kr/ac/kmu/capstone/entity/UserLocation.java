package kr.ac.kmu.Capstone.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "user_loc")
public class UserLocation {

    @Id
    @Column(name = "userloc_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private Double latitude; // x
    @Column(nullable = false, length = 500)
    private Double longitude; // y

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user; //FK
}
