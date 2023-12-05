package kr.ac.kmu.Capstone.controller;

import jakarta.validation.Valid;
import kr.ac.kmu.Capstone.dto.timetable.TimeTableResponseDto;
import kr.ac.kmu.Capstone.dto.timetable.TimetableSaveDto;
import kr.ac.kmu.Capstone.service.TimeTableService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/timetable")
@AllArgsConstructor
public class TimeTableController {

    private TimeTableService timeTableService;


    // 엑셀 업로드
    @PostMapping("/excelupload")
    public ResponseEntity uploadFile(@RequestParam("file") MultipartFile file) throws IOException {

        timeTableService.makeTimetableFromExcel(file);
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

        List<TimeTableResponseDto> emptyRooms = timeTableService.findEmptyClass(building);
        return new ResponseEntity(emptyRooms, HttpStatus.OK);
    }


}
