package kr.ac.kmu.Capstone.service;

import kr.ac.kmu.Capstone.dto.timetable.PersonalTimetableSaveDto;
import kr.ac.kmu.Capstone.entity.PersonalTimeTable;
import kr.ac.kmu.Capstone.entity.Posting;
import kr.ac.kmu.Capstone.entity.TimeTable;
import kr.ac.kmu.Capstone.entity.User;
import kr.ac.kmu.Capstone.repository.PersonalTimeTableRepository;
import kr.ac.kmu.Capstone.repository.TimeTableRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PersonalTimeTableService {

    private final PersonalTimeTableRepository personalTimeTableRepository;
    private final TimeTableRepository timeTableRepository;

    // 시간 하나씩 등록
    @Transactional
    public void save(PersonalTimetableSaveDto timetableSaveDto, User user) {
        PersonalTimeTable newTimetable = timetableSaveDto.toEntity(user);
        personalTimeTableRepository.save(newTimetable);
    }

    // 개인이 등록한 시간표 지우는 기능 - personaltimetable
    @Transactional
    public void deletePersonalTimetable(Long timetableId) {
        personalTimeTableRepository.deleteById(timetableId);
    }

    // 학교 시간표에서 불러온 개인의 시간표를 지우는 기능 - timetable
    @Transactional
    public void deleteTimetable(Long timetableId) {
        timeTableRepository.deleteById(timetableId);
    }

    public boolean checkUserinPersonal(Long userId, Long personalTimetableId) {

        PersonalTimeTable temp = personalTimeTableRepository.findById(personalTimetableId)
                .orElseThrow(() -> { // 영속화
                    return new IllegalArgumentException("시간표 찾기 실패 : timetableId를 찾을 수 없습니다.");
                });

        if (userId == temp.getUser().getId()){
            return true;
        }
        return false;
    }

    public boolean checkUserinTimetable(Long userId, Long timetableId) {

        TimeTable temp = timeTableRepository.findById(timetableId)
                .orElseThrow(() -> { // 영속화
                    return new IllegalArgumentException("시간표 찾기 실패 : timetableId를 찾을 수 없습니다.");
                });

        if (userId == temp.getUser().getId()){
            return true;
        }
        return false;
    }

}
