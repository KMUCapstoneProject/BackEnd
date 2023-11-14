package kr.ac.kmu.Capstone.dto.posting;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DateTime {

    int year;
    int month;
    int day;
    int hour;
    int minute;
}
