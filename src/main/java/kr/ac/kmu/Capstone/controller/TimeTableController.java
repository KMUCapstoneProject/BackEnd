package kr.ac.kmu.Capstone.controller;

import jakarta.validation.Valid;
import kr.ac.kmu.Capstone.config.auth.CustomUserDetails;
import kr.ac.kmu.Capstone.dto.timetable.SchoolTimeTableSearchDto;
import kr.ac.kmu.Capstone.dto.timetable.TimeTableEmptyResponseDto;
import kr.ac.kmu.Capstone.dto.timetable.PersonalTimetableSaveDto;
import kr.ac.kmu.Capstone.dto.timetable.TimeTableResponseDto;
import kr.ac.kmu.Capstone.entity.SchoolTimeTable;
import kr.ac.kmu.Capstone.entity.User;
import kr.ac.kmu.Capstone.service.PersonalTimeTableService;
import kr.ac.kmu.Capstone.service.SchoolTimeTableService;
import kr.ac.kmu.Capstone.service.TimeTableService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/timetable")
@AllArgsConstructor
public class TimeTableController {

    private SchoolTimeTableService schoolTimeTableService;
    private PersonalTimeTableService personalTimeTableService;
    private TimeTableService timeTableService;


    // 엑셀 업로드
    @PostMapping("/excelupload")
    public ResponseEntity uploadFile(@RequestParam("file") MultipartFile file) throws IOException {

        schoolTimeTableService.makeTimetableFromExcel(file);

        return new ResponseEntity(HttpStatus.CREATED);
    }


    // 현재 시간과 비교해서 빈강의실 찾기 (현재 위치 건물에서)
    @GetMapping("/findEmptyClassinBuilding")
    public ResponseEntity findEmptyClassinBuilding(@RequestParam String building) {

        List<TimeTableEmptyResponseDto> emptyRooms = schoolTimeTableService.findEmptyClass(building);
        return new ResponseEntity(emptyRooms, HttpStatus.OK);
    }


    // 개인 시간표 입력 기능
    // 시간표 하나씩 저장
    @PostMapping("/addPersonal")
    public ResponseEntity save(@Valid @RequestBody PersonalTimetableSaveDto timetableSaveDto, @AuthenticationPrincipal CustomUserDetails customUserDetails, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            List<FieldError> list = bindingResult.getFieldErrors();
            for(FieldError error : list) {
                return new ResponseEntity<>(error.getDefaultMessage(), HttpStatus.BAD_REQUEST);
            }
        }
        User user = customUserDetails.getUser();
        personalTimeTableService.save(timetableSaveDto, user);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    // 본인 시간표에 학교 시간표 불러와서 저장
    @PostMapping("/addSchool")
    public ResponseEntity save(Long timetableId, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        User user = customUserDetails.getUser();
        timeTableService.add(timetableId, user);
        return new ResponseEntity(HttpStatus.CREATED);
    }


    // 본인 시간표 불러오기
    @PostMapping("/loadbyUser")
    public ResponseEntity myTimeTable(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();
        List<TimeTableResponseDto> myTimeTable = timeTableService.loadAllMyTimeTable(user);
        return new ResponseEntity(myTimeTable, HttpStatus.OK);
    }

    // 본인시간표에서 특정 요일 선택해서 받아오도록
    // 1:월, 2:화, 3:수, 4:목, 5:금, 6:토, 7:일
    @PostMapping("/loadbyUserbyWeek")
    public ResponseEntity myTimeTablebyWeek(Integer week, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();
        List<TimeTableResponseDto> myTimeTable = timeTableService.loadAllMyTimeTable(user);
        List<TimeTableResponseDto> result = timeTableService.myTimeTablebyWeek(myTimeTable, week);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    // 시간표 검색
    @PostMapping("/searchSchool")
    public ResponseEntity search(@Valid @RequestBody SchoolTimeTableSearchDto timetableSearchDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            List<FieldError> list = bindingResult.getFieldErrors();
            for(FieldError error : list) {
                return new ResponseEntity<>(error.getDefaultMessage(), HttpStatus.BAD_REQUEST);
            }
        }
        List<SchoolTimeTable> result = timeTableService.search(timetableSearchDto);
        return new ResponseEntity(result, HttpStatus.CREATED);
    }

    @GetMapping("/personalDelete")
    public ResponseEntity deletePersonalTimetableById(@RequestParam("id") Long timetableId, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();
        if (!personalTimeTableService.checkUserinPersonal(user.getId(), timetableId)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        personalTimeTableService.deletePersonalTimetable(timetableId);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @GetMapping("/schoolDelete")
    public ResponseEntity deleteTimetableById(@RequestParam("id") Long timetableId, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();
        if (!personalTimeTableService.checkUserinTimetable(user.getId(), timetableId)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        personalTimeTableService.deleteTimetable(timetableId);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

}
