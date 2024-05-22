package kr.ac.kmu.Capstone.OpenApi.OpenApiInfo;

public class AreaCode {
    public static final int 세종특별시 =12;
    public static final int 부산광역시 = 21;
    public static final int 대구광역시 = 22;
    public static final int 인천광역시 = 23;
    public static final int 광주광역시 = 24;
    public static final int 대전광역시 = 25;
    public static final int 울산광역시 = 26;
    public static final int 구미시 = 37050;
    public static final int 제주도 = 39;

    public static String getAreaName(Long areaCode) {
        if (areaCode == AreaCode.세종특별시) return "세종특별시";
        else if (areaCode == AreaCode.부산광역시) return "부산광역시";
        else if (areaCode == AreaCode.대구광역시) return "대구광역시";
        else if (areaCode == AreaCode.인천광역시) return "인천광역시";
        else if (areaCode == AreaCode.광주광역시) return "광주광역시";
        else if (areaCode == AreaCode.대전광역시) return "대전광역시";
        else if (areaCode == AreaCode.울산광역시) return "울산광역시";
        else if (areaCode == AreaCode.구미시) return "구미시";
        else if (areaCode == AreaCode.제주도) return "제주도";
        else return "기타";
    }
}
