package kr.ac.kmu.Capstone.service;

import kr.ac.kmu.Capstone.dto.timetable.SchoolTimeTableSearchDto;
import kr.ac.kmu.Capstone.dto.timetable.TimeTableResponseDto;
import kr.ac.kmu.Capstone.entity.PersonalTimeTable;
import kr.ac.kmu.Capstone.entity.SchoolTimeTable;
import kr.ac.kmu.Capstone.entity.TimeTable;
import kr.ac.kmu.Capstone.entity.User;
import kr.ac.kmu.Capstone.repository.PersonalTimeTableRepository;
import kr.ac.kmu.Capstone.repository.SchoolTimeTableRepository;
import kr.ac.kmu.Capstone.repository.TimeTableRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TimeTableService {

    private final TimeTableRepository timeTableRepository;
    private final SchoolTimeTableRepository schoolTimeTableRepository;
    private final PersonalTimeTableRepository personalTimeTableRepository;


    // TimeTable에 저장된 저장한 학교 시간표 + PersonalTimeTable에 저장된 본인의 시간표
    // 모두 출력
    public List<TimeTableResponseDto> loadAllMyTimeTable(User user) {
        List<TimeTableResponseDto> results = new ArrayList<>();

        // 학교 시간표
        List<TimeTable> mySchoolSearched = timeTableRepository.findByUser(user);
        for (TimeTable timeTable : mySchoolSearched) {
            TimeTableResponseDto timeTableResponseDto = TimeTableResponseDto.builder()
                    .week(timeTable.getSchoolTimeTable().getWeek())
                    .starttime(timeTable.getSchoolTimeTable().getStarttime())
                    .endtime(timeTable.getSchoolTimeTable().getEndtime())
                    .building(timeTable.getSchoolTimeTable().getBuilding())
                    .classNum(timeTable.getSchoolTimeTable().getClassNum())
                    .lectureName(timeTable.getSchoolTimeTable().getLectureName())
                    .lectureNum(timeTable.getSchoolTimeTable().getLectureNum())
                    .build();
            results.add(timeTableResponseDto);
        }

        // 개인 시간표
        List<PersonalTimeTable> myPersonalSearched = personalTimeTableRepository.findByUser(user);
        for (PersonalTimeTable personalTimeTable : myPersonalSearched) {
            TimeTableResponseDto timeTableResponseDto = TimeTableResponseDto.builder()
                    .week(personalTimeTable.getWeek())
                    .starttime(personalTimeTable.getStarttime())
                    .endtime(personalTimeTable.getEndtime())
                    .building(personalTimeTable.getBuilding())
                    .classNum(personalTimeTable.getClassNum())
                    .lectureName(personalTimeTable.getLectureName())
                    .lectureNum(personalTimeTable.getLectureNum())
                    .build();

            results.add(timeTableResponseDto);
        }

        return results;
    }


    // TimeTable에 학교 시간표 불러와서 저장
    public void add(Long timetableId, User user) {
        Optional<SchoolTimeTable> timeTables = schoolTimeTableRepository.findByTimetableId(timetableId);
        SchoolTimeTable timeTable = timeTables.get();

        TimeTable newTimeTable = new TimeTable(user, timeTable);
        timeTableRepository.save(newTimeTable);
    }


    // schooltimetable에 있는 시간표 검색
    // 검색옵션 2개 -> 과목코드 검색, 강좌명 검색
    public List<SchoolTimeTable> search(SchoolTimeTableSearchDto schoolTimeTableSearchDto) {
        String keyword = schoolTimeTableSearchDto.getKeyword();
        int status = schoolTimeTableSearchDto.getStatus();

        List<SchoolTimeTable> searched = new ArrayList<>();
        if(status == 0) { // 강좌번호 검색
            searched = schoolTimeTableRepository.findByLectureNumContaining(keyword);
        } else if (status == 1) { // 과목명 검색
            searched = schoolTimeTableRepository.findByLectureNameContaining(keyword);
        }
        return searched;
    }

    // mytimetable 안에서 특정 요일만
    public List<TimeTableResponseDto> myTimeTablebyWeek(List<TimeTableResponseDto> mytimetable, Integer week) {

        List<TimeTableResponseDto> result = new ArrayList<>();

        for (TimeTableResponseDto myt : mytimetable) {
            if (myt.getWeek().equals(DayOfWeek.of(week))) {
                result.add(myt);
            }
        }
        return  result;
    }

}
