package kr.ac.kmu.Capstone.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import kr.ac.kmu.Capstone.config.common.response.ErrorCode;
import kr.ac.kmu.Capstone.dto.notification.AlarmDto;
import kr.ac.kmu.Capstone.dto.timetable.TimeTableResponseDto;
import kr.ac.kmu.Capstone.entity.User;
import kr.ac.kmu.Capstone.repository.AlarmRepository;
import kr.ac.kmu.Capstone.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlarmService {
    private final TimeTableService timeTableService;
    private final UserRepository userRepository;
    private final AlarmRepository alarmRepository;

    public void insertValue(AlarmDto alarmDto) {
        if(alarmDto.getClass() == null) {

        } else {
            alarmRepository.insert(alarmDto.getEmail(), alarmDto.getDeviceToken(), alarmDto.getAlarmChecked());
        }
    }

    // 이 기능은 사용할려면 먼저 로그인 하고나서 찾아와야할듯?
    public void updateValue(AlarmDto alarmDto) {
        if(alarmRepository.selectAlarmCheckedByEmail(alarmDto.getEmail())) { // true 일 경우 알람 off
            alarmRepository.update(alarmDto.getEmail(),false);
        } else {                                                             // false 일 경우, 알람 on
            alarmRepository.update(alarmDto.getEmail(),true);
        }
    }

    public void sendTimetableNotification(AlarmDto alarmDto) throws Exception {
        //loadAllMyTimeTable()
        List<String> users = alarmRepository.selectEmailByAlarmCheck(); // 우선 알람 on인 유저를 먼저 체크해온다

        for (int i = 0; i < users.size(); i++) {
        System.out.println(users.get(i));
           User user = userRepository.findByEmail(users.get(i)) // alarmDto.getEmail() 유저 확인
                   .orElseThrow(() -> new NullPointerException(ErrorCode.ID_NOT_EXIST.getMessage()));

           List<TimeTableResponseDto> myTimeTable = timeTableService.loadAllMyTimeTable(user);

           alarmDto.setEmail(user.getEmail());
           alarmDto.setDeviceToken(alarmRepository.selectDeviceTokenByEmail(user.getEmail()));
           alarmDto.setAlarmChecked(alarmRepository.selectAlarmCheckedByEmail(user.getEmail()));

           StringBuilder textBuilder = new StringBuilder();
           DayOfWeek today = LocalDate.now().getDayOfWeek();
                    // DayOfWeek.MONDAY; << 테스트용 월요일 출력  날짜가 없으니까 출력이 안되더라

               // 오늘의 요일이고, 시작 시간을 기준으로 정렬하여 출력
           myTimeTable.stream()
                    .filter(entry -> entry.getWeek().equals(today))
                    .sorted(Comparator.comparing(TimeTableResponseDto::getStarttime))
                    .forEach(entry -> {
           System.out.println(entry.getLectureName());
           System.out.println(entry.getWeek());
           System.out.println(entry.getStarttime());

           // textBuilder에 추가
           textBuilder.append(entry.getStarttime().toString())
                .append(" ")
                .append(entry.getLectureName())
                .append(" / ");
        });

           // 슬래시 제거
          if (textBuilder.length() > 0) {
               textBuilder.setLength(textBuilder.length() - 2); // 마지막 슬래시 제거
          }

           String text = textBuilder.toString();
            //System.out.println("Filtered text: " + text);

            if(alarmDto.getAlarmChecked()) {
                   Message message = Message.builder()
                       .setToken(alarmDto.getDeviceToken()) // DeviceToken
                       .setNotification(Notification.builder()
                       .setTitle("계명여지도") // title
                       .setBody(text) // (시간 강의명 / 시간 강의명) ~ 식으로
                       .build())
                   .build();

                   String response = FirebaseMessaging.getInstance().send(message);
                   //System.out.println(message);
                   System.out.println("Successfully sent message: " + response);
            }
        }
    }
}