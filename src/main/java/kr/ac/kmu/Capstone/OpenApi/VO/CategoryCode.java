package kr.ac.kmu.Capstone.OpenApi.VO;

import java.util.List;
import java.util.stream.Collectors;

public enum CategoryCode {
    upDownTypeCode("상하행", ""),
    dailyTypeCode("요일", ""),
    depTime("출발시간", ""),
    arrtime("도착예정시간", ""),
    arrprevstationcnt("정류장", "");

    private final String name;
    private final String unit;

    CategoryCode(String name, String unit) {
        this.name = name;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }

    public String getDescription() {
        return this.name + (this.unit.isEmpty() ? "" : " (" + this.unit + ")");
    }

    public static String getCodeValue(String name, String value) {
        try {
            CategoryCode c = CategoryCode.valueOf(name);
            if (c == CategoryCode.upDownTypeCode) {
                switch (value) {
                    case "U":
                        return "상행";
                    case "D":
                        return "하행";
                }
            } else if (c == CategoryCode.dailyTypeCode) {
                switch (value) {
                    case "01":
                        return "평일";
                    case "02":
                        return "토요일";
                    case "03":
                        return "일요일";
                }
            } else if (c == CategoryCode.depTime) {
                // HHMISS 형식을 @@시 @@분 @@초 형식으로 변환
                return formatTime(value);
            } else if (c == CategoryCode.arrtime) {
                // 버스 도착 예정시간을 초로 받아 분, 초 단위로 반환
                return formatArrivalTime(value);
            } else if (c == CategoryCode.arrprevstationcnt) {
                // 값 뒤에 "@정류장" 추가
                return value + " 정류장";
            }
        } catch (IllegalArgumentException e) {
            return value;
        }
        return value;
    }

    private static String formatTime(String value) {
        // HHMISS 형식을 @@시 @@분 @@초 형식으로 변환
        if (value.length() != 6) {
            return value;
        }
        String hour = value.substring(0, 2);
        String minute = value.substring(2, 4);
        String second = value.substring(4, 6);
        return hour + "시 " + minute + "분 " + second + "초";
    }

    private static String formatArrivalTime(String value) {
        try {
            int intValue = Integer.parseInt(value);
            if (intValue < 60) {
                return intValue + "초";
            } else {
                int minutes = intValue / 60;
                int seconds = intValue % 60;
                return minutes + "분 " + seconds + "초";
            }
        } catch (NumberFormatException e) {
            return value;
        }
    }

    public static List<String> sortArrivalTimesDescending(List<String> times) {
        return times.stream()
                .sorted((a, b) -> {
                    int timeA = Integer.parseInt(a);
                    int timeB = Integer.parseInt(b);
                    if (timeA < timeB) {
                        return 1; // 내림차순 정렬
                    } else if (timeA > timeB) {
                        return -1;
                    } else {
                        return 0;
                    }
                })
                .collect(Collectors.toList());
    }

    public static String getCodeValueWithUnit(String name, String value) {
        String codeValue = getCodeValue(name, value);
        try {
            CategoryCode c = CategoryCode.valueOf(name);
            if (!c.getUnit().isEmpty()) {
                codeValue += " " + c.getUnit();
            }
        } catch (IllegalArgumentException e) {
            // 열거형 상수가 없는 경우 예외 처리
        }
        return codeValue;
    }
}
