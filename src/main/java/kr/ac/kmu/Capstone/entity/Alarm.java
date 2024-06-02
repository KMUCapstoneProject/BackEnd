package kr.ac.kmu.Capstone.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "notification") //DB Table 이름
public class Alarm {

    @Id
    @Column (name = "email")
    private String email;

    @Column(name = "deviceToken")
    private String DeviceToken;

    @Column(name = "alarmCheck")
    private boolean isAlarmChecked;

    public String getEmail() {
        return email;
    }

    public Alarm setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getDeviceToken() {
        return DeviceToken;
    }

    public Alarm setDeviceToken(String deviceToken) {
        DeviceToken = deviceToken;
        return this;
    }

    public boolean isAlarmChecked() {
        return isAlarmChecked;
    }

    public Alarm setAlarmChecked(boolean alarmChecked) {
        isAlarmChecked = alarmChecked;
        return this;
    }
}