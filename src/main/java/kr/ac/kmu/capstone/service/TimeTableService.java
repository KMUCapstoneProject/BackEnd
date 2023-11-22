package kr.ac.kmu.Capstone.service;

import kr.ac.kmu.Capstone.dto.timetable.TimeTableMakeDto;
import kr.ac.kmu.Capstone.dto.timetable.TimetableSaveDto;
import kr.ac.kmu.Capstone.entity.TimeTable;
import kr.ac.kmu.Capstone.repository.TimeTableRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

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
    public List<String> findEmptyClass(String building){
        List<TimeTable> classLists = timeTableRepository.findByBuilding(building);

        LocalDateTime current = LocalDateTime.now();
        DayOfWeek currentWeek = current.getDayOfWeek();
        LocalTime currentTime = current.toLocalTime();

        List<String> emptyClassNumLists = new ArrayList<>();

        for (TimeTable classList : classLists) {
            //log.info(String.valueOf(classList.getWeek()));

            // 요일이 동일
            if (classList.getWeek().equals(currentWeek)) {
                // 시간이 start와 end 사이가 아닐 때 (앞이나 뒤)
                //isbefore(앞의시간이 뒤의시간보다 과거인가), isafter(앞의시간이 뒤의시간보다 미래인가)
                if (classList.getStarttime().isAfter(currentTime) && classList.getEndtime().isBefore(currentTime)){
                    emptyClassNumLists.add(classList.getClassNum());
                }
            }
            else
                emptyClassNumLists.add(classList.getClassNum());
        }

        // 중복 제거
        List<String> resultList = emptyClassNumLists.stream().distinct().collect(toList());

        return resultList;
    }

    public void makeTimetableFromExcel(String path) throws IOException {
        List<String> timetables = readExcel(path);
        parseStringList(timetables);

    }



    // 엑셀 데이터 읽어오기
    public List<String> readExcel(String path) throws IOException {

        List<String> timetableList = new ArrayList<>();

        FileInputStream file = new FileInputStream(path); // 파일 읽기
        XSSFWorkbook workbook = new XSSFWorkbook(file); // 엑셀 파일 파싱

        XSSFSheet sheet = workbook.getSheetAt(0); // 엑셀 파일의 첫번째 (0) 시트지
        int rows = sheet.getPhysicalNumberOfRows(); // 행의 수

        for (int r = 1; r < rows-1; r++) { // 마지막에 건수 있음. 그 줄 제외
            XSSFRow row = sheet.getRow(r); // 0 ~ rows

            if (row != null) { // 행이 비어있지 않을 때
                int cells=row.getPhysicalNumberOfCells(); // 열의 수

                for (int c = 0; c < cells; c++) {
                    XSSFCell cell = row.getCell(c); // 0 ~ cell
                    String value = "";

                    // 12반쩨 열이 강의시간(강의실)
                    if (cell == null || c != 12 || cell.getCellType().equals(CellType.BLANK)) { // r열 c행의 cell이 비어있을 때 혹은 시간표 열이 아닐때
                        continue;
                    } else { // 타입별로 내용 읽기
                        switch (cell.getCellType()) {
                            case FORMULA:
                                value = cell.getCellFormula();
                                break;
                            case NUMERIC:
                                value = cell.getNumericCellValue() + "";
                                break;
                            case STRING:
                                value = cell.getStringCellValue() + "";
                                break;
                            case BLANK: // 빈칸에 false 값 들어있는 듯
                                value = cell.getBooleanCellValue() + "";
                                break;
                            case ERROR:
                                value = cell.getErrorCellValue() + "";
                                break;
                        }

                    }
                    timetableList.add(value);
                }
            }
        }

        return timetableList;
    }

    // List<String> 파싱하기
    public void parseStringList(List<String> timetableList) {

        // 데이터 종류
        // 화10:30~11:45 목16:30~17:45(공1302)
        // 화14:30~16:20(공2515) 수16:00~17:50(공2222)
        // 목14:00~17:50(공2415)
        // 화16:00~17:50 목13:00~14:50(공1420-1)
        // " "을 기준으로 split하고, "\\("을 기준으로 다시 split한 데이터를 정규표현식으로 잘라야할 듯

        DayOfWeek week1 = null;
        LocalTime starttime1 = null;
        LocalTime endtime1 = null;
        DayOfWeek week2 = null;
        LocalTime starttime2 = null;
        LocalTime endtime2 = null;
        String building = null;
        String classNum = null;


        for (String timetable : timetableList) {

            // 화10:30~11:45
            // or
            // 목16:30~17:45(공1302)
            String[] timeAndPlace1 = timetable.split(" ");

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
                        building = timeTableMakeDto.getBuilding();
                        classNum = timeTableMakeDto.getClassNum();

                        if (week2 == null) { // 시간정보1, 위치정보1, 하나만 저장

                            TimetableSaveDto saveDto = TimetableSaveDto.builder()
                                    .week(week1)
                                    .starttime(starttime1)
                                    .endtime(endtime1)
                                    .building(building)
                                    .classNum(classNum)
                                    .build();
                            save(saveDto);

                        }
                        else { // 시간정보2, 위치정보1, 2개 저장

                            TimetableSaveDto saveDto1 = TimetableSaveDto.builder()
                                    .week(week1)
                                    .starttime(starttime1)
                                    .endtime(endtime1)
                                    .building(building)
                                    .classNum(classNum)
                                    .build();

                            TimetableSaveDto saveDto2 = TimetableSaveDto.builder()
                                    .week(week2)
                                    .starttime(starttime2)
                                    .endtime(endtime2)
                                    .building(building)
                                    .classNum(classNum)
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
            // 공1, 공2, 공3, 공4, 건7, 쉐, 오...
            // 공이나 건이면 2글자 가져오고 아니면 1글자
            String firstword = String.valueOf(input.charAt(0));
            if (firstword.equals("공") || firstword.equals("건")){
                String building = input.substring(0,2); // 2글자 가져오기
                //System.out.println("building: " + building);
                if (input.contains("-")) {
                    String classNum = input.substring(2,7);

                    return TimeTableMakeDto.builder()
                            .building(building)
                            .classNum(classNum)
                            .build();
                }
                else {
                    String classNum = input.substring(2,5);

                    return TimeTableMakeDto.builder()
                            .building(building)
                            .classNum(classNum)
                            .build();
                }
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
