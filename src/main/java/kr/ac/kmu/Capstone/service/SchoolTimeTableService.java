package kr.ac.kmu.Capstone.service;

import kr.ac.kmu.Capstone.dto.timetable.SchoolTimeTableSaveDto;
import kr.ac.kmu.Capstone.dto.timetable.TimeTableEmptyResponseDto;
import kr.ac.kmu.Capstone.dto.timetable.TimeTableInit;
import kr.ac.kmu.Capstone.dto.timetable.TimeTableMakeDto;
import kr.ac.kmu.Capstone.entity.SchoolTimeTable;
import kr.ac.kmu.Capstone.repository.SchoolTimeTableRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
@RequiredArgsConstructor
public class SchoolTimeTableService {

    private final SchoolTimeTableRepository schoolTimeTableRepository;

    // 시간 하나씩 등록
    @Transactional
    public void save(SchoolTimeTableSaveDto timetableSaveDto) {
        SchoolTimeTable newTimetable = timetableSaveDto.toEntity();
        schoolTimeTableRepository.save(newTimetable);
    }


    // 현재시간과 비교해서 빈 강의실 찾기 + 다음 수업 시작 시간
    public List<TimeTableEmptyResponseDto> findEmptyClass(String building){
        List<SchoolTimeTable> classLists = schoolTimeTableRepository.findByBuilding(building);

        LocalDateTime current = LocalDateTime.now();
        DayOfWeek currentWeek = current.getDayOfWeek();
        LocalTime currentTime = current.toLocalTime();

        List<String> emptyClassNumLists = new ArrayList<>();
        List<String> fullClassNumLists = new ArrayList<>();
        List<TimeTableEmptyResponseDto> forFindNextClassTimeList = new ArrayList<>();

        for (SchoolTimeTable classList : classLists) {

            // 요일이 동일
            if (classList.getWeek().equals(currentWeek)) {
                forFindNextClassTimeList.add(new TimeTableEmptyResponseDto(classList.getClassNum(), classList.getStarttime()));
                // 시간이 start와 end 사이가 아닐 때 (앞이나 뒤)
                //isbefore(앞의시간이 뒤의시간보다 과거인가), isafter(앞의시간이 뒤의시간보다 미래인가)
                if (classList.getStarttime().isBefore(currentTime) && classList.getEndtime().isAfter(currentTime)){
                    fullClassNumLists.add(classList.getClassNum());

                }
                else {
                    emptyClassNumLists.add(classList.getClassNum());
                }
            }
            else
                emptyClassNumLists.add(classList.getClassNum());
        }

        // empty에 속한 full 제거
        emptyClassNumLists.removeAll(fullClassNumLists);

        // 중복 제거
        List<String> emptyClassList = emptyClassNumLists.stream().distinct().collect(toList());


        // emptyClassList를 List<TimeTableResponseDto>로 변환
        List<TimeTableEmptyResponseDto> emptyTimeTableResponseList = new ArrayList<>();
        for (String classNum : emptyClassList) {
            // 오늘의 남은 강의가 없거나, 오늘 날짜에는 비어있다면 null로 설정
            LocalTime nextClassStartTime = null;

            // 뒤에 시작하는 강의가 있으면 그 시작 시간을 설정
            // 오늘 강의실
            for (TimeTableEmptyResponseDto timeTableResponseDto : forFindNextClassTimeList) {
                // 강의실 번호 동일
                if (timeTableResponseDto.getClassNum().equals(classNum)) {
                    // 강의실 시간 업데이트
                    if (nextClassStartTime == null || timeTableResponseDto.getNextStartTime().isAfter(LocalTime.now())
                            || timeTableResponseDto.getNextStartTime().isAfter(nextClassStartTime)) {
                        nextClassStartTime = timeTableResponseDto.getNextStartTime();
                    }
                }
            }

            // 오늘 남은 강의가 없을 때
            if (nextClassStartTime != null && nextClassStartTime.isBefore(LocalTime.now()))
                nextClassStartTime = null;


            emptyTimeTableResponseList.add(new TimeTableEmptyResponseDto(classNum, nextClassStartTime));
        }

        return emptyTimeTableResponseList;
    }

