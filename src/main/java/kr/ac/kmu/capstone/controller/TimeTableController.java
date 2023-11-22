package kr.ac.kmu.Capstone.controller;

import jakarta.validation.Valid;
import kr.ac.kmu.Capstone.dto.timetable.TimetableSaveDto;
import kr.ac.kmu.Capstone.service.TimeTableService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/timetable")
@AllArgsConstructor
public class TimeTableController {

    private TimeTableService timeTableService;


    // 엑셀 업로드
    @GetMapping("/timetable/excelupload")
    public ResponseEntity uploadTimeTableExcel() throws IOException {

        // 메모리 주소로 변경해야함 or db에 url 저장해서 불러오기
        String path = "excel의 url 입력해야함";
        timeTableService.makeTimetableFromExcel(path);

        return new ResponseEntity(HttpStatus.CREATED);
    }


    // 시간 하나씩 저장
    @PostMapping("/add")
    public ResponseEntity save(@Valid @RequestBody TimetableSaveDto timetableSaveDto){
        timeTableService.save(timetableSaveDto);
        return new ResponseEntity(HttpStatus.CREATED);
    }


    // 현재 시간과 비교해서 빈강의실 찾기 (현재 위치 건물에서)
    @GetMapping("/findClassinBuilding")
    public ResponseEntity findClassinBuilding(@RequestParam String building) {

        List<String> emptyRooms = timeTableService.findEmptyClass(building);
        return new ResponseEntity(emptyRooms, HttpStatus.OK);
    }


}
