package kr.ac.kmu.Capstone.service;

import kr.ac.kmu.Capstone.dto.timetable.TimetableSaveDto;
import kr.ac.kmu.Capstone.entity.TimeTable;
import kr.ac.kmu.Capstone.repository.TimeTableRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TimeTableService {

    private final TimeTableRepository timeTableRepository;

    // 시간 하나씩 등록
    @Transactional
    public void save(TimetableSaveDto timetableSaveDto) {
        TimeTable newTimetable = timetableSaveDto.toEntity();
        timeTableRepository.save(newTimetable);
    }

    // 현재시간과 비교해서 빈 강의실 찾기
    public List<Integer> findEmptyRoom(String building){
        List<TimeTable> roomLists = timeTableRepository.findByBuilding(building);

        LocalDateTime current = LocalDateTime.now();
        DayOfWeek currentWeek = current.getDayOfWeek();
        LocalTime currentTime = current.toLocalTime();

        List<Integer> emptyRoomNumLists = new ArrayList<>();

        for (TimeTable roomList : roomLists) {

            // 요일이 동일
            if (roomList.getWeek1().equals(currentWeek)) {
                // 시간이 start와 end 사이가 아닐 때 (앞이나 뒤)
                //isbefore(앞의시간이 뒤의시간보다 과거인가), isafter(앞의시간이 뒤의시간보다 미래인가)
                if (roomList.getStarttime1().isAfter(currentTime) && roomList.getEndtime1().isBefore(currentTime)){
                    emptyRoomNumLists.add(roomList.getRoomNum());
                }
            }
            else if (roomList.getWeek2().equals(currentWeek)) {
                // 시간이 start와 end 사이가 아닐 때 (앞이나 뒤)
                //isbefore(앞의시간이 뒤의시간보다 과거인가), isafter(앞의시간이 뒤의시간보다 미래인가)
                if (roomList.getStarttime2().isAfter(currentTime) && roomList.getEndtime2().isBefore(currentTime)){
                    emptyRoomNumLists.add(roomList.getRoomNum());
                }
            }
            else
                emptyRoomNumLists.add(roomList.getRoomNum());
        }

        return emptyRoomNumLists;
    }


}