    public void makeTimetableFromExcel(MultipartFile file) throws IOException {
        List<TimeTableInit> timetables = readExcel(file);
        parseStringList(timetables);

    }



    // 엑셀 데이터 읽어오기
    public List<TimeTableInit> readExcel(MultipartFile file) throws IOException {

        List<TimeTableInit> timetableList = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            int rows = sheet.getPhysicalNumberOfRows();

            for (int r = 1; r < rows; r++) {
                Row row = sheet.getRow(r);
                if (row != null) {
                    String num = "";
                    String name = "";
                    String value = "";

                    for (int c = 0; c < row.getPhysicalNumberOfCells(); c++) {
                        Cell cell = row.getCell(c);
                        if (cell == null || cell.getCellType().equals(CellType.BLANK))
                            continue;

                        if (c == 2) { // 3번째 열: lecture num -> string
                            switch (cell.getCellType()) {
                                case FORMULA:
                                    num = cell.getCellFormula();
                                    break;
                                case NUMERIC:
                                    num = cell.getNumericCellValue() + "";
                                    break;
                                case STRING:
                                    num = cell.getStringCellValue() + "";
                                    break;
                                case ERROR:
                                    num = cell.getErrorCellValue() + "";
                                    break;
                            }
                        } else if (c == 3) { // 4번째 열: lecture name -> numberic
                            switch (cell.getCellType()) {
                                case FORMULA:
                                    name = cell.getCellFormula();
                                    break;
                                case NUMERIC:
                                    name = cell.getNumericCellValue() + "";
                                    break;
                                case STRING:
                                    name = cell.getStringCellValue() + "";
                                    break;
                                case ERROR:
                                    name = cell.getErrorCellValue() + "";
                                    break;
                            }
                        } else if (c == 13) { // 13번째 열: 타입별로 내용 읽기
                            switch (cell.getCellType()) {
                                case FORMULA:
                                    value = cell.getCellFormula();
                                    break;
                                case NUMERIC:
                                    value = cell.getNumericCellValue() + "";
                                    log.info("1");
                                    break;
                                case STRING:
                                    value = cell.getStringCellValue() + "";
                                    break;
                                case ERROR:
                                    value = cell.getErrorCellValue() + "";
                                    break;
                            }
                        }
                    }

                    if (!value.isBlank()) {
                        if (!value.substring(0, 2).equals("원격")) // 원격강의가 아닐 때
                            timetableList.add(new TimeTableInit(num, name, value));
                    }
                }
            }
        }

