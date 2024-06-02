package kr.ac.kmu.Capstone.controller;

import kr.ac.kmu.Capstone.dto.notification.AlarmDto;
import kr.ac.kmu.Capstone.service.AlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-service/alarm")
public class AlarmController {
    @Autowired
    private AlarmService alarmService;

    // 첫 알람 설정
    @PostMapping("/send")
    public void PostSendNotification(
            @RequestParam String email,
            @RequestParam String deviceToken,
            @RequestParam boolean isChecked
    ) { // Param 값으로, email, deviceToken, 알람 동의 여부 받기
       AlarmDto alarmDto = new AlarmDto();
       alarmDto.setEmail(email);
       alarmDto.setDeviceToken(deviceToken);
       alarmDto.setAlarmChecked(isChecked);

       alarmService.insertValue(alarmDto);
    }

    //알람 on / off 기능
    @PostMapping("/update")
    public void PostUpdateAlarmCheck(
            @RequestParam String email,
            @RequestParam boolean isChecked) {

       AlarmDto alarmDto = new AlarmDto();
        alarmDto.setEmail(email);
        alarmDto.setAlarmChecked(isChecked);

       alarmService.updateValue(alarmDto);
    }

    //스케줄러 함수 만들 예정 -> (월-금) 아침 8시 알람이 자동으로 옴  일단은? 주석처리 해두고 굴리는게 맞을듯?
    //기능적인 부분은 /test 와 동일 할 예정

    //@Scheduled(cron = "0 0 8 * * MON-FRI")
    //public void sendMessage() {
    //    try {
    //        for (String token : tokenList) {
    //        AlarmDto alarmDto = new AlarmDto();
    //        alarmService.sendTimetableNotification(alarmDto);
    //        }
    //    } catch (Exception e) {
    //        e.printStackTrace();
    //    }
    //}

    @PostMapping("/test")
    public void PostTestMessage() {
       try {
               AlarmDto alarmDto = new AlarmDto();
               alarmService.sendTimetableNotification(alarmDto);
           } catch (Exception e) {
               e.printStackTrace();
           }
    }
}