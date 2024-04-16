package kr.ac.kmu.Capstone.service;

import kr.ac.kmu.Capstone.dto.timetable.PersonalTimetableSaveDto;
import kr.ac.kmu.Capstone.entity.PersonalTimeTable;
import kr.ac.kmu.Capstone.entity.User;
import kr.ac.kmu.Capstone.repository.PersonalTimeTableRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PersonalTimeTableService {

    private final PersonalTimeTableRepository personalTimeTableRepository;

    // 시간 하나씩 등록
    @Transactional
    public void save(PersonalTimetableSaveDto timetableSaveDto, User user) {
        PersonalTimeTable newTimetable = timetableSaveDto.toEntity(user);
        personalTimeTableRepository.save(newTimetable);
    }
}
