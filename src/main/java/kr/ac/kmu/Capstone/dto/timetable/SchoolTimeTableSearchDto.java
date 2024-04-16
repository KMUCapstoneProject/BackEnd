package kr.ac.kmu.Capstone.dto.timetable;

import lombok.Getter;

@Getter
public class SchoolTimeTableSearchDto {

    String keyword;
    int status; // 0이면 강좌번호 검색, 1이면 과목명 검색
}
