package kr.ac.kmu.Capstone.dto.notification;

public class AlarmDto {

    private String email;

    private String DeviceToken;

    private Boolean isAlarmChecked;

    public AlarmDto() {
    }

    public AlarmDto(String email, String deviceToken, Boolean isAlarmChecked) {
        this.email = email;
        DeviceToken = deviceToken;
        this.isAlarmChecked = isAlarmChecked;
    }

    public String getEmail() {
        return email;
    }

    public AlarmDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getDeviceToken() {
        return DeviceToken;
    }

    public AlarmDto setDeviceToken(String deviceToken) {
        DeviceToken = deviceToken;
        return this;
    }

    public Boolean getAlarmChecked() {
        return isAlarmChecked;
    }

    public AlarmDto setAlarmChecked(Boolean alarmChecked) {
        isAlarmChecked = alarmChecked;
        return this;
    }
}