        return timetableList;
    }

    // List<String> 파싱하기
    public void parseStringList(List<TimeTableInit> timetableList) {

        // 데이터 종류
        // 화10:30~11:45 목16:30~17:45(공1302)
        // 화14:30~16:20(공2515) 수16:00~17:50(공2222)
        // 목14:00~17:50(공2415)
        // 화16:00~17:50 목13:00~14:50(공1420-1)
        // " "을 기준으로 split하고, "\\("을 기준으로 다시 split한 데이터를 정규표현식으로 잘라야할 듯
        // 수16:00~16:50(채플(중)) <- 에러


        DayOfWeek week1 = null;
        LocalTime starttime1 = null;
        LocalTime endtime1 = null;
        DayOfWeek week2 = null;
        LocalTime starttime2 = null;
        LocalTime endtime2 = null;
        String building = null;
        String classNum = null;

        Map<String, String> changeBuildingValue = new HashMap<>();
        changeBuildingValue.put("공1", "공대1호관");
        changeBuildingValue.put("공2", "공대2호관");
        changeBuildingValue.put("공3", "공대3호관");
        changeBuildingValue.put("공4", "공대4호관");
        changeBuildingValue.put("공5", "공대5호관");
        changeBuildingValue.put("공6", "공대6호관");
        changeBuildingValue.put("건7", "덕래관");
        changeBuildingValue.put("오", "오산관");
        changeBuildingValue.put("쉐", "쉐턱관");
        changeBuildingValue.put("의", "의양관");
        changeBuildingValue.put("스", "스미스관");
        changeBuildingValue.put("영", "영암관");
        changeBuildingValue.put("음", "음악공연예술대학");
        changeBuildingValue.put("음B", "음악공연예술대학B");
        changeBuildingValue.put("사", "봉경관");
        changeBuildingValue.put("체", "체육관");
        changeBuildingValue.put("백", "백은관");
        changeBuildingValue.put("보", "보산관");
        changeBuildingValue.put("국", "동영관");
        changeBuildingValue.put("대", "동천관");
        changeBuildingValue.put("바", "바우어관");
        changeBuildingValue.put("M", "의과대학");
        changeBuildingValue.put("N", "전갑규관");
        changeBuildingValue.put("태", "태권도센터");
        changeBuildingValue.put("학군", "학군단");
        changeBuildingValue.put("감", "감부열관");

        //대명캠
        changeBuildingValue.put("윌", "대명 윌슨관");
        changeBuildingValue.put("수", "대명 수산관");
        changeBuildingValue.put("대쉐", "대명 쉐턱관");
        changeBuildingValue.put("아", "대명 아담스관");
        changeBuildingValue.put("비", "대명 비사관");
        changeBuildingValue.put("문", "대명 동서문화관");
        changeBuildingValue.put("동", "대명 동산관");

        // 아래는 기타로 표현해야 할듯
        changeBuildingValue.put("채", "채플");
        changeBuildingValue.put("외", "외부공간");
        changeBuildingValue.put("미", "미지정");


        for (TimeTableInit timetable : timetableList) {

            // 화10:30~11:45
            // or
            // 목16:30~17:45(공1302)
            String[] timeAndPlace1 = timetable.getInit().split(" ");

            for (String tAndp : timeAndPlace1) {
                // 화10:30~11:45
                // or
                // 목16:30~17:45
                // 공1302)
                String[] timeAndPlace2 = tAndp.split("\\(");

                for (String input : timeAndPlace2) {
                    TimeTableMakeDto timeTableMakeDto = makeTimeTableData(input);

                    if (timeTableMakeDto.getBuilding() == null && week1 == null) { // 요일, 시간 정보, 처음으로 받아오는 정보
                        week1 = timeTableMakeDto.getWeek();
                        starttime1 = timeTableMakeDto.getStarttime();
                        endtime1 = timeTableMakeDto.getEndtime();
                    }
                    else if (timeTableMakeDto.getBuilding() == null && week1 != null) { // 요일, 시간 정보, 두번째로 받아오는 정보
                        week2 = timeTableMakeDto.getWeek();
                        starttime2 = timeTableMakeDto.getStarttime();
                        endtime2 = timeTableMakeDto.getEndtime();
                    }
                    else if(timeTableMakeDto.getWeek() == null) { // 건물, 강의실 정보
                        building = changeBuildingValue.get(timeTableMakeDto.getBuilding());
                        classNum = timeTableMakeDto.getClassNum();

                        if (week2 == null) { // 시간정보1, 위치정보1, 하나만 저장

                            SchoolTimeTableSaveDto saveDto = SchoolTimeTableSaveDto.builder()
                                    .week(week1)
                                    .starttime(starttime1)
                                    .endtime(endtime1)
                                    .building(building)
                                    .classNum(classNum)
                                    .lectureNum(timetable.getLectureNum())
                                    .lectureName(timetable.getLectureName())
                                    .build();
                            save(saveDto);

                        }
                        else { // 시간정보2, 위치정보1, 2개 저장

                            SchoolTimeTableSaveDto saveDto1 = SchoolTimeTableSaveDto.builder()
                                    .week(week1)
                                    .starttime(starttime1)
                                    .endtime(endtime1)
                                    .building(building)
                                    .classNum(classNum)
                                    .lectureNum(timetable.getLectureNum())
                                    .lectureName(timetable.getLectureName())
                                    .build();

                            SchoolTimeTableSaveDto saveDto2 = SchoolTimeTableSaveDto.builder()
                                    .week(week2)
                                    .starttime(starttime2)
                                    .endtime(endtime2)
                                    .building(building)
                                    .classNum(classNum)
                                    .lectureNum(timetable.getLectureNum())
                                    .lectureName(timetable.getLectureName())
                                    .build();

                            save(saveDto1);
                            save(saveDto2);

                        }

                        // 초기화
                        week1 = null;
                        starttime1 = null;
                        endtime1 = null;
                        week2 = null;
                        starttime2 = null;
                        endtime2 = null;
                        building = null;
                        classNum = null;

                    }
                }
            }

        }
    }

    private TimeTableMakeDto makeTimeTableData(String input) {

        Map<String, Integer> dayofWeekValue = new HashMap<>();
        dayofWeekValue.put("월", 1);
        dayofWeekValue.put("화", 2);
        dayofWeekValue.put("수", 3);
        dayofWeekValue.put("목", 4);
        dayofWeekValue.put("금", 5);
        dayofWeekValue.put("토", 6);
        dayofWeekValue.put("일", 7);

        // 요일과 시간을 나타내는 정규표현식
        String dayTimeRegex = "(?<dayOfWeek>[월화수목금토일]+)(?<startTime>\\d{2}:\\d{2})~(?<endTime>\\d{2}:\\d{2})";

        // 입력 문자열이 요일과 시간을 나타내는 패턴에 일치하는지 확인
        Pattern dayTimePattern = Pattern.compile(dayTimeRegex);
        Matcher dayTimeMatcher = dayTimePattern.matcher(input);

        if (dayTimeMatcher.matches()) {
            // DayofWeek 형식의 요일 출력
            // 월화수목금토일 -> 숫자로
            String kr_dayofWeek = dayTimeMatcher.group("dayOfWeek");
            Integer num_dayofWeek = dayofWeekValue.get(kr_dayofWeek);
            DayOfWeek dayOfWeek = DayOfWeek.of(num_dayofWeek);
            log.info(dayOfWeek.name());

            // LocalTime 형식의 시작 시간 출력
            String startTimeStr = dayTimeMatcher.group("startTime");
            LocalTime startTime = LocalTime.parse(startTimeStr);

            // LocalTime 형식의 끝 시간 출력
            String endTimeStr = dayTimeMatcher.group("endTime");
            LocalTime endTime = LocalTime.parse(endTimeStr);

            return TimeTableMakeDto.builder()
                    .week(dayOfWeek)
                    .starttime(startTime)
                    .endtime(endTime)
                    .build();

        }
        else {
            String firstword = String.valueOf(input.charAt(0));
            String secondword = String.valueOf(input.charAt(1));
            // 채플, 외부공간
            if (firstword.equals("채") || firstword.equals("외") || firstword.equals("미")) {
                String building = firstword;
                log.info(input);
                return TimeTableMakeDto.builder()
                        .building(building)
                        .build();
            }
            else if (firstword.equals("공") || firstword.equals("건") || (firstword.equals("음")&&secondword.equals("B")) ){
                // 공1, 공2, 공3, 공4, 건7, 쉐, 오...
                // 공이나 건, 대쉐이면 2글자 가져오고 아니면 1글자
                String building = input.substring(0,2); // 2글자 가져오기
                //System.out.println("building: " + building);
                if (input.contains("-")) {
                    String classNum = input.substring(1,7); // 앞에 건물번호 붙여서

                    return TimeTableMakeDto.builder()
                            .building(building)
                            .classNum(classNum)
                            .build();
                }
                else {
                    String classNum = input.substring(1,5);

                    return TimeTableMakeDto.builder()
                            .building(building)
                            .classNum(classNum)
                            .build();
                }
            }
            else if ((firstword.equals("대")&&secondword.equals("쉐")) || (firstword.equals("학")&&secondword.equals("군"))) {
                String building = input.substring(0,2); // 2글자 가져오기
                String classNum = input.substring(2,5);

                return TimeTableMakeDto.builder()
                        .building(building)
                        .classNum(classNum)
                        .build();
            }
            else { // 쉐, 오,,
                String building = firstword; // 1글자 가져오기
                if (input.contains("-")) {
                    String classNum = input.substring(1,6);

                    return TimeTableMakeDto.builder()
                            .building(building)
                            .classNum(classNum)
                            .build();
                }
                else {
                    if (secondword.equals(")")) {
                        //String building = "채플(중)";
                        return TimeTableMakeDto.builder().build();
                    }

                    String classNum = input.substring(1,4);

                    return TimeTableMakeDto.builder()
                            .building(building)
                            .classNum(classNum)
                            .build();
                }
            }
        }

    }


}
