package kr.ac.kmu.Capstone.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "subway_time")
public class SubwayTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "arrTime")
    private String arrTime;

    @Column(name = "depTime")
    private String depTime;

    @Column(name = "dailyTypeCode")
    private String dailyTypeCode;

    @Column(name = "endSubwayStationNm")
    private String endSubwayStationNm;

    @Column(name = "subwayStationNm")
    private String subwayStationNm;

    @Column(name = "upDownTypeCode")
    private String upDownTypeCode;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getArrivalTime() {
        return arrTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrTime = arrivalTime;
    }

    public String getDepartureTime() {
        return depTime;
    }

    public void setDepartureTime(String departureTime) {
        this.depTime = departureTime;
    }

    public String getDailyType() {
        return dailyTypeCode;
    }

    public void setDailyType(String dailyType) {
        this.dailyTypeCode = dailyType;
    }

    public String getEndStation() {
        return endSubwayStationNm;
    }

    public void setEndStation(String endStation) {
        this.endSubwayStationNm = endStation;
    }

    public String getStation() {
        return subwayStationNm;
    }

    public void setStation(String station) {
        this.subwayStationNm = station;
    }

    public String getupDownTypeCode() {
        return upDownTypeCode;
    }

    public void setupDownTypeCode(String direction) {
        this.upDownTypeCode = direction;
    }
}
